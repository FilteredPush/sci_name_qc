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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Parent of class hierarchy for making comparisons between pairs of authorship strings
 * of scientific names.  Authorship strings can contain authors of names, authorship role
 * markers (parentheses, ex, :, in), asserted year of publication and actual year of publication.
 * Conventions vary between the codes and in some disciplines for how authorship strings
 * are typically constructed.
 *
 * @author mole
 * @version $Id: $Id
 */
public abstract class AuthorNameComparator {

	private static final Log logger = LogFactory.getLog(AuthorNameComparator.class);
	
	/**
	 * Obtain the similarity threshold for comparison.
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
	
	/**
	 *Detect, if possible, which code is applicable for some combination of kingdom and authorship.
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
	
	/**
	 * Detect, if possible, which code is applicable for some combination of kingdom, phylum, and
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
	
	/**
	 * Detect, if possible, which code is applicable for some combination of kingdom, phylum, and
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
 		logger.debug(result.toString());
		return result;
	}	
	
	/**
	 * Test to see if an authorship string appears to contain a year
	 * between 1000 and the current century.
	 *
	 * @param authorship string to test for a year.
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
	
	
	/**
	 * Return a measure of case insensitive similarity between two authorship strings,
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

	/**
	 * Return a measure of case insensitive similarity between just the alphabetic portion
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
	

	/** Lower case particles of surnames, ignored in comparing abbreviated authorships. */
	private static final Set<String> PARTICLES = new HashSet<String>(Arrays.asList(
			"de", "d'", "du", "da", "di", "van", "von", "der", "den", "la", "le", "dos", "des"));

	/**
	 * One author in an authorship string: the surname (without particles) and the initials.
	 */
	static class AuthorPart {
		final String surname;
		final String initials;
		AuthorPart(String surname, String initials) { this.surname = surname; this.initials = initials; }
		@Override public String toString() { return initials + "|" + surname; }
	}

	private static String stripAccents(String value) {
		return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
	}

	/** Transliterate German umlauts (ü to ue), as in Müll. and Muell. */
	private static String transliterateUmlauts(String value) {
		return value.replace("ü", "ue").replace("ö", "oe").replace("ä", "ae")
				.replace("Ü", "Ue").replace("Ö", "Oe").replace("Ä", "Ae");
	}

	/**
	 * Split an authorship string (without regard to parentheses, ex or sanctioning authors, or year) into
	 * its authors, each with its surname and initials, e.g. "J.B. de Lamarck &amp; Smith, 1822" gives
	 * [JB|Lamarck, |Smith].
	 *
	 * @param authorship the authorship
	 * @return the authors, in order
	 */
	static List<AuthorPart> authorParts(String authorship) {
		List<AuthorPart> result = new ArrayList<AuthorPart>();
		if (authorship==null) { return result; }
		String a = authorship.replaceAll("[()\\[\\]0-9]", " ")
				.replaceAll("\\s+(&|and|et)\\s+", ",").replace("&", ",").replace(";", ",")
				.replaceAll("\\.", ". ");
		for (String segment : a.split(",")) {
			StringBuilder surname = new StringBuilder();
			StringBuilder initials = new StringBuilder();
			for (String token : segment.trim().split("\\s+")) {
				if (token.isEmpty()) { continue; }
				if (token.matches("^[A-Z][a-z]?\\.$") && surname.length()==0) {
					initials.append(token.charAt(0));
				} else if (PARTICLES.contains(token.toLowerCase())) {
					// a particle, e.g. de, von
				} else {
					if (surname.length()>0) { surname.append(" "); }
					surname.append(token);
				}
			}
			if (surname.length()==0 && initials.length()>0) {
				// a single abbreviation such as L.: the initial is the abbreviated surname
				surname.append(initials.charAt(initials.length()-1)).append(".");
				initials.setLength(initials.length()-1);
			}
			if (surname.length()>0) {
				result.add(new AuthorPart(surname.toString(), initials.toString()));
			}
		}
		return result;
	}

