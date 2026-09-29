/** 
 * ApiFailure.java
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
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonParseException;

/**
 * Description of a failure to invoke a remote service through one of the generated swagger clients
 * (WoRMS or IRMNG), classified as a transport failure, an HTTP error status, or a failure to 
 * deserialize a response, with a diagnostic message that includes the HTTP status code, the 
 * request URL, relevant response headers, and (a truncated copy of) the response body.
 * 
 * <p>The generated clients throw an ApiException whose message is the HTTP reason phrase, 
 * which is empty for HTTP/2 responses, so the message alone is not a useful description 
 * of the failure.</p>
 * 
 * @author mole
 */
public final class ApiFailure {
	
	/** Maximum number of characters of a response body to include in a description. */
	public static final int MAX_BODY_LENGTH = 500;
	
	/** Kinds of failure. */
	public enum Kind { 
		/** Failure to connect to, send to, or receive from the service (an IOException). */
		TRANSPORT, 
		/** The service returned an HTTP error status. */
		HTTP, 
		/** The service response could not be parsed. */
		DESERIALIZATION, 
		/** Some other failure. */
		OTHER 
	}
	
	private final String serviceName;
	private final Kind kind;
	private final int httpStatusCode;
	private final String message;
	private final Map<String,List<String>> responseHeaders;
	private final String responseBody;
	private final Throwable cause;
	private final String requestUrl;
	private Throwable original;
	
	/**
	 * Constructor.
	 * 
	 * @param serviceName name of the service, e.g. WoRMS.
	 * @param message the exception message (e.g. the HTTP reason phrase), may be null or empty.
	 * @param httpStatusCode the HTTP status code, 0 if none.
	 * @param responseHeaders the response headers, may be null.
	 * @param responseBody the response body, may be null.
	 * @param cause the underlying cause, may be null.
	 * @param requestUrl the URL requested, may be null.
	 */
	public ApiFailure(String serviceName, String message, int httpStatusCode, Map<String,List<String>> responseHeaders, 
			String responseBody, Throwable cause, String requestUrl) { 
		this.serviceName = serviceName==null ? "Service" : serviceName;
		this.message = message;
		this.httpStatusCode = httpStatusCode;
		this.responseHeaders = responseHeaders==null ? Collections.<String,List<String>>emptyMap() : responseHeaders;
		this.responseBody = responseBody;
		this.cause = cause;
		this.requestUrl = requestUrl;
		if (cause instanceof IOException) { 
			kind = Kind.TRANSPORT;
		} else if (cause instanceof JsonParseException) { 
			kind = Kind.DESERIALIZATION;
		} else if (httpStatusCode > 0) { 
			kind = Kind.HTTP;
		} else { 
			kind = Kind.OTHER;
		}
	}
	
	/**
	 * Create an ApiFailure from an exception thrown when invoking a service.  The request URL is 
	 * taken from the last request made on the current thread through a {@link RequestThrottle}.
	 * 
	 * @param serviceName name of the service, e.g. WoRMS.
	 * @param t the exception thrown, an ApiException from one of the generated clients, 
	 *   a JsonParseException, an IOException, or some other exception.
	 * @return an ApiFailure describing t.
	 */
	public static ApiFailure from(String serviceName, Throwable t) { 
		ApiFailure result = build(serviceName, t, RequestThrottle.lastRequestUrl());
		result.original = t;
		return result;
	}
	
	private static ApiFailure build(String serviceName, Throwable t, String url) { 
		if (t instanceof org.marinespecies.aphia.v1_0.handler.ApiException) { 
			org.marinespecies.aphia.v1_0.handler.ApiException e = (org.marinespecies.aphia.v1_0.handler.ApiException) t;
			return new ApiFailure(serviceName, e.getMessage(), e.getCode(), e.getResponseHeaders(), e.getResponseBody(), e.getCause(), url);
		}
		if (t instanceof org.irmng.aphia.v1_0.handler.ApiException) { 
			org.irmng.aphia.v1_0.handler.ApiException e = (org.irmng.aphia.v1_0.handler.ApiException) t;
			return new ApiFailure(serviceName, e.getMessage(), e.getCode(), e.getResponseHeaders(), e.getResponseBody(), e.getCause(), url);
		}
		if (t instanceof JsonParseException || t instanceof IOException) { 
			return new ApiFailure(serviceName, t.getMessage(), 0, null, null, t, url);
		}
		return new ApiFailure(serviceName, t==null ? null : t.getMessage(), 0, null, null, t, url);
	}
	
	public Kind getKind() { return kind; }
	public int getHttpStatusCode() { return httpStatusCode; }
	public String getMessage() { return message; }
	public Map<String, List<String>> getResponseHeaders() { return responseHeaders; }
	public String getResponseBody() { return responseBody; }
	public Throwable getCause() { return cause; }
	public String getRequestUrl() { return requestUrl; }
	
	/**
	 * @return true if the failure is plausibly transient and worth retrying, see {@link RetryPolicy#isRetryable(int, Throwable)}.
	 */
	public boolean isRetryable() { 
		if (cause instanceof InterruptedIOException && !(cause instanceof SocketTimeoutException)) { 
			// interrupted while waiting, not a network failure.
			return false;
		}
		return RetryPolicy.isRetryable(httpStatusCode, cause);
	}
	
	/**
	 * @return the delay requested by a Retry-After header, or {@link RetryPolicy#NO_RETRY_AFTER}.
	 */
	public long getRetryAfterMillis() { 
		return RetryPolicy.retryAfterMillis(responseHeaders, System.currentTimeMillis());
	}
	
