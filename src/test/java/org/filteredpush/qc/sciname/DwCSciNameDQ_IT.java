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
	public void testValidationPhylumFound() {
		
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
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationPhylumFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationPhylumFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationPhylumFound("Mollusca",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationPhylumFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationPhylumFound("Mollusca",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		result = DwCSciNameDQ.validationPhylumFound("Mollusca",null);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationFamilyNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationFamilyFound() {
		
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
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationFamilyFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationFamilyFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationFamilyFound("Muricidae",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationFamilyFound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationFamilyFound("Muricidae",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		
		result = DwCSciNameDQ.validationFamilyFound("Muricidae",null);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
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
		Taxon taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		DQResponse<AmendmentValue> response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());

		family = null;
		scientificName = "Vulpes vulpes";  // no authorship provided
		taxonId = null;
		scientificNameAuthorship = null;
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));
		
		family = null;
		scientificName = "Vulpes vulpes";  // not formed according to the dwc:scientificName definition, should include authorship
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
		
		family = null;
		scientificName = "Vulpes vulpes";  // not formed according to the dwc:scientificName definition, should include authorship
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
		
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus, 1758)";  // correctly formed, with authorship included
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));		
	
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus, 1758)";  // correctly formed, with authorship included
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, null);   /// null authority should be GBIF
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));
		
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus)";
		taxonId = null;
		scientificNameAuthorship = "(Linnaeus)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:taxonID"));			
		
		family = "Muricidae";
		scientificName = "";
		taxonId = null;
		scientificNameAuthorship = "Rafinesque, 1815";   // not known to GBIF, so can't be sure of match, so won't change
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		// NOTE: If GBIF improves its data quality, this test will fail
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Muricidae"; // not used for matching, only to disambuguate.
		scientificName = "";  // no value to match on
		taxonId = null;
		scientificNameAuthorship = "";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		family = "Muricidae";
		scientificName = "Muricidae";
		taxonId = null;
		scientificNameAuthorship = "";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
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
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:148", response.getValue().getObject().get("dwc:taxonID"));
		
		family = "Muricidae";
		scientificName = "Murex pecten";
		taxonId = null;
		scientificNameAuthorship = "Lightfoot, 1786";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404683",response.getValue().getObject().get("dwc:taxonID"));
		
		family = "Muricidae";
		scientificName = "Murex pecten Lightfoot, 1786";
		taxonId = null;
		scientificNameAuthorship = "Lightfoot, 1786";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404683",response.getValue().getObject().get("dwc:taxonID"));
		
		family = "Chaetodermatidae ";
		scientificName = "Falcidens macrafrondis";
		taxonId = "urn:lsid:marinespecies.org:taxname:545069";   // correct value, shouldn't suggest ammendment
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Chaetodermatidae ";
		scientificName = "Falcidens macrafrondis Scheltema";
		taxonId = "urn:lsid:marinespecies.org:taxname:545069";   // correct value, shouldn't suggest ammendment
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());		
		
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis";
		taxonId = null;
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:taxonID"));
	
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		taxonId = null;
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:taxonID"));
				
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis";
		taxonId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
			// but has a value, so will be internal prerequisites not met.
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		taxonId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
			// but has a value, so will be internal prerequisites not met.
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		// test non-standard overwrite behavior.
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		taxonId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority, true);  // requesting overwrite, not standard behavior
		logger.debug(response.getComment());
		assertEquals(ResultState.AMENDED.getLabel(), response.getResultState().getLabel());  // overwrite
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:taxonID"));
				
		family = "Muricidae";
		scientificName = "Murex monoceros";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		taxonId = "";  
		scientificNameAuthorship = "";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Muricidae";
		scientificName = "Murex monoceros";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		taxonId = "";  
		scientificNameAuthorship = "Sowerby, 1841";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404582",response.getValue().getObject().get("dwc:taxonID"));

		family = "Muricidae";
		scientificName = "Murex monoceros Sowerby, 1841";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		taxonId = "";  
		scientificNameAuthorship = "Sowerby, 1841";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404582",response.getValue().getObject().get("dwc:taxonID"));
		
        // TODO: Debug, returning lookup taxonID for taxon NOT_AMENDED No exact match found for provided taxon in WORMS.
        // for Babelomurex benoiti (Tiberi, 1855)  should be urn:lsid:marinespecies.org:taxname:234156
        // for taxon_name_id=182110 in MCZbase.
		family = "Muricidae";
		scientificName = "Babelomurex benoiti (Tiberi, 1855)";  // correctly formed, with authorship included
		taxonId = null;
		scientificNameAuthorship = "(Tiberi, 1855)";
		taxon = new Taxon();
		taxon.setTaxonID(taxonId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentTaxonidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:234156",response.getValue().getObject().get("dwc:taxonID"));		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonAmbiguous() {
		
		String scientificName = "";
		String kingdom = "";
		String taxonomic_class = "";
		
		// ambiregnal homonyms 
		scientificName = "Graphis";  // gastropod and lichen
		kingdom = "Fungi";
		taxonomic_class= "Lecanoromycetes";
		scientificName = "Graphis Adanson, 1763";  // fungi
		kingdom = "Animalia";
		taxonomic_class = "Gastropoda";
		scientificName = "Graphis Jeffreys, 1867"; // gastropod
		
		scientificName = "Calotheca"; // beetle, grass, and junior homonym choanoflagellate
		kingdom = "Protozoa";
		taxonomic_class = "Choanoflagellatea";
		scientificName = "Calotheca Thomsen & Moestrup, 1983";
		kingdom = "Animalia";
		taxonomic_class = "Insecta";
		scientificName = "Calotheca Heyden, 1887";
		kingdom = "Plantae";
		taxonomic_class = "Magnoliopsida";
		scientificName = "Calotheca A.M.F.J. Palisot de Beauvois";
		
		scientificName = "Gaimardia";
		kingdom = "Animalia";
		taxonomic_class = "Bivalvia";
		scientificName = "Gaimardia Gould, 1852"; // clam
		kingdom = "Plantae";
		taxonomic_class = "Magnoliopsida";
		scientificName = "Gaimardia Gaudichaud-Beaupré, 1825";  // plant  
		
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
	public void testValidationClassFound() {
		
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
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationClassFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationClassFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationClassFound("Gastropoda",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationClassFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationClassFound("Gastropoda",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		
		result = DwCSciNameDQ.validationClassFound("Gastropoda",null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationKingdomNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationKingdomFound() {
		
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
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationKingdomFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationKingdomFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationKingdomFound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationKingdomFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationKingdomFound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
		
		result = DwCSciNameDQ.validationKingdomFound("Animalia",null);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());
	}


	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationOrderNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationOrderFound() {
		
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
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationOrderFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationOrderFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationOrderFound("Carnivora",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
		result = DwCSciNameDQ.validationOrderFound("Animalia",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationOrderFound("Neogastropoda",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
		result = DwCSciNameDQ.validationOrderFound("Neogastropoda",null);
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
	public void testValidationGenusFound() {
		
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
		
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
	
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationGenusFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationGenusFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		result = DwCSciNameDQ.validationGenusFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		result = DwCSciNameDQ.validationGenusFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.COMPLIANT, result.getValue());	
		
		result = DwCSciNameDQ.validationGenusFound("Murex",null);
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
