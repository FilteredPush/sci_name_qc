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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datakurator.ffdq.annotations.*;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.model.ResultState;
import org.filteredpush.qc.sciname.services.GBIFService;
import org.filteredpush.qc.sciname.services.IRMNGService;
import org.filteredpush.qc.sciname.services.ServiceException;
import org.filteredpush.qc.sciname.services.Validator;
import org.filteredpush.qc.sciname.services.WoRMSService;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.NameParser;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.Rank;
import org.gbif.nameparser.api.UnparsableNameException;
import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.xml.sax.SAXException;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.NameAuthorshipParse;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

import org.datakurator.ffdq.api.result.*;

/**
 * Implementation of the TDWG TG2 NAME (scientific name) related data quality tests.
 *
 * #212 VALIDATION_SCIENTIFICNAMEID_COMPLETE 6eeac3ed-f691-457f-a42e-eaa9c8a71ce8
 * #120	VALIDATION_SCIENTIFICNAMEID_NOTEMPTY 401bf207-9a55-4dff-88a5-abcd58ad97fa
 * #105	VALIDATION_TAXON_NOTEMPTY 06851339-843f-4a43-8422-4e61b9a00e75
 * #123	VALIDATION_CLASSIFICATION_CONSISTENT 2750c040-1d4a-4149-99fe-0512785f2d5f
 * #70	VALIDATION_TAXON_UNAMBIGUOUS 4c09f127-737b-4686-82a0-7c8e30841590
 * #81	VALIDATION_KINGDOM_FOUND 125b5493-052d-4a0d-a3e1-ed5bf792689e
 * #22	VALIDATION_PHYLUM_FOUND eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f
 * #77	VALIDATION_CLASS_FOUND 2cd6884e-3d14-4476-94f7-1191cfff309b
 * #83	VALIDATION_ORDER_FOUND 81cc974d-43cc-4c0f-a5e0-afa23b455aa3
 * #28	VALIDATION_FAMILY_FOUND 3667556d-d8f5-454c-922b-af8af38f613c
 * #122	VALIDATION_GENUS_FOUND f2ce7d55-5b1d-426a-b00e-6d4efe3058ec
 * #82	VALIDATION_SCIENTIFICNAME_NOTEMPTY 7c4b9498-a8d9-4ebb-85f1-9f200c788595
 * #46	VALIDATION_SCIENTIFICNAME_FOUND 3f335517-f442-4b98-b149-1e87ff16de45
 * #101	VALIDATION_POLYNOMIAL_CONSISTENT 17f03f1f-f74d-40c0-8071-2927cfc9487b
 * #161	VALIDATION_TAXONRANK_NOTEMPTY 14da5b87-8304-4b2b-911d-117e3c29e890
 * #162	VALIDATION_TAXONRANK_STANDARD 7bdb13a4-8a51-4ee5-be7f-20693fdb183e
 * #216 VALIDATION_KINGDOM_NOTEMPTY 36ed36c9-b1a7-40b2-b5e2-0d012e772098/2024-01-28
 * #244 VALIDATION_SCIENTIFICNAMEAUTHORSHIP_NOTEMPTY 49f1d386-5bed-43ae-bd43-deabf7df64fc"
 * #259 VALIDATION_NAMEPUBLISHEDINYEAR_NOTEMPTY ff59f77d-71e9-4eb1-aac9-8bd05c50ff70
 *
 * #57	AMENDMENT_SCIENTIFICNAMEID_FROM_TAXON 431467d6-9b4b-48fa-a197-cd5379f5e889
 * #71	AMENDMENT_SCIENTIFICNAME_FROM_SCIENTIFICNAMEID f01fb3f9-2f7e-418b-9f51-adf50f202aea
 * #163	AMENDMENT_TAXONRANK_STANDARDIZED e39098df-ef46-464c-9aef-bcdeee2a88cb
 *
 * Also, with amendmentScientificnameidFromTaxon(taxon, sourceAuthority, replaceExisting) provides
 * a variant of #57 that allows existing scientificNameID values to be conformed to a specified sourceAuthority
 * based on a lookup of the taxon terms on that authority.
 *
 * Supplementary tests:
 * #121	VALIDATION_TAXONID_COMPLETE a82c7e3a-3a50-4438-906c-6d0fefa9e984
 * #64 VALIDATION_NAMEPUBLISHEDINYEAR_INRANGE 399ef91d-425c-46f2-a6df-8a0fe4c3e86e
 * #254 VALIDATION_VERNACULARNAME_NOTEMPTY eb9b70c7-9cf6-42ea-a708-5de527211df4
 * #229 VALIDATION_HIGHERCLASSIFICATION_NOTEMPTY 72ed16f1-0587-4afd-a9fc-602c2f3f1975
 * #218 VALIDATION_PHYLUM_NOTEMPTY 19bbd107-6b14-4c82-8d3e-f7a2a67df309
 * #213 VALIDATION_CLASS_NOTEMPTY "b854b179-3572-48b6-aab2-879e3172fd7d
 * #217 VALIDATION_ORDER_NOTEMPTY "d1c40fc8-d8ad-4148-82e0-b4b7ead70051
 * #215 VALIDATION_FAMILY_NOTEMPTY 5bfa043c-1e19-4224-8d55-b568ced347c2
 * #214 VALIDATION_GENUS_NOTEMPTY d02c1ffd-af28-49bd-9c9c-e8e23a8b7258
 * #246 VALIDATION_TYPESTATUS_NOTEMPTY cd7cae15-f255-41a3-b002-c9620c40f620
 * #220 VALIDATION_SPECIFICEPITHET_NOTEMPTY cdb37443-292e-49b6-a012-4718f0d7ba64
 * #219 VALIDATION_INFRASPECIFICEPITHET_NOTEMPTY 77c6fde2-c3ca-4ad3-b2f2-3b81bba9a673
 *
 * Supplementary tests where the default source authority does not yet provide data
 * #207 VALIDATION_TRIBE_FOUND 8c15f351-26d5-4edd-b38e-07541dc64fd0 
 * #208 VALIDATION_SUBTRIBE_FOUND 4527c47e-61d9-4abb-af3e-f2999191be17
 * #206 VALIDATION_SUPERFAMILY_FOUND 2a45e0e9-446c-429f-992d-c3ec1d29eebb
 * 
 * @author mole
 * @version $Id: $Id
 */
@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",
	label="Kurator: Scientific Name Validator - DwCSciNameDQ:v1.1.0-SNAPSHOT")
public class DwCSciNameDQ {
	
