/**
 * ScientificNameComparator.java
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
 */
package edu.harvard.mcz.nametools;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.NameParser;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.UnparsableNameException;

/**
 * @author mole
 *
 */
public class ScientificNameComparator {
	
	private static final Log log = LogFactory.getLog(ScientificNameComparator.class);
	
	public NameComparison compare(String aName, String toOtherName) {
		
		NameComparison result = new NameComparison(aName, toOtherName);
		
        result.setMatchType(NameComparison.MATCH_ERROR);
		if (aName==null || toOtherName==null) {
		    result.setMatchType(NameComparison.MATCH_ERROR);
		} else { 
			if (aName.equals(toOtherName) || aName.toLowerCase().trim().equals(toOtherName.toLowerCase().trim())) 
			{ 
				result.setMatchType(NameComparison.MATCH_EXACT);
				result.setSimilarity(1.0d);
			} else {
				NameParser nameParser = new NameParserGBIF();
	    		try {
	    			ParsedName parse = nameParser.parse(aName);
	    			ParsedName otherParse = nameParser.parse(toOtherName);
	    			if (parse.getUninomial().equals(otherParse.getUninomial()) &&
	    					parse.getRank().equals(otherParse.getRank()) && 
	    					parse.getSpecificEpithet().equals(otherParse.getSpecificEpithet()) && 
	    					parse.getTerminalEpithet().equals(otherParse.getTerminalEpithet()) &&
	    					! parse.getInfragenericEpithet().equals(otherParse.getInfragenericEpithet())
	    					)  
	    			{
	    				result.setMatchType(NameComparison.SNMATCH_SUBGENUS);
	    				result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    				result.setNameOne(aName);
	    				result.setNameTwo(toOtherName);
	    			} else if (parse.getUninomial().equals(otherParse.getUninomial())  && 
	    					!parse.getSpecificEpithet().equals(otherParse.getSpecificEpithet())) 
	    			{ 
	    				result.setMatchType(NameComparison.SNMATCH_ONGENUS);
	    				result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    				result.setNameOne(aName);
	    				result.setNameTwo(toOtherName);
	    			} else if (parse.getUninomial().equals(otherParse.getUninomial())  && 
	    					!parse.getTerminalEpithet().equals(otherParse.getTerminalEpithet())) 
	    			{ 
	    				result.setMatchType(NameComparison.SNMATCH_ONGENUS);
	    				result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    				result.setNameOne(aName);
	    				result.setNameTwo(toOtherName);
	    			}
	    		} catch (UnparsableNameException e) {
	    			log.error(e.getMessage());
	    		}	
			}
		}
		return result;
	}

	/**
	 * Return a measure of the similarity between two strings in the range of
	 * 0 (no similarity) to 1 (exact same strings), using a measure of the
	 * string edit distance scaled to the length differences of the two strings.
	 * 
	 * @param string1
	 * @param string2
	 * @return a double in the range 0 to 1.
	 */
	public static double stringSimilarity(String string1, String string2) {
		double result = 0d;
		String longer = string1;
		String shorter = string2;
		if (string1.length() < string2.length()) {
			// flip so that longer string is the longest.
			longer = string2;
			shorter = string1;
		}
		if (longer.length() == 0) { 
			result =  1.0; 
		} else { 
			result =  (longer.length() - StringUtils.getLevenshteinDistance(longer, shorter)) / (double) longer.length();
		}
		return result;
	}	
	
}
