/** 
 * WoRMSService.java 
 * 
 * Copyright 2012 President and Fellows of Harvard College
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

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marinespecies.aphia.v1_0.AphiaNameServicePortTypeProxy;
import org.marinespecies.aphia.v1_0.AphiaRecord;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Provides support for scientific name validation against the WoRMS 
 * (World Register of Marine Species) Aphia web service. 
 * See: http://www.marinespecies.org/aphia.php?p=webservice
 * 
 * @author Lei Dou
 * @author Paul J. Morris
 *
 */
public class WoRMSService {

	private static final Log logger = LogFactory.getLog(WoRMSService.class);
	
	private AphiaNameServicePortTypeProxy wormsService;
	protected AuthorNameComparator authorNameComparator;
	
	private final static String WORMSGUIDPREFIX = "urn:lsid:marinespecies.org:taxname:";

	public WoRMSService(boolean test) throws IOException {
		super();
		if (test) { 
			test();
		}
	}
	
	protected void test()  throws IOException { 
		logger.debug(wormsService.getEndpoint());
		URL test = new URL(wormsService.getEndpoint());
		URLConnection conn = test.openConnection();
		conn.connect();
	}

	/**
	 * Find a taxon name record in WoRMS.
	 * 
	 * @param taxon name to look for
	 * @param author authority to look for
	 * 
	 * @return aphia id for the taxon
	 * 
	 * @throws Exception
	 */
	public static String simpleNameSearch(String taxon, String author, boolean marineOnly) throws Exception {
		String id  = null;

		AphiaNameServicePortTypeProxy wormsService = new AphiaNameServicePortTypeProxy();

		try {
			AphiaRecord[] resultsArr = wormsService.getAphiaRecords(taxon, false, false, marineOnly, 1);	
			if (resultsArr!=null || resultsArr.length==1) { 
				List<AphiaRecord> results = Arrays.asList(resultsArr);
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(resultsArr.length);
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null && ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						
						String foundId = ar.getLsid();
						String foundTaxon = ar.getScientificname();
						String foundAuthor = ar.getAuthority();
						AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(author, null);
						NameComparison comparison = comparator.compare(author, foundAuthor);
						String match = comparison.getMatchType();
						logger.debug(taxon + ":" + author + " " + match + " " + foundAuthor);
						if(foundTaxon.equalsIgnoreCase(taxon) &&
								(
								author.toLowerCase().equals(foundAuthor.toLowerCase())  || 
								match.equals(NameComparison.MATCH_EXACT) || 
								match.equals(NameComparison.MATCH_SAMEBUTABBREVIATED) || 
								match.equals(NameComparison.MATCH_SIMILAREXACTYEAR) || 
								match.equals(NameComparison.MATCH_EXACTADDSYEAR) || 
								match.equals(NameComparison.MATCH_EXACTMISSINGYEAR)
								)
						){
							logger.debug(foundId);
							id = foundId;
						}						
						
					}	
				}
			}

		} catch (NullPointerException ex) {
			// no match found
			id = null;
		} catch (RemoteException e) {
			throw new Exception("WoRMSService failed to access WoRMS Aphia service for " + taxon + ". " +e.getMessage());
		} 

		return id;
	}

}
