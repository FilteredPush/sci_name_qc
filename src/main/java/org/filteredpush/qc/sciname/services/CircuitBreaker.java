/** 
 * CircuitBreaker.java
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Circuit breaker for calls to a remote service, shared by all callers of that service.  
 * 
 * <p>After {@link ServiceClientConfig#getCircuitBreakerThreshold()} consecutive failed calls, 
 * the circuit breaker trips (opens), and calls fail with a {@link ServiceUnavailableException} 
 * without being sent for {@link ServiceClientConfig#getCircuitBreakerOpenMillis()}.  After that 
 * period, a single trial call is allowed (other calls continue to fail without being sent), if 
 * the trial call succeeds the circuit breaker closes and calls resume, if it fails the circuit 
 * breaker trips again.</p>
 * 
 * <p>A failed call is one that failed after any retries with a plausibly transient failure 
 * (a connection failure, or HTTP 408, 429, 5xx).  A response with another HTTP error status 
 * (e.g. 404) shows that the service is responding, so counts as a success.</p>
 * 
 * @author mole
 */
public class CircuitBreaker {
	
	private static final Log logger = LogFactory.getLog(CircuitBreaker.class);
	
	private static final Map<String,CircuitBreaker> BREAKERS = new ConcurrentHashMap<String,CircuitBreaker>();
	
	private final String serviceName;
	/** Consecutive failed calls, guarded by this. */
	private int consecutiveFailures;
	/** Time until which the circuit breaker is open, 0 if closed, guarded by this. */
	private long openUntilNanos;
	/** True while a trial call is being made after the open period, guarded by this. */
	private boolean trialInProgress;
	
	/**
	 * Constructor, use {@link #forService(String)} to obtain the circuit breaker shared by 
	 * all callers of a service.
	 * 
	 * @param serviceName name of the service, used in messages.
	 */
	CircuitBreaker(String serviceName) { 
		this.serviceName = serviceName;
	}
	
	/**
	 * Obtain the circuit breaker for a service, shared by all callers of that service.
	 * 
	 * @param serviceName the name of the service, e.g. WoRMS.
	 * @return the circuit breaker for the service.
	 */
	public static CircuitBreaker forService(String serviceName) { 
		return BREAKERS.computeIfAbsent(serviceName, CircuitBreaker::new);
	}
	
	/**
	 * Close all circuit breakers and reset their failure counts.
	 */
	public static void resetAll() { 
		for (CircuitBreaker breaker : BREAKERS.values()) { 
			breaker.reset();
		}
	}
	
	/**
	 * Check whether a call may be made, to be called before making a call.  If this returns 
	 * without throwing, the caller must report the outcome of the call with {@link #recordSuccess()}, 
	 * {@link #recordFailure()}, or {@link #release(boolean)}.
	 * 
	 * @param subject description of what the call is for, used in the exception message.
	 * @return true if the call is a trial call after the circuit breaker has been open.
	 * @throws ServiceUnavailableException if the circuit breaker is open, or a trial call is in progress.
	 */
	public synchronized boolean check(String subject) throws ServiceUnavailableException { 
		if (openUntilNanos==0L) { 
			return false;
		}
		String about = (subject==null) ? "" : " for [" + subject + "]";
		long remaining = openUntilNanos - System.nanoTime();
		if (remaining > 0L) { 
			throw new ServiceUnavailableException(serviceName + " unavailable after " + consecutiveFailures 
					+ " consecutive failed calls, not sending requests for another " 
					+ TimeUnit.NANOSECONDS.toMillis(remaining) + " ms, call" + about + " not made");
		}
		if (trialInProgress) { 
			throw new ServiceUnavailableException(serviceName + " unavailable after " + consecutiveFailures 
					+ " consecutive failed calls, waiting for the result of a trial call, call" + about + " not made");
		}
		trialInProgress = true;
		return true;
	}
	
	/**
	 * Record a call that succeeded (or that the service responded to), closing the circuit breaker.
	 */
	public synchronized void recordSuccess() { 
		if (openUntilNanos!=0L) { 
			logger.info(serviceName + " responding again, resuming calls");
		}
		consecutiveFailures = 0;
		openUntilNanos = 0L;
		trialInProgress = false;
	}
	
	/**
	 * Record a failed call, tripping the circuit breaker if the threshold of consecutive failures is reached.
	 */
	public synchronized void recordFailure() { 
		consecutiveFailures++;
		trialInProgress = false;
		int threshold = ServiceClientConfig.getCircuitBreakerThreshold();
		if (threshold > 0 && consecutiveFailures >= threshold) { 
			long openMillis = ServiceClientConfig.getCircuitBreakerOpenMillis();
			openUntilNanos = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(openMillis);
			if (openUntilNanos==0L) { 
				openUntilNanos = 1L;
			}
			logger.error(serviceName + " unavailable after " + consecutiveFailures 
					+ " consecutive failed calls, not sending requests for " + openMillis + " ms");
		}
	}
	
	/**
	 * Record the end of a call whose outcome says nothing about whether the service is 
	 * available (e.g. it was interrupted, or not sent), releasing a trial call if this was one.
	 * 
	 * @param trial the value returned by {@link #check(String)} for the call.
	 */
	public synchronized void release(boolean trial) { 
		if (trial) { 
			trialInProgress = false;
		}
	}
	
	/**
	 * @return true if the circuit breaker is open, so that calls will fail without being sent.
	 */
	public synchronized boolean isOpen() { 
		return openUntilNanos!=0L && openUntilNanos - System.nanoTime() > 0L;
	}
	
	/**
	 * @return the number of consecutive failed calls.
	 */
	public synchronized int getConsecutiveFailures() { 
		return consecutiveFailures;
	}
	
	/**
	 * Close the circuit breaker and reset the count of consecutive failures.
	 */
	public synchronized void reset() { 
		consecutiveFailures = 0;
		openUntilNanos = 0L;
		trialInProgress = false;
	}

}
