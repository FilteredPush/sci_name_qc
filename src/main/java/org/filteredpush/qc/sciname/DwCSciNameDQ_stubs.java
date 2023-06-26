/* NOTE: requires the ffdq-api dependecy in the maven pom.xml */

package org.filteredpush.qc.sciname;

import org.datakurator.ffdq.annotations.*;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.model.ResultState;
import org.datakurator.ffdq.api.result.*;

@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",label="Kurator: Scientific Name Validator - DwCSciNameDQ:v1.0.1-SNAPSHOT")
public class DwCSciNameDQ_stubs {

    /**
     * Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_PHYLUM_FOUND
     * Version: 2022-03-25
     *
     * @param phylum the provided dwc:phylum to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_PHYLUM_FOUND", description="Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?")
    @Provides("eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f/2022-03-25")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:phylum is EMPTY; COMPLIANT if the value of dwc:phylum was found as a value at the rank of Phylum by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei], 'API endpoint' [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationPhylumFound(@ActedUpon("dwc:phylum") String phylum) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:phylum 
        // is EMPTY; COMPLIANT if the value of dwc:phylum was found 
        // as a value at the rank of Phylum by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_FAMILY_FOUND
     * Version: 2022-03-25
     *
     * @param family the provided dwc:family to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_FAMILY_FOUND", description="Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?")
    @Provides("3667556d-d8f5-454c-922b-af8af38f613c")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/3667556d-d8f5-454c-922b-af8af38f613c/2022-03-25")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:family is EMPTY; COMPLIANT if the value of dwc:family was found as a value at the rank of Family by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei], 'API endpoint' [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationFamilyFound(@ActedUpon("dwc:family") String family) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:family 
        // is EMPTY; COMPLIANT if the value of dwc:family was found 
        // as a value at the rank of Family by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?
     *
     * Provides: VALIDATION_SCIENTIFICNAME_FOUND
     * Version: 2022-03-22
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_FOUND", description="Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?")
    @Provides("3f335517-f442-4b98-b149-1e87ff16de45")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/3f335517-f442-4b98-b149-1e87ff16de45/2022-03-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is EMPTY; COMPLIANT if there is a match of the contents of dwc:scientificName with the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationScientificnameFound(@ActedUpon("dwc:scientificName") String scientificName) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName 
        // is EMPTY; COMPLIANT if there is a match of the contents 
        // of dwc:scientificName with the bdq:sourceAuthority; otherwise 
        // NOT_COMPLIANT bdq:sourceAuthority default = "GBIF Backbone 
        // Taxonomy" [https://doi.org/10.15468/39omei],API endpoint 
        // [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.
     *
     * Provides: AMENDMENT_TAXONID_FROM_TAXON
     * Version: 2022-04-19
     *
     * @param class the provided dwc:class to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param cultivarEpithet the provided dwc:cultivarEpithet to evaluate
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
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
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_TAXONID_FROM_TAXON", description="Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.")
    @Provides("431467d6-9b4b-48fa-a197-cd5379f5e889")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/431467d6-9b4b-48fa-a197-cd5379f5e889/2022-04-19")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available;  INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is not EMPTY or if all of dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, and dwc:cultivarEpithet are EMPTY, FILLED_IN the value of taxonID for an unambiguously resolved single taxon record in the bdq:sourceAuthority through (1) the value of dwc:scientificName or (2) if dwc:scientificName is EMPTY through values of the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship and dwc:cultivarEpithet, or (3) if ambiguity produced by multiple matches in (1) or (2) can be disambiguated to a single Taxon using the values of dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:order, dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID, dwc:taxonomicRank, and dwc:vernacularName; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<AmendmentValue> amendmentTaxonidFromTaxon(@ActedUpon("dwc:class") String dwcclass, @ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:cultivarEpithet") String cultivarEpithet, @ActedUpon("dwc:taxonConceptID") String taxonConceptID, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:subfamily") String subfamily, @ActedUpon("dwc:scientificNameID") String scientificNameID, @ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:subgenus") String subgenus, @ActedUpon("dwc:higherClassification") String higherClassification, @ActedUpon("dwc:vernacularName") String vernacularName, @ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, @ActedUpon("dwc:scientificNameAuthorship") String scientificNameAuthorship, @ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, @ActedUpon("dwc:genericName") String genericName, @ActedUpon("dwc:taxonRank") String taxonRank, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet, @ActedUpon("dwc:order") String order) {
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID 
        // is not EMPTY or if all of dwc:scientificName, dwc:genericName, 
        // dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
        // and dwc:cultivarEpithet are EMPTY, FILLED_IN the value of 
        // taxonID for an unambiguously resolved single taxon record 
        // in the bdq:sourceAuthority through (1) the value of dwc:scientificName 
        // or (2) if dwc:scientificName is EMPTY through values of 
        // the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, 
        // dwc:scientificNameAuthorship and dwc:cultivarEpithet, or 
        // (3) if ambiguity produced by multiple matches in (1) or 
        // (2) can be disambiguated to a single Taxon using the values 
        // of dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:order, 
        // dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, 
        // dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, 
        // dwc:taxonConceptID, dwc:taxonomicRank, and dwc:vernacularName; 
        // otherwise NOT_AMENDED bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?
     *
     * Provides: VALIDATION_TAXON_UNAMBIGUOUS
     * Version: 2022-06-24
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
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/4c09f127-737b-4686-82a0-7c8e30841590/2022-06-24")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if all of dwc:taxonID, dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, dwc:cultivarEpithet are EMPTY; COMPLIANT if (1) dwc:taxonId references a single taxon record in the bdq:sourceAuthority, or (2) dwc:taxonID is empty and dwc:scientificName references a single taxon record in the bdq:sourceAuthority, or (3) if dwc:scientificName and dwc:taxonID are EMPTY and if a combination of the values of the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:cultivarEpithet, dwc:taxonRank, and dwc:scientificNameAuthorship can be unambiguously resolved to a unique taxon in the bdq:sourceAuthority, or (4) if ambiguity produced by multiple matches in (2) or (3) can be disambiguated to a unique Taxon using the values of dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:order, dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID and dwc:vernacularName; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationTaxonUnambiguous(@ActedUpon("dwc:class") String dwcclass, @ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:cultivarEpithet") String cultivarEpithet, @ActedUpon("dwc:taxonConceptID") String taxonConceptID, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:subfamily") String subfamily, @ActedUpon("dwc:scientificNameID") String scientificNameID, @ActedUpon("dwc:infragenericEpithet") String infragenericEpithet, @ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:subgenus") String subgenus, @ActedUpon("dwc:higherClassification") String higherClassification, @ActedUpon("dwc:vernacularName") String vernacularName, @ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, @ActedUpon("dwc:scientificNameAuthorship") String scientificNameAuthorship, @ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, @ActedUpon("dwc:genericName") String genericName, @ActedUpon("dwc:taxonRank") String taxonRank, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet, @ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if all 
        // of dwc:taxonID, dwc:scientificName, dwc:genericName, dwc:specificEpithet, 
        // dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
        // dwc:cultivarEpithet are EMPTY; COMPLIANT if (1) dwc:taxonId 
        // references a single taxon record in the bdq:sourceAuthority, 
        // or (2) dwc:taxonID is empty and dwc:scientificName references 
        // a single taxon record in the bdq:sourceAuthority, or (3) 
        // if dwc:scientificName and dwc:taxonID are EMPTY and if a 
        // combination of the values of the terms dwc:genericName, 
        // dwc:specificEpithet, dwc:infraspecificEpithet, dwc:cultivarEpithet, 
        // dwc:taxonRank, and dwc:scientificNameAuthorship can be unambiguously 
        // resolved to a unique taxon in the bdq:sourceAuthority, or 
        // (4) if ambiguity produced by multiple matches in (2) or 
        // (3) can be disambiguated to a unique Taxon using the values 
        // of dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:order, 
        // dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, 
        // dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, 
        // dwc:taxonConceptID and dwc:vernacularName; otherwise NOT_COMPLIANT 
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority.
     *
     * Provides: AMENDMENT_SCIENTIFICNAME_FROM_TAXONID
     * Version: 2022-04-19
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_SCIENTIFICNAME_FROM_TAXONID", description="Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority.")
    @Provides("f01fb3f9-2f7e-418b-9f51-adf50f202aea")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/f01fb3f9-2f7e-418b-9f51-adf50f202aea/2022-04-19")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY, the value of dwc:taxonID is ambiguous or dwc:scientificName was not EMPTY; FILLED_IN the value of dwc:scientificName if the value of dwc:taxonID could be unambiguously interpreted as a value in bdq:sourceAuthority; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<AmendmentValue> amendmentScientificnameFromTaxonid(@ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:scientificName") String scientificName) {
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID 
        // is EMPTY, the value of dwc:taxonID is ambiguous or dwc:scientificName 
        // was not EMPTY; FILLED_IN the value of dwc:scientificName 
        // if the value of dwc:taxonID could be unambiguously interpreted 
        // as a value in bdq:sourceAuthority; otherwise NOT_AMENDED 
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Is there a value in dwc:dataGeneralizations?
     *
     * Provides: ISSUE_DATAGENERALIZATIONS_NOTEMPTY
     * Version: 2022-05-16
     *
     * @param dataGeneralizations the provided dwc:dataGeneralizations to evaluate
     * @return DQResponse the response of type IssueValue to return
     */
    @Issue(label="ISSUE_DATAGENERALIZATIONS_NOTEMPTY", description="Is there a value in dwc:dataGeneralizations?")
    @Provides("13d5a10e-188e-40fd-a22c-dbaa87b91df2")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/13d5a10e-188e-40fd-a22c-dbaa87b91df2/2022-05-16")
    @Specification("POTENTIAL_ISSUE if dwc:dataGeneralizations is not EMPTY; otherwise NOT_ISSUE ")
    public DQResponse<IssueValue> issueDatageneralizationsNotempty(@ActedUpon("dwc:dataGeneralizations") String dataGeneralizations) {
        DQResponse<IssueValue> result = new DQResponse<IssueValue>();

        //TODO:  Implement specification
        // POTENTIAL_ISSUE if dwc:dataGeneralizations is not EMPTY; 
        // otherwise NOT_ISSUE 

        return result;
    }