	/**
	 * Create a ServiceException carrying the description of this failure.
	 * 
	 * @param prefix text to prepend to the description, may be null.
	 * @return a ServiceException with a non-empty message.
	 */
	public ServiceException toServiceException(String prefix) { 
		String text = (prefix==null || prefix.length()==0) ? describe() : prefix + describe();
		return new ServiceException(text, kind==Kind.HTTP ? httpStatusCode : 0, original!=null ? original : cause);
	}
	
	/**
	 * Render a diagnostic description of the failure, never empty.
	 * 
	 * @return a description including the kind of failure, HTTP status code and reason, request URL,
	 * Retry-After and Content-Type headers, and truncated response body where available.
	 */
	public String describe() { 
		StringBuilder result = new StringBuilder(serviceName);
		String from = isBlank(requestUrl) ? "" : " from " + requestUrl;
		switch (kind) { 
		case TRANSPORT:
			result.append(" connection failure").append(isBlank(requestUrl) ? "" : " requesting " + requestUrl).append(": ");
			result.append(transportDescription(cause));
			break;
		case DESERIALIZATION:
			result.append(" response could not be parsed").append(from).append(": ");
			result.append(isBlank(message) ? cause.getClass().getSimpleName() : message);
			break;
		case HTTP:
			String reason = isBlank(message) ? reasonPhrase(httpStatusCode) : message.trim();
			result.append(" HTTP ").append(httpStatusCode).append(" (").append(reason).append(")").append(from);
			break;
		default: 
			result.append(" request failed").append(from).append(": ");
			if (!isBlank(message)) { 
				result.append(message);
			} else if (cause!=null) { 
				result.append(cause.getClass().getSimpleName());
				if (!isBlank(cause.getMessage())) { 
					result.append(" ").append(cause.getMessage());
				}
			} else { 
				result.append("no further information available");
			}
		}
		String retryAfter = headerValue(responseHeaders, "Retry-After");
		if (retryAfter!=null) { 
			result.append("; Retry-After: ").append(retryAfter);
		}
		String contentType = headerValue(responseHeaders, "Content-Type");
		if (contentType!=null) { 
			result.append("; Content-Type: ").append(contentType);
		}
		if (!isBlank(responseBody)) { 
			result.append("; Response body: ").append(truncate(responseBody, MAX_BODY_LENGTH));
		}
		return result.toString();
	}
	
	private static String transportDescription(Throwable cause) { 
		String detail = cause==null || isBlank(cause.getMessage()) ? "" : " " + cause.getMessage();
		if (cause instanceof UnknownHostException) { 
			return "UnknownHostException" + detail + " (DNS lookup failed, network connection probably lost)";
		}
		if (cause instanceof SocketTimeoutException) { 
			return "SocketTimeoutException" + detail + " (service did not respond in time)";
		}
		if (cause instanceof ConnectException) { 
			return "ConnectException" + detail + " (unable to connect to service)";
		}
		return (cause==null ? "IOException" : cause.getClass().getSimpleName()) + detail;
	}
	
	/**
	 * Truncate a string to a maximum length, collapsing whitespace, appending an indication of 
	 * truncation if needed.
	 * 
	 * @param value string to truncate, may be null.
	 * @param maxLength maximum number of characters to retain.
	 * @return the truncated string, or null if value was null.
	 */
	public static String truncate(String value, int maxLength) { 
		if (value==null) { 
			return null;
		}
		String collapsed = value.replaceAll("\\s+", " ").trim();
		if (collapsed.length() <= maxLength) { 
			return collapsed;
		}
		return collapsed.substring(0, maxLength) + "...[truncated, " + collapsed.length() + " characters]";
	}
	
	/**
	 * Find the first value of a header, matching the header name case insensitively.
	 * 
	 * @param headers map of header names to values, may be null.
	 * @param name the header name to look for.
	 * @return the first value for the header, or null if not present.
	 */
	public static String headerValue(Map<String,List<String>> headers, String name) { 
		if (headers==null || name==null) { 
			return null;
		}
		for (Map.Entry<String,List<String>> entry : headers.entrySet()) { 
			if (entry.getKey()!=null && entry.getKey().equalsIgnoreCase(name) 
					&& entry.getValue()!=null && !entry.getValue().isEmpty()) { 
				return entry.getValue().get(0);
			}
		}
		return null;
	}
	
	/**
	 * Standard reason phrase for an HTTP status code, used when a response does not carry 
	 * a reason phrase (as is the case for HTTP/2).
	 * 
	 * @param code the HTTP status code.
	 * @return the standard reason phrase, or "Unknown Status" if not known.
	 */
	public static String reasonPhrase(int code) { 
		switch (code) { 
		case 400: return "Bad Request";
		case 401: return "Unauthorized";
		case 403: return "Forbidden";
		case 404: return "Not Found";
		case 405: return "Method Not Allowed";
		case 406: return "Not Acceptable";
		case 408: return "Request Timeout";
		case 409: return "Conflict";
		case 410: return "Gone";
		case 413: return "Payload Too Large";
		case 414: return "URI Too Long";
		case 415: return "Unsupported Media Type";
		case 429: return "Too Many Requests";
		case 500: return "Internal Server Error";
		case 501: return "Not Implemented";
		case 502: return "Bad Gateway";
		case 503: return "Service Unavailable";
		case 504: return "Gateway Timeout";
		default: return "Unknown Status";
		}
	}
	
	private static boolean isBlank(String value) { 
		return value==null || value.trim().length()==0;
	}

}
