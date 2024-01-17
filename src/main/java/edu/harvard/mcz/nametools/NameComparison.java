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
 * @version $Id: $Id
 */
public class NameComparison {
	
	private static final Log log = LogFactory.getLog(NameComparison.class);

	/** Constant <code>MATCH_EXACT="Exact Match"</code> */
	public static final String MATCH_EXACT = "Exact Match";
	/** Constant <code>MATCH_EXACT_BRACKETS="Exact Match except for square brackets"</code> */
	public static final String MATCH_EXACT_BRACKETS = "Exact Match except for square brackets";
	/** Constant <code>MATCH_ERROR="Error in making comparison"</code> */
	public static final String MATCH_ERROR = "Error in making comparison";
	/** Constant <code>MATCH_CONNECTFAILURE="Error connecting to service"</code> */
	public static final String MATCH_CONNECTFAILURE = "Error connecting to service";
	/** Constant <code>MATCH_FUZZY_SCINAME="Fuzzy Match on Scientific Name"</code> */
	public static final String MATCH_FUZZY_SCINAME = "Fuzzy Match on Scientific Name";
	/** Constant <code>MATCH_DISSIMILAR="Author Dissimilar"</code> */
	public static final String MATCH_DISSIMILAR = "Author Dissimilar";
	/** Constant <code>MATCH_STRONGDISSIMILAR="Author Strongly Dissimilar"</code> */
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
	/** 
	 * Zoological, authorship differs just by one case adding initials to a single author 
	 */
	public static final String MATCH_ADDSINITIALEXACTYEAR = "Authorship adds initial, Year Exact";
	
	/** Constant <code>MATCH_WEAKEXACTYEAR="Slightly Similar Author, Year Exact"</code> */
	public static final String MATCH_WEAKEXACTYEAR = "Slightly Similar Author, Year Exact";
	/** Constant <code>MATCH_SIMILAREXACTYEAR="Similar Author, Year Exact"</code> */
	public static final String MATCH_SIMILAREXACTYEAR = "Similar Author, Year Exact";
	/** Constant <code>MATCH_DIFFERANDEXACTYEAR="Author only differs in form of and, Yea"{trunked}</code> */
	public static final String MATCH_DIFFERANDEXACTYEAR = "Author only differs in form of and, Year Exact";
	/** Constant <code>MATCH_SIMILARMISSINGYEAR="Similar Author, Year Removed"</code> */
	public static final String MATCH_SIMILARMISSINGYEAR = "Similar Author, Year Removed";
	/** Constant <code>MATCH_SIMILARADDSYEAR="Similar Author, Year Added"</code> */
	public static final String MATCH_SIMILARADDSYEAR = "Similar Author, Year Added";
	/** Constant <code>MATCH_EXACTDIFFERENTYEAR="Exact Author, Years Different"</code> */
	public static final String MATCH_EXACTDIFFERENTYEAR = "Exact Author, Years Different";
	/** Constant <code>MATCH_EXACTMISSINGYEAR="Exact Author, Year Removed"</code> */
	public static final String MATCH_EXACTMISSINGYEAR = "Exact Author, Year Removed";
	/** Constant <code>MATCH_EXACTADDSYEAR="Exact Author, Year Added"</code> */
	public static final String MATCH_EXACTADDSYEAR = "Exact Author, Year Added";
	/** Constant <code>MATCH_PARENTHESIESDIFFER="Differ only in Parenthesies"</code> */
	public static final String MATCH_PARENTHESIESDIFFER = "Differ only in Parenthesies";
	/** Constant <code>MATCH_PARENYEARDIFFER="Differ in Parenthesies and Year"</code> */
	public static final String MATCH_PARENYEARDIFFER = "Differ in Parenthesies and Year";
	/** Constant <code>MATCH_AUTHSIMILAR="Author Similar"</code> */
	public static final String MATCH_AUTHSIMILAR = "Author Similar";
	/** Constant <code>MATCH_ADDSAUTHOR="Author Added"</code> */
	public static final String MATCH_ADDSAUTHOR = "Author Added";
	/** Constant <code>MATCH_MULTIPLE="Multiple Matches:"</code> */
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
	
