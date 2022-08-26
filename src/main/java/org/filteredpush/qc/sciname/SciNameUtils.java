/** 
 * SciNameUtils.java 
 * 
 * Copyright 2016 President and Fellows of Harvard College
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


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.GBIFService;
import org.filteredpush.qc.sciname.services.GNIService;
import org.filteredpush.qc.sciname.services.Validator;
import org.filteredpush.qc.sciname.services.WoRMSService;
import org.filteredpush.qc.sciname.services.ZooBankService;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.NameParser;
import org.gbif.nameparser.api.NameType;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.Rank;
import org.gbif.nameparser.api.UnparsableNameException;
import org.marinespecies.aphia.v1_0.handler.ApiException;

import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameAuthorshipParse;
import edu.harvard.mcz.nametools.NameUsage;

/**
 * Utility functions for working with DarwinCore scientific name terms.
 * 
 * @author mole
 *
 */
public class SciNameUtils {

	private static final Log logger = LogFactory.getLog(SciNameUtils.class);
	
	public SciNameUtils() { 
		
	} 
	
	/**
	 * Check to see if a name is the same as another name or the other name's synonyms.
	 * 
	 * @param name the name being checked
	 * @param compareToName the name returned from the authority to compare with name, including synonyms
	 * @param atRank the rank of the two names being compared
	 * @param sourceAuthority in which to lookup compareToName for synonyms.
	 * @return true if either name is empty, the two are the same, or if a synonym of compareToName found in sourceAuthority is the same as name. Otherwise false.
	 * @throws IOException on lookup error
	 * @throws ApiException on lookup error
	 */
	public static boolean sameOrSynoym(String name, String compareToName, String atRank, SciNameSourceAuthority sourceAuthority) throws IOException, ApiException {
		logger.debug(name);
		logger.debug(compareToName);
		boolean result = false;
		if (SciNameUtils.isEmpty(name) || SciNameUtils.isEmpty(compareToName)) { 
			result = true;
		} else if (name.equalsIgnoreCase(compareToName)) { 
			result = true;
		} else { 
			List<NameUsage> lookupResults = null;
			List<NameUsage> synonymResults = null;
			if (sourceAuthority.isGBIFChecklist()) { 
				lookupResults = GBIFService.lookupTaxonAtRank(compareToName, sourceAuthority.getAuthoritySubDataset(), atRank, 50);
			} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) { 
				lookupResults = WoRMSService.lookupTaxonAtRank(compareToName, atRank);
			}
			if (lookupResults!=null) { 
				Iterator<NameUsage> i = lookupResults.iterator();
				while (i.hasNext()) { 
					NameUsage value = i.next();
					logger.debug(value.getCanonicalName());
					if (value.getCanonicalName().equals(name)) { 
						result = true;
					} else { 
						if (sourceAuthority.isGBIFChecklist()) { 
							synonymResults= GBIFService.parseAllNameUsagesFromJSON(GBIFService.fetchSynonyms(value.getKey(), sourceAuthority.getAuthoritySubDataset()));
						} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
							// TODO: needs implementation
						}
						if (synonymResults!=null) { 
							logger.debug(synonymResults.size());
							Iterator<NameUsage> is = synonymResults.iterator();
							while (is.hasNext() && !result) { 
								NameUsage synValue = is.next();
								logger.debug(synValue.getCanonicalName());
								if (synValue.getCanonicalName().equals(name)) { 
									result = true;
								}
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	
    /**
     * Does a string contain a non-blank value.
     * 
     * @param aString to check
     * @return true if the string is null, is an empty string, 
     *     or contains only whitespace.
     */
    public static boolean isEmpty(String aString)  {
    	boolean result = true;
    	if (aString != null && aString.trim().length()>0) { 
    		// TG2, do not consider string representations of NULL as null, consider as data.
    		//if (!aString.trim().toUpperCase().equals("NULL")) { 
    		   result = false;
    		//}
    	}
    	return result;
    }
    
    /**
     * Test to see if two strings are the same or are not comparable, 
     * returns true if either or both strings are null, returns true if both
     * strings have a value and are equal, returns false only if both strings
     * are non-null and have different values, note that an empty string
     * is not treated as a null.
     * @param aString a string to compare
     * @param anotherString a string to compare
     * @return false if both strings are non-null and have different values, otherwise true.
     */
    public static boolean isEqualOrNonEmpty(String aString, String anotherString) { 
    	boolean result;
    	if (aString==null || anotherString==null) {
    		// if either string is null, can't make comparison, return true
    		result = true;
    	} else { 
    		// both strings have a value
    		result = aString.equals(anotherString);
    	}
    	return result;
    }
    
    /**
     * Test to see if two strings are the same or are not comparable, 
     * returns true if either or both strings are null or empty, returns true if both
     * strings have a value and are equal, returns false only if both strings
     * are non-null or empty and have different values, an empty string
     * is treated as a null.
     * @param aString a string to compare
     * @param anotherString a string to compare
     * @return false if both strings are non-null and have different values, otherwise true.
     * @see isEqualOrNonEmpty
     */
    public static boolean isEqualOrNonEmptyES(String aString, String anotherString) { 
    	boolean result;
    	if (aString==null || anotherString==null) {
    		// if either string is null, can't make comparison, return true
    		result = true;
    	} else if (aString.equals("") || anotherString.equals("")) {
    		// if either string is empty, can't make comparison, return true
    		result = true;
    	} else { 
    		// both strings have a value
    		result = aString.equals(anotherString);
    	}
    	return result;
    }
	
	public static String simpleWoRMSGuidLookup(String scientificName, String scientificNameAuthorship) { 
		String result = "";
		NameParserGBIF parser = new NameParserGBIF();
		logger.debug(scientificName);
		logger.debug(scientificNameAuthorship);
		try {
			ParsedName parse = parser.parse(scientificName,null,null);
			//String wormsGuid = WoRMSService.simpleNameSearch(parse.canonized(true).get(),scientificNameAuthorship,true);
			String wormsGuid = WoRMSService.simpleNameSearch(parse.canonicalName(),scientificNameAuthorship,true);
			result = wormsGuid;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.debug(result);
		try { 
			parser.close();
		} catch (Exception e) { 
			logger.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	public static String simpleGBIFGuidLookup(String scientificName, String scientificNameAuthorship) { 
		String result = "";
		NameParserGBIF parser = new NameParserGBIF();
		try {
			ParsedName parse = parser.parse(scientificName,null,null);
			GBIFService service = new GBIFService();
			NameUsage nameToTest = new NameUsage();
			nameToTest.setCanonicalName(parse.canonicalName());
			nameToTest.setOriginalAuthorship(scientificNameAuthorship);
			nameToTest.setOriginalScientificName(scientificName);
			if (service.nameSearchAgainstServices(nameToTest)) { 
			   String gbifGuid = service.getValidatedNameUsage().getGuid();
			   result = gbifGuid;
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		try { 
			parser.close();
		} catch (Exception e) { 
			logger.error(e.getMessage(), e);
		}		
		
		return result;
	}	
	

	public static void main(String[] args) { 
		
		CommandLineParser parser = new DefaultParser();
		
		Options options = new Options();
		options.addOption( "f", "file", true, "Input csv file from which to lookup names.  Assumes a csv file, first three columns being dbpk, scientificname, authorship, (TODO: family), columns after the third are ignored. " );
		options.addOption("o","output", true, "Output file into which to write results of lookup, default output.csv");
		options.addOption("s","service", true, "Service to lookup names against  WoRMS, GBIF_BACKBONE, GBIF_ITIS, GBIF_FAUNA_EUROPEA, GBIF_UKSI, GBIF_IPNI, GBIF_INDEXFUNGORUM, GBIF_COL, GBIF_PALEOBIOLOGYDB, or ZooBank (TODO: WoRMS+ZooBank). ");
		options.addOption("t","test", false, "Test connectivity with an example name");
		options.addOption("h","help", false, "Print this message");
		
		try {
			CommandLine cmd = parser.parse( options, args);
			
			if (cmd.hasOption("test")) { 
				doTest();
			}
			if (cmd.hasOption("help")) { 
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "SciNameUtils", options );
			}
			if (cmd.hasOption("file")) { 
				String infile = cmd.getOptionValue("file");
				logger.debug(infile);
				String outfile = "output.csv";
				if (cmd.hasOption("output")) { 
					outfile = cmd.getOptionValue("output");
					logger.debug(outfile);
				}
				String targetService = "WoRMS";
				if (cmd.hasOption("service")) {
					targetService = cmd.getOptionValue("service");
				}
					
				Validator validator = null;	
				switch (targetService.toLowerCase()) { 
				case "worms":
					validator = new WoRMSService(true);
					break;
				case "gbif_backbone":
					validator = new GBIFService(GBIFService.KEY_GBIFBACKBONE);
					break;
				case "gbif_itis":
					validator = new GBIFService(GBIFService.KEY_ITIS);
					break;
				case "gbif_fauna_europaea":
					validator = new GBIFService(GBIFService.KEY_ITIS);
					break;
				case "gbif_uksi":
					validator = new GBIFService(GBIFService.KEY_UKSI);
					break;
				case "gbif_ipni":
					validator = new GBIFService(GBIFService.KEY_IPNI);
					break;
				case "gbif_indexfungorum":
					validator = new GBIFService(GBIFService.KEY_INDEXFUNGORUM);
					break;
				case "gbif_col":
					validator = new GBIFService(GBIFService.KEY_COL);
					break;
				case "gbif_paleobiologydb":
					validator = new GBIFService(GBIFService.KEY_PALEIOBIOLOGY_DATABASE);
					break;
				case "zoobank":
					validator = new ZooBankService();
					break;
				default: 
					throw new SourceAuthorityException("Unknown or unsupported service: [" + targetService + "]");
				}
				
				BatchRunner runner = new BatchRunner(infile, outfile, validator);
				runner.runBatch();
			}
		} catch (ParseException e1) {
			logger.error(e1);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "SciNameUtils", options );
		} catch (FileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (SourceAuthorityException e) {
			logger.error(e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "SciNameUtils", options );
		}
		

	}

	private static void doTest() { 
		System.out.println(simpleWoRMSGuidLookup("Buccinum canetae","Clench & Aguayo"));
		
		LookupResult comparison;
		try {
			comparison = WoRMSService.nameComparisonSearch("Buccinum canetae","Clench & Aguayo", false);
			System.out.println(comparison.getMatchedName());
			System.out.println(comparison.getNameComparison().getMatchType());
			System.out.println(comparison.getNameComparison().getSimilarity());
			//System.out.println(comparison.getNameComparison().getMatchSeverity());
			//System.out.println(comparison.getNameComparison().getRemark());
			System.out.println(comparison.getMatchedAuthorship());
			System.out.println(comparison.getGuid());
		} catch (Exception e) {
			logger.error(e);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println(simpleGBIFGuidLookup("Buccinum canetae","Clench & Aguayo"));
	}
	
	/**
	 * Attempt to parse the authorship and the canonical name (name without authorship) out of a scientific name
	 * by first checking for matches in GNI where the parse is known, and if not found, failing over to using
	 * GBIF's name parser code.
	 * 
	 * @param scientificName the string to attempt to parse
	 * @return a NameAuthorshipParse object containing the separate canonical name and authorship string parts 
	 *    of the provided scientific name
	 * @throws UnparsableNameException if unable to parse
	 */
	public static NameAuthorshipParse getNameWithoutAuthorship(String scientificName) throws UnparsableNameException {
		NameAuthorshipParse result = null;

		boolean parsed = false;
		try {
			NameAuthorshipParse gniLookup = GNIService.obtainNameAuthorParse(scientificName);
			if (gniLookup!=null) { 
				result = gniLookup;
				parsed = true;
			}
		} catch (IOException e) {
			logger.debug(e.getMessage(),e);
		} catch (org.json.simple.parser.ParseException e) {
			logger.debug(e.getMessage(),e);
		}

		if (!parsed) { 
			NameParser parser = new NameParserGBIF();
			ParsedName parsedName = parser.parse(scientificName, Rank.UNRANKED, null);
			result = new NameAuthorshipParse();
			result.setNameWithAuthorship(scientificName);
			result.setNameWithoutAuthorship(parsedName.canonicalNameWithoutAuthorship());
			result.setAuthorship(parsedName.authorshipComplete());
			logger.debug(parsedName.canonicalNameWithoutAuthorship());
			logger.debug(parsedName.authorshipComplete());
			try {
				parser.close();
				parsed = true;
			} catch (Exception e) {
				logger.debug(e);
			}
		}
		if (!parsed) {
			throw new UnparsableNameException(NameType.SCIENTIFIC, scientificName, "Unable to parse authorship out of scientific name");
		}

		return result;
	}
	
}