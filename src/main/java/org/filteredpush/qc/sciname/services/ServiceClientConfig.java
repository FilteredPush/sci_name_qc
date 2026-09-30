/** 
 * ServiceClientConfig.java
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

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configuration for the HTTP clients used to invoke remote name services (WoRMS, IRMNG).
 * 
 * <p>Each setting may be provided as a java system property (e.g. 
 * <code>-Dsci_name_qc.maxRetries=5</code>), or set programmatically with the static setters
 * on this class.  The following settings are available:</p>
 * <table>
 * <caption>Configuration settings</caption>
 * <tr><th>System property</th><th>Default</th><th>Meaning</th></tr>
 * <tr><td>sci_name_qc.userAgent</td><td>FilteredPush-sci_name_qc/{version} (+https://github.com/FilteredPush/sci_name_qc)</td>
 *     <td>User-Agent header sent with each request.</td></tr>
 * <tr><td>sci_name_qc.maxRetries</td><td>3</td><td>Maximum number of retries after a transient failure 
 *     (HTTP 408, 429, 500, 502, 503, 504, or a connection level failure), total attempts are maxRetries+1.</td></tr>
 * <tr><td>sci_name_qc.backoffBaseMillis</td><td>500</td><td>Base delay for exponential backoff between retries.</td></tr>
 * <tr><td>sci_name_qc.backoffMaxMillis</td><td>8000</td><td>Maximum delay for exponential backoff between retries.</td></tr>
 * <tr><td>sci_name_qc.maxRetryAfterMillis</td><td>30000</td><td>Longest Retry-After delay requested by a service 
 *     that will be honored, if a service asks for a longer wait, the request fails without retrying.</td></tr>
 * <tr><td>sci_name_qc.maxConcurrentRequests</td><td>2</td><td>Maximum number of concurrent in-flight requests to each service.</td></tr>
 * <tr><td>sci_name_qc.minRequestIntervalMillis</td><td>100</td><td>Minimum interval between the start of successive requests 
 *     to each service, 0 for no minimum.</td></tr>
 * <tr><td>sci_name_qc.connectTimeoutMillis</td><td>10000</td><td>HTTP connect timeout.</td></tr>
 * <tr><td>sci_name_qc.readTimeoutMillis</td><td>30000</td><td>HTTP read timeout.</td></tr>
 * <tr><td>sci_name_qc.writeTimeoutMillis</td><td>30000</td><td>HTTP write timeout.</td></tr>
 * <tr><td>sci_name_qc.cacheSize</td><td>10000</td><td>Maximum number of entries in each lookup result cache, 0 disables caching.</td></tr>
 * <tr><td>sci_name_qc.acquireTimeoutMillis</td><td>60000</td><td>Longest wait for a turn to make a request to a service, 
 *     after which the request fails without being sent.</td></tr>
 * <tr><td>sci_name_qc.failureCacheMillis</td><td>60000</td><td>How long a failed lookup is remembered, during which the 
 *     same lookup fails without being resent, 0 to not remember failures.</td></tr>
 * <tr><td>sci_name_qc.circuitBreakerThreshold</td><td>5</td><td>Number of consecutive failed calls to a service after which 
 *     calls fail without being sent, 0 to disable the circuit breaker.</td></tr>
 * <tr><td>sci_name_qc.circuitBreakerOpenMillis</td><td>60000</td><td>How long calls fail without being sent once the circuit 
 *     breaker for a service has tripped, after which a single trial call is allowed.</td></tr>
 * </table>
 * 
 * <p>The retry, backoff, cache size, failure cache, and circuit breaker settings take effect immediately.  
 * The User-Agent, timeout, concurrency, acquire timeout, and request interval settings are read when the shared HTTP client for a service is first 
 * created (on the first request to that service), so they should be set before any lookups are made.</p>
 * 
 * @author mole
 */
public final class ServiceClientConfig {
	
	private static final Log logger = LogFactory.getLog(ServiceClientConfig.class);
	
