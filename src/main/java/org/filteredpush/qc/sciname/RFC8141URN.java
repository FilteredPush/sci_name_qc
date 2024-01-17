/** 
 * RFC8141URN.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for working with URNs
 *
 * @author mole
 *
 * Test for RFC8141 compliant URNs https://tools.ietf.org/html/rfc8141
 * @version $Id: $Id
 */
public class RFC8141URN {
	

	protected String nid;  // namespace id
	protected String nss;  // namepace specific string
	
	/**
	 * <p>Constructor for RFC8141URN.</p>
	 *
	 * @param urn a {@link java.lang.String} object.
	 * @throws org.filteredpush.qc.sciname.URNFormatException if any.
	 */
	public RFC8141URN(String urn) throws URNFormatException { 
		if (RFC8141URN.isRFC8141URN(urn)) {
		  Pattern urn_pattern = Pattern.compile(URN_RFC8141);
		  Matcher matcher = urn_pattern.matcher(urn);
		  if (matcher.matches()) { 
			  setNid(matcher.group(2));
			  setNss(matcher.group(3));
		  }
		} else { 
			throw new URNFormatException("Not a valid URN");
		}
	}


	/**
	 * <p>Getter for the field <code>nid</code>.</p>
	 *
	 * @return the nid Namespace identifier
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * <p>Setter for the field <code>nid</code>.</p>
	 *
	 * @param nid the namespace identifer to set
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}


	/**
	 * <p>Getter for the field <code>nss</code>.</p>
	 *
	 * @return the nss Namespace specific string
	 */
	public String getNss() {
		return nss;
	}


	/**
	 * <p>Setter for the field <code>nss</code>.</p>
	 *
	 * @param nss the namespace specific string to set
	 */
	public void setNss(String nss) {
		this.nss = nss;
	}


	private static final Log logger = LogFactory.getLog(RFC8141URN.class);

	/*
	  namestring    = assigned-name
                      [ rq-components ]
                      [ "#" f-component ]
      assigned-name = "urn" ":" NID ":" NSS
      NID           = (alphanum) 0*30(ldh) (alphanum)
      ldh           = alphanum / "-"
      NSS           = pchar *(pchar / "/")
      rq-components = [ "?+" r-component ]
                      [ "?=" q-component ]
      r-component   = pchar *( pchar / "/" / "?" )
      q-component   = pchar *( pchar / "/" / "?" )
      f-component   = fragment
	 */
	
	  /**
	   * pchar from rfc3986
	   */
	  public static String PCHAR = "[A-Za-z0-9\\-\\._~!\\$&'\\(\\)\\*\\+,;=:@]|(%[0-9A-Fa-f]{2})";
	  
	  /**
	   * fragment from rfc3986
	   */
	  public static String FRAGMENT = "((" + PCHAR + ")|/|\\?)*";

	  /**
	   * Namespace Specific String (NSS)
	   * 
	   * pchar *(pchar / "/")
	   */
	  private static String NSS = "(" + PCHAR + ")((" + PCHAR + ")|/)*";

	  /**
	   * Namespace Identifier (NID)
	   * 
	   * (alphanum) 0*30(ldh) (alphanum)
	   */
	  private static String NID = "[A-Za-z0-9][A-Za-z0-9\\-]{0,30}[A-Za-z0-9]";

	  /**
	   * assigned-name
	   * 
	   * "urn" ":" NID ":" NSS  
	   * (where urn is case insenstive)
	   */
	  private static String ASSIGNED_NAME = "([uU][rR][nN]):(" + NID + "):(" + NSS + ")";

	  /**
	   * r-component and q-component, share same definition
	   * 
	   *  pchar *( pchar / "/" / "?" )
	   */
	  private static String RQ_COMPONENT = "(" + PCHAR + ")" + FRAGMENT;
	  
	  /**
	   * rq-components 
	   * 
	   * [ "?+" r-component ] [ "?=" q-component ]
	   * 
	   */
	  private static String RQ_COMPONENTS =
	      "((\\?\\+)(" + RQ_COMPONENT + "))?((\\?=)(" + RQ_COMPONENT + "))?";

	  /**
	   * Regular expression to match RFC 8141 URNs.
	   */
	  public static String URN_RFC8141 = "^" + ASSIGNED_NAME + RQ_COMPONENTS + "(#" + FRAGMENT + ")?$";

	  /**
	   * <p>isRFC8141URN.</p>
	   *
	   * @param stringToTest a {@link java.lang.String} object.
	   * @return a boolean.
	   */
	  public static boolean isRFC8141URN(String stringToTest) {
		  boolean result = false;
		  if (stringToTest!=null) { 
			  Pattern urn_pattern = Pattern.compile(URN_RFC8141);
			  Matcher matcher = urn_pattern.matcher(stringToTest);
			  result = matcher.matches();
			  String nid = null;
			  String nss = null;
			  if (result) { 
				  for (int i=1; i<=matcher.groupCount(); i++) { 
					  logger.debug(matcher.group(i));
					  nid = matcher.group(2);
					  nss = matcher.group(3);
				  }
			  }
			  if (nid==null) { 
				  result = false;
			  }
			  if (nss==null) { 
				  result = false;
			  }
			  // additional section 51. and 5.2 rules for formal and informal namespaces
			  if (nid!=null && nid.startsWith("X-")) { 
				  result = false; // formerly permitted experimental namespace RFC3406
			  }
			  if (nid!=null && nid.startsWith("urn-")) { 
				  if (!nid.matches("^urn-[1-9][0-9]*$")) { 
					  result = false;  // not a correctly formatted informal URN namespace.
				  }
			  }
		  }
		  return result;
	  }

}

