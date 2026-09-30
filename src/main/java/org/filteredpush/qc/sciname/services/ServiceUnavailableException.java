/** 
 * ServiceUnavailableException.java
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

/**
 * Thrown when a call to a remote service is not made: because the circuit breaker for the 
 * service is open after repeated failures (see {@link CircuitBreaker}), because the same 
 * lookup failed recently (see {@link LookupCache#getOrLoad(Object, LookupCache.Loader)}), or 
 * because no turn to make the request became available in time (see {@link RequestThrottle}).
 * 
 * @author mole
 */
public class ServiceUnavailableException extends ServiceException {

	private static final long serialVersionUID = -3502868651434961387L;

	/**
	 * Constructor.
	 * 
	 * @param message description of why the call was not made.
	 */
	public ServiceUnavailableException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 * @param message description of why the call was not made.
	 * @param cause the earlier failure that led to the call not being made.
	 */
	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor, for a call not made because the same call failed recently with an HTTP 
	 * error status, carrying that status code.
	 * 
	 * @param message description of why the call was not made.
	 * @param httpStatusCode the HTTP status code of the earlier failure, 0 if none.
	 * @param cause the earlier failure that led to the call not being made.
	 */
	public ServiceUnavailableException(String message, int httpStatusCode, Throwable cause) {
		super(message, httpStatusCode, cause);
	}

}
