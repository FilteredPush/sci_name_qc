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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.GBIFService;
import org.filteredpush.qc.sciname.services.Validator;
import org.filteredpush.qc.sciname.services.WoRMSService;
import org.filteredpush.qc.sciname.services.ZooBankService;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.ParsedName;

import edu.harvard.mcz.nametools.LookupResult;
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
}