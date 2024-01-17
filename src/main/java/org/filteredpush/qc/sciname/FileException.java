/** 
 * FileException.java
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
package org.filteredpush.qc.sciname;

/**
 * <p>FileException class.</p>
 *
 * @author mole
 * @version $Id: $Id
 */
public class FileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6778115847089618803L;

	/**
	 * <p>Constructor for FileException.</p>
	 */
	public FileException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>Constructor for FileException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public FileException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>Constructor for FileException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public FileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>Constructor for FileException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public FileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>Constructor for FileException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 * @param enableSuppression a boolean.
	 * @param writableStackTrace a boolean.
	 */
	public FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