	/** Constant <code>SNMATCH_SUBGENUS="Same name, but with subgenus present in"{trunked}</code> */
	public static final String SNMATCH_SUBGENUS = "Same name, but with subgenus present in one case.";
	/** Constant <code>SNMATCH_ONGENUS="Exact match on genus, but not on specif"{trunked}</code> */
	public static final String SNMATCH_ONGENUS = "Exact match on genus, but not on specific/infraspecific epithet.";
	/** Constant <code>SNMATCH_ONHIGHER="Match is of genus or higher rank taxon."</code> */
	public static final String SNMATCH_ONHIGHER = "Match is of genus or higher rank taxon.";
	/** Constant <code>SNMATCH_QUALIFIER="Same name, but differ in qualifier(s)"</code> */
	public static final String SNMATCH_QUALIFIER = "Same name, but differ in qualifier(s)";
	/** Constant <code>SNMATCH_GENUSTOLOWER="Match of genus to contained lower rank "{trunked}</code> */
	public static final String SNMATCH_GENUSTOLOWER = "Match of genus to contained lower rank taxon.";
	/** Constant <code>SNMATCH_DISSIMILAR="Scientific name dissimilar."</code> */
	public static final String SNMATCH_DISSIMILAR = "Scientific name dissimilar.";
	/** Constant <code>SNMATCH_GENUSDIFFERENT="Difference only in generic name."</code> */
	public static final String SNMATCH_GENUSDIFFERENT = "Difference only in generic name.";
	/** Constant <code>SNMATCH_GENUSSPECIFIC="Difference only in specific name."</code> */
	public static final String SNMATCH_GENUSSPECIFIC = "Difference only in specific name.";
	/** Constant <code>SNMATCH_GENUSSUBSPECIFIC="Difference only in subspecific name."</code> */
	public static final String SNMATCH_GENUSSUBSPECIFIC = "Difference only in subspecific name.";
	
	
	private String nameOne;
	private String nameTwo;
	private String matchType;
	private String matchSeverity;
	private double similarity;
	private String remark;
	
	
	/**
	 * <p>Constructor for NameComparison.</p>
	 *
	 * @param nameOne a {@link java.lang.String} object.
	 * @param nameTwo a {@link java.lang.String} object.
	 */
	public NameComparison(String nameOne, String nameTwo) { 
		this.nameOne = nameOne;
		this.nameTwo = nameTwo;
		if (this.nameOne==null) { this.nameOne = ""; } 
		if (this.nameTwo==null) { this.nameTwo = ""; } 
	}
	
	/**
	 * <p>Getter for the field <code>nameOne</code>.</p>
	 *
	 * @return the first name in the comparison
	 */
	public String getNameOne() {
		return nameOne;
	}
	/**
	 * <p>Setter for the field <code>nameOne</code>.</p>
	 *
	 * @param nameOne the first name in the comparison to set.
	 */
	public void setNameOne(String nameOne) {
		this.nameOne = nameOne;
	}
	/**
	 * <p>Getter for the field <code>nameTwo</code>.</p>
	 *
	 * @return the second name in the comparison
	 */
	public String getNameTwo() {
		return nameTwo;
	}
	/**
	 * <p>Setter for the field <code>nameTwo</code>.</p>
	 *
	 * @param nameTwo the second name in the comparison to set.
	 */
	public void setNameTwo(String nameTwo) {
		this.nameTwo = nameTwo;
	}
	/**
	 * <p>Getter for the field <code>matchType</code>.</p>
	 *
	 * @return the matchType
	 */
	public String getMatchType() {
		if (matchType==null) { 
			return "";
		}
		return matchType;
	}
	/**
	 * <p>Setter for the field <code>matchType</code>.</p>
	 *
	 * @param matchType the matchType to set
	 */
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	/**
	 * <p>Getter for the field <code>matchSeverity</code>.</p>
	 *
	 * @return the matchSeverity
	 */
	public String getMatchSeverity() {
		return matchSeverity;
	}
	/**
	 * <p>Setter for the field <code>matchSeverity</code>.</p>
	 *
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
	 * <p>Setter for the field <code>similarity</code>.</p>
	 *
	 * @param similarity the similarity to set
	 */
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	/**
	 * <p>Getter for the field <code>remark</code>.</p>
	 *
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * <p>Setter for the field <code>remark</code>.</p>
	 *
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * Test if a match string for a scientific name (SNMATCH_***) is a plausible match.
	 *
	 * @param match the string to classify for plausiblity
	 * @return true if the string matches one of the SNMATCH_ constants that can be
	 * considered a plausible match between names.
	 */
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
	
	/**
	 * Test if a match string for a scientific name (SNMATCH_***) is a plausible match.
	 *
	 * @param match the string to classify for plausiblity
	 * @return true if the string matches one of the SNMATCH_ constants that can be
	 * considered a plausible match between names.
	 */
	public static boolean isPlausibleAuthorMatch(String match) {
		boolean result = false;
		if (match.equals(MATCH_ADDSAUTHOR) || 
				match.equals(MATCH_EXACT) || 
				match.equals(MATCH_EXACT_BRACKETS) || 
				match.equals(MATCH_EXACTADDSYEAR) ||
				match.equals(MATCH_SAMEBUTABBREVIATED) ||
				match.equals(MATCH_L) ||
				match.equals(MATCH_L_EXACTYEAR) ||
				match.equals(MATCH_SOWERBYEXACTYEAR) ||
				match.equals(MATCH_ADDSINITIALEXACTYEAR) ||
				match.equals(MATCH_DIFFERANDEXACTYEAR) ||
				match.equals(MATCH_EXACTMISSINGYEAR)
		) { 
			result = true;
		}
		
		return result;
	}
	
}
