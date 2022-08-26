/** 
 * GNIServiceTest_IT.java
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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import edu.harvard.mcz.nametools.NameAuthorshipParse;

/**
 * @author mole
 *
 */
public class GNIServiceTest_IT {

	private static final Log logger = LogFactory.getLog(GNIServiceTest_IT.class);

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.services.GNIService#obtainCanonicalName(java.lang.String)}.
	 */
	@Test
	public void testObtainCanonicalName() {
		
		
		String scientificName = "Ophiocoma Alexandri Lyman, 1860";
		String canonicalName = "";
		try {
			canonicalName = GNIService.obtainCanonicalName(scientificName);
			assertEquals("Ophiocoma alexandri", canonicalName);
		} catch (IOException e) {
			fail("Unexpected Exception: " + e.getMessage());
		} catch (ParseException e) {
			fail("Unexpected Exception: " + e.getMessage());
		}
		
		scientificName = "Aus bus cus L, 1923";
		try {
			// GNI won't return matches on strings not present in name lists, it is matching rather than parsing.
			canonicalName = GNIService.obtainCanonicalName(scientificName);
			assertEquals("", canonicalName);
		} catch (IOException e) {
			fail("Unexpected Exception: " + e.getMessage());
		} catch (ParseException e) {
			fail("Unexpected Exception: " + e.getMessage());
		}
		
	}

	/**
	 * Test method for {@link org.filteredpush.qc.sciname.services.GNIService#obtainNameAuthorParse(java.lang.String)}.
	 */
	@Test
	public void testObtainNameAuthorParse() {
		
		// historical capitalized specific epithet derived from name of a person.
		String scientificName = "Ophiocoma Alexandri Lyman, 1860";
		NameAuthorshipParse parsedName;
		try {
			parsedName = GNIService.obtainNameAuthorParse(scientificName);
			// known name places specific epithet in modern form without capitalization.
			assertEquals("Ophiocoma alexandri Lyman, 1860", parsedName.getNameWithAuthorship());
			assertEquals("Ophiocoma alexandri", parsedName.getNameWithoutAuthorship());
			assertEquals("Lyman, 1860", parsedName.getAuthorship());
		} catch (IOException e) {
			fail("Unexpected Exception: " + e.getMessage());
		} catch (ParseException e) {
			fail("Unexpected Exception: " + e.getMessage());
		}
		
	}

}
