/**
 * 
 */
package org.filteredpush.qc.sciname.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.SciNameUtils;
import org.gbif.api.vocabulary.TaxonomicStatus;
import org.junit.Test;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * @author mole
 *
 */
public class GBIFServiceTest {

	private static final Log logger = LogFactory.getLog(GBIFServiceTest.class);

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
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.services.GBIFService#searchForSpecies(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSearchForSpecies() {
		try {
			String result = GBIFService.searchForSpecies("Murex pecten Lightfoot, 1786", GBIFService.KEY_GBIFBACKBONE);
			System.out.println(result);
			List<NameUsage> resultList = GBIFService.parseAllNameUsagesFromJSON(result);
			assertTrue(resultList.size() > 0);
			Iterator<NameUsage> i = resultList.iterator();
			while (i.hasNext()) {
				NameUsage usage = i.next();
				if (usage.getTaxonomicStatus()==TaxonomicStatus.ACCEPTED.name() && usage.getFamily() == "Muricidae") {
					assertEquals("Murex pecten",usage.getCanonicalName());
					assertEquals("Murex", usage.getGenus());
				}
				assertEquals(GBIFService.KEY_GBIFBACKBONE,usage.getDatasetKey());
			}		
			result = GBIFService.searchForSpecies("Vulpes lagopus (Linnaeus, 1758)", GBIFService.KEY_GBIFBACKBONE);
			System.out.println(result);
			resultList.clear();
			resultList = GBIFService.parseAllNameUsagesFromJSON(result);
			assertTrue(resultList.size() > 0);
			i = resultList.iterator();
			while (i.hasNext()) {
				NameUsage usage = i.next();
				logger.debug(usage.getCanonicalName() + " " + usage.getAcceptedAuthorship() + " " + usage.getTaxonomicStatus() );
				if (usage.getTaxonomicStatus().equals(TaxonomicStatus.ACCEPTED.name())) {
					assertEquals("Vulpes lagopus",usage.getCanonicalName());
					assertEquals("Vulpes", usage.getGenus());
					assertEquals("(Linnaeus, 1758)",usage.getAuthorship());
			        logger.debug(usage.getAuthorship());
				}
				assertEquals(GBIFService.KEY_GBIFBACKBONE,usage.getDatasetKey());
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException " + e.getMessage());
		}
	}

	@Test
	public void testLookupGenus() {
		try {
			int requestedRecords = 1;
			List<NameUsage> resultList = GBIFService.lookupGenus("Murex", GBIFService.KEY_GBIFBACKBONE,requestedRecords);
			assertEquals(requestedRecords, resultList.size());
			assertEquals("Murex",resultList.get(0).getCanonicalName());
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException " + e.getMessage());
		}	
		try {
			int requestedRecords = 50;
			List<NameUsage> resultList = GBIFService.lookupGenus("Murex", GBIFService.KEY_GBIFBACKBONE,requestedRecords);
			Iterator<NameUsage> i = resultList.iterator();
			int murexCount = 0;
			while (i.hasNext()) {
				NameUsage usage = i.next();
				logger.debug(usage.getCanonicalName() + " " + usage.getAcceptedAuthorship() + " " + usage.getTaxonomicStatus() );
				if (usage.getTaxonomicStatus().equals(TaxonomicStatus.ACCEPTED.name()) && usage.getFamily().equals("Muricidae")) {
					if (usage.getCanonicalName().equals("Murex")) { 
						murexCount++;
					}
					if (usage.getGenus().equals("Murex")) {
						assertEquals("Murex",usage.getCanonicalName());
						assertEquals("Linnaeus, 1758", usage.getAcceptedAuthorship());
					} 
				}
				assertEquals(GBIFService.KEY_GBIFBACKBONE,usage.getDatasetKey());
			}
			assertEquals(1, murexCount);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException " + e.getMessage());
		}
	}
	
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
			logger.debug(result);
			List<NameUsage> resultList = GBIFService.parseAllNameUsagesFromJSON(result);
			Iterator<NameUsage> i = resultList.iterator();
			int murexCount = 0;
			while (i.hasNext()) {
				NameUsage usage = i.next();
				logger.debug(usage.getCanonicalName() + " " + usage.getAcceptedAuthorship() + " " + usage.getTaxonomicStatus() );
				if (usage.getTaxonomicStatus().equals(TaxonomicStatus.ACCEPTED.name()) && usage.getFamily().equals("Muricidae")) {
					if (usage.getCanonicalName().equals("Murex")) { 
						murexCount++;
					}
					if (usage.getGenus().equals("Murex")) {
						assertEquals("Murex",usage.getCanonicalName());
						assertEquals("Linnaeus, 1758", usage.getAcceptedAuthorship());
					} 
				}
				assertEquals(GBIFService.KEY_GBIFBACKBONE,usage.getDatasetKey());
			}
			assertEquals(1, murexCount);
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
