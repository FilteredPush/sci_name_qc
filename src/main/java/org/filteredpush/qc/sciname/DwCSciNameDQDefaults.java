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
import org.gbif.nameparser.api.NameParser;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.Rank;
import org.gbif.nameparser.api.UnparsableNameException;
import org.marinespecies.aphia.v1_0.handler.ApiException;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

import org.datakurator.ffdq.api.result.*;

/**
 * Implementation of the TDWG TG2 NAME (scientific name) related data quality tests,
 * using default values for parameters.
 * 
 * #82 VALIDATION_SCIENTIFICNAME_NOTEMPTY 7c4b9498-a8d9-4ebb-85f1-9f200c788595
 * #120 VALIDATION_TAXONID_NOTEMPTY 401bf207-9a55-4dff-88a5-abcd58ad97fa
 * #161 VALIDATION_TAXONRANK_NOTEMPTY 14da5b87-8304-4b2b-911d-117e3c29e890
 * #105 VALIDATION_TAXON_NOTEMPTY ** needs work **
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
 * @author mole
 *
 */
@Mechanism(value="90516df7-838c-4d53-81d9-8131be6ac713",
	label="Kurator: Scientific Name Validator - DwCSciNameDQ:v0.0.1")
public class DwCSciNameDQDefaults extends DwCSciNameDQ {
	
	private static final Log logger = LogFactory.getLog(DwCSciNameDQDefaults.class);
	
	/**
     * Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #22 VALIDATION_PHYLUM_FOUND
     *
     * @param phylum the provided dwc:phylum to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_PHYLUM_FOUND", description="Does the value of dwc:phylum occur at rank of Phylum in bdq:sourceAuthority?")
    @Provides("eaad41c5-1d46-4917-a08b-4fd1d7ff5c0f")
    public static DQResponse<ComplianceValue> validationPhylumFound(@ActedUpon("dwc:phylum") String phylum) {
    	return validationPhylumFound(phylum, null);
    }

    /**
     * Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     * 
     * Provides: #28 VALIDATION_FAMILY_FOUND
     *
     * @param family the provided dwc:family to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_FAMILY_FOUND", description="Does the value of dwc:family occur at rank of Family in bdq:sourceAuthority?")
    @Provides("3667556d-d8f5-454c-922b-af8af38f613c")
    public static DQResponse<ComplianceValue> validationFamilyFound(@ActedUpon("dwc:family") String family) {
    	return validationFamilyFound(family, null);
    }

    /**
     * Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #46 VALIDATION_SCIENTIFICNAME_FOUND
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_SCIENTIFICNAME_FOUND", description="Is there a match of the contents of dwc:scientificName with bdq:sourceAuthority?")
    @Provides("3f335517-f442-4b98-b149-1e87ff16de45")
    public static DQResponse<ComplianceValue> validationScientificnameFound(@ActedUpon("dwc:scientificName") String scientificName) {
        return validationScientificnameFound(scientificName, null);
    }
    
    /**
	 * Propose amendment to the value of dwc:taxonID if it can be unambiguously resolved from bdq:sourceAuthority 
	 * using the available taxon terms.
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
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
			@Consulted("dwc:acceptedNameUsageID") String acceptedNameUsageID
	){
		return amendmentTaxonidFromTaxon(new Taxon(taxonID, kingdom, phylum, taxonomic_class, order, family, subfamily,
				genus, subgenus, scientificName, scientificNameAuthorship, genericName, specificEpithet,
				infraspecificEpithet, taxonRank, cultivarEpithet, higherClassification, vernacularName, taxonConceptID,
				scientificNameID, originalNameUsageID, acceptedNameUsageID), null);
	}

	
    /**
     * Can the taxon be unambiguously resolved from bdq:sourceAuthority using the available taxon terms?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #70 VALIDATION_TAXON_UNAMBIGUOUS
     *
     * Uses the default sourceAuthority.
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
    		@ActedUpon("dwc:order") String order
		){
		return validationTaxonUnambiguous(new Taxon(taxonID, kingdom, phylum, taxonomic_class, order, family, subfamily,
				genus, subgenus, scientificName, scientificNameAuthorship, genericName, specificEpithet,
				infraspecificEpithet, taxonRank, cultivarEpithet, higherClassification, vernacularName, taxonConceptID,
				scientificNameID, originalNameUsageID, acceptedNameUsageID), null);
    }
 
    /**
     * Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #71 AMENDMENT_SCIENTIFICNAME_FROM_TAXONID
     *
     * @param taxonID the provided dwc:taxonID to evaluate
     * @param scientificName the provided dwc:scientificName to evaluate
     * @return DQResponse the response of type AmendmentValue to return
     */
    @Amendment(label="AMENDMENT_SCIENTIFICNAME_FROM_TAXONID", description="Propose an amendment to the value of dwc:scientificName using the taxonID value from bdq:sourceAuthority.")
    @Provides("f01fb3f9-2f7e-418b-9f51-adf50f202aea")
    public static DQResponse<AmendmentValue> amendmentScientificnameFromTaxonid(
    		@Consulted("dwc:taxonID") String taxonID, 
    		@ActedUpon("dwc:scientificName") String scientificName
    	) {
    	return amendmentScientificnameFromTaxonid(taxonID, scientificName, null);
    }


