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
	 * Test method for {@link edu.harvard.mcz.nametools.ScientificNameComparator#compare(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCompare() {
		ScientificNameComparator comparator = new ScientificNameComparator();
		assertEquals(NameComparison.MATCH_EXACT,comparator.compare("Murex murex", "Murex murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_SUBGENUS,comparator.compare("Murex murex", "Murex (Murex) murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_ONGENUS,comparator.compare("Murex brunea", "Murex murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_ONHIGHER,comparator.compare("Murex", "Muricidae").getMatchType());
		assertEquals(NameComparison.SNMATCH_QUALIFIER,comparator.compare("Chicoreus palmarosae", "Chicoreus aff. palmarosae").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSTOLOWER,comparator.compare("Chicoreus", "Chicoreus palmarosae").getMatchType());
		assertEquals(NameComparison.SNMATCH_DISSIMILAR,comparator.compare("Murex murex", "Muricidae").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSDIFFERENT,comparator.compare("Murex murex", "Mus murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSSUBSPECIFIC,comparator.compare("Murex aus murex", "Murex bus murex").getMatchType());
		assertEquals(NameComparison.SNMATCH_GENUSSPECIFIC,comparator.compare("Murex murex aus", "Murex murex bus").getMatchType());
		assertEquals(NameComparison.SNMATCH_DISSIMILAR,comparator.compare("Murex murex", "Chicoreus palmarosae").getMatchType());
		
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
