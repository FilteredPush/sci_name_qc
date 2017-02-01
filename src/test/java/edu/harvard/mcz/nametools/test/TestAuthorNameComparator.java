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
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#detectApplicableCode(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDetectApplicableCode() {
		Map<String,Boolean> comparison = AuthorNameComparator.detectApplicableCode(null, "", "");
		assertEquals(false,comparison.get("ICZN"));
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
		
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calculateHasYear(java.lang.String)}.
	 */
	@Test
	public void testCalculateHasYear() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calculateHasParen(java.lang.String)}.
	 */
	@Test
	public void testCalculateHasParen() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#consistentWithICZNAuthor(java.lang.String)}.
	 */
	@Test
	public void testConsistentWithICZNAuthor() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#consistentWithICNapfAuthor(java.lang.String)}.
	 */
	@Test
	public void testConsistentWithICNapfAuthor() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calulateSimilarityOfAuthor(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCalulateSimilarityOfAuthor() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.AuthorNameComparator#calulateSimilarityOfAuthorAlpha(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCalulateSimilarityOfAuthorAlpha() {
		fail("Not yet implemented");
	}

}
