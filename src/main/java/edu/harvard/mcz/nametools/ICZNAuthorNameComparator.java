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

/**
 * Make comparisons between pairs of scientificNameAuthor strings, under the assumption
 * that both strings are from names covered by the zoological code.
 * 
 * @author mole
 *
 */
public class ICZNAuthorNameComparator extends AuthorNameComparator {
	
	/**
	 *  Threshold of similarity (0-1) over which strong similarity is asserted.
	 */
	protected double similarityThreshold = .75d;
	/**
	 *  Threshold of similarity (0-1) over which a weak similarity is asserted.
	 */
    protected double weakThreshold = .5d;
	
    /**
     * Constructor for an ICZN author name comparator that specifies values for similarity
     * assertions.
     * 
     * @param similarityThreshold in range 0 to 1 for marking comparisons as similar
     * @param weakThreshold in range 0 to 1 for marking comparisons as weakly similar
     */
    public ICZNAuthorNameComparator(double similarityThreshold, double weakThreshold) { 
    	this.similarityThreshold = similarityThreshold;
    	this.weakThreshold = weakThreshold;
    }
    
    @Override
	public double getSimilarityThreshold() {
		return similarityThreshold;
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
			} else if (anAuthor.equals(toOtherAuthor) 
					|| anAuthor.toLowerCase().replaceAll("[ \\[\\].,]", "").equals(toOtherAuthor.toLowerCase().replaceAll("[ \\[\\].,]", ""))) 
			{ 
				result.setMatchType(NameComparison.MATCH_EXACT);
				result.setSimilarity(1.0d);
			} else {
				if (anAuthor.length()==0 && toOtherAuthor.length()> 0 ) { 
					result.setMatchType(NameComparison.MATCH_ADDSAUTHOR);
				} else { 
					double similarity = ICZNAuthorNameComparator.calulateSimilarityOfAuthor(anAuthor, toOtherAuthor);
					result.setSimilarity(similarity);
					if (similarity > similarityThreshold) { 
						result.setMatchType(NameComparison.MATCH_AUTHSIMILAR);
					} else { 
						result.setMatchType(NameComparison.MATCH_DISSIMILAR);
					}
					double similarityAlpha = ICZNAuthorNameComparator.calulateSimilarityOfAuthorAlpha(anAuthor, toOtherAuthor);
					double similarityYear = ICZNAuthorNameComparator.calulateSimilarityOfAuthorYear(anAuthor, toOtherAuthor);
					boolean parenSame = ICZNAuthorNameComparator.calculateHasParen(anAuthor)==ICZNAuthorNameComparator.calculateHasParen(toOtherAuthor);
					
					if ((similarityAlpha==1d) && parenSame && similarityYear==0d) { 
						if (ICZNAuthorNameComparator.calculateHasYear(anAuthor) && !ICZNAuthorNameComparator.calculateHasYear(toOtherAuthor)) { 
							result.setMatchType(NameComparison.MATCH_EXACTMISSINGYEAR);
						}
						if (!ICZNAuthorNameComparator.calculateHasYear(anAuthor) && ICZNAuthorNameComparator.calculateHasYear(toOtherAuthor)) { 
							result.setMatchType(NameComparison.MATCH_EXACTADDSYEAR);
						}
					} else { 
					    if (parenSame && (similarityAlpha==1d) && (similarityYear < 1d) ) { 
					    	result.setMatchType(NameComparison.MATCH_EXACTDIFFERENTYEAR);
					   }
					}
					if (parenSame && (similarityYear==1d) && similarityAlpha > weakThreshold ) { 
						result.setMatchType(NameComparison.MATCH_WEAKEXACTYEAR);
					}
					if (parenSame && (similarityYear==1d) && similarityAlpha > similarityThreshold ) { 
						result.setMatchType(NameComparison.MATCH_SIMILAREXACTYEAR);
					}
					if (parenSame && (similarityYear==1d) && similarityAlpha < 1d && anAuthor.contains("Sowerby,") && toOtherAuthor.contains("Sowerby I")) { 
						result.setMatchType(NameComparison.MATCH_SOWERBYEXACTYEAR);
					}
					if (parenSame && (similarityYear==1d) && similarityAlpha < 1d && anAuthor.contains("L.") && (toOtherAuthor.contains("Lamarck") || toOtherAuthor.contains("Linn"))) { 
						result.setMatchType(NameComparison.MATCH_L_EXACTYEAR);
					}
					if (parenSame && (similarityYear==0d) && similarityAlpha < 1d && anAuthor.contains("L.") && (toOtherAuthor.contains("Lamarck") || toOtherAuthor.contains("Linn"))) { 
						result.setMatchType(NameComparison.MATCH_L);
					}
					if (parenSame && (similarityYear==1d || similarityYear==0d) && similarityAlpha < 1d && ICZNAuthorNameComparator.knownAbbreviation(anAuthor, toOtherAuthor)) {
						if (similarityYear==0d) { 
						    if (!ICZNAuthorNameComparator.calculateHasYear(anAuthor) && ICZNAuthorNameComparator.calculateHasYear(toOtherAuthor)) { 
							   result.setMatchType(NameComparison.MATCH_EXACTADDSYEAR);
						    }
						} else { 
						    result.setMatchType(NameComparison.MATCH_SAMEBUTABBREVIATED);
						}
					}					
					if (!parenSame && (similarityYear==1d && similarityAlpha==1d)) { 
						result.setMatchType(NameComparison.MATCH_PARENTHESIESDIFFER);
					}
					if (!parenSame && (similarityYear<1d && similarityAlpha==1d)) { 
						result.setMatchType(NameComparison.MATCH_PARENYEARDIFFER);
					}
					if (result.getMatchType()!=null && result.getRemark()==null) { 
						result.setRemark(result.getMatchType());
					}
				}
			}
		}
		if (result.getRemark()==null) { 
			result.setRemark(result.getMatchType());
		}
		return result;
	}	
	
	/**
	 * Compare the non-numeric (i.e. removing the year if present) parts of two authorship strings.
	 * 
	 * @param anAuthor for comparison
	 * @param toOtherAuthor to compare with anAuthor
	 * @return similarity (in the range 0 to 1) between the two strings.
	 */
	public static double calulateSimilarityOfAuthorYear(String anAuthor, String toOtherAuthor) { 
	    String au = toOtherAuthor.toLowerCase().replaceAll("[^0-9]", "");
	    String au1 = anAuthor.toLowerCase().replaceAll("[^0-9]", "");
	    return AuthorNameComparator.stringSimilarity(au, au1);
    }
	
	/**
	 * Return true if anAuthor contains a 4 digit integer that might be a year.
	 * 
	 * @param anAuthor to check.
	 * @return true if author contains 4 numeric digits.
	 */
	public static boolean containsYear(String anAuthor) { 
	    String au = anAuthor.toLowerCase().replaceAll("[^0-9]", "");
	    return (au.length()==4);
    }	
	
	/**
	 * Return true if an author contains ( and )
	 * 
	 * @param anAuthor to check
	 * @return true if author contains parenthesizes.
	 */
	public static boolean containsParenthesies(String anAuthor) { 
		return ( anAuthor.contains("(") && anAuthor.contains(")") );
	}	
	
	public static boolean knownAbbreviation(String anAuthor, String toOtherAuthor) { 
		boolean result = false;
	    String a = anAuthor.replaceAll("[^A-Za-z'é]", "");
		String oth = toOtherAuthor.replaceAll("[^A-Za-z'é]", "");
		String longer = oth;
		String shorter = a;
		if (longer.length()<shorter.length()) { 
			shorter = oth;
			longer = a;
		}
		
		if (shorter.equals("Linne") && longer.equals("Linnaeus")) { result = true; } 
		if (shorter.equals("Linné") && longer.equals("Linnaeus")) { result = true; } 
		if (shorter.equals("Linnæus") && longer.equals("Linnaeus")) { result = true; } 
		if (shorter.equals("Linné") && longer.equals("Linnæus")) { result = true; } 
		if (shorter.equals("d'Orb.") && longer.equals("d'Orbigny")) { result = true; } 
		return result;
	}
}
