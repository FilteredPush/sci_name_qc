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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;

/**
 * Simple thread-safe, size bounded, least recently used cache for the results of lookups 
 * against remote services, so that repeated lookups of the same value are not repeatedly 
 * sent to the service.  Null values (e.g. lookups that found no match) may be cached.
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
	
	private final IntSupplier maxSize;
	private final LinkedHashMap<K,Entry<V>> map = new LinkedHashMap<K,Entry<V>>(16, 0.75f, true);
	private final AtomicLong hits = new AtomicLong();
	private final AtomicLong misses = new AtomicLong();
	
	/**
	 * Create a cache whose maximum size is taken from {@link ServiceClientConfig#getCacheSize()}.
	 */
	public LookupCache() { 
		this(ServiceClientConfig::getCacheSize);
	}
	
	/**
	 * Create a cache with a maximum size that may change over time.
	 * 
	 * @param maxSize supplier of the maximum number of entries, a value of 0 or less disables the cache.
	 */
	public LookupCache(IntSupplier maxSize) { 
		this.maxSize = maxSize;
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
	 * Remove all entries from the cache and reset the hit and miss counts.
	 */
	public void clear() { 
		synchronized (map) { 
			map.clear();
		}
		hits.set(0L);
		misses.set(0L);
	}
	
	/** @return the number of lookups that found a cached entry. */
	public long getHitCount() { return hits.get(); }
	
	/** @return the number of lookups that did not find a cached entry. */
	public long getMissCount() { return misses.get(); }

}
