/** 
 * GNIService.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.harvard.mcz.nametools.NameAuthorshipParse;

/**
 * Minimal implementation for requests to the GNI service.
 * 
 * @author mole
 *
 */
public class GNIService {

	private static final Log logger = LogFactory.getLog(GNIService.class);

	private static String endpoint = "https://resolver.globalnames.org/name_resolvers.json";
	
	/** Query GNI with a scientific name string and if a match is found, return the canonical name
	 * (name without authorship) for that scientific name from the matching authority.
	 * @param nameString to check against GNI
	 * @return the canonical portion of the name, or an empty string if no match.
	 * @throws IOException if there is an error with the service.
	 * @throws ParseException if unable to parse the returned json from the service.
	 */
	public static String obtainCanonicalName(String nameString) throws IOException, ParseException { 
		String result = "";
		
		StringBuilder request = new StringBuilder();
		request.append(endpoint).append("?names=").append(URLEncoder.encode(nameString, "utf-8")).append("&best_match_only=true");
		
		StringBuilder requestResponse = new StringBuilder();
		URL url = new URL(request.toString());
		logger.debug(url.toString());
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection)connection;
		httpConnection.setRequestMethod("GET");
		httpConnection.setDoOutput(true);
		httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		httpConnection.connect();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		while((line = reader.readLine()) != null) {
			requestResponse.append(line);
		}
		
		if (requestResponse.length() > 0) { 
			try { 
				JSONObject parse = (JSONObject) new JSONParser().parse(requestResponse.toString());
				JSONArray data = (JSONArray) parse.get("data");
				JSONObject data0 = (JSONObject) data.get(0);
				JSONArray results = (JSONArray) data0.get("results");
				if (results!=null) { 
					JSONObject match = (JSONObject) results.get(0);
					result = (String) match.get("canonical_form");
				}
			} catch (Exception e) { 
				logger.debug(e.getMessage());
			}
		}
		
		return result;
	}
	
	/** Query GNI with a scientific name string and if a match is found, return the canonical name
	 * (name without authorship) for that scientific name from the matching authority.
	 * @param nameString to check against GNI
	 * @return a NameAuthorshipParse object containing the canonical portion of the name and the authorship portion 
	 *   of the name.
	 * @throws IOException if there is an error with the service.
	 * @throws ParseException if unable to parse the returned json from the service.
	 * 
	 */
	public static NameAuthorshipParse obtainNameAuthorParse(String nameString) throws IOException, ParseException { 
		NameAuthorshipParse result = null;
		
		StringBuilder request = new StringBuilder();
		request.append(endpoint).append("?names=").append(URLEncoder.encode(nameString,"utf-8")).append("&best_match_only=true");
		
		StringBuilder requestResponse = new StringBuilder();
		URL url = new URL(request.toString());
		logger.debug(url.toString());
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection)connection;
		httpConnection.setRequestMethod("GET");
		httpConnection.setDoOutput(true);
		httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		httpConnection.connect();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		while((line = reader.readLine()) != null) {
			requestResponse.append(line);
		}
		
		logger.debug(requestResponse);
		
		if (requestResponse.length() > 0) { 
			try { 
				JSONObject parse = (JSONObject) new JSONParser().parse(requestResponse.toString());
				JSONArray data = (JSONArray) parse.get("data");
				JSONObject data0 = (JSONObject) data.get(0);
				JSONArray results = (JSONArray) data0.get("results");
				if (results!=null) { 
					JSONObject match = (JSONObject) results.get(0);
					logger.debug(match.toJSONString());
					logger.debug(match.keySet());
					String canonicalForm = (String) match.get("canonical_form");
					String fullString = (String) match.get("name_string");
					result = new NameAuthorshipParse();
					result.setNameWithoutAuthorship(canonicalForm);
					result.setAuthorship(fullString.replace(canonicalForm, "").trim());
					result.setNameWithAuthorship(fullString);
				}
			} catch (Exception e) { 
				logger.debug(e.getMessage());
			}
		}
		
		return result;
	}
}
