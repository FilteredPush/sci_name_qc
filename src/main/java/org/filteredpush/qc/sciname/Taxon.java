package org.filteredpush.qc.sciname;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.NomCode;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.UnparsableNameException;

import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;
import edu.harvard.mcz.nametools.ScientificNameComparator;

/**
 * A utility class containing the dwc:Taxon terms.
 *
 * @author mole
 * @version $Id: $Id
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

	/**
	 * <p>Constructor for Taxon.</p>
	 */
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
	 *
	 * @param taxonID a {@link java.lang.String} object.
	 * @param kingdom a {@link java.lang.String} object.
	 * @param phylum a {@link java.lang.String} object.
	 * @param taxonomic_class a {@link java.lang.String} object.
	 * @param order a {@link java.lang.String} object.
	 * @param family a {@link java.lang.String} object.
	 * @param subfamily a {@link java.lang.String} object.
	 * @param genus a {@link java.lang.String} object.
	 * @param subgenus a {@link java.lang.String} object.
	 * @param scientificName
	 * @param scientificNameAuthorship
	 * @param scientificNameID
	 * @param scientificNameAuthorship a {@link java.lang.String} object.
	 * @param genericName a {@link java.lang.String} object.
	 * @param specificEpithet a {@link java.lang.String} object.
	 * @param infraspecificEpithet a {@link java.lang.String} object.
	 * @param taxonRank a {@link java.lang.String} object.
	 * @param cultivarEpithet a {@link java.lang.String} object.
	 * @param higherClassification a {@link java.lang.String} object.
	 * @param vernacularName a {@link java.lang.String} object.
	 * @param taxonConceptID a {@link java.lang.String} object.
	 * @param scientificNameID a {@link java.lang.String} object.
	 * @param originalNameUsageID a {@link java.lang.String} object.
	 * @param acceptedNameUsageID a {@link java.lang.String} object.
	 * @param superfamily a {@link java.lang.String} object.
	 * @param tribe a {@link java.lang.String} object.
	 * @param subtribe a {@link java.lang.String} object.
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

	/**
	 * <p>Getter for the field <code>taxonID</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaxonID() {
		return taxonID;
	}

	/**
	 * <p>Setter for the field <code>taxonID</code>.</p>
	 *
	 * @param taxonID a {@link java.lang.String} object.
	 */
	public void setTaxonID(String taxonID) {
		this.taxonID = taxonID;
	}

	/**
	 * <p>Getter for the field <code>kingdom</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKingdom() {
		return kingdom;
	}

	/**
	 * <p>Setter for the field <code>kingdom</code>.</p>
	 *
	 * @param kingdom a {@link java.lang.String} object.
	 */
	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	/**
	 * <p>Getter for the field <code>phylum</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPhylum() {
		return phylum;
	}

	/**
	 * <p>Setter for the field <code>phylum</code>.</p>
	 *
	 * @param phylum a {@link java.lang.String} object.
	 */
	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	/**
	 * <p>Getter for the field <code>taxonomic_class</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaxonomic_class() {
		return taxonomic_class;
	}

	/**
	 * <p>Setter for the field <code>taxonomic_class</code>.</p>
	 *
	 * @param taxonomic_class a {@link java.lang.String} object.
	 */
	public void setTaxonomic_class(String taxonomic_class) {
		this.taxonomic_class = taxonomic_class;
	}

	/**
	 * <p>Getter for the field <code>order</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * <p>Setter for the field <code>order</code>.</p>
	 *
	 * @param order a {@link java.lang.String} object.
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * <p>Getter for the field <code>family</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * <p>Setter for the field <code>family</code>.</p>
	 *
	 * @param family a {@link java.lang.String} object.
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * <p>Getter for the field <code>subfamily</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSubfamily() {
		return subfamily;
	}

	/**
	 * <p>Setter for the field <code>subfamily</code>.</p>
	 *
	 * @param subfamily a {@link java.lang.String} object.
	 */
	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	/**
	 * <p>Getter for the field <code>genus</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getGenus() {
		return genus;
	}

	/**
	 * <p>Setter for the field <code>genus</code>.</p>
	 *
	 * @param genus a {@link java.lang.String} object.
	 */
	public void setGenus(String genus) {
		this.genus = genus;
	}

	/**
	 * <p>Getter for the field <code>subgenus</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSubgenus() {
		return subgenus;
	}

	/**
	 * <p>Setter for the field <code>subgenus</code>.</p>
	 *
	 * @param subgenus a {@link java.lang.String} object.
	 */
	public void setSubgenus(String subgenus) {
		this.subgenus = subgenus;
	}

	/**
	 * <p>Getter for the field <code>scientificName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getScientificName() {
		if (scientificName==null) { 
			return "";
		}
		return scientificName;
	}

	/**
	 * <p>Setter for the field <code>scientificName</code>.</p>
	 *
	 * @param scientificName a {@link java.lang.String} object.
	 */
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	/**
	 * <p>Getter for the field <code>scientificNameAuthorship</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getScientificNameAuthorship() {
		if (scientificNameAuthorship==null) { 
			return "";
		}
		return scientificNameAuthorship;
	}

	/**
	 * <p>Setter for the field <code>scientificNameAuthorship</code>.</p>
	 *
	 * @param scientificNameAuthorship a {@link java.lang.String} object.
	 */
	public void setScientificNameAuthorship(String scientificNameAuthorship) {
		this.scientificNameAuthorship = scientificNameAuthorship;
	}

	/**
	 * <p>Getter for the field <code>genericName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getGenericName() {
		return genericName;
	}

	/**
	 * <p>Setter for the field <code>genericName</code>.</p>
	 *
	 * @param genericName a {@link java.lang.String} object.
	 */
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	/**
	 * <p>Getter for the field <code>specificEpithet</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSpecificEpithet() {
		return specificEpithet;
	}

	/**
	 * <p>Setter for the field <code>specificEpithet</code>.</p>
	 *
	 * @param specificEpithet a {@link java.lang.String} object.
	 */
	public void setSpecificEpithet(String specificEpithet) {
		this.specificEpithet = specificEpithet;
	}

	/**
	 * <p>Getter for the field <code>infraspecificEpithet</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getInfraspecificEpithet() {
		return infraspecificEpithet;
	}

	/**
	 * <p>Setter for the field <code>infraspecificEpithet</code>.</p>
	 *
	 * @param infraspecificEpithet a {@link java.lang.String} object.
	 */
	public void setInfraspecificEpithet(String infraspecificEpithet) {
		this.infraspecificEpithet = infraspecificEpithet;
	}

	/**
	 * <p>Getter for the field <code>taxonRank</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaxonRank() {
		return taxonRank;
	}

	/**
	 * <p>Setter for the field <code>taxonRank</code>.</p>
	 *
	 * @param taxonRank a {@link java.lang.String} object.
	 */
	public void setTaxonRank(String taxonRank) {
		this.taxonRank = taxonRank;
	}

	/**
	 * <p>Getter for the field <code>cultivarEpithet</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCultivarEpithet() {
		return cultivarEpithet;
	}

	/**
	 * <p>Setter for the field <code>cultivarEpithet</code>.</p>
	 *
	 * @param cultivarEpithet a {@link java.lang.String} object.
	 */
	public void setCultivarEpithet(String cultivarEpithet) {
		this.cultivarEpithet = cultivarEpithet;
	}

	/**
	 * <p>Getter for the field <code>higherClassification</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHigherClassification() {
		return higherClassification;
	}

	/**
	 * <p>Setter for the field <code>higherClassification</code>.</p>
	 *
	 * @param higherClassification a {@link java.lang.String} object.
	 */
	public void setHigherClassification(String higherClassification) {
		this.higherClassification = higherClassification;
	}

	/**
	 * <p>Getter for the field <code>vernacularName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getVernacularName() {
		return vernacularName;
	}

	/**
	 * <p>Setter for the field <code>vernacularName</code>.</p>
	 *
	 * @param vernacularName a {@link java.lang.String} object.
	 */
	public void setVernacularName(String vernacularName) {
		this.vernacularName = vernacularName;
	}

	/**
	 * <p>Getter for the field <code>taxonConceptID</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaxonConceptID() {
		return taxonConceptID;
	}

	/**
	 * <p>Setter for the field <code>taxonConceptID</code>.</p>
	 *
	 * @param taxonConceptID a {@link java.lang.String} object.
	 */
	public void setTaxonConceptID(String taxonConceptID) {
		this.taxonConceptID = taxonConceptID;
	}

	/**
	 * <p>Getter for the field <code>scientificNameID</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getScientificNameID() {
		return scientificNameID;
	}

	/**
	 * <p>Setter for the field <code>scientificNameID</code>.</p>
	 *
	 * @param scientificNameID a {@link java.lang.String} object.
	 */
	public void setScientificNameID(String scientificNameID) {
		this.scientificNameID = scientificNameID;
	}

	/**
	 * <p>Getter for the field <code>originalNameUsageID</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getOriginalNameUsageID() {
		return originalNameUsageID;
	}

	/**
	 * <p>Setter for the field <code>originalNameUsageID</code>.</p>
	 *
	 * @param originalNameUsageID a {@link java.lang.String} object.
	 */
	public void setOriginalNameUsageID(String originalNameUsageID) {
		this.originalNameUsageID = originalNameUsageID;
	}

	/**
	 * <p>Getter for the field <code>acceptedNameUsageID</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAcceptedNameUsageID() {
		return acceptedNameUsageID;
	}

	/**
	 * <p>Setter for the field <code>acceptedNameUsageID</code>.</p>
	 *
	 * @param acceptedNameUsageID a {@link java.lang.String} object.
	 */
	public void setAcceptedNameUsageID(String acceptedNameUsageID) {
		this.acceptedNameUsageID = acceptedNameUsageID;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
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
	 * Compare the scientific name and authorship of this taxon to a nameUsage, handles comparisons
	 * where the authorship is in authorship and in scientific name, or in just one of the two, or is
	 * absent from either this or the nameUsage.  For the meanings of plausible, see the
	 * NameComparison.isPlausible... methods.
	 *
	 * @see NameComparison#isPlausibleAuthorMatch(String) list of plausibly similar authorships
	 * @see NameComparison#isPlausible(String) list of plausibly similar scientific names without authorship
	 * @see NameComparison#isPlausibleAuthorMatch(String) list of plausibly similar authorships
	 * @see NameComparison#isPlausible(String) list of plausibly similar scientific names without authorship
	 * @param nameUsage to compare this with
	 * @return true if plausible that this and the supplied nameUsage represent the same name, false if not.
	 */
	public Boolean plausiblySameNameAs(NameUsage nameUsage) { 
		Boolean result = false;
		if (this.getScientificName().equals(nameUsage.getScientificName())
				&& this.getScientificNameAuthorship().equals(nameUsage.getAuthorship())) 
		{
			// simple exact match, names are equal and authors are equal
			result=true;
		} else if (this.getScientificName().equals(nameUsage.getScientificName())
				&& !SciNameUtils.isEmpty(this.getScientificNameAuthorship())
				&& !SciNameUtils.isEmpty(nameUsage.getAuthorship())
				&&  !this.getScientificNameAuthorship().equals(nameUsage.getAuthorship())) 
		{
			// names are the same, but there are authors and the authors aren't identical
 			NameComparison authorComparison = nameUsage.getAuthorComparator().compare(this.getScientificNameAuthorship(), nameUsage.getAuthorship());
			logger.debug(authorComparison.getMatchType());
			if (NameComparison.isPlausibleAuthorMatch(authorComparison.getMatchType())) { 
				result = true;
			} else { 
				result = false;
			}
		} else { 
			// other cases, missmatches and variation in where the authorship is present
			logger.debug(this.getScientificName());
			logger.debug(this.getScientificNameAuthorship());
			logger.debug(nameUsage.getScientificName());
			logger.debug(nameUsage.getAuthorship());
			ScientificNameComparator comparator = new ScientificNameComparator();
			String nameOne = this.getScientificName();
			String authorOne = this.getScientificNameAuthorship();
			if (!SciNameUtils.isEmpty(this.getScientificNameAuthorship())) { 
				nameOne = nameOne.replace(this.getScientificNameAuthorship(), "");
			} else { 
				// parse out authorship, remove from scientific name string
				NameParserGBIF parser = new NameParserGBIF();
		        ParsedName parse = null;
		        try {
		        	if (kingdom!=null && kingdom.equals("Animalia")) { 
		        		parse = parser.parse(nameOne,null,NomCode.ZOOLOGICAL);
		        	} else if (kingdom!=null && kingdom.equals("Plantae")) { 
		        		parse = parser.parse(nameOne,null,NomCode.BOTANICAL);
		        	} else {
		        		parse = parser.parse(nameOne,null,null);
		        	}
					if (parse!=null) { 
	                  authorOne = parse.authorshipComplete();
	                  nameOne = nameOne.replace(authorOne, "");
					}
				} catch (UnparsableNameException | InterruptedException e) {
					logger.debug(e.getMessage());
				}
			}
			String nameTwo = nameUsage.getScientificName();
			String authorTwo = nameUsage.getAuthorship();
			if (!SciNameUtils.isEmpty(nameUsage.getAuthorship())) { 
				nameTwo = nameTwo.replace(nameUsage.getAuthorship(), "");
			} else { 
				// parse out authorship, remove from scientific name string
				NameParserGBIF parser = new NameParserGBIF();
		        ParsedName parse = null;
		        try {
		        	if (kingdom!=null && kingdom.equals("Animalia")) { 
		        		parse = parser.parse(nameTwo,null,NomCode.ZOOLOGICAL);
		        	} else if (kingdom!=null && kingdom.equals("Plantae")) { 
		        		parse = parser.parse(nameTwo,null,NomCode.BOTANICAL);
		        	} else {
		        		parse = parser.parse(nameTwo,null,null);
		        	}
					if (parse!=null) { 
	                  authorTwo = parse.authorshipComplete();
	                  nameTwo = nameOne.replace(authorOne, "");
					}
				} catch (UnparsableNameException | InterruptedException e) {
					logger.debug(e.getMessage());
				}
			}
			NameComparison comparison  = comparator.compareWithoutAuthor(nameOne, nameTwo);
			if (comparison.getMatchType().equals(NameComparison.MATCH_EXACT)) { 
				NameComparison authorComparison = nameUsage.getAuthorComparator().compare(this.getScientificNameAuthorship(), nameUsage.getAuthorship());
				logger.debug(authorComparison.getMatchType());
				if (NameComparison.isPlausibleAuthorMatch(authorComparison.getMatchType())) { 
					result = true;
				} else { 
					result = false;
				}
			} else { 
				// names aren't the same
				logger.debug(nameOne);
				logger.debug(nameTwo);
				result = false;
			}
			
		}
		return result;
	}
	
	/**
	 * <p>Getter for the field <code>superfamily</code>.</p>
	 *
	 * @return the superfamily
	 */
	public String getSuperfamily() {
		return superfamily;
	}

	/**
	 * <p>Setter for the field <code>superfamily</code>.</p>
	 *
	 * @param superfamily the superfamily to set
	 */
	public void setSuperfamily(String superfamily) {
		this.superfamily = superfamily;
	}

	/**
	 * <p>Getter for the field <code>tribe</code>.</p>
	 *
	 * @return the tribe
	 */
	public String getTribe() {
		return tribe;
	}

	/**
	 * <p>Setter for the field <code>tribe</code>.</p>
	 *
	 * @param tribe the tribe to set
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	/**
	 * <p>Getter for the field <code>subtribe</code>.</p>
	 *
	 * @return the subtribe
	 */
	public String getSubtribe() {
		return subtribe;
	}

	/**
	 * <p>Setter for the field <code>subtribe</code>.</p>
	 *
	 * @param subtribe the subtribe to set
	 */
	public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}
}
