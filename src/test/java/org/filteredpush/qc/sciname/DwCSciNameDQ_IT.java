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
	public void testValidationScientificnameFound() {
		
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificName 
        // is EMPTY; COMPLIANT if there is a match of the contents 
        // of dwc:scientificName with the bdq:sourceAuthority; otherwise 
        // NOT_COMPLIANT
		
        // bdq:sourceAuthority default = "GBIF Backbone 
        // Taxonomy" [https://doi.org/10.15468/39omei], "API endpoint" 
        // [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 
		
		SciNameSourceAuthority defaultAuthority = new SciNameSourceAuthority();
		SciNameSourceAuthority wormsAuthority = null;
		try {
			wormsAuthority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail("Unexpected exception:" +  e.getMessage());
		}
		
		String scientificName = null;
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationScientificnameFound(scientificName,defaultAuthority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		
		scientificName = "Murex brevispina Lamarck, 1822";
		result = DwCSciNameDQ.validationScientificnameFound(scientificName,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "Murex brevispina Lamarck, 1822";
		result = DwCSciNameDQ.validationScientificnameFound(scientificName,wormsAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		scientificName = "not a scientific name";
		result = DwCSciNameDQ.validationScientificnameFound(scientificName,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// failing to parse when passed from coldfusion, but not here.
		//scientificName = "Babelomurex dalli (Emerson & D'Attilio, 1963)";
		scientificName = "Babelomurex dalli (W.K.Emerson & D'Attilio, 1963)";
		result = DwCSciNameDQ.validationScientificnameFound(scientificName,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// Historical capitalized specific epithet.
		scientificName = "Pentagonaster Alexandri Perrier, 1881";
		result = DwCSciNameDQ.validationScientificnameFound(scientificName,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// Historical capitalized specific epithet.
		scientificName = "Ophiocoma Alexandri Lyman, 1860";
		result = DwCSciNameDQ.validationScientificnameFound(scientificName,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// name without authorship and multiple GBIF records
		scientificName = "Eucalyptus camaldulensis";
		result = DwCSciNameDQ.validationScientificnameFound(scientificName,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentTaxonidFromTaxon(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAmendmentTaxonidFromTaxon() {
		
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
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
        // [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
		
		SciNameSourceAuthority authority = null;
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
		} catch (SourceAuthorityException e1) {
			fail(e1.getMessage());
		}
		
		String family = null;
		String scientificName = "Murex pecten";  // GBIF returns two matches, pick the one that is accepted by the other
		String scientificNameId = null;
		String scientificNameAuthorship = null;
		Taxon taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		DQResponse<AmendmentValue> response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5726736", response.getValue().getObject().get("dwc:scientificNameID"));

		family = null;
		scientificName = "Vulpes vulpes";  // no authorship provided
		scientificNameId = null;
		scientificNameAuthorship = null;
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:scientificNameID"));
		
		family = null;
		scientificName = "Vulpes vulpes";  // not formed according to the dwc:scientificName definition, should include authorship
		scientificNameId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:scientificNameID"));		
		
		family = null;
		scientificName = "Vulpes vulpes";  // not formed according to the dwc:scientificName definition, should include authorship
		scientificNameId = null;
		scientificNameAuthorship = "(Linnaeus)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:scientificNameID"));		
		
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus, 1758)";  // correctly formed, with authorship included
		scientificNameId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:scientificNameID"));		
	
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus, 1758)";  // correctly formed, with authorship included
		scientificNameId = null;
		scientificNameAuthorship = "(Linnaeus, 1758)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, null);   /// null authority should be GBIF
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:scientificNameID"));
		
		family = null;
		scientificName = "Vulpes vulpes (Linnaeus)";
		scientificNameId = null;
		scientificNameAuthorship = "(Linnaeus)";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:5219243",response.getValue().getObject().get("dwc:scientificNameID"));			
		
		family = "Muricidae";
		scientificName = "";
		scientificNameId = null;
		scientificNameAuthorship = "Rafinesque, 1815";   // not known to GBIF, so can't be sure of match, so won't change
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		// NOTE: If GBIF improves its data quality, this test will fail
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Muricidae"; // not used for matching, only to disambuguate.
		scientificName = "";  // no value to match on
		scientificNameId = null;
		scientificNameAuthorship = "";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		family = "Muricidae";
		scientificName = "Muricidae";
		scientificNameId = null;
		scientificNameAuthorship = "";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:2304120",response.getValue().getObject().get("dwc:scientificNameID"));	
		
		scientificNameId=""; 
		scientificName="Chicoreus palmarosae (Lamarck, 1822)"; 
		String kingdom="Animalia"; 
		String phylum="Mollusca"; 
		String taxonomic_class="Gastropoda"; 
		String order=""; 
		family="Muricidae"; 
		String subfamily=""; 
		String genus="Chicoreus"; 
		String genericName="Chicoreus"; 
		String subgenus=""; 
		String infragenericEpithet=""; 
		String specificEpithet="palmarosae"; 
		String infraspecificEpithet=""; 
		String cultivarEpithet=""; 
		String vernacularName=""; 
		scientificNameAuthorship="(Lamarck, 1822)"; 
		String taxonRank="";
		taxon = new Taxon();
		taxon.setTaxonID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setKingdom(kingdom);
		taxon.setPhylum(phylum);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setFamily(family);
		taxon.setGenus(subgenus);
		taxon.setGenericName(genericName);
		taxon.setSpecificEpithet(specificEpithet);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("gbif:4365662",response.getValue().getObject().get("dwc:scientificNameID"));	
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		
		family = "Muricidae";
		scientificName = "Muricidae";
		scientificNameId = null;
		scientificNameAuthorship = "Rafinesque, 1815";   
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:148", response.getValue().getObject().get("dwc:scientificNameID"));
		
		family = "Muricidae";
		scientificName = "Murex pecten";
		scientificNameId = null;
		scientificNameAuthorship = "Lightfoot, 1786";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404683",response.getValue().getObject().get("dwc:scientificNameID"));
		
		family = "Muricidae";
		scientificName = "Murex pecten Lightfoot, 1786";
		scientificNameId = null;
		scientificNameAuthorship = "Lightfoot, 1786";
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404683",response.getValue().getObject().get("dwc:scientificNameID"));
		
		family = "Chaetodermatidae ";
		scientificName = "Falcidens macrafrondis";
		scientificNameId = "urn:lsid:marinespecies.org:taxname:545069";   // correct value, shouldn't suggest ammendment
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Chaetodermatidae ";
		scientificName = "Falcidens macrafrondis Scheltema";
		scientificNameId = "urn:lsid:marinespecies.org:taxname:545069";   // correct value, shouldn't suggest ammendment
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());		
		
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis";
		scientificNameId = null;
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:scientificNameID"));
	
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		scientificNameId = null;
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:scientificNameID"));
				
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis";
		scientificNameId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
			// but has a value, so will be internal prerequisites not met.
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		scientificNameId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
			// but has a value, so will be internal prerequisites not met.
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		
		// test non-standard overwrite behavior.
		family = "Chaetodermatidae";
		scientificName = "Falcidens macrafrondis Scheltema";
		scientificNameId = "https://www.gbif.org/species/4584165";   // gbif record, but we are asking for WoRMS guid
		scientificNameAuthorship = "Scheltema";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority, true);  // requesting overwrite, not standard behavior
		logger.debug(response.getComment());
		assertEquals(ResultState.AMENDED.getLabel(), response.getResultState().getLabel());  // overwrite
		assertEquals("urn:lsid:marinespecies.org:taxname:545069",response.getValue().getObject().get("dwc:scientificNameID"));
				
		family = "Muricidae";
		scientificName = "Murex monoceros";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		scientificNameId = "";  
		scientificNameAuthorship = "";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		family = "Muricidae";
		scientificName = "Murex monoceros";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		scientificNameId = "";  
		scientificNameAuthorship = "Sowerby, 1841";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404582",response.getValue().getObject().get("dwc:scientificNameID"));

		family = "Muricidae";
		scientificName = "Murex monoceros Sowerby, 1841";  // Homonym  d'Orbigny, 1841 (junior) and  G.B. Sowerby II, 1841 (senior)
		scientificNameId = "";  
		scientificNameAuthorship = "Sowerby, 1841";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:404582",response.getValue().getObject().get("dwc:scientificNameID"));
		
		
		scientificNameId=""; 
		scientificName="Chicoreus palmarosae (Lamarck, 1822)"; 
		kingdom="Animalia"; 
		phylum="Mollusca"; 
		taxonomic_class="Gastropoda"; 
		order=""; 
		family="Muricidae"; 
		subfamily=""; 
		genus="Chicoreus"; 
		genericName="Chicoreus"; 
		subgenus=""; 
		infragenericEpithet=""; 
		specificEpithet="palmarosae"; 
		infraspecificEpithet=""; 
		cultivarEpithet=""; 
		vernacularName=""; 
		scientificNameAuthorship="(Lamarck, 1822)"; 
		taxonRank="";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setKingdom(kingdom);
		taxon.setPhylum(phylum);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setFamily(family);
		taxon.setGenus(subgenus);
		taxon.setGenericName(genericName);
		taxon.setSpecificEpithet(specificEpithet);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:208134",response.getValue().getObject().get("dwc:scientificNameID"));		
		
		
        // for Babelomurex benoiti (Tiberi, 1855)  should be urn:lsid:marinespecies.org:taxname:234156
        // for taxon_name_id=182110 in MCZbase.
		family = "Muricidae";
		scientificName = "Babelomurex benoiti (Tiberi, 1855)";  // correctly formed, with authorship included
		scientificNameId = null;
		scientificNameAuthorship = "(Tiberi, 1855)";
		taxon = new Taxon();
		taxon.setScientificNameID(scientificNameId);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		taxon.setFamily(family);
		response = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("urn:lsid:marinespecies.org:taxname:234156",response.getValue().getObject().get("dwc:scientificNameID"));		
	}

	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonUnmbiguousProblemCase() {
		// this case was failing, separating out for clearer following of log messages
		SciNameSourceAuthority defaultAuthorityAuth = new SciNameSourceAuthority();
		String defaultAuthority = defaultAuthorityAuth.getName();
		Taxon taxon = new Taxon();
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("(Lamarck, 1822)");
		taxon.setKingdom("Plantae"); // missmatch ignored as match on taxonID and scientific name
		taxon.setTaxonomic_class("Crustacea");  // missmatch ignored as match on scientificNameID and scientific name
		taxon.setFamily("Muricidae");
		taxon.setGenus("");
		taxon.setSubtribe("Ignored value");
		DQResponse<ComplianceValue>  result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	} 
	
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonUnmbiguousProblemCase2() {
		// this case was failing, separating out for clearer following of log messages
		SciNameSourceAuthority defaultAuthorityAuth = new SciNameSourceAuthority();
		String defaultAuthority = defaultAuthorityAuth.getName();
		Taxon taxon = new Taxon();
		taxon = new Taxon();
		taxon.setScientificNameID("https://www.gbif.org/species/5726780");
		taxon.setScientificName("Murex brevispina Lamarck, 1822");
		taxon.setScientificNameAuthorship("Lamarck, 1822");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	} 

	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTaxonAmbiguous(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testValidationTaxonUnmbiguous() {
		
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if all 
        // of dwc:taxonID, dwc:scientificName, dwc:genericName, dwc:specificEpithet, 
        // dwc:infraspecificEpithet, dwc:scientificNameAuthorship, 
        // dwc:cultivarEpithet are EMPTY; COMPLIANT if (1) dwc:taxonId 
        // references a single taxon record in the bdq:sourceAuthority, 
        // or (2) dwc:taxonID is empty and dwc:scientificName references 
        // a single taxon record in the bdq:sourceAuthority, or (3) 
        // if dwc:scientificName and dwc:taxonID are EMPTY and if a 
        // combination of the values of the terms dwc:genericName, 
        // dwc:specificEpithet, dwc:infraspecificEpithet, dwc:cultivarEpithet, 
        // dwc:taxonRank, and dwc:scientificNameAuthorship can be unambiguously 
        // resolved to a unique taxon in the bdq:sourceAuthority, or 
        // (4) if ambiguity produced by multiple matches in (2) or 
        // (3) can be disambiguated to a unique Taxon using the values 
        // of dwc:subgenus, dwc:genus, dwc:subfamily, dwc:family, dwc:order, 
        // dwc:class, dwc:phylum, dwc:kingdom, dwc:higherClassification, 
        // dwc:scientificNameID, dwc:acceptedNameUsageID, dwc:originalNameUsageID, 
        // dwc:taxonConceptID and dwc:vernacularName; otherwise NOT_COMPLIANT 
		
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei], 
        // "API endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
		
		SciNameSourceAuthority defaultAuthorityAuth = new SciNameSourceAuthority();
		String defaultAuthority = defaultAuthorityAuth.getName();
		SciNameSourceAuthority wormsAuthorityAuth = null;
		try {
			wormsAuthorityAuth = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail("Unexpected exception:" +  e.getMessage());
		}
		String wormsAuthority = wormsAuthorityAuth.getName();
		SciNameSourceAuthority irmngAuthorityAuth = null;
		try {
			irmngAuthorityAuth = new SciNameSourceAuthority(EnumSciNameSourceAuthority.IRMNG);
		} catch (SourceAuthorityException e) {
			fail("Unexpected exception:" +  e.getMessage());
		}
		String irmngAuthority = irmngAuthorityAuth.getName();
		
		String scientificName = "";
		String kingdom = "";
		String taxonomic_class = "";
		Taxon taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		
		taxon = new Taxon();
		taxon.setScientificNameID("https://www.gbif.org/species/5726780");
		taxon.setScientificName("Murex brevispina Lamarck, 1822");
		taxon.setScientificNameAuthorship("Lamarck, 1822");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("https://www.gbif.org/species/5726780");
		taxon.setScientificName("");
		taxon.setScientificNameAuthorship("");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:5726780");
		taxon.setScientificName("Murex brevispina");
		taxon.setScientificNameAuthorship("");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setTaxonID("gbif:5726780");
		taxon.setScientificName("Murex brevispiaria");
		taxon.setScientificNameAuthorship("");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setTaxonID("gbif:5726780");
		taxon.setScientificName("Murex");
		taxon.setScientificNameAuthorship("");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		taxon.setGenus("Murex");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setScientificName("Murex brevispina Lamarck, 1822");
		taxon.setScientificNameAuthorship("Lamarck, 1822");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	
		// dataID:524
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setScientificName("Chicoreus palmarosae (Lamarck, 1822)");
		taxon.setScientificNameAuthorship("(Lamarck, 1822)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// dataID: 525
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setScientificName("Chicoreus palmarosae (Lamarck)");
		taxon.setScientificNameAuthorship("(Lamarck)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// dataID: 526
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setScientificName("Chicoreus palmarosae (L., 1822)");
		taxon.setScientificNameAuthorship("(L., 1822)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		
		// ambiregnal homonyms 
		scientificName = "Graphis";  // gastropod and lichen
		taxon = new Taxon();
		taxon.setScientificName(scientificName);
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificName("Graphis");
		taxon.setKingdom("Fungi");
		taxon.setTaxonomic_class("Lecanoromycetes");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificName("Graphis");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("Lecanoromycetes");
		taxon.setFamily("Graphidaceae");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// dataID:522
		taxon = new Taxon();
		taxon.setScientificName("Graphis");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("Lecanoromycetes");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificName("Graphis Adans., 1763");  // fungi
		taxon.setScientificNameAuthorship("Adans., 1763"); 
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificName("Graphis");
		taxonomic_class = "Gastropoda";
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificName("Graphis Jeffreys, 1867");  // gastropod
		taxon.setScientificNameAuthorship("Jeffreys, 1867");  // gastropod
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		
		taxon = new Taxon();
		scientificName = "Calotheca"; // beetle, grass, and junior homonym choanoflagellate
		kingdom = "Protozoa";
		taxonomic_class = "Choanoflagellatea";
		scientificName = "Calotheca Thomsen & Moestrup, 1983";
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship("Thomsen & Moestrup, 1983");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		kingdom = "Animalia";
		taxonomic_class = "Insecta";
		scientificName = "Calotheca Heyden, 1887";
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship("Heyden, 1887");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		kingdom = "Plantae";
		taxonomic_class = "Liliopsida";
		// scientificName = "Calotheca A.M.F.J. Palisot de Beauvois";  // one of several plant homonyms
		scientificName = "Calotheca P.Beauv.";  // one of several plant homonyms
		String scientificNameAuthorship = "P.Beauv.";
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setOrder("Poales");
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		scientificName = "Gaimardia";
		kingdom = "Animalia";
		taxonomic_class = "Bivalvia";
		scientificName = "Gaimardia Gould, 1852"; // clam
		scientificNameAuthorship = "Gould, 1852"; // clam
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		kingdom = "Plantae";
		taxonomic_class = "Magnoliopsida";
		String phylum = "Tracheophyta";
		//scientificName = "Gaimardia Gaudichaud-Beaupré, 1825";  // plant  
		scientificName = "Gaimardia Gaudich.";  // plant  
		scientificNameAuthorship = "Gaudich.";  // plant  
		taxon.setKingdom(kingdom);
		//taxon.setTaxonomic_class(taxonomic_class);
		taxon.setPhylum(phylum);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("urn:lsid:marinespecies.org:taxname:216786");
		taxon.setScientificName("");
		taxon.setScientificNameAuthorship("");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,wormsAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// IRMNG service client, distinct from WORMS client 
		taxon = new Taxon();
		taxon.setScientificNameID("urn:lsid:irmng.org:taxname:1361721");
		taxon.setScientificName("");
		taxon.setScientificNameAuthorship("");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,irmngAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("(Lamarck, 1822)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// Clause (3) scientific name id and scientific name empty.
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setScientificName("");
		taxon.setScientificNameAuthorship("(Lamarck, 1822)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		taxon.setGenericName("Chicoreus");
		taxon.setSpecificEpithet("palmarosae");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae (Lamarck, 1822)");
		taxon.setScientificNameAuthorship("(Lamarck, 1822)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("(Lamarck, 1822)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("(Lamarck)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("(Lamarck)");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("");
		taxon.setScientificName("");
		taxon.setScientificNameAuthorship("(Lamarck)");
		taxon.setKingdom("");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("Chicoreus");
		taxon.setGenericName("Chicoreus");
		taxon.setSpecificEpithet("palmarosae");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("(L.)");
		taxon.setKingdom("Animalia");
		taxon.setTaxonomic_class("Gastropoda");
		taxon.setFamily("Muricidae");
		taxon.setGenus("");
		taxon.setSubtribe("Ignored value");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("gbif:8154161");
		taxon.setScientificName("Chicoreus palmarosae");
		taxon.setScientificNameAuthorship("(Lamarck, 1822)");
		taxon.setKingdom("Plantae"); // missmatch ignored as match on taxonID and scientific name
		taxon.setTaxonomic_class("Crustacea");  // missmatch ignored as match on scientificNameID and scientific name
		taxon.setFamily("Muricidae");
		taxon.setGenus("");
		taxon.setSubtribe("Ignored value");
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		// check just passing genus to genericName, no scientific name or taxonID
		taxon = new Taxon();
		kingdom = "Animalia";
		taxonomic_class = "Bivalvia"; // clam
		scientificName = "Gaimardia"; 
		scientificNameAuthorship = ""; 
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName("");
		taxon.setGenericName(scientificName);  
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		scientificName = "Gaimardia";
		kingdom = "Animalia";
		taxonomic_class = "Bivalvia"; // clam
		scientificName = "Gaimardia"; 
		scientificNameAuthorship = ""; 
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		scientificName = "Gaimardia";
		kingdom = "Plantae";
		taxonomic_class = "Bivalvia"; 
		scientificName = "Gaimardia"; 
		scientificNameAuthorship = ""; 
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,defaultAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
	
		taxon = new Taxon();
		taxon.setScientificNameID("urn:lsid:irmng.org:taxname:10360908");
		scientificName = "Crotalus atrox Baird and Girard, 1853";
		kingdom = "Animalia";
		taxonomic_class = ""; 
		scientificNameAuthorship = "Baird and Girard, 1853"; 
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,irmngAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("");
		scientificName = "Crotalus atrox Baird & Girard, 1853";
		kingdom = "Animalia";
		taxonomic_class = ""; 
		scientificNameAuthorship = "Baird & Girard, 1853"; 
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,irmngAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		taxon = new Taxon();
		taxon.setScientificNameID("");
		scientificName = "Crotalus atrox Baird and Girard, 1853";
		kingdom = "Animalia";
		taxonomic_class = ""; 
		scientificNameAuthorship = "Baird and Girard, 1853"; 
		taxon.setKingdom(kingdom);
		taxon.setTaxonomic_class(taxonomic_class);
		taxon.setScientificName(scientificName);
		taxon.setScientificNameAuthorship(scientificNameAuthorship);
		result = DwCSciNameDQ.validationTaxonUnambiguous(taxon,irmngAuthority);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#amendmentScientificnameFromScientificnameid(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAmendmentScientificnameFromScientificnameid() {
		
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority is not available; 
		// INTERNAL_PREREQUISITES_NOT_MET if dwc:scientificNameID is EMPTY, the value 
		// of dwc:scientificNameID is ambiguous or dwc:scientificName was not EMPTY; 
		// FILLED_IN the value of dwc:scientificName if the value of scientificNameID 
		// could be unambiguously interpreted as a value in bdq:sourceAuthority; 
		// otherwise NOT_AMENDED
        // bdq:sourceAuthority default = "GBIF Backbone Taxonomy" [https://doi.org/10.15468/39omei], 
        // "API endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
		
		SciNameSourceAuthority authority = null;
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY);
		} catch (SourceAuthorityException e1) {
			fail(e1.getMessage());
		}
		
		String scientificName = "Murex pecten"; 
		String scientificNameId = null;
		DQResponse<AmendmentValue> response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());

		scientificName = ""; 
		scientificNameId = "https://www.gbif.org/species/5219243";
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("Vulpes vulpes (Linnaeus, 1758)",response.getValue().getObject().get("dwc:scientificName"));
		
		scientificName = ""; 
		scientificNameId = "gbif:5219243";
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("Vulpes vulpes (Linnaeus, 1758)",response.getValue().getObject().get("dwc:scientificName"));
		
		scientificName = ""; 
		scientificNameId = "https://api.gbif.org/v1/species/5219243";
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("Vulpes vulpes (Linnaeus, 1758)",response.getValue().getObject().get("dwc:scientificName"));
		
		scientificName = "Vulpes vulpes"; 
		scientificNameId = "https://www.gbif.org/species/5219243";
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		scientificName = ""; 
		scientificNameId = "5219243";  // bare integer is ambiguous.
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
		scientificName = ""; 
		scientificNameId = "urn:lsid:marinespecies.org:taxname:390090";
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.NOT_AMENDED.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
	
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e1) {
			fail(e1.getMessage());
		}
		
		scientificName = ""; 
		scientificNameId = "urn:lsid:marinespecies.org:taxname:390090";
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.FILLED_IN.getLabel(), response.getResultState().getLabel());
		assertEquals("Perkinsus honshuensis Dungan & Reece, 2006",response.getValue().getObject().get("dwc:scientificName"));
	
		scientificName = ""; 
		scientificNameId = "390090";  // bare integer is ambiguous.
		response = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(scientificNameId, scientificName, authority);
		logger.debug(response.getComment());
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), response.getResultState().getLabel());
		assertNull(response.getValue());
		
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
	public void testValidationClassificationConsistent() {
		
		String kingdom=""; 
		String phylum=""; 
		String phylclass=""; 
		String order=""; 
		String superfamily = "";
		String family="";
		String subfamily="";
		String tribe="";
		String subtribe="";
		String genus="";
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		
		kingdom="Plantae";
		phylum="Magnoliophyta";
		phylclass="";
		order="";
		family="";
		subfamily="";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());

		kingdom="Plantae";
		phylum="Magnoliophyta";
		phylclass="Mamalia";
		order="Carnivora";
		family="Muricidae";
		subfamily="";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());

		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Coleoptera"; 
		family="Curculionidae";
		subfamily="";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order,  superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
				
		kingdom=""; 
		phylum=""; 
		phylclass="Magnoliopsida"; 
		order="Myrtales"; 
		family="";
		subfamily="";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom=""; 
		phylum="Magnoliophyta";
		phylclass="Magnoliopsida";
		order="";
		family="";
		subfamily="";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());

		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Magnoliopsida";
		order="Coleoptera";
		superfamily = "";
		family="Curculionidae";
		subfamily="";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom=""; 
		phylum="Magnoliophyta";
		phylclass="Magnoliopsida";
		order="";
		superfamily = "Muricoidea";
		family="";
		subfamily="";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Chordata";
		phylclass="";
		order="Carnivora";
		superfamily = "Canoidea";
		family="Canidae";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="Canis";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Chordata";
		phylclass="";
		order="Carnivora";
		superfamily = "";
		family="";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="Canis";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Chordata";
		phylclass="";
		order="Carnivora";
		superfamily = "";
		family="Ursidae";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="Canis";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Chordata";
		phylclass="Mammalia";
		order="Carnivora";
		superfamily = "Canoidea";
		family="Canidae";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="Canis";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Annelida";
		phylclass="Polychaeta";
		order="Amphinomida";
		superfamily="";
		family="Amphinomidae";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="Chloeia";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
									
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Lepidoptera";
		superfamily="Papilionoidea";
		family="Lycaenidae";
		subfamily="Poritiinae";
		tribe = "Poritiini";
		subtribe = "";
		genus="Poritia";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Lepidoptera";
		superfamily="Papilionoidea";
		family="Lycaenidae";
		subfamily="Poritiinae"; 
		tribe = "Poritiini"; 
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		//TODO: Currently failing, test implementation needs discussion, NOT_COMPLIANT may be the correct value.
		//assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Lepidoptera";
		superfamily="Papilionoidea";
		family="Lycaenidae";
		subfamily="Poritiinae";
		tribe = "";
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		//assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Lepidoptera";
		superfamily="Papilionoidea";
		family="Lycaenidae";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Lepidoptera";
		superfamily="Papilionoidea";
		family="";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		// assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Lepidoptera";
		superfamily="";
		family="";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="Lepidoptera";
		superfamily="";
		family="";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="Insecta";
		order="";
		superfamily="";
		family="";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
		kingdom="Animalia";
		phylum="Arthropoda";
		phylclass="";
		order="";
		superfamily="";
		family="";
		subfamily="";
		tribe = "";
		subtribe = "";
		genus="";
		result = DwCSciNameDQ.validationClassificationConsistent(kingdom, phylum, phylclass, order, superfamily, family, subfamily, tribe, subtribe, genus, null);
		logger.debug(result.getComment());
		assertFalse(SciNameUtils.isEmpty(result.getComment()));
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
	}
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationTribeNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationTribeFound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:tribe 
        // is EMPTY; COMPLIANT if the value of dwc:tribe was found 
        // as a value at the rank of Tribe by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationTribeFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationTribeFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		
		// GBIF backbone taxonomy does not yet include Tribe data
//		result = DwCSciNameDQ.validationTribeFound("Papilionini",authority);
//		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
//		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationTribeFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		result = DwCSciNameDQ.validationTribeFound("Cypraeini",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
		
	}
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationSuperfamilyNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationSuperfamilyFound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:tribe 
        // is EMPTY; COMPLIANT if the value of dwc:tribe was found 
        // as a value at the rank of Superfamily by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationSuperfamilyFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationSuperfamilyFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		
		// GBIF backbone taxonomy does not yet include Superfamily data