	public static final String USER_AGENT_PROPERTY = "sci_name_qc.userAgent";
	public static final String MAX_RETRIES_PROPERTY = "sci_name_qc.maxRetries";
	public static final String BACKOFF_BASE_PROPERTY = "sci_name_qc.backoffBaseMillis";
	public static final String BACKOFF_MAX_PROPERTY = "sci_name_qc.backoffMaxMillis";
	public static final String MAX_RETRY_AFTER_PROPERTY = "sci_name_qc.maxRetryAfterMillis";
	public static final String MAX_CONCURRENT_REQUESTS_PROPERTY = "sci_name_qc.maxConcurrentRequests";
	public static final String MIN_REQUEST_INTERVAL_PROPERTY = "sci_name_qc.minRequestIntervalMillis";
	public static final String CONNECT_TIMEOUT_PROPERTY = "sci_name_qc.connectTimeoutMillis";
	public static final String READ_TIMEOUT_PROPERTY = "sci_name_qc.readTimeoutMillis";
	public static final String WRITE_TIMEOUT_PROPERTY = "sci_name_qc.writeTimeoutMillis";
	public static final String CACHE_SIZE_PROPERTY = "sci_name_qc.cacheSize";
	public static final String ACQUIRE_TIMEOUT_PROPERTY = "sci_name_qc.acquireTimeoutMillis";
	public static final String FAILURE_CACHE_PROPERTY = "sci_name_qc.failureCacheMillis";
	public static final String CIRCUIT_BREAKER_THRESHOLD_PROPERTY = "sci_name_qc.circuitBreakerThreshold";
	public static final String CIRCUIT_BREAKER_OPEN_PROPERTY = "sci_name_qc.circuitBreakerOpenMillis";
	
	public static final int DEFAULT_MAX_RETRIES = 3;
	public static final long DEFAULT_BACKOFF_BASE_MILLIS = 500L;
	public static final long DEFAULT_BACKOFF_MAX_MILLIS = 8000L;
	public static final long DEFAULT_MAX_RETRY_AFTER_MILLIS = 30000L;
	public static final int DEFAULT_MAX_CONCURRENT_REQUESTS = 2;
	public static final long DEFAULT_MIN_REQUEST_INTERVAL_MILLIS = 100L;
	public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 10000L;
	public static final long DEFAULT_READ_TIMEOUT_MILLIS = 30000L;
	public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 30000L;
	public static final int DEFAULT_CACHE_SIZE = 10000;
	public static final long DEFAULT_ACQUIRE_TIMEOUT_MILLIS = 60000L;
	public static final long DEFAULT_FAILURE_CACHE_MILLIS = 60000L;
	public static final int DEFAULT_CIRCUIT_BREAKER_THRESHOLD = 5;
	public static final long DEFAULT_CIRCUIT_BREAKER_OPEN_MILLIS = 60000L;
	
	/** Project URL included in the default User-Agent. */
	public static final String PROJECT_URL = "https://github.com/FilteredPush/sci_name_qc";
	
	private static final String VERSION = loadVersion();
	
	private static volatile String userAgent;
	private static volatile int maxRetries;
	private static volatile long backoffBaseMillis;
	private static volatile long backoffMaxMillis;
	private static volatile long maxRetryAfterMillis;
	private static volatile int maxConcurrentRequests;
	private static volatile long minRequestIntervalMillis;
	private static volatile long connectTimeoutMillis;
	private static volatile long readTimeoutMillis;
	private static volatile long writeTimeoutMillis;
	private static volatile int cacheSize;
	private static volatile long acquireTimeoutMillis;
	private static volatile long failureCacheMillis;
	private static volatile int circuitBreakerThreshold;
	private static volatile long circuitBreakerOpenMillis;
	
	static { 
		resetToDefaults();
	}
	
	private ServiceClientConfig() { }
	
