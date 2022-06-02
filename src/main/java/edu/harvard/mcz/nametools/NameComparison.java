/** 
 * NameComparison.java 
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Strucutred metadata concerning the conclusions of a comparison between two scientific names 
 * strings or two scientific name authorship strings.
 * 
 * @author mole
 *
 */
public class NameComparison {
	
	private static final Log log = LogFactory.getLog(NameComparison.class);

	public static final String MATCH_EXACT = "Exact Match";
	public static final String MATCH_EXACT_BRACKETS = "Exact Match except for square brackets";
	public static final String MATCH_ERROR = "Error in making comparison";
	public static final String MATCH_CONNECTFAILURE = "Error connecting to service";
	public static final String MATCH_FUZZY_SCINAME = "Fuzzy Match on Scientific Name";
	public static final String MATCH_DISSIMILAR = "Author Dissimilar";
	public static final String MATCH_STRONGDISSIMILAR = "Author Strongly Dissimilar";
	/**
	 * Zoological, "Sowerby" in one case, Sowerby I, Sowerby II, or Sowerby III in the other,
	 * specification of which of the Sowerby family was the author was added.
	 */
	public static final String MATCH_SOWERBYEXACTYEAR = "Specifying Which Sowerby, Year Exact";
	/**
	 * Zoological, L. may be abbreviation for Linnaeus or Lamarck.
	 * Does not apply to Botany.
	 */
	public static final String MATCH_L_EXACTYEAR = "Ambiguous L., Year Exact";
	/**
	 * Zoological, L. may be abbreviation for Linnaeus or Lamarck.
	 * Does not apply to Botany.
	 */
	public static final String MATCH_L = "Ambiguous L.";
	public static final String MATCH_WEAKEXACTYEAR = "Slightly Similar Author, Year Exact";
	public static final String MATCH_SIMILAREXACTYEAR = "Similar Author, Year Exact";
	public static final String MATCH_SIMILARMISSINGYEAR = "Similar Author, Year Removed";
	public static final String MATCH_SIMILARADDSYEAR = "Similar Author, Year Added";
	public static final String MATCH_EXACTDIFFERENTYEAR = "Exact Author, Years Different";
	public static final String MATCH_EXACTMISSINGYEAR = "Exact Author, Year Removed";
	public static final String MATCH_EXACTADDSYEAR = "Exact Author, Year Added";
	public static final String MATCH_PARENTHESIESDIFFER = "Differ only in Parenthesies";
	public static final String MATCH_PARENYEARDIFFER = "Differ in Parenthesies and Year";
	public static final String MATCH_AUTHSIMILAR = "Author Similar";
	public static final String MATCH_ADDSAUTHOR = "Author Added";
	public static final String MATCH_MULTIPLE = "Multiple Matches:";
	/**
	 * Botanical parenthetical, ex, sanctioning author bits are composed differently, without
	 * regard to the similarity or difference of the individual bits.  e.g. "(x) y" is 
	 * different from "x ex y" or (x) y ex z", but not different in this comparison from "(a) b".
	 */
	public static final String MATCH_PARTSDIFFER = "Authorship botanical parts tokenize differently";
    /**
     * Authorship strings appear similar (parenthesies, botanical functional parts), but the differences
     * between the strings can be explained by differences in abbreviation or addition/removal of initials.
     */
	public static final String MATCH_SAMEBUTABBREVIATED = "Same Author, but abbreviated differently.";
	
	public static final String SNMATCH_SUBGENUS = "Same name, but with subgenus present in one case.";
	public static final String SNMATCH_ONGENUS = "Exact match on genus, but not on specific/infraspecific epithet.";
	public static final String SNMATCH_ONHIGHER = "Match is of genus or higher rank taxon.";
	public static final String SNMATCH_QUALIFIER = "Same name, but differ in qualifier(s)";
	public static final String SNMATCH_GENUSTOLOWER = "Match of genus to contained lower rank taxon.";
	public static final String SNMATCH_DISSIMILAR = "Scientific name dissimilar.";
	public static final String SNMATCH_GENUSDIFFERENT = "Difference only in generic name.";
	public static final String SNMATCH_GENUSSPECIFIC = "Difference only in specific name.";
	public static final String SNMATCH_GENUSSUBSPECIFIC = "Difference only in subspecific name.";
	
	
	private String nameOne;
	private String nameTwo;
	private String matchType;
	private String matchSeverity;
	private double similarity;
	private String remark;
	
	
	public NameComparison(String nameOne, String nameTwo) { 
		this.nameOne = nameOne;
		this.nameTwo = nameTwo;
		if (this.nameOne==null) { this.nameOne = ""; } 
		if (this.nameTwo==null) { this.nameTwo = ""; } 
	}
	
	/**
	 * @return the first name in the comparison
	 */
	public String getNameOne() {
		return nameOne;
	}
	/**
	 * @param nameOne the first name in the comparison to set.
	 */
	public void setNameOne(String nameOne) {
		this.nameOne = nameOne;
	}
	/**
	 * @return the second name in the comparison
	 */
	public String getNameTwo() {
		return nameTwo;
	}
	/**
	 * @param nameTwo the second name in the comparison to set.
	 */
	public void setNameTwo(String nameTwo) {
		this.nameTwo = nameTwo;
	}
	/**
	 * @return the matchType
	 */
	public String getMatchType() {
		if (matchType==null) { 
			return "";
		}
		return matchType;
	}
	/**
	 * @param matchType the matchType to set
	 */
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	/**
	 * @return the matchSeverity
	 */
	public String getMatchSeverity() {
		return matchSeverity;
	}
	/**
	 * @param matchSeverity the matchSeverity to set
	 */
	public void setMatchSeverity(String matchSeverity) {
		this.matchSeverity = matchSeverity;
	}
	/**
	 * Similarity between name one and name two in range from zero to one.
	 * 
	 * @return the similarity
	 */
	public double getSimilarity() {
		return similarity;
	}
	/**
	 * @param similarity the similarity to set
	 */
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public static boolean isPlausible(String match) {
		boolean result = false;
		if (match.equals(SNMATCH_SUBGENUS) || 
				match.equals(SNMATCH_ONGENUS) || 
				match.equals(SNMATCH_GENUSSPECIFIC) || 
				match.equals(SNMATCH_GENUSSUBSPECIFIC) ||
				match.equals(SNMATCH_ONHIGHER)
		) { 
			result = true;
		}
		
		return result;
	}
	
}
