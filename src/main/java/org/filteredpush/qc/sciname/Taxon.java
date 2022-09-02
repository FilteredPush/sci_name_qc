package org.filteredpush.qc.sciname;

/** A utility class containing the dwc:Taxon terms.   
 * 
 * @author mole
 *
 */
public class Taxon {
	private String taxonID;
	private String kingdom;
	private String phylum;
	private String taxonomic_class;
	private String order;
	private String family;
	private String subfamily;
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
		this.taxonID = "";
		this.kingdom = "";
		this.phylum = "";
		this.taxonomic_class = "";
		this.order = "";
		this.family = "";
		this.subfamily = "";
		this.genus = "";
		this.subgenus = "";
		this.scientificName = "";
		this.scientificNameAuthorship = "";
		this.genericName = "";
		this.specificEpithet = "";
		this.infraspecificEpithet = "";
		this.taxonRank = "";
		this.cultivarEpithet = "";
		this.higherClassification = "";
		this.vernacularName = "";
		this.taxonConceptID = "";
		this.scientificNameID = "";
		this.originalNameUsageID = "";
		this.acceptedNameUsageID = "";
	}
	
	public Taxon(String taxonID, String kingdom, String phylum, String taxonomic_class, String order, String family,
			String subfamily, String genus, String subgenus, String scientificName, String scientificNameAuthorship,
			String genericName, String specificEpithet, String infraspecificEpithet, String taxonRank,
			String cultivarEpithet, String higherClassification, String vernacularName, String taxonConceptID,
			String scientificNameID, String originalNameUsageID, String acceptedNameUsageID) {
		this.taxonID = taxonID;
		this.kingdom = kingdom;
		this.phylum = phylum;
		this.taxonomic_class = taxonomic_class;
		this.order = order;
		this.family = family;
		this.subfamily = subfamily;
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
		result.append(family).append(":");
		result.append(subfamily).append(":");
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
}