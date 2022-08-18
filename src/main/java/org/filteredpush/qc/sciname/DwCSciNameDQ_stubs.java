/* NOTE: requires the ffdq-api dependecy in the maven pom.xml */

package org.filteredpush.qc.sciname;

import org.datakurator.ffdq.annotations.*;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.model.ResultState;
import org.datakurator.ffdq.api.result.*;

@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",label="Kurator: Scientific Name Validator - DwCSciNameDQ:v0.0.1")
public class DwCSciNameDQ_stubs {

    
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
        // bdq:sourceAuthority default="Taxonomic Rank GBIF Vocabulary"

        return result;
    }

}
