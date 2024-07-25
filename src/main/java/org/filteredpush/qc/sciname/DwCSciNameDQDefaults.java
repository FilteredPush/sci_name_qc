/** DwCSciNameDQ.java
 * 
 * Copyright 2022 President and Fellows of Harvard College
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
 *  
 * NOTE: requires the ffdq-api dependecy in the maven pom.xml
 */
package org.filteredpush.qc.sciname;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datakurator.ffdq.annotations.*;
import org.datakurator.ffdq.api.DQResponse;

import org.datakurator.ffdq.api.result.*;

/**
 * Implementation of the TDWG TG2 NAME (scientific name) related data quality tests,
 * using default values for parameters.
 *
 * #82 VALIDATION_SCIENTIFICNAME_NOTEMPTY 7c4b9498-a8d9-4ebb-85f1-9f200c788595
 * #120 VALIDATION_TAXONID_NOTEMPTY 401bf207-9a55-4dff-88a5-abcd58ad97fa
 * #161 VALIDATION_TAXONRANK_NOTEMPTY 14da5b87-8304-4b2b-911d-117e3c29e890
 * #101 VALIDATION_POLYNOMIAL_CONSISTENT 17f03f1f-f74d-40c0-8071-2927cfc9487b
 *
 * #81 VALIDATION_KINGDOM_FOUND 125b5493-052d-4a0d-a3e1-ed5bf792689e
 * #22 VALIDATION_PHYLUM_FOUND eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f
 * #77 VALIDATION_CLASS_FOUND 2cd6884e-3d14-4476-94f7-1191cfff309b
 * #83 VALIDATION_ORDER_FOUND 81cc974d-43cc-4c0f-a5e0-afa23b455aa3
 * #28 VALIDATION_FAMILY_FOUND 3667556d-d8f5-454c-922b-af8af38f613c
 * #122 VALIDATION_GENUS_FOUND f2ce7d55-5b1d-426a-b00e-6d4efe3058ec
 * #46 VALIDATION_SCIENTIFICNAME_FOUND 3f335517-f442-4b98-b149-1e87ff16de45
 *
 * #57 AMENDMENT_TAXONID_FROM_TAXON 431467d6-9b4b-48fa-a197-cd5379f5e889
 * #71 AMENDMENT_SCIENTIFICNAME_FROM_TAXONID f01fb3f9-2f7e-418b-9f51-adf50f202aea
 *
 * Supplementary tests where the default source authority does not yet provide data
 * and thus will not return COMPLIANT until GBIF adds these ranks
 * #207 VALIDATION_TRIBE_FOUND 8c15f351-26d5-4edd-b38e-07541dc64fd0 
 * #208 VALIDATION_SUBTRIBE_FOUND 4527c47e-61d9-4abb-af3e-f2999191be17
 * #206 VALIDATION_SUPERFAMILY_FOUND 2a45e0e9-446c-429f-992d-c3ec1d29eebb
 * 
 * @author mole
 * @version $Id: $Id
 */
@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",
	label="Kurator: Scientific Name Validator - DwCSciNameDQ:v1.1.0")
public class DwCSciNameDQDefaults extends DwCSciNameDQ {
	
	private static final Log logger = LogFactory.getLog(DwCSciNameDQDefaults.class);
	