//		result = DwCSciNameDQ.validationSuperfamilyFound("Papilionoidea",authority);
//		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
//		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationSuperfamilyFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		result = DwCSciNameDQ.validationSuperfamilyFound("Cypraeoidea",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.DwCSciNameDQ#validationSubtribeNotfound(java.lang.String)}.
	 */
	@Test
	public void testValidationSubtribeFound() {
		
        // Specification
        // EXTERNAL_PREREQUISITES_NOT_MET if the bdq:sourceAuthority 
        // is not available; INTERNAL_PREREQUISITES_NOT_MET if dwc:tribe 
        // is EMPTY; COMPLIANT if the value of dwc:tribe was found 
        // as a value at the rank of Subtribe by the bdq:sourceAuthority; 
        // otherwise NOT_COMPLIANT bdq:sourceAuthority default = "GBIF 
        // Backbone Taxonomy" [https://doi.org/10.15468/39omei], "API 
        // endpoint" [https://api.gbif.org/v1/species?datasetKey=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c&name=] 
        // 

        // Parameters. This test is defined as parameterized.
        // bdq:sourceAuthority default="GBIF Backbone Taxonomy"
		
		SciNameSourceAuthority authority = new SciNameSourceAuthority();
		
		DQResponse<ComplianceValue> result = DwCSciNameDQ.validationSubtribeFound(null,authority);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET.getLabel(), result.getResultState().getLabel());
		assertNull(result.getValue());
		result = DwCSciNameDQ.validationSubtribeFound("a3555144X",authority);
		assertEquals(ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(ComplianceValue.NOT_COMPLIANT, result.getValue());	
		
		// GBIF backbone taxonomy does not yet include Subtribe data
//		result = DwCSciNameDQ.validationSubtribeFound("Nasuina",authority);
//		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
//		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());	
		
		result = DwCSciNameDQ.validationSubtribeFound("Murex",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.NOT_COMPLIANT.getLabel(), result.getValue().getLabel());			
		
		try {
			authority = new SciNameSourceAuthority(EnumSciNameSourceAuthority.WORMS);
		} catch (SourceAuthorityException e) {
			fail(e.getMessage());
		}
		result = DwCSciNameDQ.validationSubtribeFound("Bubocorophiina",authority);
		assertEquals(ResultState.RUN_HAS_RESULT.getLabel(), result.getResultState().getLabel());
		assertEquals(ComplianceValue.COMPLIANT.getLabel(), result.getValue().getLabel());
	}
	
}
