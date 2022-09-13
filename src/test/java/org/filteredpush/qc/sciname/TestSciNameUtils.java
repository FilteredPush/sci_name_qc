/**
 * Copyright 2022 President and Fellows of Harvard College
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
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
