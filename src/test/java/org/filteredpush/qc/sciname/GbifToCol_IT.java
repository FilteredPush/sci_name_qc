package org.filteredpush.qc.sciname;

import static org.junit.Assert.*;

import org.filteredpush.qc.sciname.GbifToCol.Mapping;
import org.filteredpush.qc.sciname.GbifToCol.MatchType;
import org.junit.Test;

/**
 * Integration tests (live, against GBIF and the Catalogue of Life) of finding col: identifiers for gbif: identifiers.
 */
public class GbifToCol_IT {

	private final GbifToCol mapper = new GbifToCol("Latest");

	private void assertMapping(String gbif, String col, MatchType match) {
		Mapping mapping = mapper.map(gbif);
		assertEquals(gbif + ": " + mapping.remark, match, mapping.match);
		assertEquals(gbif + ": " + mapping.remark, col, mapping.colId);
	}

	@Test
	public void testGbifKey() {
		assertEquals(Integer.valueOf(2435099), GbifToCol.gbifKey("gbif:2435099"));
		assertEquals(Integer.valueOf(2435099), GbifToCol.gbifKey("https://www.gbif.org/species/2435099"));
		assertEquals(Integer.valueOf(2435099), GbifToCol.gbifKey("http://api.gbif.org/v1/species/2435099"));
		assertNull(GbifToCol.gbifKey("2435099"));
		assertNull(GbifToCol.gbifKey("col:4QHKG"));
	}

	@Test
	public void testMapping() {
		assertMapping("gbif:2435099", "col:4QHKG", MatchType.EXACT);                       // Puma concolor (Linnaeus, 1771)
		// A synonym maps to the COL usage of that name, not to COL's accepted name (Puma concolor).
		assertMapping("gbif:2435104", "col:3DXV5", MatchType.EXACT);                       // Felis concolor Linnaeus, 1771
		assertMapping("https://www.gbif.org/species/5726780", "col:44LVK", MatchType.EXACT); // Murex brevispina Lamarck, 1822
		// Homonyms, by kingdom.
		assertMapping("gbif:2480962", "col:5VCR", MatchType.EXACT);                        // Morus Vieillot, 1816
		assertMapping("gbif:2984545", "col:5VCQ", MatchType.EXACT);                        // Morus L.
		assertMapping("gbif:2300979", "col:8V5HQ", MatchType.EXACT);                       // Graphis Jeffreys, 1867
		assertMapping("gbif:2602041", "col:4QV4", MatchType.NAME_ONLY);                    // Graphis Adans., 1763: no authorship in COL
		// Forms of the same authorship: the ex author omitted in GBIF.
		assertMapping("gbif:8102122", "col:6LFPW", MatchType.SAME_AUTHORSHIP);             // Harpullia pendula F.Muell.
		// Citing only the author before ex is not the same authorship.
		assertMapping("gbif:8590675", "", MatchType.AUTHORSHIP_DIFFERS);                   // Harpullia pendula Planch.
		// A different year: not the same name.
		assertMapping("gbif:2113658", "", MatchType.AUTHORSHIP_DIFFERS);                   // Cyclops O.F.Müller, 1776
		assertMapping("gbif:999999999", "", MatchType.NOT_IN_GBIF);
		assertMapping("urn:lsid:marinespecies.org:taxname:138196", "", MatchType.NOT_GBIF_IDENTIFIER);
	}
}
