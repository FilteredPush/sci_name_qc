/**
 * ICZNAuthorNameComparator.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Make comparisons between pairs of scientificNameAuthor strings, under the assumption
 * that both strings are from names covered by the botanical code.
 * 
 * @author mole
 *
 */
public class ICNafpAuthorNameComparator extends AuthorNameComparator {
	
	private static final Log logger = LogFactory.getLog(ICNafpAuthorNameComparator.class);
	
	/**
	 *  Threshold of similarity (0-1) over which strong similarity is asserted.
	 */
	protected double similarityThreshold = .75d;
	/**
	 *  Threshold of similarity (0-1) over which a weak similarity is asserted.
	 */
    protected double weakThreshold = .5d;
	
    @Override
	public double getSimilarityThreshold() {
		return similarityThreshold;
	}

	/**
     * Constructor for an ICNapf author name comparator that specifies values for similarity
     * assertions.
     * 
     * @param similarityThreshold in range 0 to 1 for marking comparisons as similar
     * @param weakThreshold in range 0 to 1 for marking comparisons as weakly similar
     */
    public ICNafpAuthorNameComparator(double similarityThreshold, double weakThreshold) { 
    	this.similarityThreshold = similarityThreshold;
    	this.weakThreshold = weakThreshold;
    }
    
	/* (non-Javadoc)
	 * @see edu.harvard.mcz.nametools.AuthorNameComparator#compare(java.lang.String, java.lang.String)
	 */
	@Override
	public NameComparison compare(String anAuthor, String toOtherAuthor) {
		
		NameComparison result = new NameComparison(anAuthor, toOtherAuthor);
		
        result.setMatchType(NameComparison.MATCH_ERROR);
		if (anAuthor==null || toOtherAuthor==null) {
		    result.setMatchType(NameComparison.MATCH_ERROR);
		} else { 
			if (anAuthor.equals(toOtherAuthor) 
					|| anAuthor.toLowerCase().replaceAll("[ .,]", "").equals(toOtherAuthor.toLowerCase().replaceAll("[ .,]", ""))) 
			{ 
				result.setMatchType(NameComparison.MATCH_EXACT);
				result.setSimilarity(1.0d);
			} else {
				double similarity = AuthorNameComparator.calulateSimilarityOfAuthor(anAuthor, toOtherAuthor);
				result.setSimilarity(similarity);
				if (anAuthor.length()==0 && toOtherAuthor.length()> 0 ) { 
					result.setMatchType(NameComparison.MATCH_ADDSAUTHOR);
				} else { 
					if (similarity > similarityThreshold) { 
						result.setMatchType(NameComparison.MATCH_AUTHSIMILAR);
					} else if (similarity > weakThreshold) { 
						result.setMatchType(NameComparison.MATCH_DISSIMILAR);
					} else { 
						result.setMatchType(NameComparison.MATCH_STRONGDISSIMILAR);
					}
					double similarityAlpha = AuthorNameComparator.calulateSimilarityOfAuthorAlpha(anAuthor, toOtherAuthor);
					boolean parenSame = ICNafpAuthorNameComparator.calculateHasParen(anAuthor)==ICNafpAuthorNameComparator.calculateHasParen(toOtherAuthor);
					
					
					List<String> anAuthorBits = tokenizeAuthorship(anAuthor);
					List<String> toOtherAuthorBits = tokenizeAuthorship(toOtherAuthor);
					if (anAuthorBits.size() != toOtherAuthorBits.size()) { 
						result.setMatchType(NameComparison.MATCH_PARTSDIFFER);
					} else { 
						if (ICNafpAuthorNameComparator.matchedOnWordsInTokens(anAuthor, toOtherAuthor)) { 
							result.setMatchType(NameComparison.MATCH_SAMEBUTABBREVIATED);
						}
					}
					
					if (anAuthor.contains(" ex ") && !toOtherAuthor.contains(" ex ")) { 
						result.setMatchType(NameComparison.MATCH_PARTSDIFFER);
					}
					if (anAuthor.contains(":") && !toOtherAuthor.contains(":")) { 
						result.setMatchType(NameComparison.MATCH_PARTSDIFFER);
					}
					if (anAuthor.contains("(") && !toOtherAuthor.contains("(")) { 
						result.setMatchType(NameComparison.MATCH_PARTSDIFFER);
					}
					
					if (!parenSame && (similarityAlpha==1d)) { 
						result.setMatchType(NameComparison.MATCH_PARENTHESIESDIFFER);
					}
				}
			}
		}
		return result;
	}	
	
