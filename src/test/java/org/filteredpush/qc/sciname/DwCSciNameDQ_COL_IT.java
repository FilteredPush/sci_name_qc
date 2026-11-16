package org.filteredpush.qc.sciname;

import static org.junit.Assert.*;

import java.util.List;

import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.api.result.AmendmentValue;
import org.datakurator.ffdq.api.result.ComplianceValue;
import org.datakurator.ffdq.model.ResultState;
import org.filteredpush.qc.sciname.services.COLService;
import org.junit.Before;
import org.junit.Test;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * Integration tests (live, against the ChecklistBank API) of the Tests that consult a taxonomic
 * bdqval:sourceAuthority, with the Catalogue of Life (latest release) as the source authority
 * (tdwg/bdq#345).
 */
public class DwCSciNameDQ_COL_IT {

	private SciNameSourceAuthority col;

	@Before
	public void setUp() throws SourceAuthorityException {
		col = new SciNameSourceAuthority("CatalogOfLife:Latest");
	}

	@Test
	public void testCOLService() throws Exception {
		COLService service = new COLService();
		List<NameUsage> murex = service.lookupTaxonAtRank("Murex", "Genus");
		assertTrue(murex.size() >= 1);
		assertEquals("Muricidae", murex.get(0).getFamily());
		assertEquals("Animalia", murex.get(0).getKingdom());
		assertEquals(0, service.lookupTaxonAtRank("Murex", "Family").size());
		NameUsage byId = service.lookupTaxonByID("44LVK");
		assertEquals("Murex brevispina", byId.getScientificName());
		assertEquals("Lamarck, 1822", byId.getAuthorship());
		assertEquals("Neogastropoda", byId.getOrder());
		assertNull(service.lookupTaxonByID("NOSUCHID0"));
		assertTrue(service.lookupSynonyms("622TP").stream().anyMatch(s -> s.getCanonicalName().equals("Compositae")));
	}

	@Test
	public void testPinnedRelease() throws SourceAuthorityException {
		// The Annual Checklist 2025, by its ChecklistBank release alias and by its dataset key.
		for (String pinned : new String[] { "CatalogOfLife:COL25", "CatalogOfLife:310463" }) {
			SciNameSourceAuthority release = new SciNameSourceAuthority(pinned);
			assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationGenusFound("Murex", release));
			assertTrue(DwCSciNameDQ.validationGenusFound("Murex", release).getComment().contains(pinned.substring("CatalogOfLife:".length())));
		}
		// Magnoliophyta is a phylum (synonym) in the latest release, not in the Annual Checklist 2025.
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationPhylumFound("Magnoliophyta", new SciNameSourceAuthority("CatalogOfLife:COL25")));
	}

	@Test
	public void testHigherTaxaFound() {
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationKingdomFound("Animalia", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationKingdomFound("Animal", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationPhylumFound("Mollusca", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationPhylumFound("Gastropoda", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationClassFound("Gastropoda", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationOrderFound("Neogastropoda", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationOrderFound("Muricidae", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationFamilyFound("Muricidae", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationGenusFound("Murex", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationGenusFound("Murexx", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationSuperfamilyFound("Muricoidea", col));
		DQResponse<ComplianceValue> empty = DwCSciNameDQ.validationGenusFound("", col);
		assertEquals(ResultState.INTERNAL_PREREQUISITES_NOT_MET, empty.getResultState());
	}

	@Test
	public void testScientificNameFound() {
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Murex brevispina Lamarck, 1822", col));
		// Without authorship, an exact match on the name (as validation data dataID 230 expects).
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Murex brevispina", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Murex notarealspecies", col));
	}

	@Test
	public void testAmendmentScientificnameidFromTaxon() {
		Taxon taxon = new Taxon();
		taxon.setScientificName("Murex brevispina");
		taxon.setScientificNameAuthorship("Lamarck, 1822");
		taxon.setFamily("Muricidae");
		DQResponse<AmendmentValue> result = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon, col);
		assertEquals(result.getComment(), ResultState.FILLED_IN, result.getResultState());
		assertEquals("https://www.catalogueoflife.org/data/taxon/44LVK", result.getValue().getObject().get("dwc:scientificNameID"));
	}

	@Test
	public void testAmendmentScientificnameFromScientificnameid() {
		for (String id : new String[] { "https://www.catalogueoflife.org/data/taxon/44LVK", "col:44LVK" }) {
			DQResponse<AmendmentValue> result = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(id, "", col);
			assertEquals(result.getComment(), ResultState.FILLED_IN, result.getResultState());
			assertEquals("Murex brevispina Lamarck, 1822", result.getValue().getObject().get("dwc:scientificName"));
		}
		DQResponse<AmendmentValue> gbif = DwCSciNameDQ.amendmentScientificnameFromScientificnameid("gbif:5192826", "", col);
		assertEquals(ResultState.NOT_AMENDED, gbif.getResultState());
	}

	@Test
	public void testTaxonUnambiguous() {
		Taxon byId = new Taxon();
		byId.setScientificNameID("https://www.catalogueoflife.org/data/taxon/44LVK");
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(byId, "Catalogue of Life"));
		Taxon byName = new Taxon();
		byName.setScientificName("Murex brevispina");
		byName.setFamily("Muricidae");
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(byName, "Catalogue of Life"));
	}

	@Test
	public void testClassificationConsistent() {
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationClassificationConsistent(
				"Animalia", "Mollusca", "Gastropoda", "Neogastropoda", "", "Muricidae", "", "", "", "", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationClassificationConsistent(
				"Plantae", "Mollusca", "Gastropoda", "Neogastropoda", "", "Muricidae", "", "", "", "", col));
	}

	private static void assertCompliance(ComplianceValue expected, DQResponse<ComplianceValue> result) {
		assertEquals(result.getComment(), ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(result.getComment(), expected, result.getValue());
	}
}
