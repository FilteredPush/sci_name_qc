/** 
 * LookupCache.java
 * 
 * Copyright 2026 President and Fellows of Harvard College
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.filteredpush.qc.sciname.services;

import java.io.InterruptedIOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * Simple thread-safe, size bounded, least recently used cache for the results of lookups 
 * against remote services, so that repeated lookups of the same value are not repeatedly 
 * sent to the service.  Null values (e.g. lookups that found no match) may be cached.
 * 
 * <p>{@link #getOrLoad(Object, Loader)} also remembers lookups that failed, for 
 * {@link ServiceClientConfig#getFailureCacheMillis()}, during which the same lookup fails without 
 * being resent, and makes only one call at a time for a key, concurrent callers for the same key 
 * wait for and share the result of that call.</p>
 * 
 * @param <K> the type of the keys.
 * @param <V> the type of the cached values.
 * 
 * @author mole
 */
public class LookupCache<K,V> {
	
	/**
	 * A cache entry, wrapping a value which may be null.
	 * 
	 * @param <V> the type of the value.
	 */
	public static final class Entry<V> { 
		private final V value;
		Entry(V value) { this.value = value; }
		/** @return the cached value, may be null. */
		public V getValue() { return value; }
	}
	
	/**
	 * A lookup against a remote service, invoked by {@link LookupCache#getOrLoad(Object, Loader)}.
	 * 
	 * @param <V> the type of the value looked up.
	 */
	public interface Loader<V> { 
		/**
		 * Perform the lookup.
		 * 
		 * @return the value found, may be null (e.g. for no match).
		 * @throws ServiceException on failure to perform the lookup.
		 */
		V load() throws ServiceException;
	}
	
	/**
	 * A remembered failed lookup.
	 */
	private static final class Failure { 
		private final ServiceException exception;
		private final long expiresNanos;
		Failure(ServiceException exception, long expiresNanos) { 
			this.exception = exception;
			this.expiresNanos = expiresNanos;
		}
	}
	
	private final IntSupplier maxSize;
	private final LongSupplier failureMillis;
	/** Recently failed lookups, keyed as for the cache. */
	private final Map<K,Failure> failures = new ConcurrentHashMap<K,Failure>();
	/** Lookups in progress, keyed as for the cache. */
	private final Map<K,CompletableFuture<Entry<V>>> inFlight = new ConcurrentHashMap<K,CompletableFuture<Entry<V>>>();
	private final LinkedHashMap<K,Entry<V>> map = new LinkedHashMap<K,Entry<V>>(16, 0.75f, true);
	private final AtomicLong hits = new AtomicLong();
	private final AtomicLong misses = new AtomicLong();
	
	/**
	 * Create a cache whose maximum size is taken from {@link ServiceClientConfig#getCacheSize()}, and 
	 * which remembers failures for {@link ServiceClientConfig#getFailureCacheMillis()}.
	 */
	public LookupCache() { 
		this(ServiceClientConfig::getCacheSize, ServiceClientConfig::getFailureCacheMillis);
	}
	
	/**
	 * Create a cache with a maximum size that may change over time, which remembers failures 
	 * for {@link ServiceClientConfig#getFailureCacheMillis()}.
	 * 
	 * @param maxSize supplier of the maximum number of entries, a value of 0 or less disables the cache.
	 */
	public LookupCache(IntSupplier maxSize) { 
		this(maxSize, ServiceClientConfig::getFailureCacheMillis);
	}
	
	/**
	 * Create a cache with a maximum size and failure memory period that may change over time.
	 * 
	 * @param maxSize supplier of the maximum number of entries, a value of 0 or less disables the cache.
	 * @param failureMillis supplier of how long, in milliseconds, failed lookups are remembered, 
	 *   0 or less to not remember failures.
	 */
	public LookupCache(IntSupplier maxSize, LongSupplier failureMillis) { 
		this.maxSize = maxSize;
		this.failureMillis = failureMillis;
	}
	
