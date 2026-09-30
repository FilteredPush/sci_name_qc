/** 
 * WoRMSService.java 
 * 
 * Copyright 2012-2022 President and Fellows of Harvard College
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

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.ICZNAuthorNameComparator;
import edu.harvard.mcz.nametools.LookupResult;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;
import edu.harvard.mcz.nametools.ScientificNameComparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.IDFormatException;
import org.filteredpush.qc.sciname.SciNameUtils;
import org.marinespecies.aphia.v1_0.model.AphiaRecord;
import org.marinespecies.aphia.v1_0.model.AphiaRecordsArray;
import org.marinespecies.aphia.v1_0.api.TaxonomicDataApi;
import org.marinespecies.aphia.v1_0.handler.ApiClient;
import org.marinespecies.aphia.v1_0.handler.ApiException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Provides support for scientific name validation against the WoRMS
 * (World Register of Marine Species) Aphia web service.
 * See: http://www.marinespecies.org/aphia.php?p=webservice
 * 
 * <p>All instances share a single configured HTTP client, which identifies this library in its 
 * User-Agent, reuses connections, has explicit timeouts, and limits the number and rate of concurrent
 * requests to WoRMS, waiting a limited time for a turn to make a request.
 * </p>
 * <ul>
 * <li>Each call to WoRMS is retried on transient failures (HTTP 408, 429, 5xx, and connection failures)
 * with exponential backoff, honoring Retry-After.</li>
 * <li>The results of {@link #validate(NameUsage)}, of searches by name, and of record lookups by ID 
 * (used for habitat lookups and lookupTaxonByID) are cached, and shared by all instances and by the 
 * static lookup methods, so repeated lookups are not resent.</li>
 * <li>Concurrent lookups of the same value make a single request, and a lookup that failed is not 
 * resent for a period.</li>
 * <li>After repeated failures, a {@link CircuitBreaker} stops calls to WoRMS for a period, during 
 * which lookups fail with a {@link ServiceUnavailableException} (an ApiException from the static 
 * lookup methods) without being sent.</li>
 * </ul>
 * <p>See {@link ServiceClientConfig} for the system properties that configure this behavior.</p>
 *
 * @author Lei Dou
 * @author Paul J. Morris
 * @version $Id: $Id
 */
public class WoRMSService implements Validator {

	private static final Log logger = LogFactory.getLog(WoRMSService.class);
	
	/** Name of the service used in log and exception messages. */
	public static final String SERVICE_NAME = "WoRMS";
	
	private static final LookupCache<String,NameUsage> VALIDATION_CACHE = new LookupCache<String,NameUsage>();
	/** Results of searches by name, keyed on name and marine only flag, shared by validate() and the static lookups. */
	private static final LookupCache<String,List<AphiaRecord>> NAME_SEARCH_CACHE = new LookupCache<String,List<AphiaRecord>>();
	/** Records looked up by AphiaID, shared by habitat lookups and lookupTaxonByID(). */
	private static final LookupCache<Integer,AphiaRecord> RECORD_CACHE = new LookupCache<Integer,AphiaRecord>();
	
	private TaxonomicDataApi wormsService;
	
	/** 
	 * No longer used, retries are managed by {@link ServiceRetrier}, retained so that existing 
	 * subclasses continue to compile.
	 * @deprecated retries are managed by {@link ServiceRetrier}, will be removed in a future release.
	 */
	@Deprecated
	protected int depth;
	protected AuthorNameComparator authorNameComparator;
	
	private final static String WORMSGUIDPREFIX = "urn:lsid:marinespecies.org:taxname:";
	
	/** 
	 * Lazily created ApiClient shared by all instances and static methods, thread-safe once configured.
	 */
	private static final class SharedClient { 
		static final ApiClient INSTANCE = createApiClient(ServiceHttpClients.newThrottledClient());
	}
	
	/**
	 * @return the shared, configured, ApiClient used for all requests to WoRMS.
	 */
	static ApiClient sharedApiClient() { 
		return SharedClient.INSTANCE;
	}
	
	/**
	 * Create an ApiClient for WoRMS using the provided http client and the configured User-Agent.
	 * 
	 * @param httpClient the OkHttpClient to use.
	 * @return a new ApiClient.
	 */
	static ApiClient createApiClient(OkHttpClient httpClient) { 
		ApiClient apiClient = new ApiClient();
		apiClient.setHttpClient(httpClient);
		apiClient.setUserAgent(ServiceClientConfig.getUserAgent());
		return apiClient;
	}
	
	/**
	 * Remove all entries from the caches of validation and habitat lookup results.
	 */
	public static void clearCaches() { 
		VALIDATION_CACHE.clear();
		NAME_SEARCH_CACHE.clear();
		RECORD_CACHE.clear();
	}
	
