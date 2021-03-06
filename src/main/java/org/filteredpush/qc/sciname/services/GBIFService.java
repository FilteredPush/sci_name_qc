/**
 * GBIFService.java 
 * 
 * Copyright 2015 President and Fellows of Harvard College
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
 * 
 */
package org.filteredpush.qc.sciname.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ICNafpAuthorNameComparator;
import edu.harvard.mcz.nametools.ICZNAuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

/**
 * Wrapper for accessing the GBIF API to search for scientific names. 
 * 
 * See service documentation at: http://dev.gbif.org/wiki/display/POR/Webservice+API#WebserviceAPI-ChecklistBankServices:Nameusage
 * 
 * @author mole
 *
 */
public class GBIFService {
	
	private static final Log logger = LogFactory.getLog(GBIFService.class);
	
	public static final String GBIF_SERVICE = "http://api.gbif.org/v1";
	
	public static final String KEY_GBIFBACKBONE = "d7dddbf4-2cf0-4f39-9b2a-bb099caae36c";
	public static final String KEY_IPNI = "046bbc50-cae2-47ff-aa43-729fbf53f7c5";
	public static final String KEY_INDEXFUNGORUM = "bf3db7c9-5e5d-4fd0-bd5b-94539eaf9598";
	public static final String KEY_COL = "7ddf754f-d193-4cc9-b351-99906754a03b";
	
	private NameUsage validatedNameUsage = null;
	

	
	protected String targetKey;
	protected String targetDataSetName; 
	protected boolean fetchSynonymsAboveSpecies;

	public GBIFService()  throws IOException  { 
		super();
		targetKey = GBIFService.KEY_GBIFBACKBONE;
		targetDataSetName = "GBIF Backbone Taxonomy";
		fetchSynonymsAboveSpecies = true;
		initSciName();
		test();
	}
	
	public GBIFService(String targetKey)  throws IOException  { 
		this.targetKey = targetKey;
		fetchSynonymsAboveSpecies = true;
		//TODO: Lookup title for dataset from targetKey (and check that datset exists).
		targetDataSetName = "GBIF Dataset" + targetKey; 
		if (targetKey.equals(KEY_IPNI)) { 
		    targetDataSetName = "GBIF IPNI Dataset"; 
		}
		if (targetKey.equals(KEY_COL)) { 
		    targetDataSetName = "GBIF COL Dataset"; 
		}
		if (targetKey.equals(KEY_INDEXFUNGORUM)) { 
		    targetDataSetName = "GBIF IndexFungorum Dataset"; 
		}
		initSciName();
		test();
	}
	
	
	protected void initSciName() { 
		validatedNameUsage = new NameUsage("GBIF",new ICZNAuthorNameComparator(.75d, .5d));
	}
	
	protected void test() throws IOException  { 
		// check that the service is up.
		GBIFService.searchForGenus("Murex", GBIFService.KEY_GBIFBACKBONE,1);
	}	

	protected String getServiceImplementationName() {
		return targetDataSetName;
	}
	
	public static String fetchTaxon(String taxon, String targetChecklist) { 
		StringBuilder result = new StringBuilder();
		String datasetKey = "";
		if (targetChecklist!=null) { 
			datasetKey = "datasetKey=" + targetChecklist;
		}
		URL url;
		try {
			//url = new URL(GBIF_SERVICE + "/name_usage/" + taxon + "?limit=100&" + datasetKey);
			url = new URL(GBIF_SERVICE + "/species/?name=" + taxon + "?limit=100&" + datasetKey);
			URLConnection connection = url.openConnection();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}


		return result.toString();
	}
	