	/**
	 * Compare the two author strings to see if they look like differeing abbreviations
	 * of the same set of authors in the same semantic positions (comparing each 
	 * parenthetical author, ex author, sanctioning author, etc separately.
	 * 
	 * @param anAuthor 
	 * @param toOtherAuthor 
	 * 
	 * @return true if each semantic piece of the two author strings is the same, or if 
	 * each semantic piece appears to be an abbreviation of the corresponding semantic piece
	 * otherwise return false. 
	 */
	public static boolean matchedOnWordsInTokens(String anAuthor, String toOtherAuthor) {
		logger.debug("in matchedOnWordsInTokens(" + anAuthor + "," + toOtherAuthor + ")");
		// TODO: Break this into several methods, called from this method, each 
		// method with its own unit tests, this assembly is large enough to be difficult to 
		// debug when tests fail.
		boolean result = false;
		List<String> anAuthorBits = tokenizeAuthorship(anAuthor);
		List<String> toOtherAuthorBits = tokenizeAuthorship(toOtherAuthor);
		if (anAuthorBits.size() == toOtherAuthorBits.size()) { 
			Iterator<String> iA = anAuthorBits.iterator();
			Iterator<String> iO = toOtherAuthorBits.iterator();
			boolean foundMissmatch = false;
			while (iA.hasNext()) { 
				String anAuthorBit = iA.next();
				String toOtherAuthorBit = iO.next();
	    		if (shorterButNotAbbreviation(anAuthorBit, toOtherAuthorBit)) { 
	    			// declare a missmatch.
	    			foundMissmatch = true;
	    			logger.debug("Missmatch shorter but not abbreviation: " + anAuthorBit + ":" + toOtherAuthorBit);
	    		}
				if (!anAuthorBit.equals(toOtherAuthorBit)) {
					// Check if initials are the same
					if (initalsAreDifferent(anAuthorBit, toOtherAuthorBit)) { 
						foundMissmatch = true;
	    				 logger.debug("Missmatch: " + anAuthorBit + ":" + toOtherAuthorBit);
					}

					// remove punctuation.
		 			anAuthorBit = anAuthorBit.replaceAll("[^A-Za-z ::alpha::]", " ");
					toOtherAuthorBit = toOtherAuthorBit.replaceAll("[^A-Za-z ::alpha::]", " ");
					if (anAuthorBit.trim().equals("L") && toOtherAuthorBit.equals("Lamarck")) { 
						// Special case, botany, fail
						foundMissmatch = true;
	    				logger.debug("Missmatch: " + anAuthorBit + ": " + toOtherAuthorBit);
					}
					if (anAuthorBit.trim().equals("Lamarck") && toOtherAuthorBit.equals("L")) { 
						// Special case, botany, fail
						foundMissmatch = true;
	    				logger.debug("Missmatch: " + anAuthorBit + ": " + toOtherAuthorBit);
					}
					// remove single letters (except if authorbit is only a single letter) 
					if (!anAuthorBit.trim().matches("^[A-Z]$")) { 
					   anAuthorBit = anAuthorBit.replaceAll("^[A-Z] ", " ");
					   anAuthorBit = anAuthorBit.replaceAll(" [A-Z] ", " ");
					   anAuthorBit = anAuthorBit.replaceAll(" [A-Z]$", " ");
					}
					if (!toOtherAuthorBit.trim().matches("^[A-Z]$")) { 
					   toOtherAuthorBit = toOtherAuthorBit.replaceAll("^[A-Z] ", " ");
					   toOtherAuthorBit = toOtherAuthorBit.replaceAll(" [A-Z] ", " ");
					   toOtherAuthorBit = toOtherAuthorBit.replaceAll(" [A-Z]$", " ");
					}
					anAuthorBit = anAuthorBit.trim();
					toOtherAuthorBit = toOtherAuthorBit.trim();
					// remove double spaces
					anAuthorBit = anAuthorBit.replaceAll("  ", " ").trim();
					toOtherAuthorBit = toOtherAuthorBit.replaceAll("  ", " ").trim();
					logger.debug(anAuthorBit);
					logger.debug(toOtherAuthorBit);
					List<String> anAuthorSubBits = Arrays.asList(anAuthorBit.trim().split(" "));
					List<String> toOtherAuthorSubBits = Arrays.asList(toOtherAuthorBit.trim().split(" "));
					if (!knownMatch(anAuthorBit, toOtherAuthorBit)) { 
						if (anAuthorSubBits.size()!=toOtherAuthorSubBits.size()) {
							foundMissmatch = true;
							logger.debug("Missmatch: " + anAuthorBit.trim() + " " + anAuthorSubBits.size() + ": " + toOtherAuthorBit.trim() + " " + toOtherAuthorSubBits.size());
						} else { 
							Iterator<String> iAsb = anAuthorSubBits.iterator();
							Iterator<String> iOsb = toOtherAuthorSubBits.iterator();
							while (iAsb.hasNext()) { 
								String subBit = iAsb.next();
								String otherSubBit = iOsb.next();
								if (!compareSameOrStartsWith(subBit,otherSubBit)) { 
									foundMissmatch = true;
									logger.debug("Missmatch: " + subBit + ": " + otherSubBit);
								}
							}
						}
					}
				}
			}
			result = !foundMissmatch;
		}
		logger.debug("matchedOnWordsInTokens(): " + result);
		return result;
	}
	
