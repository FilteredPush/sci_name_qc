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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datakurator.ffdq.annotations.*;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.model.ResultState;
import org.filteredpush.qc.sciname.services.GBIFService;
import org.filteredpush.qc.sciname.services.ServiceException;
import org.filteredpush.qc.sciname.services.Validator;
import org.filteredpush.qc.sciname.services.WoRMSService;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.Rank;
import org.gbif.nameparser.api.UnparsableNameException;
import org.marinespecies.aphia.v1_0.handler.ApiException;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

import org.datakurator.ffdq.api.result.*;

/**
 * Implementation of the TDWG TG2 NAME (scientific name) related data quality tests.
 * 
 * #82 VALIDATION_SCIENTIFICNAME_NOTEMPTY 7c4b9498-a8d9-4ebb-85f1-9f200c788595
 * #120 VALIDATION_TAXONID_NOTEMPTY 401bf207-9a55-4dff-88a5-abcd58ad97fa
 * #161 VALIDATION_TAXONRANK_NOTEMPTY 14da5b87-8304-4b2b-911d-117e3c29e890
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
 *
 * Also, with amendmentTaxonidFromTaxon(taxon, sourceAuthority, replaceExisting) provides
 * a variant of #57 that allows existing taxonID values to be conformed to a specified sourceAuthority
 * based on a lookup of the taxon terms on that authority.
 * 
 * @author mole
 *
 */
@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",
	label="Kurator: Scientific Name Validator - DwCSciNameDQ:v0.0.1")
public class DwCSciNameDQ {
	
	private static final Log logger = LogFactory.getLog(DwCSciNameDQ.class);
	
    public static DQResponse<ComplianceValue> validationPhylumFound(@ActedUpon("dwc:phylum") String phylum) {
    	return validationPhylumFound(phylum, null);
    }

