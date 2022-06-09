/* NOTE: requires the ffdq-api dependecy in the maven pom.xml */

package org.filteredpush.qc.sciname;

import org.datakurator.ffdq.annotations.*;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.model.ResultState;
import org.datakurator.ffdq.api.result.*;

@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",label="Kurator: Scientific Name Validator - DwCSciNameDQ:v0.0.1")
public class DwCSciNameDQ_stubs {


    /**
     * Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?
     *
     * Provides: VALIDATION_TAXON_UNAMBIGUOUS
     *
     * @param class the provided dwc:class to evaluate
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
     */
    @Validation(label="VALIDATION_TAXON_UNAMBIGUOUS", description="Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?")
    @Provides("4c09f127-737b-4686-82a0-7c8e30841590")
    public DQResponse<ComplianceValue> validationTaxonUnambiguous(@ActedUpon("dwc:class") String taxonomic_class, @ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:cultivarEpithet") String cultivarEpithet, @ActedUpon("dwc:taxonConceptID") String taxonConceptID, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:subfamily") String subfamily, @ActedUpon("dwc:scientificNameID") String scientificNameID, @ActedUpon("dwc:infragenericEpithet") String infragenericEpithet, @ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:subgenus") String subgenus, @ActedUpon("dwc:higherClassification") String higherClassification, @ActedUpon("dwc:vernacularName") String vernacularName, @ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, @ActedUpon("dwc:scientificNameAuthorship") String scientificNameAuthorship, @ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, @ActedUpon("dwc:genericName") String genericName, @ActedUpon("dwc:taxonRank") String taxonRank, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet, @ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if all 
        // of dwc:taxonID, dwc:scientificName, dwc:genericName, dwc:specificEpithet, 
        // dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
        // dwc:cultivarEpithet are EMPTY; COMPLIANT if (1) dwc:taxonId 
        // or dwc:scientificName reference a single taxon record in 
        // the bdq:sourceAuthority, or (2) if dwc:scientificName and 
        // dwc:taxonID are EMPTY and if a combination of the values 
        // of the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, 
        // dwc:cultivarEpithet, dwc:taxonRank, and dwc:scientificNameAuthorship 
        // can be unambiguously resolved to a unique taxon in the bdq:sourceAuthority, 
        // or (3) if ambiguity produced by multiple matches in (2) 
        // can be disambiguated to a unique Taxon using the values 
        // of dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:order, 
        // dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, 
        // dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, 
        // dwc:taxonConceptID and dwc:vernacularName; otherwise NOT_COMPLIANT 
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei], 
        // "API endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"

        return result;
    }



    
    /**
     * Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?
     *
     * Provides: VALIDATION_POLYNOMIAL_CONSISTENT
     *
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_POLYNOMIAL_CONSISTENT", description="Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?")
    @Provides("17f03f1f-f74d-40c0-8071-2927cfc9487b")
    public DQResponse<ComplianceValue> validationPolynomialConsistent(@ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:genericName") String genericName, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is 
        // EMPTY, or all of dwc:genericName, dwc:specificEpithet and 
        // dwc:infraspecificEpithet are EMPTY; COMPLIANT if the polynomial, 
        // as represented in dwc:scientificName, is consistent with 
        // dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet; 
        // otherwise NOT_COMPLIANT 

        return result;
    }

    
    /**
     * Can the combination of higher classification taxonomic terms be unambiguously resolved using bdq:sourceAuthority?
     *
     * Provides: VALIDATION_CLASSIFICATION_UNAMBIGUOUS
     *
     * @param class the provided dwc:class to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param family the provided dwc:family to evaluate
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASSIFICATION_UNAMBIGUOUS", description="Can the combination of higher classification taxonomic terms be unambiguously resolved using bdq:sourceAuthority?")
    @Provides("78640f09-8353-411a-800e-9b6d498fb1c9")
    public DQResponse<ComplianceValue> validationClassificationUnambiguous(@ActedUpon("dwc:class") String taxonmic_class, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if all 
        // of the fields dwc:kingdom dwc:phylum, dwc:class, dwc:order, 
        // dwc:family are EMPTY; COMPLIANT if the combination of values 
        // of higher classification taxonomic terms (dwc:kingdom, dwc:phylum, 
        // dwc:class, dwc:order, dwc:family) can be unambiguously resolved 
        // by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority 
        // default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei], 
        // "API endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"

        return result;
    }


    /**
     * Does the value of dwc:taxonRank occur in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_TAXONRANK_STANDARD
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONRANK_STANDARD", description="Does the value of dwc:taxonRank occur in bdq:sourceAuthority?")
    @Provides("7bdb13a4-8a51-4ee5-be7f-20693fdb183e")
    public DQResponse<ComplianceValue> validationTaxonrankStandard(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonRank 
        // is EMPTY; COMPLIANT if the value of dwc:taxonRank is in 
        // the bdq:sourceAuthority; otherwise NOT_COMPLIANT. bdq:sourceAuthority 
        // default = "Taxonomic Rank GBIF Vocabulary" [https://rs.gbif.org/vocabulary/gbif/rank.xml] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.
     *
     * Provides: AMENDMENT_TAXONRANK_STANDARDIZED
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_TAXONRANK_STANDARDIZED", description="Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.")
    @Provides("e39098df-ef46-464c-9aef-bcdeee2a88cb")
    public DQResponse<AmendmentValue> amendmentTaxonrankStandardized(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; AMENDED the value of dwc:taxonRank if 
        // it could be unambiguously interpreted as a value in bdq:sourceAuthority; 
        // otherwise NOT_AMENDED bdq:sourceAuthority default = "Taxonomic 
        // Rank GBIF Vocabulary" [https://rs.gbif.org/vocabulary/gbif/rank.xml] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

}
