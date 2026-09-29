/** 
 * ServiceRetrier.java
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

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonParseException;

/**
 * Invokes a call to a remote service, retrying (iteratively, with exponential backoff and jitter, 
 * honoring Retry-After) on plausibly transient failures, up to {@link ServiceClientConfig#getMaxRetries()} 
 * times, and failing immediately on non-transient failures.  Failures are logged and reported 
 * with a diagnostic description (see {@link ApiFailure}).  Retry state is local to each invocation, 
 * so this is thread-safe.
 * 
 * @author mole
 */
public final class ServiceRetrier {
	
	private static final Log logger = LogFactory.getLog(ServiceRetrier.class);
	
	/**
	 * A call to a remote service.
	 * 
	 * @param <T> the type returned by the call.
	 */
	public interface ServiceCall<T> { 
		/**
		 * Invoke the service.
		 * @return the result.
		 * @throws Exception on failure, typically an ApiException from a generated client.
		 */
		T call() throws Exception;
	}
	
	private ServiceRetrier() { } 
	
	/**
	 * Invoke a call to a remote service with bounded retries.
	 * 
	 * @param <T> the type returned by the call.
	 * @param serviceName the name of the service, used in messages, e.g. WoRMS.
	 * @param subject description of what is being looked up, used in log messages, e.g. a scientific name.
	 * @param call the call to make.
	 * @return the result of the call.
	 * @throws ServiceException if the call failed with a non-transient failure, or failed on every attempt, 
	 *   with a non-empty message describing the failure, and the HTTP status code where applicable.
	 */
	public static <T> T execute(String serviceName, String subject, ServiceCall<T> call) throws ServiceException { 
		int maxRetries = ServiceClientConfig.getMaxRetries();
		String about = (subject==null) ? "" : " for [" + subject + "]";
		for (int attempt = 0; ; attempt++) { 
			ApiFailure failure;
			RequestThrottle.clearLastRequestUrl();
			try { 
				return call.call();
			} catch (JsonParseException e) { 
				failure = ApiFailure.from(serviceName, e);
			} catch (RuntimeException e) { 
				throw e;
			} catch (Exception e) { 
				failure = ApiFailure.from(serviceName, e);
			}
			String description = failure.describe();
			String attempts = " (attempt " + (attempt+1) + " of " + (maxRetries+1) + ")";
			if (!failure.isRetryable()) { 
				if (failure.getKind()==ApiFailure.Kind.HTTP || failure.getKind()==ApiFailure.Kind.TRANSPORT) { 
					logger.error("Not retrying" + about + ": " + description);
				} else { 
					logger.error("Not retrying" + about + ": " + description, failure.getCause());
				}
				throw failure.toServiceException(null);
			}
			if (attempt >= maxRetries) { 
				logger.error("Giving up" + about + attempts + ": " + description);
				throw failure.toServiceException("After " + (attempt+1) + " attempts: ");
			}
			long delay = RetryPolicy.delayBeforeRetryMillis(attempt, failure.getRetryAfterMillis(), ThreadLocalRandom.current().nextDouble());
			if (delay < 0L) { 
				logger.error("Not retrying" + about + ", Retry-After exceeds maximum wait of " 
						+ ServiceClientConfig.getMaxRetryAfterMillis() + " ms: " + description);
				throw failure.toServiceException(null);
			}
			logger.warn("Failed" + about + attempts + ", retrying in " + delay + " ms: " + description);
			try { 
				Thread.sleep(delay);
			} catch (InterruptedException e) { 
				Thread.currentThread().interrupt();
				throw new ServiceException("Interrupted while waiting to retry: " + description, 
						failure.getKind()==ApiFailure.Kind.HTTP ? failure.getHttpStatusCode() : 0, e);
			}
		}
	}

}