	/**
	 * @return the cache of validation results, for inspection.
	 */
	static LookupCache<String,NameUsage> getValidationCache() { 
		return VALIDATION_CACHE;
	}
	
	/**
	 * @return the cache of search by name results, for inspection.
	 */
	static LookupCache<String,List<AphiaRecord>> getNameSearchCache() { 
		return NAME_SEARCH_CACHE;
	}
	
	/**
	 * @return the cache of records looked up by AphiaID, for inspection.
	 */
	static LookupCache<Integer,AphiaRecord> getRecordCache() { 
		return RECORD_CACHE;
	}

	/**
	 * No argument constructor, creates the service, doesn't run a test.
	 * 
	 * @throws IOException
	 */
	public WoRMSService() throws IOException { 
		wormsService = new TaxonomicDataApi(sharedApiClient());
	}
	
	/**
	 * Constructor using a specified ApiClient, e.g. one pointing at a test server.
	 * 
	 * @param apiClient the ApiClient to use for instance lookups.
	 */
	WoRMSService(ApiClient apiClient) { 
		wormsService = new TaxonomicDataApi(apiClient);
	}
	
	/**
	 * <p>Constructor for WoRMSService.</p>
	 *
	 * @param test a boolean.
	 * @throws java.io.IOException if any.
	 */
	public WoRMSService(boolean test) throws IOException {
		super();
		wormsService = new TaxonomicDataApi(sharedApiClient());
		if (test) { 
			test();
		}
	}
	
	/**
	 * <p>test.</p>
	 *
	 * @throws java.io.IOException if any.
	 */
	protected void test()  throws IOException { 
		String basePath = wormsService.getApiClient().getBasePath();
		logger.debug(basePath);
		// through the configured client, so the request is throttled, and has the User-Agent and timeouts.
		Request request = new Request.Builder().url(basePath).header("User-Agent", ServiceClientConfig.getUserAgent()).get().build();
		try (Response response = wormsService.getApiClient().getHttpClient().newCall(request).execute()) { 
			logger.debug("Test request to " + basePath + " returned HTTP " + response.code());
		}
	}
	
	/**
	 * Given an AphiaID, look up the Aphia record.
	 *
	 * @param aphiaID the AphiaID to look up, should be parsable as an integer.
	 * @return a NameUsage containing the returned information
	 * @throws org.filteredpush.qc.sciname.IDFormatException if the provided aphiaID is not an integer.
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException if there is a problem invoking the service.
	 */
	public static NameUsage lookupTaxonByID(String aphiaID) throws IDFormatException, ApiException { 
		NameUsage result = new NameUsage();
		if (!SciNameUtils.isEmpty(aphiaID)) { 
			if (!aphiaID.matches("^[0-9]+$")) { 
				throw new IDFormatException("provided aphiaID is not an integer");
			}
			Integer intAphiaID = Integer.parseInt(aphiaID);
			TaxonomicDataApi wormsService = new TaxonomicDataApi(sharedApiClient());
			AphiaRecord ar = apiRecordByID(wormsService, intAphiaID);
			if (ar !=null && ar.getScientificname()!=null ) { 
				logger.debug(ar.getScientificname());
				logger.debug(ar.getAuthority());
				result.setAuthorship(ar.getAuthority());
				result.setCanonicalName(ar.getScientificname());
				result.setGuid(ar.getLsid());
				result.setRank(ar.getRank());
				result.setKingdom(ar.getKingdom());
				result.setScientificName(ar.getScientificname() + " " + ar.getAuthority());
			}
		}
		
		return result;
	}
	
