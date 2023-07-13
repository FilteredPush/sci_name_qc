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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.NameParser;
import org.gbif.nameparser.api.NamePart;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.UnparsableNameException;

/**
 * Class for evaluating the similarity between two scientific names.
 * 
 * @author mole
 *
 */
public class ScientificNameComparator {
	
	private static final Log log = LogFactory.getLog(ScientificNameComparator.class);
	
	/**
	 * Perform a comparison on two strings that are expected to be the scientific name without authorship.
	 * 
	 * @param aName one name
	 * @param toOtherName the name to compare with aName
	 * @return a name comparison object where the match type will be one of MATCH_EXACT, MATCH_ERROR, or 
	 *   one of the SNMATCH_ values
	 */
	public NameComparison compareWithoutAuthor(String aName, String toOtherName) {
		
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
	    			log.debug(aName);
	    			ParsedName parse = nameParser.parse(aName);
	    			Map<NamePart,String> qualifiers = parse.getEpithetQualifier();
	    			if (qualifiers==null) { 
	    				qualifiers = new HashMap<NamePart,String>();
	    			}
	    			log.debug(parse.getUninomial());
	    			log.debug(parse.getGenus());
	    			log.debug(parse.getInfragenericEpithet());
	    			log.debug(parse.getSpecificEpithet());
	    			log.debug(parse.getInfraspecificEpithet());
	    			log.debug(parse.getTerminalEpithet());
	    			log.debug(qualifiers.isEmpty());
	    			log.debug(parse.getRank());
	    			ParsedName otherParse = nameParser.parse(toOtherName);
	    			Map<NamePart,String> otherQualifiers = otherParse.getEpithetQualifier();
	    			if (otherQualifiers==null) { 
	    				otherQualifiers = new HashMap<NamePart,String>();
	    			}
	    			Boolean qualifiersDiffer = false;
	    			if (qualifiers.size()!= otherQualifiers.size()) { 
	    				qualifiersDiffer = true;
	    			} else if (qualifiers.size()>0) { 
	    				Set<NamePart> keys = qualifiers.keySet();
	    				Iterator<NamePart> i = keys.iterator();
	    				while (i.hasNext()) { 
	    					NamePart key = i.next();
	    					if (otherQualifiers.containsKey(key)) { 
	    						if (!qualifiers.get(key).equals(otherQualifiers.get(key))) { 
	    							qualifiersDiffer = true;
	    						}
	    					} else { 
	    						qualifiersDiffer = true;
	    					}
	    				}
	    			}
	    			log.debug(qualifiersDiffer);
	    			
	    			if (StringUtils.equals(parse.getGenus(), otherParse.getGenus()) && 
	    					StringUtils.equals(parse.getInfragenericEpithet(), otherParse.getInfragenericEpithet()) && 
	    					StringUtils.equals(parse.getSpecificEpithet(), otherParse.getSpecificEpithet()) && 
	    					StringUtils.equals(parse.getInfraspecificEpithet(), otherParse.getInfraspecificEpithet()) && 
	    					StringUtils.equals(parse.getTerminalEpithet(), otherParse.getTerminalEpithet()) && 
	    					StringUtils.equals(parse.getGenus(), otherParse.getGenus()) && 
	    					qualifiersDiffer
	    					) { 
	    				result.setMatchType(NameComparison.SNMATCH_QUALIFIER);
	    				result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    				result.setNameOne(aName);
	    				result.setNameTwo(toOtherName);
	    			} else if (parse.getUninomial()!=null && otherParse.getUninomial()!=null) { 
	    				result.setMatchType(NameComparison.SNMATCH_ONHIGHER);
	    				result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    				result.setNameOne(aName);
	    				result.setNameTwo(toOtherName);
	    				// comparison of a genus or higher rank taxon 
	    			} else if (parse.getUninomial()!=null || otherParse.getUninomial()!=null) {
	    				// comparison of a genus or higher rank taxon with a taxon of rank lower than genus.
	    				if (parse.getUninomial()!=null ) { 
	    					if (StringUtils.equals(parse.getUninomial(),otherParse.getGenus())) { 
	    						result.setMatchType(NameComparison.SNMATCH_GENUSTOLOWER);
	    						result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    						result.setNameOne(aName);
	    						result.setNameTwo(toOtherName);
	    					} else { 
	    						result.setMatchType(NameComparison.SNMATCH_DISSIMILAR);
	    						result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    						result.setNameOne(aName);
	    						result.setNameTwo(toOtherName);
	    					}
	    				} else { 
	    					if (StringUtils.equals(parse.getGenus(),otherParse.getUninomial())) { 
	    						result.setMatchType(NameComparison.SNMATCH_GENUSTOLOWER);
	    						result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    						result.setNameOne(aName);
	    						result.setNameTwo(toOtherName);
	    					} else { 
	    						result.setMatchType(NameComparison.SNMATCH_DISSIMILAR);
	    						result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    						result.setNameOne(aName);
	    						result.setNameTwo(toOtherName);
	    					}	
	    				}
	    			} else { 
	    				// comparison of two taxa of rank lower than genus
	    				if (StringUtils.equals(parse.getGenus(),otherParse.getGenus()) &&
	    						StringUtils.equals(parse.getRank().name(),otherParse.getRank().name()) && 
	    						StringUtils.equals(parse.getSpecificEpithet(),otherParse.getSpecificEpithet()) && 
	    						StringUtils.equals(parse.getTerminalEpithet(),otherParse.getTerminalEpithet()) &&
	    						! StringUtils.equals(parse.getInfragenericEpithet(),otherParse.getInfragenericEpithet())
	    						)  
	    				{
	    					result.setMatchType(NameComparison.SNMATCH_SUBGENUS);
	    					result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    					result.setNameOne(aName);
	    					result.setNameTwo(toOtherName);
	    				} else if (StringUtils.equals(parse.getGenus(),otherParse.getGenus())  && 
	    						!StringUtils.equals(parse.getSpecificEpithet(),otherParse.getSpecificEpithet())) 
	    				{ 
	    					if (StringUtils.equals(parse.getGenus(),otherParse.getGenus())  && 
	    							!StringUtils.equals(parse.getSpecificEpithet(), otherParse.getSpecificEpithet()) &&
	    							parse.getInfraspecificEpithet()!=null &&
	    							StringUtils.equals(parse.getInfraspecificEpithet(), otherParse.getInfraspecificEpithet())
	    							) 
	    					{ 
	    						result.setMatchType(NameComparison.SNMATCH_GENUSSUBSPECIFIC);
	    						result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));	
	    						result.setNameOne(aName);	
	    						result.setNameTwo(toOtherName);
	    					} else { 
	    						result.setMatchType(NameComparison.SNMATCH_ONGENUS);
	    						result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    						result.setNameOne(aName);
	    						result.setNameTwo(toOtherName);
	    					}
	    				} else if (StringUtils.equals(parse.getGenus(),otherParse.getGenus())  && 
	    				 	 	StringUtils.equals(parse.getSpecificEpithet(), otherParse.getSpecificEpithet()) &&
	    				 	 	parse.getInfraspecificEpithet()!=null &&
	    				 	 	! StringUtils.equals(parse.getInfraspecificEpithet(), otherParse.getInfraspecificEpithet())
	    					) 
	    					{ 
	    						result.setMatchType(NameComparison.SNMATCH_GENUSSPECIFIC);
	    						result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    						result.setNameOne(aName);
	    						result.setNameTwo(toOtherName);
	    				} else if (!StringUtils.equals(parse.getGenus(),otherParse.getGenus())  && 
	    							StringUtils.equals(parse.getSpecificEpithet(),otherParse.getSpecificEpithet()) &&
	    							StringUtils.equals(parse.getInfraspecificEpithet(),otherParse.getInfraspecificEpithet())
	    							) 
	    				{ 
	    					result.setMatchType(NameComparison.SNMATCH_GENUSDIFFERENT);
	    					result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    					result.setNameOne(aName);
	    					result.setNameTwo(toOtherName);
	    				} else { 
	    					result.setMatchType(NameComparison.SNMATCH_DISSIMILAR);
	    					result.setSimilarity(ScientificNameComparator.stringSimilarity(aName, toOtherName));
	    					result.setNameOne(aName);
	    					result.setNameTwo(toOtherName);
	    				}
	    				
	    			}
	    		} catch (UnparsableNameException e) {
	    			log.error(e.getMessage());
	    		} catch (InterruptedException e) {
	    			log.error(e.getMessage());
				}
	    		try {
					nameParser.close();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
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
