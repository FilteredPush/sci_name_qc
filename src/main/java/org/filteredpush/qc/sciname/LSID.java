/** 
 * LSID.java
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
 * Utility class for testing strings that may be LSIDs.
 * 
 * @author mole
 *
 */
public class LSID extends RFC8141URN {

	private static final Log logger = LogFactory.getLog(LSID.class);

	private String authority;
	private String namespace;
	private String objectID;
	private String version;
	
	/**
	 * @param urn
	 * @throws URNFormatException
	 */
	public LSID(String urn) throws URNFormatException {
		super(urn);
		// LSID specification https://www.omg.org/cgi-bin/doc?dtc/04-05-01.pdf
		// has authority:namespace:objectidenfification with optional :revisionidentification
		// where authority is usually an internet domain name or is a unique string and, 
		// namespace, objectidentification, and revisionidentification are
		// specified as "alphanumeric sequence", but the examples therin include non-alphanumeric
		// characters -., so using PCHAR without : for each._
		String chars = PCHAR.replace(":", "");
		logger.debug(chars);
		logger.debug(nss);
		String[] bits = nss.split(":");
		if (bits.length<3 || bits.length > 4) { 
			throw new URNFormatException("Not a validly formatted LSID");
		}
		if (bits[0].length()<1 || bits[1].length()<1 || bits[2].length()<1) { 
			throw new URNFormatException("Not a validly formatted LSID");
		}
		authority = bits[0];
		namespace = bits[1];
		objectID = bits[2];
		if (bits.length==4) { 
			version = bits[3];
		} else {
			version = null;
		}
	}

	/**
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the objectID
	 */
	public String getObjectID() {
		return objectID;
	}

	/**
	 * @param objectID the objectID to set
	 */
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
