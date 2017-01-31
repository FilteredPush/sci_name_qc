/** 
 * NameUsage.java 
 * 
 * Copyright 2014 Global Biodiversity Information Facility (GBIF)
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.filteredpush.kuration.services.sciname.GBIFService;
import org.gbif.api.model.checklistbank.ParsedName;
import org.gbif.api.model.common.LinneanClassification;
import org.gbif.api.util.ClassificationUtils;
import org.gbif.api.vocabulary.Rank;
import org.gbif.nameparser.NameParser;
import org.gbif.nameparser.UnparsableException;
import org.json.simple.JSONObject;
import org.marinespecies.aphia.v1_0.AphiaRecord;

/**
 * Representation of a usage of a scientific name, suitable for validation of names against 
 * authoritative sources.  Derived from an object that can handle NameUsage serializations 
 * returned from GBIF's API, with extensions to support similar data objects returned by 
 * the WoRMS aphia API, and by IndexFungorum, along with extensions to support metadata about 
 * an original pre-validation record and the comparision between pre- and post- validation 
 * records.  Derived in part from the GBIF api class of the same name.
 * 
 * Has responsibility for NameUsage data objects, serialization of those objects, comparison 
 * between values (e.g. authorships) between name usages, and assertion of constants describing
 * the nature of such comparisons.  Needs re-engineering.
 * 
 * @author mole
 *
 * $Id$ 
 */
public class NameUsage implements LinneanClassification { 
	
	private int key;  // GBIF key
	private int acceptedKey;  // GBIF pointer to accepted name record
	private String datasetKey;  // GBIF dataset
	private int parentKey;  // GBIF pointer to parent record in taxonomic heirarchy
	private String parent;
	private String acceptedName;  // name in current use for scientificName
	private String scientificName;
	private String canonicalName;
	private String authorship;  // authorship string to accompany the scientificName 
	private String acceptedAuthorship; // authorship string to accompany the acceptedName
	private String taxonomicStatus;
	private String rank;
	private String kingdom;  // classification
	private String phylum;   // classification
	private String tclass;   // classification
	private String order;    // classification
	private String family;   // classification
	private String genus;    // classification
	private String subgenus; // classification
	private String species;  // classification, the binomial
	private int numDescendants;
	private String sourceID;
	private String link;
    private boolean synonyms;
	private String sourceAuthority;  // Aphia metadata
	private String unacceptReason;   // Aphia metadata
	private String guid;             // GUID for the name usage
	
	private String matchDescription;  // metadata, description of the match between this name usage and the original
	private double authorshipStringSimilarity;
	private double scientificNameStringSimilarity;
	
	private int inputDbPK;  // Original database primary key for input
	private String originalScientificName;  
	private String originalAuthorship;    
	
	protected AuthorNameComparator authorComparator;
	
	/**
	 * Utility methods, return the value associated with a key from a JSON object, 
	 * or an empty string if the key is not matched.
	 * 
	 * @param json JSONObject to check for key-value pair
	 * @param key the key for which to find the value for.
	 * @return String value or an empty string.
	 */
	public static String getValFromKey(JSONObject json, String key) { 
		if (json==null || json.get(key)==null) { 
			return "";
		} else { 
			return json.get(key).toString();
		}
	}	
	
	private void init() { 
		kingdom = null;  
		phylum = null;   
		tclass = null;   
		order = null;    
		family = null;   
		genus = null;
		subgenus = null;
	}
	
	/**
	 * Set values for higher classification to null if they are empty strings.
	 */
	private void nullBlanks() {
		if (kingdom!=null && kingdom.trim().length()==0) { kingdom = null; }
		if (phylum!=null && phylum.trim().length()==0) { phylum = null; }
		if (tclass!=null && tclass.trim().length()==0) { tclass = null; }
		if (order!=null && order.trim().length()==0) { order = null; }
		if (family!=null && family.trim().length()==0) { family = null; }
		if (genus!=null && genus.trim().length()==0) { genus = null; }
		if (subgenus!=null && subgenus.trim().length()==0) { subgenus = null; }
	}
	
	public NameUsage() { 
		init();
		authorComparator = new ICZNAuthorNameComparator(.75d,.5d);
	}
	
