/**
 * COLService.java
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.SciNameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ICZNAuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;
import edu.harvard.mcz.nametools.ScientificNameComparator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Lookups in the Catalogue of Life (COL), through the ChecklistBank API (https://api.checklistbank.org/),
 * as a scientific name source authority.
 *
 * By default the latest release of COL is used (ChecklistBank dataset "3LR"); a specific release can be
 * used (pinned) by giving its ChecklistBank dataset key (e.g. 317005) or release alias (e.g. COL25, COL26.11).
 * As a bdqval:sourceAuthority: CatalogOfLife:Latest, or CatalogOfLife:{pin_target} (see SciNameSourceAuthority).  Name usages are identified by their COL identifiers, given
 * as IRIs in the form {@value #TAXON_IRI}&lt;id&gt; (also accepted: http, and col:&lt;id&gt;).
 *
 * Name searches are exact on the scientific name without authorship
 * (GET /dataset/{key}/nameusage/search?q=&lt;name&gt;&amp;type=EXACT&amp;content=SCIENTIFIC_NAME),
 * optionally restricted to a rank, and the results are compared on the full name, as GBIFService and
 * WoRMSService do.
 *
 * @author mole
 */
public class COLService implements Validator {

	private static final Log logger = LogFactory.getLog(COLService.class);

	/** Name of the service, for retries, circuit breaking, and comments. */
	public static final String SERVICE_NAME = "Catalogue of Life";
	/** ChecklistBank API. */
	public static final String API = "https://api.checklistbank.org";
	/** ChecklistBank dataset key of the latest release of the Catalogue of Life. */
	public static final String LATEST_RELEASE = "3LR";
	/** Prefix of the IRIs of COL taxa and name usages. */
	public static final String TAXON_IRI = "https://www.catalogueoflife.org/data/taxon/";

	private static final int SEARCH_LIMIT = 100;
	private static final Pattern ID_PATTERN = Pattern.compile(
			"^(?:https?://(?:www\\.)?catalogueoflife\\.org/data/taxon/|col:|COL:)([A-Za-z0-9]+)/?$");
	private static final Pattern DATASET_IRI = Pattern.compile(
			"^https?://api\\.checklistbank\\.org/dataset/([0-9]+|3LR)/?$", Pattern.CASE_INSENSITIVE);

	private static final LookupCache<String,String> RESPONSE_CACHE = new LookupCache<String,String>();

	private static final class SharedClient {
		static final OkHttpClient INSTANCE = ServiceHttpClients.newThrottledClient();
	}

	private final String datasetKey;

	/**
	 * Use the latest release of the Catalogue of Life.
	 */
	public COLService() {
		this(null);
	}

	/**
	 * Use a release of the Catalogue of Life.
	 *
	 * @param datasetKey the ChecklistBank dataset key or alias of the release (e.g. 317005, COL25),
	 *   or null (or Latest) for the latest release.
	 * @throws IllegalArgumentException if datasetKey is not a key or alias (letters, digits, and dots)
	 */
	public COLService(String datasetKey) {
		if (SciNameUtils.isEmpty(datasetKey) || datasetKey.trim().equalsIgnoreCase("Latest")) {
			this.datasetKey = LATEST_RELEASE;
		} else if (datasetKey.trim().matches("^[A-Za-z0-9.]+$")) {
			this.datasetKey = datasetKey.trim();
		} else {
			throw new IllegalArgumentException("Not a ChecklistBank dataset key or alias: [" + datasetKey + "]");
		}
	}

	/**
	 * @return the ChecklistBank dataset key in use.
	 */
	public String getDatasetKey() {
		return datasetKey;
	}

	/**
	 * Extract the ChecklistBank dataset key from a ChecklistBank dataset IRI
	 * (e.g. https://api.checklistbank.org/dataset/3LR/).
	 *
	 * @param iri the IRI
	 * @return the dataset key, or null if iri is not a ChecklistBank dataset IRI.
	 */
	public static String datasetKeyFromIri(String iri) {
		if (iri==null) { return null; }
		Matcher m = DATASET_IRI.matcher(iri.trim());
		return m.matches() ? m.group(1).toUpperCase() : null;
	}

	/**
	 * Extract a COL identifier from a scientificNameID.
	 *
	 * @param scientificNameID in the form https://www.catalogueoflife.org/data/taxon/{id} or col:{id}
	 * @return the identifier, or null if scientificNameID is not in a recognized form.
	 */
	public static String idFromScientificNameID(String scientificNameID) {
		if (scientificNameID==null) { return null; }
		Matcher m = ID_PATTERN.matcher(scientificNameID.trim());
		return m.matches() ? m.group(1) : null;
	}

