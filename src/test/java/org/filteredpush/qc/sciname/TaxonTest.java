/** 
 * TaxonTest.java
 * 
 * Copyright 2023 President and Fellows of Harvard College
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
import org.junit.Test;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * @author mole
 *
 */
public class TaxonTest {

	private static final Log logger = LogFactory.getLog(TaxonTest.class);

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.Taxon#Taxon(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testTaxonStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringStringString() {
		
		// confirm that order of fields is remaining unchanged.
		Taxon testTaxon = new Taxon("taxonID", "kingdom", "phylum", "taxonomic_class", "order", "family",
			"subfamily", "genus", "subgenus", "scientificName", "scientificNameAuthorship",
			"genericName", "specificEpithet", "infraspecificEpithet", "taxonRank",
			"cultivarEpithet", "higherClassification", "vernacularName", "taxonConceptID",
			"scientificNameID", "originalNameUsageID", "acceptedNameUsageID", "superfamily",
			"tribe", "subtribe");
		assertEquals("kingdom", testTaxon.getKingdom());
		assertEquals("phylum", testTaxon.getPhylum());
		assertEquals("taxonomic_class", testTaxon.getTaxonomic_class());
		assertEquals("order", testTaxon.getOrder());
		assertEquals("superfamily", testTaxon.getSuperfamily());
		assertEquals("family",testTaxon.getFamily());
		assertEquals("subfamily",testTaxon.getSubfamily());
		assertEquals("tribe", testTaxon.getTribe());
		assertEquals("subtribe", testTaxon.getSubtribe());
		assertEquals("scientificNameID", testTaxon.getScientificNameID());
		assertEquals("scientificName", testTaxon.getScientificName());
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.Taxon#sameHigherAs(edu.harvard.mcz.nametools.NameUsage)}.
	 */
	@Test
	public void testSameHigherAs() {
		Taxon testTaxon = new Taxon("taxonID", "kingdom", "phylum", "taxonomic_class", "order", "family",
				"subfamily", "genus", "subgenus", "scientificName", "scientificNameAuthorship",
				"genericName", "specificEpithet", "infraspecificEpithet", "taxonRank",
				"cultivarEpithet", "higherClassification", "vernacularName", "taxonConceptID",
				"scientificNameID", "originalNameUsageID", "acceptedNameUsageID", "superfamily",
				"tribe", "subtribe");
		NameUsage nameUsage = new NameUsage();
		nameUsage.setTclass("taxonomic_class");
		assertTrue(testTaxon.sameHigherAs(nameUsage));
		nameUsage.setKingdom("kingdom");
		assertTrue(testTaxon.sameHigherAs(nameUsage));
		nameUsage.setPhylum("phylum");
		nameUsage.setOrder("order");
		nameUsage.setFamily("family");
		assertTrue(testTaxon.sameHigherAs(nameUsage));
		nameUsage.setSubgenus("Foo");
		assertTrue(testTaxon.sameHigherAs(nameUsage));
		nameUsage.setFamily("Foo");
		assertFalse(testTaxon.sameHigherAs(nameUsage));
		testTaxon.setFamily("Foo");
		assertTrue(testTaxon.sameHigherAs(nameUsage));
	}
	
	@Test
	public void testPlausiblySameNameAs() { 
		Taxon testTaxon = new Taxon();
		testTaxon.setKingdom("Animalia");
		testTaxon.setScientificName("Chicoreus palmarosae (Lamarck, 1822)");
		testTaxon.setScientificNameAuthorship("(Lamarck, 1822)");
		NameUsage nameUsage = new NameUsage();
		nameUsage.setKingdom("Animaila");
		nameUsage.setScientificName("Chicoreus palmarosae (Lamarck, 1822)");
		nameUsage.setAuthorship("(Lamarck, 1822)");
		assertTrue(testTaxon.plausiblySameNameAs(nameUsage));
		
		testTaxon = new Taxon();
		testTaxon.setKingdom("Animalia");
		testTaxon.setScientificName("Chicoreus palmarosae");
		testTaxon.setScientificNameAuthorship("(Lamarck, 1822)");
		nameUsage = new NameUsage();
		nameUsage.setKingdom("Animaila");
		nameUsage.setScientificName("Chicoreus palmarosae");
		nameUsage.setAuthorship("(Lamarck, 1822)");
		assertTrue(testTaxon.plausiblySameNameAs(nameUsage));
		
		testTaxon = new Taxon();
		testTaxon.setKingdom("Animalia");
		testTaxon.setScientificName("Chicoreus palmarosae");
		testTaxon.setScientificNameAuthorship("");
		nameUsage = new NameUsage();
		nameUsage.setKingdom("Animaila");
		nameUsage.setScientificName("Chicoreus palmarosae");
		nameUsage.setAuthorship("");
		assertTrue(testTaxon.plausiblySameNameAs(nameUsage));
		
		testTaxon = new Taxon();
		testTaxon.setKingdom("Animalia");
		testTaxon.setScientificName("Chicoreus palmarosae");
		testTaxon.setScientificNameAuthorship("(Lamarck, 1822)");
		nameUsage = new NameUsage();
		nameUsage.setKingdom("Animaila");
		nameUsage.setScientificName("Chicoreus palmarosae");
		nameUsage.setAuthorship("(Lamarck)");
		assertTrue(testTaxon.plausiblySameNameAs(nameUsage));
		
		testTaxon = new Taxon();
		testTaxon.setKingdom("Animalia");
		testTaxon.setScientificName("Chicoreus palmarosae (Lamarck, 1822)");
		testTaxon.setScientificNameAuthorship("(Lamarck, 1822)");
		nameUsage = new NameUsage();
		nameUsage.setKingdom("Animaila");
		nameUsage.setScientificName("Chicoreus palmarosae");
		nameUsage.setAuthorship("(Lamarck)");
		assertTrue(testTaxon.plausiblySameNameAs(nameUsage));
		
		testTaxon = new Taxon();
		testTaxon.setKingdom("Animalia");
		testTaxon.setScientificName("Chicoreus palmarosae");
		testTaxon.setScientificNameAuthorship("(Lamarck, 1822)");
		nameUsage = new NameUsage();
		nameUsage.setKingdom("Animaila");
		nameUsage.setScientificName("Chicoreus palmarosae (Lamarck)");
		nameUsage.setAuthorship("(Lamarck)");
		assertTrue(testTaxon.plausiblySameNameAs(nameUsage));
		
		testTaxon = new Taxon();
		testTaxon.setKingdom("Animalia");
		testTaxon.setScientificName("Chicoreus palmarosae (Lamarck, 1822)");
		testTaxon.setScientificNameAuthorship("");
		nameUsage = new NameUsage();
		nameUsage.setKingdom("Animaila");
		nameUsage.setScientificName("Chicoreus palmarosae (Lamarck)");
		nameUsage.setAuthorship("");
		assertTrue(testTaxon.plausiblySameNameAs(nameUsage));
	}

}
