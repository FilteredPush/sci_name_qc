package org.filteredpush.qc.sciname;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.harvard.mcz.nametools.NameUsage;

/** A utility class containing the dwc:Taxon terms.   
 * 
 * @author mole
 *
 */
public class Taxon {
	
	private static final Log logger = LogFactory.getLog(Taxon.class);
	
	private String taxonID;
	private String kingdom;
	private String phylum;
	private String taxonomic_class;
	private String order;
	private String superfamily;
	private String family;
	private String subfamily;
	private String tribe;
	private String subtribe;
	private String genus;
	private String subgenus;
	private String scientificName;
	private String scientificNameAuthorship;
	private String genericName;
	private String specificEpithet;
	private String infraspecificEpithet;
	private String taxonRank;
	private String cultivarEpithet;
	private String higherClassification;
	private String vernacularName;
	private String taxonConceptID;
	private String scientificNameID;
	private String originalNameUsageID;
	private String acceptedNameUsageID;

	public Taxon() { 
		setNullValuesToEmptyString();
	}
	
	/**
	 * Set the value of any properties that are null to an empty string.
	 */
	protected void setNullValuesToEmptyString() { 
		if (this.taxonID==null) { 
			this.taxonID = "";
		}
		if (this.kingdom==null) { 
			this.kingdom = "";
		}
		if (this.phylum==null) { 
			this.phylum = "";
		}
		if (this.taxonomic_class==null) { 
			this.taxonomic_class = "";
		}
		if (this.order==null) { 
			this.order = "";
		}
		if (this.superfamily==null) { 
			this.superfamily = "";
		}
		if (this.family==null) { 
			this.family = "";
		}
		if (this.subfamily==null) { 
			this.subfamily = "";
		}
		if (this.tribe==null) { 
			this.tribe = "";
		}
		if (this.subtribe==null) { 
			this.subtribe = "";
		}
		if (this.genus==null) { 
			this.genus = "";
		}
		if (this.subgenus==null) { 
			this.subgenus = "";
		}
		if (this.scientificName==null) { 
			this.scientificName = "";
		}
		if (this.scientificNameAuthorship==null) { 
			this.scientificNameAuthorship = "";
		}
		if (this.taxonID==null) { 
			this.taxonID = "";
		}
		if (this.genericName==null) { 
			this.genericName = "";
		}
		if (this.specificEpithet==null) { 
			this.specificEpithet = "";
		}
		if (this.infraspecificEpithet==null) { 
			this.infraspecificEpithet = "";
		}
		if (this.taxonRank==null) { 
			this.taxonRank = "";
		}
		if (this.cultivarEpithet==null) { 
			this.cultivarEpithet = "";
		}
		if (this.higherClassification==null) { 
			this.higherClassification = "";
		}
		if (this.vernacularName==null) { 
			this.vernacularName = "";
		}
		if (this.taxonConceptID==null) { 
			this.taxonConceptID = "";
		}
		if (this.scientificNameID==null) { 
			this.scientificNameID = "";
		}
		if (this.originalNameUsageID==null) { 
			this.originalNameUsageID = "";
		}
		if (this.acceptedNameUsageID==null) { 
			this.acceptedNameUsageID = "";
		}
	}
	