	/**
	 * Is one surname an abbreviation of another, or the same: equal (ignoring case and accents), or
	 * an abbreviation ending with a period whose stem, of at least two letters, begins the other
	 * (e.g. Lam. and Lamarck, Desm. and Desmarest, F.Muell. and Mueller are not, Muell. and Mueller are).
	 *
	 * @param one a surname
	 * @param other another surname
	 * @return true if the same or one abbreviates the other
	 */
	static boolean sameOrAbbreviatedSurname(String one, String other) {
		if (sameOrAbbreviated(stripAccents(one), stripAccents(other))
				|| sameOrAbbreviated(transliterateUmlauts(one), transliterateUmlauts(other))) {
			return true;
		}
		// A compound surname and its last part (de Monet de Lamarck and Lamarck).
		String[] oneWords = one.trim().split("\\s+");
		String[] otherWords = other.trim().split("\\s+");
		if (oneWords.length!=otherWords.length && (oneWords.length==1 || otherWords.length==1)) {
			// The leading words of the compound surname must be lower case (v for von) or of at least three
			// letters (Monet), not a short capitalized word that may be a given name or initials (Ze).
			String[] compound = oneWords.length>1 ? oneWords : otherWords;
			for (int i=0; i<compound.length-1; i++) {
				String word = compound[i];
				if (!(word.equals(word.toLowerCase()) || word.replaceAll("[^\\p{L}]", "").length()>=3) || word.endsWith(".")) {
					return false;
				}
			}
			return sameOrAbbreviatedSurname(oneWords[oneWords.length-1], otherWords[otherWords.length-1]);
		}
		return false;
	}

	private static boolean sameOrAbbreviated(String one, String other) {
		String a = stripAccents(one).toLowerCase().trim();
		String b = stripAccents(other).toLowerCase().trim();
		if (a.replace(".", "").equals(b.replace(".", ""))) { return true; }
		for (String[] pair : new String[][] { { a, b }, { b, a } }) {
			if (pair[0].endsWith(".")) {
				String stem = pair[0].substring(0, pair[0].length()-1).replace(".", "");
				if (stem.length()>=2 && stem.matches("^[a-z' -]+$") && pair[1].replace(".", "").startsWith(stem)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Do two authorship strings name the same authors, in the same order, where some surnames are
	 * abbreviated, initials added or removed, or surname particles (de, von) added or removed, e.g.
	 * "Lam., 1822" and "Lamarck, 1822", "(Desm., 1804)" and "(Desmarest, 1804)", "J.B. de Lamarck" and "Lamarck".
	 * Initials given in both must agree.  Parentheses and years are not compared here.
	 *
	 * @param anAuthor an authorship
	 * @param toOtherAuthor another authorship
	 * @return true if the authors are the same, allowing for abbreviation
	 */
	public static boolean sameAuthorsAbbreviated(String anAuthor, String toOtherAuthor) {
		List<AuthorPart> one = authorParts(anAuthor);
		List<AuthorPart> other = authorParts(toOtherAuthor);
		if (one.isEmpty() || one.size()!=other.size()) { return false; }
		for (int i=0; i<one.size(); i++) {
			AuthorPart a = one.get(i);
			AuthorPart b = other.get(i);
			if (!sameOrAbbreviatedSurname(a.surname, b.surname)) { return false; }
			if (!a.initials.isEmpty() && !b.initials.isEmpty() && !a.initials.equals(b.initials)) { return false; }
			// A one letter abbreviation (L.) is ambiguous, handled elsewhere.
			if (a.surname.matches("^[A-Z]\\.$") != b.surname.matches("^[A-Z]\\.$")) { return false; }
		}
		return true;
	}

	/**
	 * Remove the optional parts of a botanical authorship: the authors before "ex" (e.g. "Planch. ex F.Muell."
	 * gives "F.Muell.") and sanctioning authors after ":" (e.g. "(Pers. : Fr.) P.Kumm." gives "(Pers.) P.Kumm."),
	 * in the parenthetical authorship as well.
	 *
	 * @param authorship a botanical authorship
	 * @return the authorship without ex and sanctioning authors
	 */
	public static String withoutExAndSanctioningAuthors(String authorship) {
		if (authorship==null) { return null; }
		String result = authorship.replaceAll("\\s*:\\s*[^()]*", "");
		result = result.replaceAll("(^|\\(|\\)\\s*)[^()]*?\\s+ex\\.?\\s+", "$1");
		return result.replaceAll("\\s+", " ").trim();
	}

}
