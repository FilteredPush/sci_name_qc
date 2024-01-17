/** 
 * AuthorNameComparator.java 
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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Parent of class hierarchy for making comparisons between pairs of authorship strings
 * of scientific names.  Authorship strings can contain authors of names, authorship role
 * markers (parentheses, ex, :, in), asserted year of publication and actual year of publication.
 * Conventions vary between the codes and in some disciplines for how authorship strings 
 * are typically constructed.
 * 
 * @author mole
 *
 */
public abstract class AuthorNameComparator {
	
	/** Obtain the similarity threshold for comparison.
	 * 
	 * @return double representing the threshold value for considering strings similar.
	 */
	public abstract double getSimilarityThreshold();

	/**
	 * Compare two authorship strings, and assert a comparison between the
	 * two in the form of a NameComparison.
	 * 
	 * @param anAuthor  one authorship string for comparison
	 * @param toOtherAuthor the other authorship string to comapare to.
	 * @return a string description classifying the match between the two 
	 * authorship strings, with awareness of string distance, parenthesies, and year.
	 * 
	 * @see NameComparison
	 */
	public abstract NameComparison compare(String anAuthor, String toOtherAuthor);
	
	/**
	 * Given an authorship string and a kingdom, guess at the correct author name comparator to use.
	 * 
	 * @param authorship a scientific name authorship string.
	 * @param kingdom dwc:kingdom
	 * @return an AuthorNameComparator probably appropriate for the authorship string presented.
	 */
	public static AuthorNameComparator authorNameComparatorFactory(String authorship, String kingdom) { 
		AuthorNameComparator result = new ICZNAuthorNameComparator(.75d, .5d);
		if (kingdom!=null && (kingdom.toLowerCase().equals("plantae") || kingdom.toLowerCase().equals("fungi"))) {
			// Plants and fungi follow ICNafp.
			result = new ICNafpAuthorNameComparator(.75d, .5d);
			
			// TODO: Algae, tend to include year of authorship
			
		} else { 
			if (authorship!=null) { 
				boolean plantFlag = false;
				boolean animalFlag = false;
				if (authorship.matches("[0-9]{4}")) {
					// assume an animal form of the authorship string if the authorship contains a year.
					animalFlag = true;
				}
				if (authorship.startsWith("(") && authorship.endsWith(")")) { 
					// when plant authors contain parenthesies, at least one follows the parenthesies.
					animalFlag = true;
				}
				if (authorship.contains(" ex ") || authorship.contains(":")) { 
					// only plant names should contain ex authors or sanctioning authors, designated by these characters.
					plantFlag = true;
				}
				if (authorship.startsWith("(") && authorship.contains(")") && authorship.matches("[a-zA-Z.]$") && authorship.matches(") [A-Z]")) {
					// when plant authors contain parenthesies at least one follows the parenthesis.
					plantFlag = true;
				}
				if (plantFlag==animalFlag) {
					 // we haven't figured it out yet.
					// TODO: Try harder.
				} else { 
					if (plantFlag) { 
			            result = new ICNafpAuthorNameComparator(.75d, .5d);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Attempt to detect relevant code for authorship string from the authorship
	 * 
	 * @param authorship to test for code
	 * @param withCertainty if true Will only return one applicable code, and 
	 *   will return false/false for simple strings that could fit in either code, 
	 *   otherwise could return true/true.
	 * @return map of the codes with boolean true/false for detection of the code
	 */
	public static Map<String,Boolean> detectApplicableCode(String authorship,boolean withCertainty) {
		if (withCertainty) { 
			return detectApplicableCodeWithCertainty(authorship, null, null);
		} else { 
			return detectApplicableCode(authorship, null, null);
		}
	}
	
	/**Detect, if possible, which code is applicable for some combination of kingdom and authorship.  
	 * 
	 * @param authorship to test
	 * @param kingdom to consider
	 * @param withCertainty of true, will only return one applicable code as true, will return false 
	 *   for each code if strings could fit multiple codes, if false, will return true for any code
	 *   with which the authorship and kingdom are consistent.
	 * @return map of the nomenclatural codes with boolean true/false marking detected applicability 
	 *   of each code.
	 */
	public static Map<String,Boolean> detectApplicableCode(String authorship, String kingdom, boolean withCertainty) { 
		if (withCertainty) { 
			return detectApplicableCodeWithCertainty(authorship, kingdom, null);
		} else { 
			return detectApplicableCode(authorship, kingdom, null);
		}
	}
	
	/** Detect, if possible, which code is applicable for some combination of kingdom, phylum, and 
	 * authorship.  Will only return one applicable code, and will return false/false for 
	 * simple strings that could fit in either code.
	 * 
	 * @param authorship authorship string to test, for code
	 * @param kingdom the kingdom to which the authorship string belongs, has primacy
	 *   over other values, Animalia-use-ICZN, Fungi|Plantae-use-ICNafp
	 * @param phylum not implemented yet.
	 * @return a map containing keys ICZN and ICNafp with true/false values for each for detection
	 *   of an applicable code.
	 */
	public static Map<String,Boolean> detectApplicableCodeWithCertainty(String authorship, String kingdom, String phylum) { 
		HashMap<String,Boolean> result = new HashMap<String,Boolean>();
		result.put("ICZN", false);
		result.put("ICNafp", false);
		boolean done = false;
		if (kingdom==null) { kingdom = ""; } 
		if (phylum==null) { phylum = ""; } 
		
		if (!done && kingdom.trim().equalsIgnoreCase("Animalia")) { result.put("ICZN", true); done=true; }  
		if (!done && kingdom.trim().equalsIgnoreCase("Fungi")) { result.put("ICNafp", true); done=true; }  
		if (!done && kingdom.trim().equalsIgnoreCase("Plantae")) { result.put("ICNafp", true); done=true; }  
		
		if (!done && authorship!=null) { 
			authorship = authorship.trim();
			boolean iczn = AuthorNameComparator.consistentWithICZNAuthor(authorship);
			boolean icnafp = AuthorNameComparator.consistentWithICNapfAuthor(authorship);
			if (iczn == true && icnafp == false) {
				result.put("ICZN", true);
				done = true;
			} else if (iczn == false && icnafp == true) {
				result.put("ICNafp", true);
				done = true;
			} else {
				if (authorship.startsWith("(") && authorship.endsWith(")")) {
					// when plant authors contain parenthesies, at least one follows the
					// parenthesies.
					result.put("ICZN", true);
					done = true;
				}
				if (authorship.contains(" ex ") || authorship.contains(":")) {
					// only plant names should contain ex authors or sanctioning authors, designated
					// by these characters.
					result.put("ICNafp", true);
					done = true;
				}
				if (authorship.startsWith("(") && authorship.contains(")") && authorship.matches(".*[a-zA-Z.]+$")) {
					// when plant authors contain parenthesies at least one follows the parenthesis.
					result.put("ICNafp", true);
					done = true;
				}
			}
		}		
		if (!done && authorship!=null) { 
			// further checks could go here.
		}
		
		return result;
	}
	
	/** Detect, if possible, which code is applicable for some combination of kingdom, phylum, and 
	 * authorship.  May return true/true if either code could apply, as in just a single author
	 * name as the authorship string.
	 * 
	 * @param authorship authorship string to test, for code
	 * @param kingdom the kingdom to which the authorship string belongs, has primacy
	 *   over other values, Animalia-use-ICZN, Fungi|Plantae-use-ICNafp
	 * @param phylum not implemented yet.
	 * @return a map containing keys ICZN and ICNafp with true/false values for each for detection
	 *   of an applicable code.
	 */
	public static Map<String,Boolean> detectApplicableCode(String authorship, String kingdom, String phylum) { 
		HashMap<String,Boolean> result = new HashMap<String,Boolean>();
		result.put("ICZN", false);
		result.put("ICNafp", false);
		boolean done = false;
		if (kingdom==null) { kingdom = ""; } 
		if (phylum==null) { phylum = ""; } 
		
		if (!done && kingdom.trim().equalsIgnoreCase("Animalia")) { result.put("ICZN", true); done=true; }  
		if (!done && kingdom.trim().equalsIgnoreCase("Fungi")) { result.put("ICNafp", true); done=true; }  
		if (!done && kingdom.trim().equalsIgnoreCase("Plantae")) { result.put("ICNafp", true); done=true; }  
		
		if (!done && authorship!=null) { 
			authorship = authorship.trim();
			boolean iczn = AuthorNameComparator.consistentWithICZNAuthor(authorship);
			boolean icnafp = AuthorNameComparator.consistentWithICNapfAuthor(authorship);
			if (iczn == true && icnafp == false) {
				result.put("ICZN", true);
				done = true;
			} else if (iczn == false && icnafp == true) {
				result.put("ICNafp", true);
				done = true;
			} else {
				if (authorship.startsWith("(") && authorship.endsWith(")")) {
					// when plant authors contain parenthesies, at least one follows the
					// parenthesies.
					result.put("ICZN", true);
					done = true;
				}
				if (authorship.contains(" ex ") || authorship.contains(":")) {
					// only plant names should contain ex authors or sanctioning authors, designated
					// by these characters.
					result.put("ICNafp", true);
					done = true;
				}
				if (authorship.startsWith("(") && authorship.contains(")") && authorship.matches(".*[a-zA-Z.]+$")) {
					// when plant authors contain parenthesies at least one follows the parenthesis.
					result.put("ICNafp", true);
					done = true;
				}
			}
			if (!done) { 
				if (authorship.matches("^[a-zA-Z. &]$")) { 
					result.put("ICNafp", true);
					result.put("ICZN", true);
				} else if (iczn==true && icnafp == true) { 
					result.put("ICNafp", true);
					result.put("ICZN", true);
				}
			}
			if (!done) {
				if (authorship.equals("")) { 
					result.put("ICNafp", true);
					result.put("ICZN", true);
				}
			}
		} else if (!done) {
			// authorship is null, and taxonomy didn't indicate, so return true/true
			result.put("ICNafp", true);
			result.put("ICZN", true);
		}
 		System.out.println(result.toString());
		return result;
	}	
	
	/**
	 * Test to see if an authorship string appears to contain a year 
	 * between 1000 and the current century.
	 * 
	 * @param authorship string to test for a year.
	 * 
	 * @return true if a four digit number is found.
	 */
	public static boolean calculateHasYear(String authorship) { 
		boolean result = false;
		if (authorship!=null && 
				authorship.matches("(^|.*[^0-9]+)[12][0-9]{3}([^0-9]+.*|$)") 
			) 
		{
			result = true;
		}		
		return result;
	}
	
	/**
	 * Test to see if an authorship string appears to contain parentheses.
	 * 
	 * @param authorship to test for parentheses
	 * @return true if authorship string contains '()';
	 */
	public static boolean calculateHasParen(String authorship) { 
		boolean result = false;
		if (authorship!=null && authorship.replaceAll("[^()]", "").equals("()")) { 
			result = true;
		}
		return result;
	}
	
	/**
	 * Test whether or not an authorship string is consistent with the 
	 * expected forms of a zoological authorship string. 
	 * 
	 * @param authorship string to test
	 * @return false if the string contains elements inconsistent with 
	 * a zoological authorship string, otherwise returns true.  
	 */
	public static boolean consistentWithICZNAuthor(String authorship) { 
		boolean result = true;
		if (authorship!=null) { 
			if (authorship.contains(" ex ")) {
				// ex author, botanical
				result = false;
			}
			if (authorship.contains(" emend. ")) {
				// emending author, botanical
				result = false;
			}
			if (authorship.contains(":")) {
				// sanctioning author, fungal
				result = false;  
			}
			if (authorship.matches("\\(.*\\)[ A-Za-z.]+")) { 
				// string after parenthesies, botanical
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * Test whether or not an authorship string is consistent with the 
	 * expected forms of a botanical authorship string. 
	 * 
	 * @param authorship string to test
	 * @return false if the string contains elements inconsistent with 
	 * a botanical authorship string, otherwise returns true.  
	 */
	public static boolean consistentWithICNapfAuthor(String authorship) { 
		boolean result = true;
		if (authorship!=null) { 
			if (AuthorNameComparator.calculateHasYear(authorship)) {
				// botanical names do not normally contain a year of publication
				result = false;
			}
			if (authorship.matches("^\\(.*\\)$")) { 
				// if parentheses are present, botanical names also have an author outside the parentheses
				result = false;
			}
		}
		return result;
	}
	
	
	/** Return a measure of case insensitive similarity between two authorship strings, 
	 * ignoring commas and spaces, in a range of 0 (no similarity) to 1 (exact same 
	 * strings), using a measure of the string edit distance scaled to the length 
	 * difference of the two strings.  
	 * 
	 * @param anAuthor one authorship string
	 * @param toOtherAuthor the second authorship string to make the comparason with.
	 * @return a double in the range 0 to 1 where 0 is no similarity and 1 is an exact match.
	 */
	public static double calulateSimilarityOfAuthor(String anAuthor, String toOtherAuthor) { 
		String au = toOtherAuthor.toLowerCase().replaceAll("[, ]", "");
		String au1 = anAuthor.toLowerCase().replaceAll("[, ]", "");
		return AuthorNameComparator.stringSimilarity(au, au1);
	}

	/** Return a measure of case insensitive similarity between just the alphabetic portion 
	 * of two authorship strings, ignoring commas, spaces, numbers, punctuation, and
	 * parentheses, in a range of 0 (no similarity) to 1 (no difference),
	 * using a measure of the string edit distance scaled to the length 
	 * difference of the two strings.  
	 * 
	 * @param anAuthor one authorship string
	 * @param toOtherAuthor the second authorship string to make the comparason with.
	 * @return a double in the range 0 to 1 where 0 is no similarity and 1 is an exact match.
	 */
	public static double calulateSimilarityOfAuthorAlpha(String anAuthor, String toOtherAuthor) { 
		String au = toOtherAuthor.toLowerCase().replaceAll("[^A-Za-z]", "");
		String au1 = anAuthor.toLowerCase().replaceAll("[^A-Za-z]", "");
		return AuthorNameComparator.stringSimilarity(au, au1);
	}

	/**
	 * Return a measure of the similarity between two strings in the range of
	 * 0 (no similarity) to 1 (exact same strings), using a measure of the
	 * string edit distance scaled to the length differences of the two strings.
	 * 
	 * @param string1 one string for comparison
	 * @param string2 the string to compare with string1
	 * @return a double in the range 0 to 1.
	 */
	public static double stringSimilarity(String string1, String string2) {
		double result;
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
