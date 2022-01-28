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
 * @author mole
 *
 */
public class TestDwCSciNameDQ {

	private static final Log logger = LogFactory.getLog(TestDwCSciNameDQ.class);
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#DwCSciNameDQ()}.
	 * also tests {@link org.filteredpush.qc.sciname.DwCSciNameDQ#getSourceAuthority()}.
	 */
	@Test
	public void testDwCSciNameDQ() {
		DwCSciNameDQ testInstance = new DwCSciNameDQ();
		assertEquals(EnumSciNameSourceAuthority.GBIF, testInstance.getSourceAuthority());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#DwCSciNameDQ(org.filteredpush.qc.sciname.EnumSciNameSourceAuthority)}.
	 * also tests {@link org.filteredpush.qc.sciname.DwCSciNameDQ#getSourceAuthority()}.
	 */
	@Test
	public void testDwCSciNameDQEnumSciNameSourceAuthority() {
		DwCSciNameDQ testInstance = new DwCSciNameDQ(EnumSciNameSourceAuthority.WORMS);
		assertEquals(EnumSciNameSourceAuthority.WORMS, testInstance.getSourceAuthority());
		DwCSciNameDQ testInstance2 = new DwCSciNameDQ(EnumSciNameSourceAuthority.GBIF);
		assertEquals(EnumSciNameSourceAuthority.GBIF, testInstance2.getSourceAuthority());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationPhylumNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationPhylumNotfound() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationFamilyNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationFamilyNotfound() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationScientificnameNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationScientificnameNotfound() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentTaxonidFromTaxon(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAmendmentTaxonidFromTaxon() {
		
		DwCSciNameDQ tester = new DwCSciNameDQ();
		
		String family = null;
		String scientificName = "Murex pecten";  // GBIF returns two matches, we can't tell which to use.
		String taxonId = null;
		String scientificNameAuthorship = null;
		DQResponse<AmendmentValue> response = tester.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.NO_CHANGE.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());

		family = null;
		scientificName = "Vulpes vulpes";
		taxonId = null;
		scientificNameAuthorship = null;
		response = tester.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));
		
		family = null;
		scientificName = "Vulpes vulpes";
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		response = tester.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
		
		family = null;
		scientificName = "Vulpes vulpes";
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus)";
		response = tester.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
		
		family = "Muricidae";
		scientificName = "";
		taxonId = null;
		scientificNameAuthorship = "Rafinesque, 1815";   // not known to GBIF, so can't be sure of match, so won't change
		response = tester.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null);
		logger.debug(response.getComment());
		// NOTE: If GBIF improves its data quality, this test will fail
		assertEquals(ResultState.NO_CHANGE.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Muricidae";
		scientificName = "";
		taxonId = null;
		scientificNameAuthorship = "";
		response = tester.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:2304120",response.getValue().getObject().get("dwc:taxonID"));	
		
		
		
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonAmbiguous() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentScientificnameFromTaxonid(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAmendmentScientificnameFromTaxonid() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationClassNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationClassNotfound() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationKingdomNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationKingdomNotfound() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationScientificnameEmpty(java.lang.String)}.
	 */
	@Test
	public void testValidationScientificnameEmpty() {
		
		// COMPLIANT if dwc:scientificName is not EMPTY; otherwise NOT_COMPLIANT 
		
		String scientificName = "Murex pecten";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationScientificnameEmpty(scientificName);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "";
		result = DwCSciNameDQ.validationScientificnameEmpty(scientificName);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());

	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationOrderNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationOrderNotfound() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationPolynomialInconsistent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationPolynomialInconsistent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonEmpty(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonEmpty() {
		DQResponse<ComplianceValue> result =DwCSciNameDQ.validationTaxonEmpty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());
		result =DwCSciNameDQ.validationTaxonEmpty("class", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonidEmpty(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonidEmpty() {
		// COMPLIANT if dwc:taxonID is not EMPTY; otherwise NOT_COMPLIANT
		
		String taxonId = "foo";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonidEmpty(taxonId);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxonId = "";
		result = DwCSciNameDQ.validationTaxonidEmpty(taxonId);
		logger.debug(result.getComment());
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());		
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonidAmbiguous(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonidAmbiguous() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationGenusNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationGenusNotfound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:genus is EMPTY; COMPLIANT if the value of dwc:genus 
        // was found as a value at the rank of genus by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 
		
		DwCSciNameDQ tester = new DwCSciNameDQ();
	
		DQResponse<ComplianceValue> result = tester.validationGenusNotfound(null);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = tester.validationGenusNotfound("a3555144X");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = tester.validationGenusNotfound("Murex");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationClassificationAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationClassificationAmbiguous() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonrankEmpty(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonrankEmpty() {
		//COMPLIANT if dwc:taxonRank is not EMPTY; otherwise NOT_COMPLIANT 
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonrankEmpty(null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());
		result = DwCSciNameDQ.validationTaxonrankEmpty("string");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationTaxonrankEmpty("species");
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonrankNotstandard(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonrankNotstandard() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentTaxonrankStandardized(java.lang.String)}.
	 */
	@Test
	public void testAmendmentTaxonrankStandardized() {
		fail("Not yet implemented");
	}

}