	/**
	 * Construct a taxon instance.  Any null values of parameters will be set to an empty string.
	 * @param taxonID
	 * @param kingdom
	 * @param phylum
	 * @param taxonomic_class
	 * @param order
	 * @param family
	 * @param subfamily
	 * @param genus
	 * @param subgenus
	 * @param scientificName
	 * @param scientificNameAuthorship
	 * @param genericName
	 * @param specificEpithet
	 * @param infraspecificEpithet
	 * @param taxonRank
	 * @param cultivarEpithet
	 * @param higherClassification
	 * @param vernacularName
	 * @param taxonConceptID
	 * @param scientificNameID
	 * @param originalNameUsageID
	 * @param acceptedNameUsageID
	 * @param superfamily
	 * @param tribe
	 * @param subtribe
	 */
	public Taxon(String taxonID, String kingdom, String phylum, String taxonomic_class, String order, String family,
			String subfamily, String genus, String subgenus, String scientificName, String scientificNameAuthorship,
			String genericName, String specificEpithet, String infraspecificEpithet, String taxonRank,
			String cultivarEpithet, String higherClassification, String vernacularName, String taxonConceptID,
			String scientificNameID, String originalNameUsageID, String acceptedNameUsageID, String superfamily,
			String tribe, String subtribe
			) {
		this.taxonID = taxonID;
		this.kingdom = kingdom;
		this.phylum = phylum;
		this.taxonomic_class = taxonomic_class;
		this.order = order;
		this.superfamily = superfamily;
		this.family = family;
		this.subfamily = subfamily;
		this.tribe = tribe;
		this.subtribe = subtribe;
		this.genus = genus;
		this.subgenus = subgenus;
		this.scientificName = scientificName;
		this.scientificNameAuthorship = scientificNameAuthorship;
		this.genericName = genericName;
		this.specificEpithet = specificEpithet;
		this.infraspecificEpithet = infraspecificEpithet;
		this.taxonRank = taxonRank;
		this.cultivarEpithet = cultivarEpithet;
		this.higherClassification = higherClassification;
		this.vernacularName = vernacularName;
		this.taxonConceptID = taxonConceptID;
		this.scientificNameID = scientificNameID;
		this.originalNameUsageID = originalNameUsageID;
		this.acceptedNameUsageID = acceptedNameUsageID;
		setNullValuesToEmptyString();
	}

	public String getTaxonID() {
		return taxonID;
	}

	public void setTaxonID(String taxonID) {
		this.taxonID = taxonID;
	}

	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getPhylum() {
		return phylum;
	}

	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	public String getTaxonomic_class() {
		return taxonomic_class;
	}

