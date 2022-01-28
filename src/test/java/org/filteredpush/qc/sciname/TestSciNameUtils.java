/**
 * 
 */
package org.filteredpush.qc.sciname;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author mole
 *
 */
public class TestSciNameUtils {

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.SciNameUtils#isEmpty(java.lang.String)}.
	 */
	@Test
	public void testIsEmpty() {
		assertEquals(true,SciNameUtils.isEmpty(""));
		assertEquals(true,SciNameUtils.isEmpty(null));
		assertEquals(true,SciNameUtils.isEmpty(" "));
		assertEquals(true,SciNameUtils.isEmpty("\t"));
		
		assertEquals(false,SciNameUtils.isEmpty("A"));
		assertEquals(false,SciNameUtils.isEmpty("9"));
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.SciNameUtils#isEqualOrNonEmpty(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsEqualOrNonEmpty() {
		assertEquals(true,SciNameUtils.isEqualOrNonEmpty("A", "A"));
		assertEquals(true,SciNameUtils.isEqualOrNonEmpty(null, "A"));
		assertEquals(true,SciNameUtils.isEqualOrNonEmpty("A", null));
		assertEquals(true,SciNameUtils.isEqualOrNonEmpty(null,null));
		assertEquals(true,SciNameUtils.isEqualOrNonEmpty("", ""));
		
		assertEquals(false,SciNameUtils.isEqualOrNonEmpty("A", "a"));
		assertEquals(false,SciNameUtils.isEqualOrNonEmpty("A", ""));
		assertEquals(false,SciNameUtils.isEqualOrNonEmpty("", " "));
	}

}