	/**
	 * Construct a NameUsage instance with a given source authority
	 * and authorship comparator.
	 * 
	 * @param sourceAuthority the source authority for the name usage.
	 * @param authorNameComparator the comparator to use when making comparisons
	 * of authors with this name usage.
	 */
	public NameUsage(String sourceAuthority, AuthorNameComparator authorNameComparator) {
		init();
		this.authorComparator = authorNameComparator;
		this.setSourceAuthority(sourceAuthority);
	}	
	
	/**
	 * Construct a NameUsage instance with a specified original scientific name and
	 * authorship, and with sourceAuthority and authorNameComparator.  Expected 
	 * formulation for a call to validate on SciNameServiceParent.
	 * 
	 * @param sourceAuthority
	 * @param authorNameComparator
	 * @param originalScientificName
	 * @param originalAuthorship
	 */
	public NameUsage(String sourceAuthority, AuthorNameComparator authorNameComparator, String originalScientificName, String originalAuthorship) { 
		init();
		this.authorComparator = authorNameComparator;
		this.setSourceAuthority(sourceAuthority);
		setOriginalAuthorship(originalAuthorship);
		setOriginalScientificName(originalScientificName);
	}
	

	
	public NameUsage(JSONObject json) { 
		init();
		if (json!=null) { 
			key = Integer.parseInt(getValFromKey(json,"key"));
			taxonomicStatus = getValFromKey(json,"taxonomicStatus");
			if (taxonomicStatus.equals("ACCEPTED")) { 
			    acceptedKey = Integer.parseInt(getValFromKey(json,"key"));
			    acceptedName = getValFromKey(json,"scientificName");
			} else { 
				try { 
			        acceptedKey = Integer.parseInt(getValFromKey(json,"acceptedKey"));
				} catch (NumberFormatException e) { 
					acceptedKey = 0;
				}
			    acceptedName = getValFromKey(json,"accepted");
			}
			datasetKey = getValFromKey(json,"datasetKey");
			try { 
			    parentKey = Integer.parseInt(getValFromKey(json,"parentKey"));
			} catch (NumberFormatException e) { 
				parentKey = 0;
			}
			numDescendants = Integer.parseInt(getValFromKey(json,"numDescendants"));
			parent = getValFromKey(json,"parent");
			scientificName = getValFromKey(json,"scientificName");
			canonicalName = getValFromKey(json,"canonicalName");
			authorship = getValFromKey(json,"authorship");
			rank = getValFromKey(json,"rank");
			kingdom = getValFromKey(json,"kingdom");
			phylum = getValFromKey(json,"phylum");
			tclass = getValFromKey(json,"clazz");
			order = getValFromKey(json,"order");
			family = getValFromKey(json,"family");
			genus = getValFromKey(json,"genus");
			sourceID = getValFromKey(json,"sourceId");
			link = getValFromKey(json,"link");
            synonyms = Boolean.parseBoolean(getValFromKey(json,"synonym"));
            fixAuthorship();
            nullBlanks();
		}
	}
	
	/**
	 * Construct a NameUsage instance from a gbif checklistbank NameUsage instance.
	 * 
	 * @param record a gbif Checklist API NameUsage.
	 * @see org.gbif.api.model.checklistbank.NameUsage
	 */
	public NameUsage(org.gbif.api.model.checklistbank.NameUsage record) {
		init();
		if (record.getDatasetKey().equals(GBIFService.KEY_GBIFBACKBONE)) { 
		    this.setSourceAuthority("GBIF Backbone Taxonomy");
		} else { 
			this.setSourceAuthority("GBIF Dataset " + record.getDatasetKey());
		}
		this.setScientificName(record.getScientificName());
		this.setRank(record.getRank().getMarker());
		this.setAuthorship(record.getAuthorship());
		this.setAcceptedName(record.getAccepted());
		this.setKingdom(record.getKingdom());
		this.setPhylum(record.getPhylum());
		this.setTclass(record.getClazz());
		this.setOrder(record.getOrder());
		this.setFamily(record.getFamily());
		this.setGenus(record.getGenus());		
		fixAuthorship();
        nullBlanks();
	}	
	
