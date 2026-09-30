/** 
 * RequestNotSentException.java
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

/**
 * Thrown by {@link RequestThrottle} when a request is not sent to a service because no turn 
 * to make the request became available within {@link ServiceClientConfig#getAcquireTimeoutMillis()}.
 * An IOException, so that it can be thrown from an OkHttp interceptor, it is not retried, 
 * and is reported to callers as a {@link ServiceUnavailableException}.
 * 
 * @author mole
 */
public class RequestNotSentException extends IOException {

	private static final long serialVersionUID = 6019311264893140417L;

	/**
	 * Constructor.
	 * 
	 * @param message description of why the request was not sent.
	 */
	public RequestNotSentException(String message) {
		super(message);
	}

}
