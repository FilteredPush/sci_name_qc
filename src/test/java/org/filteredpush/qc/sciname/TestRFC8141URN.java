/** 
 * TestRFC8141URN.java
 * 
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Test RFC8141URN and its subclass LSID
 *  
 * @author mole
 *
 */
public class TestRFC8141URN {

	private static final Log logger = LogFactory.getLog(TestRFC8141URN.class);

	@Test
	public void testConstructor() { 
		try {
			RFC8141URN test = new RFC8141URN("urn:lsid:marinespecies.org:taxname:148");
			assertEquals("lsid",test.getNid());
			assertEquals("marinespecies.org:taxname:148",test.getNss());
		} catch (URNFormatException e) {
			fail("Threw exception for validly formatted URN");
		}
	}
	
	@Test
	public void testLSIDConstructor() { 
		try {
			LSID test = new LSID("urn:lsid:marinespecies.org:taxname:148");
			assertEquals("lsid",test.getNid());
			assertEquals("marinespecies.org:taxname:148",test.getNss());
			assertEquals("marinespecies.org",test.getAuthority());
			assertEquals("taxname",test.getNamespace());
			assertEquals("148",test.getObjectID());
			assertNull(test.getVersion());
		} catch (URNFormatException e) {
			logger.debug(e.getMessage());
			fail("Threw exception for validly formatted LSID");
		}
		try {
			LSID test = new LSID("URN:LSID:ebi.ac.uk:SWISS-PROT.accession:P34355:3");
			assertEquals("ebi.ac.uk",test.getAuthority());
			assertEquals("SWISS-PROT.accession",test.getNamespace());
			assertEquals("P34355",test.getObjectID());
			assertEquals("3",test.getVersion());
		} catch (URNFormatException e) {
			logger.debug(e.getMessage());
			fail("Threw exception for validly formatted LSID");
		}
		try {
			LSID test = new LSID("urn:lsid:b021cfc4-883b-4f48-9679-47985dd006ef");
			fail("Failed to throw exception for invalidly formatted LSID");
		} catch (URNFormatException e) {
			// expected exception thrown
		}
	}
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.RFC8141URN#isRFC8141URN(java.lang.String)}.
	 */
	@Test
	public void testIsRFC8141URN() {
		
		assertEquals(true, RFC8141URN.isRFC8141URN("urn:lsid:marinespecies.org:taxname:148"));
		assertEquals(true, RFC8141URN.isRFC8141URN("urn:uuid:c65c3ede-484f-45af-813e-65f606dff750"));
		
		assertEquals(false, RFC8141URN.isRFC8141URN("c65c3ede-484f-45af-813e-65f606dff750"));
		assertEquals(false, RFC8141URN.isRFC8141URN("https://www.gbif.org/species/2529789"));
		
		assertEquals(true, RFC8141URN.isRFC8141URN("urn:urn-99999:string"));
		assertEquals(false, RFC8141URN.isRFC8141URN("urn:urn-09999:string"));
		assertEquals(false, RFC8141URN.isRFC8141URN("urn:X-999:string"));
	}

}
