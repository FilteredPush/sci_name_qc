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
package org.filteredpush.qc.sciname.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ICZNAuthorNameComparator;
import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

/**
 * @author mole
 *
 */
public class WoRMSServiceTestIT {

	private static final Log logger = LogFactory.getLog(WoRMSServiceTestIT.class);
	
	protected WoRMSService service;
	
	public void setUp() { 
		try {
			service = new WoRMSService(true);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.services.WoRMSService#simpleNameSearch(java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testSimpleNameSearch() {
		try {
			assertEquals("urn:lsid:marinespecies.org:taxname:216786", service.simpleNameSearch("Cypraea argus", "Linnaeus, 1758", true));
			assertEquals(null, service.simpleNameSearch("Pseudamussium septemradiatum", "(Muller, 1776)", true));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testLookupGenus() {
		try {
			
			String genus = "Murex";
			List<NameUsage> matches;
			matches = WoRMSService.lookupGenus(genus);
			assertTrue(matches.size() > 0);
			Iterator<NameUsage> i = matches.iterator();
			while (i.hasNext()) { 
				NameUsage match = i.next();
				assertEquals(genus,match.getCanonicalName());
				assertEquals("Genus",match.getRank());
			}

			genus = "M99995RRx";
			matches = WoRMSService.lookupGenus(genus);
			assertTrue(matches.size() == 0);
			
			genus = "";
			matches = WoRMSService.lookupGenus(genus);
			assertTrue(matches.size() == 0);
			
		} catch (Exception e) {
			// Expected if service is down
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testLookupNameAtRank() {
		try {
			
			String name = "Murex";
			List<NameUsage> matches;
			matches = WoRMSService.lookupTaxonAtRank(name,"Genus");
			logger.debug(matches.size());
			assertTrue(matches.size() > 0);
			Iterator<NameUsage> i = matches.iterator();
			while (i.hasNext()) { 
				NameUsage match = i.next();
				assertEquals(name,match.getCanonicalName());
				assertEquals("Genus",match.getRank());
			}

			name = "M99995RRx";
			matches = WoRMSService.lookupTaxonAtRank(name,"Kingdom");
			assertTrue(matches.size() == 0);
			
			name = "";
			matches = WoRMSService.lookupTaxonAtRank(name,"Order");
			assertTrue(matches.size() == 0);
			
			name = "Gastropoda";
			matches = WoRMSService.lookupTaxonAtRank(name,"Kingdom");
			assertTrue(matches.size() == 0);
			
			name = "Muricidae";
			matches = WoRMSService.lookupTaxonAtRank(name,"Family");
			assertTrue(matches.size() > 0);
			i = matches.iterator();
			while (i.hasNext()) { 
				NameUsage match = i.next();
				assertEquals(name,match.getCanonicalName());
				assertEquals("Family",match.getRank());
			}
			
			name = "Gastropoda";
			matches = WoRMSService.lookupTaxonAtRank(name,"Class");
			assertTrue(matches.size() > 0);
			i = matches.iterator();
			while (i.hasNext()) { 
				NameUsage match = i.next();
				assertEquals(name,match.getCanonicalName());
				assertEquals("Class",match.getRank());
			}	
			
			name = "Mollusca";
			matches = WoRMSService.lookupTaxonAtRank(name,"Phylum");
			assertTrue(matches.size() > 0);
			i = matches.iterator();
			while (i.hasNext()) { 
				NameUsage match = i.next();
				assertEquals(name,match.getCanonicalName());
				assertEquals("Phylum",match.getRank());
			}				
			
			name = "Animalia";
			matches = WoRMSService.lookupTaxonAtRank(name,"Kingdom");
			assertTrue(matches.size() > 0);
			i = matches.iterator();
			while (i.hasNext()) { 
				NameUsage match = i.next();
				assertEquals(name,match.getCanonicalName());
				assertEquals("Kingdom",match.getRank());
			}				
			
		} catch (Exception e) {
			// Expected if service is down
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testRankLookups() { 
		
		assertEquals(Integer.valueOf(180),WoRMSService.rankStringToNumber("Genus"));
		assertEquals("Genus",WoRMSService.rankIdToString(180));
		
		for (Integer i=10; i<290; i=i+10) { 
			String rank = WoRMSService.rankIdToString(i);
			assertEquals(i,WoRMSService.rankStringToNumber(rank));
			assertNotNull(rank);
			assertNotNull(WoRMSService.rankStringToNumber(rank));
		}
	}
	
	/**
	 * Test method for {@link org.filteredpush.qc.sciname.services.WoRMSService#nameComparisonSearch(java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testNameComparisonSearch() {
		try {
			LookupResult testResult = WoRMSService.nameComparisonSearch("Cypraea argus", "Linnaeus, 1758", true);
			assertEquals("urn:lsid:marinespecies.org:taxname:216786", testResult.getGuid());
			assertEquals("Linnaeus, 1758", testResult.getMatchedAuthorship());
			assertEquals("Cypraea argus", testResult.getMatchedName());
			assertEquals(NameComparison.MATCH_EXACT,testResult.getNameComparison().getMatchType());
			assertEquals("Linnaeus, 1758", testResult.getNameComparison().getNameTwo());
			
			testResult = WoRMSService.nameComparisonSearch("Cypraea argus", "Linnaeus", true);
			assertEquals("urn:lsid:marinespecies.org:taxname:216786", testResult.getGuid());
			assertEquals("Linnaeus, 1758", testResult.getMatchedAuthorship());
			assertEquals("Cypraea argus", testResult.getMatchedName());
			assertEquals(NameComparison.MATCH_EXACTADDSYEAR,testResult.getNameComparison().getMatchType());
			assertEquals("Linnaeus", testResult.getNameComparison().getNameOne());			
			assertEquals("Linnaeus, 1758", testResult.getNameComparison().getNameTwo());			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test 
	public void testValidate() { 
		if (service==null) { 
			setUp();
		}
		String sciName = "Partula lutea";
		String authorship = "Lesson, 1831";
		NameUsage toTest = new NameUsage("WoRMS", AuthorNameComparator.authorNameComparatorFactory(authorship, "Animalia"), sciName, authorship); 
		toTest.setScientificName(sciName);
		toTest.setAuthorship(authorship);
		NameUsage result =  service.validate(toTest);
		assertEquals("urn:lsid:marinespecies.org:taxname:986771",result.getGuid());
		assertEquals("Exact Match",result.getMatchDescription());
		assertEquals("Exact Match",result.getNameMatchDescription());
		assertEquals("true",result.getExtension().get("terrestrial"));
		assertEquals("false",result.getExtension().get("marine"));
	}

	
}
