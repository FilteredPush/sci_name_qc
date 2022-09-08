/** 
 * IRMNGService.java
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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.IDFormatException;
import org.filteredpush.qc.sciname.SciNameUtils;
import org.marinespecies.aphia.v1_0.api.TaxonomicDataApi;
import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.marinespecies.aphia.v1_0.model.AphiaRecord;
import org.marinespecies.aphia.v1_0.model.AphiaRecordsArray;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ICZNAuthorNameComparator;
import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;
import edu.harvard.mcz.nametools.ScientificNameComparator;

/**
 * @author mole
 *
 */
public class IRMNGService implements Validator {

	private static final Log logger = LogFactory.getLog(IRMNGService.class);
	private static int MAX_RETRIES = 3;
	
	private TaxonomicDataApi irmngService;
	protected AuthorNameComparator authorNameComparator;
	protected int depth;  // for managing retries on network failure
	
	private final static String IRMNGGUIDPREFIX = "urn:lsid:irmng.org:taxname:";
	private final static String IRMNGBASEPATH = "https://www.irmng.org/rest";

	public IRMNGService(boolean test) throws IOException {
		super();
		irmngService = new TaxonomicDataApi();
		irmngService.getApiClient().setBasePath(IRMNGBASEPATH);
		depth = MAX_RETRIES;  // on test failure, don't retry
		if (test) { 
			test();
		}
		depth = 0;
	}
	
	protected void test()  throws IOException { 
		logger.debug(irmngService.getApiClient().getBasePath());
		URL test = new URL(irmngService.getApiClient().getBasePath());
		URLConnection conn = test.openConnection();
		conn.connect();
	}
	
	/**
	 * Given an AphiaID, look up the Aphia record.
	 * 
	 * @param aphiaID the AphiaID to look up, should be parsable as an integer.
	 * @return a NameUsage containing the returned information
	 * @throws IDFormatException if the provided aphiaID is not an integer.
	 * @throws ApiException if there is a problem invoking the service.
	 */
	public static NameUsage lookupTaxonByID(String aphiaID) throws IDFormatException, ApiException { 
		NameUsage result = new NameUsage();
		if (!SciNameUtils.isEmpty(aphiaID)) { 
			if (!aphiaID.matches("^[0-9]+$")) { 
				throw new IDFormatException("provided aphiaID is not an integer");
			}
			Integer intAphiaID = Integer.parseInt(aphiaID);
			TaxonomicDataApi irmngService = new TaxonomicDataApi();
			irmngService.getApiClient().setBasePath(IRMNGBASEPATH);
			AphiaRecord ar = irmngService.aphiaRecordByAphiaID(intAphiaID);
			if (ar !=null && ar.getScientificname()!=null ) { 
				logger.debug(ar.getScientificname());
				logger.debug(ar.getAuthority());
				result.setAuthorship(ar.getAuthority());
				result.setCanonicalName(ar.getScientificname());
				result.setGuid(ar.getLsid());
				result.setRank(ar.getRank());
				result.setKingdom(ar.getKingdom());
				result.setScientificName(ar.getScientificname() + " " + ar.getAuthority());
			}
		}
		
		return result;
	}
	
