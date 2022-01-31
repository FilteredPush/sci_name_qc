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
 * Tests of DwCSciNameDQ which require online access to remote services, named, to fit the expectations of the
 * maven-failsafe-plugin with an IT (integration test) ending, so as to run in the integration-test
 * phase.  See the maven-failsafe-plugin section in pom.xml
 * 
 * @see TestDwCSciNameDQ for tests of internal logic independent of access to external services.
 * 
 * @author mole
 *
 */
public class DwCSciNameDQ_IT {

	private static final Log logger = LogFactory.getLog(DwCSciNameDQ_IT.class);

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationPhylumNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationPhylumNotfound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:phylum is EMPTY; COMPLIANT if the value of dwc:phylum 
        // was found as a value at the rank of phylum by the bdq:sourceAuthority 
        // service; otherwise NOT_COMPLIANT 		
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationPhylumNotfound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationPhylumNotfound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationPhylumNotfound("Mollusca",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationPhylumNotfound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationPhylumNotfound("Mollusca",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationFamilyNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationFamilyNotfound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:family is EMPTY; COMPLIANT if the value of dwc:family 
        // was found as a value at the rank of family by the bdq:sourceAuthority 
        // service; otherwise NOT_COMPLIANT 
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationFamilyNotfound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationFamilyNotfound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationFamilyNotfound("Muricidae",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationFamilyNotfound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationFamilyNotfound("Muricidae",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationScientificnameNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationScientificnameNotfound() {
// TODO:		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentTaxonidFromTaxon(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAmendmentTaxonidFromTaxon() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if all of dwc:kingdom, dwc:phylum, dwc:class, dwc:order, 
        // dwc:family, dwc:genus, and dwc:scientificName are EMPTY; 
        // AMENDED if a value for dwc:taxonID is unique and resolvable 
        // on the basis of the value of the lowest ranking not EMPTY 
        // taxon classification terms dwc:scientificName, dwc:scientificNameAuthorship, 
        // dwc:kingdom, dwc:phylum, dwc:class, etc.; otherwise NOT_CHANGED 
        //
		
		SciNameSourceAuthority authority = null;
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
		} catch (SourceAuthorityException e1) {
			fail(e1.getMessage());
		}
		
		String family = null;
		String scientificName = "Murex pecten";  // GBIF returns two matches, we can't tell which to use.
		String taxonId = null;
		String scientificNameAuthorship = null;
		DQResponse<AmendmentValue> response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		assertEquals(ResultState.NO_CHANGE.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());

		family = null;
		scientificName = "Vulpes vulpes";  // no authorship provided
		taxonId = null;
		scientificNameAuthorship = null;
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));
		
		family = null;
		scientificName = "Vulpes vulpes";  // not formed according to the dwc:scientificName definition, should include authorship
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
		
		family = null;
		scientificName = "Vulpes vulpes";  // not formed according to the dwc:scientificName definition, should include authorship
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus)";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
		
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus, 1758)";  // correctly formed, with authorship included
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
		
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus)";
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus)";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));			
		
		family = "Muricidae";
		scientificName = "";
		taxonId = null;
		scientificNameAuthorship = "Rafinesque, 1815";   // not known to GBIF, so can't be sure of match, so won't change
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		// NOTE: If GBIF improves its data quality, this test will fail
		assertEquals(ResultState.NO_CHANGE.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Muricidae";
		scientificName = "";
		taxonId = null;
		scientificNameAuthorship = "";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:2304120",response.getValue().getObject().get("dwc:taxonID"));	
		
		family = "Muricidae";
		scientificName = "Muricidae";
		taxonId = null;
		scientificNameAuthorship = "";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:2304120",response.getValue().getObject().get("dwc:taxonID"));			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		family = "Muricidae";
		scientificName = "Muricidae";
		taxonId = null;
		scientificNameAuthorship = "Rafinesque, 1815";   
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:148", response.getValue().getObject().get("dwc:taxonID"));
		
		family = "Muricidae";
		scientificName = "Murex pecten";
		taxonId = null;
		scientificNameAuthorship = "Lightfoot, 1786";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404683",response.getValue().getObject().get("dwc:taxonID"));
		
		family = "Muricidae";
		scientificName = "Murex pecten Lightfoot, 1786";
		taxonId = null;
		scientificNameAuthorship = "Lightfoot, 1786";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404683",response.getValue().getObject().get("dwc:taxonID"));
		
		family = "Chaetodermatidae ";
		scientificName = "Falcidens macrafrondis";
		taxonId = "urn:lsid:marinespecies.org:taxname:545069";   // correct value, shouldn't suggest ammendment
		scientificNameAuthorship = "Scheltema";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.NO_CHANGE.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Chaetodermatidae ";
		scientificName = "Falcidens macrafrondis Scheltema";
		taxonId = "urn:lsid:marinespecies.org:taxname:545069";   // correct value, shouldn't suggest ammendment
		scientificNameAuthorship = "Scheltema";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.NO_CHANGE.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());		
		
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis";
		taxonId = null;
		scientificNameAuthorship = "Scheltema";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:taxonID"));
	
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		taxonId = null;
		scientificNameAuthorship = "Scheltema";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:taxonID"));
				
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis";
		taxonId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
		scientificNameAuthorship = "Scheltema";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:taxonID"));
		
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		taxonId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
		scientificNameAuthorship = "Scheltema";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:taxonID"));
				
		family = "Muricidae";
		scientificName = "Murex monoceros";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		taxonId = "";  
		scientificNameAuthorship = "";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.NO_CHANGE.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Muricidae";
		scientificName = "Murex monoceros";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		taxonId = "";  
		scientificNameAuthorship = "Sowerby, 1841";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404582",response.getValue().getObject().get("dwc:taxonID"));

		family = "Muricidae";
		scientificName = "Murex monoceros Sowerby, 1841";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		taxonId = "";  
		scientificNameAuthorship = "Sowerby, 1841";
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxonId, null, null, null, null, family, null, null, scientificName, scientificNameAuthorship, null, null, null, null, null, null, null, null, null, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.CHANGED.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404582",response.getValue().getObject().get("dwc:taxonID"));
		
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonAmbiguous() {
// TODO:		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentScientificnameFromTaxonid(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAmendmentScientificnameFromTaxonid() {
// TODO:		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationClassNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationClassNotfound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:class is EMPTY; COMPLIANT if the value of dwc:class 
        // was found as a value at the rank of class by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationClassNotfound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationClassNotfound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationClassNotfound("Gastropoda",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationClassNotfound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationClassNotfound("Gastropoda",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationKingdomNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationKingdomNotfound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:phylum is EMPTY; COMPLIANT if the value of dwc:phylum 
        // was found as a value at the rank of phylum by the bdq:sourceAuthority 
        // service; otherwise NOT_COMPLIANT 
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationKingdomNotfound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationKingdomNotfound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationKingdomNotfound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationKingdomNotfound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationKingdomNotfound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
	}


	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationOrderNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationOrderNotfound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // service was not available; INTERNAL_PREREQUISITES_NOT_MET 
        // if dwc:order is EMPTY; COMPLIANT if the value of dwc:order 
        // was found as a value at the rank of order by the bdq:sourceAuthority 
        //service; otherwise NOT_COMPLIANT 

		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationOrderNotfound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationOrderNotfound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationOrderNotfound("Carnivora",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
		result = DwCSciNameDQ.validationOrderNotfound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationOrderNotfound("Neogastropoda",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
	}


	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonidAmbiguous(java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonidAmbiguous() {
// TODO: 		fail("Not yet implemented");
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
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
	
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationGenusNotfound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationGenusNotfound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationGenusNotfound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationGenusNotfound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationClassificationAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationClassificationAmbiguous() {
// TODO: 		fail("Not yet implemented");
	}


}
