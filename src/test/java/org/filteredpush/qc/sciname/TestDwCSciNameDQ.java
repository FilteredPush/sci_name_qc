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
		// TODO: specification needs work.
		
		String taxonID = "";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "3256236";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "urn:lsid:marinespecies.org:taxname:148";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "urn:lsid:marinespecies.org:taxname:";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://www.gbif.org/species/2529789";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://www.gbif.org/";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "https://www.gbif.org/species/";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "urn:uuid:c65c3ede-484f-45af-813e-65f606dff750";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "c65c3ede-484f-45af-813e-65f606dff750";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonID = "gbif:2529789";
		result = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
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
		
		scientificName = "";
		result = DwCSciNameDQ.validationScientificnameNotempty(scientificName);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());

	}


	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationPolynomialInconsistent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationPolynomialInconsistent() {
// TODD:		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonEmpty(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonEmpty() {
		DQResponse<ComplianceValue> result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty("class", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A", null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "A");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonNotempty("A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonidNotempty(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonidEmpty() {
		// COMPLIANT if dwc:taxonID is not EMPTY; otherwise NOT_COMPLIANT
		
		String taxonId = "foo";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonidNotempty(taxonId);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonId = "";
		result = DwCSciNameDQ.validationTaxonidNotempty(taxonId);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());		
		
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
		result = DwCSciNameDQ.validationTaxonrankNotempty("string");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationTaxonrankNotempty("species");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonrankNotstandard(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonrankNotstandard() {
// TODO:		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentTaxonrankStandardized(java.lang.String)}.
	 */
	@Test
	public void testAmendmentTaxonrankStandardized() {
// TODO:		fail("Not yet implemented");
	}

}
