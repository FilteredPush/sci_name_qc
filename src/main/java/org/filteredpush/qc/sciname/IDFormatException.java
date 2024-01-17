/** 
 * IDFormatException.java
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
 * <p>IDFormatException class.</p>
 *
 * @author mole
 * @version $Id: $Id
 */
public class IDFormatException extends Exception {

	private static final long serialVersionUID = 5647129845729161541L;

	/**
	 * <p>Constructor for IDFormatException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public IDFormatException(String message) { 
		super(message);
	}

}