	/**
	 * Given a string, return any initials from that string.
	 * @param name
	 * @return
	 */
	public static String extractInitials(String name) { 
		StringBuffer result = new StringBuffer();
		Pattern p = Pattern.compile("(^| |\\()[A-Z]\\.");
		Matcher matcher = p.matcher(name.replaceAll("\\.", ". "));
		while (matcher.find()) { 
			String match = matcher.group();
			result.append(match.replaceAll("[^A-Z]", ""));
		}
		return result.toString();
	}
	
	/**
	 * Compare to strings to see if they are the same or if the longer starts with
	 * the shorter.  Special case, "L." is not same or starts with "Lamarck".  
	 * 
	 * @param aString
	 * @param anotherString
	 * @return true if the two strings are the same, or if the longer of the two strings
	 * starts with the shorter of the two strings.
	 */
	public static boolean compareSameOrStartsWith(String aString, String anotherString) { 
		boolean result = false;
		if (aString.equals(anotherString)) { 
		   result = true;	
		} else {
			String shorter = aString;
			String longer = anotherString;
			if (aString.length() > anotherString.length()) { 
				shorter = anotherString;
				longer = aString;
			}
			if (longer.replace(".", "").startsWith(shorter.replace(".", ""))) { 
				result = true;
			}
			if (knownMatch(shorter.replace(".", ""), longer.replace(".",""))) { 
				result = true;
			}
		    // Special case, for botany, L doesn't abbreviate Lamarck, only Linnaeus:
		    if (shorter.replace(".", "").equals("L") && longer.equals("Lamarck")) { 
		    	result = false;
		    }
		}
		return result;
	}
	
	/**
	 * Given a botanical authorship string, split it into a list of component 
	 * authors on parenthetical authors, ex authors, and sanctioning authors.
	 * 
	 * @param authorship
	 * @return a list of authorship strings representing the components of the 
	 * authorship string.
	 */
     public static List<String> tokenizeAuthorship(String authorship) { 
		ArrayList<String> bits = new ArrayList<String>();
		ArrayList<String> subbits = new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();
		
		if (authorship==null || authorship.length()==0) { 
			return result;
		}
		
		// separate out parenthetical author
		logger.debug(authorship);
		if (authorship.matches("^\\(.*\\).+$")) { 
			String[] parBits = authorship.split("\\)");
			logger.debug(parBits.length);
			logger.debug(parBits[0]);
			bits.add(parBits[0].replaceFirst("^\\(", ""));
			bits.add(parBits[1]);
		} else { 
			bits.add(authorship);
		}

		// separate out ex author
		Iterator<String> i = bits.iterator();
		while (i.hasNext()) { 
			String bit = i.next();
			if (bit.contains(" ex ")) { 
				String[] exBits = bit.split(" ex ");
				logger.debug(exBits.length);
				logger.debug(exBits[0]);
				logger.debug(exBits[1]);
				subbits.add(exBits[0]);
				subbits.add(exBits[1]);
			} else { 
				logger.debug(bit);
				subbits.add(bit);
			}

		}

		// separate out sanctioning author
		Iterator<String> ir = subbits.iterator();
		while (ir.hasNext()) { 
			String bit = ir.next();
			if (bit.contains(":")) { 
				String[] exBits = bit.split(":");
				result.add(exBits[0]);
				result.add(exBits[1]);
			} else { 
				result.add(bit);
			}

		}		
		// strip any leading/trailing spaces
		for (int j=0; j<result.size(); j++) { 
			result.set(j, result.get(j).trim());
		}
		
		return result;
	}
     