	public void setTaxonomic_class(String taxonomic_class) {
		this.taxonomic_class = taxonomic_class;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getSubfamily() {
		return subfamily;
	}

	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getSubgenus() {
		return subgenus;
	}

	public void setSubgenus(String subgenus) {
		this.subgenus = subgenus;
	}

	public String getScientificName() {
		if (scientificName==null) { 
			return "";
		}
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getScientificNameAuthorship() {
		if (scientificNameAuthorship==null) { 
			return "";
		}
		return scientificNameAuthorship;
	}

	public void setScientificNameAuthorship(String scientificNameAuthorship) {
		this.scientificNameAuthorship = scientificNameAuthorship;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getSpecificEpithet() {
		return specificEpithet;
	}

	public void setSpecificEpithet(String specificEpithet) {
		this.specificEpithet = specificEpithet;
	}

	public String getInfraspecificEpithet() {
		return infraspecificEpithet;
	}

	public void setInfraspecificEpithet(String infraspecificEpithet) {
		this.infraspecificEpithet = infraspecificEpithet;
	}

	public String getTaxonRank() {
		return taxonRank;
	}

	public void setTaxonRank(String taxonRank) {
		this.taxonRank = taxonRank;
	}

	public String getCultivarEpithet() {
		return cultivarEpithet;
	}

	public void setCultivarEpithet(String cultivarEpithet) {
		this.cultivarEpithet = cultivarEpithet;
	}

	public String getHigherClassification() {
		return higherClassification;
	}

	public void setHigherClassification(String higherClassification) {
		this.higherClassification = higherClassification;
	}

	public String getVernacularName() {
		return vernacularName;
	}

	public void setVernacularName(String vernacularName) {
		this.vernacularName = vernacularName;
	}

	public String getTaxonConceptID() {
		return taxonConceptID;
	}

	public void setTaxonConceptID(String taxonConceptID) {
		this.taxonConceptID = taxonConceptID;
	}

	public String getScientificNameID() {
		return scientificNameID;
	}

	public void setScientificNameID(String scientificNameID) {
		this.scientificNameID = scientificNameID;
	}

	public String getOriginalNameUsageID() {
		return originalNameUsageID;
	}

	public void setOriginalNameUsageID(String originalNameUsageID) {
		this.originalNameUsageID = originalNameUsageID;
	}

	public String getAcceptedNameUsageID() {
		return acceptedNameUsageID;
	}

	public void setAcceptedNameUsageID(String acceptedNameUsageID) {
		this.acceptedNameUsageID = acceptedNameUsageID;
	}
	
	public String toString() { 
		StringBuilder result = new StringBuilder();
		result.append(taxonID).append(":");
		result.append(kingdom).append(":");
		result.append(phylum).append(":");
		result.append(taxonomic_class).append(":");
		result.append(order).append(":");
		result.append(superfamily).append(":");
		result.append(family).append(":");
		result.append(subfamily).append(":");
		result.append(tribe).append(":");
		result.append(subtribe).append(":");
		result.append(genus).append(":");
		result.append(subgenus).append(":");
		result.append(scientificName).append(":");
		result.append(scientificNameAuthorship).append(":");
		result.append(genericName).append(":");
		result.append(specificEpithet).append(":");
		result.append(infraspecificEpithet).append(":");
		result.append(taxonRank).append(":");
		result.append(cultivarEpithet).append(":");
		result.append(higherClassification).append(":");
		result.append(vernacularName).append(":");
		result.append(taxonConceptID).append(":");
		result.append(scientificNameID).append(":");
		result.append(originalNameUsageID).append(":");
		result.append(acceptedNameUsageID).append(":");
		return result.toString().replaceAll(":[:]+",":");
	}

	/**
	 * Test if the major ranks (kingdom, phylum, class, order, family) are the same between this Taxon and
	 * a NameUsage instance.  Does not assess tribe, does not assess super/sub ranks.  Only compares properties
	 * that have non-empty values.  
	 * 
	 * @param match NameUsage to compare with.
	 * 
	 * @return false if any of the non-empty values of kingdom, phylum, class, order, family are different between the two 
	 * NameUsages, otherwise returns true.
	 */
	public boolean sameHigherAs(NameUsage match) {
		boolean result = true;
		logger.debug(taxonomic_class);
		logger.debug(match.getTclass());
		if (!SciNameUtils.isEmpty(kingdom) && !match.getKingdom().equals("") && !kingdom.trim().toLowerCase().equals(match.getKingdom().trim().toLowerCase()) ) {
			logger.debug("Missmatch: " + kingdom + " " + match.getKingdom());
			result = false;
		}
		if (!SciNameUtils.isEmpty(phylum) && !match.getPhylum().equals("") && !phylum.trim().toLowerCase().equals(match.getPhylum().trim().toLowerCase()) ) {
			logger.debug("Missmatch: " + phylum + " " + match.getPhylum());
			result = false;
		}
		if (!SciNameUtils.isEmpty(taxonomic_class) && !match.getTclass().equals("") && !taxonomic_class.trim().toLowerCase().equals(match.getTclass().trim().toLowerCase()) ) {
			logger.debug("Missmatch: " + taxonomic_class+ " " + match.getTclass());
			result = false;
		}
		if (!SciNameUtils.isEmpty(order) && !match.getOrder().equals("") && !order.trim().toLowerCase().equals(match.getOrder().trim().toLowerCase()) ) {
			logger.debug("Missmatch: " + order + " " + match.getOrder());
			result = false;
		}
		if (!SciNameUtils.isEmpty(family) && !match.getFamily().equals("") && !family.trim().toLowerCase().equals(match.getFamily().trim().toLowerCase()) ) {
			logger.debug("Missmatch: " + family + " " + match.getFamily());
			result = false;
		}
		return result;
	}

	/**
	 * @return the superfamily
	 */
	public String getSuperfamily() {
		return superfamily;
	}

	/**
	 * @param superfamily the superfamily to set
	 */
	public void setSuperfamily(String superfamily) {
		this.superfamily = superfamily;
	}

	/**
	 * @return the tribe
	 */
	public String getTribe() {
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
		return subtribe;
	}

	/**
	 * @param subtribe the subtribe to set
	 */
	public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}
}