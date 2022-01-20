/**
 * 
 */
package org.filteredpush.qc.sciname.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.gbif.api.vocabulary.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * @author mole
 *
 */
public class GBIFServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

//	/**
//	 * Test method for {@link org.filteredpush.qc.sciname.services.GBIFService#fetchTaxon(java.lang.String, java.lang.String)}.
//	 */
//	@Test
//	public void testFetchTaxon() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link org.filteredpush.qc.sciname.services.GBIFService#searchForTaxon(java.lang.String, java.lang.String)}.
//	 */
//	@Test
//	public void testSearchForTaxon() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link org.filteredpush.qc.sciname.services.GBIFService#searchForSpecies(java.lang.String, java.lang.String)}.
//	 */
//	@Test
//	public void testSearchForSpecies() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.services.GBIFService#searchForGenus(java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public void testSearchForGenus() {
		try {
			int requestedRecords = 1;
			String result = GBIFService.searchForGenus("Murex", GBIFService.KEY_GBIFBACKBONE,requestedRecords);
			System.out.println(result);
			List<NameUsage> resultList = GBIFService.parseAllNameUsagesFromJSON(result);
			assertEquals(requestedRecords, resultList.size());
			assertEquals("Murex",resultList.get(0).getCanonicalName());
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException " + e.getMessage());
		}
		try {
			int requestedRecords = 50;
			String result = GBIFService.searchForGenus("Murex", GBIFService.KEY_GBIFBACKBONE,requestedRecords);
			System.out.println(result);
			List<NameUsage> resultList = GBIFService.parseAllNameUsagesFromJSON(result);
			Iterator<NameUsage> i = resultList.iterator();
			while (i.hasNext()) {
				NameUsage usage = i.next();
				if (usage.getTaxonomicStatus()==TaxonomicStatus.ACCEPTED.name() && usage.getFamily() == "Muricidae") {
					assertEquals("Murex",usage.getCanonicalName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException " + e.getMessage());
		}		
	}

//	/**
//	 * Test method for {@link org.filteredpush.qc.sciname.services.GBIFService#fetchSynonyms(int, java.lang.String)}.
//	 */
//	@Test
//	public void testFetchSynonyms() {
//		fail("Not yet implemented");
//	}

}
