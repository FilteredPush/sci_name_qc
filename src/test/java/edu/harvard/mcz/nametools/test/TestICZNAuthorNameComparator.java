package edu.harvard.mcz.nametools.test;

import static org.junit.Assert.*;

import java.time.Year;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ICZNAuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;

public class TestICZNAuthorNameComparator {

	private ICZNAuthorNameComparator comparator;
	
	@Before
	public void setUp() throws Exception {
		comparator = new ICZNAuthorNameComparator(.75d,.5d);
	}
	
	@Test
	public void testCompare() {
		assertEquals(NameComparison.MATCH_ERROR, comparator.compare(null, "Smith").getMatchType());
		assertEquals(NameComparison.MATCH_ERROR, comparator.compare("Smith", null).getMatchType());
		assertEquals(NameComparison.MATCH_ERROR, comparator.compare(null, null).getMatchType());
		
		assertEquals(NameComparison.MATCH_EXACT, comparator.compare("Smith, 1880","Smith, 1880").getMatchType());
		assertEquals(NameComparison.MATCH_EXACT, comparator.compare("(Smith, 1880)","(Smith, 1880)").getMatchType());
		
		assertEquals(NameComparison.MATCH_EXACT, comparator.compare("C.B. Adams, 1852","C. B. Adams, 1852").getMatchType());
		
		assertEquals(NameComparison.MATCH_EXACTADDSYEAR, comparator.compare("(Smith)","(Smith, 1880)").getMatchType());
		assertEquals(NameComparison.MATCH_EXACTADDSYEAR, comparator.compare("Reeve","Reeve, 1843").getMatchType());
		assertEquals(NameComparison.MATCH_EXACTMISSINGYEAR, comparator.compare("(Smith, 1880)","(Smith)").getMatchType());
		
		assertEquals(NameComparison.MATCH_WEAKEXACTYEAR, comparator.compare("(Von Born, 1778)", "(Born, 1778)").getMatchType());
		
		assertEquals(NameComparison.MATCH_SIMILAREXACTYEAR, comparator.compare("(v Born, 1778)", "(Born, 1778)").getMatchType());
		assertEquals(NameComparison.MATCH_SIMILAREXACTYEAR, comparator.compare("Ferussac, 1822", "Férussac, 1822").getMatchType());
		
		assertEquals(NameComparison.MATCH_EXACTDIFFERENTYEAR, comparator.compare("(Carpenter, 1864)", "(Carpenter, 1857)").getMatchType());
		
		assertEquals(NameComparison.MATCH_ADDSAUTHOR, comparator.compare("", "(Conrad, 1833)").getMatchType());
		
		assertEquals(NameComparison.MATCH_PARENTHESIESDIFFER, comparator.compare("Schilder, 1922","(Schilder, 1922)").getMatchType()); 
		assertEquals(NameComparison.MATCH_PARENYEARDIFFER, comparator.compare("Schilder, 1922","(Schilder, 1920)").getMatchType()); 
		
		comparator.compare("Myers and D'Attilio","Myers & D'Attilio, 1990").getMatchType(); 
		
		assertEquals(NameComparison.MATCH_PARENYEARDIFFER,comparator.compare("Watson","(Watson, 1881)").getMatchType()); 
	    assertEquals(NameComparison.MATCH_L,comparator.compare("L.","Linnaeus, 1758").getMatchType());
	    
		assertEquals(NameComparison.MATCH_EXACTADDSYEAR, comparator.compare("Linné","Linnaeus, 1758").getMatchType());
		
		assertEquals(NameComparison.MATCH_SAMEBUTABBREVIATED, comparator.compare("Linné","Linnaeus").getMatchType()); 
		assertEquals(NameComparison.MATCH_SAMEBUTABBREVIATED,comparator.compare("Linné, 1758","Linnaeus, 1758").getMatchType()); 
		assertEquals(NameComparison.MATCH_PARENYEARDIFFER,comparator.compare("(Hornung & Mermod, 1925)", "Hornung & Mermod, 1924").getMatchType());
		
		assertEquals(NameComparison.MATCH_SOWERBYEXACTYEAR, comparator.compare("(Sowerby, 1833)", "(G. B. Sowerby I, 1833)").getMatchType());
		assertEquals(NameComparison.MATCH_SOWERBYEXACTYEAR, comparator.compare("Sowerby, 1860", "G. B. Sowerby II, 1860").getMatchType());
		assertEquals(NameComparison.MATCH_SOWERBYEXACTYEAR, comparator.compare("Sowerby, 1892", "G. B. Sowerby III, 1892").getMatchType());
		
		assertEquals(NameComparison.MATCH_L_EXACTYEAR, comparator.compare("L., 1758", "Linnaeus, 1758").getMatchType());
		assertEquals(NameComparison.MATCH_L_EXACTYEAR, comparator.compare("L., 1758", "Linné, 1758").getMatchType());
		assertEquals(NameComparison.MATCH_L_EXACTYEAR, comparator.compare("L., 1758", "Linne, 1758").getMatchType());
		assertEquals(NameComparison.MATCH_L_EXACTYEAR, comparator.compare("L.", "Lamarck").getMatchType());
		
		assertEquals(NameComparison.MATCH_ADDSINITIALEXACTYEAR, comparator.compare("(Adams, 1854)", "(A. Adams, 1854)").getMatchType());
		assertEquals(NameComparison.MATCH_ADDSINITIALEXACTYEAR, comparator.compare("Adams, 1854", "A. Adams, 1854").getMatchType());
		assertEquals(NameComparison.MATCH_ADDSINITIALEXACTYEAR, comparator.compare("(Verrill, 1873)", "(A. E. Verrill, 1873)").getMatchType());
		assertEquals(NameComparison.MATCH_ADDSINITIALEXACTYEAR, comparator.compare("Verrill, 1873)", "A. E. Verrill, 1873)").getMatchType());
		assertEquals(NameComparison.MATCH_ADDSINITIALEXACTYEAR, comparator.compare("Verrill, 1873)", "A.E. Verrill, 1873)").getMatchType());
	}