    /**
     * Are anAuthor and anotherAuthor a known match for a small set of 
     * unusual standard abbreviations linked to name and initials 
     * (e.g. DC. for de Candolle). 
     * 
     * @param anAuthor
     * @param anotherAuthor
     * @return true if anAuthor and anotherAuthor form a known pair of
     * name and abbreviation.
     */
 	public static boolean knownMatch(String anAuthor, String anotherAuthor) {
		boolean result = false;
		
		HashMap<String,String> knowns = new HashMap<String,String>();
		knowns.put("DC", "de Candolle");
		knowns.put("Mendoza-García", "M Mendoza G");
		knowns.put("L f", "C Linnaeus f");
		knowns.put("Müll Arg", "J. Müller Arg");
		knowns.put("Ruiz","H Ruíz L");
		knowns.put("Germ","J N E Germain S-P");
		knowns.put("Müll Berol","K Müller Berol");
		knowns.put("Chick","J W Chickering, Jr");
		knowns.put("Velez-Nauer","C Vélez N");
		knowns.put("Ibarra-Manr","G Ibarra M");
		knowns.put("Vera-Caletti","P Vera C");
		knowns.put("Cházaro","M J Chazaro B");
		knowns.put("Sahagun","E Sahagún G");
		knowns.put("Marrero Rodr","A Marrero R");
		knowns.put("Magalh","C T Magalhães G");
		knowns.put("Lucas Rodr","R L Rodriguez C");
		knowns.put("M Schultz","Schultz, M");
		knowns.put("Hechav","L Hechavarría S");
		knowns.put("FrancGut","J A Francisco Gut");
		knowns.put("Karnk","A Karnkowska-Ish");
		knowns.put("Sm","J E Smith");
		knowns.put("Hue","A-M Hue");
		knowns.put("Day","M A Day");
		knowns.put("Fr","E M Fries");
		
		String m1 = knowns.get(anAuthor.replace(".", ""));
		String m2 = knowns.get(anotherAuthor.replace(".", ""));
		
		if ((m1!=null && m1.equals(anotherAuthor)) || (m2!=null && m2.equals(anAuthor))) { 
			result = true;
		}
		return result;
	}    
 	
 	/**
	 * If initials are present in both strings, true false if they are different, otherwise
	 * return true.
	 * 
	 * @param anAuthorBit
	 * @param toOtherAuthorBit
	 * @return false if initials are present in both author bits, otherwise return true
	 */
	public static boolean initalsAreDifferent(String anAuthorBit, String toOtherAuthorBit) {
		boolean result = false;
		String initA = extractInitials(anAuthorBit);
		String initO = extractInitials(toOtherAuthorBit);
		if (!initA.equals(initO)) { 
			if (initA.length()==initO.length() && initA.length()>0) { 
				result = true;
			    logger.debug("Different Initials: " + initA + ":" + initO);
			}
		}
		return result;
	}

	/**
	 * Given two strings, does the shorter appear to not be an abbreviation.
	 * 
	 * @param shorter a string (doesn't have to be the shorter of the two).
	 * @param longer string to compare with (doesn't have to be the longer of the two).
	 * 
	 * @return true if one string is shorter than the other and doesn't end with a period, 
	 *  and the longer of the two doesn't contain a period, thus the shorter doesn't 
	 *  appear to be an abbreviation of the longer.  Otherwise returns false (including
	 *  if both strings are the same length).
	 */
	public static boolean shorterButNotAbbreviation(String shorter, String longer) {
		boolean result = false;
		String s = shorter; 
		String l = longer;
		if (s.length() > l.length()) { 
			shorter = l;
		    longer = s;
	    }
		if (shorter.length()<longer.length()) { 
			if (!shorter.endsWith(".") && !longer.contains(".")) {
				// if the shorter of the two bits doesn't end with a period 
				// and the longer of the two bits doesn't contain a period 
			    // then assume that the shorter isn't an abbreviation. 
				result = true;
				logger.debug("Missmatch: " + shorter + ":" + longer);
			}
		}
		if (shorter.length()==longer.length()) { 
			//result = false;
		}
		return result;
	}
	

}
