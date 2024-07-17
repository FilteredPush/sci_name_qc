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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.filteredpush.qc.sciname.services.IRMNGService;
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
 * @version $Id: $Id
 */
public class SciNameUtils {

	private static final Log logger = LogFactory.getLog(SciNameUtils.class);
	
	/**
	 * <p>Constructor for SciNameUtils.</p>
	 */
	public SciNameUtils() { 
		
	} 
	
	/**
	 * Obtain a list of taxonomic rank names, ordered from highest to lowest rank, such that
	 * indexOf("kingdom") will return an index smaller than indexOf("phylum")
	 * 
	 * @return a list of rank names in lower case
	 */
	public static final List<String> getRankListLC() { 
		final LinkedList<String> values = new LinkedList<String>();
		values.addLast("domain");
		values.addLast("kingdom");
		values.addLast("subkingdom");
		values.addLast("superphylum");
		values.addLast("phylum");
		values.addLast("subphylum");
		values.addLast("superclass");
		values.addLast("class");
		values.addLast("subclass");
		values.addLast("supercohort");
		values.addLast("cohort");
		values.addLast("subcohort");
		values.addLast("superorder");
		values.addLast("order");
		values.addLast("suborder");
		values.addLast("infraorder");
		values.addLast("superfamily");
		values.addLast("family");
		values.addLast("subfamily");
		values.addLast("tribe");
		values.addLast("subtribe");
		values.addLast("genus");
		values.addLast("subgenus");
		values.addLast("section");
		values.addLast("subsection");
		values.addLast("series");
		values.addLast("subseries");
		values.addLast("speciesAggregate");
		values.addLast("species");
		values.addLast("subspecificAggregate");
		values.addLast("subspecies");
		values.addLast("variety");
		values.addLast("subvariety");
		values.addLast("form");
		values.addLast("subform");
		values.addLast("cultivarGroup");
		values.addLast("cultivar");
		values.addLast("strain");
		return values;
	}
	
	/**
	 * Test to see if one taxonomic rank string represents the same or higher rank than 
	 * an other.  Case insensitive, and ignores any leading/trailing whitespace.
	 * 
	 * @param lowerRank to evaluate as the lower taxon rank
	 * @param higherRank to evaluate as the higher taxon rank
	 * @return null if either lowerRank or higherRank are not found in the list of ranks, 
	 *    true if lowerRank is at the same or a lower taxonomic rank than higherRank, 
	 *    false if higherRank is below lowerRank in taxonomic rank, for example
	 *    given lowerRank = genus and higherRank = kingdom will return true.
	 */
	public static Boolean isRankAtOrAbove(String lowerRank, String higherRank) { 
		Boolean result = null;
		lowerRank = lowerRank.trim().toLowerCase();
		higherRank = higherRank.trim().toLowerCase();
		List ranks = getRankListLC();
		if (ranks.contains(lowerRank) && ranks.contains(higherRank)) { 
			if (lowerRank.equals(higherRank)) { 
				result = true;
			} else { 
				result = ranks.indexOf(lowerRank) > ranks.indexOf(higherRank);
			}
		}
		return result;
	}
	
	/**
	 * Test to see if one taxonomic rank string represents a higher rank than 
	 * an other.  Case insensitive, and ignores any leading/trailing whitespace.
	 * 
	 * @param lowerRank to evaluate as the lower taxon rank
	 * @param higherRank to evaluate as the higher taxon rank
	 * @return null if either lowerRank or higherRank are not found in the list of ranks, 
	 *    true if lowerRank is at a lower taxonomic rank than higherRank, 
	 *    false if higherRank is the same or below lowerRank in taxonomic rank, for example
	 *    given lowerRank = genus and higherRank = kingdom will return true.
	 */
	public static Boolean isRankAbove(String lowerRank, String higherRank) { 
		Boolean result = null;
		if (lowerRank!=null && higherRank!=null) { 
			lowerRank = lowerRank.trim().toLowerCase();
			higherRank = higherRank.trim().toLowerCase();
			List ranks = getRankListLC();
			if (ranks.contains(lowerRank) && ranks.contains(higherRank)) { 
				if (lowerRank.equals(higherRank)) { 
					result = true;
				} else { 
					result = ranks.indexOf(lowerRank) > ranks.indexOf(higherRank);
				}
			}
		}
		return result;
	}
	
