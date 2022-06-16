/** 
 * NameAuthorshipParse.java
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
package edu.harvard.mcz.nametools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Data object to hold parsed scientificName without authorship and scientific name authorship.
 * 
 * @author mole
 *
 */
public class NameAuthorshipParse {

	private static final Log logger = LogFactory.getLog(NameAuthorshipParse.class);

	private String nameWithoutAuthorship;
	private String authorship;
	private String nameWithAuthorship;
	
	/**
	 * @param nameWithoutAuthorship
	 * @param authorship
	 * @param nameWithAuthorship
	 */
	public NameAuthorshipParse(String nameWithoutAuthorship, String authorship, String nameWithAuthorship) {
		this.nameWithoutAuthorship = nameWithoutAuthorship;
		this.authorship = authorship;
		this.nameWithAuthorship = nameWithAuthorship;
	}

	public NameAuthorshipParse() {
	}

	/**
	 * @return the nameWithoutAuthorship
	 */
	public String getNameWithoutAuthorship() {
		if (nameWithoutAuthorship==null) { 
			return "";
		}
		return nameWithoutAuthorship;
	}

	/**
	 * @param nameWithoutAuthorship the nameWithoutAuthorship to set
	 */
	public void setNameWithoutAuthorship(String nameWithoutAuthorship) {
		this.nameWithoutAuthorship = nameWithoutAuthorship;
	}

	/**
	 * @return the authorship
	 */
	public String getAuthorship() {
		if (authorship==null) { 
			return "";
		}
		return authorship;
	}

	/**
	 * @param authorship the authorship to set
	 */
	public void setAuthorship(String authorship) {
		this.authorship = authorship;
	}

	/**
	 * @return the nameWithAuthorship
	 */
	public String getNameWithAuthorship() {
		if (nameWithAuthorship==null) { 
			return "";
		}
		return nameWithAuthorship;
	}

	/**
	 * @param nameWithAuthorship the nameWithAuthorship to set
	 */
	public void setNameWithAuthorship(String nameWithAuthorship) {
		this.nameWithAuthorship = nameWithAuthorship;
	}
	
	
}