    /**
     * Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #77 VALIDATION_CLASS_FOUND
     *
     * @param taxonomic_class the provided dwc:class to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_CLASS_FOUND", description="Does the value of dwc:class occur at rank of Class in bdq:sourceAuthority?")
    @Provides("2cd6884e-3d14-4476-94f7-1191cfff309b")
    public static DQResponse<ComplianceValue> validationClassFound(@ActedUpon("dwc:class") String taxonomic_class) {
    	return validationClassFound(taxonomic_class, null);
    }
    
    /**
     * Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #81 VALIDATION_KINGDOM_FOUND
     *
     * @param kingdom the provided dwc:kingdom to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_KINGDOM_FOUND", description="Does the value of dwc:kingdom occur at rank of Kingdom in bdq:sourceAuthority?")
    @Provides("125b5493-052d-4a0d-a3e1-ed5bf792689e")
    public static DQResponse<ComplianceValue> validationKingdomFound(@ActedUpon("dwc:kingdom") String kingdom) {
        return validationKingdomFound(kingdom, null);
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
    	return DwCSciNameDQ.validationScientificnameNotempty(scientificName);
    }

    /**
     * Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #83 VALIDATION_ORDER_FOUND
     *
     * @param order the provided dwc:order to evaluate
     * @return DQResponse tyyhe response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_ORDER_FOUND", description="Does the value of dwc:order occur at rank of Order in bdq:sourceAuthority?")
    @Provides("81cc974d-43cc-4c0f-a5e0-afa23b455aa3")
    public static DQResponse<ComplianceValue> validationOrderFound(@ActedUpon("dwc:order") String order) {
    	return validationOrderFound(order,null);
    }

    /**
     * Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?
     *
     * Provides: #101 VALIDATION_POLYNOMIAL_CONSISTENT
     *
     * @param scientificName the provided dwc:scientificName to evaluate
     * @param genericName the provided dwc:genericName to evaluate
     * @param specificEpithet the provided dwc:specificEpithet to evaluate
     * @param infraspecificEpithet the provided dwc:infraspecificEpithet to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_POLYNOMIAL_CONSISTENT", description="Is the polynomial represented in dwc:scientificName consistent with the equivalent values in dwc:genericName, dwc:specificEpithet, dwc:infraspecificEpithet?")
    @Provides("17f03f1f-f74d-40c0-8071-2927cfc9487b")
    public static DQResponse<ComplianceValue> validationPolynomialConsistent(
    		@ActedUpon("dwc:scientificName") String scientificName, 
    		@ActedUpon("dwc:genericName") String genericName, 
    		@ActedUpon("dwc:specificEpithet") String specificEpithet,
    		@ActedUpon("dwc:infraspecificEpithet") String infraspecificEpithet 
    ) {
        return DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
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
    	
        return DwCSciNameDQ.validationTaxonNotempty(taxonomic_class, genus, taxonConceptID, phylum, scientificNameID, taxonID, parentNameUsageID, subgenus, higherClassification, vernacularName, originalNameUsageID, acceptedNameUsageID, kingdom, family, scientificName, genericName, infragenericEpithet, specificEpithet, infraspecificEpithet, order);
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
    	return DwCSciNameDQ.validationTaxonidNotempty(taxonID);
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
    	return DwCSciNameDQ.validationTaxonidComplete(taxonID);
    }

    /**
     * Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?
	 * where bdq:sourceAuthority is the default GBIF Backbone Taxonomy.
     *
     * Provides: #122 VALIDATION_GENUS_FOUND
     *
     * @param genus the provided dwc:genus to evaluate
     * @return DQResponse the response of type ComplianceValue  to return
     */
    @Validation(label="VALIDATION_GENUS_FOUND", description="Does the value of dwc:genus occur at the rank of Genus in bdq:sourceAuthority?")
    @Provides("f2ce7d55-5b1d-426a-b00e-6d4efe3058ec")
    public static DQResponse<ComplianceValue> validationGenusFound(@ActedUpon("dwc:genus") String genus) {
    	return validationGenusFound(genus, null);
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
    	return DwCSciNameDQ.validationTaxonrankNotempty(taxonRank);
    }
    
}