	/**
	 * Construct a NameUsage instance from an AphiaRecord instance,
	 * assumes that the source authority is WoRMS.
	 * 
	 * @param record an AphiaRecord from WoRMS.
	 */
	public NameUsage(AphiaRecord record) {
		init();
		authorComparator = new ICZNAuthorNameComparator(.75d,.5d);
		this.setSourceAuthority("WoRMS (World Register of Marine Species)");
		this.setScientificName(record.getScientificname());
		this.setRank(record.getRank());
		this.setAuthorship(record.getAuthority());
		this.setAcceptedName(record.getValid_name());
		this.setKingdom(record.getKingdom());
		this.setPhylum(record.getPhylum());
		this.setTclass(record.get_class());
		this.setOrder(record.getOrder());
		this.setFamily(record.getFamily());
		this.setGenus(record.getGenus());
		this.setGuid("urn:lsid:marinespecies.org:taxname:" + Integer.toString(record.getAphiaID()));
		this.setTaxonomicStatus(record.getStatus());
		this.setUnacceptReason(record.getUnacceptreason());
		fixAuthorship();
        nullBlanks();
	}	
	
	public static String csvHeaderLine() { 
		return "\"scientificName\",\"canonicalName\",\"authorship\"," +
				"\"taxonomicStatus\",\"acceptedName\",\"rank\"," +
				"\"kingdom\",\"phylum\",\"class\",\"order\",\"family\",\"genus\"," +
		        "\"key\",\"acceptedKey\",\"datasetKey\"," +
		        "\"parentKey\",\"parent\",\"childTaxaCount\",\"sourceid\",\"link\"" +
				"\n";
	}
	
	public String toCSVLine() { 
		StringBuffer result = new StringBuffer();
		result.append('"').append(scientificName).append("\",");
		result.append('"').append(canonicalName).append("\",");
		result.append('"').append(authorship).append("\",");
		result.append('"').append(taxonomicStatus).append("\",");
		result.append('"').append(acceptedName).append("\",");
		result.append('"').append(rank).append("\",");
		result.append('"').append(kingdom).append("\",");
		result.append('"').append(phylum).append("\",");
		result.append('"').append(tclass).append("\",");
		result.append('"').append(order).append("\",");
		result.append('"').append(family).append("\",");
		result.append('"').append(genus).append("\",");
		result.append(key).append(",");
		result.append(acceptedKey).append(",");
		result.append('"').append(datasetKey).append("\",");
		result.append(parentKey).append(",");
		result.append('"').append(parent).append("\",");
		result.append('"').append(numDescendants).append("\",");
		result.append('"').append(sourceID).append("\",");
		result.append('"').append(link).append("\",");
		
		result.append("\n");
		return result.toString();
	}

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * @return the acceptedKey
	 */
	public int getAcceptedKey() {
		return acceptedKey;
	}

	/**
	 * @param acceptedKey the acceptedKey to set
	 */
	public void setAcceptedKey(int acceptedKey) {
		this.acceptedKey = acceptedKey;
	}

	/**
	 * @return the acceptedName or an empty string if acceptedName is null.
	 */
	public String getAcceptedName() {
		if (acceptedName==null) { 
			return "";
		}
		return acceptedName;
	}

	/**
	 * @param acceptedName the acceptedName to set
	 */
	public void setAcceptedName(String acceptedName) {
		this.acceptedName = acceptedName;
	}

	/**
	 * @return the datasetKey
	 */
	public String getDatasetKey() {
		return datasetKey;
	}

	/**
	 * @param datasetKey the datasetKey to set
	 */
	public void setDatasetKey(String datasetKey) {
		this.datasetKey = datasetKey;
	}

	/**
	 * @return the parentKey
	 */
	public int getParentKey() {
		return parentKey;
	}

	/**
	 * @param parentKey the parentKey to set
	 */
	public void setParentKey(int parentKey) {
		this.parentKey = parentKey;
	}

	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * @return the scientificName or an empty string if scientificName is null
	 */
	public String getScientificName() {
		if (scientificName==null) { 
			return "";
		}
		return scientificName;
	}

	/**
	 * @param scientificName the scientificName to set
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	/**
	 * @return the canonicalName
	 */
	public String getCanonicalName() {
		return canonicalName;
	}

	/**
	 * @param canonicalName the canonicalName to set
	 */
	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

	/**
	 * @return the status
	 */
	public String getTaxonomicStatus() {
		if(taxonomicStatus==null) { 
			return "";
		}
		return taxonomicStatus;
	}

	/**
	 * @param status the status to set
	 */
	public void setTaxonomicStatus(String status) {
		this.taxonomicStatus = status;
	}

