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
		nameResponses = new ConcurrentLinkedQueue<MockResponse>();
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
					return jsonResponse(record);
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
		
		// a different authorship is a cache miss
		wormsService().validate(toValidate(NAME, "L.", 4));
		assertEquals(4, nameRequests.get());
		// but the habitat for the same AphiaID is cached
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
		// failures are not cached
		nameResponses.clear();
		assertNotNull(wormsService().validate(toValidate(NAME, AUTHOR, 1)));
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
					return jsonResponse("[" + WORMS_RECORD + "]");
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
					if (service.validate(toValidate(NAME, AUTHOR, id))==null) { 
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

}
