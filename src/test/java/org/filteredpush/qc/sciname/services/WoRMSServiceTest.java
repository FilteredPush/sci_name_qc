/**
 * 
 */
package org.filteredpush.qc.sciname.services;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * @author mole
 *
 */
public class WoRMSServiceTest {

	
	WoRMSService service;
	
	public void setUp() { 
		try {
			service = new WoRMSService(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
