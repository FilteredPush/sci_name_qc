/** 
 * APNIService.java
 * 
 * Copyright 2024 President and Fellows of Harvard College
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.IDFormatException;
import org.irmng.aphia.v1_0.handler.ApiException;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * @author mole
 *
 */
public class APNIService {

	private static final Log logger = LogFactory.getLog(APNIService.class);

	private static String endpoint = "https://api.biodiversity.org.au/name/check?dataset=APNI";
			
	public static NameUsage lookupTaxonByID(String apniID) throws IDFormatException, ApiException { 
		NameUsage result = new NameUsage();
		// TODO: Implement
		// https://id.biodiversity.org.au/name/apni/100473
		
		return result;
	}
	
	public static  List<NameUsage> lookupTaxon(String taxon,  String authorship) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		// TODO: Implement
		// https://api.biodiversity.org.au/name/check?dataset=APNI&q=Solanum%20centrale%20%E2%80%98Desert%20tang%E2%80%99
		
		return result;
		
	}
}
