/** 
 * ServiceHttpClients.java
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

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Factory for the shared, configured, thread-safe OkHttpClient instances used to invoke remote 
 * name services.  All clients share a single connection pool and dispatcher (so connections are
 * reused with keep-alive), have explicit connect, read, and write timeouts, and each has its own 
 * {@link RequestThrottle} limiting the rate of requests to its service.  
 * See {@link ServiceClientConfig} for the settings used.
 * 
 * @author mole
 */
public final class ServiceHttpClients {
	
	private static volatile OkHttpClient baseClient;
	
	private ServiceHttpClients() { } 
	
	/**
	 * @return the shared base OkHttpClient, with timeouts from {@link ServiceClientConfig}, 
	 *   created on first use.
	 */
	public static OkHttpClient baseClient() { 
		OkHttpClient result = baseClient;
		if (result==null) { 
			synchronized (ServiceHttpClients.class) { 
				result = baseClient;
				if (result==null) { 
					result = new OkHttpClient.Builder()
							.connectTimeout(ServiceClientConfig.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS)
							.readTimeout(ServiceClientConfig.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)
							.writeTimeout(ServiceClientConfig.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS)
							.retryOnConnectionFailure(true)
							.build();
					baseClient = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Create an OkHttpClient sharing the connection pool and configuration of the base client, 
	 * with a new {@link RequestThrottle} configured from {@link ServiceClientConfig}.  Intended to 
	 * be called once per remote service, with the result shared by all callers of that service.
	 * 
	 * @return a new throttled OkHttpClient.
	 */
	public static OkHttpClient newThrottledClient() { 
		return newThrottledClient(new RequestThrottle(ServiceClientConfig.getMaxConcurrentRequests(), 
				ServiceClientConfig.getMinRequestIntervalMillis(), ServiceClientConfig.getAcquireTimeoutMillis()));
	}
	
	/**
	 * Create an OkHttpClient sharing the connection pool and configuration of the base client, 
	 * using the provided throttle.
	 * 
	 * @param throttle the throttle to apply to requests made with the client.
	 * @return a new throttled OkHttpClient.
	 */
	public static OkHttpClient newThrottledClient(RequestThrottle throttle) { 
		return baseClient().newBuilder().addInterceptor(throttle).build();
	}

}