	/**
	 * Reset all settings to their defaults, as overridden by any system properties.
	 */
	public static synchronized void resetToDefaults() { 
		String ua = System.getProperty(USER_AGENT_PROPERTY);
		userAgent = (ua==null || ua.trim().length()==0) ? getDefaultUserAgent() : ua.trim();
		maxRetries = (int) readLong(MAX_RETRIES_PROPERTY, DEFAULT_MAX_RETRIES, 0);
		backoffBaseMillis = readLong(BACKOFF_BASE_PROPERTY, DEFAULT_BACKOFF_BASE_MILLIS, 0);
		backoffMaxMillis = readLong(BACKOFF_MAX_PROPERTY, DEFAULT_BACKOFF_MAX_MILLIS, 0);
		maxRetryAfterMillis = readLong(MAX_RETRY_AFTER_PROPERTY, DEFAULT_MAX_RETRY_AFTER_MILLIS, 0);
		maxConcurrentRequests = (int) readLong(MAX_CONCURRENT_REQUESTS_PROPERTY, DEFAULT_MAX_CONCURRENT_REQUESTS, 1);
		minRequestIntervalMillis = readLong(MIN_REQUEST_INTERVAL_PROPERTY, DEFAULT_MIN_REQUEST_INTERVAL_MILLIS, 0);
		connectTimeoutMillis = readLong(CONNECT_TIMEOUT_PROPERTY, DEFAULT_CONNECT_TIMEOUT_MILLIS, 0);
		readTimeoutMillis = readLong(READ_TIMEOUT_PROPERTY, DEFAULT_READ_TIMEOUT_MILLIS, 0);
		writeTimeoutMillis = readLong(WRITE_TIMEOUT_PROPERTY, DEFAULT_WRITE_TIMEOUT_MILLIS, 0);
		cacheSize = (int) readLong(CACHE_SIZE_PROPERTY, DEFAULT_CACHE_SIZE, 0);
		acquireTimeoutMillis = readLong(ACQUIRE_TIMEOUT_PROPERTY, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, 1);
		failureCacheMillis = readLong(FAILURE_CACHE_PROPERTY, DEFAULT_FAILURE_CACHE_MILLIS, 0);
		circuitBreakerThreshold = (int) readLong(CIRCUIT_BREAKER_THRESHOLD_PROPERTY, DEFAULT_CIRCUIT_BREAKER_THRESHOLD, 0);
		circuitBreakerOpenMillis = readLong(CIRCUIT_BREAKER_OPEN_PROPERTY, DEFAULT_CIRCUIT_BREAKER_OPEN_MILLIS, 0);
	}
	
	private static long readLong(String property, long defaultValue, long minimum) { 
		String value = System.getProperty(property);
		if (value==null || value.trim().length()==0) { 
			return defaultValue;
		}
		try { 
			long result = Long.parseLong(value.trim());
			if (result < minimum) { 
				logger.warn("Value " + result + " for " + property + " is less than " + minimum + ", using " + minimum);
				result = minimum;
			}
			return result;
		} catch (NumberFormatException e) { 
			logger.warn("Unable to parse value [" + value + "] for " + property + ", using default " + defaultValue);
			return defaultValue;
		}
	}
	
	private static String loadVersion() { 
		String version = null;
		try (InputStream in = ServiceClientConfig.class.getResourceAsStream("/sci_name_qc-version.properties")) { 
			if (in!=null) { 
				Properties properties = new Properties();
				properties.load(in);
				version = properties.getProperty("version");
			}
		} catch (Exception e) { 
			logger.debug("Unable to read sci_name_qc version: " + e.getMessage());
		}
		if (version==null || version.trim().length()==0 || version.contains("${")) { 
			// not filtered by maven, e.g. running from an IDE
			version = ServiceClientConfig.class.getPackage()==null ? null : ServiceClientConfig.class.getPackage().getImplementationVersion();
		}
		if (version==null || version.trim().length()==0) { 
			version = "unknown";
		}
		return version.trim();
	}
	
	/**
	 * @return the version of this library, as provided by the maven build, or "unknown".
	 */
	public static String getVersion() { 
		return VERSION;
	}
	
	/**
	 * @return the default User-Agent, identifying this library and its project URL.
	 */
	public static String getDefaultUserAgent() { 
		return "FilteredPush-sci_name_qc/" + VERSION + " (+" + PROJECT_URL + ")";
	}

	public static String getUserAgent() { return userAgent; }
	public static void setUserAgent(String userAgent) { 
		ServiceClientConfig.userAgent = (userAgent==null || userAgent.trim().length()==0) ? getDefaultUserAgent() : userAgent.trim();
	}