	/**
	 * Obtain the value for a key from the cache, or, if it is not cached, by performing the lookup 
	 * and caching its result.  
	 * 
	 * <ul>
	 * <li>If the lookup for this key failed within the failure memory period, fails with a 
	 * {@link ServiceUnavailableException} without performing the lookup.</li>
	 * <li>If a lookup for this key is already in progress on another thread, waits for it and 
	 * returns its result (or fails with its failure), rather than performing another lookup.</li>
	 * <li>If the lookup fails, the failure is remembered (unless the lookup was not made, see 
	 * {@link ServiceUnavailableException}, or was interrupted), and rethrown.</li>
	 * </ul>
	 * 
	 * <p>The same value instance is returned to all callers for a key, callers that may modify 
	 * the value should copy it.</p>
	 * 
	 * @param key the key to look up.
	 * @param loader the lookup to perform if the key is not cached.
	 * @return the cached or looked up value, may be null.
	 * @throws ServiceUnavailableException if the lookup for this key failed recently.
	 * @throws ServiceException if the lookup failed.
	 */
	public V getOrLoad(K key, Loader<V> loader) throws ServiceException { 
		Entry<V> cached = get(key);
		if (cached!=null) { 
			return cached.getValue();
		}
		Failure failure = failures.get(key);
		if (failure!=null) { 
			long remaining = failure.expiresNanos - System.nanoTime();
			if (remaining > 0L) { 
				throw new ServiceUnavailableException("Lookup failed recently, not resending for another " 
						+ TimeUnit.NANOSECONDS.toMillis(remaining) + " ms: " + failure.exception.getMessage(), 
						failure.exception.getHttpStatusCode(), failure.exception);
			}
			failures.remove(key, failure);
		}
		CompletableFuture<Entry<V>> mine = new CompletableFuture<Entry<V>>();
		CompletableFuture<Entry<V>> existing = inFlight.putIfAbsent(key, mine);
		if (existing!=null) { 
			return await(existing);
		}
		try { 
			V value = loader.load();
			put(key, value);
			mine.complete(new Entry<V>(value));
			return value;
		} catch (ServiceException e) { 
			remember(key, e);
			mine.completeExceptionally(e);
			throw e;
		} catch (RuntimeException e) { 
			mine.completeExceptionally(e);
			throw e;
		} finally { 
			inFlight.remove(key, mine);
		}
	}
	
	/**
	 * Remember a failed lookup, unless it was not made, or was interrupted.
	 */
	private void remember(K key, ServiceException e) { 
		long millis = failureMillis.getAsLong();
		if (millis <= 0L || e instanceof ServiceUnavailableException || isInterruption(e)) { 
			return;
		}
		failures.put(key, new Failure(e, System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(millis)));
	}
	
	private static boolean isInterruption(Throwable t) { 
		while (t!=null) { 
			if (t instanceof InterruptedException || 
					(t instanceof InterruptedIOException && !(t instanceof java.net.SocketTimeoutException))) { 
				return true;
			}
			t = t.getCause();
		}
		return false;
	}
	
	private V await(CompletableFuture<Entry<V>> future) throws ServiceException { 
		try { 
			return future.get().getValue();
		} catch (InterruptedException e) { 
			Thread.currentThread().interrupt();
			throw new ServiceException("Interrupted while waiting for a lookup in progress on another thread", e);
		} catch (ExecutionException e) { 
			Throwable cause = e.getCause();
			if (cause instanceof ServiceException) { 
				throw (ServiceException) cause;
			}
			if (cause instanceof RuntimeException) { 
				throw (RuntimeException) cause;
			}
			throw new ServiceException(cause==null ? null : cause.getMessage(), cause);
		}
	}
	
	/**
	 * @return the number of failed lookups currently remembered (including any that have expired 
	 *   but have not yet been removed).
	 */
	public int getFailureCount() { 
		return failures.size();
	}
	
	/**
	 * Look up a key in the cache.
	 * 
	 * @param key the key to look up.
	 * @return the cache entry for the key, or null if the key is not in the cache (or caching is disabled).
	 */
	public Entry<V> get(K key) { 
		if (!isEnabled()) { 
			return null;
		}
		Entry<V> result;
		synchronized (map) { 
			result = map.get(key);
		}
		if (result==null) { 
			misses.incrementAndGet();
		} else { 
			hits.incrementAndGet();
		}
		return result;
	}
	
	/**
	 * Add a value to the cache, evicting the least recently used entries if the cache is full.
	 * 
	 * @param key the key.
	 * @param value the value, may be null.
	 */
	public void put(K key, V value) { 
		int max = maxSize.getAsInt();
		if (max <= 0) { 
			return;
		}
		synchronized (map) { 
			map.put(key, new Entry<V>(value));
			Iterator<K> i = map.keySet().iterator();
			while (map.size() > max && i.hasNext()) { 
				i.next();
				i.remove();
			}
		}
	}
	
	/**
	 * @return true if caching is enabled (maximum size greater than 0).
	 */
	public boolean isEnabled() { 
		return maxSize.getAsInt() > 0;
	}
	
	/**
	 * @return the number of entries in the cache.
	 */
	public int size() { 
		synchronized (map) { 
			return map.size();
		}
	}
	
	/**
	 * Remove all entries and remembered failures from the cache and reset the hit and miss counts.
	 */
	public void clear() { 
		synchronized (map) { 
			map.clear();
		}
		failures.clear();
		hits.set(0L);
		misses.set(0L);
	}
	
	/** @return the number of lookups that found a cached entry. */
	public long getHitCount() { return hits.get(); }
	
	/** @return the number of lookups that did not find a cached entry. */
	public long getMissCount() { return misses.get(); }

}
