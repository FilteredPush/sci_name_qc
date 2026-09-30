/** 
 * TestServiceResilience.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.harvard.mcz.nametools.NameUsage;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Tests of the retry, rate limiting, User-Agent, and caching behavior of WoRMSService and IRMNGService
 * against a local MockWebServer standing in for the remote service.  No network access is required.
 * 
 * @author mole
 */
public class TestServiceResilience {
	
	private static final String NAME = "Haematopus ostralegus";
	private static final String AUTHOR = "Linnaeus, 1758";
	private static final String WORMS_RECORD = "{\"AphiaID\":147436,\"scientificname\":\"" + NAME + "\",\"authority\":\"" + AUTHOR + "\","
			+ "\"status\":\"accepted\",\"rank\":\"Species\",\"kingdom\":\"Animalia\",\"lsid\":\"urn:lsid:marinespecies.org:taxname:147436\","
			+ "\"isMarine\":1,\"isBrackish\":1,\"isFreshwater\":0,\"isTerrestrial\":1,\"isExtinct\":null}";
	private static final String IRMNG_RECORD = "{\"IRMNG_ID\":10180000,\"scientificname\":\"" + NAME + "\",\"authority\":\"" + AUTHOR + "\","
			+ "\"status\":\"accepted\",\"rank\":\"Species\",\"kingdom\":\"Animalia\","
			+ "\"isMarine\":1,\"isBrackish\":1,\"isFreshwater\":0,\"isTerrestrial\":1,\"isExtinct\":null}";
	
	private MockWebServer server;
	private ConcurrentLinkedQueue<MockResponse> nameResponses;
	private ConcurrentLinkedQueue<MockResponse> recordResponses;
	private AtomicInteger nameRequests;
	private AtomicInteger recordRequests;
	private String record;
	
