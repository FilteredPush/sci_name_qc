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
import org.filteredpush.qc.sciname.services.WoRMSService;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

import org.datakurator.ffdq.api.result.*;

/**
 * Implementation of the TDWG TG2 NAME (scientific name) related data quality tests.
 * 
 * @author mole
 *
 */
@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",
	label="Kurator: Scientific Name Validator - DwCSciNameDQ:v0.0.1")
public class DwCSciNameDQ {
	
	private static final Log logger = LogFactory.getLog(DwCSciNameDQ.class);
	
	private EnumSciNameSourceAuthority sourceAuthority = EnumSciNameSourceAuthority.GBIF;
	
	/** 
	 * Construct an instance of DwCSciNameDQ using the default (GBIF backbone) source authority.
	 * 
	 */
	public DwCSciNameDQ() {
		super();
		this.sourceAuthority = EnumSciNameSourceAuthority.GBIF;
	}
	
    /**
     * Construct an instance of DwCSciNameDQ with a specified bdq:sourceAuthority.
     * 
	 * @param sourceAuthority the source authority to use in invocations of the class instance.
	 */
	public DwCSciNameDQ(EnumSciNameSourceAuthority sourceAuthority) {
		super();
		this.sourceAuthority = sourceAuthority;
	}

	/**
	 * 
	 * @return the source authority used by the instance.
	 */
	public EnumSciNameSourceAuthority getSourceAuthority() {
		return sourceAuthority;
	}

