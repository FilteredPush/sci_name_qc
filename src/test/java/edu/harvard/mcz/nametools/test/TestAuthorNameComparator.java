/**
 * 
 */
package edu.harvard.mcz.nametools.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.harvard.mcz.nametools.AuthorNameComparator;

/**
 * @author mole
 *
 */
public class TestAuthorNameComparator {

	private AuthorNameComparator icznComparator;
	private AuthorNameComparator icnafpComparator; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		icznComparator = AuthorNameComparator.authorNameComparatorFactory("(Smith, 1882)", "Animalia");
		icnafpComparator = AuthorNameComparator.authorNameComparatorFactory("(Smith) Jones", "Fungi");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#detectApplicableCodeWithCertainty(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDetectApplicableCodeWithCertainty() {
		Map<String,Boolean> comparison = AuthorNameComparator.detectApplicableCodeWithCertainty(null, "", "");
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("", "", "");
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("", "Foo", "");
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("Smith", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("L.", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));	
		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("L. & Smith", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));			
		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("Smith, 1822", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("Smith, 1822", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("Foo", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("()", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("1920", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Smith, 1920", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Smith, 1920)", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Foo)", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("()", "A", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("1920", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Smith, 1920", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Smith, 1920)", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));		
		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty(null, "Animalia", "");
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty(null, "Plantae", "");
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty(null, "Fungi", "");
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Jones) Smith", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Jones ex. Bull.) Smith", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Bull.: Fr.) Murr", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("Smith, 1822", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Smith, 1822)", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Smith)", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		
		
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Jones) Smith", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Jones & Smith) Smith", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));	
		comparison = AuthorNameComparator.detectApplicableCodeWithCertainty("(Jones & Smith ex Bull.) Smith", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));			
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#detectApplicableCode(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDetectApplicableCode() {
		Map<String,Boolean> comparison = AuthorNameComparator.detectApplicableCode(null, "", "");
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("", "", "");
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("", "Foo", "");
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		
		comparison = AuthorNameComparator.detectApplicableCode("Smith", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));		
		comparison = AuthorNameComparator.detectApplicableCode("L.", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));	
		
		comparison = AuthorNameComparator.detectApplicableCode("L. & Smith", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));			
		
		comparison = AuthorNameComparator.detectApplicableCode("Smith, 1822", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("Smith, 1822", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("Foo", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("()", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("1920", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Smith, 1920", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Smith, 1920)", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		
		comparison = AuthorNameComparator.detectApplicableCode("(Foo)", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("()", "A", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("1920", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Smith, 1920", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Smith, 1920)", "Foo", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));		
		
		comparison = AuthorNameComparator.detectApplicableCode(null, "Animalia", "");
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode(null, "Plantae", "");
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode(null, "Fungi", "");
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		
		comparison = AuthorNameComparator.detectApplicableCode("(Jones) Smith", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Jones ex. Bull.) Smith", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Bull.: Fr.) Murr", "", ""); 
		assertEquals(false,comparison.get("ICZN"));
		assertEquals(true,comparison.get("ICNafp"));		
		comparison = AuthorNameComparator.detectApplicableCode("Smith, 1822", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Smith, 1822)", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Smith)", "", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		
		
		comparison = AuthorNameComparator.detectApplicableCode("(Jones) Smith", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));
		comparison = AuthorNameComparator.detectApplicableCode("(Jones & Smith) Smith", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));	
		comparison = AuthorNameComparator.detectApplicableCode("(Jones & Smith ex Bull.) Smith", "Animalia", ""); 
		assertEquals(true,comparison.get("ICZN"));
		assertEquals(false,comparison.get("ICNafp"));			
	}
	
	
	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calculateHasYear(java.lang.String)}.
	 */
	@Test
	public void testCalculateHasYear() {
		assertEquals(false,AuthorNameComparator.calculateHasYear(""));
		assertEquals(false,AuthorNameComparator.calculateHasYear("Smith"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("1"));
		assertEquals(false,AuthorNameComparator.calculateHasYear(Integer.toString(Integer.MAX_VALUE)));
		assertEquals(false,AuthorNameComparator.calculateHasYear("Smith, 1"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("Smith, 1xxx"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("20001"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("Smith, 20001"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("Smith, '58"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("(Smith)"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("()"));
		assertEquals(false,AuthorNameComparator.calculateHasYear("Smith, 195x"));
		
		assertEquals(true,AuthorNameComparator.calculateHasYear("Smith, 1880"));
		assertEquals(true,AuthorNameComparator.calculateHasYear("Smith, 1880[sic]"));
		assertEquals(true,AuthorNameComparator.calculateHasYear("2000"));
		assertEquals(true,AuthorNameComparator.calculateHasYear("2000 1"));
		assertEquals(true,AuthorNameComparator.calculateHasYear("4th Smith, 1935"));
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calculateHasParen(java.lang.String)}.
	 */
	@Test
	public void testCalculateHasParen() {
		assertEquals(false,AuthorNameComparator.calculateHasParen(""));
		assertEquals(false,AuthorNameComparator.calculateHasParen("Smith"));
		assertEquals(false,AuthorNameComparator.calculateHasParen("1"));
		assertEquals(false,AuthorNameComparator.calculateHasParen(Integer.toString(Integer.MAX_VALUE)));
		assertEquals(false,AuthorNameComparator.calculateHasParen("Smith, 1"));
		assertEquals(false,AuthorNameComparator.calculateHasParen("Smith, 1xxx"));
		assertEquals(false,AuthorNameComparator.calculateHasParen("20001"));
		assertEquals(false,AuthorNameComparator.calculateHasParen("Smith, 20001"));
		assertEquals(false,AuthorNameComparator.calculateHasParen("Smith, '58"));
		assertEquals(false,AuthorNameComparator.calculateHasParen("("));
		assertEquals(false,AuthorNameComparator.calculateHasParen("Smith, 1952)"));
		
		assertEquals(true,AuthorNameComparator.calculateHasParen("(Smith, 1880)"));
		assertEquals(true,AuthorNameComparator.calculateHasParen("(Smith)"));
		assertEquals(true,AuthorNameComparator.calculateHasParen("()"));
		assertEquals(true,AuthorNameComparator.calculateHasParen(",(Smith, 1880)"));
		assertEquals(true,AuthorNameComparator.calculateHasParen("(Smith, 1880)[sic]"));
	}

	@Test
	public void testConsistentWithBothAuthor() {
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor(""));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor(" "));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("Smith"));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("L."));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("Linné"));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor(""));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor(" "));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("Smith"));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("L."));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("Linné"));
	}
	
	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#consistentWithICZNAuthor(java.lang.String)}.
	 */
	@Test
	public void testConsistentWithICZNAuthor() {
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor(""));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor(" "));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("Smith"));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("L."));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("Linné"));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("Smith, 1880"));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("1880"));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("(Smith)"));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("(Smith, 1880)"));
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("(Smith, 1880) [sic]"));
		
		assertEquals(true,AuthorNameComparator.consistentWithICZNAuthor("()"));

		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("(Vill.) Cout."));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("Smith ex Jones"));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("(Villar ex Rothm.) M.Mayor & Fern.Benito"));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("L. emend. Müll. Arg"));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("(Froel. ex DC.) Fr."));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("(Bull.: Fr.) Murr"));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("(Bull.: Fr.) Pers. "));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("(Bull.: Fr.) Pers."));
		assertEquals(false,AuthorNameComparator.consistentWithICZNAuthor("Pers.: Pers."));
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#consistentWithICNapfAuthor(java.lang.String)}.
	 */
	@Test
	public void testConsistentWithICNapfAuthor() {
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor(""));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor(" "));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("Smith"));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("L."));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("Linné"));
		assertEquals(false,AuthorNameComparator.consistentWithICNapfAuthor("Smith, 1880"));
		assertEquals(false, AuthorNameComparator.consistentWithICNapfAuthor("1880"));
		assertEquals(false,AuthorNameComparator.consistentWithICNapfAuthor("(Smith)"));
		assertEquals(false,AuthorNameComparator.consistentWithICNapfAuthor("(Smith, 1880)"));
		assertEquals(false,AuthorNameComparator.consistentWithICNapfAuthor("(Smith, 1880) [sic]"));
		
		assertEquals(false,AuthorNameComparator.consistentWithICNapfAuthor("()"));

		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("(Vill.) Cout."));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("Smith ex Jones"));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("(Villar ex Rothm.) M.Mayor & Fern.Benito"));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("L. emend. Müll. Arg"));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("(Froel. ex DC.) Fr."));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("(Bull.: Fr.) Murr"));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("(Bull.: Fr.) Pers. "));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("(Bull.: Fr.) Pers."));
		assertEquals(true,AuthorNameComparator.consistentWithICNapfAuthor("Pers.: Pers."));
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calulateSimilarityOfAuthor(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCalulateSimilarityOfAuthor() {
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("", ""),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("", " "),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "Smith"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "smith"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("Smith, 1880", "Smith, 1880"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("Smith 1880", "Smith, 1880"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("Smith,1880", "Smith, 1880"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthor("Smith1880", "Smith, 1880"),.00001d);

		assertEquals(0,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "Foo"),.00001d);
		assertEquals(0,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "1880"),.00001d);
		assertEquals(0,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", ""),.00001d);
		
		assertEquals(0.555555d,AuthorNameComparator.calulateSimilarityOfAuthor("Smith", "Smith, 1880"),.00001d);
		assertEquals(0.555555d,AuthorNameComparator.calulateSimilarityOfAuthor("Smith, 1880", "Smith"),.00001d);
		assertEquals(0.888888d,AuthorNameComparator.calulateSimilarityOfAuthor("Smith, 1881", "Smith, 1880"),.00001d);
		assertEquals(0.888888d,AuthorNameComparator.calulateSimilarityOfAuthor("Smitn, 1880", "Smith, 1880"),.00001d);
		assertEquals(0.888888d,AuthorNameComparator.calulateSimilarityOfAuthor("Smitĥ, 1880", "Smith, 1880"),.00001d);
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calulateSimilarityOfAuthorAlpha(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCalulateSimilarityOfAuthorAlpha() {
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("", ""),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith", "Smith"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith", "smith"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith, 1880", "Smith, 1880"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith 1880", "Smith, 1880"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith,1880", "Smith, 1880"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith1880", "Smith, 1880"),.00001d);

		assertEquals(0,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith", "Foo"),.00001d);
		assertEquals(0,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith", "1880"),.00001d);
		assertEquals(0,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith", ""),.00001d);
		
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith", "Smith, 1880"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith, 1880", "Smith"),.00001d);
		assertEquals(1,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith, 1881", "Smith, 1880"),.00001d);
		assertEquals(0.800000d,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smitn, 1880", "Smith, 1880"),.00001d);
		assertEquals(0.888888d,AuthorNameComparator.calulateSimilarityOfAuthor("Smitĥ, 1880", "Smith, 1880"),.00001d);
		assertEquals(0.200000d,AuthorNameComparator.calulateSimilarityOfAuthorAlpha("Smith", "htims"),.00001d);
	}

}
