package edu.harvard.mcz.nametools.test;

import static org.junit.Assert.*;

import org.filteredpush.qc.sciname.services.IPNIAuthorService;
import org.junit.Before;
import org.junit.Test;

import edu.harvard.mcz.nametools.ICNafpAuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;

/**
 * Integration tests (live, against IPNI) of botanical authorship comparison with IPNI standard forms of authors.
 */
public class AuthorshipIPNI_IT {

	private final ICNafpAuthorNameComparator comparator = new ICNafpAuthorNameComparator(.70d, .5d);

	@Before
	public void setUp() {
		System.setProperty(IPNIAuthorService.ENABLED_PROPERTY, "true");
	}

	private boolean same(String one, String other) {
		return NameComparison.isPlausibleAuthorMatch(comparator.compare(one, other).getMatchType());
	}

	@Test
	public void testLookupStandardForm() {
		assertEquals("Lamarck", IPNIAuthorService.lookupStandardForm("Lam.").surname);
		assertEquals("Desmazières", IPNIAuthorService.lookupStandardForm("Desm.").surname);
		assertEquals("Mueller", IPNIAuthorService.lookupStandardForm("F. Muell.").surname);
		assertEquals("Linnaeus", IPNIAuthorService.lookupStandardForm("L.").surname);
		assertNull(IPNIAuthorService.lookupStandardForm("Xqzvw."));
	}

	@Test
	public void testStandardForms() {
		// Standard forms and the authors' surnames.
		assertTrue(same("Lam.", "Lamarck"));
		assertTrue(same("Mill.", "Miller"));
		assertTrue(same("F.Muell.", "Mueller"));
		assertTrue(same("Planch. ex F.Muell.", "Planchon ex Mueller"));
		assertTrue(same("L.", "Linnaeus"));
		// In botany Desm. is Desmazières, not Desmarest (as it may be in zoology).
		assertFalse(same("Desm.", "Desmarest"));
		NameComparison desm = comparator.compare("Desm.", "Desmarest");
		assertEquals(NameComparison.MATCH_DISSIMILAR, desm.getMatchType());
		assertTrue(desm.getRemark(), desm.getRemark().contains("Desmazières"));
		assertTrue(same("Desm.", "Desmazières"));
		// In botany L. is Linnaeus.
		assertFalse(same("L.", "Lamarck"));
		// Arnold is the standard form of F.C.G. Arnold (1829-1901), not J.F.Arnold (fl. 1785, author of Pinus nigra);
		// without IPNI, the rules cannot tell these apart.
		assertFalse(same("Arnold", "J.F.Arnold"));
		assertTrue(same("J. F. Arnold", "J.F.Arnold"));
		// Initials that disagree with the forename are a different person.
		assertFalse(same("Mill.", "R.Miller"));
	}

	@Test
	public void testAuthorshipVariantsWithIPNI() {
		// The regression cases hold with IPNI standard forms as well as without.
		new TestAuthorshipVariants().testAuthorshipVariants();
	}
}
