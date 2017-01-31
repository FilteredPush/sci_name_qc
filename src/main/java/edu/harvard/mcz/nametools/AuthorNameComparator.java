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

import org.apache.commons.lang3.StringUtils;

/**
 * Parent of class heirarchy for making comparisons between pairs of authorship strings
 * of scientific names.
 * 
 * @author mole
 *
 */
public abstract class AuthorNameComparator {
	
	public abstract double getSimilarityThreshold();

	/**
	 * Compare two authorship strings, and assert a comparison between the
	 * two in the form of a NameComparison.
	 * 
	 * @param anAuthor
	 * @param toOtherAuthor
	 * @return a string description classifying the match between the two 
	 * authorship strings, with awareness of string distance, parenthesies, and year.
	 * 
	 * @see NameComparison
	 */
	public abstract NameComparison compare(String anAuthor, String toOtherAuthor);
	
	/**
	 * Given an authorship string and a kingdom, guess at the correct author name comparator to use.
	 * 
	 * @param authorship
	 * @param kingdom
	 * @return
	 */
	public static AuthorNameComparator authorNameComparatorFactory(String authorship, String kingdom) { 
		AuthorNameComparator result = new ICZNAuthorNameComparator(.75d, .5d);
		if (kingdom!=null && (kingdom.toLowerCase().equals("plantae") || kingdom.toLowerCase().equals("fungi"))) {
			// Plants and fungi follow ICNafp.
			result = new ICNafpAuthorNameComparator(.75d, .5d);
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
	 * Test to see if an authorship string appears to contain a year 
	 * between 1000 and the current century.
	 * 
	 * @param authorship string to test for a year.
	 * 
	 * @return true if a four digit number is found.
	 */
	public static boolean calculateHasYear(String authorship) { 
		boolean result = false;
		if (authorship!=null && authorship.matches(".*[12][0-9]{3}.*") && authorship.replaceAll("[^0-9]","").length()==4) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Test to see if an authorship string appears to contain parentheses.
	 * 
	 * @param authorship to test for parenthesies
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
			if (authorship.contains(":")) {
				// sanctioning author, fungal
				result = false;  
			}
			if (authorship.matches("\\(.*\\)[ A-Za-z]+")) { 
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
	
	public static double calulateSimilarityOfAuthor(String anAuthor, String toOtherAuthor) { 
		String au = toOtherAuthor.toLowerCase().replaceAll("[, ]", "");
		String au1 = anAuthor.toLowerCase().replaceAll("[, ]", "");
		return AuthorNameComparator.stringSimilarity(au, au1);
	}

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