	/**
	 * <p>validateTaxonID.</p>
	 *
	 * @param taxonID a {@link java.lang.String} object.
	 * @param sourceAuthority a {@link org.filteredpush.qc.sciname.SciNameSourceAuthority} object.
	 * @return a boolean.
	 * @throws org.filteredpush.qc.sciname.IDFormatException if any.
	 * @throws org.filteredpush.qc.sciname.UnsupportedSourceAuthorityException if any.
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException if any.
	 * @throws org.irmng.aphia.v1_0.handler.ApiException if any.
	 */
	public static boolean validateTaxonID(String taxonID, SciNameSourceAuthority sourceAuthority) throws IDFormatException, UnsupportedSourceAuthorityException, ApiException, org.irmng.aphia.v1_0.handler.ApiException { 
		boolean result = false;
		if (sourceAuthority.isGBIFChecklist()) { 
			String id = taxonID.replaceFirst("http[s]{0,1}://[wapi]{3}\\.gbif\\.org/[v1/]{0,3}species/", "");
			id = id.replace("https://www.gbif.org/species/", "");
			String matches = GBIFService.fetchTaxonByID(id, sourceAuthority.getAuthoritySubDataset());
        	List<NameUsage>matchList = GBIFService.parseAllNameUsagesFromJSON(matches);
        	if (matchList.size()==1) { 
        		logger.debug(matchList.get(0).getScientificName());
        		result = true;
        	}
		} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
			String id = taxonID.replace("urn:lsid:marinespecies.org:taxname:", "");
			NameUsage match = WoRMSService.lookupTaxonByID(id);
			if (match!=null) { 
				logger.debug(match.getScientificName());
				result=true;
			}
		} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) {
			String id = taxonID.replace("urn:lsid:irmng.org:taxname:", "");
			NameUsage match = IRMNGService.lookupTaxonByID(id);
			if (match!=null) { 
				logger.debug(match.getScientificName());
				result=true;
			}
		} else { 
			throw new UnsupportedSourceAuthorityException("Source Authority Not Implemented");
		} 
        	
		return result;
	}
	
	/**
	 * Check to see if a name is the same as another name or the other name's synonyms.
	 *
	 * @param name the name being checked
	 * @param compareToName the name returned from the authority to compare with name, including synonyms
	 * @param atRank the rank of the two names being compared
	 * @param sourceAuthority in which to lookup compareToName for synonyms.
	 * @return true if either name is empty, the two are the same, or if a synonym of compareToName found in sourceAuthority is the same as name. Otherwise false.
	 * @throws java.io.IOException on lookup error
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException on lookup error
	 * @throws org.irmng.aphia.v1_0.handler.ApiException if any.
	 */
	public static boolean sameOrSynonym(String name, String compareToName, String atRank, SciNameSourceAuthority sourceAuthority) throws IOException, ApiException, org.irmng.aphia.v1_0.handler.ApiException {
			return sameOrSynonym(compareToName, name, atRank, sourceAuthority, true);
	}
	
	private static boolean sameOrSynonym(String name, String compareToName, String atRank, SciNameSourceAuthority sourceAuthority, boolean tryReversed) throws IOException, ApiException, org.irmng.aphia.v1_0.handler.ApiException {
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
			} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) { 
				lookupResults = IRMNGService.lookupTaxonAtRank(compareToName, atRank);
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
		
		if (result==false && tryReversed) { 
			result = sameOrSynonym(compareToName, name, atRank, sourceAuthority, false);
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
     *
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
     *
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
	
	/**
	 * <p>simpleWoRMSGuidLookup.</p>
	 *
	 * @param scientificName a {@link java.lang.String} object.
	 * @param scientificNameAuthorship a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
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
	
	/**
	 * <p>simpleGBIFGuidLookup.</p>
	 *
	 * @param scientificName a {@link java.lang.String} object.
	 * @param scientificNameAuthorship a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
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
	

	/**
	 * Entry point for command line usage of SciNameUtils.
	 *
	 * @param args command line arguments, run with -h to list options.
	 */
	public static void main(String[] args) { 
		
		CommandLineParser parser = new DefaultParser();
		
		Options options = new Options();
		options.addOption( "f", "file", true, "Input csv file from which to lookup names.  Assumes a csv file, first three columns being dbpk, scientificName, authorship, (optionally: kingdom, family), columns after the third are ignored. " );
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
	 * @throws org.gbif.nameparser.api.UnparsableNameException if unable to parse
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
			ParsedName parsedName;
			try {
				parsedName = parser.parse(scientificName, Rank.UNRANKED, null);
				result = new NameAuthorshipParse();
				result.setNameWithAuthorship(scientificName);
				result.setNameWithoutAuthorship(parsedName.canonicalNameWithoutAuthorship());
				result.setAuthorship(parsedName.authorshipComplete());
				logger.debug(parsedName.canonicalNameWithoutAuthorship());
				logger.debug(parsedName.authorshipComplete());
			} catch (UnparsableNameException e1) {
				try {
					parser.close();
					parsed = true;
				} catch (Exception e) {
					logger.debug(e);
				}
				throw e1;
			} catch (InterruptedException e1) {
				logger.debug(e1.getMessage());
			}
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
	
	/**
	 * <p>isSameClassificationInAuthority.</p>
	 *
	 * @param kingdom a {@link java.lang.String} object.
	 * @param phylum a {@link java.lang.String} object.
	 * @param taxonomic_class a {@link java.lang.String} object.
	 * @param order a {@link java.lang.String} object.
	 * @param family a {@link java.lang.String} object.
	 * @param subfamily a {@link java.lang.String} object.
	 * @param genus a {@link java.lang.String} object.
	 * @param sourceAuthority a {@link org.filteredpush.qc.sciname.SciNameSourceAuthority} object.
	 * @return a {@link org.filteredpush.qc.sciname.BooleanWithComment} object.
	 * @throws java.io.IOException if any.
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException if any.
	 * @throws org.irmng.aphia.v1_0.handler.ApiException if any.
	 * @throws org.filteredpush.qc.sciname.UnsupportedSourceAuthorityException if any.
	 */
	public static BooleanWithComment isSameClassificationInAuthority(String kingdom, String phylum, String taxonomic_class, String order, String family, String subfamily, String genus, SciNameSourceAuthority sourceAuthority)
			throws IOException, ApiException, org.irmng.aphia.v1_0.handler.ApiException, UnsupportedSourceAuthorityException 
	{ 
		BooleanWithComment result = new BooleanWithComment(false, "");
		if (kingdom==null) { kingdom = ""; }
		if (phylum==null) { phylum = ""; }
		if (taxonomic_class==null) { taxonomic_class = ""; }
		if (order==null) { order = ""; }
		if (family==null) { family = ""; }
		if (subfamily==null) { subfamily = ""; }
		if (genus==null) { genus = ""; }
		
    	String lowestRankingTaxon = null;
    	String lowestRank = null;
    	if (!SciNameUtils.isEmpty(genus)) { 
    		lowestRankingTaxon = genus;
    		lowestRank = "Genus";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(subfamily)) { 
    		lowestRankingTaxon = subfamily;
    		lowestRank = "Subfamily";
    	}     	
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(family)) { 
    		lowestRankingTaxon = family;
    		lowestRank = "Family";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(order)) { 
    		lowestRankingTaxon = order;
    		lowestRank = "Order";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(taxonomic_class)) { 
    		lowestRankingTaxon = taxonomic_class;
    		lowestRank = "Class";

    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(phylum)) { 
    		lowestRankingTaxon = phylum;
    		lowestRank = "Phylum";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(kingdom)) { 
    		lowestRankingTaxon = kingdom;
    		lowestRank = "Kingdom";
    	} 
    	logger.debug(lowestRank);
    	logger.debug(lowestRankingTaxon);
		List<NameUsage> lookupResult = null;
		if (sourceAuthority.isGBIFChecklist()) { 
			lookupResult = GBIFService.lookupTaxonAtRank(lowestRankingTaxon, sourceAuthority.getAuthoritySubDataset(), lowestRank, 10);
		} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) { 
			lookupResult = WoRMSService.lookupTaxonAtRank(lowestRankingTaxon, lowestRank);
		} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) { 
			lookupResult = IRMNGService.lookupTaxonAtRank(lowestRankingTaxon, lowestRank);
		} else {
			throw new UnsupportedSourceAuthorityException("Authority " + sourceAuthority.getName() + " Not implemented.");
		}
		if (lookupResult!=null) { 
			logger.debug(lookupResult.size());
			if (lowestRank=="Kingdom") { 
				result.setBooleanValue(true);
				result.addComment("Matched Kingdom");
			} else { 
				Boolean hasKingdomMissmatch = null;
				Boolean hasKingdomMatch = null;
				Boolean hasPhylumMissmatch = null;
				Boolean hasPhylumMatch = null;
				Boolean hasClassMissmatch = null;
				Boolean hasClassMatch = null;
				Boolean hasOrderMissmatch = null;
				Boolean hasOrderMatch = null;
				Boolean hasFamilyMissmatch = null;
				Boolean hasFamilyMatch = null;
				String matchKingdom = null;
				String matchPhylum = null;
				String matchClass = null;
				String matchOrder = null;
				String matchFamily = null;
				Iterator<NameUsage> i = lookupResult.iterator();
				while (i.hasNext()) {
					NameUsage match = i.next();
					logger.debug(match.getCanonicalName());
					if (SciNameUtils.isEqualOrNonEmptyES(lowestRankingTaxon, match.getCanonicalName())) {
						matchKingdom = match.getKingdom();
						if (!SciNameUtils.isEmpty(matchKingdom) && !SciNameUtils.isEmpty(kingdom) && !kingdom.equals(matchKingdom)) { 
							hasKingdomMissmatch = true;
							logger.debug(matchKingdom);
						} else if (!SciNameUtils.isEmpty(matchKingdom) && !SciNameUtils.isEmpty(kingdom) && kingdom.equals(matchKingdom)) { 
							hasKingdomMatch = true;
						}
						if (lowestRank!="Kingdom" && lowestRank!="Phylum") { 
							matchPhylum = match.getPhylum();
							if (!SciNameUtils.isEmpty(matchPhylum) && !SciNameUtils.isEmpty(phylum) && !phylum.equals(matchPhylum)) { 
								hasPhylumMissmatch = true;
								logger.debug(matchPhylum);
							} else if (!SciNameUtils.isEmpty(matchPhylum) && !SciNameUtils.isEmpty(phylum) && phylum.equals(matchPhylum)) { 
								hasPhylumMatch = true;
							}
						}
						if (lowestRank!="Kingdom" && lowestRank!="Phylum" && lowestRank!="Class") { 
							matchClass = match.getClazz();
							if (!SciNameUtils.isEmpty(matchClass) && !SciNameUtils.isEmpty(taxonomic_class) && !taxonomic_class.equals(matchClass)) { 
								hasClassMissmatch = true;
								logger.debug(matchClass);
							} else if (!SciNameUtils.isEmpty(matchClass) && !SciNameUtils.isEmpty(taxonomic_class) && taxonomic_class.equals(matchClass)) { 
								hasClassMatch = true;
							}
						}
						if (lowestRank!="Kingdom" && lowestRank!="Phylum" && lowestRank!="Class" && lowestRank!="Order") { 
							matchOrder = match.getOrder();
							if (!SciNameUtils.isEmpty(matchOrder) && !SciNameUtils.isEmpty(order) && !order.equals(matchOrder)) { 
								hasOrderMissmatch = true;
								logger.debug(matchOrder);
							} else if (!SciNameUtils.isEmpty(matchOrder) && !SciNameUtils.isEmpty(order) && order.equals(matchOrder)) { 
								hasOrderMatch = true;
							}
						}
						if (lowestRank!="Kingdom" && lowestRank!="Phylum" && lowestRank!="Class" && lowestRank!="Order" && lowestRank!="Family") { 
							matchFamily = match.getFamily();
							if (!SciNameUtils.isEmpty(matchFamily) && !SciNameUtils.isEmpty(family) && !family.equals(matchFamily)) { 
								hasFamilyMissmatch = true;
								logger.debug(matchFamily);
							} else if (!SciNameUtils.isEmpty(matchFamily) && !SciNameUtils.isEmpty(family) && family.equals(matchFamily)) { 
								hasFamilyMatch = true;
							}
						}
						// no subfamily returned in match, can't check its parentage
					}
				}
				logger.debug(hasKingdomMatch);
				logger.debug(hasKingdomMissmatch);
				Boolean workUpTree = null;
				if (hasKingdomMatch!=null && hasKingdomMatch) { 
					// ok
					if (workUpTree==null) { 
						workUpTree = true;
					}
				} else if (hasKingdomMatch==null && hasKingdomMissmatch!=null && hasKingdomMissmatch) { 
					if (SciNameUtils.sameOrSynonym(kingdom, matchKingdom, "Kingdom", sourceAuthority)) { 
						result.addComment("Kingdom ["+ kingdom+"] matched synonym ["+matchKingdom+"] in " + sourceAuthority.getName());
						if (workUpTree==null) { workUpTree = true; }
					} else { 
						// failed
						result.setBooleanValue(false);
						result.addComment("Missmatched Kingdom for ["+kingdom+"] authority has ["+matchKingdom+"]");
						workUpTree = false;
					}
				}
				if (hasPhylumMatch!=null && hasPhylumMatch) { 
					// ok
					if (workUpTree==null) { 
						workUpTree = true;
					}
				} else if (hasPhylumMatch==null && hasPhylumMissmatch!=null && hasPhylumMissmatch) { 
					if (SciNameUtils.sameOrSynonym(phylum, matchPhylum, "Phylum", sourceAuthority)) { 
						result.addComment("Phylum ["+ phylum+"] matched synonym ["+matchPhylum+"] in " + sourceAuthority.getName());
						if (workUpTree==null) { workUpTree = true; }
					} else { 
						// failed
						result.setBooleanValue(false);
						result.addComment("Missmatched Phylum for ["+phylum+"] authority has ["+matchPhylum+"]");
						workUpTree = false;
					}
				}
				if (hasClassMatch!=null && hasClassMatch) { 
					// ok
					if (workUpTree==null) { 
						workUpTree = true;
					}
				} else if (hasClassMatch==null && hasClassMissmatch!=null && hasClassMissmatch) { 
					if (SciNameUtils.sameOrSynonym(taxonomic_class, matchClass, "Class", sourceAuthority)) { 
						result.addComment("Class ["+ taxonomic_class +"] matched synonym ["+matchClass+"] in " + sourceAuthority.getName());
						if (workUpTree==null) { workUpTree = true; }
					} else { 
						// failed
						result.setBooleanValue(false);
						result.addComment("Missmatched Class for ["+taxonomic_class+"] authority has ["+matchClass+"]");
						workUpTree = false;
					}
				}
				if (hasOrderMatch!=null && hasOrderMatch) { 
					// ok
					if (workUpTree==null) { 
						workUpTree = true;
					}
				} else if (hasOrderMatch==null && hasOrderMissmatch!=null && hasOrderMissmatch) { 
					if (SciNameUtils.sameOrSynonym(order, matchOrder, "Order", sourceAuthority)) { 
						result.addComment("Order ["+ order+"] matched synonym ["+matchOrder+"] in " + sourceAuthority.getName());
						if (workUpTree==null) { workUpTree = true; }
					} else { 
						// failed
						result.setBooleanValue(false);
						result.addComment("Missmatched Order for ["+order+"] authority has ["+matchOrder+"]");
						workUpTree = false;
					}
				}
				if (hasFamilyMatch!=null && hasFamilyMatch) { 
					// ok
					if (workUpTree==null) { 
						workUpTree = true;
					}
				} else if (hasFamilyMatch==null && hasFamilyMissmatch!=null && hasFamilyMissmatch) { 
					if (SciNameUtils.sameOrSynonym(family, matchFamily, "Family", sourceAuthority)) { 
						result.addComment("Family ["+ family+"] matched synonym ["+matchFamily+"] in " + sourceAuthority.getName());
						if (workUpTree==null) { workUpTree = true; }
					} else { 
						// failed
						result.setBooleanValue(false);
						result.addComment("Missmatched Family for ["+family+"] authority has ["+matchFamily+"]");
						workUpTree = false;
					}
				}
				logger.debug(result.getBooleanValue());
				logger.debug(result.getComment());
				logger.debug(workUpTree);
				
				if (workUpTree!=null && workUpTree) { 
					if (lowestRank.equals("Genus")) { 
						result = isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, subfamily, null, sourceAuthority);
					} else if (lowestRank.equals("Subfamily")) { 
						result = isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, null, null, sourceAuthority);
					} else if (lowestRank.equals("Family")) { 
						result = isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, null, null, null, sourceAuthority);
					} else if (lowestRank.equals("Order")) { 
						result = isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, null, null, null, null, sourceAuthority);
					} else if (lowestRank.equals("Class")) { 
						result = isSameClassificationInAuthority(kingdom, phylum, null, null, null, null, null, sourceAuthority);
					} else if (lowestRank.equals("Phylum")) { 
						result = isSameClassificationInAuthority(kingdom, null, null, null, null, null, null, sourceAuthority);
					} else {
						result.setBooleanValue(true);
					}
				} else if (workUpTree==null) { 
					result.setBooleanValue(true);
					result.addComment("No more higher ranks found to compare");
				}
			} 
		}
		
		return result;
	}
	
}
