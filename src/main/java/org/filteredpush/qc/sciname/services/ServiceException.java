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
 * @author mole
 * @version $Id: $Id
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -2466745701520747852L;
	private static final Log logger = LogFactory.getLog(ServiceException.class);
	
	/**
	 * <p>Constructor for ServiceException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ServiceException(String message) { 
		super(message);
	}

}