	@Before
	public void setUp() throws IOException { 
		ServiceClientConfig.resetToDefaults();
		ServiceClientConfig.setBackoffBaseMillis(1L);
		ServiceClientConfig.setBackoffMaxMillis(5L);
		WoRMSService.clearCaches();
		IRMNGService.clearCaches();
		CircuitBreaker.resetAll();
		nameResponses = new ConcurrentLinkedQueue<MockResponse>();
		recordResponses = new ConcurrentLinkedQueue<MockResponse>();
		nameRequests = new AtomicInteger();
		recordRequests = new AtomicInteger();
		record = WORMS_RECORD;
		server = new MockWebServer();
		server.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) {
				String path = request.getPath();
				if (path.contains("/AphiaRecordsByName/")) { 
					nameRequests.incrementAndGet();
					MockResponse response = nameResponses.poll();
					if (response==null) { 
						response = jsonResponse("[" + record + "]");
					}
					return response;
				} else if (path.contains("/AphiaRecordByAphiaID/") || path.contains("/AphiaRecordByIRMNG_ID/")) { 
					recordRequests.incrementAndGet();
					MockResponse response = recordResponses.poll();
					return response==null ? jsonResponse(record) : response;
				}
				return new MockResponse().setResponseCode(404);
			}
		});
		server.start();
	}
	
	@After
	public void tearDown() throws IOException { 
		server.shutdown();
		ServiceClientConfig.resetToDefaults();
		WoRMSService.clearCaches();
		IRMNGService.clearCaches();
		CircuitBreaker.resetAll();
	}
	
	private static MockResponse jsonResponse(String body) { 
		return new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(body);
	}
	
	/** 
	 * An error response with an empty reason phrase, as is the case for HTTP/2 responses.
	 */
	private static MockResponse errorResponse(int code, String body) { 
		return new MockResponse().setStatus("HTTP/1.1 " + code + " ").setHeader("Content-Type", "text/plain").setBody(body);
	}
	
	/**
	 * A WoRMS record for the name searched for in a request for AphiaRecordsByName.
	 */
	private static String recordFor(RecordedRequest request) { 
		String name = request.getRequestUrl().pathSegments().get(request.getRequestUrl().pathSize()-1);
		return WORMS_RECORD.replace("\"scientificname\":\"" + NAME + "\"", "\"scientificname\":\"" + name + "\"");
	}
	
	private WoRMSService wormsService(RequestThrottle throttle) { 
		org.marinespecies.aphia.v1_0.handler.ApiClient apiClient = WoRMSService.createApiClient(ServiceHttpClients.newThrottledClient(throttle));
		apiClient.setBasePath(server.url("/rest").toString());
		return new WoRMSService(apiClient);
	}
	
	private WoRMSService wormsService() { 
		return wormsService(new RequestThrottle(2, 0L));
	}
	
	private static NameUsage toValidate(String name, String authorship, int inputDbPK) { 
		NameUsage result = new NameUsage();
		result.setScientificName(name);
		result.setAuthorship(authorship);
		result.setInputDbPK(inputDbPK);
		return result;
	}

	@Test
	public void testRetryThenSuccessThenCached() throws Exception {
		nameResponses.add(errorResponse(503, "Service temporarily unavailable"));
		nameResponses.add(errorResponse(502, "Bad gateway"));
		WoRMSService service = wormsService();
		NameUsage result = service.validate(toValidate(NAME, AUTHOR, 1));
		assertNotNull(result);
		assertEquals(NAME, result.getScientificName());
		assertEquals(1, result.getInputDbPK());
		assertEquals("true", result.getExtension().get("marine"));
		assertEquals(3, nameRequests.get());
		assertEquals(1, recordRequests.get());
		
		// the request identifies this library in the User-Agent
		RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
		assertNotNull(request);
		String userAgent = request.getHeader("User-Agent");
		assertTrue(userAgent, userAgent.startsWith("FilteredPush-sci_name_qc/"));
		
		// a repeated lookup of the same name is served from the cache, by a new service instance
		NameUsage again = wormsService().validate(toValidate(NAME, AUTHOR, 2));
		assertNotNull(again);
		assertNotSame(result, again);
		assertEquals(2, again.getInputDbPK());
		assertEquals(NAME, again.getScientificName());
		assertEquals(result.getMatchDescription(), again.getMatchDescription());
		assertEquals("true", again.getExtension().get("marine"));
		assertEquals(3, nameRequests.get());
		assertEquals(1, recordRequests.get());
		assertEquals(1L, WoRMSService.getValidationCache().getHitCount());
		
		// modifying a returned result does not modify the cached result
		again.getExtension().put("marine", "false");
		NameUsage third = wormsService().validate(toValidate(NAME, AUTHOR, 3));
		assertEquals("true", third.getExtension().get("marine"));
		
		// a different authorship is a validation cache miss
		long misses = WoRMSService.getValidationCache().getMissCount();
		wormsService().validate(toValidate(NAME, "L.", 4));
		assertEquals(misses + 1, WoRMSService.getValidationCache().getMissCount());
		// but the search by the same name, and the habitat for the same AphiaID, are cached
		assertEquals(3, nameRequests.get());
		assertEquals(1, recordRequests.get());
	}
	
	@Test
	public void testCacheCanBeDisabled() throws Exception {
		ServiceClientConfig.setCacheSize(0);
		WoRMSService service = wormsService();
		assertNotNull(service.validate(toValidate(NAME, AUTHOR, 1)));
		assertNotNull(service.validate(toValidate(NAME, AUTHOR, 1)));
		assertEquals(2, nameRequests.get());
		assertEquals(2, recordRequests.get());
	}
	
	@Test
	public void testGivesUpWithDiagnosticMessage() throws Exception {
		ServiceClientConfig.setMaxRetries(3);
		for (int i=0; i<10; i++) { 
			nameResponses.add(errorResponse(503, "Service temporarily unavailable"));
		}
		try { 
			wormsService().validate(toValidate(NAME, AUTHOR, 1));
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(4, nameRequests.get());
			assertEquals(503, e.getHttpStatusCode());
			String message = e.getMessage();
			assertTrue(message, message.contains("HTTP 503 (Service Unavailable)"));
			assertTrue(message, message.contains("/AphiaRecordsByName/Haematopus%20ostralegus"));
			assertTrue(message, message.contains("Service temporarily unavailable"));
			assertTrue(message, message.contains("Content-Type: text/plain"));
		}
		// the failure is remembered, the same lookup fails without being resent
		nameResponses.clear();
		try { 
			wormsService().validate(toValidate(NAME, AUTHOR, 1));
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertEquals(503, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().contains("failed recently"));
		}
		assertEquals(4, nameRequests.get());
		// failed results are not cached, once the failure is forgotten the lookup is made
		WoRMSService.clearCaches();
		assertNotNull(wormsService().validate(toValidate(NAME, AUTHOR, 1)));
		assertEquals(5, nameRequests.get());
	}
	
	@Test
	public void testRetryAfterHonored() throws Exception {
		nameResponses.add(errorResponse(429, "Too many requests").setHeader("Retry-After", "1"));
		long start = System.currentTimeMillis();
		NameUsage result = wormsService().validate(toValidate(NAME, AUTHOR, 1));
		long elapsed = System.currentTimeMillis() - start;
		assertNotNull(result);
		assertEquals(2, nameRequests.get());
		assertTrue("elapsed " + elapsed, elapsed >= 900L);
	}
	
	@Test
	public void testForbiddenNotRetried() throws Exception {
		for (int i=0; i<10; i++) { 
			nameResponses.add(errorResponse(403, "Forbidden"));
		}
		NameUsage result = wormsService().validate(toValidate("Haematopus ? ostralegus", AUTHOR, 1));
		assertNull(result);
		assertEquals(1, nameRequests.get());
	}
	
	@Test
	public void testBadRequestNotRetried() throws Exception {
		for (int i=0; i<10; i++) { 
			nameResponses.add(errorResponse(400, "Bad request body"));
		}
		try { 
			wormsService().validate(toValidate(NAME, AUTHOR, 1));
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(1, nameRequests.get());
			assertEquals(400, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().contains("HTTP 400 (Bad Request)"));
			assertTrue(e.getMessage(), e.getMessage().contains("Bad request body"));
		}
	}
	
	@Test
	public void testUnparsableResponseNotRetried() throws Exception {
		for (int i=0; i<10; i++) { 
			nameResponses.add(jsonResponse("{not json"));
		}
		try { 
			wormsService().validate(toValidate(NAME, AUTHOR, 1));
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(1, nameRequests.get());
			assertEquals(0, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().contains("could not be parsed"));
		}
	}
	
	@Test
	public void testConnectionFailureRetried() throws Exception {
		ServiceClientConfig.setMaxRetries(2);
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(1, TimeUnit.SECONDS)
				.addInterceptor(new RequestThrottle(1, 0L)).build();
		org.marinespecies.aphia.v1_0.handler.ApiClient apiClient = WoRMSService.createApiClient(client);
		String url = server.url("/rest").toString();
		server.shutdown();
		apiClient.setBasePath(url);
		try { 
			new WoRMSService(apiClient).validate(toValidate(NAME, AUTHOR, 1));
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(0, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().contains("connection failure"));
			assertTrue(e.getMessage(), e.getMessage().contains("After 3 attempts"));
		}
		server = new MockWebServer();  // so that tearDown can shut it down
	}
	
	@Test
	public void testConcurrentRequestsAreLimited() throws Exception {
		ServiceClientConfig.setCacheSize(0);
		final AtomicInteger inFlight = new AtomicInteger();
		final AtomicInteger maxInFlight = new AtomicInteger();
		server.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				int current = inFlight.incrementAndGet();
				maxInFlight.accumulateAndGet(current, Math::max);
				Thread.sleep(20);
				inFlight.decrementAndGet();
				if (request.getPath().contains("/AphiaRecordsByName/")) { 
					nameRequests.incrementAndGet();
					return jsonResponse("[" + recordFor(request) + "]");
				}
				return jsonResponse(WORMS_RECORD);
			}
		});
		final WoRMSService service = wormsService(new RequestThrottle(2, 0L));
		int threadCount = 8;
		final CountDownLatch start = new CountDownLatch(1);
		final AtomicInteger failures = new AtomicInteger();
		List<Thread> threads = new ArrayList<Thread>();
		for (int t=0; t<threadCount; t++) { 
			final int id = t;
			Thread thread = new Thread(() -> { 
				try { 
					start.await();
					// distinct names, so that concurrent lookups are not combined into one request
					if (service.validate(toValidate(NAME + " var" + id, AUTHOR, id))==null) { 
						failures.incrementAndGet();
					}
				} catch (Exception e) { 
					failures.incrementAndGet();
				}
			});
			threads.add(thread);
			thread.start();
		}
		start.countDown();
		for (Thread thread : threads) { 
			thread.join(30000L);
		}
		assertEquals(0, failures.get());
		assertEquals(threadCount, nameRequests.get());
		assertTrue("max in flight " + maxInFlight.get(), maxInFlight.get() <= 2);
	}
	
	@Test
	public void testMinimumRequestInterval() throws Exception {
		ServiceClientConfig.setCacheSize(0);
		WoRMSService service = wormsService(new RequestThrottle(4, 100L));
		long start = System.nanoTime();
		service.validate(toValidate(NAME, AUTHOR, 1));
		service.validate(toValidate(NAME, AUTHOR, 2));
		long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
		// four requests (two name lookups, two habitat lookups), at least 100 ms apart
		assertEquals(2, nameRequests.get());
		assertTrue("elapsed " + elapsedMillis, elapsedMillis >= 290L);
	}
	
	@Test
	public void testIRMNGRetryAndCache() throws Exception {
		record = IRMNG_RECORD;
		nameResponses.add(errorResponse(504, "Gateway timeout"));
		org.irmng.aphia.v1_0.handler.ApiClient apiClient = IRMNGService.createApiClient(ServiceHttpClients.newThrottledClient(new RequestThrottle(2, 0L)));
		apiClient.setBasePath(server.url("/rest").toString());
		IRMNGService service = new IRMNGService(apiClient);
		NameUsage result = service.validate(toValidate(NAME, AUTHOR, 1));
		assertNotNull(result);
		assertEquals(NAME, result.getScientificName());
		assertEquals(2, nameRequests.get());
		assertNotNull(service.validate(toValidate(NAME, AUTHOR, 2)));
		assertEquals(2, nameRequests.get());
		
		for (int i=0; i<10; i++) { 
			nameResponses.add(errorResponse(404, "Not here"));
		}
		try { 
			service.validate(toValidate("Other name", AUTHOR, 3));
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(3, nameRequests.get());
			assertEquals(404, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().startsWith("IRMNG HTTP 404 (Not Found)"));
		}
	}

	/**
	 * Point the shared WoRMS ApiClient, used by the static lookup methods, at the test server.
	 * 
	 * @return the base path to restore after the test.
	 */
	private String pointSharedWoRMSClientAtServer() { 
		String original = WoRMSService.sharedApiClient().getBasePath();
		WoRMSService.sharedApiClient().setBasePath(server.url("/rest").toString());
		return original;
	}
	
	/**
	 * Concurrent validations of the same name make a single request, each caller gets its own copy of the result.
	 */
	@Test
	public void testConcurrentSameNameSingleRequest() throws Exception { 
		ServiceClientConfig.setCacheSize(0);
		server.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				Thread.sleep(200);
				if (request.getPath().contains("/AphiaRecordsByName/")) { 
					nameRequests.incrementAndGet();
					return jsonResponse("[" + WORMS_RECORD + "]");
				}
				recordRequests.incrementAndGet();
				return jsonResponse(WORMS_RECORD);
			}
		});
		final WoRMSService service = wormsService();
		int threadCount = 8;
		final CountDownLatch start = new CountDownLatch(1);
		final List<NameUsage> results = java.util.Collections.synchronizedList(new ArrayList<NameUsage>());
		final AtomicInteger failures = new AtomicInteger();
		List<Thread> threads = new ArrayList<Thread>();
		for (int t=0; t<threadCount; t++) { 
			final int id = t;
			Thread thread = new Thread(() -> { 
				try { 
					start.await();
					results.add(service.validate(toValidate(NAME, AUTHOR, id)));
				} catch (Exception e) { 
					failures.incrementAndGet();
				}
			});
			threads.add(thread);
			thread.start();
		}
		start.countDown();
		for (Thread thread : threads) { 
			thread.join(30000L);
		}
		assertEquals(0, failures.get());
		assertEquals(threadCount, results.size());
		assertEquals(1, nameRequests.get());
		assertEquals(1, recordRequests.get());
		java.util.Set<Integer> keys = new java.util.HashSet<Integer>();
		java.util.Set<NameUsage> instances = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<NameUsage,Boolean>());
		for (NameUsage result : results) { 
			assertNotNull(result);
			assertEquals(NAME, result.getScientificName());
			keys.add(result.getInputDbPK());
			instances.add(result);
		}
		// each caller gets its own copy, with its own input key
		assertEquals(threadCount, keys.size());
		assertEquals(threadCount, instances.size());
	}
	
	/**
	 * A failure of a habitat lookup is retried on its own, without resending the search by name.
	 */
	@Test
	public void testHabitatRetriedSeparately() throws Exception { 
		recordResponses.add(errorResponse(503, "Service temporarily unavailable"));
		recordResponses.add(errorResponse(500, "Internal error"));
		NameUsage result = wormsService().validate(toValidate(NAME, AUTHOR, 1));
		assertNotNull(result);
		assertEquals("true", result.getExtension().get("marine"));
		assertEquals(1, nameRequests.get());
		assertEquals(3, recordRequests.get());
	}
	
	/**
	 * After repeated failures, the circuit breaker stops requests, which fail without being sent, until 
	 * the open period has passed and a trial request succeeds.
	 */
	@Test
	public void testCircuitBreaker() throws Exception { 
		ServiceClientConfig.setMaxRetries(0);
		ServiceClientConfig.setFailureCacheMillis(0L);
		ServiceClientConfig.setCircuitBreakerThreshold(3);
		ServiceClientConfig.setCircuitBreakerOpenMillis(300L);
		for (int i=0; i<3; i++) { 
			nameResponses.add(errorResponse(503, "Service temporarily unavailable"));
		}
		WoRMSService service = wormsService();
		for (int i=0; i<3; i++) { 
			assertFalse(CircuitBreaker.forService(WoRMSService.SERVICE_NAME).isOpen());
			try { 
				service.validate(toValidate(NAME + " var" + i, AUTHOR, i));
				fail("Expected ServiceException");
			} catch (ServiceException e) { 
				assertEquals(503, e.getHttpStatusCode());
			}
		}
		assertEquals(3, nameRequests.get());
		assertTrue(CircuitBreaker.forService(WoRMSService.SERVICE_NAME).isOpen());
		try { 
			service.validate(toValidate(NAME, AUTHOR, 4));
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertTrue(e.getMessage(), e.getMessage().contains("unavailable after 3 consecutive failed calls"));
		}
		assertEquals(3, nameRequests.get());
		// IRMNG has its own circuit breaker
		assertFalse(CircuitBreaker.forService(IRMNGService.SERVICE_NAME).isOpen());
		
		// after the open period a trial request is made, which succeeds, closing the circuit breaker
		Thread.sleep(400L);
		assertNotNull(service.validate(toValidate(NAME, AUTHOR, 4)));
		assertEquals(4, nameRequests.get());
		assertFalse(CircuitBreaker.forService(WoRMSService.SERVICE_NAME).isOpen());
		assertEquals(0, CircuitBreaker.forService(WoRMSService.SERVICE_NAME).getConsecutiveFailures());
	}
	
	/**
	 * Responses with non-transient error statuses show the service is responding, so do not trip the circuit breaker.
	 */
	@Test
	public void testCircuitBreakerIgnoresClientErrors() throws Exception { 
		ServiceClientConfig.setFailureCacheMillis(0L);
		ServiceClientConfig.setCircuitBreakerThreshold(2);
		for (int i=0; i<4; i++) { 
			nameResponses.add(errorResponse(400, "Bad request"));
		}
		WoRMSService service = wormsService();
		for (int i=0; i<4; i++) { 
			try { 
				service.validate(toValidate(NAME + " var" + i, AUTHOR, i));
				fail("Expected ServiceException");
			} catch (ServiceException e) { 
				assertEquals(400, e.getHttpStatusCode());
			}
		}
		assertFalse(CircuitBreaker.forService(WoRMSService.SERVICE_NAME).isOpen());
		assertEquals(0, CircuitBreaker.forService(WoRMSService.SERVICE_NAME).getConsecutiveFailures());
	}
	
	/**
	 * A request that waits too long for a turn fails without being sent, without being remembered as 
	 * a failure, and without counting towards the circuit breaker.
	 */
	@Test
	public void testAcquireTimeout() throws Exception { 
		ServiceClientConfig.setCircuitBreakerThreshold(1);
		server.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				if (request.getPath().contains("/AphiaRecordsByName/")) { 
					nameRequests.incrementAndGet();
					Thread.sleep(500);
					return jsonResponse("[" + recordFor(request) + "]");
				}
				recordRequests.incrementAndGet();
				return jsonResponse(WORMS_RECORD);
			}
		});
		final WoRMSService service = wormsService(new RequestThrottle(1, 0L, 50L));
		Thread slow = new Thread(() -> { 
			try { 
				service.validate(toValidate(NAME + " slow", AUTHOR, 1));
			} catch (ServiceException e) { 
				// checked below
			}
		});
		slow.start();
		long deadline = System.currentTimeMillis() + 10000L;
		while (nameRequests.get()==0 && System.currentTimeMillis() < deadline) { 
			Thread.sleep(5L);
		}
		try { 
			service.validate(toValidate(NAME, AUTHOR, 2));
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertTrue(e.getMessage(), e.getMessage().contains("not sent"));
		}
		slow.join(10000L);
		assertEquals(1, nameRequests.get());
		assertFalse(CircuitBreaker.forService(WoRMSService.SERVICE_NAME).isOpen());
		// not remembered as a failure
		assertNotNull(service.validate(toValidate(NAME, AUTHOR, 2)));
		assertEquals(2, nameRequests.get());
	}
	
	/**
	 * A 403 for a name remembered from an earlier lookup is still treated as no match.
	 */
	@Test
	public void testRememberedForbiddenStillNoMatch() throws Exception { 
		nameResponses.add(errorResponse(403, ""));
		assertNull(wormsService().validate(toValidate(NAME, AUTHOR, 1)));
		// a different authorship, so not in the validation cache, the search by name failure is remembered
		assertNull(wormsService().validate(toValidate(NAME, "L.", 2)));
		assertEquals(1, nameRequests.get());
	}
	
	/**
	 * The static lookup methods retry, cache, and report failures as ApiExceptions.
	 */
	@Test
	public void testStaticLookups() throws Exception { 
		String originalBasePath = pointSharedWoRMSClientAtServer();
		try { 
			nameResponses.add(errorResponse(503, "Service temporarily unavailable"));
			List<NameUsage> matches = WoRMSService.lookupTaxon(NAME, AUTHOR);
			assertEquals(1, matches.size());
			assertEquals(AUTHOR, matches.get(0).getAuthorship());
			assertEquals(2, nameRequests.get());
			// cached
			matches = WoRMSService.lookupTaxon(NAME, AUTHOR);
			assertEquals(1, matches.size());
			assertEquals(2, nameRequests.get());
			
			NameUsage byId = WoRMSService.lookupTaxonByID("147436");
			assertEquals(NAME + " " + AUTHOR, byId.getScientificName());
			assertEquals(1, recordRequests.get());
			assertNotNull(WoRMSService.lookupTaxonByID("147436"));
			assertEquals(1, recordRequests.get());
			
			// a failure is reported as an ApiException carrying the status code
			ServiceClientConfig.setMaxRetries(0);
			nameResponses.add(errorResponse(503, "Service temporarily unavailable"));
			try { 
				WoRMSService.lookupGenus("Haematopus");
				fail("Expected ApiException");
			} catch (org.marinespecies.aphia.v1_0.handler.ApiException e) { 
				assertEquals(503, e.getCode());
				assertTrue(e.getMessage(), e.getMessage().contains("HTTP 503"));
				assertTrue(e.getCause() instanceof ServiceException);
			}
			
			// while the circuit breaker is open, as an ApiException with no status code
			ServiceClientConfig.setCircuitBreakerThreshold(1);
			nameResponses.add(errorResponse(503, "Service temporarily unavailable"));
			try { 
				WoRMSService.lookupTaxon("Haematopus palliatus", AUTHOR);
				fail("Expected ApiException");
			} catch (org.marinespecies.aphia.v1_0.handler.ApiException e) { 
				assertEquals(503, e.getCode());
			}
			int sent = nameRequests.get();
			try { 
				WoRMSService.lookupTaxon("Haematopus bachmani", AUTHOR);
				fail("Expected ApiException");
			} catch (org.marinespecies.aphia.v1_0.handler.ApiException e) { 
				assertEquals(0, e.getCode());
				assertTrue(e.getCause() instanceof ServiceUnavailableException);
			}
			assertEquals(sent, nameRequests.get());
		} finally { 
			WoRMSService.sharedApiClient().setBasePath(originalBasePath);
		}
	}
	
	/**
	 * The connectivity test made by the constructor goes through the configured client.
	 */
	@Test
	public void testConnectivityTestUsesConfiguredClient() throws Exception { 
		WoRMSService service = wormsService();
		service.test();
		RecordedRequest request = server.takeRequest(1, TimeUnit.SECONDS);
		assertNotNull(request);
		assertTrue(request.getHeader("User-Agent"), request.getHeader("User-Agent").startsWith("FilteredPush-sci_name_qc/"));
	}

}
