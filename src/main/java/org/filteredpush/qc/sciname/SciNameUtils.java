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


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.GBIFService;
import org.filteredpush.qc.sciname.services.WoRMSService;
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
		options.addOption( "f", "file", true, "input csv file from which to lookup names" );
		options.addOption("o","output", true, "output file into which to write results of lookup");
		options.addOption("t","test", false, "test connectivity with an example name");
		options.addOption("h","help", false, "print this message");
		
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
				if (cmd.hasOption("output")) { 
				String outfile = cmd.getOptionValue("file");
					logger.debug(outfile);
				}
			}
		} catch (ParseException e1) {
			logger.error(e1);
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