    /**
     * Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_CLASS_FOUND
     * Version: 2022-04-22
     *
     * @param class the provided dwc:class to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASS_FOUND", description="Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?")
    @Provides("2cd6884e-3d14-4476-94f7-1191cfff309b")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2cd6884e-3d14-4476-94f7-1191cfff309b/2022-04-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:class is EMPTY; COMPLIANT if the value of dwc:class was found as a value at the rank of Class in the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationClassFound(@ActedUpon("dwc:class") String dwcclass) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:class 
        // is EMPTY; COMPLIANT if the value of dwc:class was found 
        // as a value at the rank of Class in the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_KINGDOM_FOUND
     * Version: 2022-03-22
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_KINGDOM_FOUND", description="Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?")
    @Provides("125b5493-052d-4a0d-a3e1-ed5bf792689e")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/125b5493-052d-4a0d-a3e1-ed5bf792689e/2022-03-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:kingdom is EMPTY; COMPLIANT if the value of dwc:kingdom was found as a value at the rank of kingdom by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationKingdomFound(@ActedUpon("dwc:kingdom") String kingdom) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:kingdom 
        // is EMPTY; COMPLIANT if the value of dwc:kingdom was found 
        // as a value at the rank of kingdom by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Is there a value in dwc:scientificName?
     *
     * Provides: VALIDATION_SCIENTIFICNAME_NOTEMPTY
     * Version: 2022-03-22
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_NOTEMPTY", description="Is there a value in dwc:scientificName?")
    @Provides("7c4b9498-a8d9-4ebb-85f1-9f200c788595")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/7c4b9498-a8d9-4ebb-85f1-9f200c788595/2022-03-22")
    @Specification("COMPLIANT if dwc:scientificName is not EMPTY; otherwise NOT_COMPLIANT ")
    public DQResponse<ComplianceValue> validationScientificnameNotempty(@ActedUpon("dwc:scientificName") String scientificName) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // COMPLIANT if dwc:scientificName is not EMPTY; otherwise 
        // NOT_COMPLIANT 

        return result;
    }

    /**
     * Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_ORDER_FOUND
     * Version: 2022-03-25
     *
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_ORDER_FOUND", description="Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?")
    @Provides("81cc974d-43cc-4c0f-a5e0-afa23b455aa3")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/81cc974d-43cc-4c0f-a5e0-afa23b455aa3/2022-03-25")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:order is EMPTY; COMPLIANT if the value of dwc:order was found as a value at the rank of Order by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationOrderFound(@ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:order 
        // is EMPTY; COMPLIANT if the value of dwc:order was found 
        // as a value at the rank of Order by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?
     *
     * Provides: VALIDATION_POLYNOMIAL_CONSISTENT
     * Version: 2022-04-03
     *
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_POLYNOMIAL_CONSISTENT", description="Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?")
    @Provides("17f03f1f-f74d-40c0-8071-2927cfc9487b")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/17f03f1f-f74d-40c0-8071-2927cfc9487b/2022-04-03")
    @Specification("INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is EMPTY, or all of dwc:genericName, dwc:specificEpithet and dwc:infraspecificEpithet are EMPTY; COMPLIANT if the polynomial, as represented in dwc:scientificName, is consistent with NOT_EMPTY values of dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet; otherwise NOT_COMPLIANT. ")
    public DQResponse<ComplianceValue> validationPolynomialConsistent(@ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:genericName") String genericName, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is 
        // EMPTY, or all of dwc:genericName, dwc:specificEpithet and 
        // dwc:infraspecificEpithet are EMPTY; COMPLIANT if the polynomial, 
        // as represented in dwc:scientificName, is consistent with 
        // NOT_EMPTY values of dwc:genericName, dwc:specificEpithet, 
        // dwc:infraspecificEpithet; otherwise NOT_COMPLIANT. 

        return result;
    }


    /**
     * Does the value of dwc:taxonID contain a complete identifier?
     *
     * Provides: VALIDATION_TAXONID_COMPLETE
     * Version: 2022-11-07
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONID_COMPLETE", description="Does the value of dwc:taxonID contain a complete identifier?")
    @Provides("a82c7e3a-3a50-4438-906c-6d0fefa9e984")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/a82c7e3a-3a50-4438-906c-6d0fefa9e984/2022-11-07")
    @Specification("INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY; COMPLIANT if (1) taxonID is a validly formed LSID, or (2) taxonID is a validly formed URN with at least NID and NSS present, or (3) taxonID is in the form scope:value, or (4) taxonID is a validly formed URI with host and path where path consists of more than just '/'; otherwise NOT_COMPLIANT ")
    public DQResponse<ComplianceValue> validationTaxonidComplete(@ActedUpon("dwc:taxonID") String taxonID) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY; 
        // COMPLIANT if (1) taxonID is a validly formed LSID, or (2) 
        // taxonID is a validly formed URN with at least NID and NSS 
        // present, or (3) taxonID is in the form scope:value, or (4) 
        // taxonID is a validly formed URI with host and path where 
        // path consists of more than just "/"; otherwise NOT_COMPLIANT 
        // 

        return result;
    }

    /**
     * Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_GENUS_FOUND
     * Version: 2022-03-22
     *
     * @param genus the provided dwc:genus to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_GENUS_FOUND", description="Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?")
    @Provides("f2ce7d55-5b1d-426a-b00e-6d4efe3058ec")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/f2ce7d55-5b1d-426a-b00e-6d4efe3058ec/2022-03-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available;  INTERNAL_PREREQUISITES_NOT_MET if dwc:genus is EMPTY; COMPLIANT if the value of dwc:genus was found as a value at the rank of genus by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationGenusFound(@ActedUpon("dwc:genus") String genus) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:genus 
        // is EMPTY; COMPLIANT if the value of dwc:genus was found 
        // as a value at the rank of genus by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?
     *
     * Provides: VALIDATION_CLASSIFICATION_CONSISTENT
     * Version: 2022-08-24
     *
     * @param class the provided dwc:class to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param family the provided dwc:family to evaluate
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASSIFICATION_CONSISTENT", description="Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?")
    @Provides("2750c040-1d4a-4149-99fe-0512785f2d5f")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2750c040-1d4a-4149-99fe-0512785f2d5f/2022-08-24")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if all of the fields dwc:kingdom dwc:phylum, dwc:class, dwc:order, dwc:family, dwc:subfamily, dwc:genericName are EMPTY; COMPLIANT if the combination of values of higher classification taxonomic terms (dwc:kingdom, dwc:phylum, dwc:class, dwc:order, dwc:family, dwc:subfamily, dwc:genus) are consistent with the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public DQResponse<ComplianceValue> validationClassificationConsistent(@ActedUpon("dwc:class") String dwcclass, @ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:subfamily") String subfamily, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if all 
        // of the fields dwc:kingdom dwc:phylum, dwc:class, dwc:order, 
        // dwc:family, dwc:subfamily, dwc:genericName are EMPTY; COMPLIANT 
        // if the combination of values of higher classification taxonomic 
        // terms (dwc:kingdom, dwc:phylum, dwc:class, dwc:order, dwc:family, 
        // dwc:subfamily, dwc:genus) are consistent with the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Does the value of dwc:taxonRank occur in bdq:sourceAuthority?
     *
     * Provides: VALIDATION_TAXONRANK_STANDARD
     * Version: 2022-03-22
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONRANK_STANDARD", description="Does the value of dwc:taxonRank occur in bdq:sourceAuthority?")
    @Provides("7bdb13a4-8a51-4ee5-be7f-20693fdb183e")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/7bdb13a4-8a51-4ee5-be7f-20693fdb183e/2022-03-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonRank is EMPTY; COMPLIANT if the value of dwc:taxonRank is in the bdq:sourceAuthority; otherwise NOT_COMPLIANT. bdq:sourceAuthority default = 'GBIF Vocabulary: Taxonomic Rank' [https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]")
    public DQResponse<ComplianceValue> validationTaxonrankStandard(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonRank 
        // is EMPTY; COMPLIANT if the value of dwc:taxonRank is in 
        // the bdq:sourceAuthority; otherwise NOT_COMPLIANT. bdq:sourceAuthority 
        // default = "GBIF Vocabulary: Taxonomic Rank" [https://api.gbif.org/v1/vocabularies/TaxonRank/concepts] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.
     *
     * Provides: AMENDMENT_TAXONRANK_STANDARDIZED
     * Version: 2023-03-20
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_TAXONRANK_STANDARDIZED", description="Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.")
    @Provides("e39098df-ef46-464c-9aef-bcdeee2a88cb")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/e39098df-ef46-464c-9aef-bcdeee2a88cb/2023-03-20")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL PREREQUISITES_NOT_MET if dwc:taxonRank is EMPTY; AMENDED the value of dwc:taxonRank if it can be unambiguously matched to a term in bdq:sourceAuthority; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Vocabulary: Taxonomic Rank' [https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]")
    public DQResponse<AmendmentValue> amendmentTaxonrankStandardized(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL PREREQUISITES_NOT_MET if dwc:taxonRank 
        // is EMPTY; AMENDED the value of dwc:taxonRank if it can be 
        // unambiguously matched to a term in bdq:sourceAuthority; 
        // otherwise NOT_AMENDED bdq:sourceAuthority default = "GBIF 
        // Vocabulary: Taxonomic Rank" [https://api.gbif.org/v1/vocabularies/TaxonRank/concepts] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

}