	public static String searchForTaxon(String name, String targetChecklist) { 
		StringBuilder result = new StringBuilder();
		String datasetKey = "";
		if (targetChecklist!=null) { 
			datasetKey = "datasetKey=" + targetChecklist;
		}
		URL url;
		try {
			//url = new URL(GBIF_SERVICE + "/name_usage/search?q=" + name + "&limit=100&" + datasetKey);
			url = new URL(GBIF_SERVICE + "/species/?name=" + URLEncoder.encode(name,"UTF-8") + "&limit=100&" + datasetKey);
			URLConnection connection = url.openConnection();
			logger.debug(url.toString());
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return result.toString();
	}
	
	public static String searchForSpecies(String name, String targetChecklist) throws IOException { 
		StringBuilder result = new StringBuilder();
		String datasetKey = "";
		if (targetChecklist!=null) { 
			datasetKey = "datasetKey=" + targetChecklist;
		}
		URL url;
			//url = new URL(GBIF_SERVICE + "/name_usage/search?q=" + URLEncoder.encode(name,"UTF-8") + "&rank=SPECIES&limit=100&" + datasetKey);
			url = new URL(GBIF_SERVICE + "/species/search?q=" + URLEncoder.encode(name,"UTF-8") + "&rank=SPECIES&limit=100&" + datasetKey);
			logger.debug(url.toString());
			URLConnection connection = url.openConnection();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				result.append(line);
				logger.debug(line);
			}
		return result.toString();
	}	
	