    /**
     * Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #22 VALIDATION_PHYLUM_FOUND
     * Version: 2022-03-25
     *
     * @param phylum the provided dwc:phylum to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_PHYLUM_FOUND", description="Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?")
    @Provides("eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f/2022-03-25")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:phylum is EMPTY; COMPLIANT if the value of dwc:phylum was found as a value at the rank of Phylum by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei], 'API endpoint' [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public static DQResponse<ComplianceValue> validationPhylumFound(@ActedUpon("dwc:phylum") String phylum) {
    	return validationPhylumFound(phylum, null);
    }

    /**
     * Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #28 VALIDATION_FAMILY_FOUND
     * Version: 2023-09-17
     *
     * @param family the provided dwc:family to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_FAMILY_FOUND", description="Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?")
    @Provides("3667556d-d8f5-454c-922b-af8af38f613c")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/3667556d-d8f5-454c-922b-af8af38f613c/2023-09-17")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:family is EMPTY; COMPLIANT if the value of dwc:family was found as a value at the rank of Family by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationFamilyFound(@ActedUpon("dwc:family") String family) {
    	return validationFamilyFound(family, null);
    }

    /**
     * Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #46 VALIDATION_SCIENTIFICNAME_FOUND
     * Version: 2023-09-17
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_FOUND", description="Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?")
    @Provides("3f335517-f442-4b98-b149-1e87ff16de45")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/3f335517-f442-4b98-b149-1e87ff16de45/2023-09-17")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is EMPTY; COMPLIANT if there is a match of the contents of dwc:scientificName with the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationScientificnameFound(@ActedUpon("dwc:scientificName") String scientificName) {
        return validationScientificnameFound(scientificName, null);
    }
    
    /**
     * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority
     * using the available taxon terms.
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #57 AMENDMENT_TAXONID_FROM_TAXON
     * Version: 2023-07-04
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param order the provided dwc:order to evaluate
     * @param taxonomic_class the provided dwc:class to evaluate
     * @param family the provided dwc:family to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param subgenus the provided dwc:subgenus to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param scientificNameAuthorship the provided dwc:scientificNameAuthorship to evaluate
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @param cultivarEpithet the provided dwc:cultivarEpithet to evaluate
     * @param higherClassification the provided dwc:higherClassification to evaluate
     * @param vernacularName the provided dwc:vernacularName to evaluate
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
     * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_TAXONID_FROM_TAXON", description="Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.")
    @Provides("431467d6-9b4b-48fa-a197-cd5379f5e889")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/431467d6-9b4b-48fa-a197-cd5379f5e889/2023-07-04")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available;  INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is not EMPTY or if all of dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, and dwc:cultivarEpithet are EMPTY, FILLED_IN the value of taxonID for an unambiguously resolved single taxon record in the bdq:sourceAuthority through (1) the value of dwc:scientificName or (2) if dwc:scientificName is EMPTY through values of the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship and dwc:cultivarEpithet, or (3) if ambiguity produced by multiple matches in (1) or (2) can be disambiguated to a single Taxon using the values of dwc:subtribe, dwc:tribe, dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:superfamily, dwc:order, dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID, dwc:taxonomicRank, and dwc:vernacularName; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
	public static  DQResponse<AmendmentValue> amendmentTaxonidFromTaxon(
			@ActedUpon("dwc:taxonID") String taxonID, 
			@Consulted("dwc:kingdom") String kingdom, 
			@Consulted("dwc:phylum") String phylum, 
			@Consulted("dwc:class") String taxonomic_class, 
			@Consulted("dwc:order") String order,
			@Consulted("dwc:family") String family, 
			@Consulted("dwc:subfamily") String subfamily, 
			@Consulted("dwc:genus") String genus, 
			@Consulted("dwc:subgenus") String subgenus, 
			@Consulted("dwc:scientificName") String scientificName, 
			@Consulted("dwc:scientificNameAuthorship") String scientificNameAuthorship, 
			@Consulted("dwc:genericName") String genericName,
			@Consulted("dwc:specificEpithet") String specificEpithet,
			@Consulted("dwc:infraspecificEpithet") String infraspecificEpithet, 
			@Consulted("dwc:taxonRank") String taxonRank, 
			@Consulted("dwc:cultivarEpithet") String cultivarEpithet,
			@Consulted("dwc:higherClassification") String higherClassification, 
			@Consulted("dwc:vernacularName") String vernacularName, 
			@Consulted("dwc:taxonConceptID") String taxonConceptID, 
			@Consulted("dwc:scientificNameID") String scientificNameID, 
			@Consulted("dwc:originalNameUsageID") String originalNameUsageID, 
			@Consulted("dwc:acceptedNameUsageID") String acceptedNameUsageID,
			@Consulted("dwc:superfamily") String superfamily,
			@Consulted("dwc:tribe") String tribe,
			@Consulted("dwc:subtribe") String subtribe
	){
		return amendmentScientificnameidFromTaxon(new Taxon(taxonID, kingdom, phylum, taxonomic_class, order, family, subfamily,
				genus, subgenus, scientificName, scientificNameAuthorship, genericName, specificEpithet,
				infraspecificEpithet, taxonRank, cultivarEpithet, higherClassification, vernacularName, taxonConceptID,
				scientificNameID, originalNameUsageID, acceptedNameUsageID, superfamily, tribe, subtribe), null);
	}

	
    /**
     * Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #70 VALIDATION_TAXON_UNAMBIGUOUS
     * Version: 2023-09-18
     *
     * Uses the default sourceAuthority.
     *
     * @param genus the provided dwc:genus to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param cultivarEpithet the provided dwc:cultivarEpithet to evaluate
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param infragenericEpithet the provided dwc:infragenericEpithet to evaluate
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param subgenus the provided dwc:subgenus to evaluate
     * @param higherClassification the provided dwc:higherClassification to evaluate
     * @param vernacularName the provided dwc:vernacularName to evaluate
     * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
     * @param scientificNameAuthorship the provided dwc:scientificNameAuthorship to evaluate
     * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param family the provided dwc:family to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     * @param taxonomic_class a {@link java.lang.String} object.
     * @param superfamily a {@link java.lang.String} object.
     * @param tribe a {@link java.lang.String} object.
     * @param subtribe a {@link java.lang.String} object.
     */
    @Validation(label="VALIDATION_TAXON_UNAMBIGUOUS", description="Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?")
    @Provides("4c09f127-737b-4686-82a0-7c8e30841590")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/4c09f127-737b-4686-82a0-7c8e30841590/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if all of dwc:scientificNameID, dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, dwc:cultivarEpithet are EMPTY; COMPLIANT if (1) dwc:scientificNameID references a single taxon record in the bdq:sourceAuthority, or (2) dwc:scientificNameID is empty and dwc:scientificName references a single taxon record in the bdq:sourceAuthority, or (3) if dwc:scientificName and dwc:scientificNameID are EMPTY and if a combination of the values of the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:cultivarEpithet, dwc:taxonRank, and dwc:scientificNameAuthorship can be unambiguously resolved to a unique taxon in the bdq:sourceAuthority, or (4) if ambiguity produced by multiple matches in (2) or (3) can be disambiguated to a unique Taxon using the values of dwc:tribe, dwc:subtribe, dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:superfamily, dwc:order, dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, dwc:taxonID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID and dwc:vernacularName; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationTaxonUnambiguous(
    		@ActedUpon("dwc:class") String taxonomic_class, 
    		@ActedUpon("dwc:genus") String genus, 
    		@ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, 
    		@ActedUpon("dwc:cultivarEpithet") String cultivarEpithet, 
    		@ActedUpon("dwc:taxonConceptID") String taxonConceptID, 
    		@ActedUpon("dwc:phylum") String phylum, 
    		@ActedUpon("dwc:subfamily") String subfamily, 
    		@ActedUpon("dwc:scientificNameID") String scientificNameID, 
    		@ActedUpon("dwc:infragenericEpithet") String infragenericEpithet, 
    		@ActedUpon("dwc:taxonID") String taxonID, 
    		@ActedUpon("dwc:subgenus") String subgenus, 
    		@ActedUpon("dwc:higherClassification") String higherClassification, 
    		@ActedUpon("dwc:vernacularName") String vernacularName, 
    		@ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, 
    		@ActedUpon("dwc:scientificNameAuthorship") String scientificNameAuthorship, 
    		@ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, 
    		@ActedUpon("dwc:genericName") String genericName, 
    		@ActedUpon("dwc:taxonRank") String taxonRank, 
    		@ActedUpon("dwc:kingdom") String kingdom, 
    		@ActedUpon("dwc:family") String family, 
    		@ActedUpon("dwc:scientificName") String scientificName, 
    		@ActedUpon("dwc:specificEpithet") String specificEpithet, 
    		@ActedUpon("dwc:order") String order,
    		@ActedUpon("dwc:superfamily") String superfamily,
    		@ActedUpon("dwc:tribe") String tribe,
    		@ActedUpon("dwc:subtribe") String subtribe
		){
		return validationTaxonUnambiguous(new Taxon(taxonID, kingdom, phylum, taxonomic_class, order, family, subfamily,
				genus, subgenus, scientificName, scientificNameAuthorship, genericName, specificEpithet,
				infraspecificEpithet, taxonRank, cultivarEpithet, higherClassification, vernacularName, taxonConceptID,
				scientificNameID, originalNameUsageID, acceptedNameUsageID, superfamily, tribe, subtribe), null);
    }
 
