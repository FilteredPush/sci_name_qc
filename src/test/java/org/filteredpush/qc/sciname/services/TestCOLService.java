package org.filteredpush.qc.sciname.services;

import static org.junit.Assert.*;

import org.filteredpush.qc.sciname.EnumSciNameSourceAuthority;
import org.filteredpush.qc.sciname.SciNameSourceAuthority;
import org.filteredpush.qc.sciname.SourceAuthorityException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * Tests of the Catalogue of Life source authority that do not call the service.
 */
public class TestCOLService {

	@Test
	public void testSourceAuthorityStrings() throws SourceAuthorityException {
		for (String s : new String[] { "CatalogOfLife:Latest", "catalogoflife:latest", "Catalogue of Life", "COL", "col", "CATALOGUE_OF_LIFE", "https://www.catalogueoflife.org/",
				"https://api.checklistbank.org/dataset/3LR/", "https://api.checklistbank.org/dataset/3LR" }) {
			SciNameSourceAuthority authority = new SciNameSourceAuthority(s);
			assertEquals(s, EnumSciNameSourceAuthority.CATALOGUE_OF_LIFE, authority.getAuthority());
			assertEquals(s, COLService.LATEST_RELEASE, authority.getAuthoritySubDataset());
			assertFalse(authority.isGBIFChecklist());
		}
		assertEquals("CatalogOfLife:Latest", new SciNameSourceAuthority("COL").getName());
		// A specific release (pinned), by its ChecklistBank dataset key or release alias.
		for (String[] pin : new String[][] { { "CatalogOfLife:317005", "317005" }, { "CatalogOfLife:COL25", "COL25" },
				{ "CatalogOfLife:COL26.11", "COL26.11" }, { "https://api.checklistbank.org/dataset/317005/", "317005" } }) {
			SciNameSourceAuthority release = new SciNameSourceAuthority(pin[0]);
			assertEquals(EnumSciNameSourceAuthority.CATALOGUE_OF_LIFE, release.getAuthority());
			assertEquals(pin[1], release.getAuthoritySubDataset());
			assertEquals("CatalogOfLife:" + pin[1], release.getName());
		}
		// Not a pin target.
		try {
			new SciNameSourceAuthority("CatalogOfLife:not a release");
			fail("Expected SourceAuthorityException");
		} catch (SourceAuthorityException e) {
			// expected
		}
		// The COL checklist in GBIF remains a different authority.
		assertEquals(EnumSciNameSourceAuthority.GBIF_COL, new SciNameSourceAuthority("GBIF_COL").getAuthority());
	}

	@Test
	public void testIdFromScientificNameID() {
		assertEquals("44LVK", COLService.idFromScientificNameID("https://www.catalogueoflife.org/data/taxon/44LVK"));
		assertEquals("44LVK", COLService.idFromScientificNameID("http://www.catalogueoflife.org/data/taxon/44LVK"));
		assertEquals("44LVK", COLService.idFromScientificNameID("https://catalogueoflife.org/data/taxon/44LVK/"));
		assertEquals("44LVK", COLService.idFromScientificNameID("col:44LVK"));
		assertEquals("44LVK", COLService.idFromScientificNameID("https://identifiers.org/col:44LVK"));
		assertEquals("44LVK", COLService.idFromScientificNameID("https://bioregistry.io/col:44LVK"));
		assertNull(COLService.idFromScientificNameID("gbif:5192826"));
		assertNull(COLService.idFromScientificNameID("44LVK"));
		assertNull(COLService.idFromScientificNameID("urn:lsid:marinespecies.org:taxname:138196"));
		assertNull(COLService.idFromScientificNameID(null));
	}

	@Test
	public void testNameUsageFromSearchResult() throws Exception {
		// A synonym, as returned by GET /dataset/3LR/nameusage/search (abridged): the classification
		// is that of the accepted name, followed by the synonym itself.
		String json = "{\"id\":\"5T83V\",\"classification\":["
				+ "{\"id\":\"P\",\"name\":\"Plantae\",\"rank\":\"kingdom\"},"
				+ "{\"id\":\"TP\",\"name\":\"Tracheophyta\",\"rank\":\"phylum\"},"
				+ "{\"id\":\"MG\",\"name\":\"Magnoliopsida\",\"rank\":\"class\"},"
				+ "{\"id\":\"AS\",\"name\":\"Asterales\",\"rank\":\"order\"},"
				+ "{\"id\":\"622TP\",\"name\":\"Asteraceae\",\"rank\":\"family\"},"
				+ "{\"id\":\"5T83V\",\"name\":\"Compositae\",\"rank\":\"family\"}],"
				+ "\"usage\":{\"id\":\"5T83V\",\"status\":\"synonym\","
				+ "\"name\":{\"scientificName\":\"Compositae\",\"authorship\":\"Giseke\",\"rank\":\"family\"},"
				+ "\"accepted\":{\"id\":\"622TP\",\"name\":{\"scientificName\":\"Asteraceae\",\"authorship\":\"Bercht. & J.Presl\"}}}}";
		JSONObject item = (JSONObject) new JSONParser().parse(json);
		NameUsage usage = COLService.nameUsageFromSearchResult(item);
		assertEquals("Compositae", usage.getCanonicalName());
		assertEquals("Giseke", usage.getAuthorship());
		assertEquals("family", usage.getRank());
		assertEquals("SYNONYM", usage.getTaxonomicStatus());
		assertEquals("col:5T83V", usage.getGuid());
		assertEquals("Asteraceae", usage.getAcceptedName());
		assertEquals("622TP".hashCode(), usage.getAcceptedKey());
		assertEquals("Plantae", usage.getKingdom());
		assertEquals("Asterales", usage.getOrder());
		// The family of a synonym is that of its accepted name, not the synonym itself.
		assertEquals("Asteraceae", usage.getFamily());
		assertNull(COLService.nameUsage(new JSONObject(), new JSONArray()));
	}
}
