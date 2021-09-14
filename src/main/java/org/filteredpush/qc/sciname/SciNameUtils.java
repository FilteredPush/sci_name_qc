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


import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.GBIFService;
import org.filteredpush.qc.sciname.services.WoRMSService;
/*
import org.globalnames.parser.ScientificNameParser;
import org.globalnames.parser.ScientificNameParser.Result;
import org.globalnames.gnparser.resultpojo.Gnparser;
import org.globalnames.parser.ScientificName;
import org.globalnames.parser.Species;
import org.json4s.JsonAST.JValue;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import scala.collection.immutable.List;
*/

import org.gbif.api.model.checklistbank.ParsedName;
import org.gbif.nameparser.NameParser;

import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameComparison;
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
		NameParser parser = new NameParser();
		logger.debug(scientificName);
		logger.debug(scientificNameAuthorship);
		try {
			ParsedName parse = parser.parse(scientificName);
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
		NameParser parser = new NameParser();
		try {
			ParsedName parse = parser.parse(scientificName);
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
		System.out.println(simpleWoRMSGuidLookup("Buccinum canetae","Clench & Aguayo"));
		
		LookupResult comparison;
		try {
			comparison = WoRMSService.nameComparisonSearch("Buccinum canetae","Clench & Aguayo", false);
			System.out.println(comparison.getMatchedName());
			System.out.println(comparison.getNameComparison().getMatchType());
			System.out.println(comparison.getNameComparison().getSimilarity());
			System.out.println(comparison.getNameComparison().getMatchSeverity());
			System.out.println(comparison.getNameComparison().getRemark());
			System.out.println(comparison.getMatchedAuthorship());
			System.out.println(comparison.getGuid());
		} catch (Exception e) {
			logger.error(e);
			System.out.println(e.getMessage());
		}
		
		System.out.println(simpleGBIFGuidLookup("Buccinum canetae","Clench & Aguayo"));
	}
	
/*	
		Result parse = ScientificNameParser.instance().fromString("Buccinum canetae Clench & Aguayo, 1944");
		System.out.println(parse.input().verbatim());
		System.out.println(parse.ambiguousAuthorship());
		System.out.println(parse.delimitedString("|"));
		System.out.println(parse.authorshipDelimited().get());
		System.out.println(parse.canonized(true).get());
		System.out.println(parse.normalized().get());
		System.out.println(parse.scientificName());
		ScientificName sciName = parse.scientificName();
		System.out.println(sciName.isHybrid());
		System.out.println(sciName.isVirus());
		System.out.println(sciName.quality());
		System.out.println(sciName.year());
		System.out.println(sciName.authorship());
		System.out.println(sciName.surrogate());
		System.out.println(sciName.namesGroup());
		System.out.println(sciName.unparsedTail());
		JValue detailed = parse.detailed();
		System.out.println(parse.detailed());
		List<JValue> parseBits = detailed.children(); 
		scala.collection.Iterator<JValue> i = parseBits.iterator();
		while (i.hasNext()) {
			JValue bit = i.next();
			System.out.println(bit.toString());
			scala.collection.Iterator<JValue> it = bit.children().iterator();
			while (it.hasNext()) { 
				JValue bit2 = it.next();
				System.out.println(bit2.toString());
			}
		}
		System.out.println(parse.json(true));
		
		
		//JsonParser jp = new JsonFactory().createJsonParser(parse.toString());
		//jp.
		// StdDeserializer<Gnparser> gnparser = new BaseNodeDeserializer<Gnparser>().deserializeWithType(jp, ctxt, typeDeserializer);
		
	}
*/	
}
