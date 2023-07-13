/* NOTE: requires the ffdq-api dependecy in the maven pom.xml */

package org.filteredpush.qc.sciname;

import org.datakurator.ffdq.annotations.*;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.model.ResultState;
import org.datakurator.ffdq.api.result.*;

@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",label="Kurator: Scientific Name Validator - DwCSciNameDQ:v1.0.1-SNAPSHOT")
public class DwCSciNameDQ_stubs {


    /**
     * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.
     *
     * Provides: AMENDMENT_TAXONID_FROM_TAXON
     * Version: 2023-07-04
     *
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param scientificNameAuthorship the provided dwc:scientificNameAuthorship to evaluate
     * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param class the provided dwc:class to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param cultivarEpithet the provided dwc:cultivarEpithet to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subgenus the provided dwc:subgenus to evaluate
     * @param higherClassification the provided dwc:higherClassification to evaluate
     * @param vernacularName the provided dwc:vernacularName to evaluate
     * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @param family the provided dwc:family to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_TAXONID_FROM_TAXON", description="Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.")
    @Provides("431467d6-9b4b-48fa-a197-cd5379f5e889")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/431467d6-9b4b-48fa-a197-cd5379f5e889/2023-07-04")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available;  INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is not EMPTY or if all of dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, and dwc:cultivarEpithet are EMPTY, FILLED_IN the value of taxonID for an unambiguously resolved single taxon record in the bdq:sourceAuthority through (1) the value of dwc:scientificName or (2) if dwc:scientificName is EMPTY through values of the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship and dwc:cultivarEpithet, or (3) if ambiguity produced by multiple matches in (1) or (2) can be disambiguated to a single Taxon using the values of dwc:subtribe, dwc:tribe, dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:superfamily, dwc:order, dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID, dwc:taxonomicRank, and dwc:vernacularName; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public DQResponse<AmendmentValue> amendmentTaxonidFromTaxon(@ActedUpon("dwc:taxonConceptID") String taxonConceptID, @ActedUpon("dwc:scientificNameID") String scientificNameID, @ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:scientificNameAuthorship") String scientificNameAuthorship, @ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:specificEpithet") String specificEpithet, @ActedUpon("dwc:class") String taxonomic_class, @ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:cultivarEpithet") String cultivarEpithet, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:subfamily") String subfamily, @ActedUpon("dwc:tribe") String tribe, @ActedUpon("dwc:subgenus") String subgenus, @ActedUpon("dwc:higherClassification") String higherClassification, @ActedUpon("dwc:vernacularName") String vernacularName, @ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, @ActedUpon("dwc:subtribe") String subtribe, @ActedUpon("dwc:genericName") String genericName, @ActedUpon("dwc:superfamily") String superfamily, @ActedUpon("dwc:taxonRank") String taxonRank, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:order") String order) {
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
        // of dwc:subtribe, dwc:tribe, dwc:subgenus, dwc:genus, dwc:subfamily, 
        // dwc:family, dwc:superfamily, dwc:order, dwc:class, dwc:phylum, 
        // dwc:kingdom, dwc:higherClassification, dwc:scientificNameID, 
        // dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID, 
        // dwc:taxonomicRank, and dwc:vernacularName; otherwise NOT_AMENDED 
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" {[https://doi.org/10.15468/39omei]} 
        // {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 
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
