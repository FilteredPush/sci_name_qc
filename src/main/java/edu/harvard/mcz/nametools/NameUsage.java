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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.GBIFService;
import org.gbif.api.model.common.LinneanClassification;
import org.gbif.api.util.ClassificationUtils;
import org.gbif.api.vocabulary.Rank;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.NomCode;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.UnparsableNameException;
import org.json.simple.JSONObject;
import org.marinespecies.aphia.v1_0.model.AphiaRecord;

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
 */
public class NameUsage implements LinneanClassification { 
	
	private static final Log logger = LogFactory.getLog(NameUsage.class);
	
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
	private String superfamily;   // classification
	private String family;   // classification
	private String subfamily; // classification
	private String tribe; 	 // classification
	private String subtribe; // classification
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
	private String nameMatchDescription; // medatdata, description of the match between the name part excluding the authorshship of this name usage and the original
	private double authorshipStringSimilarity;
	private double scientificNameStringSimilarity;
	
	private int inputDbPK;  // Original database primary key for input
	private String originalScientificName;  
	private String originalAuthorship;    
	
	private Map<String,String> extension;  // additional attributes specific to some services but not all.
	
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
		superfamily = null;
		family = null;   
		subfamily = null;
		tribe = null;
		subtribe = null;
		genus = null;
		subgenus = null;
		extension = new HashMap<String,String>();
	}
	
	/**
	 * Set values for higher classification to null if they are empty strings.
	 */
	private void nullBlanks() {
		if (kingdom!=null && kingdom.trim().length()==0) { kingdom = null; }
		if (phylum!=null && phylum.trim().length()==0) { phylum = null; }
		if (tclass!=null && tclass.trim().length()==0) { tclass = null; }
		if (order!=null && order.trim().length()==0) { order = null; }
		if (superfamily!=null && superfamily.trim().length()==0) { superfamily = null; }
		if (family!=null && family.trim().length()==0) { family = null; }
		if (subfamily!=null && subfamily.trim().length()==0) { subfamily = null; }
		if (tribe!=null && tribe.trim().length()==0) { tribe = null; }
		if (subtribe!=null && subtribe.trim().length()==0) { subtribe = null; }
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
			if (getValFromKey(json,"class") != null) { 
				tclass = getValFromKey(json,"class");
			} else { 
				tclass = getValFromKey(json,"clazz");
			}
			order = getValFromKey(json,"order");
			superfamily = getValFromKey(json,"superfamily");
			family = getValFromKey(json,"family");
			subfamily = getValFromKey(json,"subfamily");
			tribe = getValFromKey(json,"tribe");
			subtribe = getValFromKey(json,"subtribe");
			genus = getValFromKey(json,"genus");
			sourceID = getValFromKey(json,"sourceId");
			link = getValFromKey(json,"link");
            synonyms = Boolean.parseBoolean(getValFromKey(json,"synonym"));
            guid = getValFromKey(json,"taxonID");
            fixAuthorship();
            nullBlanks();
            if (authorComparator==null ) { 
            	authorComparator = AuthorNameComparator.authorNameComparatorFactory(authorship, kingdom);
            }
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
		this.setSuperfamily(superfamily);
		this.setFamily(record.getFamily());
		this.setSubfamily(subfamily);
		this.setTribe(tribe);
		this.setSubtribe(subtribe);
		this.setGenus(record.getGenus());	
		if (Integer.toString(record.getKey()).length()>0) { 
			this.setGuid("gbif:" + Integer.toString(record.getKey()));
		}
		fixAuthorship();
        nullBlanks();
	}	
	
	/**
	 * Construct a NameUsage instance from an marinespecies.org AphiaRecord instance,
	 * assumes that the source authority is WoRMS.
	 * 
	 * @param record an AphiaRecord from WoRMS.
	 */
	public NameUsage(org.marinespecies.aphia.v1_0.model.AphiaRecord record) {
		init();
		authorComparator = new ICZNAuthorNameComparator(.75d,.5d);
		this.setSourceAuthority("WoRMS (World Register of Marine Species)");
		this.setScientificName(record.getScientificname());
		this.setRank(record.getRank());
		this.setAuthorship(record.getAuthority());
		this.setAcceptedName(record.getValidName());
		this.setKingdom(record.getKingdom());
		this.setPhylum(record.getPhylum());
		this.setTclass(record.getPropertyClass());
		this.setOrder(record.getOrder());
		this.setFamily(record.getFamily());
		this.setGenus(record.getGenus());
		this.setGuid("urn:lsid:marinespecies.org:taxname:" + Integer.toString(record.getAphiaID()));
		this.setTaxonomicStatus(record.getStatus());
		this.setUnacceptReason(record.getUnacceptreason());
		fixAuthorship();
        nullBlanks();
	}	
	
	/**
	 * Construct a NameUsage instance from an irmng.org AphiaRecord instance,
	 * assumes that the source authority is IRMNG.
	 * 
	 * @param record an AphiaRecord from IRMNG.
	 */
	public NameUsage(org.irmng.aphia.v1_0.model.AphiaRecord record) {
		init();
		authorComparator = new ICZNAuthorNameComparator(.75d,.5d);
		this.setSourceAuthority("IRMNG (Interim Register Marine and Non-marine Genera)");
		this.setScientificName(record.getScientificname());
		this.setRank(record.getRank());
		this.setAuthorship(record.getAuthority());
		this.setAcceptedName(record.getValidName());
		this.setKingdom(record.getKingdom());
		this.setPhylum(record.getPhylum());
		this.setTclass(record.getPropertyClass());
		this.setOrder(record.getOrder());
		this.setFamily(record.getFamily());
		this.setGenus(record.getGenus());
		this.setGuid("urn:lsid:irmng.org:taxname:" + Integer.toString(record.getIRMNGID()));
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
		if (canonicalName ==null) {
			return "";
		}
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
		if (rank==null) { 
			return "";
		} else { 
		   return rank;
		}
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
		if (kingdom==null) { 
			return "";
		}
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
		if (phylum==null) { 
			return "";
		}
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
		if (tclass==null) { 
			return "";
		}
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
		if (order==null) { 
			return "";
		}
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
		if (family==null) {
			return "";
		}
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
		if (genus==null) {
			return "";
		}
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
	 * @param scientificNameStringEditDistance the scientificNameStringEditDistance to set
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
	 * @param authorshipStringEditDistance the authorshipStringEditDistance to set
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
			NameParserGBIF parser = new NameParserGBIF();
	        ParsedName parse = null;
	        try {
	        	if (kingdom!=null && kingdom.equals("Animalia")) { 
	        		parse = parser.parse(getAcceptedName(),null,NomCode.ZOOLOGICAL);
	        	} else if (kingdom!=null && kingdom.equals("Plantae")) { 
	        		parse = parser.parse(getAcceptedName(),null,NomCode.BOTANICAL);
	        	} else {
	        		parse = parser.parse(getAcceptedName(),null,null);
	        	}
				if (parse!=null) { 
                   String author = parse.authorshipComplete();
				   setAcceptedAuthorship(author);
				   try { 
					   if (getAcceptedName().lastIndexOf(author) > -1) {
						   // remove authorship string from accepted name
						   setAcceptedName(getAcceptedName().substring(0, getAcceptedName().lastIndexOf(author)).trim());
					   }
				   } catch (NullPointerException e) { 
					   // expected when no accepted name
					   logger.debug(e.getMessage());
				   }
				}
			} catch (UnparsableNameException e) {
				// couldn't parse
			} catch (InterruptedException e) {
				// threading issue
				logger.debug(e.getMessage());
			}	
    		try {
				parser.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
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
		if (unacceptReason==null) { 
			return "";
		}
		return unacceptReason;
	}

	/**
	 * @param unacceptReason the unacceptReason to set
	 */
	public void setUnacceptReason(String unacceptReason) {
		this.unacceptReason = unacceptReason;
	}

	/**
	 * @return the extension
	 */
	public Map<String,String> getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(Map<String,String> extension) {
		this.extension = extension;
	}

	public String getNameMatchDescription() {
		return nameMatchDescription;
	}

	public void setNameMatchDescription(String nameMatchDescription) {
		this.nameMatchDescription = nameMatchDescription;
	}

	/**
	 * @return the superfamily
	 */
	public String getSuperfamily() {
		if (superfamily==null) { 
			return "";
		}
		return superfamily;
	}

	/**
	 * @param superfamily the superfamily to set
	 */
	public void setSuperfamily(String superfamily) {
		this.superfamily = superfamily;
	}

	/**
	 * @return the subfamily
	 */
	public String getSubfamily() {
		if (subfamily==null) { 
			return "";
		}
		return subfamily;
	}

	/**
	 * @param subfamily the subfamily to set
	 */
	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	/**
	 * @return the tribe
	 */
	public String getTribe() {
		if (tribe==null) { 
			return "";
		}
		return tribe;
	}

	/**
	 * @param tribe the tribe to set
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	/**
	 * @return the subtribe
	 */
	public String getSubtribe() {
		if (subtribe==null) { 
			return "";
		}
		return subtribe;
	}

	/**
	 * @param subtribe the subtribe to set
	 */
	public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}	
	
}