	/**
	 * <p>lookupTaxon.</p>
	 *
	 * @param taxon a {@link java.lang.String} object.
	 * @param authorship a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException if any.
	 */
	public static  List<NameUsage> lookupTaxon(String taxon,  String authorship) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(taxon)) { 
			TaxonomicDataApi wormsService = new TaxonomicDataApi(sharedApiClient());

			List<AphiaRecord> results = apiRecordsByName(wormsService, taxon, false);
			if (results!=null && results.size()>0) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null 
							&& ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						NameUsage match = new NameUsage();
						match.setAuthorship(ar.getAuthority());
						match.setOriginalAuthorship(authorship);
						match.setCanonicalName(ar.getScientificname());
						match.setGuid(ar.getLsid());
						match.setRank(ar.getRank());
						match.setKingdom(ar.getKingdom());
						NameComparison comparison = match.getAuthorComparator().compare(authorship, ar.getAuthority());
						if (comparison.getMatchType().equals(NameComparison.MATCH_STRONGDISSIMILAR)) { 
							// leave out
							logger.debug(comparison.getMatchType());
						} else {  
							result.add(match);
						}
					}
				}
			}
		}
		return result;
	}
	
	
	/**
	 * Find a taxon name record in WoRMS.
	 *
	 * @param taxon name to look for
	 * @param author authority to look for
	 * @return aphia id for the taxon
	 * @throws java.lang.Exception if any.
	 * @param marineOnly a boolean.
	 */
	public static String simpleNameSearch(String taxon, String author, boolean marineOnly) throws Exception {
		String id  = null;

		TaxonomicDataApi wormsService = new TaxonomicDataApi(sharedApiClient());

		try {
			List<AphiaRecord> results = apiRecordsByName(wormsService, taxon, marineOnly);	
			if (results!=null || results.size()==1) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null && ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						
						String foundId = ar.getLsid();
						String foundTaxon = ar.getScientificname();
						String foundAuthor = ar.getAuthority();
						AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(author, null);
						NameComparison comparison = comparator.compare(author, foundAuthor);
						String match = comparison.getMatchType();
						logger.debug(taxon + ":" + author + " " + match + " " + foundAuthor);
						if(foundTaxon.equalsIgnoreCase(taxon) &&
								(
								author.toLowerCase().equals(foundAuthor.toLowerCase())  || 
								match.equals(NameComparison.MATCH_EXACT) || 
								match.equals(NameComparison.MATCH_SAMEBUTABBREVIATED) || 
								match.equals(NameComparison.MATCH_SIMILAREXACTYEAR) || 
								match.equals(NameComparison.MATCH_EXACTADDSYEAR) || 
								match.equals(NameComparison.MATCH_EXACTMISSINGYEAR)
								)
						){
							logger.debug(foundId);
							id = foundId;
						}						
						
					}	
				}
			}

		} catch (NullPointerException ex) {
			// no match found
			logger.debug("No match found");
			id = null;
		} catch (ApiException e) {
			throw new Exception("WoRMSService failed to access WoRMS Aphia service for " + taxon + ". " + ApiFailure.from(SERVICE_NAME, e).describe(), e);
		} 
		return id;
	}
	
	/**
	 * <p>lookupGenus.</p>
	 *
	 * @param genus a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException if any.
	 */
	public static  List<NameUsage> lookupGenus(String genus) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(genus)) { 
			TaxonomicDataApi wormsService = new TaxonomicDataApi(sharedApiClient());

			List<AphiaRecord> results = apiRecordsByName(wormsService, genus, false);
			if (results!=null && results.size()>0) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && genus!=null 
							&& ar.getScientificname().equalsIgnoreCase(genus)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						logger.debug(ar.getTaxonRankID());
						if (ar.getTaxonRankID()==180) { 
							NameUsage match = new NameUsage();
							match.setAuthorship(ar.getAuthority());
							match.setCanonicalName(ar.getScientificname());
							match.setGuid(ar.getLsid());
							match.setRank(ar.getRank());
							match.setKingdom(ar.getKingdom());
							result.add(match);
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * <p>lookupTaxonAtRank.</p>
	 *
	 * @param taxon a {@link java.lang.String} object.
	 * @param rank a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException if any.
	 */
	public static  List<NameUsage> lookupTaxonAtRank(String taxon, String rank) throws ApiException { 
		List<NameUsage> result  = new ArrayList<NameUsage>();
		
		if (!SciNameUtils.isEmpty(taxon)) { 
			TaxonomicDataApi wormsService = new TaxonomicDataApi(sharedApiClient());

			List<AphiaRecord> results = apiRecordsByName(wormsService, taxon, false);
			if (results!=null && results.size()>0) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null 
							&& ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						logger.debug(ar.getTaxonRankID());
						logger.debug(WoRMSService.rankStringToNumber(rank));
						if (ar.getTaxonRankID().equals(WoRMSService.rankStringToNumber(rank))) { 
							NameUsage match = new NameUsage();
							match.setAuthorship(ar.getAuthority());
							match.setCanonicalName(ar.getScientificname());
							match.setGuid(ar.getLsid());
							match.setRank(ar.getRank());
							match.setKingdom(ar.getKingdom());
							result.add(match);
						}
					}
				}
			}
		}

		return result;
	}	

	/**
	 * Return a rank as a string for a given aphia rankID.
	 *
	 * Note: values as of 2022 Jan 31 from https://www.marinespecies.org/rest/AphiaTaxonRanksByID/-1
	 * without awareness of kingdom applicability, and using the values Phylum/Subphylum instead of
	 * Phylum (Division)/Subphylum (Subdivision).
	 *
	 * @param rankID  the aphia rank id for which to look up a rank string
	 * @return a rank represented as a string, or an empty string if no match is found
	 */
	public static String rankIdToString(int rankID) { 
		String result = "";

		switch (rankID) { 
		case 10: 
			result = "Kingdom";
			break; 
		case 20:
			result = "Subkingdom";
			break;
		case 30:
			result = "Phylum";
			break;
		case 40:
			result = "Subphylum";
			break;
		case 50:
			result = "Superclass";
			break;
		case 60:
			result = "Class";
			break;
		case 70:
			result = "Subclass";
			break;
		case 80:
			result = "Infraclass";
			break;
		case 90:
			result = "Superorder";
			break;
		case 100:
			result = "Order";
			break;
		case 110:
			result = "Suborder";
			break;
		case 120:
			result = "Infraorder";
			break;
		case 130:
			result = "Superfamily";
			break;
		case 140:
			result = "Family";
			break;
		case 150:
			result = "Subfamily";
			break;
		case 160:
			result = "Tribe";
			break;
		case 170:
			result = "Subtribe";
			break;
		case 180:
			result = "Genus";
			break;
		case 190:
			result = "Subgenus";
			break;
		case 200:
			result = "Section";
			break;
		case 210:
			result = "Subsection";
			break;
		case 220:
			result = "Species";
			break;
		case 230:
			result = "Subspecies";
			break;
		case 240:
			result = "Variety";
			break;
		case 250:
			result = "Subvariety";
			break;
		case 260:
			result = "Forma";
			break;
		case 270:
			result = "Subforma";
			break;
		case 280:
			result = "Mutatio";
			break;
		}

		return result;
	}
	
	/**
	 * For a string representing a taxon rank, return the corresponding aphia rankID
	 *
	 * @param rank a case insensitive string for which to look up a taxon rank
	 * @return the aphia rankID for the given rank
	 */
	public static Integer rankStringToNumber(String rank) { 
		Integer result = null;

		switch (rank.toLowerCase()) { 
		case "kingdom": 
			result = 10;;
			break; 
		case "subkingdom":
			result = 20;
			break;
		case "phylum":
			result = 30;
			break;
		case "subphylum":
			result = 40;
			break;
		case "phylum (division)":
			result = 30;
			break;
		case "subphylum (division)":
			result = 40;
			break;		
		case "division":
			result = 30;
			break;
		case "subdivision":
			result = 40;
			break;				
		case "superclass":
			result = 50;
			break;
		case "class":
			result = 60;
			break;
		case "subclass":
			result = 70;
			break;
		case "infraclass":
			result = 80;
			break;
		case "superorder":
			result = 90;
			break;
		case "order":
			result = 100;
			break;
		case "suborder":
			result = 110;
			break;
		case "infraorder":
			result = 120;
			break;
		case "superfamily":
			result = 130;
			break;
		case "family":
			result = 140;
			break;
		case "subfamily":
			result = 150;
			break;
		case "tribe":
			result = 160;
			break;
		case "subtribe":
			result = 170;
			break;
		case "genus":
			result = 180;
			break;
		case "subgenus":
			result = 190;
			break;
		case "section":
			result = 200;
			break;
		case "subsection":
			result = 210;
			break;
		case "species":
			result = 220;
			break;
		case "subspecies":
			result = 230;
			break;
		case "variety":
			result = 240;
			break;
		case "var.":
			result = 240;
			break;
		case "var":
			result = 240;
			break;			
		case "subvariety":
			result = 250;
			break;
		case "forma":
			result = 260;
			break;
		case "form":
			result = 260;
			break;	
		case "f.":
			result = 260;
			break;			
		case "subforma":
			result = 270;
			break;
		case "mutatio":
			result = 280;
			break;
		}

		return result;
	}
	
	
	
	/**
	 * <p>nameComparisonSearch.</p>
	 *
	 * @param taxon a {@link java.lang.String} object.
	 * @param author a {@link java.lang.String} object.
	 * @param marineOnly a boolean.
	 * @return a {@link edu.harvard.mcz.nametools.LookupResult} object.
	 * @throws java.lang.Exception if any.
	 */
	public static LookupResult nameComparisonSearch(String taxon, String author, boolean marineOnly) throws Exception {
		LookupResult result  = null;

		TaxonomicDataApi wormsService = new TaxonomicDataApi(sharedApiClient());

		try {
			List<AphiaRecord> results = apiRecordsByName(wormsService, taxon, marineOnly);	
			if (results!=null || results.size()==1) { 
				Iterator<AphiaRecord> i = results.iterator();
				logger.debug(results.size());
				while (i.hasNext()) { 
					AphiaRecord ar = i.next();
					if (ar !=null && ar.getScientificname()!=null && taxon!=null && ar.getScientificname().equalsIgnoreCase(taxon)) {
						logger.debug(ar.getScientificname());
						logger.debug(ar.getAuthority());
						
						String foundId = ar.getLsid();
						String foundTaxon = ar.getScientificname();
						String foundAuthor = ar.getAuthority();
						AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(author, null);
						NameComparison comp = comparator.compare(author, foundAuthor);
						result = new LookupResult(comp,foundTaxon, foundAuthor,foundId, WoRMSService.class);
						
						String match = result.getNameComparison().getMatchType();
						logger.debug(taxon + ":" + author + " " + match + " " + foundAuthor);
					}	
				}
			}

		} catch (NullPointerException ex) {
			// no match found
			logger.debug("No match found");
			result = new LookupResult();
		} catch (ApiException e) {
			throw new Exception("WoRMSService failed to access WoRMS Aphia service for " + taxon + ". " + ApiFailure.from(SERVICE_NAME, e).describe(), e);
		} 
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public NameUsage validate(NameUsage taxonNameToValidate) throws ServiceException {
		logger.debug("Checking: " + taxonNameToValidate.getScientificName() + " " + taxonNameToValidate.getAuthorship());
		if (taxonNameToValidate.getScientificName().trim().equals("?")) {
			logger.debug("Not looking up scientificName = ?, will produce forbidden exception on service");
			return null;
		}
		if (taxonNameToValidate.getScientificName().trim().startsWith("? ")) {
			// Note, URL encoding doesn't solve this
			logger.debug("Not looking up scientificName starting with '? ', will produce forbidden exception on service");
			return null;
		}
		String authorship = taxonNameToValidate.getAuthorship();
		final AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(authorship, taxonNameToValidate.getKingdom());
		authorNameComparator = comparator;
		taxonNameToValidate.setAuthorComparator(comparator);
		
		String cacheKey = validationCacheKey(taxonNameToValidate);
		NameUsage found = VALIDATION_CACHE.getOrLoad(cacheKey, () -> { 
			try { 
				NameUsage lookedUp = lookupAndCompare(taxonNameToValidate, comparator);
				return lookedUp==null ? null : new NameUsage(lookedUp);
			} catch (ServiceException e) { 
				if (e.getHttpStatusCode()==403) {
					// Form of name provided is invalid, GBIF Parser can return '? epithet', which WoRMS can't lookup.
					// The failure has already been logged, treat as no match.
					logger.debug("Request to lookup [" + taxonNameToValidate.getScientificName() + "] denied, treating as no match");
					return null;
				}
				throw e;
			}
		});
		return copyForInput(found, taxonNameToValidate);
	}
	
	/**
	 * Build a key for the cache of validation results from the scientific name, authorship, and 
	 * kingdom (which determines the authorship comparator used) of the name to validate.
	 * 
	 * @param taxonNameToValidate the name to validate.
	 * @return a cache key.
	 */
	static String validationCacheKey(NameUsage taxonNameToValidate) { 
		return String.valueOf(taxonNameToValidate.getScientificName()) + "\u0000" 
				+ String.valueOf(taxonNameToValidate.getAuthorship()) + "\u0000" 
				+ String.valueOf(taxonNameToValidate.getKingdom());
	}
	
	/**
	 * Copy a cached result for return to a caller, setting the input record key from the name to validate.
	 * 
	 * @param cached the cached result, may be null.
	 * @param taxonNameToValidate the name being validated.
	 * @return a copy of cached, or null if cached is null.
	 */
	static NameUsage copyForInput(NameUsage cached, NameUsage taxonNameToValidate) { 
		if (cached==null) { 
			return null;
		}
		NameUsage result = new NameUsage(cached);
		result.setInputDbPK(taxonNameToValidate.getInputDbPK());
		return result;
	}
	
	/**
	 * Look up a name in WoRMS, and compare the results with the name to validate.  Each call to the service
	 * is retried separately, subject to the circuit breaker for the service, and name searches and 
	 * habitat lookups are cached.
	 * 
	 * @param taxonNameToValidate the name to validate.
	 * @param authorNameComparator the comparator to use for authorship comparisons.
	 * @return the matched name usage, or null if no match was found.
	 * @throws ServiceException on failure to invoke the service.
	 */
	private NameUsage lookupAndCompare(NameUsage taxonNameToValidate, AuthorNameComparator authorNameComparator) throws ServiceException { 
		NameUsage result = null;
		String taxonName = taxonNameToValidate.getScientificName();
		String authorship = taxonNameToValidate.getAuthorship();
		ScientificNameComparator scientificNameComparator = new ScientificNameComparator();
		List<AphiaRecord> results = recordsByName(wormsService, taxonName, false);
		if (results!=null && results.size()>0) { 
			// We got at least one result
			Iterator<AphiaRecord> i = results.iterator();
			//Multiple matches indicate homonyms (or in WoRMS, deleted records).
			if (results.size()>1) {
			    logger.debug("More than one match: " + results.size());
				boolean exactMatch = false;
				List<AphiaRecord> matches = new ArrayList<AphiaRecord>();
				while (i.hasNext() && !exactMatch) { 
				    AphiaRecord ar = i.next();
				    matches.add(ar);
				    logger.debug(ar.getScientificname());
				    logger.debug(ar.getAphiaID());
				    logger.debug(ar.getAuthority());
				    logger.debug(ar.getUnacceptreason());
				    logger.debug(ar.getStatus());
				    if (ar !=null && ar.getScientificname()!=null && taxonName!=null && ar.getScientificname().equals(taxonName)) {
				    	if (ar.getAuthority()!=null && ar.getAuthority().equals(authorship)) {
				    		// If one of the results is an exact match on scientific name and authorship, pick that one. 
				    		result = new NameUsage(ar);
				    		result.setInputDbPK(taxonNameToValidate.getInputDbPK());
				    		result.setMatchDescription(NameComparison.MATCH_EXACT);
				    		result.setNameMatchDescription(NameComparison.MATCH_EXACT);
				    		result.setAuthorshipStringEditDistance(1d);
				    		result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
				    		result.setOriginalScientificName(taxonNameToValidate.getScientificName());
				    		result.setScientificNameStringEditDistance(1d);
				    		result.setExtension(habitatFor(ar));
				    		exactMatch = true;
				    	}
				    }
				}
				if (!exactMatch) {
					// If we didn't find an exact match on scientific name and authorship in the list, pick the 
					// closest authorship and list all of the potential matches.  
					Iterator<AphiaRecord> im = matches.iterator();
					NameUsage closest = null;
					StringBuffer names = new StringBuffer();
					while (im.hasNext()) { 
						AphiaRecord ar = im.next();
						NameUsage current = new NameUsage(ar);
						NameComparison comparison = scientificNameComparator.compareWithoutAuthor(taxonName, current.getScientificName());
						if (NameComparison.isPlausible(comparison.getMatchType())) { 
							names.append("; ").append(current.getScientificName()).append(" ").append(current.getAuthorship()).append(" ").append(current.getUnacceptReason()).append(" ").append(current.getTaxonomicStatus());
							if (closest==null || ICZNAuthorNameComparator.calulateSimilarityOfAuthor(closest.getAuthorship(), authorship) < ICZNAuthorNameComparator.calulateSimilarityOfAuthor(current.getAuthorship(), authorship)) { 
								current.setExtension(habitatFor(ar));
								closest = current;
							}
						}
					}
					if (closest==null) {
						// none of the responses were plausible, treat as no match.
						logger.debug("No plausible matches");
					} else { 
						result = closest;
						result.setInputDbPK(taxonNameToValidate.getInputDbPK());
						result.setMatchDescription(NameComparison.MATCH_MULTIPLE + " " + names.toString());
						result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
						result.setOriginalScientificName(taxonNameToValidate.getScientificName());
						result.setScientificNameStringEditDistance(1d);
						result.setAuthorshipStringEditDistance(ICZNAuthorNameComparator.calulateSimilarityOfAuthor(taxonNameToValidate.getAuthorship(), result.getAuthorship()));
					}
				}
			} else { 
			  // we got exactly one result
			  while (i.hasNext()) { 
				AphiaRecord ar = i.next();
				if (ar !=null && ar.getScientificname()!=null && taxonName!=null && ar.getScientificname().equals(taxonName)) {
					if (ar.getAuthority()!=null && ar.getAuthority().equals(authorship)) { 
						// scientific name and authorship are an exact match 
						result = new NameUsage(ar);
						result.setInputDbPK(taxonNameToValidate.getInputDbPK());
						result.setMatchDescription(NameComparison.MATCH_EXACT);
			    		result.setNameMatchDescription(NameComparison.MATCH_EXACT);
						result.setAuthorshipStringEditDistance(1d);
						result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
						result.setOriginalScientificName(taxonNameToValidate.getScientificName());
						result.setScientificNameStringEditDistance(1d);
						result.setExtension(habitatFor(ar));
					} else {
						// find how 
						if (authorship!=null && ar!=null && ar.getAuthority()!=null) { 
							//double similarity = taxonNameToValidate.calulateSimilarityOfAuthor(ar.getAuthority());
							logger.debug(authorship);
							logger.debug(ar.getAuthority());
							NameComparison comparison = authorNameComparator.compare(authorship, ar.getAuthority());
							String match = comparison.getMatchType();
							double similarity = comparison.getSimilarity();
							logger.debug(similarity);
							result = new NameUsage(ar);
							result.setInputDbPK(taxonNameToValidate.getInputDbPK());
							result.setAuthorshipStringEditDistance(similarity);
							result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
							result.setOriginalScientificName(taxonNameToValidate.getScientificName());
							result.setMatchDescription(match);
							NameComparison nameComparison = scientificNameComparator.compareWithoutAuthor(taxonName, ar.getScientificname());
							result.setNameMatchDescription(nameComparison.getMatchType());
							result.setScientificNameStringEditDistance(nameComparison.getSimilarity());
							result.setExtension(habitatFor(ar));
						} else { 
							// no authorship was provided in the results, treat as no match
							logger.error("Result with null authorship.");
						}
					}
				}
			  }
			}
		} else { 
			logger.debug("No match.");
			// Try WoRMS fuzzy matching query
			String[] searchNames = { taxonName + " " + authorship };
			List<String> searchNamesList = Arrays.asList(searchNames);
			List<AphiaRecordsArray> matchResultsArr = ServiceRetrier.execute(SERVICE_NAME, taxonName, 
					() -> wormsService.aphiaRecordsByMatchNames(searchNamesList, false));
			if (matchResultsArr!=null && matchResultsArr.size()>0) {
				Iterator<AphiaRecordsArray> i0 = matchResultsArr.iterator();
				while (i0.hasNext()) {
					// iterate through the inputs, there should be one and only one
					AphiaRecordsArray matchResArr = i0.next();
					Iterator<AphiaRecord> im = matchResArr.iterator();
					List<NameUsage> potentialMatches = new ArrayList<NameUsage>();
					while (im.hasNext()) { 
						// iterate through the results, no match will have one result that is null
						AphiaRecord ar = im.next();
						if (ar!=null) { 
							NameUsage match = new NameUsage(ar);
							double similarity = ICZNAuthorNameComparator.calulateSimilarityOfAuthor(taxonNameToValidate.getAuthorship(), match.getAuthorship());
							match.setAuthorshipStringEditDistance(similarity);
							logger.debug(match.getScientificName());
							logger.debug(match.getAuthorship());
							logger.debug(similarity);
							NameComparison comparison = scientificNameComparator.compareWithoutAuthor(taxonName, match.getScientificName());
							if (NameComparison.isPlausible(comparison.getMatchType())) { 
								match.setNameMatchDescription(comparison.getMatchType());
								match.setScientificNameStringEditDistance(comparison.getSimilarity());
								match.setExtension(habitatFor(ar));
								potentialMatches.add(match);
							}
						} else {
							logger.debug("im.next() was null");
						}
					} 
					logger.debug("Fuzzy Matches: " + potentialMatches.size());
					if (potentialMatches.size()==1) { 
						result = potentialMatches.get(0);
						String authorComparison = authorNameComparator.compare(taxonNameToValidate.getAuthorship(), result.getAuthorship()).getMatchType();
						result.setMatchDescription(NameComparison.MATCH_FUZZY_SCINAME + "; authorship " + authorComparison);
						result.setOriginalAuthorship(taxonNameToValidate.getAuthorship());
						result.setOriginalScientificName(taxonNameToValidate.getScientificName());
						result.setInputDbPK(taxonNameToValidate.getInputDbPK());
					}
				} // iterator over input names, should be just one.
		    } else {
		    	logger.error("Fuzzy match query returned null instead of a result set.");
		    }
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> supportedExtensionTerms() {
	    List<String> terms = new ArrayList<String>();
	    terms.add("brackish");
	    terms.add("freshwater");
	    terms.add("marine");
	    terms.add("terrestrial");
	    terms.add("extinct");
		return terms;
	}
	
	/**
	 * Look up the habitat flags (brackish, freshwater, marine, terrestrial, extinct) for a record.
	 * The record is looked up by its ID through the record cache, with retries.
	 *
	 * @param ar the record for which to look up habitat flags.
	 * @return a map of habitat flag names to "true", "false", or "" if not known, empty if ar is null,
	 *   has no ID, or no record was found for its ID.
	 * @throws org.marinespecies.aphia.v1_0.handler.ApiException on failure to invoke the service.
	 */
	public Map<String,String> lookupHabitat(AphiaRecord ar) throws ApiException { 
		try { 
			return habitatFor(ar);
		} catch (ServiceException e) { 
			throw toApiException(e);
		}
	}
	
	/**
	 * Look up the habitat flags for a record, see {@link #lookupHabitat(AphiaRecord)}.
	 *
	 * @param ar the record for which to look up habitat flags.
	 * @return a new map of habitat flag names to values.
	 * @throws ServiceException on failure to invoke the service.
	 */
	private Map<String,String> habitatFor(AphiaRecord ar) throws ServiceException { 
		Map<String,String> attributes = new HashMap<String,String>();
		if (ar==null || ar.getAphiaID()==null) { 
			return attributes;
		}
		AphiaRecord record = recordByID(wormsService, ar.getAphiaID());
		if (record==null) { 
			logger.debug("No record returned for AphiaID " + ar.getAphiaID());
			return attributes;
		}
		attributes.put("brackish", record.isIsBrackish()==null ? "" : record.isIsBrackish().toString());
		attributes.put("freshwater", record.isIsFreshwater()==null ? "" : record.isIsFreshwater().toString());
		attributes.put("marine", record.isIsMarine()==null ? "" : record.isIsMarine().toString());
		attributes.put("terrestrial", record.isIsTerrestrial()==null ? "" : record.isIsTerrestrial().toString());
		attributes.put("extinct", record.isIsExtinct()==null ? "" : record.isIsExtinct().toString());
		logger.debug(attributes);
		return attributes;
	}
	
	/**
	 * Search for records by name, through the name search cache, with retries, subject to the 
	 * circuit breaker for the service.
	 * 
	 * @param api the client to use for the search if it is not cached.
	 * @param name the name to search for.
	 * @param marineOnly passed to the service, limit results to marine taxa.
	 * @return a new list of the matching records, or null if the service returned none (callers 
	 *   must not modify the records).
	 * @throws ServiceException on failure to invoke the service.
	 */
	static List<AphiaRecord> recordsByName(TaxonomicDataApi api, String name, boolean marineOnly) throws ServiceException { 
		List<AphiaRecord> result = NAME_SEARCH_CACHE.getOrLoad(name + "\u0000" + marineOnly, 
				() -> ServiceRetrier.execute(SERVICE_NAME, name, () -> api.aphiaRecordsByName(name, false, marineOnly, 1)));
		return result==null ? null : new ArrayList<AphiaRecord>(result);
	}
	
	/**
	 * Search for records by name, as {@link #recordsByName(TaxonomicDataApi, String, boolean)}, 
	 * reporting failures as an ApiException, for the static lookup methods.
	 * 
	 * @param api the client to use for the search if it is not cached.
	 * @param name the name to search for.
	 * @param marineOnly passed to the service, limit results to marine taxa.
	 * @return a new list of the matching records, or null if the service returned none.
	 * @throws ApiException on failure to invoke the service.
	 */
	static List<AphiaRecord> apiRecordsByName(TaxonomicDataApi api, String name, boolean marineOnly) throws ApiException { 
		try { 
			return recordsByName(api, name, marineOnly);
		} catch (ServiceException e) { 
			throw toApiException(e);
		}
	}
	
	/**
	 * Look up a record by its AphiaID, through the record cache, with retries, subject to 
	 * the circuit breaker for the service.
	 * 
	 * @param api the client to use for the lookup if it is not cached.
	 * @param id the AphiaID to look up.
	 * @return the record, or null if none was returned (callers must not modify the record).
	 * @throws ServiceException on failure to invoke the service.
	 */
	static AphiaRecord recordByID(TaxonomicDataApi api, Integer id) throws ServiceException { 
		return RECORD_CACHE.getOrLoad(id, 
				() -> ServiceRetrier.execute(SERVICE_NAME, "AphiaID " + id, () -> api.aphiaRecordByAphiaID(id)));
	}
	
	/**
	 * Look up a record by its AphiaID, as {@link #recordByID(TaxonomicDataApi, Integer)}, 
	 * reporting failures as an ApiException, for the static lookup methods.
	 * 
	 * @param api the client to use for the lookup if it is not cached.
	 * @param id the AphiaID to look up.
	 * @return the record, or null if none was returned.
	 * @throws ApiException on failure to invoke the service.
	 */
	static AphiaRecord apiRecordByID(TaxonomicDataApi api, Integer id) throws ApiException { 
		try { 
			return recordByID(api, id);
		} catch (ServiceException e) { 
			throw toApiException(e);
		}
	}
	
	/**
	 * Report a failure as an ApiException, for methods that declare ApiException, carrying the 
	 * description and HTTP status code (0 if none) of the failure.
	 * 
	 * @param e the failure.
	 * @return an ApiException with e as its cause.
	 */
	static ApiException toApiException(ServiceException e) { 
		return new ApiException(e.getMessage(), e, e.getHttpStatusCode(), null);
	}
}
