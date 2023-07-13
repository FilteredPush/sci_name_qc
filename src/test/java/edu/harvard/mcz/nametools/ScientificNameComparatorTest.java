/** 
 * ScientificNameComparatorTest.java
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
package edu.harvard.mcz.nametools;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author mole
 *
 */
public class ScientificNameComparatorTest {

	private static final Log logger = LogFactory.getLog(ScientificNameComparatorTest.class);

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.ScientificNameComparator#compareWithoutAuthor(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCompare() {
		ScientificNameComparator comparator = new ScientificNameComparator();
		assertEquals(NameComparison.MATCH_EXACT,comparator.compareWithoutAuthor("Murex murex", "Murex murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_SUBGENUS,comparator.compareWithoutAuthor("Murex murex", "Murex (Murex) murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_ONGENUS,comparator.compareWithoutAuthor("Murex brunea", "Murex murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_ONHIGHER,comparator.compareWithoutAuthor("Murex", "Muricidae").getMatchType());
		assertEquals(NameComparison.SNMATCH_QUALIFIER,comparator.compareWithoutAuthor("Chicoreus palmarosae", "Chicoreus aff. palmarosae").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSTOLOWER,comparator.compareWithoutAuthor("Chicoreus", "Chicoreus palmarosae").getMatchType());
		assertEquals(NameComparison.SNMATCH_DISSIMILAR,comparator.compareWithoutAuthor("Murex murex", "Muricidae").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSDIFFERENT,comparator.compareWithoutAuthor("Murex murex", "Mus murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSSUBSPECIFIC,comparator.compareWithoutAuthor("Murex aus murex", "Murex bus murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSSPECIFIC,comparator.compareWithoutAuthor("Murex murex aus", "Murex murex bus").getMatchType());
		assertEquals(NameComparison.SNMATCH_DISSIMILAR,comparator.compareWithoutAuthor("Murex murex", "Chicoreus palmarosae").getMatchType());
		// not intended for authorship, if present and exact will match
		assertEquals(NameComparison.MATCH_EXACT,comparator.compareWithoutAuthor("Murex murex L.", "Murex murex L.").getMatchType());
		// if authorship is present but different, report mismatch
		assertEquals(NameComparison.SNMATCH_DISSIMILAR,comparator.compareWithoutAuthor("Murex murex L.", "Murex murex Linnaeus").getMatchType());
		
	}

	/**
	 * Test method for {@link edu.harvard.mcz.nametools.ScientificNameComparator#stringSimilarity(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testStringSimilarity() {
		assertEquals(1,ScientificNameComparator.stringSimilarity("", ""),.00001d);
		assertEquals(1,ScientificNameComparator.stringSimilarity("Murex murex", "Murex murex"),.00001d);
		assertEquals(0,ScientificNameComparator.stringSimilarity("Murex", "Foo"),.00001d);
		assertEquals(0,ScientificNameComparator.stringSimilarity("Murex murex", "Foo"),.00001d);
		
		assertEquals(.875,ScientificNameComparator.stringSimilarity("Aus bus", "Aus busi"),.00001d);
	}

}