	/**
	 * @return the numDescendants
	 */
	public int getNumDescendants() {
		return numDescendants;
	}

	/**
	 * @param numDescendants the numDescendants to set
	 */
	public void setNumDescendants(int numDescendants) {
		this.numDescendants = numDescendants;
	}

	/**
	 * @return the sourceID
	 */
	public String getSourceID() {
		return sourceID;
	}

	/**
	 * @param sourceID the sourceID to set
	 */
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * @return the kingdom
	 */
	public String getKingdom() {
		return kingdom;
	}

	/**
	 * @param kingdom the kingdom to set
	 */
	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	/**
	 * @return the authorship or an empty string if authorship is null
	 */
	public String getAuthorship() {
		if (authorship==null) { 
			return "";
		} else { 
		   return authorship;
		}
	}

	/**
	 * @param authorship the authorship to set
	 */
	public void setAuthorship(String authorship) {
		this.authorship = authorship;
	}

	/**
	 * @return the phylum
	 */
	public String getPhylum() {
		return phylum;
	}

	/**
	 * @param phylum the phylum to set
	 */
	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	/**
	 * @return the tclass
	 */
	public String getTclass() {
		return tclass;
	}

	/**
	 * @param tclass the tclass to set
	 */
	public void setTclass(String tclass) {
		this.tclass = tclass;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * @return the genus
	 */
	public String getGenus() {
		return genus;
	}

	/**
	 * @param genus the genus to set
	 */
	public void setGenus(String genus) {
		this.genus = genus;
	}

    //insert by Tianhong
    public void setSynonyms (boolean synonyms){
        this.synonyms = synonyms;
    }

    public boolean getSynonyms(){
        return synonyms;
    }
    
	/**
	 * @return the matchDescription
	 */
	public String getMatchDescription() {
		return matchDescription;
	}

	/**
	 * @param matchDescription the matchDescription to set
	 */
	public void setMatchDescription(String matchDescription) {
		this.matchDescription = matchDescription;
	}

	/**
	 * @return the authorshipStringSimilarity
	 */
	public double getAuthorshipStringSimilarity() {
		return authorshipStringSimilarity;
	}

	/**
	 * @param authorshipStringSimilarity the authorshipStringSimilarity to set
	 */
	public void setAuthorshipStringSimilarity(double authorshipStringSimilarity) {
		this.authorshipStringSimilarity = authorshipStringSimilarity;
	}

	/**
	 * @return the inputDbPK
	 */
	public int getInputDbPK() {
		return inputDbPK;
	}

	/**
	 * @param inputDbPK the inputDbPK to set
	 */
	public void setInputDbPK(int inputDbPK) {
		this.inputDbPK = inputDbPK;
	}

	/**
	 * @return the originalScientificName or an empty string if originalScientificName is null
	 */
	public String getOriginalScientificName() {
		if (originalScientificName == null) { 
			return "";
		}
		return originalScientificName;
	}

	/**
	 * @param originalScientificName the originalScientificName to set
	 */
	public void setOriginalScientificName(String originalScientificName) {
		this.originalScientificName = originalScientificName;
	}

	/**
	 * @return the originalAuthorship or an empty string if originalAuthorship is null
	 */
	public String getOriginalAuthorship() {
		if (originalAuthorship==null) {
			return "";
		}
		return originalAuthorship;
	}

	/**
	 * @param originalAuthorship the originalAuthorship to set
	 */
	public void setOriginalAuthorship(String originalAuthorship) {
		this.originalAuthorship = originalAuthorship;
	}

	/**
	 * @return the acceptedAuthorship or an empty string if acceptedAuthorship is null.
	 */
	public String getAcceptedAuthorship() {
		if (acceptedAuthorship==null) { 
			return "";
		}
		return acceptedAuthorship;
	}

	/**
	 * @param acceptedAuthorship the acceptedAuthorship to set
	 */
	public void setAcceptedAuthorship(String acceptedAuthorship) {
		this.acceptedAuthorship = acceptedAuthorship;
	}

	/**
	 * @return the scientificNameStringSimilarity
	 */
	public double getScientificNameStringEditDistance() {
		return scientificNameStringSimilarity;
	}

	/**
	 * @param scientificNameStringSimilarity the scientificNameStringSimilarity to set
	 */
	public void setScientificNameStringEditDistance(
			double scientificNameStringEditDistance) {
		this.scientificNameStringSimilarity = scientificNameStringEditDistance;
	} 
	
	
	/**
	 * @return the authorshipStringSimilarity
	 */
	public double getAuthorshipStringEditDistance() {
		return authorshipStringSimilarity;
	}

	/**
	 * @param authorshipStringSimilarity the authorshipStringSimilarity to set
	 */
	public void setAuthorshipStringEditDistance(double authorshipStringEditDistance) {
		this.authorshipStringSimilarity = authorshipStringEditDistance;
	}	

	/**
	 * @return the guid for the NameUsage, or an empty string if the guid is null
	 */
	public String getGuid() {
		if (guid==null) { 
			return ""; 
		} else { 
		    return guid;
		}
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		if (guid.equals("http://api.gbif.org/v1/species/")) { 
			guid = null;
		}
		this.guid = guid;
	}
	
	/**
	 * @return the authorComparator
	 */
	public AuthorNameComparator getAuthorComparator() {
		return authorComparator;
	}

	/**
	 * @param authorComparator the authorComparator to set
	 */
	public void setAuthorComparator(AuthorNameComparator authorComparator) {
		this.authorComparator = authorComparator;
	}	
	
	/**
	 * Fix certain known cases of errors in the formulation of an 
	 * authorship string, sensitive to relevant nomenclatural code.
	 * Remove authorship from scientific name if present.
	 */
	public void fixAuthorship() { 
		if (authorship!=null) { 
			if (scientificName != null && scientificName.contains(authorship)) { 
				scientificName.replace(authorship, "");
			    scientificName = scientificName.trim();
			}
			authorship = authorship.trim();
			if (kingdom!=null && kingdom.equals("Animalia")) { 
				if (ICZNAuthorNameComparator.containsParenthesies(authorship)) { 
					// Fix pathological case sometimes returned by COL: Author (year)
					// which should be (Author, year).
					
					//^([A-Za-z., ]+)[, ]*\(([0-9]{4})\)$
					Pattern p = Pattern.compile("^([A-Za-z., ]+)[, ]*\\(([0-9]{4})\\)$");
					Matcher matcher = p.matcher(authorship);
					if (matcher.matches()) { 
					   StringBuffer retval = new StringBuffer();
					   retval.append("(");
					   retval.append(matcher.group(1).trim());
					   retval.append(", ");
					   retval.append(matcher.group(2));
					   retval.append(")");
	 				   authorship = retval.toString().trim();
					}
				}
			}
		}
		
		// Check to see if acceptedName contains the acceptedNameAuthorship.
		if (getAcceptedName().length()>0 && getAcceptedAuthorship().length()==0 ) { 
			NameParser parser = new NameParser();
	        ParsedName parse = null;
	        try {
				parse = parser.parse(getAcceptedName());
				if (parse!=null) { 
                   String author = parse.authorshipComplete();
				   setAcceptedAuthorship(author);
				   setAcceptedName(getAcceptedName().substring(0, getAcceptedName().lastIndexOf(author)).trim());
				}
			} catch (UnparsableException e) {
				// couldn't parse
			}	
	                
		}
		
	}

	@Override
	public String getClazz() {
		return this.tclass;
	}

	@Override
	public void setClazz(String clazz) {
		this.tclass = clazz;
	}

	@Override
	public String getSpecies() {
		return this.species;
	}

	@Override
	public void setSpecies(String species) {
		this.species = species;
		
	}

	@Override
	public String getSubgenus() {
		return this.subgenus;
	}

	@Override
	public void setSubgenus(String subgenus) {
		this.subgenus = subgenus;
	}

	@Override
	public String getHigherRank(Rank rank) {
		return ClassificationUtils.getHigherRank(this, rank);
	}

	/**
	 * @return the sourceAuthority
	 */
	public String getSourceAuthority() {
		return sourceAuthority;
	}

	/**
	 * @param sourceAuthority the sourceAuthority to set
	 */
	public void setSourceAuthority(String sourceAuthority) {
		this.sourceAuthority = sourceAuthority;
	}

	/**
	 * @return the unacceptReason
	 */
	public String getUnacceptReason() {
		return unacceptReason;
	}

	/**
	 * @param unacceptReason the unacceptReason to set
	 */
	public void setUnacceptReason(String unacceptReason) {
		this.unacceptReason = unacceptReason;
	}	
	
}