	private static final Log logger = LogFactory.getLog(DwCSciNameDQ.class);
	
	
    /**
     * Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?
     *
     * Provides: #22 VALIDATION_PHYLUM_FOUND
     * Version: 2022-03-25
     *
     * @param phylum the provided dwc:phylum to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_PHYLUM_FOUND", description="Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?")
    @Provides("eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f/2022-03-25")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:phylum is EMPTY; COMPLIANT if the value of dwc:phylum was found as a value at the rank of Phylum by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei], 'API endpoint' [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public static DQResponse<ComplianceValue> validationPhylumFound(@ActedUpon("dwc:phylum") String phylum, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:phylum 
        // is EMPTY; COMPLIANT if the value of dwc:phylum was found 
        // as a value at the rank of Phylum by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"

        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage());
			}
        }
        return validateHigherTaxonAtRank(phylum, "Phylum", sourceAuthority);
    }

    /**
     * Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?
     *
     * Provides: #28 VALIDATION_FAMILY_FOUND
     * Version: 2023-09-17
     *
     * @param family the provided dwc:family to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_FAMILY_FOUND", description="Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?")
    @Provides("3667556d-d8f5-454c-922b-af8af38f613c")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/3667556d-d8f5-454c-922b-af8af38f613c/2023-09-17")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:family is EMPTY; COMPLIANT if the value of dwc:family was found as a value at the rank of Family by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationFamilyFound(@ActedUpon("dwc:family") String family, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:family 
        // is EMPTY; COMPLIANT if the value of dwc:family was found 
        // as a value at the rank of Family by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT 
        // bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" {[https://doi.org/10.15468/39omei]} {API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"

        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        return validateHigherTaxonAtRank(family, "Family", sourceAuthority);
    }

    /**
     * Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?
     *
     * Provides: #46 VALIDATION_SCIENTIFICNAME_FOUND
     * Version: 2023-09-17
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_FOUND", description="Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?")
    @Provides("3f335517-f442-4b98-b149-1e87ff16de45")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/3f335517-f442-4b98-b149-1e87ff16de45/2023-09-17")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is EMPTY; COMPLIANT if there is a match of the contents of dwc:scientificName with the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationScientificnameFound(
    		@ActedUpon("dwc:scientificName") String scientificName,
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
    	) {

        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName 
        // is EMPTY; COMPLIANT if there is a match of the contents 
        // of dwc:scientificName with the bdq:sourceAuthority; otherwise 
        // NOT_COMPLIANT 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default = "GBIF Backbone 
        // Taxonomy" {[https://doi.org/10.15468/39omei]} {API endpoint 
        // [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 
        
        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        if (SciNameUtils.isEmpty(scientificName)) { 
        	result.addComment("No value provided for dwc:scientificName.");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	Validator service = null;
        	if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY)) { 
        		try {
        			service= new GBIFService(false);
        		} catch (IOException e) { 
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}
        	} else if (sourceAuthority.isGBIFChecklist()) { 
        		try {
        			service = new GBIFService(sourceAuthority.getAuthoritySubDataset(), false);
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}        		
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
        		try {
        			service = new WoRMSService(false);
        		} catch (IOException e) {
        			result.addComment("Error setting up WoRMS aphia API:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        			logger.error(e.getMessage(),e);
        		}
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) {
        		try {
        			service = new IRMNGService(false);
        		} catch (IOException e) {
        			result.addComment("Error setting up IRMNG aphia API:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        			logger.error(e.getMessage(),e);
        		}
        	}         	
        	if (service==null) { 
        		result.addComment("Source Authority Not Implemented.");
        		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	} else { 
        		NameUsage toValidate = new NameUsage();
        		boolean set = false;
        		try { 
        			NameAuthorshipParse parsedName = SciNameUtils.getNameWithoutAuthorship(scientificName);
        			toValidate.setScientificName(parsedName.getNameWithoutAuthorship());
        			toValidate.setAuthorship(parsedName.getAuthorship());
        			logger.debug(parsedName.getNameWithoutAuthorship());
        			logger.debug(parsedName.getAuthorship());
        			set = true;
        		} catch (UnparsableNameException e) { 
        			result.addComment("Unable to parse authorship out of provided scientificName, trying GNI and GBIF parser service with ["+scientificName+"].");
        			logger.debug(e.getMessage(), e);
        			// If local parse fails, try a parse via the GBIF service.
        			// This supports embedding the library in coldfusion for MCZbase, where 
        			// there is a library version conflict with the guava version provided
        			// by coldfusion and the version needed by the GBIF name parser.
        			try { 
        				NameAuthorshipParse parsedName = GBIFService.parseAuthorshipFromNameString(scientificName);
        				toValidate.setScientificName(parsedName.getNameWithoutAuthorship());
        				toValidate.setAuthorship(parsedName.getAuthorship());
        				set = true;
        			} catch (Exception ex) {
        				// simple failover handler, try the full name
        				result.addComment("Unable to parse authorship out of provided scientificName, looking up full name.");
        				result.addComment(e.getMessage());	
        			} 
        		} catch (Exception e) {
       				// could be thrown from parser.close()
        			logger.error(e.getMessage(), e);
        		}
        		if (!set) { 
        			toValidate.setScientificName(scientificName);
        		}
        		try {
        			logger.debug(toValidate.getScientificName());
        			logger.debug(toValidate.getAuthorship());
					NameUsage validationResponse = service.validate(toValidate);
					if (validationResponse==null) { 
						result.addComment("No Match found for dwc:scientificName["+scientificName+"] parsed as name=["+toValidate.getScientificName()+"], authorship=["+toValidate.getAuthorship()+"] in ["+sourceAuthority.getName()+"]");
						result.setResultState(ResultState.RUN_HAS_RESULT);
						result.setValue(ComplianceValue.NOT_COMPLIANT);
					} else { 
						logger.debug(validationResponse.getMatchDescription());
						logger.debug(validationResponse.getNameMatchDescription());
						if (validationResponse.getMatchDescription().equals(NameComparison.MATCH_EXACT)) { 
							result.addComment("Exact Match found for ["+scientificName+"] to ["+validationResponse.getGuid()+"]");
							result.setResultState(ResultState.RUN_HAS_RESULT);
							result.setValue(ComplianceValue.COMPLIANT);
						} else if (validationResponse.getMatchDescription().startsWith(NameComparison.MATCH_MULTIPLE) && SciNameUtils.isEmpty(toValidate.getAuthorship()) && validationResponse.getNameMatchDescription().equals(NameComparison.MATCH_EXACT)) { 
							result.addComment("Exact Match found for ["+scientificName+"] where authorship is empty to ["+validationResponse.getAcceptedName()+"]");
							result.setResultState(ResultState.RUN_HAS_RESULT);
							result.setValue(ComplianceValue.COMPLIANT);
						} else { 
							result.addComment("No Exact Match found for ["+scientificName+"] in ["+sourceAuthority.getName()+"]");
							result.addComment("Non-Exact match ["+ validationResponse.getScientificName() + " " + validationResponse.getAuthorship() +"]");
							result.addComment("Match on canonical name is: " + validationResponse.getNameMatchDescription()  );
							result.addComment("Match on canonical name+authorship is " + validationResponse.getMatchDescription());
							result.addComment("Canonical name string edit distance is " + validationResponse.getScientificNameStringEditDistance());
							result.addComment("Authorship string edit distance is " + validationResponse.getAuthorshipStringEditDistance());
							result.setResultState(ResultState.RUN_HAS_RESULT);
							result.setValue(ComplianceValue.NOT_COMPLIANT);
						}
					}
				} catch (ServiceException e) {
					logger.debug(e.getMessage());
					result.addComment("Error Invoking Remote Service");
					result.addComment(e.getMessage());
					result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
				}
        	}
        }

        return result;
    }

    
    /**
     * Propose amendment to the value of dwc:scientificNameID if it can be unambiguously resolved from bdq:sourceAuthority
     * using the available taxon terms.
     *
     * Provides: #57 AMENDMENT_SCIENTIFICNAMEID_FROM_TAXON
     * Version: 2023-09-17
     *
     * @param scientificNameID the provided dwc:scientificNameID to evaluate as ActedUpon.
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
     * @param genericName the provided dwc:genericName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @param cultivarEpithet the provided dwc:cultivarEpithet to evaluate
     * @param higherClassification the provided dwc:higherClassification to evaluate
     * @param vernacularName the provided dwc:vernacularName to evaluate
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
     * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to evaluate against, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_SCIENTIFICNAMEID_FROM_TAXON", description="Propose amendment to the value of dwc:scientificNameID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.")
    @Provides("431467d6-9b4b-48fa-a197-cd5379f5e889")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/431467d6-9b4b-48fa-a197-cd5379f5e889/2023-09-17")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available;  INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID is not EMPTY or if all of dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, and dwc:cultivarEpithet are EMPTY, FILLED_IN the value of dwc:scientificNameID for an unambiguously resolved single taxon record in the bdq:sourceAuthority through (1) the value of dwc:scientificName or (2) if dwc:scientificName is EMPTY through values of the terms dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship and dwc:cultivarEpithet, or (3) if ambiguity produced by multiple matches in (1) or (2) can be disambiguated to a single Taxon using the values of dwc:subtribe, dwc:tribe, dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:superfamily, dwc:order, dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, dwc:taxonID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID, dwc:taxonomicRank, and dwc:vernacularName; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
	public static  DQResponse<AmendmentValue> amendmentScientificnameidFromTaxon(
			@ActedUpon("dwc:scientificNameID") String scientificNameID, 
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
			@Consulted("dwc:taxonID") String taxonID, 
			@Consulted("dwc:originalNameUsageID") String originalNameUsageID, 
			@Consulted("dwc:acceptedNameUsageID") String acceptedNameUsageID,
			@Consulted("dwc:superfamily") String superfamily,
			@Consulted("dwc:tribe") String tribe,
			@Consulted("dwc:subtribe") String subtribe,
			@Parameter(name="bdq:sourceAuthority") String sourceAuthority
	){
		DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();
		SciNameSourceAuthority sourceAuthorityObject;
		try {
			if (SciNameUtils.isEmpty(sourceAuthority)) { 
				sourceAuthorityObject = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} else { 
				sourceAuthorityObject = new SciNameSourceAuthority(sourceAuthority);
			}
			result = amendmentScientificnameidFromTaxon(new Taxon(taxonID, kingdom, phylum, taxonomic_class, order, family, subfamily,
			genus, subgenus, scientificName, scientificNameAuthorship, genericName, specificEpithet,
			infraspecificEpithet, taxonRank, cultivarEpithet, higherClassification, vernacularName, taxonConceptID,
			scientificNameID, originalNameUsageID, acceptedNameUsageID, superfamily, tribe, subtribe), sourceAuthorityObject);
		} catch (SourceAuthorityException e) {
			logger.error(e.getMessage());
			result.addComment("Unable to process:" + e.getMessage());
			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
		}
		return result;
	}

	/**
	 * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority
	 * using the available taxon terms.  Utility method using a Taxon object as input instead of a separate annotated
	 * parameters for each taxon term.
	 *
	 * Provides: #57 AMENDMENT_SCIENTIFICNAMEID_FROM_TAXON
	 *
	 * @param taxon a dwc:Taxon object to evaluate.
	 * @param sourceAuthority the bdq:sourceAuthority to evaluate against, defaults to GBIF Backbone Taxonomy if null
	 * @return DQResponse the response of type AmendmentValue to return
	 */
	public static  DQResponse<AmendmentValue> amendmentScientificnameidFromTaxon(
			Taxon taxon, 
			SciNameSourceAuthority sourceAuthority
			){
		return amendmentScientificnameidFromTaxon(taxon,sourceAuthority,false);
	}
	
	
	/**
	 * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.
	 * Includes a non-standard option to conform existing dwc:taxonID values to the sourceAuthority vocabulary.
	 *
	 * Provides: #57 AMENDMENT_SCIENTIFICNAMEID_FROM_TAXON, but with option of overwriting existing values
	 * Version: 2023-09-17
	 *
	 * @param taxon a dwc:Taxon object to evaluate.
	 * @param sourceAuthority the bdq:sourceAuthority to evaluate against, defaults to GBIF Backbone Taxonomy if null
	 * @return DQResponse the response of type AmendmentValue to return
	 * @param replaceExisting a boolean.
	 */
	public static  DQResponse<AmendmentValue> amendmentScientificnameidFromTaxon(
			Taxon taxon, 
			SciNameSourceAuthority sourceAuthority,
			boolean replaceExisting
			){

		DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID 
        // is not EMPTY or if all of dwc:scientificName, dwc:genericName, 
        // dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
        // and dwc:cultivarEpithet are EMPTY, FILLED_IN the value of 
        // dwc:scientificNameID for an unambiguously resolved single 
        // taxon record in the bdq:sourceAuthority through (1) the 
        // value of dwc:scientificName or (2) if dwc:scientificName 
        // is EMPTY through values of the terms dwc:genericName, dwc:specificEpithet, 
        // dwc:infraspecificEpithet, dwc:scientificNameAuthorship and 
        // dwc:cultivarEpithet, or (3) if ambiguity produced by multiple 
        // matches in (1) or (2) can be disambiguated to a single Taxon 
        // using the values of dwc:subtribe, dwc:tribe, dwc:subgenus, 
        // dwc:genus, dwc:subfamily, dwc:family, dwc:superfamily, dwc:order, 
        // dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, 
        // dwc:taxonID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, 
        // dwc:taxonConceptID, dwc:taxonomicRank, and dwc:vernacularName; 
        // otherwise NOT_AMENDED 
        // 

		// Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default = "GBIF // Backbone Taxonomy" 
		// {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 

		// NOTES: ... Return a result with no value and a result state of 
		// NO CHANGE with ambiguous nature noted in the comment if the 
		// information provided does not resolve to a 
		// unique result (e.g. if homonyms exist and there is insufficient 
		// information in the provided data to resolve them)


		// **********
		// NOTE: replaceExisting=true produces non-standard behavior, 
		// only replaceExisting=false will return INTERNAL_PREREQUSITES_NOT_MET if provided taxonID 
		// contains a value 
		// **********
		if (!replaceExisting && !SciNameUtils.isEmpty(taxon.getScientificNameID())) { 
			result.addComment("dwc:scientificNameID already contains a value.  [" + taxon.getScientificNameID() + "].");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
		} else if (SciNameUtils.isEmpty(taxon.getScientificName()) 
				&& SciNameUtils.isEmpty(taxon.getGenericName()) 
				&& SciNameUtils.isEmpty(taxon.getSpecificEpithet()) 
				&& SciNameUtils.isEmpty(taxon.getInfraspecificEpithet()) 
				&& SciNameUtils.isEmpty(taxon.getScientificNameAuthorship())
				&& SciNameUtils.isEmpty(taxon.getCultivarEpithet())
				) 
		{
			result.addComment("No value provided for any of dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, or dwc:cultivarEpithet.");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
		} else { 
			if (sourceAuthority==null) { 
				try {
					sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
				} catch (SourceAuthorityException e) {
					logger.error(e.getMessage(),e);
				}
			}
			if (sourceAuthority==null) { 
				sourceAuthority = new SciNameSourceAuthority();
			}
			result.addComment("Provided taxon [" + taxon.toString() + "]");
			String lookMeUp = taxon.getScientificName();
			if (SciNameUtils.isEmpty(lookMeUp)) { 
				lookMeUp = taxon.getGenericName();
				if (!SciNameUtils.isEmpty(taxon.getSpecificEpithet())) { 
					lookMeUp = lookMeUp + " " + taxon.getSpecificEpithet();
				}
				if (!SciNameUtils.isEmpty(taxon.getInfraspecificEpithet())) { 
					lookMeUp = lookMeUp + " " + taxon.getInfraspecificEpithet();
				}
				if (!SciNameUtils.isEmpty(taxon.getCultivarEpithet())) { 
					lookMeUp = lookMeUp + " " + taxon.getCultivarEpithet();
				}
			}
			logger.debug(lookMeUp);
			if (!SciNameUtils.isEmpty(taxon.getScientificNameAuthorship())) { 
				if (lookMeUp.endsWith(taxon.getScientificNameAuthorship())) { 
					lookMeUp = lookMeUp.substring(0, lookMeUp.lastIndexOf(taxon.getScientificNameAuthorship())).trim();
				}
			}

			try {
				List<NameUsage> matchList = null;
				if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY)) { 
					String matches = GBIFService.searchForTaxon(lookMeUp, GBIFService.KEY_GBIFBACKBONE);
					logger.debug(matches);
					matchList = GBIFService.parseAllNameUsagesFromJSON(matches);
				} else if (sourceAuthority.isGBIFChecklist()) { 
					String matches = GBIFService.searchForTaxon(lookMeUp, sourceAuthority.getAuthoritySubDataset());
					logger.debug(matches);
					matchList = GBIFService.parseAllNameUsagesFromJSON(matches);					
				} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
					matchList = WoRMSService.lookupTaxon(lookMeUp, taxon.getScientificNameAuthorship());
				} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) {
					matchList = IRMNGService.lookupTaxon(lookMeUp, taxon.getScientificNameAuthorship());
				} else { 
					throw new UnsupportedSourceAuthorityException("Source Authority Not Implemented");
				} 
				logger.debug(matchList.size());
				if (matchList.size()>0) {
					result.addComment(matchList.size() + " potential matches returned from authority.");
					boolean hasMatch = false;
					int matchCounter = 0;
					Map<String,String> kvp = new HashMap<String,String>();
					Iterator<NameUsage> i = matchList.iterator();
					Set<String> matchedGUIDS = new HashSet<String>();   // to handle duplicate identical records in GBIF
					while (i.hasNext()) { 
						NameUsage match = i.next();
						logger.debug(match.getCanonicalName());
						if (	(taxon.sameHigherAs(match)) && 
								(SciNameUtils.isEqualOrNonEmpty(lookMeUp, match.getCanonicalName())) 
								) 
						{ 
							logger.debug(match.getCanonicalName());
							logger.debug(match.getAuthorship());
							logger.debug(match.getAuthorshipStringSimilarity());
							logger.debug(taxon.getScientificNameAuthorship());
							boolean authorshipOK = false;
							if (!SciNameUtils.isEmpty(taxon.getScientificNameAuthorship())) { 
								if (match.getAuthorComparator()==null) { 
									match.setAuthorComparator(AuthorNameComparator.authorNameComparatorFactory(taxon.getScientificNameAuthorship(), taxon.getKingdom()));
								}
								logger.debug(match.getAuthorComparator().compare(taxon.getScientificNameAuthorship(), match.getAuthorship()).getMatchType());
								NameComparison authorshipComparison = match.getAuthorComparator().compare(taxon.getScientificNameAuthorship(), match.getAuthorship());
								if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACT)) {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with exact match on authorship. ");
									authorshipOK = true;
								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACT_BRACKETS)) {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with exact match (except for square brackets) on authorship.");
									authorshipOK = true;
								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACTADDSYEAR)) {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
									authorshipOK = true;
								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACTMISSINGYEAR)) {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
									authorshipOK = true;
								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_SOWERBYEXACTYEAR)) {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
									authorshipOK = true;
								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_L_EXACTYEAR)) {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
									authorshipOK = true;
								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_SAMEBUTABBREVIATED)) {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
									authorshipOK = true;
								} else {
									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " has different author: " + authorshipComparison.getRemark());
									authorshipOK = false;
								}
							} else { 
								authorshipOK = true; // no basis to compare, assume ok.
							}
							logger.debug(match.getGuid());
							if (!SciNameUtils.isEmpty(match.getGuid()) && authorshipOK) {
								if (sourceAuthority.isGBIFChecklist()) { 
									logger.debug(match.getKey());
									logger.debug(match.getAcceptedKey());
									// handle duplicate records in GBIF
									// assuming that GBIF results are sorted so that the accepted name comes before identical synonym records.
									if (!matchedGUIDS.contains(Integer.toString(match.getAcceptedKey()))) {
										hasMatch=true;
										matchCounter++;
										kvp.put("dwc:scientificNameID", match.getGuid());
										matchedGUIDS.add(Integer.toString(match.getKey()));
									} else {  
										logger.debug("Additional potential match has accepted key of previous match, skipping.");
										result.addComment("Match has accepted key of a previous match, treating as duplicate.");
									}
								} else { 
									hasMatch=true;
									matchCounter++;
									kvp.put("dwc:scientificNameID", match.getGuid());
								}
							}
						} else { 
							if (! SciNameUtils.isEqualOrNonEmptyES(taxon.getKingdom(), match.getKingdom())) { 
								result.addComment("Mismatch in Kingdom [" + taxon.getKingdom() + "] authority has [" + match.getKingdom() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getPhylum(), match.getPhylum()))) { 
								result.addComment("Mismatch in Phylum [" + taxon.getPhylum() + "] authority has [" + match.getPhylum() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getTaxonomic_class(), match.getClazz()))) { 
								result.addComment("Mismatch in Class [" + taxon.getTaxonomic_class() + "] authority has [" + match.getClazz() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getOrder(), match.getOrder()))) { 
								result.addComment("Mismatch in Order [" + taxon.getOrder() + "] authority has [" + match.getOrder() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getSuperfamily(), match.getSuperfamily()))) { 
								result.addComment("Mismatch in Superfamily [" + taxon.getSuperfamily() + "] authority has [" + match.getSuperfamily() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getFamily(), match.getFamily()))) { 
								result.addComment("Mismatch in Family [" + taxon.getFamily() + "] authority has [" + match.getFamily() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getTribe(), match.getTribe()))) { 
								result.addComment("Mismatch in Tribe [" + taxon.getTribe() + "] authority has [" + match.getTribe() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getSubtribe(), match.getSubtribe()))) { 
								result.addComment("Mismatch in Subtribe [" + taxon.getSubtribe() + "] authority has [" + match.getSubtribe() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmptyES(taxon.getGenus(), match.getGenus()))) { 
								result.addComment("Mismatch in Genus [" + taxon.getGenus() + "] authority has [" + match.getGenus() +"]");
							}
							if (!(SciNameUtils.isEqualOrNonEmpty(lookMeUp, match.getCanonicalName()))) { 
								result.addComment("Mismatch in Name [" + lookMeUp + "] authority has [" + match.getCanonicalName() +"]");
							}
						}
					}
					if (hasMatch) {
						if (matchCounter>1) { 
							result.addComment("More than one exact match found for provided taxon in " + sourceAuthority.getName() + ".");
							result.setResultState(ResultState.NOT_AMENDED);
							// result.setResultState(ResultState.AMBIGUOUS);  Discussed in TG2 call 2022 Jan 30, use NOT_AMENDED and discuss ambiguity in comments.
							// separate examination of ambiguity into a separate validation, rather than asserting as a result.
						} else { 
							logger.debug(kvp.get("dwc:scientificNameID"));
							if (kvp.get("dwc:scientificNameID").equals(taxon.getScientificNameID())) { 
								result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ", matching the current value of dwc:taxonID");
								result.setResultState(ResultState.NOT_AMENDED);
							} else  {
								if (replaceExisting) { 
									logger.debug("Non-standard behavior, may overwrite value and assert AMENDED");
									// **********
									// Non-standard behaviour, allows replacement of existing value
									result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ".");
									AmendmentValue ammendedTaxonID = new AmendmentValue(kvp);
									result.setValue(ammendedTaxonID);
									if (SciNameUtils.isEmpty(taxon.getScientificNameID())) { 
										result.setResultState(ResultState.FILLED_IN);
									} else { 
										result.addComment("Existing value changed to conform to the specified sourceAuthority " + sourceAuthority.getName());
										result.setResultState(ResultState.AMENDED);
									}
									//  ************
								} else { 
									logger.debug("Standard behavior, only asserts FILLED_IN");
									// Specification states that amendment is to be provided only for an empty taxonID
									// test for empty dwc:taxonID provided in outermost if statement.
									result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ".");
									AmendmentValue ammendedTaxonID = new AmendmentValue(kvp);
									result.setValue(ammendedTaxonID);
									result.setResultState(ResultState.FILLED_IN);
								}
							} 
						}
					} else { 
						result.addComment("No exact match found for provided taxon in " + sourceAuthority.getName() + ".");
						result.setResultState(ResultState.NOT_AMENDED);
					}
				} else { 
					result.addComment("No match found for provided taxon in " + sourceAuthority.getName() + ".");
					result.setResultState(ResultState.NOT_AMENDED);
				}
			} catch (IOException e) {
				result.addComment(sourceAuthority.getName() + " API not available:" + e.getMessage());
				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
			} catch (UnsupportedSourceAuthorityException e) { 
				result.addComment("Unable to process:" + e.getMessage());
				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
			} catch (ApiException e) {
				result.addComment(sourceAuthority.getName() + " API invocation error:" + e.getMessage());
				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
			} catch (org.irmng.aphia.v1_0.handler.ApiException e) {
				result.addComment(sourceAuthority.getName() + " IRMNG API invocation error:" + e.getMessage());
				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
			}
		}

		return result;
	}

	
    /**
     *
     * Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?
     *
     * Provides: #70 VALIDATION_TAXON_UNAMBIGUOUS
     * Version: 2023-09-18
     *
     * @param taxonomic_class the provided dwc:class to evaluate
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
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @param sourceAuthorityString the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
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
    		@ActedUpon("dwc:subtribe") String subtribe,
    		@Parameter(name="bdq:sourceAuthority") String sourceAuthorityString
		){
		return validationTaxonUnambiguous(new Taxon(taxonID, kingdom, phylum, taxonomic_class, order, family, subfamily,
				genus, subgenus, scientificName, scientificNameAuthorship, genericName, specificEpithet,
				infraspecificEpithet, taxonRank, cultivarEpithet, higherClassification, vernacularName, taxonConceptID,
				scientificNameID, originalNameUsageID, acceptedNameUsageID, superfamily, tribe, subtribe), sourceAuthorityString);
    }
    
    
    /**
     * Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?
     * Utility method using a Taxon object as input instead of a separate annotated
     * parameters for each taxon term.
     *
     * Provides: #70 VALIDATION_TAXON_UNAMBIGUOUS
     *
     * @param taxon a dwc:Taxon object to evaluate.
     * @return DQResponse the response of type ComplianceValue  to return
     * @param sourceAuthorityString a {@link java.lang.String} object.
     */
    public static DQResponse<ComplianceValue> validationTaxonUnambiguous(
    		Taxon taxon, 
			String sourceAuthorityString
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if all 
        // of dwc:scientificNameID, dwc:scientificName, dwc:genericName, 
        // dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
        // dwc:cultivarEpithet are EMPTY; COMPLIANT if (1) dwc:scientificNameID 
        // references a single taxon record in the bdq:sourceAuthority, 
        // or (2) dwc:scientificNameID is empty and dwc:scientificName 
        // references a single taxon record in the bdq:sourceAuthority, 
        // or (3) if dwc:scientificName and dwc:scientificNameID are 
        // EMPTY and if a combination of the values of the terms dwc:genericName, 
        // dwc:specificEpithet, dwc:infraspecificEpithet, dwc:cultivarEpithet, 
        // dwc:taxonRank, and dwc:scientificNameAuthorship can be unambiguously 
        // resolved to a unique taxon in the bdq:sourceAuthority, or 
        // (4) if ambiguity produced by multiple matches in (2) or 
        // (3) can be disambiguated to a unique Taxon using the values 
        // of dwc:tribe, dwc:subtribe, dwc:subgenus, dwc:genus, dwc:subfamily, 
        // dwc:family, dwc:superfamily, dwc:order, dwc:class, dwc:phylum, 
        // dwc:kingdom, dwc:higherClassification, dwc:taxonID, dwc:acceptedNameUsageID, 
        // dwc:originalNameUsageID, dwc:taxonConceptID and dwc:vernacularName; 
        // otherwise NOT_COMPLIANT 
        // 
        
        // TODO: Specification changed in disambiguation clause, disambiguate on taxonID (and other ID terms).
        
        // TODO: dwc:cultivarEpithet not handled yet
        // dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, 
        // dwc:taxonConceptID and dwc:vernacularName
        
        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" {[https://doi.org/10.15468/39omei]} 
        // {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 

        if (SciNameUtils.isEmpty(taxon.getScientificNameID()) &&
        		SciNameUtils.isEmpty(taxon.getScientificName()) &&
        		SciNameUtils.isEmpty(taxon.getGenericName()) &&
        		SciNameUtils.isEmpty(taxon.getSpecificEpithet()) &&
        		SciNameUtils.isEmpty(taxon.getInfraspecificEpithet()) &&
        		SciNameUtils.isEmpty(taxon.getScientificNameAuthorship()) &&
        		SciNameUtils.isEmpty(taxon.getCultivarEpithet())
        		) 
        { 
        	// INTERNAL_PREREQUISITES_NOT_MET if all 
        	// of dwc:scientificNameID, dwc:scientificName, dwc:genericName, 
        	// dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
        	// dwc:cultivarEpithet are EMPTY; 
        	result.addComment("none of dwc:scientificNameID, dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, or dwc:cultivarEpithet contain a value.");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	SciNameSourceAuthority sourceAuthority = null;
        	if (SciNameUtils.isEmpty(sourceAuthorityString)) { 
        		try {
        			sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
        		} catch (SourceAuthorityException e) {
        			logger.error(e.getMessage(),e);
        			sourceAuthority = new SciNameSourceAuthority();
        		}
        	} else {
        		try {
        			sourceAuthority = new SciNameSourceAuthority(sourceAuthorityString);
        			// TDOO: Implement https://biodiversity.org.au/nsl/services/rest/name/apni/
        			// Invoking: https://biodiversity.org.au/nsl/services/api/name/taxon-search.json?q=Solanum%20tuberosum       			
        			
        		} catch (SourceAuthorityException e) {
        			logger.debug(e.getMessage());
        			result.addComment("Unsupported Source Authority: " + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}
        	}
        	if (sourceAuthority!=null) { 
        		try {
        			boolean completed = false;
        			logger.debug(taxon.getScientificNameID());
        			if (!SciNameUtils.isEmpty(taxon.getScientificNameID())) { 
        				try {
        					if( SciNameUtils.validateTaxonID(taxon.getScientificNameID(), sourceAuthority)) { 
        						// COMPLIANT if (1) dwc:scientificNameID 
        						// references a single taxon record in the bdq:sourceAuthority,
        						// NOTE: Internal consistency with other terms not checked in this test.
        						result.addComment("Exact match to provided scientificNameId found in " + sourceAuthority.getName() + ", matching the provided value of dwc:scientificNameID");
        						result.setValue(ComplianceValue.COMPLIANT);
        						result.setResultState(ResultState.RUN_HAS_RESULT);
        						completed = true;
        					} else { 
        						result.addComment("Provided scientificNameID not found in " + sourceAuthority.getName() + ".");
        						result.setValue(ComplianceValue.NOT_COMPLIANT);
        						result.setResultState(ResultState.RUN_HAS_RESULT);
        						completed = true;
        					}
        				} catch (IDFormatException e) {
        					result.addComment("Provided scientificNameID ["+taxon.getScientificNameID()+"] not found in " + sourceAuthority.getName() + ", format does not conform to expectations for that source: " + e.getMessage());
        					result.setValue(ComplianceValue.NOT_COMPLIANT);
        					result.setResultState(ResultState.RUN_HAS_RESULT);
        					completed = true;
        				}
        			}

        			if (!completed) { 
        				String lookMeUp = taxon.getScientificName();
        				if (SciNameUtils.isEmpty(lookMeUp)) { 
        					lookMeUp = taxon.getGenericName();
        					if (!SciNameUtils.isEmpty(taxon.getSpecificEpithet())) { 
        						lookMeUp = lookMeUp + " " + taxon.getSpecificEpithet();
        					}
        					if (!SciNameUtils.isEmpty(taxon.getInfraspecificEpithet())) { 
        						lookMeUp = lookMeUp + " " + taxon.getInfraspecificEpithet();
        					}
        					if (!SciNameUtils.isEmpty(taxon.getCultivarEpithet())) { 
        						lookMeUp = lookMeUp + " " + taxon.getCultivarEpithet();
        					}
        				}
        				logger.debug(lookMeUp);
        				if (!SciNameUtils.isEmpty(taxon.getScientificNameAuthorship())) { 
        					if (lookMeUp.endsWith(taxon.getScientificNameAuthorship())) { 
        						lookMeUp = lookMeUp.substring(0, lookMeUp.lastIndexOf(taxon.getScientificNameAuthorship())).trim();
        					}
        				}
        				logger.debug(lookMeUp);

        				result.addComment("Provided taxon ["+taxon.toString()+"]");
        				List<NameUsage> matchList = null;
        				if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY)) { 
        					String matches = GBIFService.searchForTaxon(lookMeUp, GBIFService.KEY_GBIFBACKBONE);
        					logger.debug(matches);
        					matchList = GBIFService.parseAllNameUsagesFromJSON(matches);
        				} else if (sourceAuthority.isGBIFChecklist()) { 
        					String matches = GBIFService.searchForTaxon(lookMeUp, sourceAuthority.getAuthoritySubDataset());
        					logger.debug(matches);
        					matchList = GBIFService.parseAllNameUsagesFromJSON(matches);					
        				} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
        					matchList = WoRMSService.lookupTaxon(lookMeUp, taxon.getScientificNameAuthorship());
        				} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) {
        					matchList = IRMNGService.lookupTaxon(lookMeUp, taxon.getScientificNameAuthorship());        						
        				} else { 
        					throw new UnsupportedSourceAuthorityException("Source Authority Not Implemented");
        				} 
        				logger.debug(matchList.size());
        				if (matchList.size()>0) {
        					// Cases 2, 3, and 4, lookup, need to disambiguate if more than 1 match (case 4).
        					boolean hasMatch = false;
        					int matchCounter = 0;
        					Integer matchedKey = null; 
        					Map<String,String> kvp = new HashMap<String,String>();
        					Iterator<NameUsage> i = matchList.iterator();
        					while (i.hasNext()) { 
        						NameUsage match = i.next();
        						logger.debug(match.getCanonicalName());
        						logger.debug(match.getTaxonomicStatus());
        						logger.debug(match.getAcceptedKey());
        						if ( taxon.sameHigherAs(match) ) { 
        							logger.debug(match.getCanonicalName());
        							logger.debug(match.getAuthorship());
        							logger.debug(match.getAuthorshipStringSimilarity());
        							boolean authorshipOK = false;
        							if (!SciNameUtils.isEmpty(taxon.getScientificNameAuthorship())) { 
        								if (match.getAuthorComparator()==null) { 
        									match.setAuthorComparator(AuthorNameComparator.authorNameComparatorFactory(taxon.getScientificNameAuthorship(), taxon.getKingdom()));
        								}
        								logger.debug(match.getAuthorComparator().compare(taxon.getScientificNameAuthorship(), match.getAuthorship()).getMatchType());
        								NameComparison authorshipComparison = match.getAuthorComparator().compare(taxon.getScientificNameAuthorship(), match.getAuthorship());
        								if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACT)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with exact match on authorship. ");
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACT_BRACKETS)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with exact match (except for square brackets) on authorship.");
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_DIFFERANDEXACTYEAR)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with exact match (except for use of and/amperstand) on authorship.");
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACTADDSYEAR)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACTMISSINGYEAR)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_SOWERBYEXACTYEAR)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_L_EXACTYEAR)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_SAMEBUTABBREVIATED)) {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with similar author: " + authorshipComparison.getRemark());
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_L_EXACTYEAR) && taxon.getKingdom()=="Animalia") {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " with ambiguous L., but same year: " + authorshipComparison.getRemark());
        									authorshipOK = true;
        								} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_L) && taxon.getKingdom()=="Animalia") {
        									result.addComment("Match for provided (animal) taxon in " + sourceAuthority.getName() + " with ambiguous L.: " + authorshipComparison.getRemark());
        									authorshipOK = true;
        								} else {
        									result.addComment("Match for provided taxon in " + sourceAuthority.getName() + " has different author: " + authorshipComparison.getRemark());
        									authorshipOK = false;
        								}
        							} else { 
        								authorshipOK = true; // no basis to compare, assume ok.
        							}
        							logger.debug(match.getGuid());
        							if (!SciNameUtils.isEmpty(match.getGuid()) && authorshipOK) {
        								if (taxon.sameHigherAs(match)) { 
        									// Case (4) disambuguate matches by higher taxonomy.
        									if (hasMatch) { 
        										logger.debug(match.getAuthorship());
        										logger.debug(match.getAuthorComparator());
        										NameComparison authorshipComparison = match.getAuthorComparator().compare(taxon.getScientificNameAuthorship(), match.getAuthorship());
        										if (match.getAcceptedKey()==matchedKey && 
        												match.getTaxonomicStatus().equals("SYNONYM") && (
        														authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACT) ||
        														authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACTADDSYEAR) || 
        														authorshipComparison.getMatchType().equals(NameComparison.MATCH_L_EXACTYEAR)
        														)
        												) 
        										{ 
        											// already exact match and this is an identical synonym entry
        										} else { 
        											hasMatch=true;
        											matchedKey = match.getKey();
        											matchCounter++;
        											kvp.put("dwc:taxonID", match.getGuid());
        										}
        									} else { 
        										hasMatch=true;
        										matchedKey = match.getKey();
        										matchCounter++;
        										kvp.put("dwc:taxonID", match.getGuid());
        									} 
        								} else {
        									// TODO: Disambiguate on dwc:taxonID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, dwc:taxonConceptID and dwc:vernacularName;
        									logger.debug("Higher taxonomy not matched, excluding " + match.getKey());
        								}
        							}
        						}
        					}
        					if (hasMatch) {
        						if (matchCounter>1) { 
        							// Encountered case (4), ambiguity exists, but unable to resolve 
        							result.addComment("More than one exact match found for provided taxon in " + sourceAuthority.getName() + ".");
        							result.setValue(ComplianceValue.NOT_COMPLIANT);
        							result.setResultState(ResultState.RUN_HAS_RESULT);
        						} else { 
        							// single match
        							//  COMPLIANT if (2) dwc:scientificNameID is empty and dwc:scientificName 
        							// references a single taxon record in the bdq:sourceAuthority,
        							if (kvp.get("dwc:taxonID").equals(taxon.getTaxonID())) { 
        								result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ", matching the current value of dwc:taxonID");
        								result.setValue(ComplianceValue.COMPLIANT);
        								result.setResultState(ResultState.RUN_HAS_RESULT);
        							} else if (SciNameUtils.isEmpty(taxon.getTaxonID())) { 
        								result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ", and provided value of dwc:taxonID is empty.");
        								result.setValue(ComplianceValue.COMPLIANT);
        								result.setResultState(ResultState.RUN_HAS_RESULT);
//        							} else {
//        								// Consistency not in specification
//        								result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ", but returned taxonID ["+kvp.get("dwc:taxonID")+"] is not equal to provided taxonID ["+taxon.getTaxonID()+"].");
//        								result.setValue(ComplianceValue.NOT_COMPLIANT);
//        								result.setResultState(ResultState.RUN_HAS_RESULT);
        							} 
        						}
        					} else { 
        						result.addComment("No exact match found for provided taxon in " + sourceAuthority.getName() + ".");
        						result.setValue(ComplianceValue.NOT_COMPLIANT);
        						result.setResultState(ResultState.RUN_HAS_RESULT);
        					}
        				} else { 
        					result.addComment("No match found for provided taxon in " + sourceAuthority.getName() + ".");
        					result.setValue(ComplianceValue.NOT_COMPLIANT);
        					result.setResultState(ResultState.RUN_HAS_RESULT);
        				}
        			}
        		} catch (IOException e) {
        			// EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        			// is not available;  
        			result.addComment(sourceAuthority.getName() + " API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (UnsupportedSourceAuthorityException e) { 
        			result.addComment("Unable to process:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (ApiException e) {
        			logger.debug(e.getMessage(),e);
        			result.addComment(sourceAuthority.getName() + " API invocation error:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (org.irmng.aphia.v1_0.handler.ApiException e) {
        			logger.debug(e.getMessage(),e);
					e.printStackTrace();
        			result.addComment("Error accessing " + sourceAuthority.getName() + ": " + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
				}
        	}
        }
        
        return result;
    }
    
    /**
     * Propose an amendment to the value of dwc:scientificName using the dwc:scientificNameID value from bdq:sourceAuthority.
     *
     * Provides: #71 AMENDMENT_SCIENTIFICNAME_FROM_SCIENTIFICNAMEID
     * Version: 2022-12-13
     *
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_SCIENTIFICNAME_FROM_SCIENTIFICNAMEID", description="Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority.")
    @Provides("f01fb3f9-2f7e-418b-9f51-adf50f202aea")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/f01fb3f9-2f7e-418b-9f51-adf50f202aea/2022-12-13")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID is EMPTY, the value of dwc:scientificNameID is ambiguous or dwc:scientificName was not EMPTY; FILLED_IN the value of dwc:scientificName if the value of scientificNameID could be unambiguously interpreted as a value in bdq:sourceAuthority; otherwise NOT_AMENDED. bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public static DQResponse<AmendmentValue> amendmentScientificnameFromScientificnameid(
    		@Consulted("dwc:scientificNameID") String scientificNameID, 
    		@ActedUpon("dwc:scientificName") String scientificName,
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
    	) {
    	
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; 
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID is EMPTY, 
        // the value of dwc:scientificNameID is ambiguous or dwc:scientificName was 
        // not EMPTY; FILLED_IN the value of dwc:scientificName if the value of 
        // scientificNameID could be unambiguously interpreted as a value in 
        // bdq:sourceAuthority; otherwise NOT_AMENDED

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        
        // Notes:
		// When referencing a GBIF taxon by
		// GBIF's identifier for that taxon, use the the pseudo-namespace "gbif:" and
		// the form "gbif:{integer}" as the value for dwc:scientificNameID. Implementors can be
		// aware of the current GBIF api endpoint that can replace the pseduo-namespace
		// gbif: when looking up the dwc:scientificNameID, e.g.
		// s/gbif:/https:\/\/api.gbif.org\/v1\/species\// will transform the value
		// dwc:scientificNameID=gbif:8102122 to the resolvable endpoint
		// https://api.gbif.org/v1/species/8102122 The pseudo-namespace gbif: is
		// recommeded by GBIF for use in scientificNameID to reference GBIF taxon records. Where
		// resolvable persistent identifiers exist for taxonID values, they should be
		// used in full, but implementors will need to support at least the gbif:
		// pseudo-namespace.

        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        if (SciNameUtils.isEmpty(scientificNameID)) { 
			result.addComment("dwc:scientificNameID does not contains a value.");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else if (!SciNameUtils.isEmpty(scientificName)) { 
			result.addComment("dwc:scientificName already contains a value ["+ scientificName +"].");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else if (scientificNameID.matches("^[0-9]+$")) { 
			result.addComment("dwc:scientificNameID is a bare integer and thus inherently ambiguous.");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	// GBIF recommends the pseudo-namespace gbif with taxonID values in the form "gbif:{integer}"
        	// But https://www.gbif.org/species/{integer}, http and api references might also be used.
        	if (sourceAuthority.getName().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY.getName())) { 
        		if (scientificNameID.startsWith("https://www.gbif.org/species/") ||
        			 scientificNameID.startsWith("http://www.gbif.org/species/") ||
        			 scientificNameID.startsWith("https://api.gbif.org/v1/species/") ||
        			 scientificNameID.startsWith("http://api.gbif.org/v1/species/") ||
        			 scientificNameID.startsWith("gbif:") ||
        			 scientificNameID.startsWith("GBIF:")
        		) { 
        			String id = scientificNameID.replaceFirst("http[s]{0,1}://[wapi]{3}\\.gbif\\.org/[v1/]{0,3}species/", "gbif:");
        			id = id.replace("gbif:", "");  // preferred form
        			id = id.replace("GBIF:", "");  // possible case variation
        			logger.debug(id);
        			if (id.matches("^[0-9]+$")) { 
        				try {
							String sciNameLookup = GBIFService.lookupScientificNameByID(id);
							if (!SciNameUtils.isEmpty(sciNameLookup)) { 
								result.setResultState(ResultState.FILLED_IN);
								Map<String,String> amend = new HashMap<String,String>();
								amend.put("dwc:scientificName", sciNameLookup);
 								result.setValue(new AmendmentValue(amend));
							} else { 
								result.setResultState(ResultState.NOT_AMENDED);
								result.addComment("Unable to find scientificName for provided scientificNameID in " + sourceAuthority.getName());
							}
						} catch (IOException e) {
							result.addComment("Error looking up scientific name in sourceAuthority: "+ e.getMessage() );
							result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
						}
        			} else { 
						result.addComment("dwc:scientificNameID not interpretable as an identifier for " + sourceAuthority.getName() +" ["+ scientificNameID +"].");
						result.setResultState(ResultState.NOT_AMENDED);
        			}
        		} else { 
					result.addComment("dwc:scientificNameID not interpretable as an identifier in " + sourceAuthority.getName() +" ["+ scientificNameID +"].");
					result.setResultState(ResultState.NOT_AMENDED);
        		}
        	} else if (sourceAuthority.getName().equals(EnumSciNameSourceAuthority.WORMS.getName())) {
        		// WoRMS guid is the LSID.
        		if (scientificNameID.startsWith("urn:lsid:marinespecies.org:taxname:") ) { 
        			String id = scientificNameID.replaceFirst("urn:lsid:marinespecies.org:taxname:", "");
        			logger.debug(id);
        			if (id.matches("^[0-9]+$")) { 
        				try {
							NameUsage nameUsage = WoRMSService.lookupTaxonByID(id);
							if (SciNameUtils.isEmpty(nameUsage.getScientificName())) { 
								result.setResultState(ResultState.NOT_AMENDED);
								result.addComment("Unable to find scientificName for provided scientificNameID in " + sourceAuthority.getName());
							} else { 
								if (nameUsage.getGuid().equals(scientificNameID)) { 
									result.setResultState(ResultState.FILLED_IN);
									Map<String,String> amend = new HashMap<String,String>();
									amend.put("dwc:scientificName", nameUsage.getScientificName());
									result.setValue(new AmendmentValue(amend));
								} else { 
									result.setResultState(ResultState.NOT_AMENDED);
									result.addComment("Query on scientificNameID ["+scientificNameID+"] in " + sourceAuthority.getName() + " returned a different scientificNameID ["+ nameUsage.getGuid() +"]");
								}
							}
 						} catch (IDFormatException e) {
							logger.error(e.getMessage(),e);
							result.addComment("Error extracting the integer AphiaID from provided scientificNameID ["+ scientificNameID +"].");
							result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
						} catch (ApiException ex) {
							logger.debug(ex.getMessage());
							result.addComment("Error looking up scientific name in sourceAuthority: "+ ex.getMessage() );
							result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
						}
        			} else { 
        				result.addComment("Unable to extract the integer AphiaID from provided scientificNameID ["+ scientificNameID +"].");
        				result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        			}
        		} else { 
					result.addComment("dwc:scientificNameID not interpretable as an identifier in " + sourceAuthority.getName() +" ["+ scientificNameID +"].");
					result.setResultState(ResultState.NOT_AMENDED);
        		}
        	} else if (sourceAuthority.getName().equals(EnumSciNameSourceAuthority.IRMNG.getName())) {
        		// IRMNG guid is the LSID.
        		if (scientificNameID.startsWith("urn:lsid:irmng.org:taxname:") ) { 
        			String id = scientificNameID.replaceFirst("urn:lsid:irmng.org:taxname:", "");
        			logger.debug(id);
        			if (id.matches("^[0-9]+$")) { 
        				try {
							NameUsage nameUsage = IRMNGService.lookupTaxonByID(id);
							if (SciNameUtils.isEmpty(nameUsage.getScientificName())) { 
								result.setResultState(ResultState.NOT_AMENDED);
								result.addComment("Unable to find scientificName for provided scientificNameID in " + sourceAuthority.getName());
							} else { 
								if (nameUsage.getGuid().equals(scientificNameID)) { 
									result.setResultState(ResultState.FILLED_IN);
									Map<String,String> amend = new HashMap<String,String>();
									amend.put("dwc:scientificName", nameUsage.getScientificName());
									result.setValue(new AmendmentValue(amend));
								} else { 
									result.setResultState(ResultState.NOT_AMENDED);
									result.addComment("Query on scientificNameID ["+scientificNameID+"] in " + sourceAuthority.getName() + " returned a different scientificNameID ["+ nameUsage.getGuid() +"]");
								}
							}
 						} catch (IDFormatException e) {
							logger.error(e.getMessage(),e);
							result.addComment("Error extracting the integer AphiaID from provided scientificNameID ["+ scientificNameID +"].");
							result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
						} catch (org.irmng.aphia.v1_0.handler.ApiException ex) {
							logger.debug(ex.getMessage());
							result.addComment("Error looking up scientific name in sourceAuthority: "+ ex.getMessage() );
							result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
						}
        			} else { 
        				result.addComment("Unable to extract the integer AphiaID from provided scientificNameID ["+ scientificNameID +"].");
        				result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        			}
        		} else { 
					result.addComment("dwc:scientificNameID not interpretable as an identifier in " + sourceAuthority.getName() +" ["+ scientificNameID +"].");
					result.setResultState(ResultState.NOT_AMENDED);
        		}
        	} else { 
				result.addComment("Unsupported Source Authority: " + sourceAuthority.getName() +".");
				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	}
        	
        }

        return result;
        
    }

    
    /**
     * Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?
     *
     * Provides: #77 VALIDATION_CLASS_FOUND
     * Version: 2023-09-18
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASS_FOUND", description="Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?")
    @Provides("2cd6884e-3d14-4476-94f7-1191cfff309b")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2cd6884e-3d14-4476-94f7-1191cfff309b/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:class is EMPTY; COMPLIANT if the value of dwc:class was found as a value at the rank of Class in the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationClassFound(
    		@ActedUpon("dwc:class") String taxonomic_class, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:class 
        // is EMPTY; COMPLIANT if the value of dwc:class was found 
        // as a value at the rank of Class in the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" {[https://doi.org/10.15468/39omei]} {API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"

        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        return validateHigherTaxonAtRank(taxonomic_class, "Class", sourceAuthority);
    }

    /**
     * Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?
     *
     * Provides: #81 VALIDATION_KINGDOM_FOUND
     * Version: 2023-09-18
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_KINGDOM_FOUND", description="Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?")
    @Provides("125b5493-052d-4a0d-a3e1-ed5bf792689e")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/125b5493-052d-4a0d-a3e1-ed5bf792689e/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:kingdom is EMPTY; COMPLIANT if the value of dwc:kingdom was found as a value at the rank of kingdom by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationKingdomFound(@ActedUpon("dwc:kingdom") String kingdom, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:kingdom 
        // is EMPTY; COMPLIANT if the value of dwc:kingdom was found 
        // as a value at the rank of kingdom by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
        // {[https://doi.org/10.15468/39omei]} {API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 

        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        return validateHigherTaxonAtRank(kingdom, "Kingdom", sourceAuthority);
    }

    /**
     * Is there a value in dwc:scientificName?
     *
     * Provides: #82 VALIDATION_SCIENTIFICNAME_NOTEMPTY
     * Version: 2023-09-18
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_NOTEMPTY", description="Is there a value in dwc:scientificName?")
    @Provides("7c4b9498-a8d9-4ebb-85f1-9f200c788595")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/7c4b9498-a8d9-4ebb-85f1-9f200c788595/2023-09-18")
    @Specification("COMPLIANT if dwc:scientificName is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationScientificnameNotempty(@ActedUpon("dwc:scientificName") String scientificName) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // COMPLIANT if dwc:scientificName is not EMPTY; otherwise 
        // NOT_COMPLIANT 

		if (SciNameUtils.isEmpty(scientificName)) {
			result.addComment("No value provided for scientificName.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for scientificName.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        return result;
        
    }

    /**
     * Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?
     *
     * Provides: #83 VALIDATION_ORDER_FOUND
     * Version: 2023-09-18
     *
     * @param order the provided dwc:order to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse tyyhe response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_ORDER_FOUND", description="Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?")
    @Provides("81cc974d-43cc-4c0f-a5e0-afa23b455aa3")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/81cc974d-43cc-4c0f-a5e0-afa23b455aa3/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:order is EMPTY; COMPLIANT if the value of dwc:order was found as a value at the rank of Order by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationOrderFound(@ActedUpon("dwc:order") String order,  
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:order 
        // is EMPTY; COMPLIANT if the value of dwc:order was found 
        // as a value at the rank of Order by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
        // {[https://doi.org/10.15468/39omei]} {API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 
        
        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        if (sourceAuthority==null) { 
        	sourceAuthority = new SciNameSourceAuthority();
        }

        if (SciNameUtils.isEmpty(order)) { 
        	result.addComment("No value provided for order.");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY)) { 
        		try {
        			List<NameUsage> matches = GBIFService.lookupTaxonAtRank(order, GBIFService.KEY_GBIFBACKBONE, "Order", 100);
        			logger.debug(matches.size());
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided order found in GBIF backbone taxonomy as an order.");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided order found in GBIF backbone taxonomy as an order.");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}
        	} else if (sourceAuthority.isGBIFChecklist()) { 
        		try {
        			List<NameUsage> matches = GBIFService.lookupTaxonAtRank(order, sourceAuthority.getAuthoritySubDataset(), "Order", 100);
        			logger.debug(matches.size());
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided order found in " + sourceAuthority.getAuthority().getName() + " as an order.");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided order found  " + sourceAuthority.getAuthority().getName() + "  as an order.");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}        		
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
        		try {
        			List<NameUsage> matches = WoRMSService.lookupTaxonAtRank(order,"Order");
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided order found in WoRMS as an order.");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided order found in WoRMS as an order.");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (ApiException e) {
        			result.addComment("WoRMS aphia API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (Exception e) {
        			result.addComment("Error using WoRMS aphia API:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        			logger.error(e.getMessage(),e);
        		}
        	} else { 
        		result.addComment("Source Authority Not Implemented.");
        		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	} 
        }
		
        return result;
    }

    /**
     * Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?
     *
     * Provides: #101 VALIDATION_POLYNOMIAL_CONSISTENT
     * Version: 2022-09-18
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_POLYNOMIAL_CONSISTENT", description="Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?")
    @Provides("17f03f1f-f74d-40c0-8071-2927cfc9487b")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/17f03f1f-f74d-40c0-8071-2927cfc9487b/2022-09-18")
    @Specification("INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is EMPTY, or all of dwc:genericName, dwc:specificEpithet and dwc:infraspecificEpithet are EMPTY; COMPLIANT if the polynomial, as represented in dwc:scientificName, is consistent with NOT_EMPTY values of dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet; otherwise NOT_COMPLIANT. ")
    public static DQResponse<ComplianceValue> validationPolynomialConsistent(
    		@ActedUpon("dwc:scientificName") String scientificName, 
    		@ActedUpon("dwc:genericName") String genericName, 
    		@ActedUpon("dwc:specificEpithet") String specificEpithet,
    		@ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet 
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is 
        // EMPTY, or all of dwc:genericName, dwc:specificEpithet and 
        // dwc:infraspecificEpithet are EMPTY; COMPLIANT if the polynomial, 
        // as represented in dwc:scientificName, is consistent with 
        // NOT_EMPTY values of dwc:genericName, dwc:specificEpithet, 
        // dwc:infraspecificEpithet; otherwise NOT_COMPLIANT. 
        
        if (SciNameUtils.isEmpty(scientificName)) { 
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        	result.addComment("No value was provided for dwc:scientificName.");
        } else if (SciNameUtils.isEmpty(genericName) && SciNameUtils.isEmpty(specificEpithet) && SciNameUtils.isEmpty(infraspecificEpithet)) { 
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        	result.addComment("No value was provided for any of dwc:genericName, dwc:specificEpithet, or dwc:infraspecificEpithet.");
        } else { 
        	String sciNameTrailingSpace = scientificName+" ";
        	if (genericName==null) { genericName = ""; }
        	if (specificEpithet==null) { specificEpithet = ""; }
        	if (infraspecificEpithet==null) { infraspecificEpithet = ""; }
        	if (scientificName.equals(genericName+" "+specificEpithet+" "+infraspecificEpithet)) { 
        		// simple trinomial match
   				result.addComment("Exact match of scientificName to genericName+specificEpithet+infraspecificEpithet.");
  				result.setValue(ComplianceValue.COMPLIANT);
  				result.setResultState(ResultState.RUN_HAS_RESULT);
        	} else if (scientificName.equals(genericName) && SciNameUtils.isEmpty(specificEpithet) && SciNameUtils.isEmpty(infraspecificEpithet) ) { 
        		// simple uninomial match
   				result.addComment("Exact match of scientificName to genericName.");
  				result.setValue(ComplianceValue.COMPLIANT);
  				result.setResultState(ResultState.RUN_HAS_RESULT);
        	} else if (SciNameUtils.isEmpty(infraspecificEpithet) && scientificName.equals(genericName+" "+specificEpithet)) { 
        		// simple binomial match
   				result.addComment("Exact match of scientificName to genericName+specificEpithet.");
  				result.setValue(ComplianceValue.COMPLIANT);
  				result.setResultState(ResultState.RUN_HAS_RESULT);
        	} else if (!SciNameUtils.isEmpty(genericName) &&  !sciNameTrailingSpace.startsWith(genericName+" ")) { 
        		// failure case, doesn't start with generic name
   				result.addComment("Provided dwc:scientificName does not start with genericName");
  				result.setValue(ComplianceValue.NOT_COMPLIANT);
  				result.setResultState(ResultState.RUN_HAS_RESULT);
        	} else if (!SciNameUtils.isEmpty(specificEpithet) &&
        			scientificName.matches("^[A-Z][a-z]+ [a-z]+( [a-z]){0,1}$") &&
        			!sciNameTrailingSpace.matches("^[A-Z][a-z]+ " + specificEpithet + " ([a-z]+ ){0,1}$")) { 
        		// failure case, two or three words, doesn't contain the specificEpithet as second word
   				result.addComment("Provided dwc:scientificName does not contain the specificEpithet [" + specificEpithet + "]");
  				result.setValue(ComplianceValue.NOT_COMPLIANT);
  				result.setResultState(ResultState.RUN_HAS_RESULT);
        	} else if (!SciNameUtils.isEmpty(infraspecificEpithet) && 
        			scientificName.matches("^[A-Z][a-z]+ [a-z]+ [a-z]$") &&
        			!sciNameTrailingSpace.matches("^[A-Z][a-z]+ [a-z]+ " + infraspecificEpithet + "$")) { 
        		// failure case, three words, doesn't contain the infraspecificEpithet as second word
   				result.addComment("Provided dwc:scientificName does not contain the specificEpithet [" + specificEpithet + "]");
  				result.setValue(ComplianceValue.NOT_COMPLIANT);
  				result.setResultState(ResultState.RUN_HAS_RESULT);
        	} else { 
	    		NameParser nameParser = new NameParserGBIF();
	    		try {
	    			ParsedName parse = nameParser.parse(scientificName,Rank.UNRANKED, null);
	    			logger.debug(parse.toString());
	    			String parseGeneric = parse.getGenus();
	    			if (parseGeneric==null) { parseGeneric = ""; }
	    			String parseSpecific = parse.getSpecificEpithet();
	    			if (parseSpecific==null) { parseSpecific = ""; }
	    			String parseInfraspecific = parse.getInfraspecificEpithet();
	    			if (parseInfraspecific==null) { parseInfraspecific = ""; }
	    			logger.debug(parseGeneric);
	    			logger.debug(parseSpecific);
	    			logger.debug(parseInfraspecific);
	    			logger.debug(parse.getRank());
	    			String uninomial = parse.getUninomial();
	    			boolean done = false;
	    			if (parse.getRank().equals(Rank.UNRANKED) && !SciNameUtils.isEmpty(uninomial)) { 
	    				logger.debug(uninomial);
	    				if (uninomial.equals(scientificName) && uninomial.equals(genericName) && SciNameUtils.isEmpty(specificEpithet) && SciNameUtils.isEmpty(infraspecificEpithet)) { 
	    					result.addComment("Parsed uninomial matches generic name.");
	    					result.setValue(ComplianceValue.COMPLIANT);
	    					result.setResultState(ResultState.RUN_HAS_RESULT);
	    					done = true;
	    				} else if (uninomial.equals(scientificName) && uninomial.equals(genericName) && (!SciNameUtils.isEmpty(specificEpithet) || !SciNameUtils.isEmpty(infraspecificEpithet))) { 
	    					result.addComment("Parsed uninomial matches generic name, but specific or infraspecific epithet are populated.");
	    					result.setValue(ComplianceValue.NOT_COMPLIANT);
	    					result.setResultState(ResultState.RUN_HAS_RESULT);
	    					done = true;
	    				} else if (scientificName.startsWith(uninomial) && uninomial.equals(genericName) && SciNameUtils.isEmpty(specificEpithet) && SciNameUtils.isEmpty(infraspecificEpithet)) { 
	    					result.addComment("Parsed uninomial matches generic name, scientific name contains unparsed text");
	    					result.setValue(ComplianceValue.COMPLIANT);
	    					result.setResultState(ResultState.RUN_HAS_RESULT);
	    					done = true;
	    				} else if (!uninomial.equals(genericName) ) { 
	    					result.addComment("Uninomial parsed out of dwc:scientificName does not match dwc:genericName.");
	    					result.setValue(ComplianceValue.NOT_COMPLIANT);
	    					result.setResultState(ResultState.RUN_HAS_RESULT);
	    					done = true;
	    				}
	    			}
	    			if (!done && !SciNameUtils.isEmpty(genericName) && !parseGeneric.equals(genericName)) { 
	    				result.addComment("Genus parsed out of dwc:scientificName does not match dwc:genericName.");
	    				result.setValue(ComplianceValue.NOT_COMPLIANT);
	    				result.setResultState(ResultState.RUN_HAS_RESULT);
	    			} else if (!done && !SciNameUtils.isEmpty(specificEpithet) && !parseSpecific.equals(specificEpithet)) { 
	    				result.addComment("Specific Epithet parsed out of dwc:scientificName does not match dwc:specificEpithet.");
	    				result.setValue(ComplianceValue.NOT_COMPLIANT);
	    				result.setResultState(ResultState.RUN_HAS_RESULT);
	    			} else if (!done && !SciNameUtils.isEmpty(infraspecificEpithet) && !parseInfraspecific.equals(infraspecificEpithet)) { 
	    				logger.debug(parseInfraspecific);
	    				result.addComment("Infraspecific Epithet parsed out of dwc:scientificName does not match dwc:infraspecificEpithet.");
	    				result.setValue(ComplianceValue.NOT_COMPLIANT);
	    				result.setResultState(ResultState.RUN_HAS_RESULT);
	    			} else if (!done) { 
	    				if (parseGeneric.equals(genericName) && parseSpecific.equals(specificEpithet) && parseInfraspecific.equals(infraspecificEpithet)) { 
	    					result.addComment("The values of dwc:genericName, specificEpithet, and infraspecificEpithet are pasrsed out of dwc:scientificName in their expected positions.");
	    					result.setValue(ComplianceValue.COMPLIANT);
	    					result.setResultState(ResultState.RUN_HAS_RESULT);
	    				} else if (genericName.equals("") && parseSpecific.equals(specificEpithet) && parseInfraspecific.equals(infraspecificEpithet)) { 
	    					result.addComment("The value of genericName is empty while the values of specificEpithet, and infraspecificEpithet are pasrsed out of dwc:scientificName in their expected positions.");
	    					result.setValue(ComplianceValue.COMPLIANT);
	    					result.setResultState(ResultState.RUN_HAS_RESULT);
	    				} else if (
	    						(parseGeneric.equals(genericName) || genericName.equals(""))
	    						&& 
	    						(parseSpecific.equals(specificEpithet) || specificEpithet.equals(""))
	    						&& 
	    						(parseInfraspecific.equals(infraspecificEpithet) || infraspecificEpithet.equals("") )
	    						) { 
	    					result.addComment("The values of genericName, specificEpithet, and infraspecificEpithet are empty or match values pasrsed out of dwc:scientificName in their expected positions.");
	    					result.setValue(ComplianceValue.COMPLIANT);
	    					result.setResultState(ResultState.RUN_HAS_RESULT);
	    				}
	    			}
	    		} catch (UnparsableNameException e) {
	    			logger.error(e.getMessage());
	    		} catch (InterruptedException e) {
	    			logger.error(e.getMessage());
   					result.addComment("Thread Error trying to parse scientific name: " + e.getMessage());
   					result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
				}
	    		try {
					nameParser.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
        	}
        }
        
        return result;
    }

    /**
     * Is there a value in any of the terms needed to determine that the taxon exists?
     *
     * Provides: #105 VALIDATION_TAXON_NOTEMPTY
     * Version: 2023-09-18
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param parentNameUsageID the provided dwc:parentNameUsageID to evaluate
     * @param subgenus the provided dwc:subgenus to evaluate
     * @param higherClassification the provided dwc:higherClassification to evaluate
     * @param vernacularName the provided dwc:vernacularName to evaluate
     * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
     * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param family the provided dwc:family to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param infragenericEpithet the provided dwc:infragenericEpithet to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param order the provided dwc:order to evaluate
     * @param cultivarEpithet the provided dwc:cultivarEpithet to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXON_NOTEMPTY", description="Is there a value in any of the terms needed to determine that the taxon exists?")
    @Provides("06851339-843f-4a43-8422-4e61b9a00e75")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/06851339-843f-4a43-8422-4e61b9a00e75/2023-09-18")
    @Specification("COMPLIANT if at least one term needed to determine the taxon of the entity exists and is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationTaxonNotempty(
    		@ActedUpon("dwc:class") String taxonomic_class, 
    		@ActedUpon("dwc:genus") String genus, 
    		@ActedUpon("dwc:taxonConceptID") String taxonConceptID, 
    		@ActedUpon("dwc:phylum") String phylum, 
    		@ActedUpon("dwc:scientificNameID") String scientificNameID, 
    		@ActedUpon("dwc:taxonID") String taxonID, 
    		@ActedUpon("dwc:parentNameUsageID") String parentNameUsageID, 
    		@ActedUpon("dwc:subgenus") String subgenus, 
    		@ActedUpon("dwc:higherClassification") String higherClassification, 
    		@ActedUpon("dwc:vernacularName") String vernacularName, 
    		@ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, 
    		@ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, 
    		@ActedUpon("dwc:kingdom") String kingdom, 
    		@ActedUpon("dwc:family") String family, 
    		@ActedUpon("dwc:scientificName") String scientificName, 
    		@ActedUpon("dwc:genericName") String genericName,
    		@ActedUpon("dwc:infragenericEpithet") String infragenericEpithet, 
    		@ActedUpon("dwc:specificEpithet") String specificEpithet, 
    		@ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, 
    		@ActedUpon("dwc:order") String order,
    		@ActedUpon("dwc:cultivarEpithet") String cultivarEpithet,
    		@ActedUpon("dwc:subfamily") String subfamily,
    		@ActedUpon("dwc:superfamily") String superfamily,
    		@ActedUpon("dwc:tribe") String tribe,
    		@ActedUpon("dwc:subtribe") String subtribe) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if at least one term needed to determine the taxon 
        // of the entity exists and is not EMPTY; otherwise NOT_COMPLIANT 
        //
        
		if (SciNameUtils.isEmpty(taxonID) &&
			SciNameUtils.isEmpty(kingdom) &&
			SciNameUtils.isEmpty(phylum) &&
			SciNameUtils.isEmpty(taxonomic_class) &&
			SciNameUtils.isEmpty(order) &&
			SciNameUtils.isEmpty(superfamily) &&
			SciNameUtils.isEmpty(family) &&
			SciNameUtils.isEmpty(subfamily) &&
			SciNameUtils.isEmpty(tribe) &&
			SciNameUtils.isEmpty(subtribe) &&
			SciNameUtils.isEmpty(genus) &&
			SciNameUtils.isEmpty(subgenus) &&
			SciNameUtils.isEmpty(scientificName) &&
			SciNameUtils.isEmpty(genericName) &&
			SciNameUtils.isEmpty(infragenericEpithet) &&
			SciNameUtils.isEmpty(specificEpithet) &&
			SciNameUtils.isEmpty(infraspecificEpithet) &&
			SciNameUtils.isEmpty(scientificNameID) &&
			SciNameUtils.isEmpty(taxonConceptID) &&
			SciNameUtils.isEmpty(parentNameUsageID) &&
			SciNameUtils.isEmpty(acceptedNameUsageID) &&
			SciNameUtils.isEmpty(originalNameUsageID) &&
			SciNameUtils.isEmpty(higherClassification) &&
			SciNameUtils.isEmpty(vernacularName) &&
			SciNameUtils.isEmpty(cultivarEpithet)
			) {
			result.addComment("No value provided for any Taxon term.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for at least one Taxon term.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}

        return result;
    }

    /**
     * Is there a value in dwc:scientificNameID?
     *
     * Provides: #120 VALIDATION_SCIENTIFICNAMEID_NOTEMPTY
     * Version: 2023-09-18
     *
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAMEID_NOTEMPTY", description="Is there a value in dwc:scientificNameID?")
    @Provides("401bf207-9a55-4dff-88a5-abcd58ad97fa")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/401bf207-9a55-4dff-88a5-abcd58ad97fa/2023-09-18")
    @Specification("COMPLIANT if dwc:scientificNameID is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationscientificNameIDNotempty(@ActedUpon("dwc:scientificNameID") String scientificNameID) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:scientificNameID is not EMPTY; otherwise NOT_COMPLIANT 
        //

		if (SciNameUtils.isEmpty(scientificNameID)) {
			result.addComment("No value provided for scientificNameID.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for scientificNameID.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        return result;
    }


    /**
     * Does the value of dwc:taxonID contain a complete identifier?
     *
     * Provides: #121 VALIDATION_TAXONID_COMPLETE
     * Version: 2023-09-18
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONID_COMPLETE", description="Does the value of dwc:taxonID contain a complete identifier?")
    @Provides("a82c7e3a-3a50-4438-906c-6d0fefa9e984")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/a82c7e3a-3a50-4438-906c-6d0fefa9e984/2023-09-18")
    @Specification("INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY; COMPLIANT if (1) taxonID is a validly formed LSID, or (2) taxonID is a validly formed URN with at least NID and NSS present, or (3) taxonID is in the form scope:value, or (4) taxonID is a validly formed URI with host and path where path consists of more than just '/'; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationTaxonidComplete(@ActedUpon("dwc:taxonID") String taxonID) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY; 
        // COMPLIANT if (1) taxonID is a validly formed LSID, or (2) 
        // taxonID is a validly formed URN with at least NID and NSS 
        // present, or (3) taxonID is in the form scope:value, or (4) 
        // taxonID is a validly formed URI with host and path where 
        // path consists of more than just "/"; otherwise NOT_COMPLIANT 
        
        
        if (SciNameUtils.isEmpty(taxonID)) { 
        	result.addComment("No value provided for taxonId.");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else if (taxonID.matches("^gbif:[0-9]+$")) { 
        	result.addComment("Provided taxonID recognized as a GBIF taxon identifier using the pseudo-namespace gbif:");
        	result.setResultState(ResultState.RUN_HAS_RESULT);
        	result.setValue(ComplianceValue.COMPLIANT);
        } else if (taxonID.matches("^[0-9]+$")) { 
        	result.addComment("Provided taxonID ["+ taxonID +"] is a bare integer without an authority and thus is incomplete.");
        	result.setResultState(ResultState.RUN_HAS_RESULT);
        	result.setValue(ComplianceValue.NOT_COMPLIANT);
        } else { 
        	try { 
        		RFC8141URN urn = new RFC8141URN(taxonID);
        		if (urn.getNid().equalsIgnoreCase("lsid")) { 
        			try { 
        				LSID lsid = new LSID(taxonID);
        				lsid.getAuthority();
        				lsid.getNamespace();
        				lsid.getObjectID();
        				result.addComment("Provided taxonID recognized as an LSID.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.COMPLIANT);
        			} catch (URNFormatException e2) { 
        				logger.debug(e2.getMessage());
        				result.addComment("Provided value for taxonID ["+taxonID+"] claims to be an lsid, but is not correctly formatted as such.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        			}
        		} else { 
        			logger.debug(urn.getNid());
        			logger.debug(urn.getNss());
        			if (urn.getNid().length()>0 && urn.getNss().length()>0) { 
        				result.addComment("Provided taxonID recognized as an URN.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.COMPLIANT);
        			} else { 
        				result.addComment("Provided taxonID appears to be a URN, but doesn't have both NID and NSS");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        			}
        		}
        	} catch (URNFormatException e) { 
        		logger.debug(e.getMessage());
        		if (taxonID.toLowerCase().matches("^[a-z]+:[0-9]+$")) { 
        			result.addComment("Provided taxonID ["+taxonID+"] matches the pattern scope:value where value is an integer.");
        			result.setResultState(ResultState.RUN_HAS_RESULT);
        			result.setValue(ComplianceValue.COMPLIANT);
        		} else if (taxonID.toLowerCase().matches("^[a-z]+:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) { 
        			result.addComment("Provided taxonID ["+taxonID+"] matches the pattern scope:value where value is a uuid.");
        			result.setResultState(ResultState.RUN_HAS_RESULT);
        			result.setValue(ComplianceValue.COMPLIANT);
        		} else if (taxonID.toLowerCase().matches("^[a-z0-9]+:[a-z0-9]+$")) {
        			// scope:value is not well defined, adding a alphanumericscope:alphanumericvalue pattern as a match
        			// other valid scope:value pairs might exist and need to have support added.
        			result.addComment("Provided taxonID ["+taxonID+"] matches the pattern scope:value where scope and value are alpha numeric strings.");
        			result.setResultState(ResultState.RUN_HAS_RESULT);
        			result.setValue(ComplianceValue.COMPLIANT);
        		} else { 
        			try {
        				URI uri = new URI(taxonID);
        				logger.debug(uri.getScheme());
        				logger.debug(uri.getAuthority());
        				logger.debug(uri.getHost());
        				logger.debug(uri.getPath());
        				if (uri.getHost()!=null && uri.getPath()!=null 
        						&& uri.getHost().length()>0 && uri.getPath().length()>0
        						&& !uri.getPath().equals("/")) {
        					if (uri.getHost().equalsIgnoreCase("www.gbif.org") && uri.getPath().equals("/species/")) { 
        						result.addComment("Provided taxonID recognized as GBIF species URL, but lacks the ID ["+taxonID+"]");
        						result.setResultState(ResultState.RUN_HAS_RESULT);
        						result.setValue(ComplianceValue.NOT_COMPLIANT);
        					} else { 
        						result.addComment("Provided taxonID recognized as an URI with host, and path.");
        						result.setResultState(ResultState.RUN_HAS_RESULT);
        						result.setValue(ComplianceValue.COMPLIANT);
        					}
        				} else { 
        					result.addComment("Provided taxonID may be a URI, but doesn't have host and path ["+taxonID+"]");
        					result.setResultState(ResultState.RUN_HAS_RESULT);
        					result.setValue(ComplianceValue.NOT_COMPLIANT);
        				}
        			} catch (URISyntaxException e1) {
        				logger.debug(e1);
        				result.addComment("Provided value for taxonID ["+taxonID+"] is not a LSID, URN, URI, or identifier in the form scope:value.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        			}
        		}
        	}
        	
        }
        return result;
    }

    /**
     * Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?
     *
     * Provides: #122 VALIDATION_GENUS_FOUND
     * Version: 2023-09-18
     *
     * @param genus the provided dwc:genus to evaluate
     * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_GENUS_FOUND", description="Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?")
    @Provides("f2ce7d55-5b1d-426a-b00e-6d4efe3058ec")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/f2ce7d55-5b1d-426a-b00e-6d4efe3058ec/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available;  INTERNAL_PREREQUISITES_NOT_MET if dwc:genus is EMPTY; COMPLIANT if the value of dwc:genus was found as a value at the rank of genus by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationGenusFound(@ActedUpon("dwc:genus") String genus, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:genus 
        // is EMPTY; COMPLIANT if the value of dwc:genus was found 
        // as a value at the rank of genus by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
        // {[https://doi.org/10.15468/39omei]} {API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 
    
        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        if (sourceAuthority==null) { 
        	sourceAuthority = new SciNameSourceAuthority();
        }
        
        if (SciNameUtils.isEmpty(genus)) { 
        	result.addComment("No value provided for genus.");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY)) { 
        		try {
        			List<NameUsage> matches = GBIFService.lookupGenus(genus, GBIFService.KEY_GBIFBACKBONE, 100);
        			logger.debug(matches.size());
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided genus found in GBIF backbone taxonomy as a genus.");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided genus found in GBIF backbone taxonomy as a genus.");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}
        	} else if (sourceAuthority.isGBIFChecklist()) { 
        		try {
        			List<NameUsage> matches = GBIFService.lookupGenus(genus, sourceAuthority.getAuthoritySubDataset(), 100);
        			logger.debug(matches.size());
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided genus found in  " + sourceAuthority.getAuthority().getName() + "  as a genus.");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided genus found in  " + sourceAuthority.getAuthority().getName() + "  as a genus.");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}        		
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
        		try {
        			List<NameUsage> matches = WoRMSService.lookupGenus(genus);
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided genus found in WoRMS as a genus.");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided genus found in WoRMS as a genus.");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (ApiException e) {
        			result.addComment("WoRMS aphia API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (Exception e) {
        			result.addComment("Error using WoRMS aphia API:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        			logger.error(e.getMessage(),e);
        		}
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) {
        		try {
        			List<NameUsage> matches = IRMNGService.lookupGenus(genus);
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided genus found in IRMNG as a genus.");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided genus found in IRMNG as a genus.");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (org.irmng.aphia.v1_0.handler.ApiException e) {
        			result.addComment("IRMNG aphia API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (Exception e) {
        			result.addComment("Error using IRMNG aphia API:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        			logger.error(e.getMessage(),e);
        		}        		
        	} else { 
        		result.addComment("Source Authority Not Implemented.");
        		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	} 
        }
		
        return result;
    }

    /**
     * Is there a value in dwc:taxonRank?
     *
     * Provides: #161 VALIDATION_TAXONRANK_NOTEMPTY
     * Version: 2023-09-18
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONRANK_NOTEMPTY", description="Is there a value in dwc:taxonRank?")
    @Provides("14da5b87-8304-4b2b-911d-117e3c29e890")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/14da5b87-8304-4b2b-911d-117e3c29e890/2023-09-18")
    @Specification("COMPLIANT if dwc:taxonRank is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationTaxonrankNotempty(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();
      
        // Specification
        // COMPLIANT if dwc:taxonRank is not EMPTY; otherwise NOT_COMPLIANT 
        //

		if (SciNameUtils.isEmpty(taxonRank)) {
			result.addComment("No value provided for taxonRank.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for taxonRank.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        return result;
    }
 

    /**
     * Does the value of dwc:taxonRank occur in bdq:sourceAuthority?
     *
     * Provides: #162 VALIDATION_TAXONRANK_STANDARD
     * Version: 2023-09-18
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @param sourceAuthority the authority for taxonRank values
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONRANK_STANDARD", description="Does the value of dwc:taxonRank occur in bdq:sourceAuthority?")
    @Provides("7bdb13a4-8a51-4ee5-be7f-20693fdb183e")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/7bdb13a4-8a51-4ee5-be7f-20693fdb183e/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonRank is EMPTY; COMPLIANT if the value of dwc:taxonRank is in the bdq:sourceAuthority; otherwise NOT_COMPLIANT. bdq:sourceAuthority default = 'GBIF Vocabulary: Taxonomic Rank' {[https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]} {dwc:taxonRank [https://dwc.tdwg.org/list/#dwc_taxonRank]}")
    public static DQResponse<ComplianceValue> validationTaxonrankStandard(
		   @ActedUpon("dwc:taxonRank") String taxonRank,
		   @Parameter(name="bdq:sourceAuthority") String sourceAuthority
		   ) {
       DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

       // Specification
       // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
       // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonRank 
       // is EMPTY; COMPLIANT if the value of dwc:taxonRank is in 
       // the bdq:sourceAuthority; otherwise NOT_COMPLIANT. 
       // 

       // Parameters. This test is defined as parameterized.
       // bdq:sourceAuthority
       // default = "GBIF Vocabulary: Taxonomic Rank" {[https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]} 
       // {dwc:taxonRank [https://dwc.tdwg.org/list/#dwc_taxonRank]} 

       if (SciNameUtils.isEmpty(sourceAuthority)) { 
    	   sourceAuthority = "https://rs.gbif.org/vocabulary/gbif/rank.xml";
       }
       if (sourceAuthority.equals("GBIF Vocabulary: Taxonomic Rank")) { 
    	   sourceAuthority = "https://rs.gbif.org/vocabulary/gbif/rank.xml";
       }
       if (SciNameUtils.isEmpty(taxonRank)) { 
    	   result.addComment("No value provided value for taxonRank");
    	   result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
       } else { 

    	   try { 
    		   if (sourceAuthority.equals("https://rs.gbif.org/vocabulary/gbif/rank.xml") || sourceAuthority.equals("Taxonomic Rank GBIF Vocabulary")) { 

    			   HashSet<String> values = new HashSet<String>();
    			   values.add("domain");
    			   values.add("kingdom");
    			   values.add("subkingdom");
    			   values.add("superphylum");
    			   values.add("phylum");
    			   values.add("subphylum");
    			   values.add("superclass");
    			   values.add("class");
    			   values.add("subclass");
    			   values.add("supercohort");
    			   values.add("cohort");
    			   values.add("subcohort");
    			   values.add("superorder");
    			   values.add("order");
    			   values.add("suborder");
    			   values.add("infraorder");
    			   values.add("superfamily");
    			   values.add("family");
    			   values.add("subfamily");
    			   values.add("tribe");
    			   values.add("subtribe");
    			   values.add("genus");
    			   values.add("subgenus");
    			   values.add("section");
    			   values.add("subsection");
    			   values.add("series");
    			   values.add("subseries");
    			   values.add("speciesAggregate");
    			   values.add("species");
    			   values.add("subspecificAggregate");
    			   values.add("subspecies");
    			   values.add("variety");
    			   values.add("subvariety");
    			   values.add("form");
    			   values.add("subform");
    			   values.add("cultivarGroup");
    			   values.add("cultivar");
    			   values.add("strain");

    			   if (values.contains(taxonRank)) {
    				   result.addComment("Provided value for taxonRank ["+ taxonRank+"] found in the GBIF taxon rank vocabulary.");
    				   result.setValue(ComplianceValue.COMPLIANT);
    				   result.setResultState(ResultState.RUN_HAS_RESULT);
    			   } else if (SciNameSingleton.getInstance().checkRankKnown(taxonRank)) {
    				   result.addComment("Provided value for taxonRank ["+ taxonRank+"] found as an alternative form for [" + SciNameSingleton.getInstance().getRank(taxonRank) + "] in the GBIF taxon rank vocabulary.");
    				   result.setValue(ComplianceValue.COMPLIANT);
    				   result.setResultState(ResultState.RUN_HAS_RESULT);
    			   } else { 
    				   result.addComment("Provided value for taxonRank ["+ taxonRank+"] not found in the GBIF taxon rank vocabulary.");
    				   result.setValue(ComplianceValue.NOT_COMPLIANT);
    				   result.setResultState(ResultState.RUN_HAS_RESULT);
    			   }
    		   } else { 
    			   throw new UnsupportedSourceAuthorityException("Specified bdq:sourceAuthority not supported");
    		   }
    	   } catch (UnsupportedSourceAuthorityException e) { 
    		   result.addComment("Unable to process:" + e.getMessage());
    		   result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
    	   }
       }
       
       return result;
    }
   
   /**
    * Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.
    *
    * Provides: #163 AMENDMENT_TAXONRANK_STANDARDIZED
    * Version: 2023-09-18
    *
    * @param taxonRank the provided dwc:taxonRank to evaluate
    * @return DQResponse the response of type AmendmentValue to return
    * @param sourceAuthority a {@link java.lang.String} object.
    */
   @Amendment(label="AMENDMENT_TAXONRANK_STANDARDIZED", description="Propose amendment to the value of dwc:taxonRank using bdq:sourceAuthority.")
   @Provides("e39098df-ef46-464c-9aef-bcdeee2a88cb")
   @ProvidesVersion("https://rs.tdwg.org/bdq/terms/e39098df-ef46-464c-9aef-bcdeee2a88cb/2023-09-18")
   @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL PREREQUISITES_NOT_MET if dwc:taxonRank is EMPTY; AMENDED the value of dwc:taxonRank if it can be unambiguously matched to a term in bdq:sourceAuthority; otherwise NOT_AMENDED bdq:sourceAuthority default = 'GBIF Vocabulary: Taxonomic Rank' {[https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]} {dwc:taxonRank [https://dwc.tdwg.org/list/#dwc_taxonRank]}")
   public static DQResponse<AmendmentValue> amendmentTaxonrankStandardized(
		@ActedUpon("dwc:taxonRank") String taxonRank,
   		@Parameter(name="bdq:sourceAuthority") String sourceAuthority
   ){
       DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

       // Specification
       // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
       // is not available; INTERNAL PREREQUISITES_NOT_MET if dwc:taxonRank 
       // is EMPTY; AMENDED the value of dwc:taxonRank if it can be 
       // unambiguously matched to a term in bdq:sourceAuthority; 
       // otherwise NOT_AMENDED 

       // Parameters. This test is defined as parameterized.
       // bdq:sourceAuthority default = "GBIF Vocabulary: Taxonomic Rank" 
       // {[https://api.gbif.org/v1/vocabularies/TaxonRank/concepts]} 
       // {dwc:taxonRank [https://dwc.tdwg.org/list/#dwc_taxonRank]} 
       
       if (SciNameUtils.isEmpty(sourceAuthority)) { 
    	   sourceAuthority = "https://rs.gbif.org/vocabulary/gbif/rank.xml";
       }
       if (SciNameUtils.isEmpty(taxonRank)) { 
    	   result.addComment("No value provided value for taxonRank");
    	   result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
       } else { 
    	   try { 
    		   if (sourceAuthority.equals("https://rs.gbif.org/vocabulary/gbif/rank.xml") || sourceAuthority.equals("Taxonomic Rank GBIF Vocabulary")) { 

    			   RankAuthorityLoader loader = new RankAuthorityLoader();
    			   try {
    				   loader.load();
    			   } catch (IOException  e) { 
    				   result.addComment("Error looking up " + sourceAuthority + " "  +e.getMessage());
    		   		} catch (ParserConfigurationException | SAXException e) {
    				   logger.debug(e.getMessage(), e);
    			   }

    			   Map<String,String> mapping = loader.getValuesCopy();
    			   // TODO: Lookup and cache file
    			   HashSet<String> values = new HashSet<String>();
    			   values.add("domain");
    			   values.add("kingdom");
    			   values.add("subkingdom");
    			   values.add("superphylum");
    			   values.add("phylum");
    			   values.add("subphylum");
    			   values.add("superclass");
    			   values.add("class");
    			   values.add("subclass");
    			   values.add("supercohort");
    			   values.add("cohort");
    			   values.add("subcohort");
    			   values.add("superorder");
    			   values.add("order");
    			   values.add("suborder");
    			   values.add("infraorder");
    			   values.add("superfamily");
    			   values.add("family");
    			   values.add("subfamily");
    			   values.add("tribe");
    			   values.add("subtribe");
    			   values.add("genus");
    			   values.add("subgenus");
    			   values.add("section");
    			   values.add("subsection");
    			   values.add("series");
    			   values.add("subseries");
    			   values.add("speciesAggregate");
    			   values.add("species");
    			   mapping.put("sp.", "species");
    			   mapping.put("sp", "species");
    			   values.add("subspecificAggregate");
    			   values.add("subspecies");
    			   values.add("variety");
    			   mapping.put("var.", "variety");
    			   mapping.put("var", "variety");
    			   values.add("subvariety");
    			   mapping.put("subvar", "subvariety");
    			   mapping.put("subvar", "subvariety");
    			   mapping.put("sub var.", "subvariety");
    			   values.add("form");
    			   mapping.put("f.", "form");
    			   mapping.put("forma", "form");
    			   values.add("subform");
    			   values.add("cultivarGroup");
    			   values.add("cultivar");
    			   values.add("strain");

    			   if (values.contains(taxonRank)) {
    				   result.addComment("Provided value for taxonRank ["+ taxonRank+"] found in the GBIF taxon rank vocabulary.");
    				   result.setResultState(ResultState.NOT_AMENDED);
    			   } else { 
    				   if (values.contains(taxonRank.trim().toLowerCase())) { 
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] matched to value in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.AMENDED);
    					   Map<String,String> keyValue = new HashMap<String,String>();
    					   keyValue.put("dwc:taxonRank", taxonRank.toLowerCase());
    					   AmendmentValue amendment = new AmendmentValue(keyValue);
    					   result.setValue(amendment);
    				   } else if (taxonRank.trim().equals("var.")) {
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] matched to value in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.AMENDED);
    					   Map<String,String> keyValue = new HashMap<String,String>();
    					   keyValue.put("dwc:taxonRank", "variety");
    					   AmendmentValue amendment = new AmendmentValue(keyValue);
    					   result.setValue(amendment);
    				   } else if (taxonRank.trim().toLowerCase().equals("forma")) {
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] matched to value in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.AMENDED);
    					   Map<String,String> keyValue = new HashMap<String,String>();
    					   keyValue.put("dwc:taxonRank", "form");
    					   AmendmentValue amendment = new AmendmentValue(keyValue);
    					   result.setValue(amendment);
    				   } else if (taxonRank.trim().toLowerCase().equals("cultivar group")) {
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] matched to value in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.AMENDED);
    					   Map<String,String> keyValue = new HashMap<String,String>();
    					   keyValue.put("dwc:taxonRank", "cultivarGroup");
    					   AmendmentValue amendment = new AmendmentValue(keyValue);
    					   result.setValue(amendment);
    				   } else if (taxonRank.trim().toLowerCase().equals("species aggregate")) {
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] matched to value in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.AMENDED);
    					   Map<String,String> keyValue = new HashMap<String,String>();
    					   keyValue.put("dwc:taxonRank", "speciesAggregate");
    					   AmendmentValue amendment = new AmendmentValue(keyValue);
    					   result.setValue(amendment);
    				   } else if (taxonRank.trim().toLowerCase().equals("subspecific aggregate") || taxonRank.trim().toLowerCase().equals("subspecies aggregate")) {
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] matched to value in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.AMENDED);
    					   Map<String,String> keyValue = new HashMap<String,String>();
    					   keyValue.put("dwc:taxonRank", "subspecificAggregate");
    					   AmendmentValue amendment = new AmendmentValue(keyValue);
    					   result.setValue(amendment);
    				   } else if (mapping.containsKey(taxonRank.trim().toLowerCase())) { 
    					   String mappedValue = mapping.get(taxonRank.trim().toLowerCase());
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] mapped to value ["+mappedValue+"] in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.AMENDED);
    					   Map<String,String> keyValue = new HashMap<String,String>();
    					   keyValue.put("dwc:taxonRank", mappedValue);
    					   AmendmentValue amendment = new AmendmentValue(keyValue);
    					   result.setValue(amendment);
    				   } else {
    					   result.addComment("Provided value for taxonRank ["+ taxonRank+"] not matched to a value in the GBIF taxon rank vocabulary.");
    					   result.setResultState(ResultState.NOT_AMENDED);
    				   }
    			   }
    		   } else { 
    			   throw new UnsupportedSourceAuthorityException("Specified bdq:sourceAuthority not supported");
    		   }
    	   } catch (UnsupportedSourceAuthorityException e) { 
    		   result.addComment("Unable to process:" + e.getMessage());
    		   result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
    	   }
       }
    
       return result;
   }
    /**
     * Provides internals for validationKingdomNotFound etc.
     *
     * @param taxon name of the taxon to look up
     * @param rank at which potential matches of the taxon must be to match (expected to be single word, first letter capitalized.
     * @param sourceAuthority in which to look up the taxon
     * @return a validation compliance value specifying a match (COMPLIANT) or not.
     */
    public static DQResponse<ComplianceValue> validateHigherTaxonAtRank(String taxon, String rank, SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }

        if (SciNameUtils.isEmpty(taxon)) { 
        	result.addComment("No value provided for " + rank + ".");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY)) { 
        		try {
        			List<NameUsage> matches = GBIFService.lookupTaxonAtRank(taxon, GBIFService.KEY_GBIFBACKBONE, rank, 100);
        			logger.debug(matches.size());
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided " + rank +  " found in GBIF backbone taxonomy at rank " + rank + ".");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided " + rank + " ["+ taxon + "] found in GBIF backbone taxonomy at rank " + rank + ".");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}
        	} else if (sourceAuthority.isGBIFChecklist()) { 
        		try {
        			List<NameUsage> matches = GBIFService.lookupTaxonAtRank(taxon, sourceAuthority.getAuthoritySubDataset(), "Order", 100);
        			logger.debug(matches.size());
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided " + rank + " found in " + sourceAuthority.getAuthority().getName() + " at rank " + rank + ".");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided " + rank +  "  ["+ taxon + "] found  " + sourceAuthority.getAuthority().getName() + " at rank " + rank + ".");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}        		
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.INVALID)) {
        		result.addComment("Invalid service is not available.");
        		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
        		try {
        			List<NameUsage> matches = WoRMSService.lookupTaxonAtRank(taxon,rank);
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided "+rank+" found in WoRMS at rank " + rank + ".");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided " + rank + "  ["+ taxon + "] found in WoRMS at rank " + rank + ".");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (ApiException e) {
        			result.addComment("WoRMS aphia API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (Exception e) {
        			result.addComment("Error using WoRMS aphia API:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        			logger.error(e.getMessage(),e);
        		}
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) {
        		try {
        			List<NameUsage> matches = IRMNGService.lookupTaxonAtRank(taxon,rank);
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided "+rank+" found in IRMNG at rank " + rank + ".");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided " + rank + "  ["+ taxon + "] found in IRMNG at rank " + rank + ".");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (org.irmng.aphia.v1_0.handler.ApiException e) {
        			result.addComment("IRMNG aphia API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		} catch (Exception e) {
        			result.addComment("Error using IRMNG aphia API:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        			logger.error(e.getMessage(),e);
        		}        		
        	} else { 
        		result.addComment("Source Authority Not Implemented.");
        		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	} 
        }
		
        return result;
    }    
    
    
    /**
     * Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?
     *
     * Provides: #123 VALIDATION_CLASSIFICATION_CONSISTENT
     * Version: 2023-09-18
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param taxonomic_class the provided dwc:class to evaluate.
     * @param order the provided dwc:order to evaluate
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param family the provided dwc:family to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param sourceAuthorityString identifier for the source authority in which to look up the taxon
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASSIFICATION_CONSISTENT", description="Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?")
    @Provides("2750c040-1d4a-4149-99fe-0512785f2d5f")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2750c040-1d4a-4149-99fe-0512785f2d5f/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if all of the fields dwc:kingdom dwc:phylum, dwc:class, dwc:order, dwc:superfamily, dwc:family, dwc:subfamily, dwc:tribe, dwc:subtribe, dwc:genus are EMPTY; COMPLIANT if the combination of values of higher classification taxonomic terms (dwc:kingdom, dwc:phylum, dwc:class, dwc:order, dwc:superfamily, dwc:family, dwc:subfamily, dwc:tribe, dwc:subtribe, dwc:genus) are consistent with the lowest ranking matched element in the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationClassificationConsistentString(
    		@ActedUpon("dwc:kingdom") String kingdom, 
    		@ActedUpon("dwc:phylum") String phylum, 
    		@ActedUpon("dwc:class") String taxonomic_class, 
    		@ActedUpon("dwc:order") String order,
    		@ActedUpon("dwc:superfamily") String superfamily,
    		@ActedUpon("dwc:family") String family, 
    		@ActedUpon("dwc:subfamily") String subfamily,
    		@ActedUpon("dwc:tribe") String tribe,
    		@ActedUpon("dwc:subtribe") String subtribe,
    		@ActedUpon("dwc:genus") String genus,
    		@Parameter(name="bdq:sourceAuthority") String sourceAuthorityString
    ) {
 	
    	DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();
    	SciNameSourceAuthority sourceAuthority = null;

    	try {
    		if (SciNameUtils.isEmpty(sourceAuthorityString)) { 
    			sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
    		} else { 
    			sourceAuthority = new SciNameSourceAuthority(sourceAuthorityString);
    		}
    		result = validationClassificationConsistent(kingdom, phylum, taxonomic_class, order, superfamily, family, subfamily, tribe, subtribe, genus, sourceAuthority);
    	} catch (SourceAuthorityException e) {
    		logger.error(e.getMessage());
    		result.addComment("Unable to process:" + e.getMessage());
    		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
    	}

    	return result;
    }
    
    /**
     * Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?
     *
     * Provides: #123 VALIDATION_CLASSIFICATION_CONSISTENT
     * Version: 2023-09-18
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param taxonomic_class the provided dwc:class to evaluate.
     * @param order the provided dwc:order to evaluate
     * @param superfamily the provided dwc:superfamily to evaluate
     * @param family the provided dwc:family to evaluate
     * @param subfamily the provided dwc:subfamily to evaluate
     * @param tribe the provided dwc:tribe to evaluate
     * @param subtribe the provided dwc:subtribe to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param sourceAuthority in which to look up the taxon
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASSIFICATION_CONSISTENT", description="Is the combination of higher classification taxonomic terms consistent using bdq:sourceAuthority?")
    @Provides("2750c040-1d4a-4149-99fe-0512785f2d5f")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2750c040-1d4a-4149-99fe-0512785f2d5f/2023-09-18")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if all of the fields dwc:kingdom dwc:phylum, dwc:class, dwc:order, dwc:superfamily, dwc:family, dwc:subfamily, dwc:tribe, dwc:subtribe, dwc:genus are EMPTY; COMPLIANT if the combination of values of higher classification taxonomic terms (dwc:kingdom, dwc:phylum, dwc:class, dwc:order, dwc:superfamily, dwc:family, dwc:subfamily, dwc:tribe, dwc:subtribe, dwc:genus) are consistent with the lowest ranking matched element in the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' {[https://doi.org/10.15468/39omei]} {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]}")
    public static DQResponse<ComplianceValue> validationClassificationConsistent(
    		@ActedUpon("dwc:kingdom") String kingdom, 
    		@ActedUpon("dwc:phylum") String phylum, 
    		@ActedUpon("dwc:class") String taxonomic_class, 
    		@ActedUpon("dwc:order") String order,
    		@ActedUpon("dwc:superfamily") String superfamily,
    		@ActedUpon("dwc:family") String family, 
    		@ActedUpon("dwc:subfamily") String subfamily,
    		@ActedUpon("dwc:tribe") String tribe,
    		@ActedUpon("dwc:subtribe") String subtribe,
    		@ActedUpon("dwc:genus") String genus,
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
    ) {
    	DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

    	// Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if all 
        // of the fields dwc:kingdom dwc:phylum, dwc:class, dwc:order, 
        // dwc:superfamily, dwc:family, dwc:subfamily, dwc:tribe, dwc:subtribe, 
        // dwc:genus are EMPTY; COMPLIANT if the combination of values 
        // of higher classification taxonomic terms (dwc:kingdom, dwc:phylum, 
        // dwc:class, dwc:order, dwc:superfamily, dwc:family, dwc:subfamily, 
        // dwc:tribe, dwc:subtribe, dwc:genus) are consistent with 
        // the lowest ranking matched element in the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT 
        // 

    	// Parameters. This test is defined as parameterized.
    	// bdq:sourceAuthority default = "GBIF Backbone Taxonomy" 
    	// {[https://doi.org/10.15468/39omei]} 
    	// {API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]} 

    	if (sourceAuthority==null) { 
    		sourceAuthority = new SciNameSourceAuthority();
    	}

    	String lowestRankingTaxon = null;
    	String lowestRank = null;
    	Map<String,String> parentsOfHighers = new HashMap<String,String>();
    	if (!SciNameUtils.isEmpty(genus)) { 
    		lowestRankingTaxon = genus;
    		lowestRank = "Genus";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(subfamily)) { 
    		lowestRankingTaxon = subfamily;
    		lowestRank = "Subfamily";
    	}     	
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(subtribe)) { 
    		lowestRankingTaxon = subtribe;
    		lowestRank = "Subtribe";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(tribe)) { 
    		lowestRankingTaxon = tribe;
    		lowestRank = "Tribe";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(family)) { 
    		lowestRankingTaxon = family;
    		lowestRank = "Family";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(superfamily)) { 
    		lowestRankingTaxon = superfamily;
    		lowestRank = "Superfamily";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(order)) { 
    		lowestRankingTaxon = order;
    		lowestRank = "Order";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(taxonomic_class)) { 
    		lowestRankingTaxon = taxonomic_class;
    		lowestRank = "Class";

    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(phylum)) { 
    		lowestRankingTaxon = phylum;
    		lowestRank = "Phylum";
    	} 
    	if (lowestRankingTaxon == null && !SciNameUtils.isEmpty(kingdom)) { 
    		lowestRankingTaxon = kingdom;
    		lowestRank = "Kingdom";
    	} 

    	if (sourceAuthority.getName().equals(EnumSciNameSourceAuthority.INVALID.getName())) { 
    		result.addComment("Invalid source authority.");
    		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
    	} else if (SciNameUtils.isEmpty(lowestRankingTaxon)) { 
    		result.addComment("No value provided for kingdom, phylum, class, order, or family.");
    		result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
    	} else {
    		logger.debug("looking up " + lowestRankingTaxon + " at rank " + lowestRank);
    		DQResponse<ComplianceValue> lowestLookup = validateHigherTaxonAtRank(lowestRankingTaxon,lowestRank,sourceAuthority);
    		if (lowestLookup.getResultState()==ResultState.RUN_HAS_RESULT && lowestLookup.getValue().equals(ComplianceValue.COMPLIANT)) { 
    			result.addComment(lowestRank + " " + lowestRankingTaxon + " found in " + sourceAuthority.getName());
    			try {
    				List<NameUsage> lookupResult = null;
    				if (sourceAuthority.isGBIFChecklist()) { 
    					lookupResult = GBIFService.lookupTaxonAtRank(lowestRankingTaxon, sourceAuthority.getAuthoritySubDataset(), lowestRank, 10);
    				} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) { 
    					lookupResult = WoRMSService.lookupTaxonAtRank(lowestRankingTaxon, lowestRank);
    				} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.IRMNG)) { 
    					lookupResult = IRMNGService.lookupTaxonAtRank(lowestRankingTaxon, lowestRank);
    				} else {
    					throw new UnsupportedSourceAuthorityException("Authority " + sourceAuthority.getName() + " Not implemented.");
    				}
    				boolean hasMatch = false;
    				Iterator<NameUsage> i = lookupResult.iterator();
    				while (i.hasNext() && !hasMatch) { 
    					NameUsage aResult = i.next();
    					logger.debug(aResult.getCanonicalName());
    					logger.debug(aResult.getKingdom());
    					logger.debug(aResult.getPhylum());
    					logger.debug(aResult.getClazz());
    					logger.debug(aResult.getOrder());
    					logger.debug(aResult.getSuperfamily());
    					logger.debug(aResult.getFamily());
    					logger.debug(aResult.getSubfamily());
    					logger.debug(aResult.getTribe());
    					logger.debug(aResult.getSubtribe());
    					logger.debug(aResult.getGenus());
    					// Check if returned values are exact matches or synonyms of values present.
    					if (lowestRank.equalsIgnoreCase("Kingdom")) { 
    						hasMatch=true;
    					} else if (lowestRank.equalsIgnoreCase("Phylum")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							hasMatch=true;
    						} 
    					} else if (lowestRank.equalsIgnoreCase("Class")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								hasMatch=true;
    							}
    						} 
    					} else if (lowestRank.equalsIgnoreCase("Order")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								if (SciNameUtils.sameOrSynonym(taxonomic_class, aResult.getClazz(), "Class", sourceAuthority)) {
    									hasMatch=true;
    								}
    							}
    						} 
    					} else if (lowestRank.equalsIgnoreCase("Superfamily")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								if (SciNameUtils.sameOrSynonym(taxonomic_class, aResult.getClazz(), "Class", sourceAuthority)) {
    									if (SciNameUtils.sameOrSynonym(order, aResult.getOrder(), "Order", sourceAuthority)) {
    										hasMatch=true;
    									}
    								}
    							}
    						}     						
    					} else if (lowestRank.equalsIgnoreCase("Family")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								if (SciNameUtils.sameOrSynonym(taxonomic_class, aResult.getClazz(), "Class", sourceAuthority)) {
    									if (SciNameUtils.sameOrSynonym(order, aResult.getOrder(), "Order", sourceAuthority)) {
    										if (SciNameUtils.sameOrSynonym(superfamily, aResult.getSuperfamily(), "Superfamily", sourceAuthority)) {
    											hasMatch=true;
    										}
    									}
    								}
    							}
    						} 
       					} else if (lowestRank.equalsIgnoreCase("Subfamily")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								if (SciNameUtils.sameOrSynonym(taxonomic_class, aResult.getClazz(), "Class", sourceAuthority)) {
    									if (SciNameUtils.sameOrSynonym(order, aResult.getOrder(), "Order", sourceAuthority)) {
    										if (SciNameUtils.sameOrSynonym(superfamily, aResult.getSuperfamily(), "Superfamily", sourceAuthority)) {
    											if (SciNameUtils.sameOrSynonym(family, aResult.getFamily(), "Family", sourceAuthority)) {
    												hasMatch=true;
    											}
    										}
    									}
    								}
    							}
    						} 
      					} else if (lowestRank.equalsIgnoreCase("Tribe")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								if (SciNameUtils.sameOrSynonym(taxonomic_class, aResult.getClazz(), "Class", sourceAuthority)) {
    									if (SciNameUtils.sameOrSynonym(order, aResult.getOrder(), "Order", sourceAuthority)) {
    										if (SciNameUtils.sameOrSynonym(superfamily, aResult.getSuperfamily(), "Superfamily", sourceAuthority)) {
    											if (SciNameUtils.sameOrSynonym(family, aResult.getFamily(), "Family", sourceAuthority)) {
    												if (SciNameUtils.sameOrSynonym(subfamily, aResult.getSubfamily(), "Subfamily", sourceAuthority)) {
    													hasMatch=true;
    												}
    											}
    										}
    									}
    								}
    							}
    						} 
      					} else if (lowestRank.equalsIgnoreCase("Subtribe")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								if (SciNameUtils.sameOrSynonym(taxonomic_class, aResult.getClazz(), "Class", sourceAuthority)) {
    									if (SciNameUtils.sameOrSynonym(order, aResult.getOrder(), "Order", sourceAuthority)) {
    										if (SciNameUtils.sameOrSynonym(superfamily, aResult.getSuperfamily(), "Superfamily", sourceAuthority)) {
    											if (SciNameUtils.sameOrSynonym(family, aResult.getFamily(), "Family", sourceAuthority)) {
    												if (SciNameUtils.sameOrSynonym(subfamily, aResult.getSubfamily(), "Subfamily", sourceAuthority)) {
    													if (SciNameUtils.sameOrSynonym(tribe, aResult.getTribe(), "Tribe", sourceAuthority)) {
    														hasMatch=true;
    													}
    												}
    											}
    										}
    									}
    								}
    							}
    						}        						
    					} else if (lowestRank.equalsIgnoreCase("Genus")) {
    						if (SciNameUtils.sameOrSynonym(kingdom, aResult.getKingdom(), "Kingdom", sourceAuthority)) {
    							if (SciNameUtils.sameOrSynonym(phylum, aResult.getPhylum(), "Phylum", sourceAuthority)) {
    								if (SciNameUtils.sameOrSynonym(taxonomic_class, aResult.getClazz(), "Class", sourceAuthority)) {
    									if (SciNameUtils.sameOrSynonym(order, aResult.getOrder(), "Order", sourceAuthority)) {
    										if (SciNameUtils.sameOrSynonym(superfamily, aResult.getSuperfamily(), "Superfamily", sourceAuthority)) {
    											if (SciNameUtils.sameOrSynonym(family, aResult.getFamily(), "Family", sourceAuthority)) {
    												if (SciNameUtils.sameOrSynonym(subfamily, aResult.getSubfamily(), "Subfamily", sourceAuthority)) {
    													if (SciNameUtils.sameOrSynonym(tribe, aResult.getTribe(), "Tribe", sourceAuthority)) {
    														if (SciNameUtils.sameOrSynonym(subtribe, aResult.getSubtribe(), "Subtribe", sourceAuthority)) {
    															hasMatch=true;
    														}
    													}
    												}
    											}
    										}
    									}
    								}
    							}
    						}     						
    					}
    					if (hasMatch) { 
    						result.addComment("Matches to higher ranks found in " + sourceAuthority.getAuthority().getName());
    						BooleanWithComment checkHierarchy = SciNameUtils.isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, subfamily, genus, sourceAuthority);
    						result.setResultState(ResultState.RUN_HAS_RESULT);
    						result.addComment(checkHierarchy.getComment());
    						if (checkHierarchy.getBooleanValue()) { 
    							result.setValue(ComplianceValue.COMPLIANT);
    						} else { 
    							result.setValue(ComplianceValue.NOT_COMPLIANT);
    						}
    					} else { 
    						result.addComment("No matching classification found in " + sourceAuthority.getAuthority().getName());
    						result.setResultState(ResultState.RUN_HAS_RESULT);
    						result.setValue(ComplianceValue.NOT_COMPLIANT);
    					}
    				}
    			} catch (IOException e) {
    				result.addComment("Error looking up taxon in " + sourceAuthority.getAuthority().getName());
    				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
    			} catch (UnsupportedSourceAuthorityException e) {
    				result.addComment("Lookup in source Authority " + sourceAuthority.getAuthority().getName() + " Not implemented");
    				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
				} catch (ApiException e) {
    				result.addComment("Error looking up taxon in " + sourceAuthority.getAuthority().getName());
    				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
				} catch (org.irmng.aphia.v1_0.handler.ApiException e) {
    				result.addComment("Error looking up taxon in " + sourceAuthority.getAuthority().getName());
    				result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
				}

    		} else { 
    			result.addComment("Value provided for " + lowestRank + " [" + lowestRankingTaxon + "] not found in " + sourceAuthority.getName());
    			result.setResultState(ResultState.RUN_HAS_RESULT);
    			result.setValue(ComplianceValue.NOT_COMPLIANT);
    		}

    	} 
    	return result;
    }
    
    /**
     * Does the value of dwc:scientificNameID contain a complete identifier?
     *
     * Provides: #212 VALIDATION_SCIENTIFICNAMEID_COMPLETE
     * Version: 2023-09-18
     *
     * @param scientificNameID the provided dwc:scientificNameID to evaluate as ActedUpon.
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAMEID_COMPLETE", description="Does the value of dwc:scientificNameID contain a complete identifier?")
    @Provides("6eeac3ed-f691-457f-a42e-eaa9c8a71ce8")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/6eeac3ed-f691-457f-a42e-eaa9c8a71ce8/2023-09-18")
    @Specification("INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID is EMPTY; COMPLIANT if (1) dwc:scientificNameID is a validly formed LSID, or (2) dwc:scientificNameID is a validly formed URN with at least NID and NSS present, or (3) dwc:scientificNameID is in the form scope:value, or (4) dwc:scientificNameID is a validly formed URI with host and path where path consists of more than just '/'; otherwise NOT_COMPLIANT ")
    public DQResponse<ComplianceValue> validationScientificnameidComplete(
        @ActedUpon("dwc:scientificNameID") String scientificNameID
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID is 
        // EMPTY; COMPLIANT if (1) dwc:scientificNameID is a validly 
        // formed LSID, or (2) dwc:scientificNameID is a validly formed 
        // URN with at least NID and NSS present, or (3) dwc:scientificNameID 
        // is in the form scope:value, or (4) dwc:scientificNameID 
        // is a validly formed URI with host and path where path consists 
        // of more than just "/"; otherwise NOT_COMPLIANT 

        if (SciNameUtils.isEmpty(scientificNameID))  {
        	result.addComment("No value provided for scientificNameId.");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else if (scientificNameID.matches("^[0-9]+$")) { 
        	result.addComment("Provided scientificNameID ["+ scientificNameID +"] is a bare integer without an authority and this is incomplete.");
        	result.setResultState(ResultState.RUN_HAS_RESULT);
        	result.setValue(ComplianceValue.NOT_COMPLIANT);
        } else { 
        	try { 
        		RFC8141URN urn = new RFC8141URN(scientificNameID);
        		if (urn.getNid().equalsIgnoreCase("lsid")) { 
        			try { 
        				LSID lsid = new LSID(scientificNameID);
        				lsid.getAuthority();
        				lsid.getNamespace();
        				lsid.getObjectID();
        				result.addComment("Provided scientificNameID recognized as an LSID.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.COMPLIANT);
        			} catch (URNFormatException e2) { 
        				logger.debug(e2.getMessage());
        				result.addComment("Provided value for scientificNameID ["+scientificNameID+"] claims to be an lsid, but is not correctly formatted as such.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        			}
        		} else { 
        			logger.debug(urn.getNid());
        			logger.debug(urn.getNss());
        			if (urn.getNid().length()>0 && urn.getNss().length()>0) { 
        				result.addComment("Provided scientificNameID recognized as an URN.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.COMPLIANT);
        			} else { 
        				result.addComment("Provided scientificNameID appears to be a URN, but doesn't have both NID and NSS");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        			}
        		}
        	} catch (URNFormatException e) { 
        		logger.debug(e.getMessage());
        		if (scientificNameID.matches("^gbif:[0-9]+$")) { 
        			result.addComment("Provided scientificNameID recognized as a GBIF taxon identifier using the pseudo-namespace gbif:");
        			result.setResultState(ResultState.RUN_HAS_RESULT);
        			result.setValue(ComplianceValue.COMPLIANT);
        		} else if (scientificNameID.toLowerCase().matches("^[a-z]+:[0-9]+$")) { 
        			result.addComment("Provided scientificNameID ["+scientificNameID+"] matches the pattern scope:value where value is an integer.");
        			result.setResultState(ResultState.RUN_HAS_RESULT);
        			result.setValue(ComplianceValue.COMPLIANT);
        		} else if (scientificNameID.toLowerCase().matches("^[a-z]+:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) { 
        			result.addComment("Provided scientificNameID ["+scientificNameID+"] matches the pattern scope:value where value is a uuid.");
        			result.setResultState(ResultState.RUN_HAS_RESULT);
        			result.setValue(ComplianceValue.COMPLIANT);
        		} else { 
        			try {
        				URI uri = new URI(scientificNameID);
        				logger.debug(uri.getScheme());
        				logger.debug(uri.getAuthority());
        				logger.debug(uri.getHost());
        				logger.debug(uri.getPath());
        				if (uri.getHost()!=null && uri.getPath()!=null 
        						&& uri.getHost().length()>0 && uri.getPath().length()>0
        						&& !uri.getPath().equals("/")) {
        					if (uri.getHost().equalsIgnoreCase("www.gbif.org") && uri.getPath().equals("/species/")) { 
        						result.addComment("Provided scientificNameID recognized as GBIF species URL, but lacks the ID ["+scientificNameID+"]");
        						result.setResultState(ResultState.RUN_HAS_RESULT);
        						result.setValue(ComplianceValue.NOT_COMPLIANT);
        					} else { 
        						result.addComment("Provided scientificNameID recognized as an URI with host, and path.");
        						result.setResultState(ResultState.RUN_HAS_RESULT);
        						result.setValue(ComplianceValue.COMPLIANT);
        					}
        				} else { 
        					result.addComment("Provided scientificNameID may be a URI, but doesn't have host and path ["+scientificNameID+"]");
        					result.setResultState(ResultState.RUN_HAS_RESULT);
        					result.setValue(ComplianceValue.NOT_COMPLIANT);
        				}
        			} catch (URISyntaxException e1) {
        				logger.debug(e1);
        				result.addComment("Provided value for scientificNameID ["+scientificNameID+"] is not a LSID, URN, URI, or identifier in the form scope:value.");
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        			}
        		}
        	}
        }
        
        return result;
    }


    /**
    * Is there a value in dwc:kingdom?
    *
    * Provides: #216 VALIDATION_KINGDOM_NOTEMPTY
    * Version: 2024-01-28
    *
    * @param kingdom the provided dwc:kingdom to evaluate as ActedUpon.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_KINGDOM_NOTEMPTY", description="Is there a value in dwc:kingdom?")
    @Provides("36ed36c9-b1a7-40b2-b5e2-0d012e772098")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/36ed36c9-b1a7-40b2-b5e2-0d012e772098/2024-01-28")
    @Specification("COMPLIANT if dwc:kingdom is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationKingdomNotempty(
        @ActedUpon("dwc:kingdom") String kingdom
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:kingdom is not EMPTY; otherwise NOT_COMPLIANT 
        // 

		if (SciNameUtils.isEmpty(kingdom)) {
			result.addComment("No value provided for kingdom.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for kingdom.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:scientificNameAuthorship?
    *
    * Provides: #244 VALIDATION_SCIENTIFICNAMEAUTHORSHIP_NOTEMPTY
    * Version: 2024-02-04
    *
    * @param scientificNameAuthorship the provided dwc:scientificNameAuthorship to evaluate as ActedUpon.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_SCIENTIFICNAMEAUTHORSHIP_NOTEMPTY", description="Is there a value in dwc:scientificNameAuthorship?")
    @Provides("49f1d386-5bed-43ae-bd43-deabf7df64fc")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/49f1d386-5bed-43ae-bd43-deabf7df64fc/2024-02-04")
    @Specification("COMPLIANT if dwc:scientificNameAuthorship is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationScientificnameauthorshipNotempty(
        @ActedUpon("dwc:scientificNameAuthorship") String scientificNameAuthorship
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:scientificNameAuthorship is not EMPTY; 
        // otherwise NOT_COMPLIANT 

		if (SciNameUtils.isEmpty(scientificNameAuthorship)) {
			result.addComment("No value provided for scientificNameAuthorship.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for scientificNameAuthorship.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:namePublishedInYear?
    *
    * Provides: #259 VALIDATION_NAMEPUBLISHEDINYEAR_NOTEMPTY
    * Version: 2024-02-07
    *
    * @param namePublishedInYear the provided dwc:namePublishedInYear to evaluate as ActedUpon.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_NAMEPUBLISHEDINYEAR_NOTEMPTY", description="Is there a value in dwc:namePublishedInYear?")
    @Provides("ff59f77d-71e9-4eb1-aac9-8bd05c50ff70")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/ff59f77d-71e9-4eb1-aac9-8bd05c50ff70/2024-02-07")
    @Specification("COMPLIANT if dwc:namePublishedInYear is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationNamepublishedinyearNotempty(
        @ActedUpon("dwc:namePublishedInYear") String namePublishedInYear
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:namePublishedInYear is not EMPTY; otherwise 
        // NOT_COMPLIANT 

		if (SciNameUtils.isEmpty(namePublishedInYear)) {
			result.addComment("No value provided for namePublishedInYear.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for namePublishedInYear.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:vernacularName?
    *
    * Provides: 254 VALIDATION_VERNACULARNAME_NOTEMPTY
    * Version: 2024-02-04
    *
    * @param vernacularName the provided dwc:vernacularName to evaluate as ActedUpon.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_VERNACULARNAME_NOTEMPTY", description="Is there a value in dwc:vernacularName?")
    @Provides("eb9b70c7-9cf6-42ea-a708-5de527211df4")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/eb9b70c7-9cf6-42ea-a708-5de527211df4/2024-02-04")
    @Specification("COMPLIANT if dwc:vernacularName is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationVernacularnameNotempty(
        @ActedUpon("dwc:vernacularName") String vernacularName
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:vernacularName is not EMPTY; otherwise 
        // NOT_COMPLIANT 

		if (SciNameUtils.isEmpty(vernacularName)) {
			result.addComment("No value provided for vernacularName.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for vernacularName.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:typeStatus?
    *
    * The vast majority of current biodiversity data will be expected to not have a value in dwc:typeStatus. 
    * This test does not have general utility, but could have value in assessing fittness for use for a 
    * set of data quality needs/use cases related to type specimens.
    *
    * Provides: 246 VALIDATION_TYPESTATUS_NOTEMPTY
    * Version: 2024-02-04
    *
    * @param typeStatus the provided dwc:typeStatus to evaluate as ActedUpon.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_TYPESTATUS_NOTEMPTY", description="Is there a value in dwc:typeStatus?")
    @Provides("cd7cae15-f255-41a3-b002-c9620c40f620")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/cd7cae15-f255-41a3-b002-c9620c40f620/2024-02-04")
    @Specification("COMPLIANT if dwc:typeStatus is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationTypestatusNotempty(
        @ActedUpon("dwc:typeStatus") String typeStatus
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:typeStatus is not EMPTY; otherwise NOT_COMPLIANT 
        // 
		if (SciNameUtils.isEmpty(typeStatus)) {
			result.addComment("No value provided for typeStatus.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for typeStatus.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:higherClassification?
    *
    * Provides: 229 VALIDATION_HIGHERCLASSIFICATION_NOTEMPTY
    * Version: 2024-01-29
    *
    * @param higherClassification the provided dwc:higherClassification to evaluate as ActedUpon.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_HIGHERCLASSIFICATION_NOTEMPTY", description="Is there a value in dwc:higherClassification?")
    @Provides("72ed16f1-0587-4afd-a9fc-602c2f3f1975")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/72ed16f1-0587-4afd-a9fc-602c2f3f1975/2024-01-29")
    @Specification("COMPLIANT if dwc:higherClassification is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationHigherclassificationNotempty(
        @ActedUpon("dwc:higherClassification") String higherClassification
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:higherClassification is not EMPTY; otherwise 
        // NOT_COMPLIANT 

		if (SciNameUtils.isEmpty(higherClassification)) {
			result.addComment("No value provided for higherClassification.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for higherClassification.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:specificEpithet?
    *
    * Provides: 220 VALIDATION_SPECIFICEPITHET_NOTEMPTY
    * Version: 2024-06-05
    *
    * @param specificEpithet the provided dwc:specificEpithet to evaluate as ActedUpon.
    * @param taxonRank the provided dwc:taxonRank to evaluate as Consulted.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_SPECIFICEPITHET_NOTEMPTY", description="Is there a value in dwc:specificEpithet?")
    @Provides("cdb37443-292e-49b6-a012-4718f0d7ba64")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/cdb37443-292e-49b6-a012-4718f0d7ba64/2024-06-05")
    @Specification("COMPLIANT if dwc:specificEpithet is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationSpecificepithetNotempty(
        @ActedUpon("dwc:specificEpithet") String specificEpithet,
        @Consulted("dwc:taxonRank") String taxonRank
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:specificEpithet is EMPTY 
        // and dwc:taxonRank contains a value that is not interpretable as a 
        // taxon rank; COMPLIANT if dwc:specificEpithet is not EMPTY, or 
        // dwc:specificEpithet is EMPTY and the value in dwc:taxonRank is 
        // higher than specific epithet; otherwise NOT_COMPLIANT
        
		if (SciNameUtils.isEmpty(specificEpithet)) {
			Boolean rankTest = SciNameUtils.isRankAbove("species",taxonRank);
			if (rankTest==null) { 
				DQResponse<AmendmentValue> lookupRank = amendmentTaxonrankStandardized(taxonRank,null);
				logger.debug(lookupRank.getResultState().getLabel());
				if (lookupRank.getResultState()==ResultState.AMENDED) { 
					String ammendedRank = lookupRank.getValue().getObject().get("dwc:taxonRank");
					rankTest = SciNameUtils.isRankAbove("species",ammendedRank);
					if (rankTest==null) { 
						result.addComment("No value provided for specificEpithet, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:specificEpithet should be empty or not.");
						result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
					}
				} else { 
					result.addComment("No value provided for specificEpithet, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:specificEpithet should be empty or not.");
					result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
				}
			} 
			if (rankTest !=null) { 
				if (rankTest==true) { 
					result.addComment("No value provided for specificEpithet, bur taxonRank value is above species indicating that dwc:specificEpithet is correctly empty.");
					result.setValue(ComplianceValue.COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				} else { 
					result.addComment("No value provided for specificEpithet and taxonRank indicates that a value should be present.");
					result.setValue(ComplianceValue.NOT_COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				}
			}
		} else { 
			result.addComment("Some value provided for specificEpithet.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:infraspecificEpithet?
    *
    * Provides: 219 VALIDATION_INFRASPECIFICEPITHET_NOTEMPTY
    * Version: 2024-06-05
    *
    * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate as ActedUpon.
    * @param taxonRank the provided dwc:taxonRank to evaluate as Consulted.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_INFRASPECIFICEPITHET_NOTEMPTY", description="Is there a value in dwc:infraspecificEpithet?")
    @Provides("77c6fde2-c3ca-4ad3-b2f2-3b81bba9a673")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/77c6fde2-c3ca-4ad3-b2f2-3b81bba9a673/2024-06-05")
    @Specification("COMPLIANT if dwc:infraspecificEpithet is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationInfraspecificepithetNotempty(
        @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet,
        @Consulted("dwc:taxonRank") String taxonRank
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:infraspecificEpithet is EMPTY 
        // and dwc:taxonRank contains a value that is not interpretable as a 
        // taxon rank; COMPLIANT if dwc:infraspecificEpithet is not EMPTY, or 
        // dwc:infraspecificEpithet is EMPTY and the value in dwc:taxonRank is 
        // higher than specific epithet; otherwise NOT_COMPLIANT
        
		if (SciNameUtils.isEmpty(infraspecificEpithet)) {
			Boolean rankTest = SciNameUtils.isRankAtOrAbove("species",taxonRank);
			if (rankTest==null) { 
				DQResponse<AmendmentValue> lookupRank = amendmentTaxonrankStandardized(taxonRank,null);
				if (lookupRank.getResultState()==ResultState.AMENDED) { 
					String ammendedRank = lookupRank.getValue().getObject().get("dwc:taxonRank");
					rankTest = SciNameUtils.isRankAtOrAbove("species",ammendedRank);
					if (rankTest==null) { 
						result.addComment("No value provided for infraspecificEpithet, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:infraspecificEpithet should be empty or not.");
						result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
					}
				} else { 
					result.addComment("No value provided for infraspecificEpithet, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:infraspecificEpithet should be empty or not.");
					result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
				}
			} 
			if (rankTest !=null) { 
				if (rankTest==true) { 
					result.addComment("No value provided for infraspecificEpithet, bur taxonRank value is species or above indicating that dwc:infraspecificEpithet is correctly empty.");
					result.setValue(ComplianceValue.COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				} else { 
					result.addComment("No value provided for infraspecificEpithet and taxonRank indicates that a value should be present.");
					result.setValue(ComplianceValue.NOT_COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				}
			}
		} else { 
			result.addComment("Some value provided for infraspecificEpithet.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }
    
    /**
    * Is there a value in dwc:phylum?
    *
    * Provides: 218 VALIDATION_PHYLUM_NOTEMPTY
    * Version: 2024-01-28
    *
    * @param phylum the provided dwc:phylum to evaluate as ActedUpon.
    * @param taxonRank the provided dwc:taxonRank to evaluate as Consulted.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_PHYLUM_NOTEMPTY", description="Is there a value in dwc:phylum?")
    @Provides("19bbd107-6b14-4c82-8d3e-f7a2a67df309")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/19bbd107-6b14-4c82-8d3e-f7a2a67df309/2024-01-28")
    @Specification("COMPLIANT if dwc:phylum is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationPhylumNotempty(
        @ActedUpon("dwc:phylum") String phylum,
        @Consulted("dwc:taxonRank") String taxonRank
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:phylum is EMPTY and 
        // dwc:taxonRank contains a value that is not interpretable as 
        // a taxon rank; COMPLIANT if dwc:phylum is not EMPTY, or 
        // dwc:phylum is EMPTY and the value in dwc:taxonRank is 
        // higher than phylum; otherwise NOT_COMPLIANT.
        // 
        
		if (SciNameUtils.isEmpty(phylum)) {
			Boolean rankTest = SciNameUtils.isRankAbove("phylum",taxonRank);
			if (rankTest==null) { 
				DQResponse<AmendmentValue> lookupRank = amendmentTaxonrankStandardized(taxonRank,null);
				if (lookupRank.getResultState()==ResultState.AMENDED) { 
					String ammendedRank = lookupRank.getValue().getObject().get("dwc:taxonRank");
					rankTest = SciNameUtils.isRankAtOrAbove("phylum",ammendedRank);
					if (rankTest==null) { 
						result.addComment("No value provided for phylum, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:phylum should be empty or not.");
						result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
					}
				} else { 
					result.addComment("No value provided for phylum, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:phylum should be empty or not.");
					result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
				}
			} 
			if (rankTest !=null) { 
				if (rankTest==true) { 
					result.addComment("No value provided for phylum, bur taxonRank value is above Phylum indicating that dwc:phylum is correctly empty.");
					result.setValue(ComplianceValue.COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				} else { 
					result.addComment("No value provided for phylum and taxonRank indicates that a value should be present.");
					result.setValue(ComplianceValue.NOT_COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				}
			}
		} else { 
			result.addComment("Some value provided for phylum.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        return result;
    }

    /**
    * Is there a value in dwc:order?
    *
    * Provides: 217 VALIDATION_ORDER_NOTEMPTY
    * Version: 2024-01-28
    *
    * @param order the provided dwc:order to evaluate as ActedUpon.
    * @param taxonRank the provided dwc:taxonRank to evaluate as Consulted.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_ORDER_NOTEMPTY", description="Is there a value in dwc:order?")
    @Provides("d1c40fc8-d8ad-4148-82e0-b4b7ead70051")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/d1c40fc8-d8ad-4148-82e0-b4b7ead70051/2024-01-28")
    @Specification("COMPLIANT if dwc:order is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationOrderNotempty(
        @ActedUpon("dwc:order") String order,
        @Consulted("dwc:taxonRank") String taxonRank
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:order is EMPTY 
        // and dwc:taxonRank contains a value that is not interpretable 
        // as a taxon rank; COMPLIANT if dwc:order is not EMPTY, 
        // or dwc:order is EMPTY and the value in dwc:taxonRank 
        // is higher than order; otherwise NOT_COMPLIANT.
        // 

		if (SciNameUtils.isEmpty(order)) {
			Boolean rankTest = SciNameUtils.isRankAbove("order",taxonRank);
			if (rankTest==null) { 
				DQResponse<AmendmentValue> lookupRank = amendmentTaxonrankStandardized(taxonRank,null);
				logger.debug(lookupRank.getResultState().getLabel());
				logger.debug(lookupRank.getComment());
				if (lookupRank.getResultState()==ResultState.AMENDED) { 
					String ammendedRank = lookupRank.getValue().getObject().get("dwc:taxonRank");
					rankTest = SciNameUtils.isRankAtOrAbove("order",ammendedRank);
					if (rankTest==null) { 
						result.addComment("No value provided for order, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:order should be empty or not.");
						result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
					}
				} else { 
					result.addComment("No value provided for order, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:order should be empty or not.");
					result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
				}
			} 
			if (rankTest !=null) { 
				if (rankTest==true) { 
					result.addComment("No value provided for order, bur taxonRank value is above Order indicating that dwc:order is correctly empty.");
					result.setValue(ComplianceValue.COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				} else { 
					result.addComment("No value provided for order and taxonRank indicates that a value should be present.");
					result.setValue(ComplianceValue.NOT_COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				}
			}
		} else { 
			result.addComment("Some value provided for order.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:family?
    *
    * Provides: 215 VALIDATION_FAMILY_NOTEMPTY
    * Version: 2024-01-28
    *
    * @param family the provided dwc:family to evaluate as ActedUpon.
    * @param taxonRank the provided dwc:taxonRank to evaluate as Consulted.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_FAMILY_NOTEMPTY", description="Is there a value in dwc:family?")
    @Provides("5bfa043c-1e19-4224-8d55-b568ced347c2")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/5bfa043c-1e19-4224-8d55-b568ced347c2/2024-01-28")
    @Specification("COMPLIANT if dwc:family is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationFamilyNotempty(
        @ActedUpon("dwc:family") String family,
        @Consulted("dwc:taxonRank") String taxonRank
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:family is EMPTY and 
        // dwc:taxonRank contains a value that is not interpretable as 
        // a taxon rank; COMPLIANT if dwc:family is not EMPTY, 
        // or dwc:family is EMPTY and the value in dwc:taxonRank 
        // is higher than family; otherwise NOT_COMPLIANT.
        // 

		if (SciNameUtils.isEmpty(family)) {
			Boolean rankTest = SciNameUtils.isRankAbove("family",taxonRank);
			if (rankTest==null) { 
				DQResponse<AmendmentValue> lookupRank = amendmentTaxonrankStandardized(taxonRank,null);
				if (lookupRank.getResultState()==ResultState.AMENDED) { 
					String ammendedRank = lookupRank.getValue().getObject().get("dwc:taxonRank");
					rankTest = SciNameUtils.isRankAtOrAbove("family",ammendedRank);
					if (rankTest==null) { 
						result.addComment("No value provided for family, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:family should be empty or not.");
						result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
					}
				} else { 
					result.addComment("No value provided for family, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:family should be empty or not.");
					result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
				}
			} 
			if (rankTest !=null) { 
				if (rankTest==true) { 
					result.addComment("No value provided for family, bur taxonRank value is above Family indicating that dwc:family is correctly empty.");
					result.setValue(ComplianceValue.COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				} else { 
					result.addComment("No value provided for family and taxonRank indicates that a value should be present.");
					result.setValue(ComplianceValue.NOT_COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				}
			}
		} else { 
			result.addComment("Some value provided for family.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        
        return result;
    }

    /**
    * Is there a value in dwc:genus?
    *
    * Provides: 214 VALIDATION_GENUS_NOTEMPTY
    * Version: 2024-01-28
    *
    * @param genus the provided dwc:genus to evaluate as ActedUpon.
    * @param taxonRank the provided dwc:taxonRank to evaluate as Consulted.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_GENUS_NOTEMPTY", description="Is there a value in dwc:genus?")
    @Provides("d02c1ffd-af28-49bd-9c9c-e8e23a8b7258")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/d02c1ffd-af28-49bd-9c9c-e8e23a8b7258/2024-06-05")
    @Specification("COMPLIANT if the value in dwc:taxonRank is higher than genus or if dwc:genus is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationGenusNotempty(
        @ActedUpon("dwc:genus") String genus, 
        @Consulted("dwc:taxonRank") String taxonRank
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:genus is EMPTY and dwc:taxonRank 
        // contains a value that is not interpretable as a taxon rank; 
        // COMPLIANT if dwc:genus is not EMPTY, or dwc:genus is EMPTY and 
        // the value in dwc:taxonRank is higher than genus; otherwise NOT_COMPLIANT.

		if (SciNameUtils.isEmpty(genus)) {
			Boolean rankTest = SciNameUtils.isRankAbove("genus",taxonRank);
			if (rankTest==null) { 
				DQResponse<AmendmentValue> lookupRank = amendmentTaxonrankStandardized(taxonRank,null);
				logger.debug(lookupRank.getResultState().getLabel());
				if (lookupRank.getResultState()==ResultState.AMENDED) { 
					String ammendedRank = lookupRank.getValue().getObject().get("dwc:taxonRank");
					rankTest = SciNameUtils.isRankAtOrAbove("genus",ammendedRank);
					if (rankTest==null) { 
						result.addComment("No value provided for genus, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:genus should be empty or not.");
						result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
					}
				} else { 
					result.addComment("No value provided for genus, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:genus should be empty or not.");
					result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
				}
			} 
			if (rankTest !=null) { 
				if (rankTest==true) { 
					result.addComment("No value provided for genus, bur taxonRank value is above Genus indicating that dwc:genus is correctly empty.");
					result.setValue(ComplianceValue.COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				} else { 
					result.addComment("No value provided for genus and taxonRank indicates that a value should be present.");
					result.setValue(ComplianceValue.NOT_COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				}
			}
		} else { 
			result.addComment("Some value provided for genus.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
		
        return result;
    }

    /**
    * Is there a value in dwc:class?
    *
    * Provides: 213 VALIDATION_CLASS_NOTEMPTY
    * Version: 2024-01-28
    *
    * @param taxonomic_class the provided dwc:class to evaluate as ActedUpon.
    * @param taxonRank the provided dwc:taxonRank to evaluate as Consulted.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_CLASS_NOTEMPTY", description="Is there a value in dwc:class?")
    @Provides("b854b179-3572-48b6-aab2-879e3172fd7d")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/b854b179-3572-48b6-aab2-879e3172fd7d/2024-01-28")
    @Specification("COMPLIANT if dwc:class is not EMPTY; otherwise NOT_COMPLIANT ")
    public static DQResponse<ComplianceValue> validationClassNotempty(
        @ActedUpon("dwc:class") String taxonomic_class,
        @Consulted("dwc:taxonRank") String taxonRank
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:class is EMPTY and dwc:taxonRank 
        // contains a value that is not interpretable as a taxon rank; 
        // COMPLIANT if dwc:class is not EMPTY, or dwc:class is EMPTY and 
        // the value in dwc:taxonRank is higher than class; 
        // otherwise NOT_COMPLIANT.
        // 

		if (SciNameUtils.isEmpty(taxonomic_class)) {
			Boolean rankTest = SciNameUtils.isRankAbove("class",taxonRank);
			if (rankTest==null) { 
				DQResponse<AmendmentValue> lookupRank = amendmentTaxonrankStandardized(taxonRank,null);
				if (lookupRank.getResultState()==ResultState.AMENDED) { 
					String ammendedRank = lookupRank.getValue().getObject().get("dwc:taxonRank");
					rankTest = SciNameUtils.isRankAtOrAbove("class",ammendedRank);
					if (rankTest==null) { 
						result.addComment("No value provided for class, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:class should be empty or not.");
						result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
					}
				} else { 
					result.addComment("No value provided for class, but provided value for dwc:taxonRank ["+taxonRank+"] not interpretable to determine if dwc:class should be empty or not.");
					result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
				}
			} 
			if (rankTest !=null) { 
				if (rankTest==true) { 
					result.addComment("No value provided for class, bur taxonRank value is above Class indicating that dwc:class is correctly empty.");
					result.setValue(ComplianceValue.COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				} else { 
					result.addComment("No value provided for class and taxonRank indicates that a value should be present.");
					result.setValue(ComplianceValue.NOT_COMPLIANT);
					result.setResultState(ResultState.RUN_HAS_RESULT);
				}
			}
		} else { 
			result.addComment("Some value provided for class.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}

        return result;
    }

    /**
    * Does the value of dwc:subtribe occur at rank of Subtribe in bdq:sourceAuthority?
    *
    * Provides: 208 VALIDATION_SUBTRIBE_FOUND
    * Version: 2023-09-22
    *
    * @param subtribe the provided dwc:subtribe to evaluate as ActedUpon.
    * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
    *   note that this does not yet provide subtribe data, so the default will never return COMPLIANT.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_SUBTRIBE_FOUND", description="Does the value of dwc:subtribe occur at rank of Subtribe in bdq:sourceAuthority?")
    @Provides("4527c47e-61d9-4abb-af3e-f2999191be17")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/4527c47e-61d9-4abb-af3e-f2999191be17/2023-09-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:subtribe is EMPTY; COMPLIANT if the value of dwc:subtribe was found as a value at the rank of subtribe by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public static DQResponse<ComplianceValue> validationSubtribeFound(
        @ActedUpon("dwc:subtribe") String subtribe,
    	@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:subtribe 
        // is EMPTY; COMPLIANT if the value of dwc:subtribe was found 
        // as a value at the rank of subtribe by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei]
        // NOTE: GBIF backbone taxonomy does not yet include subtribe data

        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage());
			}
        }
        return validateHigherTaxonAtRank(subtribe, "Subtribe", sourceAuthority);
    }

    /**
    * Does the value of dwc:tribe occur at rank of Tribe in bdq:sourceAuthority?
    *
    * Provides: 207 VALIDATION_TRIBE_FOUND
    * Version: 2023-09-22
    *
    * @param tribe the provided dwc:tribe to evaluate as ActedUpon.
    * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
    *   note that this does not yet provide tribe data, so the default will never return COMPLIANT.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_TRIBE_FOUND", description="Does the value of dwc:tribe occur at rank of Tribe in bdq:sourceAuthority?")
    @Provides("8c15f351-26d5-4edd-b38e-07541dc64fd0")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/8c15f351-26d5-4edd-b38e-07541dc64fd0/2023-09-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:tribe is EMPTY; COMPLIANT if the value of dwc:tribe was found as a value at the rank of tribe by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public static DQResponse<ComplianceValue> validationTribeFound(
        @ActedUpon("dwc:tribe") String tribe,
    	@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:tribe 
        // is EMPTY; COMPLIANT if the value of dwc:tribe was found 
        // as a value at the rank of tribe by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei]
        // NOTE: GBIF backbone taxonomy does not yet include tribe data
        
        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage());
			}
        }
        return validateHigherTaxonAtRank(tribe, "Tribe", sourceAuthority);
    }

    /**
    * Does the value of dwc:superfamily occur at rank of Superfamily in bdq:sourceAuthority?
    *
    * Provides: 206 VALIDATION_SUPERFAMILY_FOUND
    * Version: 2023-09-22
    *
    * @param superfamily the provided dwc:superfamily to evaluate as ActedUpon.
    * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
    *   note that this does not yet provide superfamily data, so the default will never return COMPLIANT.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_SUPERFAMILY_FOUND", description="Does the value of dwc:superfamily occur at rank of Superfamily in bdq:sourceAuthority?")
    @Provides("2a45e0e9-446c-429f-992d-c3ec1d29eebb")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/2a45e0e9-446c-429f-992d-c3ec1d29eebb/2023-09-22")
    @Specification("EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:superfamily is EMPTY; COMPLIANT if the value of dwc:superfamily was found as a value at the rank of superfamily by the bdq:sourceAuthority; otherwise NOT_COMPLIANT bdq:sourceAuthority default = 'GBIF Backbone Taxonomy' [https://doi.org/10.15468/39omei],API endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=]")
    public static DQResponse<ComplianceValue> validationSuperfamilyFound(
        @ActedUpon("dwc:superfamily") String superfamily,
    	@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Secification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:superfamily 
        // is EMPTY; COMPLIANT if the value of dwc:superfamily was 
        // found as a value at the rank of superfamily by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei],API 
        // endpoint [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei]
        // NOTE: GBIF backbone taxonomy does not yet include tribe data
        
        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage());
			}
        }
        return validateHigherTaxonAtRank(superfamily, "Superfamily", sourceAuthority);

    }

    
    /**
    * The value of dwc:namePublishedInYear is between 1753-01-01 date and the current date, inclusive
    *
    * Provides: #64 VALIDATION_NAMEPUBLISHEDINYEAR_INRANGE
    * Version: 2024-02-15
    *
    * @param namePublishedInYear the provided dwc:namePublishedInYear to evaluate as ActedUpon.
    * @return DQResponse the response of type ComplianceValue  to return
    */
    @Validation(label="VALIDATION_NAMEPUBLISHEDINYEAR_INRANGE", description="The value of dwc:namePublishedInYear is between 1753-01-01 date and the current date, inclusive")
    @Provides("399ef91d-425c-46f2-a6df-8a0fe4c3e86e")
    @ProvidesVersion("https://rs.tdwg.org/bdq/terms/399ef91d-425c-46f2-a6df-8a0fe4c3e86e/2024-02-15")
    @Specification("INTERNAL_PREREQUISITES_NOT_MET if dwc:namePublishedInYear is EMPTY;  COMPLIANT if the value of dwc:namePublishedInYear is interpretable as a year between 1753 and the current year, inclusive ")
    public static DQResponse<ComplianceValue> validationNamepublishedinyearInrange(
        @ActedUpon("dwc:namePublishedInYear") String namePublishedInYear
    ) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:namePublishedInYear 
        // is EMPTY; COMPLIANT if the value of dwc:namePublishedInYear 
        // is interpretable as a year between 1753 and the current 
        // year, inclusive 

    	Integer lowerBound = 1753;
    	Integer upperBound = LocalDateTime.now().getYear();
    	
    	if (SciNameUtils.isEmpty(namePublishedInYear)) {
    		result.addComment("No value provided for dwc:namePublishedInYear.");
    		result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
    	} else if (namePublishedInYear.trim().length()<4) {
    		result.addComment("Value provided for dwc:namePublishedInYear not interpretable as a year after 1752.");
    		result.setResultState(ResultState.RUN_HAS_RESULT);
    		result.setValue(ComplianceValue.NOT_COMPLIANT);
    	} else { 
    		try { 
    			String testValue = namePublishedInYear.replaceAll("[\\[\\]()?]", "");
    			int numericYear = Integer.parseInt(testValue.trim().substring(0, 4));
    			if (numericYear<lowerBound || numericYear>upperBound) { 
    				result.setValue(ComplianceValue.NOT_COMPLIANT);
    				result.addComment("Provided value for dwc:namePublishedInYear '" + namePublishedInYear + "' is not an integer in the range " + lowerBound.toString() + " to " + upperBound.toString() + " (current year).");
    			} else { 
    				result.setValue(ComplianceValue.COMPLIANT);
    				result.addComment("Provided value for dwc:namePublishedInYear '" + namePublishedInYear + "' is an integer in the range " + lowerBound.toString() + " to " + upperBound.toString() + " (current year).");
    			}
    			result.setResultState(ResultState.RUN_HAS_RESULT);
    		} catch (NumberFormatException e) {
    			logger.debug(e.getMessage());
   				result.setValue(ComplianceValue.NOT_COMPLIANT);
    			result.setResultState(ResultState.RUN_HAS_RESULT);
    			result.addComment("Unable to parse dwc:namePublishedInYear as an integer:" + e.getMessage());
    		}
    	}
        
        return result;
    }

}