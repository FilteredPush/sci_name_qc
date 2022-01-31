/** 
 * WoRMSService.java 
 * 
 * Copyright 2012-2022 President and Fellows of Harvard College
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
import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.SciNameUtils;
import org.marinespecies.aphia.v1_0.AphiaNameServicePortTypeProxy;
import org.marinespecies.aphia.v1_0.AphiaRecord;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
	
	public static List<NameUsage> lookupTaxon(String taxon, String author, boolean marineOnly) throws RemoteException {
		List<NameUsage> result  = new ArrayList<NameUsage>();
	
		AphiaNameServicePortTypeProxy wormsService = new AphiaNameServicePortTypeProxy();
		AphiaRecord[] resultsArr = wormsService.getAphiaRecords(taxon, false, false, marineOnly, 1);	
		if (resultsArr!=null || resultsArr.length==1) { 
			List<AphiaRecord> results = Arrays.asList(resultsArr);
			Iterator<AphiaRecord> i = results.iterator();
			logger.debug(resultsArr.length);
			while (i.hasNext()) { 
				AphiaRecord ar = i.next();
				NameUsage usage = new NameUsage(ar);
				result.add(usage);
			}
		}
		
		return result;
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
			logger.debug("No match found");
			id = null;
		} catch (RemoteException e) {
			throw new Exception("WoRMSService failed to access WoRMS Aphia service for " + taxon + ". " +e.getMessage());
		} 
		return id;
	}
	
	public static  List<NameUsage> lookupGenus(String genus) throws RemoteException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(genus)) { 
			AphiaNameServicePortTypeProxy wormsService = new AphiaNameServicePortTypeProxy();

			AphiaRecord[] resultsArr = wormsService.getAphiaRecords(genus, false, false, false, 1);
			if (resultsArr!=null && resultsArr.length>0) { 
				List<AphiaRecord> results = Arrays.asList(resultsArr);
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(resultsArr.length);
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && genus!=null 
							&& ar.getScientificname().equalsIgnoreCase(genus)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						logger.debug(ar.getTaxonRankID());
						if (ar.getTaxonRankID()==180) { 
							NameUsage match = new NameUsage();
							match.setAuthorship(ar.getAuthority());
							match.setCanonicalName(ar.getScientificname());
							match.setGuid(ar.getLsid());
							match.setRank(ar.getRank());
							match.setKingdom(ar.getKingdom());
							result.add(match);
						}
					}
				}
			}
		}

		return result;
	}

	public static  List<NameUsage> lookupTaxonAtRank(String taxon, String rank) throws RemoteException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(taxon)) { 
			AphiaNameServicePortTypeProxy wormsService = new AphiaNameServicePortTypeProxy();

			AphiaRecord[] resultsArr = wormsService.getAphiaRecords(taxon, false, false, false, 1);
			if (resultsArr!=null && resultsArr.length>0) { 
				List<AphiaRecord> results = Arrays.asList(resultsArr);
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(resultsArr.length);
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null 
							&& ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						logger.debug(ar.getTaxonRankID());
						if (ar.getTaxonRankID()==WoRMSService.rankStringToNumber(rank)) { 
							NameUsage match = new NameUsage();
							match.setAuthorship(ar.getAuthority());
							match.setCanonicalName(ar.getScientificname());
							match.setGuid(ar.getLsid());
							match.setRank(ar.getRank());
							match.setKingdom(ar.getKingdom());
							result.add(match);
						}
					}
				}
			}
		}

		return result;
	}	

	/**
	 * Return a rank as a string for a given aphia rankID. 
	 * 
	 * Note: values as of 2022 Jan 31 from https://www.marinespecies.org/rest/AphiaTaxonRanksByID/-1
	 * without awareness of kingdom applicability, and using the values Phylum/Subphylum instead of
	 * Phylum (Division)/Subphylum (Subdivision).
	 * 
	 * @param rankID  the aphia rank id for which to look up a rank string
	 * @return a rank represented as a string, or an empty string if no match is found
	 */
	public static String rankIdToString(int rankID) { 
		String result = "";

		switch (rankID) { 
		case 10: 
			result = "Kingdom";
			break; 
		case 20:
			result = "Subkingdom";
			break;
		case 30:
			result = "Phylum";
			break;
		case 40:
			result = "Subphylum";
			break;
		case 50:
			result = "Superclass";
			break;
		case 60:
			result = "Class";
			break;
		case 70:
			result = "Subclass";
			break;
		case 80:
			result = "Infraclass";
			break;
		case 90:
			result = "Superorder";
			break;
		case 100:
			result = "Order";
			break;
		case 110:
			result = "Suborder";
			break;
		case 120:
			result = "Infraorder";
			break;
		case 130:
			result = "Superfamily";
			break;
		case 140:
			result = "Family";
			break;
		case 150:
			result = "Subfamily";
			break;
		case 160:
			result = "Tribe";
			break;
		case 170:
			result = "Subtribe";
			break;
		case 180:
			result = "Genus";
			break;
		case 190:
			result = "Subgenus";
			break;
		case 200:
			result = "Section";
			break;
		case 210:
			result = "Subsection";
			break;
		case 220:
			result = "Species";
			break;
		case 230:
			result = "Subspecies";
			break;
		case 240:
			result = "Variety";
			break;
		case 250:
			result = "Subvariety";
			break;
		case 260:
			result = "Forma";
			break;
		case 270:
			result = "Subforma";
			break;
		case 280:
			result = "Mutatio";
			break;
		}

		return result;
	}
	
	/**
	 * For a string representing a taxon rank, return the corresponding aphia rankID
	 * 
	 * @param rank a case insensitive string for which to look up a taxon rank
	 * @return the aphia rankID for the given rank
	 */
	public static Integer rankStringToNumber(String rank) { 
		Integer result = null;

		switch (rank.toLowerCase()) { 
		case "kingdom": 
			result = 10;;
			break; 
		case "subkingdom":
			result = 20;
			break;
		case "phylum":
			result = 30;
			break;
		case "subphylum":
			result = 40;
			break;
		case "phylum (division)":
			result = 30;
			break;
		case "subphylum (division)":
			result = 40;
			break;		
		case "division":
			result = 30;
			break;
		case "subdivision":
			result = 40;
			break;				
		case "superclass":
			result = 50;
			break;
		case "class":
			result = 60;
			break;
		case "subclass":
			result = 70;
			break;
		case "infraclass":
			result = 80;
			break;
		case "superorder":
			result = 90;
			break;
		case "order":
			result = 100;
			break;
		case "suborder":
			result = 110;
			break;
		case "infraorder":
			result = 120;
			break;
		case "superfamily":
			result = 130;
			break;
		case "family":
			result = 140;
			break;
		case "subfamily":
			result = 150;
			break;
		case "tribe":
			result = 160;
			break;
		case "subtribe":
			result = 170;
			break;
		case "genus":
			result = 180;
			break;
		case "subgenus":
			result = 190;
			break;
		case "section":
			result = 200;
			break;
		case "subsection":
			result = 210;
			break;
		case "species":
			result = 220;
			break;
		case "subspecies":
			result = 230;
			break;
		case "variety":
			result = 240;
			break;
		case "var.":
			result = 240;
			break;
		case "var":
			result = 240;
			break;			
		case "subvariety":
			result = 250;
			break;
		case "forma":
			result = 260;
			break;
		case "form":
			result = 260;
			break;	
		case "f.":
			result = 260;
			break;			
		case "subforma":
			result = 270;
			break;
		case "mutatio":
			result = 280;
			break;
		}

		return result;
	}
	
	
	
	public static LookupResult nameComparisonSearch(String taxon, String author, boolean marineOnly) throws Exception {
		LookupResult result  = null;

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
						NameComparison comp = comparator.compare(author, foundAuthor);
						result = new LookupResult(comp,foundTaxon, foundAuthor,foundId, WoRMSService.class);
						
						String match = result.getNameComparison().getMatchType();
						logger.debug(taxon + ":" + author + " " + match + " " + foundAuthor);
					}	
				}
			}

		} catch (NullPointerException ex) {
			// no match found
			logger.debug("No match found");
			result = new LookupResult();
		} catch (RemoteException e) {
			throw new Exception("WoRMSService failed to access WoRMS Aphia service for " + taxon + ". " +e.getMessage());
		} 
		return result;
	}
	
}
