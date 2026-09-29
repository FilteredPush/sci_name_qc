/** 
 * RequestThrottle.java
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

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp interceptor that limits the rate at which requests are made to a remote service, 
 * so that many concurrent callers do not flood the service with requests.  Limits the number 
 * of concurrent in-flight requests, and enforces a minimum interval between the start of 
 * successive requests.  The response body is read while the permit is held, so the limit 
 * covers the whole exchange.
 * 
 * <p>Also records the URL of the last request made on each thread, so that failures can be 
 * reported with the URL that failed.</p>
 * 
 * @author mole
 */
public class RequestThrottle implements Interceptor {
	
	private static final ThreadLocal<String> LAST_REQUEST_URL = new ThreadLocal<String>();
	
	private final Semaphore permits;
	private final int maxConcurrentRequests;
	private final long minIntervalNanos;
	private long nextStartNanos;  // guarded by this
	
	/**
	 * Constructor.
	 * 
	 * @param maxConcurrentRequests maximum number of concurrent in-flight requests, at least 1.
	 * @param minIntervalMillis minimum interval between the start of successive requests, 0 for none.
	 */
	public RequestThrottle(int maxConcurrentRequests, long minIntervalMillis) { 
		this.maxConcurrentRequests = Math.max(1, maxConcurrentRequests);
		this.permits = new Semaphore(this.maxConcurrentRequests, true);
		this.minIntervalNanos = TimeUnit.MILLISECONDS.toNanos(Math.max(0L, minIntervalMillis));
		this.nextStartNanos = System.nanoTime();
	}
	
	/**
	 * @return the URL of the last request made through any RequestThrottle on the current thread, 
	 *   or null if none (or cleared).
	 */
	public static String lastRequestUrl() { 
		return LAST_REQUEST_URL.get();
	}
	
	/**
	 * Clear the record of the last request URL made on the current thread.
	 */
	public static void clearLastRequestUrl() { 
		LAST_REQUEST_URL.remove();
	}
	
	/**
	 * @return the maximum number of concurrent requests allowed by this throttle.
	 */
	public int getMaxConcurrentRequests() { 
		return maxConcurrentRequests;
	}
	
	/**
	 * @return the number of requests that could currently start without waiting for a permit.
	 */
	public int availablePermits() { 
		return permits.availablePermits();
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		LAST_REQUEST_URL.set(request.url().toString());
		try { 
			permits.acquire();
		} catch (InterruptedException e) { 
			Thread.currentThread().interrupt();
			throw new InterruptedIOException("Interrupted while waiting to make request to " + request.url());
		}
		try { 
			waitForStartSlot(request);
			Response response = chain.proceed(request);
			ResponseBody body = response.body();
			if (body==null) { 
				return response;
			}
			// buffer the (small) response so that the permit covers reading the body
			byte[] bytes = body.bytes();
			return response.newBuilder().body(ResponseBody.create(bytes, body.contentType())).build();
		} finally { 
			permits.release();
		}
	}
	
	private void waitForStartSlot(Request request) throws InterruptedIOException { 
		if (minIntervalNanos <= 0L) { 
			return;
		}
		long waitNanos;
		synchronized (this) { 
			long now = System.nanoTime();
			long start = (nextStartNanos - now > 0L) ? nextStartNanos : now;
			nextStartNanos = start + minIntervalNanos;
			waitNanos = start - now;
		}
		if (waitNanos > 0L) { 
			try { 
				TimeUnit.NANOSECONDS.sleep(waitNanos);
			} catch (InterruptedException e) { 
				Thread.currentThread().interrupt();
				throw new InterruptedIOException("Interrupted while waiting to make request to " + request.url());
			}
		}
	}

}
