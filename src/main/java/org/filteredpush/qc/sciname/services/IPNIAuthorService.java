/**
 * IPNIAuthorService.java
 *
 * Copyright 2026 President and Fellows of Harvard College
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
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Standard forms of the names of authors of plant names, from the International Plant Names Index
 * (IPNI, https://www.ipni.org/), which records the standard form (abbreviation) of each author, e.g. Lam. for
 * Jean-Baptiste de Lamarck, Desm. for J.B.H.J. Desmazières (not Desmarest), F.Muell. for Ferdinand von Mueller.
 *
 * Lookups are by exact standard form (GET https://www.ipni.org/api/1/search?f=f_author&amp;q=author std:{form}),
 * cached, and fail quietly (returning null), so that comparisons fall back to rules that do not use IPNI.
 * Lookups can be turned off with the system property {@value #ENABLED_PROPERTY}=false (e.g. to work offline).
 *
 * @author mole
 */
public class IPNIAuthorService {

	private static final Log logger = LogFactory.getLog(IPNIAuthorService.class);

	/** Name of the service, for retries and circuit breaking. */
	public static final String SERVICE_NAME = "IPNI authors";
	/** IPNI search API. */
	public static final String API = "https://www.ipni.org/api/1/search";
	/** System property that turns lookups off when false. */
	public static final String ENABLED_PROPERTY = "sci_name_qc.ipniAuthors";

	/** An author of plant names in IPNI. */
	public static class IPNIAuthor {
		public final String id;
		public final String standardForm;
		public final String surname;
		public final String forename;
		public IPNIAuthor(String id, String standardForm, String surname, String forename) {
			this.id = id;
			this.standardForm = standardForm;
			this.surname = surname;
			this.forename = forename;
		}
		@Override public String toString() { return standardForm + " (" + forename + " " + surname + ", ipni:" + id + ")"; }
	}

	/** Cached result for a standard form that is not in IPNI. */
	private static final IPNIAuthor NOT_FOUND = new IPNIAuthor("", "", "", "");
	private static final LookupCache<String,IPNIAuthor> CACHE = new LookupCache<String,IPNIAuthor>();

	private static final class SharedClient {
		static final OkHttpClient INSTANCE = ServiceHttpClients.newThrottledClient();
	}

	private IPNIAuthorService() { }

	/**
	 * Are lookups enabled (system property {@value #ENABLED_PROPERTY} is not false)?
	 *
	 * @return true if enabled
	 */
	public static boolean isEnabled() {
		return !"false".equalsIgnoreCase(System.getProperty(ENABLED_PROPERTY, "true"));
	}

	/**
	 * Normalize a string that may be a standard form: remove the spaces after periods ("F. Muell." to "F.Muell.").
	 *
	 * @param form the string
	 * @return the normalized string
	 */
	public static String normalize(String form) {
		return form==null ? null : form.trim().replaceAll("\\.\\s+", ".").replaceAll("\\s+", " ");
	}

	/**
	 * Look up an author by standard form.
	 *
	 * @param form a standard form, e.g. Lam. or F.Muell. (spaces after periods are ignored)
	 * @return the author whose standard form this is, or null if there is none, IPNI cannot be queried,
	 *   or lookups are not enabled
	 */
	public static IPNIAuthor lookupStandardForm(String form) {
		if (!isEnabled() || form==null) { return null; }
		final String standardForm = normalize(form);
		if (standardForm.isEmpty() || !standardForm.matches("^[\\p{L}][\\p{L}.' -]*$")) { return null; }
		try {
			IPNIAuthor result = CACHE.getOrLoad(standardForm, () -> ServiceRetrier.execute(SERVICE_NAME, standardForm, () -> query(standardForm)));
			return result==NOT_FOUND || result==null ? null : result;
		} catch (ServiceException e) {
			logger.debug("IPNI author lookup failed for [" + standardForm + "]: " + e.getMessage());
			return null;
		}
	}

	private static IPNIAuthor query(String standardForm) throws IOException {
		String url;
		try {
			url = API + "?perPage=10&f=f_author&q=" + URLEncoder.encode("author std:" + standardForm, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
		Request request = new Request.Builder().url(url)
				.header("Accept", "application/json")
				.header("User-Agent", ServiceClientConfig.getUserAgent())
				.build();
		String body;
		try (Response response = SharedClient.INSTANCE.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("HTTP " + response.code() + " from " + url);
			}
			body = response.body()==null ? "" : response.body().string();
		}
		try {
			Object parsed = new JSONParser().parse(body);
			if (parsed instanceof JSONObject && ((JSONObject)parsed).get("results") instanceof JSONArray) {
				for (Object o : (JSONArray)((JSONObject)parsed).get("results")) {
					if (o instanceof JSONObject) {
						JSONObject author = (JSONObject)o;
						Object std = author.get("standardForm");
						if (std!=null && normalize(std.toString()).equals(standardForm)) {
							return new IPNIAuthor(text(author.get("id")), std.toString(), text(author.get("surname")), text(author.get("forename")));
						}
					}
				}
			}
		} catch (org.json.simple.parser.ParseException e) {
			throw new IOException("Not JSON from IPNI: " + e.getMessage(), e);
		}
		return NOT_FOUND;
	}

	private static String text(Object value) {
		return value==null ? "" : value.toString().trim();
	}

	/** Clear cached lookups (for tests). */
	public static void clearCache() {
		CACHE.clear();
	}
}
