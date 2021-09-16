/** 
 * TestLookupResult.java
 * 
 * Copyright 2021 President and Fellows of Harvard College
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
package edu.harvard.mcz.nametools.test;

import static org.junit.Assert.*;

import org.filteredpush.qc.sciname.services.WoRMSService;
import org.junit.Before;
import org.junit.Test;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ExpectationsNotMetException;
import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameComparison;

/**
 * @author mole
 *
 */
public class TestLookupResult {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.LookupResult#LookupResult(edu.harvard.mcz.nametools.NameComparison, java.lang.String, java.lang.String, java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testLookupResultNameComparisonStringStringStringClassOfQ() {
		String matchedName = "Scientific name";
		String guid = "9999999";
		String author = "author";
		String foundAuthor = "foundAuthor";
		AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(author, null);
		NameComparison nameComparison = comparator.compare(author, foundAuthor);
		try {
			LookupResult testInstance = new LookupResult(nameComparison, matchedName, foundAuthor, guid, WoRMSService.class);
			assertEquals(author,testInstance.getNameComparison().getNameOne());
			assertEquals(foundAuthor,testInstance.getNameComparison().getNameTwo());
			assertEquals(foundAuthor,testInstance.getMatchedAuthorship());
			assertEquals(matchedName,testInstance.getMatchedName());
			assertEquals(guid,testInstance.getGuid());
			assertEquals(WoRMSService.class,testInstance.getService());
			assertEquals(LookupResult.class,testInstance.getClass());
		} catch (ExpectationsNotMetException e) {
			fail("Error in LookupResult constructor, foundAuthor and nameTwo in name comparator are not the same.");
		}
	}

}