	public static  List<NameUsage> lookupTaxon(String taxon,  String authorship) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(taxon)) { 
			TaxonomicDataApi irmngService = new TaxonomicDataApi();
			irmngService.getApiClient().setBasePath(IRMNGBASEPATH);

			List<AphiaRecord> results = irmngService.aphiaRecordsByName(taxon, false, false, 1);
			if (results!=null && results.size()>0) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null 
							&& ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						NameUsage match = new NameUsage();
						match.setAuthorship(ar.getAuthority());
						match.setOriginalAuthorship(authorship);
						match.setCanonicalName(ar.getScientificname());
						match.setGuid(ar.getLsid());
						match.setRank(ar.getRank());
						match.setKingdom(ar.getKingdom());
						NameComparison comparison = match.getAuthorComparator().compare(authorship, ar.getAuthority());
						if (comparison.getMatchType().equals(NameComparison.MATCH_STRONGDISSIMILAR)) { 
							// leave out
							logger.debug(comparison.getMatchType());
						} else {  
							result.add(match);
						}
					}
				}
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

		TaxonomicDataApi irmngService = new TaxonomicDataApi();
		irmngService.getApiClient().setBasePath(IRMNGBASEPATH);

		try {
			List<AphiaRecord> results = irmngService.aphiaRecordsByName(taxon, false, marineOnly, 1);	
			if (results!=null || results.size()==1) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
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
		} catch (ApiException e) {
			throw new Exception("WoRMSService failed to access WoRMS Aphia service for " + taxon + ". " +e.getMessage());
		} 
		return id;
	}
	
	public static  List<NameUsage> lookupGenus(String genus) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(genus)) { 
			TaxonomicDataApi irmngService = new TaxonomicDataApi();
			irmngService.getApiClient().setBasePath(IRMNGBASEPATH);

			List<AphiaRecord> results = irmngService.aphiaRecordsByName(genus, false, false, 1);
			if (results!=null && results.size()>0) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
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

	public static  List<NameUsage> lookupTaxonAtRank(String taxon, String rank) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(taxon)) { 
			TaxonomicDataApi irmngService = new TaxonomicDataApi();
			irmngService.getApiClient().setBasePath(IRMNGBASEPATH);

			List<AphiaRecord> results = irmngService.aphiaRecordsByName(taxon, false, false, 1);
			if (results!=null && results.size()>0) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null 
							&& ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						logger.debug(ar.getTaxonRankID());
						logger.debug(WoRMSService.rankStringToNumber(rank));
						if (ar.getTaxonRankID().equals(WoRMSService.rankStringToNumber(rank))) { 
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

		TaxonomicDataApi irmngService = new TaxonomicDataApi();
		irmngService.getApiClient().setBasePath(IRMNGBASEPATH);

		try {
			List<AphiaRecord> results = irmngService.aphiaRecordsByName(taxon, false, marineOnly, 1);	
			if (results!=null || results.size()==1) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
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
		} catch (ApiException e) {
			throw new Exception("WoRMSService failed to access WoRMS Aphia service for " + taxon + ". " +e.getMessage());
		} 
		return result;
	}

	@Override
	public NameUsage validate(NameUsage taxonNameToValidate) throws ServiceException {
		logger.debug("Checking: " + taxonNameToValidate.getScientificName() + " " + taxonNameToValidate.getAuthorship());
		NameUsage result = null;
		depth++;   
		try {
			String taxonName = taxonNameToValidate.getScientificName();
			String authorship = taxonNameToValidate.getAuthorship();
			authorNameComparator = AuthorNameComparator.authorNameComparatorFactory(authorship, taxonNameToValidate.getKingdom());
			ScientificNameComparator scientificNameComparator = new ScientificNameComparator();
			taxonNameToValidate.setAuthorComparator(authorNameComparator);
			List<AphiaRecord> results = irmngService.aphiaRecordsByName(taxonName, false, false, 1);
			if (results!=null && results.size()>0) { 
				// We got at least one result
				Iterator<AphiaRecord> i = results.iterator();
				//Multiple matches indicate homonyms (or in WoRMS, deleted records).
				if (results.size()>1) {
				    logger.debug("More than one match: " + results.size());
					boolean exactMatch = false;
					List<AphiaRecord> matches = new ArrayList<AphiaRecord>();
					while (i.hasNext() && !exactMatch) { 
					    AphiaRecord ar = i.next();
					    matches.add(ar);
					    logger.debug(ar.getScientificname());
					    logger.debug(ar.getAphiaID());
					    logger.debug(ar.getAuthority());
					    logger.debug(ar.getUnacceptreason());
					    logger.debug(ar.getStatus());
					    if (ar !=null && ar.getScientificname()!=null && taxonName!=null && ar.getScientificname().equals(taxonName)) {
					    	if (ar.getAuthority()!=null && ar.getAuthority().equals(authorship)) {
					    		// If one of the results is an exact match on scientific name and authorship, pick that one. 
					    		result = new NameUsage(ar);
					    		result.setInputDbPK(taxonNameToValidate.getInputDbPK());
					    		result.setMatchDescription(NameComparison.MATCH_EXACT);
					    		result.setNameMatchDescription(NameComparison.MATCH_EXACT);
					    		result.setAuthorshipStringEditDistance(1d);
					    		result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
					    		result.setOriginalScientificName(taxonNameToValidate.getScientificName());
					    		result.setScientificNameStringEditDistance(1d);
					    		result.setExtension(lookupHabitat(ar));
					    		exactMatch = true;
					    	}
					    }
					}
					if (!exactMatch) {
						// If we didn't find an exact match on scientific name and authorship in the list, pick the 
						// closest authorship and list all of the potential matches.  
						Iterator<AphiaRecord> im = matches.iterator();
						NameUsage closest = null;
						StringBuffer names = new StringBuffer();
						while (im.hasNext()) { 
							AphiaRecord ar = im.next();
							NameUsage current = new NameUsage(ar);
							NameComparison comparison = scientificNameComparator.compare(taxonName, current.getScientificName());
							if (NameComparison.isPlausible(comparison.getMatchType())) { 
								names.append("; ").append(current.getScientificName()).append(" ").append(current.getAuthorship()).append(" ").append(current.getUnacceptReason()).append(" ").append(current.getTaxonomicStatus());
								if (ICZNAuthorNameComparator.calulateSimilarityOfAuthor(closest.getAuthorship(), authorship) < ICZNAuthorNameComparator.calulateSimilarityOfAuthor(current.getAuthorship(), authorship)) { 
									current.setExtension(lookupHabitat(ar));
									closest = current;
								}
							}
						}
						if (closest==null) {
							// none of the responses were plausible, treat as no match.
							logger.debug("No plausible matches");
						} else { 
							result = closest;
							result.setInputDbPK(taxonNameToValidate.getInputDbPK());
							result.setMatchDescription(NameComparison.MATCH_MULTIPLE + " " + names.toString());
							result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
							result.setOriginalScientificName(taxonNameToValidate.getScientificName());
							result.setScientificNameStringEditDistance(1d);
							result.setAuthorshipStringEditDistance(ICZNAuthorNameComparator.calulateSimilarityOfAuthor(taxonNameToValidate.getAuthorship(), result.getAuthorship()));
						}
					}
				} else { 
				  // we got exactly one result
				  while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxonName!=null && ar.getScientificname().equals(taxonName)) {
						if (ar.getAuthority()!=null && ar.getAuthority().equals(authorship)) { 
							// scientific name and authorship are an exact match 
							result = new NameUsage(ar);
							result.setInputDbPK(taxonNameToValidate.getInputDbPK());
							result.setMatchDescription(NameComparison.MATCH_EXACT);
				    		result.setNameMatchDescription(NameComparison.MATCH_EXACT);
							result.setAuthorshipStringEditDistance(1d);
							result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
							result.setOriginalScientificName(taxonNameToValidate.getScientificName());
							result.setScientificNameStringEditDistance(1d);
							result.setExtension(lookupHabitat(ar));
						} else {
							// find how 
							if (authorship!=null && ar!=null && ar.getAuthority()!=null) { 
								//double similarity = taxonNameToValidate.calulateSimilarityOfAuthor(ar.getAuthority());
								logger.debug(authorship);
								logger.debug(ar.getAuthority());
								NameComparison comparison = authorNameComparator.compare(authorship, ar.getAuthority());
								String match = comparison.getMatchType();
								double similarity = comparison.getSimilarity();
								logger.debug(similarity);
								result = new NameUsage(ar);
								result.setInputDbPK(taxonNameToValidate.getInputDbPK());
								result.setAuthorshipStringEditDistance(similarity);
								result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
								result.setOriginalScientificName(taxonNameToValidate.getScientificName());
								result.setMatchDescription(match);
								NameComparison nameComparison = scientificNameComparator.compare(taxonName, ar.getScientificname());
								result.setNameMatchDescription(nameComparison.getMatchType());
								result.setScientificNameStringEditDistance(nameComparison.getSimilarity());
								result.setExtension(lookupHabitat(ar));
							} else { 
								// no authorship was provided in the results, treat as no match
								logger.error("Result with null authorship.");
							}
						}
					}
				  }
				}
			} else { 
				logger.debug("No match.");
				// Try WoRMS fuzzy matching query
				String[] searchNames = { taxonName + " " + authorship };
				List<String> searchNamesList = Arrays.asList(searchNames);
				List<AphiaRecordsArray> matchResultsArr = irmngService.aphiaRecordsByMatchNames(searchNamesList, false);
				if (matchResultsArr!=null && matchResultsArr.size()>0) {
					Iterator<AphiaRecordsArray> i0 = matchResultsArr.iterator();
					while (i0.hasNext()) {
						// iterate through the inputs, there should be one and only one
						AphiaRecordsArray matchResArr = i0.next();
						Iterator<AphiaRecord> im = matchResArr.iterator();
						List<NameUsage> potentialMatches = new ArrayList<NameUsage>();
						while (im.hasNext()) { 
							// iterate through the results, no match will have one result that is null
							AphiaRecord ar = im.next();
							if (ar!=null) { 
								NameUsage match = new NameUsage(ar);
								double similarity = ICZNAuthorNameComparator.calulateSimilarityOfAuthor(taxonNameToValidate.getAuthorship(), match.getAuthorship());
								match.setAuthorshipStringEditDistance(similarity);
								logger.debug(match.getScientificName());
								logger.debug(match.getAuthorship());
								logger.debug(similarity);
								NameComparison comparison = scientificNameComparator.compare(taxonName, match.getScientificName());
								if (NameComparison.isPlausible(comparison.getMatchType())) { 
									match.setNameMatchDescription(comparison.getMatchType());
									match.setScientificNameStringEditDistance(comparison.getSimilarity());
									match.setExtension(lookupHabitat(ar));
									potentialMatches.add(match);
								}
							} else {
								logger.debug("im.next() was null");
							}
						} 
						logger.debug("Fuzzy Matches: " + potentialMatches.size());
						if (potentialMatches.size()==1) { 
							result = potentialMatches.get(0);
							String authorComparison = authorNameComparator.compare(taxonNameToValidate.getAuthorship(), result.getAuthorship()).getMatchType();
							result.setMatchDescription(NameComparison.MATCH_FUZZY_SCINAME + "; authorship " + authorComparison);
							result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
							result.setOriginalScientificName(taxonNameToValidate.getScientificName());
							result.setInputDbPK(taxonNameToValidate.getInputDbPK());
						}
					} // iterator over input names, should be just one.
			    } else {
			    	logger.error("Fuzzy match query returned null instead of a result set.");
			    }
			}
		} catch (ApiException e) {
			if (e.getMessage().equals("Connection timed out")) { 
				logger.error(e.getMessage() + " " + taxonNameToValidate.getScientificName() + " " + taxonNameToValidate.getInputDbPK());
			} else if (e.getCause()!=null && e.getCause().getClass().equals(UnknownHostException.class)) { 
				logger.error("Connection Probably Lost.  UnknownHostException: "+ e.getMessage());
			} else {
				logger.error(e.getMessage(), e);
			}
			if (depth<=MAX_RETRIES) {
				// Try again, up to MAX_RETRIES times.
				result = this.validate(taxonNameToValidate);
			} else { 
				depth = 1;
				throw new ServiceException(e.getMessage());
			}
		}
		depth--;
		return result;		
	}

	@Override
	public List<String> supportedExtensionTerms() {
	    List<String> terms = new ArrayList<String>();
	    terms.add("brackish");
	    terms.add("freshwater");
	    terms.add("marine");
	    terms.add("terrestrial");
	    terms.add("extinct");
		return terms;
	}
	
	protected Map<String,String> lookupHabitat(AphiaRecord ar) throws ApiException { 
		Map<String,String> attributes = new HashMap<String,String>();
		if (ar!=null)  {
			AphiaRecord wormsRecord = irmngService.aphiaRecordByAphiaID(ar.getAphiaID());
			if (wormsRecord.isIsBrackish()==null) { 
				attributes.put("brackish", "");
			} else { 
				attributes.put("brackish", wormsRecord.isIsBrackish().toString());
			}
			if (wormsRecord.isIsFreshwater()==null) { 
				attributes.put("freshwater", "");
			} else { 
				attributes.put("freshwater", wormsRecord.isIsFreshwater().toString());
			}
			if (wormsRecord.isIsMarine()==null) { 
				attributes.put("marine", "");
			} else { 
				attributes.put("marine", wormsRecord.isIsMarine().toString());
			}
			if (wormsRecord.isIsTerrestrial()==null) { 
				attributes.put("terrestrial", "");
			} else { 
				attributes.put("terrestrial", wormsRecord.isIsTerrestrial().toString());
			}
			if (wormsRecord.isIsExtinct()==null) { 
				attributes.put("extinct", "");
			} else { 
				attributes.put("extinct", wormsRecord.isIsExtinct().toString());
			}
			Iterator<String> ia = attributes.keySet().iterator();
			while (ia.hasNext()) { 
				String key = ia.next();
				logger.debug(key + " " + attributes.get(key));
			}
		}
		return attributes;
	}

	
}
