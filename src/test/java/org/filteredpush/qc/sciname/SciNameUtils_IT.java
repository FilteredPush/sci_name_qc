/** 
 * SciNameUtils_IT.java
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
 */
package org.filteredpush.qc.sciname;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.marinespecies.aphia.v1_0.handler.ApiException;

/**
 * Integration tests for SciNameUtils methods that invoke external services
 * 
 * @author mole
 *
 */
public class SciNameUtils_IT {

	private static final Log logger = LogFactory.getLog(SciNameUtils_IT.class);

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.SciNameUtils#sameOrSynonym(java.lang.String, java.lang.String, java.lang.String, org.filteredpush.qc.sciname.SciNameSourceAuthority)}.
	 */
	@Test
	public void testSameOrSynoym() {
		SciNameSourceAuthority sourceAuthority = new SciNameSourceAuthority();
		
		try {
			boolean result = SciNameUtils.sameOrSynonym("Murexiella levicula", "Favartia levicula", "Species", sourceAuthority);
			assertTrue(result);
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException e) {
			fail("Unexpected Exception " + e.getMessage());
		}
		try {
			boolean result = SciNameUtils.sameOrSynonym( "Favartia levicula", "Murexiella levicula", "Species", sourceAuthority);
			assertTrue(result);
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException e) {
			fail("Unexpected Exception " + e.getMessage());
		}
		try {
			boolean result = SciNameUtils.sameOrSynonym( "Favartia levicula", "Quercus alba", "Species", sourceAuthority);
			assertFalse(result);
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException e) {
			fail("Unexpected Exception " + e.getMessage());
		}
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.SciNameUtils#simpleWoRMSGuidLookup(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSimpleWoRMSGuidLookup() {
		String result = SciNameUtils.simpleWoRMSGuidLookup("Chicoreus", "Montfort, 1810");
		logger.debug(result);
		assertEquals("urn:lsid:marinespecies.org:taxname:205487", result);
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.SciNameUtils#simpleGBIFGuidLookup(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSimpleGBIFGuidLookup() {
		String result = SciNameUtils.simpleGBIFGuidLookup("Chicoreus", "Montfort, 1810");
		logger.debug(result);
		assertEquals("2304298", result.replace("/v1/", "").replaceAll("[^0-9]+", ""));
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.SciNameUtils#isSameClassificationInAuthority(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.filteredpush.qc.sciname.SciNameSourceAuthority)}.
	 */
	@Test
	public void testisSameClassificationInAuthority() { 
		SciNameSourceAuthority sourceAuthority = new SciNameSourceAuthority();
	
		String kingdom = "Animalia";
		String phylum = "Mollusca";
		String taxonomic_class = "Gastropoda";
		String order = "Neogastropoda";
		String family = "Muricidae";
		String subfamily = "";
		String genus = "Murex";
		BooleanWithComment result;
		try {
			result = SciNameUtils.isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, subfamily, genus, sourceAuthority);
			logger.debug(result.getComment());
			assertTrue(result.getBooleanValue());
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException | UnsupportedSourceAuthorityException e) {
			fail("unexpected exception" + e.getMessage());	
		}
		
		kingdom = "Animalia";
		phylum = "Mollusca";
		taxonomic_class = "Magnoliopsida";
		order = "Neogastropoda";
		family = "Muricidae";
		subfamily = "";
		genus = "Murex";
		try {
			result = SciNameUtils.isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, subfamily, genus, sourceAuthority);
			logger.debug(result.getComment());
			assertFalse(result.getBooleanValue());
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException | UnsupportedSourceAuthorityException e) {
			fail("unexpected exception" + e.getMessage());	
		}
		
		kingdom = "Animalia";
		phylum = "";
		taxonomic_class = "";
		order = "";
		family = "";
		subfamily = "";
		genus = "Murex";
		try {
			result = SciNameUtils.isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, subfamily, genus, sourceAuthority);
			logger.debug(result.getComment());
			assertTrue(result.getBooleanValue());
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException | UnsupportedSourceAuthorityException e) {
			fail("unexpected exception" + e.getMessage());	
		}

		kingdom = "Plantae";
		phylum = "Mollusca";
		taxonomic_class = "";
		order = "Lamiales";
		family = "";
		subfamily = "";
		genus = "Murex";
		try {
			result = SciNameUtils.isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, subfamily, genus, sourceAuthority);
			logger.debug(result.getComment());
			assertFalse(result.getBooleanValue());
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException | UnsupportedSourceAuthorityException e) {
			fail("unexpected exception" + e.getMessage());	
		}	
		
		kingdom = "";
		phylum = "Mollusca";
		taxonomic_class = "";
		order = "Lamiales";
		family = "";
		subfamily = "";
		genus = "Murex";
		try {
			result = SciNameUtils.isSameClassificationInAuthority(kingdom, phylum, taxonomic_class, order, family, subfamily, genus, sourceAuthority);
			logger.debug(result.getComment());
			assertFalse(result.getBooleanValue());
		} catch (IOException | ApiException | org.irmng.aphia.v1_0.handler.ApiException | UnsupportedSourceAuthorityException e) {
			fail("unexpected exception" + e.getMessage());	
		}		
		
	}

}
