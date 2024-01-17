/** 
 * BooleanWithComment.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Data structure to hold a Boolean and a string comment.
 *
 * @author mole
 * @version $Id: $Id
 */
public class BooleanWithComment {

	private static final Log logger = LogFactory.getLog(BooleanWithComment.class);
	
	private Boolean booleanValue;
	private StringBuilder comment;
	
	/**
	 * Construct an instance of BooleanWithComment with a specified value and comment
	 *
	 * @param booleanValue true or false
	 * @param comment associated with booleanValue
	 */
	public BooleanWithComment(Boolean booleanValue, String comment) {
		super();
		this.booleanValue = booleanValue;
		this.comment = new StringBuilder();
		this.comment.append(comment);
	}

	/**
	 * <p>Getter for the field <code>booleanValue</code>.</p>
	 *
	 * @return the booleanValue
	 */
	public Boolean getBooleanValue() {
		return booleanValue;
	}

	/**
	 * <p>Setter for the field <code>booleanValue</code>.</p>
	 *
	 * @param booleanValue the booleanValue to set
	 */
	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	/**
	 * <p>Getter for the field <code>comment</code>.</p>
	 *
	 * @return the comment as a string
	 */
	public String getComment() {
		return comment.toString();
	}

	/**
	 * Append a string to the comment
	 *
	 * @param comment the string to append to the comment
	 */
	public void addComment(String comment) {
		if (this.comment==null) { 
			this.comment = new StringBuilder();
		}
		if (this.comment.length()>0) { 
			this.comment.append("|");
		}
		this.comment.append(comment);
	}


}