	@Test
	public void testCalculateHasYear() {
        // values that contain a year
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("Smith, 1882"));
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("(Smith, 1882)"));
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("1882"));
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("1000"));

        // values that don't contain a year
        assertFalse(ICZNAuthorNameComparator.calculateHasYear("Smith"));
        assertFalse(ICZNAuthorNameComparator.calculateHasYear(""));

        assertFalse(ICZNAuthorNameComparator.calculateHasYear("18.32"));
        assertFalse(ICZNAuthorNameComparator.calculateHasYear("18 Smith 62"));
        assertFalse(ICZNAuthorNameComparator.calculateHasYear("Smith, 188"));
        assertFalse(ICZNAuthorNameComparator.calculateHasYear("(Smith, 188)"));
        assertFalse(ICZNAuthorNameComparator.calculateHasYear("188414314"));
        
        // out of range for meaning of year in this context
        // (between 1000 and now).
        
        int currentCentury = (int) (Math.floor(Year.now().getValue()/100));
        String nextCentury = Integer.toString(currentCentury+10).concat("00");
        assertFalse(ICZNAuthorNameComparator.calculateHasYear(nextCentury));
        assertFalse(ICZNAuthorNameComparator.calculateHasYear("999"));
    
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("1758"));
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("1850"));
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("1950"));
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("1999"));
        assertTrue(ICZNAuthorNameComparator.calculateHasYear("2010"));
        // edge cases for integers
        String maxint = Integer.toString(Integer.MAX_VALUE);
        assertFalse(ICZNAuthorNameComparator.calculateHasYear(maxint));
        String minint = Integer.toString(Integer.MIN_VALUE);
        assertFalse(ICZNAuthorNameComparator.calculateHasYear(minint));

        // handling of null
        assertFalse(ICZNAuthorNameComparator.calculateHasYear(null));
	}

	@Test
	public void testCalculateHasParen() {
		assertTrue(ICZNAuthorNameComparator.calculateHasParen("(Smith, 1882)"));
		assertTrue(ICZNAuthorNameComparator.calculateHasParen("(1882)"));
		assertTrue(ICZNAuthorNameComparator.calculateHasParen("()"));
		assertFalse(ICZNAuthorNameComparator.calculateHasParen("Smith, 1888"));
		assertFalse(ICZNAuthorNameComparator.calculateHasParen("(Smith, 1888"));
		assertFalse(ICZNAuthorNameComparator.calculateHasParen("(Smith, 1888))"));
	}

	@Test
	public void testConsistentWithICZNAuthor() {
		assertTrue(ICZNAuthorNameComparator.consistentWithICZNAuthor(""));
		assertTrue(ICZNAuthorNameComparator.consistentWithICZNAuthor(null));
		assertTrue(ICZNAuthorNameComparator.consistentWithICZNAuthor("Smith"));
		
		assertTrue(ICZNAuthorNameComparator.consistentWithICZNAuthor("Smith, 1880"));
		assertTrue(ICZNAuthorNameComparator.consistentWithICZNAuthor("(Smith, 1880)"));
		
		assertFalse(ICZNAuthorNameComparator.consistentWithICZNAuthor("(Smith) Jones"));
		assertFalse(ICZNAuthorNameComparator.consistentWithICZNAuthor("(Persoon) P. Kummer: Fries"));
		assertFalse(ICZNAuthorNameComparator.consistentWithICZNAuthor("Fries: Fries"));
		assertFalse(ICZNAuthorNameComparator.consistentWithICZNAuthor("(Linnaeus: Fries) Redhead, Lutzoni, Moncalvo & Vilgalys"));
	}

	@Test
	public void testConsistentWithICNapfAuthor() {
		assertTrue(ICZNAuthorNameComparator.consistentWithICNapfAuthor(""));
		assertTrue(ICZNAuthorNameComparator.consistentWithICNapfAuthor(null));
		assertTrue(ICZNAuthorNameComparator.consistentWithICNapfAuthor("Smith"));
		
		assertFalse(ICZNAuthorNameComparator.consistentWithICNapfAuthor("Smith, 1880"));
		assertFalse(ICZNAuthorNameComparator.consistentWithICNapfAuthor("(Smith, 1880)"));
		
		assertTrue(ICZNAuthorNameComparator.consistentWithICNapfAuthor("(Smith) Jones"));
		assertTrue(ICZNAuthorNameComparator.consistentWithICNapfAuthor("(Persoon) P. Kummer: Fries"));
		assertTrue(ICZNAuthorNameComparator.consistentWithICNapfAuthor("Fries: Fries"));
		assertTrue(ICZNAuthorNameComparator.consistentWithICNapfAuthor("(Linnaeus: Fries) Redhead, Lutzoni, Moncalvo & Vilgalys"));
	}

    @Test
    public void testSimilarity() { 
    	assertEquals(1d,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "Smith"),.00000001d);
    	assertEquals(0d,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "Abbouf"),.00000001d);
    	
    	assertEquals(0d,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "Jones"),.00000001d);
    	assertEquals(0d,AuthorNameComparator.calulateSimilarityOfAuthor("C.B. Adams, 1852","C. B. Adams, 1852"),00000001d);
    	
    	assertEquals(0.7895d,AuthorNameComparator.calulateSimilarityOfAuthor("Van Dover & Lichtwardt","Van Dover & Lichtw."),001d);
		
    }

	
}
