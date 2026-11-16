package edu.harvard.mcz.nametools.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;

/**
 * Regression tests of authorship comparison: variant forms of the same authorship, as found in
 * checklists (many from the ChecklistBank names index, which lists the authorship strings given for a
 * name across its datasets), must be plausible matches, and different authorships must not.
 */
public class TestAuthorshipVariants {

	/** { kingdom, authorship in the source authority, authorship given, "Y" if the same authorship, "N" if not } */
	private static final String[][] CASES = {
		// Zoological
		{ "Animalia", "Lamarck, 1822", "Lamarck, 1822", "Y" },
		{ "Animalia", "Lamarck, 1822", "Lamarck 1822", "Y" },
		{ "Animalia", "Lamarck, 1822", "Lam., 1822", "Y" },
		{ "Animalia", "Lamarck, 1822", "Lamarck", "Y" },
		{ "Animalia", "Lamarck, 1822", "J.B.Lamarck, 1822", "Y" },
		{ "Animalia", "Lamarck, 1822", "de Lamarck, 1822", "Y" },
		{ "Animalia", "Lamarck, 1822", "de Monet de Lamarck, 1822", "Y" },
		{ "Animalia", "Lamarck, 1822", "Lamarck, 1823", "N" },
		{ "Animalia", "Lamarck, 1822", "Linnaeus, 1758", "N" },
		{ "Animalia", "Lamarck, 1822", "(Lamarck, 1822)", "N" },
		{ "Animalia", "Lamarck, 1822", "(Lam.)", "N" },
		{ "Animalia", "(Linnaeus, 1771)", "(Linnaeus, 1771)", "Y" },
		{ "Animalia", "(Linnaeus, 1771)", "(L., 1771)", "Y" },
		{ "Animalia", "(Linnaeus, 1771)", "(Linné, 1771)", "Y" },
		{ "Animalia", "(Linnaeus, 1771)", "(Linnaeus 1771)", "Y" },
		{ "Animalia", "(Linnaeus, 1771)", "Linnaeus, 1771", "N" },
		{ "Animalia", "(Desmarest, 1804)", "(Desmarest, 1804)", "Y" },
		{ "Animalia", "(Desmarest, 1804)", "(Desm., 1804)", "Y" },
		{ "Animalia", "(Desmarest, 1804)", "Desmarest, 1804", "N" },
		{ "Animalia", "Vieillot, 1816", "Vieillot 1816", "Y" },
		{ "Animalia", "Clench & Aguayo, 1938", "Clench and Aguayo, 1938", "Y" },
		{ "Animalia", "J. Smith, 1900", "A. Smith, 1900", "N" },
		{ "Animalia", "Smith, 1900", "Smyth, 1900", "N" },
		// Botanical
		{ "Plantae", "L.", "L.", "Y" },
		{ "Plantae", "L.", "Linnaeus", "Y" },
		{ "Plantae", "L.", "Linn.", "Y" },
		{ "Plantae", "L.", "L. (1753)", "Y" },
		{ "Plantae", "L.", "Mill.", "N" },
		{ "Plantae", "L.", "Vieillot, 1816", "N" },
		{ "Plantae", "Mill.", "Miller", "Y" },
		{ "Plantae", "Mill.", "Mill", "Y" },
		{ "Plantae", "J.F.Arnold", "J. F. Arnold", "Y" },
		{ "Plantae", "J.F.Arnold", "Arnold", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "Planch. ex F.Muell.", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "Planch. ex F. Muell.", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "Planch. ex F.Müll.", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "Planch. ex F. Müll.", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "Planch. ex F.Muell., 1859", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "F.Muell.", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "F. Muell.", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "F.Mueller", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "Mueller, 1859", "Y" },
		{ "Plantae", "Planch. ex F.Muell.", "Planchon ex Mueller", "Y" },
		// Citing only the author before "ex" is not a form of the authorship.
		{ "Plantae", "Planch. ex F.Muell.", "Planch.", "N" },
		{ "Plantae", "(Hook. ex DC.) Benth.", "(DC.) Benth.", "Y" },
		{ "Plantae", "(Hook. ex DC.) Benth.", "(Hook.) Benth.", "N" },
		// Sanctioning authors (fungi) may be omitted.
		{ "Fungi", "Bull. : Fr.", "Bull.", "Y" },
		{ "Fungi", "(Pers. : Fr.) P.Kumm.", "(Pers.) P.Kumm.", "Y" },
		{ "Fungi", "(Pers. : Fr.) P.Kumm.", "(Pers. : Fr.) P. Kumm.", "Y" },
		{ "Fungi", "(Pers. : Fr.) P.Kumm.", "(Fr.) P.Kumm.", "N" },
	};

	@Test
	public void testAuthorshipVariants() {
		StringBuilder failures = new StringBuilder();
		for (String[] c : CASES) {
			AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(c[2], c[0]);
			NameComparison comparison = comparator.compare(c[2], c[1]);
			boolean plausible = NameComparison.isPlausibleAuthorMatch(comparison.getMatchType());
			if (plausible != c[3].equals("Y")) {
				failures.append("\n").append(c[0]).append(": [").append(c[2]).append("] vs [").append(c[1])
					.append("] expected ").append(c[3].equals("Y") ? "same" : "different")
					.append(", got ").append(comparison.getMatchType());
			}
		}
		assertEquals(failures.toString(), 0, failures.length());
	}

	@Test
	public void testWithoutExAndSanctioningAuthors() {
		assertEquals("F.Muell.", AuthorNameComparator.withoutExAndSanctioningAuthors("Planch. ex F.Muell."));
		assertEquals("(DC.) Benth.", AuthorNameComparator.withoutExAndSanctioningAuthors("(Hook. ex DC.) Benth."));
		assertEquals("(DC.) Hook.f.", AuthorNameComparator.withoutExAndSanctioningAuthors("(DC.) Benth. ex Hook.f."));
		assertEquals("(Pers.) P.Kumm.", AuthorNameComparator.withoutExAndSanctioningAuthors("(Pers. : Fr.) P.Kumm."));
		assertEquals("Bull.", AuthorNameComparator.withoutExAndSanctioningAuthors("Bull. : Fr."));
		assertEquals("L.", AuthorNameComparator.withoutExAndSanctioningAuthors("L."));
	}

	@Test
	public void testSameAuthorsAbbreviated() {
		assertTrue(AuthorNameComparator.sameAuthorsAbbreviated("Lam., 1822", "Lamarck, 1822"));
		assertTrue(AuthorNameComparator.sameAuthorsAbbreviated("Desm.", "Desmarest"));
		assertTrue(AuthorNameComparator.sameAuthorsAbbreviated("Clench & Aguayo", "Clench and Aguayo"));
		// Different initials are different people.
		assertFalse(AuthorNameComparator.sameAuthorsAbbreviated("J. Smith", "A. Smith"));
		// A single letter is ambiguous (L. for Linnaeus or Lamarck), and is compared by other rules.
		assertFalse(AuthorNameComparator.sameAuthorsAbbreviated("L.", "Lamarck"));
		// A different number of authors.
		assertFalse(AuthorNameComparator.sameAuthorsAbbreviated("Clench", "Clench & Aguayo"));
	}
}