    /**
     * Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #71 AMENDMENT_SCIENTIFICNAME_FROM_TAXONID
     * Version: 2023-12-13
     *
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_SCIENTIFICNAME_FROM_SCIENTIFICNAMEID", description="Propose an amendment to the value of dwc:scientificName using the dwc:scientificNameID value from bdq:sourceAuthority.")
    @Provides("f01fb3f9-2f7e-418b-9f51-adf50f202aea")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/f01fb3f9-2f7e-418b-9f51-adf50f202aea/2023-12-13")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID is EMPTY, the value of dwc:scientificNameID is ambiguous or dwc:scientificName was not EMPTY; FILLED_IN the value of dwc:scientificName if the value of scientificNameID could be unambiguously interpreted as a value in bdq:sourceAuthority; otherwise NOT_AMENDED. bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public static DQResponse<AmendmentValue> amendmentScientificnameFromTaxonid(
    		@Consulted("dwc:scientificNameID") String scientificNameID, 
    		@ActedUpon("dwc:scientificName") String scientificName
    	) {
    	return amendmentScientificnameFromScientificnameid(scientificNameID, scientificName, null);
    }


    /**
     * Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #77 VALIDATION_CLASS_FOUND
     * Version: 2023-09-18
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASS_FOUND", description="Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?")
    @Provides("2cd6884e-3d14-4476-94f7-1191cfff309b")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2cd6884e-3d14-4476-94f7-1191cfff309b/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:class is EMPTY; COMPLIANT if the value of dwc:class was found as a value at the rank of Class in the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationClassFound(@ActedUpon("dwc:class") String taxonomic_class) {
    	return validationClassFound(taxonomic_class, null);
    }
    
    /**
     * Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #81 VALIDATION_KINGDOM_FOUND
     * Version: 2023-09-18
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_KINGDOM_FOUND", description="Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?")
    @Provides("125b5493-052d-4a0d-a3e1-ed5bf792689e")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/125b5493-052d-4a0d-a3e1-ed5bf792689e/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:kingdom is EMPTY; COMPLIANT if the value of dwc:kingdom was found as a value at the rank of kingdom by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationKingdomFound(@ActedUpon("dwc:kingdom") String kingdom) {
        return validationKingdomFound(kingdom, null);
    }
    

    /**
     * Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #83 VALIDATION_ORDER_FOUND
     * Version: 2023-09-18
     *
     * @param order the provided dwc:order to evaluate
     * @return DQResponse tyyhe response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_ORDER_FOUND", description="Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?")
    @Provides("81cc974d-43cc-4c0f-a5e0-afa23b455aa3")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/81cc974d-43cc-4c0f-a5e0-afa23b455aa3/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:order is EMPTY; COMPLIANT if the value of dwc:order was found as a value at the rank of Order by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationOrderFound(@ActedUpon("dwc:order") String order) {
    	return validationOrderFound(order,null);
    }


    /**
     * Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #122 VALIDATION_GENUS_FOUND
     * Version: 2023-09-18
     *
     * @param genus the provided dwc:genus to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_GENUS_FOUND", description="Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?")
    @Provides("f2ce7d55-5b1d-426a-b00e-6d4efe3058ec")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/f2ce7d55-5b1d-426a-b00e-6d4efe3058ec/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available;  INTERNAL_PREREQUISITES_NOT_MET if dwc:genus is EMPTY; COMPLIANT if the value of dwc:genus was found as a value at the rank of genus by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationGenusFound(@ActedUpon("dwc:genus") String genus) {
    	return validationGenusFound(genus, null);
    }

        
    /**
     * Does the value of dwc:taxonRank occur in bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF taxon rank vocabulary.
     *
     * Provides: #162 VALIDATION_TAXONRANK_STANDARD
     * Version: 2023-09-18
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONRANK_STANDARD", description="Does the value of dwc:taxonRank occur in bdq:sourceAuthority?")
    @Provides("7bdb13a4-8a51-4ee5-be7f-20693fdb183e")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/7bdb13a4-8a51-4ee5-be7f-20693fdb183e/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonRank is EMPTY; COMPLIANT if the value of dwc:taxonRank is in the bdq:sourceAuthority; otherwise NOT_COMPLIANT. bdq:sourceAuthority default = 'GBIF Vocabulary: Taxonomic Rank' {[https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]} {dwc:taxonRank [https://dwc.tdwg.org/list/#dwc_taxonRank]}")
    public static DQResponse<ComplianceValue> validationTaxonrankStandard(@ActedUpon("dwc:taxonRank") String taxonRank) {
       DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();
       return DwCSciNameDQ.validationTaxonrankStandard(taxonRank, null);
   }
   
   /**
    * Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.
    * where bdq:sourceAuthority is the default GBIF taxon rank vocabulary.
    *
    * Provides: #163 AMENDMENT_TAXONRANK_STANDARDIZED
    * Version: 2023-09-18
    *
    * @param taxonRank the provided dwc:taxonRank to evaluate
    * @return DQResponse the response of type AmendmentValue to return
    */
   @Amendment(label="AMENDMENT_TAXONRANK_STANDARDIZED", description="Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.")
   @Provides("e39098df-ef46-464c-9aef-bcdeee2a88cb")
   @ProvidesVersion("https://rs.tdwg.org/bdq/terms/e39098df-ef46-464c-9aef-bcdeee2a88cb/2023-09-18")
   @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL PREREQUISITES_NOT_MET if dwc:taxonRank is EMPTY; AMENDED the value of dwc:taxonRank if it can be unambiguously matched to a term in bdq:sourceAuthority; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Vocabulary: Taxonomic Rank' {[https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]} {dwc:taxonRank [https://dwc.tdwg.org/list/#dwc_taxonRank]}")
   public static DQResponse<AmendmentValue> amendmentTaxonrankStandardized(@ActedUpon("dwc:taxonRank") String taxonRank) {
	   return DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, null);
   }
    
    /**
     * Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?
     *
     * Can the combination of higher classification taxonomic terms be unambiguously resolved using bdq:sourceAuthority?
     * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #123 VALIDATION_CLASSIFICATION_CONSISTENT
     * Version: 2023-07-13
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param order the provided dwc:order to evaluate
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param family the provided dwc:family to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     * @param taxonmic_class a {@link java.lang.String} object.
     */
    @Validation(label="VALIDATION_CLASSIFICATION_CONSISTENT", description="Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?")
    @Provides("2750c040-1d4a-4149-99fe-0512785f2d5f")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2750c040-1d4a-4149-99fe-0512785f2d5f/2023-07-13")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if all of the fields dwc:kingdom dwc:phylum, dwc:class, dwc:order, dwc:superfamily, dwc:family, dwc:subfamily, dwc:tribe, dwc:subtribe, dwc:genus are EMPTY; COMPLIANT if the combination of values of higher classification taxonomic terms (dwc:kingdom, dwc:phylum, dwc:class, dwc:order, dwc:superfamily, dwc:family, dwc:subfamily, dwc:tribe, dwc:subtribe, dwc:genus) are consistent with the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationClassificationConsistent(
    		@ActedUpon("dwc:kingdom") String kingdom, 
    		@ActedUpon("dwc:phylum") String phylum, 
    		@ActedUpon("dwc:class") String taxonmic_class, 
    		@ActedUpon("dwc:order") String order,
    		@ActedUpon("dwc:superfamily") String superfamily,
    		@ActedUpon("dwc:family") String family,
    		@ActedUpon("dwc:subfamily") String subfamily,
    		@ActedUpon("dwc:tribe") String tribe,
    		@ActedUpon("dwc:subtribe") String subtribe,
    		@ActedUpon("dwc:genus") String genus
    		
    ) {
        return DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, taxonmic_class, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
    }
    
    /**
     * Does the value of dwc:subtribe occur at rank of Subtribe in bdq:sourceAuthority?
     * using the default GBIF backbone taxonomy.
     * NOTE: GBIF backbone taxonomy does not yet include subtribe data, until it does, this will never return compliant.
     *
     * Provides: 208 VALIDATION_SUBTRIBE_FOUND
     * Version: 2023-09-22
     *
     * @param subtribe the provided dwc:subtribe to evaluate as ActedUpon.
     * @return DQResponse the response of type ComplianceValue  to return
     */
     @Validation(label="VALIDATION_SUBTRIBE_FOUND", description="Does the value of dwc:subtribe occur at rank of Subtribe in bdq:sourceAuthority?")
     @Provides("4527c47e-61d9-4abb-af3e-f2999191be17")
     @ProvidesVersion("https://rs.tdwg.org/bdq/terms/4527c47e-61d9-4abb-af3e-f2999191be17/2023-09-22")
     @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:subtribe is EMPTY; COMPLIANT if the value of dwc:subtribe was found as a value at the rank of subtribe by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
     public static DQResponse<ComplianceValue> validationSubtribeFound(
         @ActedUpon("dwc:subtribe") String subtribe
     ) {
         return validationSubtribeFound(subtribe, null);
     }

     /**
     * Does the value of dwc:tribe occur at rank of Tribe in bdq:sourceAuthority
     * using the default GBIF backbone taxonomy.
     * NOTE: GBIF backbone taxonomy does not yet include tribe data, until it does, this will never return compliant.
     *
     * Provides: 207 VALIDATION_TRIBE_FOUND
     * Version: 2023-09-22
     *
     * @param tribe the provided dwc:tribe to evaluate as ActedUpon.
     * @return DQResponse the response of type ComplianceValue  to return
     */
     @Validation(label="VALIDATION_TRIBE_FOUND", description="Does the value of dwc:tribe occur at rank of Tribe in bdq:sourceAuthority?")
     @Provides("8c15f351-26d5-4edd-b38e-07541dc64fd0")
     @ProvidesVersion("https://rs.tdwg.org/bdq/terms/8c15f351-26d5-4edd-b38e-07541dc64fd0/2023-09-22")
     @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:tribe is EMPTY; COMPLIANT if the value of dwc:tribe was found as a value at the rank of tribe by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
     public static DQResponse<ComplianceValue> validationTribeFound(
         @ActedUpon("dwc:tribe") String tribe
     ) {
         return validationTribeFound(tribe, null);
     }

     /**
     * Does the value of dwc:superfamily occur at rank of Superfamily in bdq:sourceAuthority
     * using the default GBIF backbone taxonomy.
     * NOTE: GBIF backbone taxonomy does not yet include tribe data, until it does, this will never return compliant.
     *
     * Provides: 206 VALIDATION_SUPERFAMILY_FOUND
     * Version: 2023-09-22
     *
     * @param superfamily the provided dwc:superfamily to evaluate as ActedUpon.
     * @return DQResponse the response of type ComplianceValue  to return
     */
     @Validation(label="VALIDATION_SUPERFAMILY_FOUND", description="Does the value of dwc:superfamily occur at rank of Superfamily in bdq:sourceAuthority?")
     @Provides("2a45e0e9-446c-429f-992d-c3ec1d29eebb")
     @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2a45e0e9-446c-429f-992d-c3ec1d29eebb/2023-09-22")
     @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:superfamily is EMPTY; COMPLIANT if the value of dwc:superfamily was found as a value at the rank of superfamily by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
     public static DQResponse<ComplianceValue> validationSuperfamilyFound(
         @ActedUpon("dwc:superfamily") String superfamily
     ) {
         return validationSuperfamilyFound(superfamily, null);
     }
    
// TODO: Implementation of AMENDMENT_SCIENTIFICNAMEID_FROM_TAXON is not up to date with current version: https://rs.tdwg.org/bdq/terms/431467d6-9b4b-48fa-a197-cd5379f5e889/2023-09-17 see line: 150
// TODO: Implementation of AMENDMENT_SCIENTIFICNAME_FROM_SCIENTIFICNAMEID is not up to date with current version: https://rs.tdwg.org/bdq/terms/f01fb3f9-2f7e-418b-9f51-adf50f202aea/2022-09-18 see line: 276
// TODO: Implementation of VALIDATION_CLASSIFICATION_CONSISTENT is not up to date with current version: https://rs.tdwg.org/bdq/terms/2750c040-1d4a-4149-99fe-0512785f2d5f/2023-09-18 see line: 421
}
