/**
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
 */
package org.filteredpush.qc.sciname;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.api.result.AmendmentValue;
import org.datakurator.ffdq.api.result.ComplianceValue;
import org.datakurator.ffdq.model.ResultState;
import org.junit.Test;

/**
 * Tests of DwCSciNameDQ which rely only on internal logic and don't require access to online services.
 * 
 * @See TestDwCSciNameDQ_IT for tests of methods that require access to external services.
 * 
 * @author mole
 *
 */
public class TestDwCSciNameDQ {

	private static final Log logger = LogFactory.getLog(TestDwCSciNameDQ.class);

	@Test 
	public void testvalidationTaxonidComplete() { 
		
        // Specification
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY; 
        // COMPLIANT if (1) taxonID is a validly formed LSID, or (2) 
        // taxonID is a validly formed URN with at least NID and NSS 
        // present, or (3) taxonID is in the form scope:value, or (4) 
        // taxonID is a validly formed URI with host and path where 
        // path consists of more than just "/"; otherwise NOT_COMPLIANT 
		
		String taxonID = "";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		
		taxonID = " ";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		
		taxonID = "3256236";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "urn:lsid:marinespecies.org:taxname:148";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "urn:lsid:marinespecies.org:taxname:";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://www.gbif.org/species/2529789";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "gbif:2529789";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://www.gbif.org/";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://www.gbif.org/species/";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "urn:uuid:c65c3ede-484f-45af-813e-65f606dff750";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "c65c3ede-484f-45af-813e-65f606dff750";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "pseudonamespace:c65c3ede-484f-45af-813e-65f606dff750";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "gbif:2529789";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "pseudonamespace:2529789";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "gbif:";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://invalid/99999999";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://example.com/99999999";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
	}
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationScientificnameNotempty(java.lang.String)}.
	 */
	@Test
	public void testValidationScientificnameNotempty() {
		
		// COMPLIANT if dwc:scientificName is not EMPTY; otherwise NOT_COMPLIANT 
		
		String scientificName = "Murex pecten";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationScientificnameNotempty(scientificName);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
		scientificName = "";
		result = DwCSciNameDQ.validationScientificnameNotempty(scientificName);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));

	}


	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationPolynomialConsistent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationPolynomialConsistent() {
		// Specification:
        // INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName is 
        // EMPTY, or all of dwc:genericName, dwc:specificEpithet and 
        // dwc:infraspecificEpithet are EMPTY; COMPLIANT if the polynomial, 
        // as represented in dwc:scientificName, is consistent with 
        // NOT_EMPTY values of dwc:genericName, dwc:specificEpithet, 
        // dwc:infraspecificEpithet; otherwise NOT_COMPLIANT. 
		
		String scientificName = "Aus bus cus";
		String genericName = "Aus";
		String specificEpithet = "bus";
		String infraspecificEpithet = "cus";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus cus";
		genericName = "Aus";
		specificEpithet = "somethingelse";
		infraspecificEpithet = "cus";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
		scientificName = "Hakea";
		genericName = "Hakea";
		specificEpithet = "";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());		
	
		scientificName="Hakea decurrens physocarpa";
		genericName="";
		specificEpithet="decurrens";
		infraspecificEpithet="physocarpa";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		scientificName="Hakea decurrens ssp. ? physocarpa";
		genericName="Hakea";
		specificEpithet="decurrens";
		infraspecificEpithet="";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		scientificName="Hakea decurrens ssp. Aff. physocarpa";
		genericName="Hakea";
		specificEpithet="decurrens";
		infraspecificEpithet="";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		// Validation dataID: 812
		scientificName="Hakea decurrens ssp. physocarpa";
		genericName="";
		specificEpithet="decurrens";
		infraspecificEpithet="physocarpa";  
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());		
		
		scientificName = "Ausareum bus cus";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "cus";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Ausareum bus Cuvier";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus Cuvier";
		genericName = "Ausareum";
		specificEpithet = "bus";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus cus Lamarck";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "cus";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus var. cus Lamarck";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "cus";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus f. cus  Lamarck, 1822";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "cus";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus Lamarck, 1822";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Solanum lycopersicum L. \"VAUGHN'S EARLY\"";
		genericName = "Solanum";
		specificEpithet = "lycopersicum";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Indigofera charlieriana subsp. sessilis var. scaberrima (Schinz) J.B. Gillett";
		genericName = "Indigofera";
		specificEpithet = "charlieriana";
		infraspecificEpithet = "scaberrima";   // per definition, dwc:infraspecificEpithet is the terminal epithet
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Indigofera charlieriana subsp. sessilis var. scaberrima (Schinz) J.B. Gillett";
		genericName = "Indigofera";
		specificEpithet = "charlieriana";
		infraspecificEpithet = "sessilis";  // not correct per definition
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Indigofera charlieriana subsp. sessilis var. scaberrima (Schinz) J.B. Gillett";
		genericName = "Indigofera";
		specificEpithet = "charlieriana";
		infraspecificEpithet = "var. scaberrima";  // not correct per definition, rank is not included in term
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus";
		genericName = "";
		specificEpithet = "";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		
		scientificName = "";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		
		// test NOT_EMPTY compliance, data compared with not-data passes
		scientificName = "Aus bus cus";
		genericName = "Aus";
		specificEpithet = "";
		infraspecificEpithet = "cus";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus cus";
		genericName = "";
		specificEpithet = "bus";
		infraspecificEpithet = "cus";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Aus bus cus";
		genericName = "Aus";
		specificEpithet = "bus";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// problem case from validation data
		scientificName = "Hakea";
		genericName = "Hakea";
		specificEpithet = "";
		infraspecificEpithet = null;
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Hakea";
		genericName = "Hakea";
		specificEpithet = "";
		infraspecificEpithet = String.valueOf(Character.toChars(0));
		logger.debug(infraspecificEpithet);
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Hakea";
		genericName = "Hakea";
		specificEpithet = "";
		infraspecificEpithet = "foo";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		// wrong path in test
		assertNotEquals("Genus parsed out of dwc:scientificName does not match dwc:genericName.", result.getComment());
		
		scientificName = "Hakea";
		genericName = "Hakea";
		specificEpithet = "foo";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		// wrong path in test
		assertNotEquals("Genus parsed out of dwc:scientificName does not match dwc:genericName.", result.getComment());
		
		scientificName = "Hakea [sp]";
		genericName = "Hakea";
		specificEpithet = "";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Crotalus atrox Baird and Girard, 1853";
		genericName = "Crotalus";
		specificEpithet = "atrox";
		infraspecificEpithet = "";
		result = DwCSciNameDQ.validationPolynomialConsistent(scientificName, genericName, specificEpithet, infraspecificEpithet);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonEmpty(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonEmpty() {
		DQResponse<ComplianceValue> result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		result =DwCSciNameDQ.validationTaxonNotempty("class", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		result =DwCSciNameDQ.validationTaxonNotempty(null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty("A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationScientificNameidNotempty(java.lang.String)}.
	 */
	@Test
	public void testValidationScientificNameidEmpty() {
		// COMPLIANT if dwc:taxonID is not EMPTY; otherwise NOT_COMPLIANT
		
		String taxonId = "foo";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationscientificNameIDNotempty(taxonId);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
		taxonId = "";
		result = DwCSciNameDQ.validationscientificNameIDNotempty(taxonId);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());		
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
	}


	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonrankNotempty(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonrankEmpty() {
		//COMPLIANT if dwc:taxonRank is not EMPTY; otherwise NOT_COMPLIANT 
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonrankNotempty(null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
		result = DwCSciNameDQ.validationTaxonrankNotempty("string");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
		result = DwCSciNameDQ.validationTaxonrankNotempty("species");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonrankNotstandard(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonrankNotstandard() {
		
		// Default source authority, has local copy, so tests should run even if service is unavailable.
		
		DQResponse<ComplianceValue> result= DwCSciNameDQ.validationTaxonrankStandard("kingdom", null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		result = DwCSciNameDQ.validationTaxonrankStandard("subspecies", null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		result = DwCSciNameDQ.validationTaxonrankStandard("", null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertEquals(null, result.getValue());
		
		result = DwCSciNameDQ.validationTaxonrankStandard("invalidvalue", null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		result = DwCSciNameDQ.validationTaxonrankStandard("kingdom", "https://example.com/example");
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.EXTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertEquals(null, result.getValue());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentTaxonrankStandardized(java.lang.String)}.
	 */
	@Test
	public void testAmendmentTaxonrankStandardized() {
		String taxonRank = "species"; 
		DQResponse<AmendmentValue> response = DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, null);
		logger.debug(response.getComment());
		assertFalse(SciNameUtils.isEmpty(response.getComment()));
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		taxonRank = "Species"; 
		response = DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, null);
		logger.debug(response.getComment());
		assertFalse(SciNameUtils.isEmpty(response.getComment()));
		assertEquals(ResultState.AMENDED.getLabel(), response.getResultState().getLabel());
		assertEquals("species",response.getValue().getObject().get("dwc:taxonRank"));
		
		taxonRank = "";
		response = DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, "https://www.example.com/example");
		logger.debug(response.getComment());
		assertFalse(SciNameUtils.isEmpty(response.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		taxonRank = "kingdom";
		response = DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, "https://www.example.com/example");
		logger.debug(response.getComment());
		assertFalse(SciNameUtils.isEmpty(response.getComment()));
		assertEquals(ResultState.EXTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		taxonRank = "familia"; 
		response = DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, null);
		logger.debug(response.getComment());
		assertFalse(SciNameUtils.isEmpty(response.getComment()));
		assertEquals(ResultState.AMENDED.getLabel(), response.getResultState().getLabel());
		assertEquals("family",response.getValue().getObject().get("dwc:taxonRank"));
		
		taxonRank = "Familia "; 
		response = DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, null);
		logger.debug(response.getComment());
		assertFalse(SciNameUtils.isEmpty(response.getComment()));
		assertEquals(ResultState.AMENDED.getLabel(), response.getResultState().getLabel());
		assertEquals("family",response.getValue().getObject().get("dwc:taxonRank"));
		
		taxonRank = "not a rank string"; 
		response = DwCSciNameDQ.amendmentTaxonrankStandardized(taxonRank, null);
		logger.debug(response.getComment());
		assertFalse(SciNameUtils.isEmpty(response.getComment()));
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		
	}

}