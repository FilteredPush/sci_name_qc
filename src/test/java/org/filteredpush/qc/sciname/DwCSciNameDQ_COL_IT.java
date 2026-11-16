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
	public void testSimilarNames() throws Exception {
		COLService service = new COLService();
		List<NameUsage> brevispinna = service.lookupSimilarNames("Murex brevispinna", null);
		assertEquals("Murex brevispina", brevispinna.get(0).getCanonicalName());
		assertEquals(IRI + "44LVK", brevispinna.get(0).getGuid());
		// Names with a different number of words (e.g. subspecies) are not candidates.
		assertTrue(brevispinna.stream().allMatch(u -> u.getCanonicalName().split(" ").length==2));
		assertEquals(IRI + "4QHKG", service.lookupSimilarNames("Puma concolour", null).get(0).getGuid());
		// Both homonyms of Morus are candidates for Morrus, at rank genus.
		List<NameUsage> morrus = service.lookupSimilarNames("Morrus", "Genus");
		assertTrue(morrus.stream().anyMatch(u -> u.getGuid().equals(IRI + "5VCQ")));
		assertTrue(morrus.stream().anyMatch(u -> u.getGuid().equals(IRI + "5VCR")));
		assertTrue(morrus.stream().allMatch(u -> u.getRank().equalsIgnoreCase("genus")));
		// An exact name is not its own candidate, and a name like nothing in COL has none.
		assertTrue(service.lookupSimilarNames("Morus", "Genus").stream().noneMatch(u -> u.getCanonicalName().equals("Morus")));
		assertEquals(0, service.lookupSimilarNames("Xqzvwkjhyt", null).size());
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

	private static final String IRI = COLService.TAXON_IRI;

	private static Taxon taxon(String kingdom, String family, String scientificName, String authorship) {
		Taxon taxon = new Taxon();
		if (kingdom!=null) { taxon.setKingdom(kingdom); }
		if (family!=null) { taxon.setFamily(family); }
		taxon.setScientificName(scientificName);
		if (authorship!=null) { taxon.setScientificNameAuthorship(authorship); }
		return taxon;
	}

	private static void assertFilledIn(String expectedId, DQResponse<AmendmentValue> result) {
		assertEquals(result.getComment(), ResultState.FILLED_IN, result.getResultState());
		assertEquals(result.getComment(), IRI + expectedId, result.getValue().getObject().get("dwc:scientificNameID"));
	}

	/**
	 * Ambiregnal homonyms: Morus L. (Plantae, mulberries, 5VCQ) and Morus Vieillot, 1816 (Animalia, gannets, 5VCR);
	 * Pieris D.Don (Plantae) and Pieris (Animalia, butterflies).  The name alone is found, but is ambiguous;
	 * the kingdom, family, or authorship distinguishes them.
	 */
	@Test
	public void testAmbiregnalHomonyms() {
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationGenusFound("Morus", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationGenusFound("Pieris", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Morus L.", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Morus Vieillot, 1816", col));

		// The name alone: two taxa, so not unambiguous, and no identifier is proposed.
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(taxon(null, null, "Morus", null), "CatalogOfLife:Latest"));
		assertEquals(ResultState.NOT_AMENDED, DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, null, "Morus", null), col).getResultState());

		// Distinguished by kingdom.
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(taxon("Plantae", null, "Morus", null), "CatalogOfLife:Latest"));
		assertFilledIn("5VCQ", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Plantae", null, "Morus", null), col));
		assertFilledIn("5VCR", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Animalia", null, "Morus", null), col));
		// Distinguished by family.
		assertFilledIn("5VCR", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, "Sulidae", "Morus", null), col));
		assertFilledIn("5VCQ", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, "Moraceae", "Morus", null), col));
		// Distinguished by authorship.
		assertFilledIn("5VCQ", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, null, "Morus", "L."), col));
		assertFilledIn("5VCR", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, null, "Morus", "Vieillot, 1816"), col));

		// The classification is checked against the homonym in the right kingdom.
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationClassificationConsistent(
				"Animalia", "", "", "", "", "Sulidae", "", "", "", "Morus", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationClassificationConsistent(
				"Plantae", "", "", "", "", "Moraceae", "", "", "", "Morus", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationClassificationConsistent(
				"Plantae", "", "", "", "", "Sulidae", "", "", "", "", col));
	}

	/**
	 * Homonyms in one kingdom.  Cyclops O.F. Müller, 1785 (a copepod, accepted, 7NV4D) and Cyclops Montfort, 1810
	 * (a gastropod, a synonym of Tritia, BDVPW).  Aaroniella badonneli (Danks, 1950) (accepted, 8HDV) and
	 * Aaroniella badonneli Thornton, 1990 (an ambiguous synonym of Aaroniella andrei, 8HDW), both Psocodea.
	 */
	@Test
	public void testHomonymsInOneKingdom() {
		assertFilledIn("BDVPW", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, null, "Cyclops", "Montfort, 1810"), col));
		assertFilledIn("7NV4D", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, "Cyclopidae", "Cyclops", null), col));
		assertEquals(ResultState.NOT_AMENDED, DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Animalia", null, "Cyclops", null), col).getResultState());

		assertFilledIn("8HDV", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, null, "Aaroniella badonneli", "(Danks, 1950)"), col));
		assertFilledIn("8HDW", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon(null, null, "Aaroniella badonneli", "Thornton, 1990"), col));
		// Without authorship the two cannot be told apart.
		assertEquals(ResultState.NOT_AMENDED, DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Animalia", null, "Aaroniella badonneli", null), col).getResultState());
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(taxon("Animalia", null, "Aaroniella badonneli", null), "CatalogOfLife:Latest"));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(taxon("Animalia", null, "Aaroniella badonneli", "Thornton, 1990"), "CatalogOfLife:Latest"));
	}

	/**
	 * A name that COL treats as a synonym is identified as that name, not as COL's accepted name for the taxon:
	 * Felis concolor Linnaeus, 1771 (3DXV5) is a synonym of Puma concolor (Linnaeus, 1771) (4QHKG);
	 * Compositae Giseke (5T83V) of Asteraceae (622TP).
	 */
	@Test
	public void testSynonymsKeepTheirOwnIdentifiers() throws Exception {
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Felis concolor Linnaeus, 1771", col));
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationFamilyFound("Compositae", col));

		DQResponse<AmendmentValue> felis = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Animalia", "Felidae", "Felis concolor", "Linnaeus, 1771"), col);
		assertFilledIn("3DXV5", felis);
		assertFilledIn("4QHKG", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Animalia", "Felidae", "Puma concolor", "(Linnaeus, 1771)"), col));
		assertFilledIn("5T83V", DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Plantae", null, "Compositae", null), col));

		// The identifier of the synonym gives back the synonym's name.
		DQResponse<AmendmentValue> name = DwCSciNameDQ.amendmentScientificnameFromScientificnameid(IRI + "3DXV5", "", col);
		assertEquals(ResultState.FILLED_IN, name.getResultState());
		assertEquals("Felis concolor Linnaeus, 1771", name.getValue().getObject().get("dwc:scientificName"));
		NameUsage usage = new COLService().lookupTaxonByID("3DXV5");
		assertEquals("Felis concolor", usage.getScientificName());
		assertEquals("SYNONYM", usage.getTaxonomicStatus());
		assertEquals("Puma concolor", usage.getAcceptedName());

		Taxon byId = new Taxon();
		byId.setScientificNameID(IRI + "3DXV5");
		assertCompliance(ComplianceValue.COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(byId, "CatalogOfLife:Latest"));
	}

	/**
	 * Misspelled names are not found, and are not silently replaced by a correctly spelled name.
	 */
	@Test
	public void testMisspellings() {
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Murex brevispinna Lamarck, 1822", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Puma concolour", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationGenusFound("Morrus", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationFamilyFound("Muricidea", col));
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationKingdomFound("Plantea", col));
		assertEquals(ResultState.NOT_AMENDED, DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Animalia", null, "Puma concolour", null), col).getResultState());
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationTaxonUnambiguous(taxon("Animalia", null, "Puma concolour", null), "CatalogOfLife:Latest"));
		// The misspellings are reported with the similar names in COL as candidates, without changing the result.
		DQResponse<ComplianceValue> puma = DwCSciNameDQ.validationScientificnameFound("Puma concolour", col);
		assertEquals(ComplianceValue.NOT_COMPLIANT, puma.getValue());
		assertTrue(puma.getComment(), puma.getComment().contains("Puma concolor (Linnaeus, 1771)"));
		assertTrue(puma.getComment(), puma.getComment().contains(IRI + "4QHKG"));
		DQResponse<ComplianceValue> morrus = DwCSciNameDQ.validationGenusFound("Morrus", col);
		assertTrue(morrus.getComment(), morrus.getComment().contains(IRI + "5VCQ") && morrus.getComment().contains(IRI + "5VCR"));
		DQResponse<AmendmentValue> concolour = DwCSciNameDQ.amendmentScientificnameidFromTaxon(taxon("Animalia", null, "Puma concolour", null), col);
		assertNull(concolour.getValue());
		assertTrue(concolour.getComment(), concolour.getComment().contains("Puma concolor"));
		// A misspelled authorship: the name is found, but does not match exactly.
		assertCompliance(ComplianceValue.NOT_COMPLIANT, DwCSciNameDQ.validationScientificnameFound("Murex brevispina Lamark, 1822", col));
	}

	private static void assertCompliance(ComplianceValue expected, DQResponse<ComplianceValue> result) {
		assertEquals(result.getComment(), ResultState.RUN_HAS_RESULT, result.getResultState());
		assertEquals(result.getComment(), expected, result.getValue());
	}
}