	public static int getMaxRetries() { return maxRetries; }
	public static void setMaxRetries(int maxRetries) { ServiceClientConfig.maxRetries = Math.max(0, maxRetries); }

	public static long getBackoffBaseMillis() { return backoffBaseMillis; }
	public static void setBackoffBaseMillis(long millis) { ServiceClientConfig.backoffBaseMillis = Math.max(0L, millis); }

	public static long getBackoffMaxMillis() { return backoffMaxMillis; }
	public static void setBackoffMaxMillis(long millis) { ServiceClientConfig.backoffMaxMillis = Math.max(0L, millis); }

	public static long getMaxRetryAfterMillis() { return maxRetryAfterMillis; }
	public static void setMaxRetryAfterMillis(long millis) { ServiceClientConfig.maxRetryAfterMillis = Math.max(0L, millis); }

	public static int getMaxConcurrentRequests() { return maxConcurrentRequests; }
	public static void setMaxConcurrentRequests(int max) { ServiceClientConfig.maxConcurrentRequests = Math.max(1, max); }

	public static long getMinRequestIntervalMillis() { return minRequestIntervalMillis; }
	public static void setMinRequestIntervalMillis(long millis) { ServiceClientConfig.minRequestIntervalMillis = Math.max(0L, millis); }

	public static long getConnectTimeoutMillis() { return connectTimeoutMillis; }
	public static void setConnectTimeoutMillis(long millis) { ServiceClientConfig.connectTimeoutMillis = Math.max(0L, millis); }

	public static long getReadTimeoutMillis() { return readTimeoutMillis; }
	public static void setReadTimeoutMillis(long millis) { ServiceClientConfig.readTimeoutMillis = Math.max(0L, millis); }

	public static long getWriteTimeoutMillis() { return writeTimeoutMillis; }
	public static void setWriteTimeoutMillis(long millis) { ServiceClientConfig.writeTimeoutMillis = Math.max(0L, millis); }

	public static int getCacheSize() { return cacheSize; }
	/**
	 * @param size maximum number of entries in each lookup cache, 0 disables caching.
	 */
	public static void setCacheSize(int size) { ServiceClientConfig.cacheSize = Math.max(0, size); }
	
	/** @return the longest wait, in milliseconds, for a turn to make a request to a service. */
	public static long getAcquireTimeoutMillis() { return acquireTimeoutMillis; }
	/**
	 * @param millis the longest wait, in milliseconds, for a turn to make a request to a service, at least 1,
	 *   read when the shared HTTP client for a service is created.
	 */
	public static void setAcquireTimeoutMillis(long millis) { ServiceClientConfig.acquireTimeoutMillis = Math.max(1L, millis); }

	/** @return how long, in milliseconds, a failed lookup is remembered. */
	public static long getFailureCacheMillis() { return failureCacheMillis; }
	/**
	 * @param millis how long, in milliseconds, a failed lookup is remembered, 0 to not remember failures.
	 */
	public static void setFailureCacheMillis(long millis) { ServiceClientConfig.failureCacheMillis = Math.max(0L, millis); }

	/** @return the number of consecutive failed calls that trips the circuit breaker for a service. */
	public static int getCircuitBreakerThreshold() { return circuitBreakerThreshold; }
	/**
	 * @param threshold the number of consecutive failed calls that trips the circuit breaker for a service, 
	 *   0 to disable the circuit breaker.
	 */
	public static void setCircuitBreakerThreshold(int threshold) { ServiceClientConfig.circuitBreakerThreshold = Math.max(0, threshold); }

	/** @return how long, in milliseconds, calls fail without being sent once the circuit breaker has tripped. */
	public static long getCircuitBreakerOpenMillis() { return circuitBreakerOpenMillis; }
	/**
	 * @param millis how long, in milliseconds, calls fail without being sent once the circuit breaker has tripped.
	 */
	public static void setCircuitBreakerOpenMillis(long millis) { ServiceClientConfig.circuitBreakerOpenMillis = Math.max(0L, millis); }

}
