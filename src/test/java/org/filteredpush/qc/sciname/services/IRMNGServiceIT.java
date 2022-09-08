/** 
 * IRMNGServiceIT.java
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
package org.filteredpush.qc.sciname.services;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * @author mole
 *
 */
public class IRMNGServiceIT {

	private static final Log logger = LogFactory.getLog(IRMNGServiceIT.class);

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.services.IRMNGService#lookupGenus(java.lang.String)}.
	 */
	@Test
	public void testLookupGenus() {
		
		try {
			
			String genus = "Murex";
			List<NameUsage> matches;
			matches = IRMNGService.lookupGenus(genus);
			assertTrue(matches.size() > 0);
			Iterator<NameUsage> i = matches.iterator();
			while (i.hasNext()) { 
				NameUsage match = i.next();
				assertEquals(genus,match.getCanonicalName());
				assertEquals("Genus",match.getRank());
				assertEquals("urn:lsid:irmng.org:taxname:",match.getGuid().substring(0, 27));
				logger.debug(match.getGuid());
			}

			genus = "M99995RRx";
			matches = IRMNGService.lookupGenus(genus);
			assertTrue(matches.size() == 0);
			
			genus = "";
			matches = IRMNGService.lookupGenus(genus);
			assertTrue(matches.size() == 0);
			
		} catch (Exception e) {
			// Expected if service is down
			fail(e.getMessage());
		}
	}

}
