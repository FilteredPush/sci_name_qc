/** 
 * AISandbox.java
 * 
 * Copyright 2024 President and Fellows of Harvard College
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

import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.api.result.ComplianceValue;
import org.datakurator.ffdq.model.ResultState;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Examples of exploration of Generative AI produced unit tests, with modifications needed to actually execute,
 * are informative about the intelligibility and specificity of the Specifications of the QC tests.
 * 
 * @author mole
 *
 */
public class AISandbox {
	
	private static final Log logger = LogFactory.getLog(AISandbox.class);
	
	/**
	 * Source: Harvard FAS IT AI Sandbox 
	 * Model: Claude Instant
	 * Prompt: Given the Specification("INTERNAL_PREREQUISITES_NOT_MET if dwc:taxonID is EMPTY;
	 * COMPLIANT if (1) taxonID is a validly formed LSID, or (2) taxonID is a
	 * validly formed URN with at least NID and NSS present, or (3) taxonID is in
	 * the form scope:value, or (4) taxonID is a validly formed URI with host and
	 * path where path consists of more than just '/'; otherwise NOT_COMPLIANT ").
	 * Write unit tests for the method signature: public static
	 * DQResponse<ComplianceValue>
	 * validationTaxonidComplete(@ActedUpon("dwc:taxonID") String taxonID) covering
	 * the entire specification, where DQResponse is specified by
	 * https://raw.githubusercontent.com/kurator-org/ffdq-api/master/src/main/java/org/datakurator/ffdq/api/DQResponse.java
	 * and ComplianceValue is specified by
	 * https://raw.githubusercontent.com/kurator-org/ffdq-api/master/src/main/java/org/datakurator/ffdq/api/result/ComplianceValue.java
	 * Response: 
	Here are unit tests covering the entire specification for the given method signature:
	import static org.junit.jupiter.api.Assertions.assertEquals;

	import org.datakurator.ffdq.api.DQResponse;
	import org.datakurator.ffdq.api.result.ComplianceValue;
	import org.junit.jupiter.api.Test;

	public class TaxonIdValidationTest {

	  @Test
	  public void testEmptyTaxonId() {
	    String taxonID = "";
	    DQResponse<ComplianceValue> response = TaxonIdValidator.validationTaxonidComplete(taxonID);
	    assertEquals(ComplianceValue.INTERNAL_PREREQUISITES_NOT_MET, response.getValue());
	  }

	  @Test
	  public void testValidLsid() {
	    String taxonID = "urn:lsid:taxonomy.org:taxnames:12345";
	    DQResponse<ComplianceValue> response = TaxonIdValidator.validationTaxonidComplete(taxonID);
	    assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	  }

	  @Test
	  public void testValidUrn() {
	    String taxonID = "urn:nid:12345:nss:MyTaxon";
	    DQResponse<ComplianceValue> response = TaxonIdValidator.validationTaxonidComplete(taxonID);
	    assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	  }

	  @Test
	  public void testScopeValue() {
	    String taxonID = "http://example.com/scope:value";
	    DQResponse<ComplianceValue> response = TaxonIdValidator.validationTaxonidComplete(taxonID);
	    assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	  }

	  @Test
	  public void testValidUri() {
	    String taxonID = "http://example.com/path/to/taxon";
	    DQResponse<ComplianceValue> response = TaxonIdValidator.validationTaxonidComplete(taxonID);
	    assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	  }

	  @Test
	  public void testInvalidFormat() {
	    String taxonID = "invalid";
	    DQResponse<ComplianceValue> response = TaxonIdValidator.validationTaxonidComplete(taxonID);
	    assertEquals(ComplianceValue.NOT_COMPLIANT, response.getValue());
	  }

	  @Test
	  public void testUriPathOnly() {
	    String taxonID = "http://example.com/";
	    DQResponse<ComplianceValue> response = TaxonIdValidator.validationTaxonidComplete(taxonID);
	    assertEquals(ComplianceValue.NOT_COMPLIANT, response.getValue());
	  }
	  These tests cover all cases specified in the requirement - empty, valid LSID, valid URN, scope:value, 
	  valid URI, invalid format, and URI with only path. The DQResponse and ComplianceValue types are 
	  imported from the provided classes.
	  
	  Notes:
	  (1) UnitTest framework wasn't specified, selected a different one than in use in this project.
      (2) Class for validationTaxonidComplete wasn't specified, plausible TaxonIdValidator was asserted.
      (3) Structure of DQResponse wasn't understood, so ComplianceValue.INTERNAL_PREREQUISITES_NOT_MET is
      incorrectly asserted instead of its place in the DQResponse structure.
      
      (4) Not present in the specification is that response.getResultState() should return RUN_HAS_RESULT 
      when there is a ComplianceValue.
      (5) Not present in the specification is that response.getValue() should be null when the result state
      is not RUN_HAS_RESULT.
      
	  (6) String taxonID = "http://example.com/scope:value"; assumes that scope:value is an http URI, not 
	  the expected scope:value, gbif:35252, of the specification writers.
      
	}
	*/
	
	@Test
	public void testEmptyTaxonId() {
		String taxonID = "";
		DQResponse<ComplianceValue> response = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET, response.getResultState());
		assertNull(response.getValue());
	}

	@Test
	public void testValidLsid() {
		String taxonID = "urn:lsid:taxonomy.org:taxnames:12345";
		DQResponse<ComplianceValue> response = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		assertEquals(ResultState.RUN_HAS_RESULT, response.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	}

	@Test
	public void testValidUrn() {
		String taxonID = "urn:nid:12345:nss:MyTaxon";
		DQResponse<ComplianceValue> response = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		assertEquals(ResultState.RUN_HAS_RESULT, response.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	}

	@Test
	public void testScopeValue() {
		// String taxonID = "http://example.com/scope:value";
		String taxonID = "scope:6262626";
		DQResponse<ComplianceValue> response = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		assertEquals(ResultState.RUN_HAS_RESULT, response.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	}

	@Test
	public void testValidUri() {
		String taxonID = "http://example.com/path/to/taxon";
		DQResponse<ComplianceValue> response = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		assertEquals(ResultState.RUN_HAS_RESULT, response.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, response.getValue());
	}

	@Test
	public void testInvalidFormat() {
		String taxonID = "invalid";
		DQResponse<ComplianceValue> response = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		assertEquals(ResultState.RUN_HAS_RESULT, response.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, response.getValue());
	}

	@Test
	public void testUriPathOnly() {
		String taxonID = "http://example.com/";
		DQResponse<ComplianceValue> response = DwCSciNameDQ.validationTaxonidComplete(taxonID);
		assertEquals(ResultState.RUN_HAS_RESULT, response.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, response.getValue());
	}


	
}
