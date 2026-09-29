/** 
 * ServiceException.java
 * 
 * Copyright 2022 President and Fellows of Harvard College
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>ServiceException class.</p>
 * 
 * <p>Thrown when a remote service (e.g. WoRMS, IRMNG) could not be used to complete a lookup.
 * The message is never empty.  Where the failure was an HTTP error status returned by the service,
 * the status code is available from {@link #getHttpStatusCode()}, allowing callers to distinguish
 * between, for example, a service that is unavailable or rate limiting (429, 5xx) and a request
 * that the service refused (4xx).</p>
 *
 * @author mole
 * @version $Id: $Id
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -2466745701520747852L;
	private static final Log logger = LogFactory.getLog(ServiceException.class);
	
	/** Message used when no other description of the failure is available. */
	public static final String UNSPECIFIED_MESSAGE = "Unspecified error accessing service";
	
	private final int httpStatusCode;
	
	/**
	 * <p>Constructor for ServiceException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ServiceException(String message) { 
		this(message, 0, null);
	}
	
	/**
	 * Constructor for ServiceException with an underlying cause.
	 * 
	 * @param message description of the failure, if null or blank, a generic message is used.
	 * @param cause the underlying cause of the failure.
	 */
	public ServiceException(String message, Throwable cause) { 
		this(message, 0, cause);
	}
	
	/**
	 * Constructor for ServiceException carrying the HTTP status code returned by the service.
	 * 
	 * @param message description of the failure, if null or blank, a generic message is used.
	 * @param httpStatusCode the HTTP status code returned by the service, or 0 if the failure 
	 *   was not an HTTP error status (e.g. a connection failure).
	 * @param cause the underlying cause of the failure, may be null.
	 */
	public ServiceException(String message, int httpStatusCode, Throwable cause) { 
		super(nonEmptyMessage(message, httpStatusCode), cause);
		this.httpStatusCode = httpStatusCode;
	}
	
	/**
	 * @return the HTTP status code returned by the service, or 0 if the failure was not 
	 *   an HTTP error status (e.g. a transport failure or a failure to parse a response).
	 */
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	/**
	 * @return true if this exception represents an HTTP error status returned by the service.
	 */
	public boolean isHttpError() { 
		return httpStatusCode > 0;
	}
	
	private static String nonEmptyMessage(String message, int httpStatusCode) { 
		if (message!=null && message.trim().length()>0) { 
			return message;
		}
		if (httpStatusCode > 0) { 
			return "HTTP " + httpStatusCode + " (" + ApiFailure.reasonPhrase(httpStatusCode) + ")";
		}
		return UNSPECIFIED_MESSAGE;
	}

}