	public static String searchForGenus(String name, String targetChecklist, int limit) throws IOException { 
		StringBuilder result = new StringBuilder();
		String datasetKey = "";
		if (targetChecklist!=null) { 
			datasetKey = "datasetKey=" + targetChecklist;
		}
		URL url;
			url = new URL(GBIF_SERVICE + "/species/search?q=" + URLEncoder.encode(name,"UTF-8") + "&rank=GENUS&limit=" + Integer.toString(limit) + "&" + datasetKey);
			logger.debug(url.toString());
			URLConnection connection = url.openConnection();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				result.append(line);
				logger.debug(line);
			}
		return result.toString();
	}	
	
	public static String fetchSynonyms(int taxonId, String targetChecklist) { 
		StringBuilder result = new StringBuilder();
		String datasetKey = "";
		if (targetChecklist!=null) { 
			datasetKey = "datasetKey=" + targetChecklist;
		}
		URL url;
		try {
			//url = new URL(GBIF_SERVICE + "/name_usage/" + taxonId + "/synonyms?limit=1000&" + datasetKey);
			url = new URL(GBIF_SERVICE + "/species/" + taxonId + "/synonyms?limit=1000&" + datasetKey);
			URLConnection connection = url.openConnection();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}


		return result.toString();
	}		
	
	public static List<NameUsage> parseAllNameUsagesFromJSON(String json) {
		boolean gotAll = true;
		ArrayList<NameUsage> result = new ArrayList<NameUsage>(); 
        JSONParser parser=new JSONParser();

        try {
        	JSONArray array = new JSONArray();
        	try {
        	    JSONObject o = (JSONObject)parser.parse(json);
			    array = (JSONArray)o.get("results");
			    //System.out.println(o.get("offset"));
			    //System.out.println(o.get("limit"));
			    //System.out.println(o.get("endOfRecords"));
			    //System.out.println(o.get("count"));
			    if (o.get("endOfRecords").equals("false")) { 
			    	gotAll = false;
			    }
        	} catch (ClassCastException e) { 
    			array = (JSONArray)parser.parse(json);
        	}
    			 
			Iterator i = array.iterator();
			while (i.hasNext()) { 
				JSONObject obj = (JSONObject)i.next();
				NameUsage name = new NameUsage(obj);
				result.add(name);
			}
 
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		// TODO: Report not getting all records.
		if (!gotAll) { 
			logger.error("Incomplete Harvest");
			System.out.println("Incomplete Harvest");
		}
		
		return result;
	}	
	
	public static NameUsage parseNameUsageFromJSON(String targetName, String json) { 
        JSONParser parser=new JSONParser();

        try {
        	JSONArray array = new JSONArray();
        	try {
        	    JSONObject o = (JSONObject)parser.parse(json);
        	    if (o.get("results")!=null) {  
			        array = (JSONArray)o.get("results");
        	    } else { 
        	    	// array = (JSONArray)parser.parse(json);
        	    }
        	} catch (ClassCastException e) { 
        		logger.debug(e.getMessage(),e);
    			array = (JSONArray)parser.parse(json);
        	}
    			 
			Iterator<JSONObject> i = array.iterator();
			while (i.hasNext()) { 
				JSONObject obj = i.next();
				// System.out.println(obj.toJSONString());
				NameUsage name = new NameUsage(obj);
				if (name.getCanonicalName().equals(targetName)) { 
					return name;
				}
			}
 
		} catch (ParseException e) {
        	logger.debug(e.getMessage(),e);
		}
		return null;
	}


	@SuppressWarnings("static-access")
	public boolean nameSearchAgainstServices(NameUsage toCheck) {
		boolean result = false;
		if (toCheck!=null) {
			// TODO: Handle autonyms of botanical names (should not have an authorship).
			String taxonName = toCheck.getCanonicalName();
			if (taxonName==null || taxonName.length()==0) { 
				taxonName = toCheck.getOriginalScientificName();
				toCheck.setCanonicalName(taxonName);
			}
			String authorship = toCheck.getOriginalAuthorship();
			List<NameUsage> hits = GBIFService.parseAllNameUsagesFromJSON(GBIFService.searchForTaxon(taxonName, targetKey));
			if (hits==null || hits.size()==0) { 
				// no matches
				result = false;
			} else if (hits.size()==1) { 
				Iterator<NameUsage> i = hits.iterator();
				// One possible match
				NameUsage potentialMatch = i.next();
				AuthorNameComparator authorNameComparator = new ICNafpAuthorNameComparator(.70d,.5d);
				if (potentialMatch.getKingdom().equals("Animalia")) { 
					authorNameComparator = new ICZNAuthorNameComparator(.75d,.5d);
				}
				if (potentialMatch.getCanonicalName().equals(taxonName) && potentialMatch.getAuthorship().equals(authorship)) { 
					potentialMatch.setMatchDescription(NameComparison.MATCH_EXACT);
					validatedNameUsage = potentialMatch;
					validatedNameUsage.setAuthorshipStringEditDistance(1d);
			        addToComment("Exact match found in found in " + targetDataSetName + ".");
				} else { 
					NameComparison authorComparison = authorNameComparator.compare(authorship, potentialMatch.getAuthorship());
					double similarity = authorComparison.getSimilarity();
					String match = authorComparison.getMatchType();
					logger.debug(authorship);
					logger.debug(potentialMatch.getAuthorship());
					logger.debug(similarity);
					potentialMatch.setMatchDescription(match);
					validatedNameUsage = potentialMatch;
					validatedNameUsage.setAuthorshipStringEditDistance(similarity);
			        addToComment("Potential match found in found in " + targetDataSetName + ". " + validatedNameUsage.getMatchDescription());
				}
				validatedNameUsage.setInputDbPK(toCheck.getInputDbPK());
				validatedNameUsage.setScientificNameStringEditDistance(1d);
				validatedNameUsage.setOriginalAuthorship(toCheck.getOriginalAuthorship());
				validatedNameUsage.setOriginalScientificName(toCheck.getCanonicalName());
				if(validatedNameUsage.getKingdom()==null) { validatedNameUsage.setKingdom(potentialMatch.getKingdom()); }
				if(validatedNameUsage.getPhylum()==null) { validatedNameUsage.setPhylum(potentialMatch.getPhylum()); } 
				if(validatedNameUsage.getClazz()==null) { validatedNameUsage.setClazz(potentialMatch.getClazz()); }
				if(validatedNameUsage.getOrder()==null) { validatedNameUsage.setOrder(potentialMatch.getOrder()); }
				if(validatedNameUsage.getFamily()==null) { validatedNameUsage.setFamily(potentialMatch.getFamily()); }
				
				result = true;
			} else { 
				// multiple possible matches
				Iterator<NameUsage> i = hits.iterator();
				logger.debug("More than one match: " + hits.size());
				boolean exactMatch = false;
				List<NameUsage> matches = new ArrayList<NameUsage>();
				while (i.hasNext() && !exactMatch) { 
					NameUsage potentialMatch = i.next();
					matches.add(potentialMatch);
					logger.debug(potentialMatch.getScientificName());
					logger.debug(potentialMatch.getCanonicalName());
					logger.debug(potentialMatch.getKey());
					logger.debug(potentialMatch.getAuthorship());
					logger.debug(potentialMatch.getTaxonomicStatus());
					if (potentialMatch.getScientificName().equals(taxonName)) {
						if (potentialMatch.getAuthorship().equals(authorship)) {
							// If one of the results is an exact match on scientific name and authorship, pick that one. 
							validatedNameUsage = potentialMatch;
							validatedNameUsage.setInputDbPK(toCheck.getInputDbPK());
							validatedNameUsage.setMatchDescription(NameComparison.MATCH_EXACT);
							validatedNameUsage.setAuthorshipStringEditDistance(1d);
							validatedNameUsage.setOriginalAuthorship(toCheck.getOriginalAuthorship());
							validatedNameUsage.setOriginalScientificName(toCheck.getCanonicalName());
							validatedNameUsage.setScientificNameStringEditDistance(1d);
			                addToComment("Exact match found in multiple results found in " + targetDataSetName + ".  There may be homonyms. " + validatedNameUsage.getScientificName() + " " + validatedNameUsage.getAuthorship() + " " + validatedNameUsage.getMatchDescription());
							exactMatch = true;
							result = true;
						}
					}
				}
				if (!exactMatch) {
					if (matches.size()>1 && toCheck.getOriginalAuthorship().trim().length()==0) { 
						// There are multiple matches in GBIF, and we don't have an authorship string to 
						// disambiguate amongst them.
			            addToComment("Multiple results found in " + targetDataSetName + ".  A homonym or hemihomonym could exist, and authorship was not provided.");
					    Iterator<NameUsage> im = matches.iterator();
					    while (im.hasNext()) {
							NameUsage current = im.next();
					    	addToComment(current.getScientificName() + " " + current.getAuthorship());
					    }
					    result = false;
					} else { 
					// If we didn't find an exact match on scientific name and authorship in the list, pick the 
					// closest authorship and list all of the potential matches.  
					Iterator<NameUsage> im = matches.iterator();
					if (im.hasNext()) { 
						NameUsage closest = im.next();
						StringBuffer names = new StringBuffer();
						names.append(closest.getScientificName()).append(" ").append(closest.getAuthorship()).append(" ").append(closest.getUnacceptReason()).append(" ").append(closest.getTaxonomicStatus());
						while (im.hasNext()) { 
							NameUsage current = im.next();
							names.append("; ").append(current.getScientificName()).append(" ").append(current.getAuthorship()).append(" ").append(current.getUnacceptReason()).append(" ").append(current.getTaxonomicStatus());
							if (toCheck.getAuthorComparator().calulateSimilarityOfAuthor(closest.getAuthorship(), authorship) < toCheck.getAuthorComparator().calulateSimilarityOfAuthor(current.getAuthorship(), authorship)) { 
								closest = current;
							}
						}
						validatedNameUsage = closest;
						validatedNameUsage.setInputDbPK(toCheck.getInputDbPK());
						validatedNameUsage.setMatchDescription(NameComparison.MATCH_MULTIPLE + " " + names.toString());
						validatedNameUsage.setOriginalAuthorship(toCheck.getOriginalAuthorship());
						validatedNameUsage.setOriginalScientificName(toCheck.getCanonicalName());
						validatedNameUsage.setScientificNameStringEditDistance(1d);
						validatedNameUsage.setAuthorshipStringEditDistance(toCheck.getAuthorComparator().calulateSimilarityOfAuthor(toCheck.getAuthorship(), validatedNameUsage.getAuthorship()));
			            addToComment("Plausible match in multiple results found in " + targetDataSetName + ".  The result could incorrectly be a homonym of the desired name. " + validatedNameUsage.getScientificName() + " " + validatedNameUsage.getAuthorship() + " " + validatedNameUsage.getMatchDescription());
						result = true;
					}
					}
				}
			}
		}
		if (validatedNameUsage!=null) { 
			// GBIF includes the authorship in the scientific name.
			// result.setScientificName(result.getScientificName().replaceAll(result.getAuthorship() + "$", ""));
			validatedNameUsage.setScientificName(validatedNameUsage.getCanonicalName());
			// set a guid for the gbif records
			validatedNameUsage.setGuid("http://api.gbif.org/v1/species/" + Integer.toString(validatedNameUsage.getKey()));
		}
		if (!result) { 
			addToComment("No match found in " + targetDataSetName + ".");
		}
		return result;
	}
	
	private StringBuffer comments;

	public void addToComment(String comment) {
		logger.debug(comment);
		if (comments==null) { 
			comments = new StringBuffer();
			comments.append(comment);
		} else { 
			comments.append("|").append(comment);
		}
	}
	
	public NameUsage getValidatedNameUsage() { 
		return validatedNameUsage;
	}	
	
}