	/**
	 * Name usages with exactly the given scientific name (without authorship), at any rank.
	 *
	 * @param name the scientific name without authorship
	 * @return the matching name usages, possibly none
	 * @throws IOException if COL cannot be queried
	 */
	public List<NameUsage> lookupName(String name) throws IOException {
		return lookupTaxonAtRank(name, null);
	}

	/**
	 * Name usages with exactly the given scientific name (without authorship) at the given rank.
	 *
	 * @param name the scientific name without authorship
	 * @param rank the rank (case insensitive, e.g. Order, family), or null for any rank
	 * @return the matching name usages, possibly none
	 * @throws IOException if COL cannot be queried
	 */
	public List<NameUsage> lookupTaxonAtRank(String name, String rank) throws IOException {
		List<NameUsage> result = new ArrayList<NameUsage>();
		if (SciNameUtils.isEmpty(name)) {
			return result;
		}
		String url = API + "/dataset/" + datasetKey + "/nameusage/search?q=" + encode(name.trim())
			+ "&type=EXACT&content=SCIENTIFIC_NAME&limit=" + SEARCH_LIMIT;
		if (!SciNameUtils.isEmpty(rank)) {
			url = url + "&rank=" + encode(rank.trim().toLowerCase());
		}
		Object response = parse(get(url));
		if (response instanceof JSONObject && ((JSONObject)response).get("result") instanceof JSONArray) {
			Iterator<?> i = ((JSONArray)((JSONObject)response).get("result")).iterator();
			while (i.hasNext()) {
				Object item = i.next();
				if (item instanceof JSONObject) {
					NameUsage usage = nameUsageFromSearchResult((JSONObject)item);
					if (usage!=null && usage.getCanonicalName().equalsIgnoreCase(name.trim())
							&& (SciNameUtils.isEmpty(rank) || usage.getRank().equalsIgnoreCase(rank.trim()))) {
						result.add(usage);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Name usages for a name, as GBIFService.searchForTaxon, WoRMSService.lookupTaxon, and
	 * IRMNGService.lookupTaxon provide them: exact matches on the name without authorship,
	 * at any rank.  The authorship is compared by the caller.
	 *
	 * @param name the scientific name without authorship
	 * @param authorship the authorship, not used in the search
	 * @return the matching name usages, possibly none
	 * @throws IOException if COL cannot be queried
	 */
	public List<NameUsage> lookupTaxon(String name, String authorship) throws IOException {
		return lookupName(name);
	}

	/**
	 * The name usage with a COL identifier.
	 *
	 * @param id the COL identifier (e.g. 44LVK)
	 * @return the name usage, or null if there is none with that identifier
	 * @throws IOException if COL cannot be queried
	 */
	public NameUsage lookupTaxonByID(String id) throws IOException {
		if (SciNameUtils.isEmpty(id) || !id.trim().matches("^[A-Za-z0-9]+$")) {
			return null;
		}
		String body = get(API + "/dataset/" + datasetKey + "/nameusage/" + encode(id.trim()));
		if (body==null) {
			return null;
		}
		Object usage = parse(body);
		if (!(usage instanceof JSONObject)) {
			return null;
		}
		// The classification of a synonym is that of its accepted name.
		JSONObject u = (JSONObject)usage;
		String classificationId = id.trim();
		if (u.get("accepted") instanceof JSONObject) {
			Object acceptedId = ((JSONObject)u.get("accepted")).get("id");
			if (acceptedId!=null) { classificationId = acceptedId.toString(); }
		}
		JSONArray classification = null;
		String classBody = get(API + "/dataset/" + datasetKey + "/taxon/" + encode(classificationId) + "/classification");
		if (classBody!=null) {
			Object c = parse(classBody);
			if (c instanceof JSONArray) { classification = (JSONArray)c; }
		}
		return nameUsage(u, classification);
	}

	/**
	 * The scientific names of the synonyms of an accepted taxon.
	 *
	 * @param id the COL identifier of the accepted taxon
	 * @return the synonyms' name usages (without classification), possibly none
	 * @throws IOException if COL cannot be queried
	 */
	public List<NameUsage> lookupSynonyms(String id) throws IOException {
		List<NameUsage> result = new ArrayList<NameUsage>();
		if (SciNameUtils.isEmpty(id)) { return result; }
		String body = get(API + "/dataset/" + datasetKey + "/taxon/" + encode(id.trim()) + "/synonyms");
		if (body==null) { return result; }
		Object response = parse(body);
		if (response instanceof JSONObject) {
			for (String group : new String[] { "homotypic", "heterotypic", "misapplied" }) {
				Object list = ((JSONObject)response).get(group);
				if (list instanceof JSONArray) {
					for (Object s : (JSONArray)list) {
						if (s instanceof JSONObject) {
							NameUsage usage = nameUsage((JSONObject)s, null);
							if (usage!=null) { result.add(usage); }
						}
					}
				}
			}
		}
		return result;
	}

	/** {@inheritDoc}
	 *
	 * Exact match on the name without authorship, then comparison of the authorship: an exact match
	 * on both, or on the name when no authorship is given, gives MATCH_EXACT; with several name usages and none exact, the closest authorship is
	 * returned with MATCH_MULTIPLE and the list of candidates.
	 */
	@Override
	public NameUsage validate(NameUsage taxonNameToValidate) throws ServiceException {
		String taxonName = taxonNameToValidate.getScientificName();
		String authorship = taxonNameToValidate.getAuthorship();
		if (SciNameUtils.isEmpty(taxonName)) {
			return null;
		}
		AuthorNameComparator authorNameComparator = AuthorNameComparator.authorNameComparatorFactory(authorship, taxonNameToValidate.getKingdom());
		ScientificNameComparator scientificNameComparator = new ScientificNameComparator();
		List<NameUsage> hits;
		try {
			hits = lookupName(taxonName);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		NameUsage result = null;
		if (hits.size()==0) {
			return null;
		}
		// An exact match on the name and authorship, or, if no authorship is given, on the name: the
		// provided name is matched, with no authorship to compare (as validation data dataID 230 expects).
		for (NameUsage hit : hits) {
			if (hit.getCanonicalName().equals(taxonName) &&
					(SciNameUtils.isEmpty(authorship) || authorship.equals(hit.getAuthorship()))) {
				result = hit;
				result.setMatchDescription(NameComparison.MATCH_EXACT);
				result.setNameMatchDescription(NameComparison.MATCH_EXACT);
				result.setAuthorshipStringEditDistance(1d);
				result.setScientificNameStringEditDistance(1d);
				break;
			}
		}
		if (result==null && hits.size()==1) {
			result = hits.get(0);
			NameComparison authorComparison = authorNameComparator.compare(authorship, result.getAuthorship());
			result.setMatchDescription(authorComparison.getMatchType());
			result.setAuthorshipStringEditDistance(authorComparison.getSimilarity());
			NameComparison nameComparison = scientificNameComparator.compareWithoutAuthor(taxonName, result.getCanonicalName());
			result.setNameMatchDescription(nameComparison.getMatchType());
			result.setScientificNameStringEditDistance(nameComparison.getSimilarity());
		} else if (result==null) {
			StringBuffer names = new StringBuffer();
			NameUsage closest = null;
			for (NameUsage hit : hits) {
				names.append("; ").append(hit.getCanonicalName()).append(" ").append(hit.getAuthorship()).append(" ").append(hit.getTaxonomicStatus());
				if (closest==null || ICZNAuthorNameComparator.calulateSimilarityOfAuthor(closest.getAuthorship(), authorship)
						< ICZNAuthorNameComparator.calulateSimilarityOfAuthor(hit.getAuthorship(), authorship)) {
					closest = hit;
				}
			}
			result = closest;
			result.setMatchDescription(NameComparison.MATCH_MULTIPLE + " " + names.toString());
			result.setNameMatchDescription(NameComparison.MATCH_EXACT);
			result.setScientificNameStringEditDistance(1d);
			result.setAuthorshipStringEditDistance(ICZNAuthorNameComparator.calulateSimilarityOfAuthor(authorship, result.getAuthorship()));
		}
		result.setInputDbPK(taxonNameToValidate.getInputDbPK());
		result.setOriginalScientificName(taxonName);
		result.setOriginalAuthorship(authorship);
		result.setAuthorComparator(authorNameComparator);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> supportedExtensionTerms() {
		return new ArrayList<String>();
	}

	/**
	 * A NameUsage from a ChecklistBank name usage search result (a usage with its classification).
	 *
	 * @param item a search result
	 * @return the name usage, or null if item has none
	 */
	static NameUsage nameUsageFromSearchResult(JSONObject item) {
		if (!(item.get("usage") instanceof JSONObject)) {
			return null;
		}
		Object classification = item.get("classification");
		return nameUsage((JSONObject)item.get("usage"), classification instanceof JSONArray ? (JSONArray)classification : null);
	}

	/**
	 * A NameUsage from a ChecklistBank name usage and its classification.  The classification gives
	 * the higher taxa (kingdom, phylum, class, order, superfamily, family, subfamily, tribe, subtribe, genus);
	 * the usage itself (which a search result's classification includes) is skipped.
	 *
	 * @param usage a ChecklistBank name usage (with id, name, status, and for a synonym accepted)
	 * @param classification the classification, from the root, or null
	 * @return the name usage
	 */
	static NameUsage nameUsage(JSONObject usage, JSONArray classification) {
		Object nameObject = usage.get("name");
		if (!(nameObject instanceof JSONObject)) {
			return null;
		}
		JSONObject name = (JSONObject)nameObject;
		String id = text(usage.get("id"));
		NameUsage result = new NameUsage();
		result.setSourceAuthority(SERVICE_NAME);
		result.setScientificName(text(name.get("scientificName")));
		result.setCanonicalName(text(name.get("scientificName")));
		result.setAuthorship(text(name.get("authorship")));
		result.setRank(text(name.get("rank")));
		result.setTaxonomicStatus(text(usage.get("status")).toUpperCase().replace(' ', '_'));
		if (!id.isEmpty()) {
			result.setGuid(TAXON_IRI + id);
			result.setKey(id.hashCode());
			result.setAcceptedKey(id.hashCode());
		}
		if (usage.get("accepted") instanceof JSONObject) {
			JSONObject accepted = (JSONObject)usage.get("accepted");
			String acceptedId = text(accepted.get("id"));
			if (!acceptedId.isEmpty()) { result.setAcceptedKey(acceptedId.hashCode()); }
			if (accepted.get("name") instanceof JSONObject) {
				result.setAcceptedName(text(((JSONObject)accepted.get("name")).get("scientificName")));
				result.setAcceptedAuthorship(text(((JSONObject)accepted.get("name")).get("authorship")));
			}
		} else {
			result.setAcceptedName(result.getScientificName());
			result.setAcceptedAuthorship(result.getAuthorship());
		}
		if (classification!=null) {
			Map<String,String> ranks = new LinkedHashMap<String,String>();
			for (Object o : classification) {
				if (o instanceof JSONObject) {
					JSONObject taxon = (JSONObject)o;
					String rank = text(taxon.get("rank")).toLowerCase();
					if (!text(taxon.get("id")).equals(id) && !ranks.containsKey(rank)) {
						ranks.put(rank, text(taxon.get("name")));
					}
				}
			}
			if (ranks.containsKey("kingdom")) { result.setKingdom(ranks.get("kingdom")); }
			if (ranks.containsKey("phylum")) { result.setPhylum(ranks.get("phylum")); }
			if (ranks.containsKey("class")) { result.setTclass(ranks.get("class")); }
			if (ranks.containsKey("order")) { result.setOrder(ranks.get("order")); }
			if (ranks.containsKey("superfamily")) { result.setSuperfamily(ranks.get("superfamily")); }
			if (ranks.containsKey("family")) { result.setFamily(ranks.get("family")); }
			if (ranks.containsKey("subfamily")) { result.setSubfamily(ranks.get("subfamily")); }
			if (ranks.containsKey("tribe")) { result.setTribe(ranks.get("tribe")); }
			if (ranks.containsKey("subtribe")) { result.setSubtribe(ranks.get("subtribe")); }
			if (ranks.containsKey("genus")) { result.setGenus(ranks.get("genus")); }
		}
		// A name at one of these ranks is its own value at that rank.
		String rank = result.getRank().toLowerCase();
		if (rank.equals("kingdom") && result.getKingdom()==null) { result.setKingdom(result.getCanonicalName()); }
		if (rank.equals("genus") && result.getGenus()==null) { result.setGenus(result.getCanonicalName()); }
		return result;
	}

	private static String text(Object value) {
		return value==null ? "" : value.toString().trim();
	}

	private static String encode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	private static Object parse(String json) throws IOException {
		if (json==null) { return null; }
		try {
			return new JSONParser().parse(json);
		} catch (ParseException e) {
			throw new IOException("Not JSON from " + SERVICE_NAME + ": " + e.getMessage(), e);
		}
	}

	/** Marks an HTTP 404 response, cached as an empty body. */
	private static final String NOT_FOUND = "\u0000404";

	/**
	 * GET a URL from ChecklistBank, with retries, the circuit breaker, and caching of responses.
	 *
	 * @param url the URL
	 * @return the body, or null if the response is 404 (not found)
	 * @throws IOException if the service cannot be queried
	 */
	private static String get(final String url) throws IOException {
		String body;
		try {
			body = RESPONSE_CACHE.getOrLoad(url, () -> ServiceRetrier.execute(SERVICE_NAME, url, () -> {
				Request request = new Request.Builder().url(url)
						.header("Accept", "application/json")
						.header("User-Agent", ServiceClientConfig.getUserAgent())
						.build();
				try (Response response = SharedClient.INSTANCE.newCall(request).execute()) {
					if (response.code()==404) {
						return NOT_FOUND;
					}
					if (!response.isSuccessful()) {
						throw new IOException("HTTP " + response.code() + " from " + url);
					}
					return response.body()==null ? "" : response.body().string();
				}
			}));
		} catch (ServiceException e) {
			throw new IOException(SERVICE_NAME + " not available: " + e.getMessage(), e);
		}
		logger.debug(url);
		return NOT_FOUND.equals(body) ? null : body;
	}

	/**
	 * Clear cached responses (for tests).
	 */
	public static void clearCaches() {
		RESPONSE_CACHE.clear();
	}
}
