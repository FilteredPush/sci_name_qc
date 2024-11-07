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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.IDFormatException;
import org.irmng.aphia.v1_0.handler.ApiException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ICNafpAuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

/**
 * @author mole
 *
 */
public class APNIService {

	private static final Log logger = LogFactory.getLog(APNIService.class);

	private static String endpoint = "https://api.biodiversity.org.au/name/check?dataset=APNI";
			
	public static void main( String[] args ) {
		try {
		 	NameUsage response = APNIService.lookupTaxonByID("100473");
		 	logger.debug(response.getAcceptedName());
		 	
		 	List<NameUsage> responseList = APNIService.lookupTaxon("Solanum centrale 'Desert Tang'", "");
		 	
		} catch (IDFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static NameUsage lookupTaxonByID(String apniID) throws IDFormatException, ApiException { 
		NameUsage result = new NameUsage();
		// https://id.biodiversity.org.au/name/apni/100473.json
		// fullName and simpleName nodes
		
		if (apniID==null || apniID.length()==0 || !apniID.matches("^[0-9]+$")) { 
			throw new IDFormatException("Provided APNI ID ["+apniID+"] is not an integer");
		}
		
		String lookup = "https://id.biodiversity.org.au/name/apni/" + apniID + ".json";
		URI lookupURI = URI.create(lookup);
		
		HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
		HttpRequest request = HttpRequest.newBuilder().uri(lookupURI).GET().build();
		
		HttpResponse<String> response;
		try {
			response = client.send(request,HttpResponse.BodyHandlers.ofString());
			JSONObject responseJson = (JSONObject) JSONValue.parse(response.body());
			
			logger.debug(response);
			logger.debug(response.body());
			
			logger.debug(responseJson.get("class"));
			if (responseJson.get("class").toString().equals("au.org.biodiversity.nsl.Name")) { 
				String fullName = responseJson.get("fullName").toString();
				String simpleName = responseJson.get("simpleName").toString();
				logger.debug(fullName);
				result.setAcceptedName(fullName);
				result.setCanonicalName(simpleName);
				result.setKey(Integer.parseInt(apniID));
				result.setAuthorship(((JSONObject)responseJson.get("author")).get("name").toString());
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ApiException("IO Error accessing APNI:" + e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			throw new ApiException("Error accessing APNI:" + e.getMessage());
		}
		
		
		return result;
	}
	
	public static  List<NameUsage> lookupTaxon(String taxon,  String authorship) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();

		// Cultivar names need correct quotes to find match
		// wrong quotes, 
		// https://api.biodiversity.org.au/name/check?dataset=APNI&q=Solanum%20centrale%20%E2%80%98Desert%20Tang%E2%80%99
		// wrong quotes
		// https://api.biodiversity.org.au/name/check?dataset=APNI&q=Solanum+centrale+%E2%80%98Desert+Tang%E2%80%99
		// works: 
		// https://api.biodiversity.org.au/name/check?dataset=APNI&q=Solanum%20centrale%20%27Desert%20Tang%27
		
		String lookup = "";
		lookup = "https://api.biodiversity.org.au/name/check?dataset=APNI&q=" + taxon;
		try {
			lookup = "https://api.biodiversity.org.au/name/check?dataset=APNI&q=" + URLEncoder.encode(taxon, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug(e.getMessage());
		}
		
		logger.debug(lookup);
		URI lookupURI = URI.create(lookup);
		
		HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
		HttpRequest request = HttpRequest.newBuilder().uri(lookupURI).GET().build();
		
		HttpResponse<String> response;
		try {
			response = client.send(request,HttpResponse.BodyHandlers.ofString());
			JSONObject responseJson = (JSONObject) JSONValue.parse(response.body());
			logger.debug(response);
			logger.debug(response.body());
			
			logger.debug(responseJson.get("class"));
			
			Integer noOfResults = Integer.valueOf(responseJson.get("noOfResults").toString());
			logger.debug(noOfResults);
			
			if (noOfResults==0) {
				logger.debug("No Matches");
			} else { 
				JSONArray queryResults =  (JSONArray) responseJson.get("results");
				for (int i=0; i<noOfResults; i++) { 
					NameUsage nameUsage = new NameUsage();
					JSONObject match = (JSONObject) queryResults.get(0);
					String resultNameMatchType = (String) match.get("resultNameMatchType");
					logger.debug(resultNameMatchType);
					logger.debug(match.get("scientificName"));
					nameUsage.setAcceptedName(match.get("scientificName").toString());
					nameUsage.setCanonicalName(match.get("canonicalName").toString());
					logger.debug(match.get("nameType"));
					logger.debug(match.get("scientificNameID"));
					logger.debug(match.get("scientificNameAuthorship"));
					String matchAuthor = (String)match.get("scientificNameAuthorship");
					if (matchAuthor != null && matchAuthor.length()>0) {
						nameUsage.setAuthorship(matchAuthor);
						AuthorNameComparator authorComparator = AuthorNameComparator.authorNameComparatorFactory(authorship, "Plantae");
						NameComparison nameComparison = authorComparator.compare(matchAuthor, authorship);
						nameUsage.setAuthorComparator(authorComparator);
					}
					String id = match.get("scientificNameID").toString();
					nameUsage.setAcceptedKey(Integer.parseInt(id.replace("https://id.biodiversity.org.au/name/apni/", "")));
					if (resultNameMatchType.equals("Exact")) { 
						nameUsage.setNameMatchDescription(NameComparison.MATCH_EXACT);
						nameUsage.setGuid(id);
					}
					result.add(nameUsage);
				}
			}
				
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ApiException("IO Error accessing APNI:" + e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			throw new ApiException("Error accessing APNI:" + e.getMessage());
		} 
		return result;
		
	}
}
