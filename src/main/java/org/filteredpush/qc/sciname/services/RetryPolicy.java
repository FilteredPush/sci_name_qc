/** 
 * RetryPolicy.java
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Decisions about whether and when to retry a failed request to a remote service.
 * 
 * <p>Only failures that are plausibly transient are retried: HTTP 408, 429, 500, 502, 503, 504, and 
 * transport level failures (an {@link IOException} such as a connection refused, timeout, or 
 * unknown host).  Other HTTP error statuses (e.g. 400, 401, 403, 404) are not retried.</p>
 * 
 * <p>Delays between retries use exponential backoff with jitter, and honor a Retry-After 
 * header provided by the service.</p>
 * 
 * @author mole
 */
public final class RetryPolicy {
	
	/** Value returned by retry after parsing methods when no usable Retry-After is present. */
	public static final long NO_RETRY_AFTER = -1L;
	
	private RetryPolicy() { } 
	
	/**
	 * Is an HTTP status code one which indicates a plausibly transient failure worth retrying.
	 * 
	 * @param httpStatusCode the HTTP status code.
	 * @return true for 408, 429, 500, 502, 503, and 504, otherwise false.
	 */
	public static boolean isRetryableStatus(int httpStatusCode) { 
		switch (httpStatusCode) { 
		case 408:
		case 429:
		case 500:
		case 502:
		case 503:
		case 504:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Is a failure worth retrying.
	 * 
	 * @param httpStatusCode the HTTP status code, 0 if none.
	 * @param cause the underlying cause of the failure, may be null.
	 * @return true if the cause is a transport level failure ({@link IOException}), or if 
	 *   the status code is retryable.
	 */
	public static boolean isRetryable(int httpStatusCode, Throwable cause) { 
		if (cause instanceof IOException) { 
			return true;
		}
		return isRetryableStatus(httpStatusCode);
	}
	
	/**
	 * Compute an exponential backoff delay with jitter.  The exponential delay for an attempt is
	 * baseMillis * 2^attempt, capped at maxMillis, the returned delay is between half of that 
	 * delay and that delay, depending on the jitter value.
	 * 
	 * @param attempt zero based index of the retry (0 for the first retry).
	 * @param baseMillis the base delay in milliseconds.
	 * @param maxMillis the maximum delay in milliseconds.
	 * @param jitter a value in the range [0,1), typically random.
	 * @return the delay in milliseconds before making the retry.
	 */
	public static long computeBackoffMillis(int attempt, long baseMillis, long maxMillis, double jitter) { 
		if (baseMillis <= 0L || maxMillis <= 0L) { 
			return 0L;
		}
		int shift = Math.max(0, Math.min(attempt, 30));
		long exponential = baseMillis << shift;
		if (exponential <= 0L || exponential > maxMillis || (exponential >> shift) != baseMillis) { 
			// capped, or overflowed
			exponential = maxMillis;
		}
		double boundedJitter = Math.max(0d, Math.min(jitter, 1d));
		long half = exponential / 2L;
		return half + (long) ((exponential - half) * boundedJitter);
	}
	
	/**
	 * Parse the value of a Retry-After header, which may be either a number of seconds or an HTTP date.
	 * 
	 * @param value the value of the Retry-After header.
	 * @param nowMillis the current time in milliseconds since the epoch.
	 * @return the number of milliseconds to wait, or {@link #NO_RETRY_AFTER} if value is null or 
	 *   not parsable.
	 */
	public static long parseRetryAfterMillis(String value, long nowMillis) { 
		if (value==null || value.trim().length()==0) { 
			return NO_RETRY_AFTER;
		}
		String trimmed = value.trim();
		if (trimmed.matches("^[0-9]+$")) { 
			try { 
				long seconds = Long.parseLong(trimmed);
				if (seconds > Long.MAX_VALUE / 1000L) { 
					return Long.MAX_VALUE;
				}
				return seconds * 1000L;
			} catch (NumberFormatException e) {
				return Long.MAX_VALUE;
			}
		}
		try { 
			ZonedDateTime date = ZonedDateTime.parse(trimmed, DateTimeFormatter.RFC_1123_DATE_TIME);
			return Math.max(0L, date.toInstant().toEpochMilli() - nowMillis);
		} catch (DateTimeParseException e) { 
			return NO_RETRY_AFTER;
		}
	}
	
	/**
	 * Find and parse a Retry-After header in a map of response headers.
	 * 
	 * @param headers response headers, keys are matched case insensitively, may be null.
	 * @param nowMillis the current time in milliseconds since the epoch.
	 * @return the number of milliseconds to wait, or {@link #NO_RETRY_AFTER} if not present or not parsable.
	 */
	public static long retryAfterMillis(Map<String,List<String>> headers, long nowMillis) { 
		return parseRetryAfterMillis(ApiFailure.headerValue(headers, "Retry-After"), nowMillis);
	}
	
	/**
	 * Determine the delay before the next retry, combining exponential backoff with any 
	 * Retry-After requested by the service, using the settings in {@link ServiceClientConfig}.
	 * 
	 * @param attempt zero based index of the retry (0 for the first retry).
	 * @param retryAfterMillis the delay requested by the service, or {@link #NO_RETRY_AFTER}.
	 * @param jitter a value in the range [0,1), typically random.
	 * @return the delay in milliseconds, or -1 if the service requested a delay longer than 
	 *   {@link ServiceClientConfig#getMaxRetryAfterMillis()}, in which case the request should not be retried.
	 */
	public static long delayBeforeRetryMillis(int attempt, long retryAfterMillis, double jitter) { 
		long backoff = computeBackoffMillis(attempt, ServiceClientConfig.getBackoffBaseMillis(), ServiceClientConfig.getBackoffMaxMillis(), jitter);
		if (retryAfterMillis == NO_RETRY_AFTER) { 
			return backoff;
		}
		if (retryAfterMillis > ServiceClientConfig.getMaxRetryAfterMillis()) { 
			return -1L;
		}
		return Math.max(backoff, retryAfterMillis);
	}

}