	/**
     * Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?
     *
     * Provides: #22 VALIDATION_PHYLUM_FOUND
     *
     * @param phylum the provided dwc:phylum to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_PHYLUM_FOUND", description="Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?")
    @Provides("eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f")
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
				logger.error(e.getMessage(),e);
			}
        }
        return validateHigherTaxonAtRank(phylum, "Phylum", sourceAuthority);
    }

    public static DQResponse<ComplianceValue> validationFamilyFound(@ActedUpon("dwc:family") String family) {
    	return validationFamilyFound(family, null);
    }
    /**
     * Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?
     * 
     * Provides: #28 VALIDATION_FAMILY_FOUND
     *
     * @param family the provided dwc:family to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_FAMILY_FOUND", description="Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?")
    @Provides("3667556d-d8f5-454c-922b-af8af38f613c")
    public static DQResponse<ComplianceValue> validationFamilyFound(@ActedUpon("dwc:family") String family, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:family 
        // is EMPTY; COMPLIANT if the value of dwc:family was found 
        // as a value at the rank of Family by the bdq:sourceAuthority; 
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
				logger.error(e.getMessage(),e);
			}
        }
        return validateHigherTaxonAtRank(family, "Family", sourceAuthority);
    }

    @Provides("3f335517-f442-4b98-b149-1e87ff16de45")
    public static DQResponse<ComplianceValue> validationScientificnameFound(@ActedUpon("dwc:scientificName") String scientificName) {
        return validationScientificnameFound(scientificName, null);
    }
    
    /**
     * Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?
     *
     * Provides: #46 VALIDATION_SCIENTIFICNAME_FOUND
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_FOUND", description="Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?")
    @Provides("3f335517-f442-4b98-b149-1e87ff16de45")
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
        // NOT_COMPLIANT bdq:sourceAuthority default = "GBIF Backbone 
        // Taxonomy" [https://doi.org/10.15468/39omei], "API endpoint" 
        // [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
        
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
        	} 
        	if (service==null) { 
        		result.addComment("Source Authority Not Implemented.");
        		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	} else { 
        		NameUsage toValidate = new NameUsage();
        		boolean set = false;
        		try { 
        			NameParserGBIF parser = new NameParserGBIF();
        			ParsedName parsedName = parser.parse(scientificName, Rank.UNRANKED, null);
        			toValidate.setScientificName(parsedName.canonicalNameWithoutAuthorship());
        			toValidate.setAuthorship(parsedName.authorshipComplete());
        			logger.debug(parsedName.canonicalNameWithoutAuthorship());
        			logger.debug(parsedName.authorshipComplete());
        			parser.close();
        			set = true;
        		} catch (UnparsableNameException e) { 
        			result.addComment("Unable to parse authorship out of provided scientificName");
        			result.addComment(e.getMessage());
        		} catch (Exception e) {
        			logger.error(e.getMessage(), e);
				}
        		if (!set) { 
        			toValidate.setScientificName(scientificName);
        		}
        		try {
					NameUsage validationResponse = service.validate(toValidate);
					if (validationResponse==null) { 
						result.addComment("No Match found for ["+scientificName+"] in ["+sourceAuthority.getName()+"]");
						result.setResultState(ResultState.RUN_HAS_RESULT);
						result.setValue(ComplianceValue.NOT_COMPLIANT);
					} else { 
						logger.debug(validationResponse.getMatchDescription());
						if (validationResponse.getMatchDescription().equals(NameComparison.MATCH_EXACT)) { 
							result.addComment("Exact Match found for ["+scientificName+"] to ["+validationResponse.getGuid()+"]");
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
	 * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.
	 *
	 * Provides: #57 AMENDMENT_TAXONID_FROM_TAXON
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
	 * @param genericName the provided dwc:genericName to evaluate
	 * @param specificEpithet the provided dwc:specificEpithet to evaluate
	 * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
	 * @param taxonRank the provided dwc:taxonRank to evaluate
	 * @param cultivarEpithet the provided dwc:cultivarEpithet to evaluate
	 * @param higherClassification the provided dwc:higherClassification to evaluate
	 * @param vernacularName the provided dwc:vernacularName to evaluate
	 * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
	 * @param scientificNameID the provided dwc:scientificNameID to evaluate
	 * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
	 * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to evaluate against, defaults to GBIF Backbone Taxonomy if null
	 * @return DQResponse the response of type AmendmentValue to return
	 */
	@Amendment(label="AMENDMENT_TAXONID_FROM_TAXON", description="Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.")
	@Provides("431467d6-9b4b-48fa-a197-cd5379f5e889")
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
			@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
	){
		return amendmentTaxonidFromTaxon(new Taxon(taxonID, kingdom, phylum, taxonomic_class, order, family, subfamily,
				genus, subgenus, scientificName, scientificNameAuthorship, genericName, specificEpithet,
				infraspecificEpithet, taxonRank, cultivarEpithet, higherClassification, vernacularName, taxonConceptID,
				scientificNameID, originalNameUsageID, acceptedNameUsageID), sourceAuthority);
	}

	/**
     * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.
     *
     * Provides: #57 AMENDMENT_TAXONID_FROM_TAXON
     * 
     * @param taxon a dwc:Taxon object to evaluate.
	 * @param sourceAuthority the bdq:sourceAuthority to evaluate against, defaults to GBIF Backbone Taxonomy if null
     *
     * @return DQResponse the response of type AmendmentValue to return
     */
	public static  DQResponse<AmendmentValue> amendmentTaxonidFromTaxon(
			@Parameter(name="dwc:Taxon") Taxon taxon, 
			@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
			){
		return amendmentTaxonidFromTaxon(taxon,sourceAuthority,false);
	}
	
	/**
	 * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority using the available taxon terms.
	 * Includes a non-standard option to conform existing dwc:taxonID values to the sourceAuthority vocabulary.
	 *
	 * Provides: #57 AMENDMENT_TAXONID_FROM_TAXON, but with option of overwriting existing values
	 * 
	 * @param taxon a dwc:Taxon object to evaluate.
	 * @param sourceAuthority the bdq:sourceAuthority to evaluate against, defaults to GBIF Backbone Taxonomy if null
	 * @param replaceExisting, if true, behavior is changed to allow AMENDED to replace existing values instead of INTERNAL_PREREQUISITES_NOT_MET,
	 *    use false to provide the standard behaviour of AMENDMENT_TAXONID_FROM_TAXON
	 *
	 * @return DQResponse the response of type AmendmentValue to return
	 */
	public static  DQResponse<AmendmentValue> amendmentTaxonidFromTaxon(
			Taxon taxon, 
			SciNameSourceAuthority sourceAuthority,
			boolean replaceExisting
			){

		DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

		// Specification
		// INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is not EMPTY 
		// or if all of dwc:scientificName, dwc:genericName, dwc:specificEpithet, 
		// dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
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
		// Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
		// endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
		// 

		// Parameters. This test is defined as parameterized.
		// bdq:sourceAuthority default="GBIF Backbone Taxonomy"

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
		if (!replaceExisting && !SciNameUtils.isEmpty(taxon.getTaxonID())) { 
			result.addComment("dwc:taxonID already contains a value.  [" + taxon.getTaxonID() + "].");
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

			if (SciNameUtils.isEmpty(taxon.getScientificName()) && SciNameUtils.isEmpty(taxon.getGenericName()) && SciNameUtils.isEmpty(taxon.getSpecificEpithet()) &&
					SciNameUtils.isEmpty(taxon.getInfraspecificEpithet()) && SciNameUtils.isEmpty(taxon.getScientificNameAuthorship()) &&
					SciNameUtils.isEmpty(taxon.getCultivarEpithet())
					) {
				result.addComment("No value provided for any of dwc:scientificName, dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet, dwc:scientificNameAuthorship, or dwc:cultivarEpithet.");
				result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
			} else { 
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
					} else { 
						throw new UnsupportedSourceAuthorityException("Source Authority Not Implemented");
					} 
					logger.debug(matchList.size());
					if (matchList.size()>0) {
						boolean hasMatch = false;
						int matchCounter = 0;
						Map<String,String> kvp = new HashMap<String,String>();
						Iterator<NameUsage> i = matchList.iterator();
						while (i.hasNext()) { 
							NameUsage match = i.next();
							logger.debug(match.getCanonicalName());
							if (
									(SciNameUtils.isEqualOrNonEmptyES(taxon.getKingdom(), match.getKingdom())) &&
									(SciNameUtils.isEqualOrNonEmptyES(taxon.getPhylum(), match.getPhylum())) &&
									(SciNameUtils.isEqualOrNonEmptyES(taxon.getTaxonomic_class(), match.getClazz())) &&
									(SciNameUtils.isEqualOrNonEmptyES(taxon.getOrder(), match.getOrder())) &&
									(SciNameUtils.isEqualOrNonEmptyES(taxon.getFamily(), match.getFamily())) &&
									(SciNameUtils.isEqualOrNonEmptyES(taxon.getGenus(), match.getGenus())) &&
									(SciNameUtils.isEqualOrNonEmpty(lookMeUp, match.getCanonicalName())) 
									) 
							{ 
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
									hasMatch=true;
									matchCounter++;
									kvp.put("dwc:taxonID", match.getGuid());
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
								if (kvp.get("dwc:taxonID").equals(taxon.getTaxonID())) { 
									result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ", matching the current value of dwc:taxonID");
									result.setResultState(ResultState.NOT_AMENDED);
								} else  {
									if (replaceExisting) { 
										// **********
										// Non-standard behaviour, allows replacement of existing value
										result.addComment("Exact match to provided taxon found in " + sourceAuthority.getName() + ".");
										AmendmentValue ammendedTaxonID = new AmendmentValue(kvp);
										result.setValue(ammendedTaxonID);
										if (SciNameUtils.isEmpty(taxon.getTaxonID())) { 
											result.setResultState(ResultState.FILLED_IN);
										} else if (taxon.getTaxonID().equals(ammendedTaxonID)) { 
											result.addComment("Returned value for taxonID was the same as the provided value, no change needed.");
											result.setResultState(ResultState.NOT_AMENDED);
										} else { 
											result.addComment("Existing value changed to conform to the specified sourceAuthority " + sourceAuthority.getName());
											result.setResultState(ResultState.AMENDED);
										}
										//  ************
									} else { 
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
				}
			}
		}

		return result;
    }

    /**
     * #70 Validation SingleRecord Conformance: taxon ambiguous
     *
     * Provides: VALIDATION_TAXON_AMBIGUOUS
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param subgenus the provided dwc:subgenus to evaluate
     * @param higherClassification the provided dwc:higherClassification to evaluate
     * @param vernacularName the provided dwc:vernacularName to evaluate
     * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
     * @param scientificNameAuthorship the provided dwc:scientificNameAuthorship to evaluate
     * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param family the provided dwc:family to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("4c09f127-737b-4686-82a0-7c8e30841590")
    public DQResponse<ComplianceValue> validationTaxonAmbiguous(@ActedUpon("dwc:class") String taxonomic_class, @ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:taxonConceptID") String taxonConceptID, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:scientificNameID") String scientificNameID, @ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:subgenus") String subgenus, @ActedUpon("dwc:higherClassification") String higherClassification, @ActedUpon("dwc:vernacularName") String vernacularName, @ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, @ActedUpon("dwc:scientificNameAuthorship") String scientificNameAuthorship, @ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, @ActedUpon("dwc:taxonRank") String taxonRank, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet, @ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName, dwc:subgenus, 
        // dwc:genus, dwc:family, dwc:order, dwc:class, dwc:phylum, 
        // dwc:kingdom are EMPTY, COMPLIANT if the combination of values 
        // of dwc:Taxon terms (dwc:scientificName, dwc:scientificNameAuthorship, 
        // dwc:subgenus, dwc:genus, dwc:family, dwc:order, dwc:class, 
        // dwc:phylum, dwc:kingdom, dwc:taxonRank) can be unambiguously 
        // resolved by the specified source authority service; otherwise 
        //NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }
    
    public static DQResponse<AmendmentValue> amendmentScientificnameFromTaxonid(
    		@Consulted("dwc:taxonID") String taxonID, 
    		@ActedUpon("dwc:scientificName") String scientificName
    	) {
    	return amendmentScientificnameFromTaxonid(taxonID, scientificName, null);
    }

    /**
     * Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority.
     *
     * Provides: #71 AMENDMENT_SCIENTIFICNAME_FROM_TAXONID
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_SCIENTIFICNAME_FROM_TAXONID", description="Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority.")
    @Provides("f01fb3f9-2f7e-418b-9f51-adf50f202aea")
    public static DQResponse<AmendmentValue> amendmentScientificnameFromTaxonid(
    		@Consulted("dwc:taxonID") String taxonID, 
    		@ActedUpon("dwc:scientificName") String scientificName,
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority
    	) {
    	
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID 
        // is EMPTY, the value of dwc:taxonID is ambiguous or dwc:scientificName 
        // was not EMPTY; FILLED_IN the value of dwc:scientificName 
        // if the value of dwc:taxonID could be unambiguously interpreted 
        // as a value in bdq:sourceAuthority; otherwise NOT_AMENDED 
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei], 
        // "API endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
        
        if (sourceAuthority==null) { 
        	try {
				sourceAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
			} catch (SourceAuthorityException e) {
				logger.error(e.getMessage(),e);
			}
        }
        if (SciNameUtils.isEmpty(taxonID)) { 
			result.addComment("dwc:taxonID does not contains a value.");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else if (!SciNameUtils.isEmpty(scientificName)) { 
			result.addComment("dwc:scientificName already contains a value ["+ scientificName +"].");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	
        }

        return result;
        
    }

    public static DQResponse<ComplianceValue> validationClassFound(@ActedUpon("dwc:class") String taxonomic_class) {
    	return validationClassFound(taxonomic_class, null);
    }
    
    /**
     * Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?
     *
     * Provides: #77 VALIDATION_CLASS_FOUND
     *
     * @param taxonomic_class the provided dwc:class to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASS_FOUND", description="Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?")
    @Provides("2cd6884e-3d14-4476-94f7-1191cfff309b")
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
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

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

    public static DQResponse<ComplianceValue> validationKingdomFound(@ActedUpon("dwc:kingdom") String kingdom) {
        return validationKingdomFound(kingdom, null);
    }
    
    /**
     * Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?
     *
     * Provides: #81 VALIDATION_KINGDOM_FOUND
     *
     * @param kingdom the provided dwc:kingdom to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_KINGDOM_FOUND", description="Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?")
    @Provides("125b5493-052d-4a0d-a3e1-ed5bf792689e")
    public static DQResponse<ComplianceValue> validationKingdomFound(@ActedUpon("dwc:kingdom") String kingdom, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:kingdom 
        // is EMPTY; COMPLIANT if the value of dwc:kingdom was found 
        // as a value at the rank of kingdom by the bdq:sourceAuthority; 
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
				logger.error(e.getMessage(),e);
			}
        }
        return validateHigherTaxonAtRank(kingdom, "Kingdom", sourceAuthority);
    }

    /**
     * Is there a value in dwc:scientificName?
     *
     * Provides: #82 VALIDATION_SCIENTIFICNAME_NOTEMPTY
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_NOTEMPTY", description="Is there a value in dwc:scientificName?")
    @Provides("7c4b9498-a8d9-4ebb-85f1-9f200c788595")
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

    public static DQResponse<ComplianceValue> validationOrderFound(@ActedUpon("dwc:order") String order) {
    	return validationOrderFound(order,null);
    }
    /**
     * Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?
     *
     * Provides: #83 VALIDATION_ORDER_FOUND
     *
     * @param order the provided dwc:order to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_ORDER_FOUND", description="Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?")
    @Provides("81cc974d-43cc-4c0f-a5e0-afa23b455aa3")
    public static DQResponse<ComplianceValue> validationOrderFound(@ActedUpon("dwc:order") String order,  
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:order 
        // is EMPTY; COMPLIANT if the value of dwc:order was found 
        // as a value at the rank of Order by the bdq:sourceAuthority; 
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
     * #101 Validation SingleRecord Consistency: polynomial inconsistent
     *
     * Provides: VALIDATION_POLYNOMIAL_INCONSISTENT
     *
     * @param genus the provided dwc:genus to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("17f03f1f-f74d-40c0-8071-2927cfc9487b")
    public DQResponse<ComplianceValue> validationPolynomialInconsistent(@ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // INTERNAL_PREREQUISITES_NOT_MET if all of the component terms 
        // are EMPTY; COMPLIANT if the polynomial, as represented in 
        // dwc:scientificName, is consistent with the atomic parts 
        // dwc:genus, dwc:specificEpithet, dwc:infraspecificEpithet; 
        //otherwise NOT_COMPLIANT 

        return result;
    }

    /**
     * Is there a value in any of the terms needed to determine that the taxon exists?
     *
     * Provides: #105 VALIDATION_TAXON_NOTEMPTY
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
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("06851339-843f-4a43-8422-4e61b9a00e75")
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
    		@ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // TODO: Specification needs work, higher taxon terms shouldn't be included.
        
        // Specification
        // COMPLIANT if at least one term needed to determine the taxon 
        // of the entity exists and is not EMPTY; otherwise NOT_COMPLIANT 
        //

		if (SciNameUtils.isEmpty(taxonID) &&
			SciNameUtils.isEmpty(kingdom) &&
			SciNameUtils.isEmpty(phylum) &&
			SciNameUtils.isEmpty(taxonomic_class) &&
			SciNameUtils.isEmpty(order) &&
			SciNameUtils.isEmpty(family) &&
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
			SciNameUtils.isEmpty(vernacularName)
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
     * Is there a value in dwc:taxonID?
     *
     * Provides: #120 VALIDATION_TAXONID_NOTEMPTY
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONID_NOTEMPTY", description="Is there a value in dwc:taxonID?")
    @Provides("401bf207-9a55-4dff-88a5-abcd58ad97fa")
    public static DQResponse<ComplianceValue> validationTaxonidNotempty(@ActedUpon("dwc:taxonID") String taxonID) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // COMPLIANT if dwc:taxonID is not EMPTY; otherwise NOT_COMPLIANT 
        //

		if (SciNameUtils.isEmpty(taxonID)) {
			result.addComment("No value provided for taxonID.");
			result.setValue(ComplianceValue.NOT_COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		} else { 
			result.addComment("Some value provided for taxonID.");
			result.setValue(ComplianceValue.COMPLIANT);
			result.setResultState(ResultState.RUN_HAS_RESULT);
		}
        return result;
    }

    /**
     * Does the value of dwc:taxonID contain both a URI and namespace indicator?
     *
     * Provides: #121 VALIDATION_TAXONID_COMPLETE
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONID_COMPLETE", description="Does the value of dwc:taxonID contain both a URI and namespace indicator?")
    @Provides("a82c7e3a-3a50-4438-906c-6d0fefa9e984")
    public static DQResponse<ComplianceValue> validationTaxonidComplete(@ActedUpon("dwc:taxonID") String taxonID) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO: Specification needs work.
        // something like COMPLIANT if taxonID is a validly formed LSID, or taxonID
        // is a validly formed URN with at least NID and NSS, or taxonID is a 
        // validly formed URI with host and path where path consists of 
        // more than just "/", and if host is www.gbif.org and path begins with 
        // "/species/", the path contains additional trailing characters, otherwise
        // NOT_COMPLIANT
        
        //TODO:  Implement specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY; 
        // COMPLIANT if dwc:taxonID contains both a URI and a namespace 
        // indicator; otherwise NOT_COMPLIANT 

        
        if (SciNameUtils.isEmpty(taxonID)) { 
        	result.addComment("No value provided for taxonId.");
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
					result.addComment("Provided value for taxonID ["+taxonID+"] is not a URN or a URI.");
					result.setResultState(ResultState.RUN_HAS_RESULT);
					result.setValue(ComplianceValue.NOT_COMPLIANT);
				}
        	}
        	
        }
        return result;
    }

    public static DQResponse<ComplianceValue> validationGenusFound(@ActedUpon("dwc:genus") String genus) {
    	return validationGenusFound(genus, null);
    }

    /**
     * Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?
     *
     * Provides: #122 VALIDATION_GENUS_FOUND
     *
     * @param genus the provided dwc:genus to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_GENUS_FOUND", description="Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?")
    @Provides("f2ce7d55-5b1d-426a-b00e-6d4efe3058ec")
    public static DQResponse<ComplianceValue> validationGenusFound(@ActedUpon("dwc:genus") String genus, 
    		@Parameter(name="bdq:sourceAuthority") SciNameSourceAuthority sourceAuthority) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:genus 
        // is EMPTY; COMPLIANT if the value of dwc:genus was found 
        // as a value at the rank of genus by the bdq:sourceAuthority; 
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
        	} else { 
        		result.addComment("Source Authority Not Implemented.");
        		result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        	} 
        }
		
        return result;
    }

    /**
     * #123 Validation SingleRecord Conformance: classification ambiguous
     *
     * Provides: VALIDATION_CLASSIFICATION_AMBIGUOUS
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param family the provided dwc:family to evaluate
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("78640f09-8353-411a-800e-9b6d498fb1c9")
    public DQResponse<ComplianceValue> validationClassificationAmbiguous(@ActedUpon("dwc:class") String taxonomic_class, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if all of the fields dwc:kingdom dwc:phylum, dwc:class, 
        // dwc:order, dwc:family are EMPTY; COMPLIANT if the combination 
        // of values of higher classification taxonomic terms (dwc:kingdom, 
        // dwc:phylum, dwc:class, dwc:order, dwc:family) can be unambiguously 
        // resolved by the specified source authority service; otherwise 
        //NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * Is there a value in dwc:taxonRank?
     *
     * Provides: #161 VALIDATION_TAXONRANK_NOTEMPTY
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_TAXONRANK_NOTEMPTY", description="Is there a value in dwc:taxonRank?")
    @Provides("14da5b87-8304-4b2b-911d-117e3c29e890")
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
     * #162 Validation SingleRecord Conformance: taxonrank notstandard
     *
     * Provides: VALIDATION_TAXONRANK_NOTSTANDARD
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("7bdb13a4-8a51-4ee5-be7f-20693fdb183e")
    public DQResponse<ComplianceValue> validationTaxonrankNotstandard(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:taxonRank is EMPTY; COMPLIANT if the value of dwc:taxonRank 
        // is in the bdq:sourceAuthority; otherwise NOT_COMPLIANT. 
        //

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * #163 Amendment SingleRecord Conformance: taxonrank standardized
     *
     * Provides: AMENDMENT_TAXONRANK_STANDARDIZED
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
	 * @param sourceAuthority the bdq:sourceAuthority to consult, defaults to GBIF Backbone Taxonomy if null
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Provides("e39098df-ef46-464c-9aef-bcdeee2a88cb")
    public DQResponse<AmendmentValue> amendmentTaxonrankStandardized(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; AMENDED if the value of dwc:taxonRank 
        // was standardized using the bdq:sourceAuthority service; 
        //otherwise NOT_AMENDED 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority
        
        // NOTES: Default sourceAuthority is https://rs.gbif.org/vocabulary/gbif/rank.xml
        //  meaning that "using" must be interpreted as "to a value in", as this is a static xml 
        //  document not a service which itself can perform transformations
        
        

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
        	sourceAuthority = new SciNameSourceAuthority();
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
        				result.addComment("No exact match to provided " + rank + " found in GBIF backbone taxonomy at rank " + rank + ".");
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
        				result.addComment("No exact match to provided " + rank +  " found  " + sourceAuthority.getAuthority().getName() + " at rank " + rank + ".");
        				result.setValue(ComplianceValue.NOT_COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}        		
        	} else if (sourceAuthority.getAuthority().equals(EnumSciNameSourceAuthority.WORMS)) {
        		try {
        			List<NameUsage> matches = WoRMSService.lookupTaxonAtRank(taxon,rank);
        			if (matches.size()>0) { 
        				result.addComment("Exact match to provided "+rank+" found in WoRMS at rank " + rank + ".");
        				result.setValue(ComplianceValue.COMPLIANT);
        				result.setResultState(ResultState.RUN_HAS_RESULT);
        			} else { 
        				result.addComment("No exact match to provided " + rank + " found in WoRMS at rank " + rank + ".");
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

}