	/**
     * #22 Validation SingleRecord Conformance: phylum notfound
     *
     * Provides: VALIDATION_PHYLUM_NOTFOUND
     *
     * @param phylum the provided dwc:phylum to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f")
    public DQResponse<ComplianceValue> validationPhylumNotfound(@ActedUpon("dwc:phylum") String phylum) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:phylum is EMPTY; COMPLIANT if the value of dwc:phylum 
        // was found as a value at the rank of phylum by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * #28 Validation SingleRecord Conformance: family notfound
     *
     * Provides: VALIDATION_FAMILY_NOTFOUND
     *
     * @param family the provided dwc:family to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("3667556d-d8f5-454c-922b-af8af38f613c")
    public DQResponse<ComplianceValue> validationFamilyNotfound(@ActedUpon("dwc:family") String family) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:family is EMPTY; COMPLIANT if the value of dwc:family 
        // was found as a value at the rank of family by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * #46 Validation SingleRecord Conformance: scientificname notfound
     *
     * Provides: VALIDATION_SCIENTIFICNAME_NOTFOUND
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("3f335517-f442-4b98-b149-1e87ff16de45")
    public DQResponse<ComplianceValue> validationScientificnameNotfound(@ActedUpon("dwc:scientificName") String scientificName) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:scientificName is EMPTY; COMPLIANT if there is a 
        // match of the contents of dwc:scientificName with the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * #57 Amendment SingleRecord Conformance: taxonid from taxon
     *
     * Provides: AMENDMENT_TAXONID_FROM_TAXON
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param kingdom the provided dwc:kingdom to evaluate
     * @param phylum the provided dwc:phylum to evaluate
     * @param order the provided dwc:order to evaluate
     * @param taxonomic_class the provided dwc:class to evaluate
     * @param family the provided dwc:family to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param subgenus the provided dwc:subgenus to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param scientificNameAuthorship the provided dwc:scientificNameAuthorship to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @param higherClassification the provided dwc:higherClassification to evaluate
     * @param vernacularName the provided dwc:vernacularName to evaluate
     * @param taxonConceptID the provided dwc:taxonConceptID to evaluate
     * @param scientificNameID the provided dwc:scientificNameID to evaluate
     * @param originalNameUsageID the provided dwc:originalNameUsageID to evaluate
     * @param acceptedNameUsageID the provided dwc:acceptedNameUsageID to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Provides("431467d6-9b4b-48fa-a197-cd5379f5e889")
    public DQResponse<AmendmentValue> amendmentTaxonidFromTaxon(
    		@ActedUpon("dwc:taxonID") String taxonID, 
    		@Consulted("dwc:kingdom") String kingdom, 
    		@Consulted("dwc:phylum") String phylum, 
    		@Consulted("dwc:class") String taxonomic_class, 
    		@Consulted("dwc:order") String order,
    		@Consulted("dwc:family") String family, 
    		@Consulted("dwc:genus") String genus, 
    		@Consulted("dwc:subgenus") String subgenus, 
    		@Consulted("dwc:scientificName") String scientificName, 
    		@Consulted("dwc:scientificNameAuthorship") String scientificNameAuthorship, 
    		@Consulted("dwc:specificEpithet") String specificEpithet,
    		@Consulted("dwc:infraspecificEpithet") String infraspecificEpithet, 
    		@Consulted("dwc:taxonRank") String taxonRank, 
    		@Consulted("dwc:higherClassification") String higherClassification, 
    		@Consulted("dwc:vernacularName") String vernacularName, 
    		@Consulted("dwc:taxonConceptID") String taxonConceptID, 
    		@Consulted("dwc:scientificNameID") String scientificNameID, 
    		@Consulted("dwc:originalNameUsageID") String originalNameUsageID, 
    		@Consulted("dwc:acceptedNameUsageID") String acceptedNameUsageID
    ){
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if all of dwc:kingdom, dwc:phylum, dwc:class, dwc:order, 
        // dwc:family, dwc:genus, and dwc:scientificName are EMPTY; 
        // AMENDED if a value for dwc:taxonID is unique and resolvable 
        // on the basis of the value of the lowest ranking not EMPTY 
        // taxon classification terms dwc:scientificName, dwc:scientificNameAuthorship, 
        // dwc:kingdom, dwc:phylum, dwc:class, etc.; otherwise NOT_CHANGED 
        //

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        if (SciNameUtils.isEmpty(kingdom) && SciNameUtils.isEmpty(phylum) && SciNameUtils.isEmpty(taxonomic_class) &&
        		SciNameUtils.isEmpty(order) && SciNameUtils.isEmpty(family) && SciNameUtils.isEmpty(genus) &&
        		SciNameUtils.isEmpty(scientificName)
        		) {
			result.addComment("No value provided for any of kingdom, phylum, class, order, family, genus and scientificName.");
			result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	
        	if (this.sourceAuthority.equals(EnumSciNameSourceAuthority.GBIF)) { 
        		try {
        			String matches = GBIFService.searchForTaxon(scientificName, GBIFService.KEY_GBIFBACKBONE);
        			logger.debug(matches);
        			List<NameUsage> matchList = GBIFService.parseAllNameUsagesFromJSON(matches);
        			logger.debug(matchList.size());
        			if (matchList.size()>0) {
        				boolean hasMatch = false;
        				int matchCounter = 0;
        				Map<String,String> kvp = new HashMap<String,String>();
        				Iterator<NameUsage> i = matchList.iterator();
        				while (i.hasNext()) { 
        					NameUsage match = i.next();
        					if (
        							(SciNameUtils.isEqualOrNonEmpty(kingdom, match.getKingdom())) &&
        							(SciNameUtils.isEqualOrNonEmpty(phylum, match.getPhylum())) &&
        							(SciNameUtils.isEqualOrNonEmpty(taxonomic_class, match.getClazz())) &&
        							(SciNameUtils.isEqualOrNonEmpty(order, match.getOrder())) &&
        							(SciNameUtils.isEqualOrNonEmpty(family, match.getFamily())) &&
        							(SciNameUtils.isEqualOrNonEmpty(genus, match.getGenus())) &&
        							(SciNameUtils.isEqualOrNonEmpty(scientificName, match.getCanonicalName())) 
        						) 
        					{ 
        						logger.debug(match.getCanonicalName());
        						logger.debug(match.getAuthorship());
        						logger.debug(match.getAuthorshipStringSimilarity());
        						boolean authorshipOK = false;
        						if (!SciNameUtils.isEmpty(scientificNameAuthorship)) { 
        							if (match.getAuthorComparator()==null) { 
        								match.setAuthorComparator(AuthorNameComparator.authorNameComparatorFactory(scientificNameAuthorship, kingdom));
        							}
        							logger.debug(match.getAuthorComparator().compare(scientificNameAuthorship, match.getAuthorship()).getMatchType());
        							NameComparison authorshipComparison = match.getAuthorComparator().compare(scientificNameAuthorship, match.getAuthorship());
        							if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACT)) {
        								result.addComment("Match for provided taxon in GBIF backbone taxonomy with exact match on authorship. ");
        								authorshipOK = true;
        							} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACTADDSYEAR)) {
        								result.addComment("Match for provided taxon in GBIF backbone taxonomy with similar author: " + authorshipComparison.getRemark());
        								authorshipOK = true;
        							} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_EXACTMISSINGYEAR)) {
        								result.addComment("Match for provided taxon in GBIF backbone taxonomy with similar author: " + authorshipComparison.getRemark());
        								authorshipOK = true;
        							} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_SOWERBYEXACTYEAR)) {
        								result.addComment("Match for provided taxon in GBIF backbone taxonomy with similar author: " + authorshipComparison.getRemark());
        								authorshipOK = true;
        							} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_L_EXACTYEAR)) {
        								result.addComment("Match for provided taxon in GBIF backbone taxonomy with similar author: " + authorshipComparison.getRemark());
        								authorshipOK = true;
        							} else if (authorshipComparison.getMatchType().equals(NameComparison.MATCH_SAMEBUTABBREVIATED)) {
        								result.addComment("Match for provided taxon in GBIF backbone taxonomy with similar author: " + authorshipComparison.getRemark());
        								authorshipOK = true;
        							} else {
        								result.addComment("Match for provided taxon in GBIF backbone taxonomy has different author: " + authorshipComparison.getRemark());
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
        						result.addComment("More than one exact match found for provided taxon in GBIF backbone taxonomy.");
        						result.setResultState(ResultState.NO_CHANGE);
        					} else { 
        						result.addComment("Exact match to provided taxon found in GBIF backbone taxonomy.");
        						AmendmentValue ammendedTaxonID = new AmendmentValue(kvp);
        						result.setValue(ammendedTaxonID);
        						result.setResultState(ResultState.CHANGED);
        					}
        				} else { 
        					result.addComment("No exact match found for provided taxon in GBIF backbone taxonomy.");
        					result.setResultState(ResultState.NO_CHANGE);
        				}
        			} else { 
        				result.addComment("No match found for provided taxon in GBIF backbone taxonomy.");
        				result.setResultState(ResultState.NO_CHANGE);
        			}
        		} catch (IOException e) {
        			result.addComment("GBIF API not available:" + e.getMessage());
        			result.setResultState(ResultState.EXTERNAL_PREREQUISITES_NOT_MET);
        		}
        	} else if (this.sourceAuthority.equals(EnumSciNameSourceAuthority.WORMS)) {
        		try {
        			List<NameUsage> matches = WoRMSService.lookupGenus(genus);
        			// TODO: Implement
        		} catch (IOException e) {
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

    /**
     * #71 Amendment SingleRecord Completeness: scientificname from taxonid
     *
     * Provides: AMENDMENT_SCIENTIFICNAME_FROM_TAXONID
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Provides("f01fb3f9-2f7e-418b-9f51-adf50f202aea")
    public DQResponse<AmendmentValue> amendmentScientificnameFromTaxonid(@ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:scientificName") String scientificName) {
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:taxonID is EMPTY, the value of dwc:taxonID is ambiguous 
        // or dwc:scientificName was not EMPTY; AMENDED if dwc:scientificName 
        // was added from a successful lookup of dwc:taxonID in the 
        //bdq:sourceAuthority; otherwise NOT_CHANGED 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * #77 Validation SingleRecord Conformance: class notfound
     *
     * Provides: VALIDATION_CLASS_NOTFOUND
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("2cd6884e-3d14-4476-94f7-1191cfff309b")
    public DQResponse<ComplianceValue> validationClassNotfound(@ActedUpon("dwc:class") String taxonomic_class) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:class is EMPTY; COMPLIANT if the value of dwc:class 
        // was found as a value at the rank of class by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * #81 Validation SingleRecord Conformance: kingdom notfound
     *
     * Provides: VALIDATION_KINGDOM_NOTFOUND
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("125b5493-052d-4a0d-a3e1-ed5bf792689e")
    public DQResponse<ComplianceValue> validationKingdomNotfound(@ActedUpon("dwc:kingdom") String kingdom) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:kingdom is EMPTY; COMPLIANT if the value of dwc:kingdom 
        // was found as a value at the rank of kingdom by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

    /**
     * #82 Validation SingleRecord Completeness: scientificname empty
     *
     * Provides: VALIDATION_SCIENTIFICNAME_EMPTY
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("urn:uuid:7c4b9498-a8d9-4ebb-85f1-9f200c788595")
    public static DQResponse<ComplianceValue> validationScientificnameEmpty(@ActedUpon("dwc:scientificName") String scientificName) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // COMPLIANT if dwc:scientificName is not EMPTY; otherwise 
        //NOT_COMPLIANT 

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
     * #83 Validation SingleRecord Conformance: order notfound
     *
     * Provides: VALIDATION_ORDER_NOTFOUND
     *
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("81cc974d-43cc-4c0f-a5e0-afa23b455aa3")
    public DQResponse<ComplianceValue> validationOrderNotfound(@ActedUpon("dwc:order") String order) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:order is EMPTY; COMPLIANT if the value of dwc:order 
        // was found as a value at the rank of order by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

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
     * #105 Validation SingleRecord Completeness: taxon empty
     *
     * Provides: VALIDATION_TAXON_EMPTY
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @param genus the provided dwc:genus to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
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
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param order the provided dwc:order to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("06851339-843f-4a43-8422-4e61b9a00e75")
    public static DQResponse<ComplianceValue> validationTaxonEmpty(@ActedUpon("dwc:class") String taxonomic_class, @ActedUpon("dwc:genus") String genus, @ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet, @ActedUpon("dwc:taxonConceptID") String taxonConceptID, @ActedUpon("dwc:phylum") String phylum, @ActedUpon("dwc:scientificNameID") String scientificNameID, @ActedUpon("dwc:taxonID") String taxonID, @ActedUpon("dwc:parentNameUsageID") String parentNameUsageID, @ActedUpon("dwc:subgenus") String subgenus, @ActedUpon("dwc:higherClassification") String higherClassification, @ActedUpon("dwc:vernacularName") String vernacularName, @ActedUpon("dwc:originalNameUsageID") String originalNameUsageID, @ActedUpon("dwc:acceptedNameUsageID") String acceptedNameUsageID, @ActedUpon("dwc:kingdom") String kingdom, @ActedUpon("dwc:family") String family, @ActedUpon("dwc:scientificName") String scientificName, @ActedUpon("dwc:specificEpithet") String specificEpithet, @ActedUpon("dwc:order") String order) {
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
			SciNameUtils.isEmpty(family) &&
			SciNameUtils.isEmpty(genus) &&
			SciNameUtils.isEmpty(subgenus) &&
			SciNameUtils.isEmpty(scientificName) &&
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
     * #120 Validation SingleRecord Completeness: taxonid empty
     *
     * Provides: VALIDATION_TAXONID_EMPTY
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("urn:uuid:401bf207-9a55-4dff-88a5-abcd58ad97fa")
    public static DQResponse<ComplianceValue> validationTaxonidEmpty(@ActedUpon("dwc:taxonID") String taxonID) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

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
     * #121 Validation SingleRecord Conformance: taxonid ambiguous
     *
     * Provides: VALIDATION_TAXONID_AMBIGUOUS
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("a82c7e3a-3a50-4438-906c-6d0fefa9e984")
    public DQResponse<ComplianceValue> validationTaxonidAmbiguous(@ActedUpon("dwc:taxonID") String taxonID) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the GBIF backbone taxonomy 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:taxonID is EMPTY or does not include the resolving 
        // authority; COMPLIANT if the value of dwc:taxonID is resolvable; 
        //otherwise NOT_COMPLIANT 

        return result;
    }

    /**
     * #122 Validation SingleRecord Conformance: genus notfound
     *
     * Provides: VALIDATION_GENUS_NOTFOUND
     *
     * @param genus the provided dwc:genus to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("3667556d-d8f5-454c-922b-af8af38f613c")
    public DQResponse<ComplianceValue> validationGenusNotfound(@ActedUpon("dwc:genus") String genus) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();

        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:genus is EMPTY; COMPLIANT if the value of dwc:genus 
        // was found as a value at the rank of genus by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority
        if (SciNameUtils.isEmpty(genus)) { 
        	result.addComment("No value provided for genus.");
        	result.setResultState(ResultState.INTERNAL_PREREQUISITES_NOT_MET);
        } else { 
        	if (this.sourceAuthority.equals(EnumSciNameSourceAuthority.GBIF)) { 
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
        	} else if (this.sourceAuthority.equals(EnumSciNameSourceAuthority.WORMS)) {
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
        		} catch (IOException e) {
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
     * #161 Validation SingleRecord Completeness: taxonrank empty
     *
     * Provides: VALIDATION_TAXONRANK_EMPTY
     *
     * @param taxonRank the provided dwc:taxonRank to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Provides("urn:uuid:14da5b87-8304-4b2b-911d-117e3c29e890")
    public static DQResponse<ComplianceValue> validationTaxonrankEmpty(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<ComplianceValue> result = new DQResponse<ComplianceValue>();
        
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
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Provides("e39098df-ef46-464c-9aef-bcdeee2a88cb")
    public DQResponse<AmendmentValue> amendmentTaxonrankStandardized(@ActedUpon("dwc:taxonRank") String taxonRank) {
        DQResponse<AmendmentValue> result = new DQResponse<AmendmentValue>();

        //TODO:  Implement specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; AMENDED if the value of dwc:taxonRank 
        // was standardized using the bdq:sourceAuthority service; 
        //otherwise NOT_CHANGED 

        //TODO: Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority

        return result;
    }

}
