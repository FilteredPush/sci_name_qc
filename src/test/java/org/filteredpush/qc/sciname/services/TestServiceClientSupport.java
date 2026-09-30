/** 
 * TestServiceClientSupport.java
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.marinespecies.aphia.v1_0.handler.ApiException;

import com.google.gson.JsonParseException;

/**
 * Unit tests for the retry, backoff, failure description, and caching support used by the 
 * remote name services.  No network access is required.
 * 
 * @author mole
 */
public class TestServiceClientSupport {
	
	@Before
	public void setUp() { 
		ServiceClientConfig.resetToDefaults();
		RequestThrottle.clearLastRequestUrl();
	}
	
	@After
	public void tearDown() { 
		ServiceClientConfig.resetToDefaults();
		RequestThrottle.clearLastRequestUrl();
	}
	
	private static Map<String,List<String>> headers(String... namesAndValues) { 
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		for (int i=0; i<namesAndValues.length; i=i+2) { 
			result.put(namesAndValues[i], Arrays.asList(namesAndValues[i+1]));
		}
		return result;
	}

	@Test
	public void testRetryableStatusCodes() {
		int[] retryable = { 408, 429, 500, 502, 503, 504 };
		for (int code : retryable) { 
			assertTrue("expected " + code + " to be retryable", RetryPolicy.isRetryableStatus(code));
			assertTrue(RetryPolicy.isRetryable(code, null));
		}
		int[] notRetryable = { 0, 200, 204, 301, 400, 401, 403, 404, 405, 410, 501 };
		for (int code : notRetryable) { 
			assertFalse("expected " + code + " not to be retryable", RetryPolicy.isRetryableStatus(code));
			assertFalse(RetryPolicy.isRetryable(code, null));
		}
		// transport level failures are retryable
		assertTrue(RetryPolicy.isRetryable(0, new IOException("connection reset")));
		assertTrue(RetryPolicy.isRetryable(0, new UnknownHostException("www.marinespecies.org")));
		assertTrue(RetryPolicy.isRetryable(0, new SocketTimeoutException("timeout")));
		assertTrue(RetryPolicy.isRetryable(0, new ConnectException("refused")));
		// but not deserialization failures
		assertFalse(RetryPolicy.isRetryable(0, new JsonParseException("bad json")));
	}
	
	@Test
	public void testBackoff() { 
		// no jitter gives half the exponential delay, full jitter approaches the exponential delay
		assertEquals(250L, RetryPolicy.computeBackoffMillis(0, 500L, 8000L, 0d));
		assertEquals(500L, RetryPolicy.computeBackoffMillis(1, 500L, 8000L, 0d));
		assertEquals(1000L, RetryPolicy.computeBackoffMillis(2, 500L, 8000L, 0d));
		assertEquals(2000L, RetryPolicy.computeBackoffMillis(3, 500L, 8000L, 0d));
		assertEquals(4000L, RetryPolicy.computeBackoffMillis(4, 500L, 8000L, 0d));
		// capped
		assertEquals(4000L, RetryPolicy.computeBackoffMillis(5, 500L, 8000L, 0d));
		assertEquals(4000L, RetryPolicy.computeBackoffMillis(62, 500L, 8000L, 0d));
		assertEquals(4000L, RetryPolicy.computeBackoffMillis(Integer.MAX_VALUE, 500L, 8000L, 0d));
		long max = RetryPolicy.computeBackoffMillis(3, 500L, 8000L, 0.999999d);
		assertTrue(max > 3990L && max <= 4000L);
		for (int attempt=0; attempt<10; attempt++) { 
			for (double jitter = 0d; jitter < 1d; jitter = jitter + 0.1d) { 
				long delay = RetryPolicy.computeBackoffMillis(attempt, 500L, 8000L, jitter);
				assertTrue(delay >= 250L);
				assertTrue(delay <= 8000L);
			}
		}
		// disabled
		assertEquals(0L, RetryPolicy.computeBackoffMillis(3, 0L, 8000L, 0.5d));
	}
	
	@Test
	public void testRetryAfterParsing() { 
		long now = 1000000000000L;
		assertEquals(RetryPolicy.NO_RETRY_AFTER, RetryPolicy.parseRetryAfterMillis(null, now));
		assertEquals(RetryPolicy.NO_RETRY_AFTER, RetryPolicy.parseRetryAfterMillis("", now));
		assertEquals(RetryPolicy.NO_RETRY_AFTER, RetryPolicy.parseRetryAfterMillis("soon", now));
		assertEquals(0L, RetryPolicy.parseRetryAfterMillis("0", now));
		assertEquals(5000L, RetryPolicy.parseRetryAfterMillis("5", now));
		assertEquals(120000L, RetryPolicy.parseRetryAfterMillis(" 120 ", now));
		// HTTP date, 1000000000000 ms is Sun, 09 Sep 2001 01:46:40 GMT
		assertEquals(10000L, RetryPolicy.parseRetryAfterMillis("Sun, 09 Sep 2001 01:46:50 GMT", now));
		// date in the past
		assertEquals(0L, RetryPolicy.parseRetryAfterMillis("Sun, 09 Sep 2001 01:46:30 GMT", now));
		// header lookup is case insensitive
		assertEquals(7000L, RetryPolicy.retryAfterMillis(headers("retry-after", "7"), now));
		assertEquals(7000L, RetryPolicy.retryAfterMillis(headers("Retry-After", "7"), now));
		assertEquals(RetryPolicy.NO_RETRY_AFTER, RetryPolicy.retryAfterMillis(null, now));
		assertEquals(RetryPolicy.NO_RETRY_AFTER, RetryPolicy.retryAfterMillis(headers("Content-Type", "text/html"), now));
	}
	
	@Test
	public void testDelayBeforeRetry() { 
		ServiceClientConfig.setBackoffBaseMillis(500L);
		ServiceClientConfig.setBackoffMaxMillis(8000L);
		ServiceClientConfig.setMaxRetryAfterMillis(30000L);
		assertEquals(250L, RetryPolicy.delayBeforeRetryMillis(0, RetryPolicy.NO_RETRY_AFTER, 0d));
		// Retry-After longer than backoff is honored
		assertEquals(5000L, RetryPolicy.delayBeforeRetryMillis(0, 5000L, 0d));
		// backoff longer than Retry-After is used
		assertEquals(4000L, RetryPolicy.delayBeforeRetryMillis(5, 1000L, 0d));
		// Retry-After longer than the maximum wait means don't retry
		assertEquals(-1L, RetryPolicy.delayBeforeRetryMillis(0, 60000L, 0d));
	}
	
	@Test
	public void testDescribeHttpFailureWithBlankReasonPhrase() { 
		String body = "<html><body>Too many requests, slow down</body></html>";
		ApiFailure failure = new ApiFailure("WoRMS", "", 429, 
				headers("retry-after", "5", "content-type", "text/html"), body, null, 
				"https://www.marinespecies.org/rest/AphiaRecordsByName/Haematopus%20ostralegus?like=false&marine_only=false");
		assertEquals(ApiFailure.Kind.HTTP, failure.getKind());
		assertTrue(failure.isRetryable());
		assertEquals(5000L, failure.getRetryAfterMillis());
		String description = failure.describe();
		assertTrue(description, description.startsWith("WoRMS HTTP 429 (Too Many Requests) from https://www.marinespecies.org/rest/AphiaRecordsByName/"));
		assertTrue(description, description.contains("Retry-After: 5"));
		assertTrue(description, description.contains("Content-Type: text/html"));
		assertTrue(description, description.contains("Too many requests, slow down"));
		
		ServiceException se = failure.toServiceException(null);
		assertEquals(429, se.getHttpStatusCode());
		assertTrue(se.isHttpError());
		assertEquals(description, se.getMessage());
	}
	
	@Test
	public void testDescribeFromApiExceptionWithNullMessage() { 
		ApiException e = new ApiException(null, 503, headers("Content-Type", "application/json"), null);
		assertNull(e.getMessage());
		ApiFailure failure = ApiFailure.from("WoRMS", e);
		assertEquals(ApiFailure.Kind.HTTP, failure.getKind());
		assertEquals(503, failure.getHttpStatusCode());
		assertTrue(failure.isRetryable());
		String description = failure.describe();
		assertTrue(description, description.startsWith("WoRMS HTTP 503 (Service Unavailable)"));
		ServiceException se = failure.toServiceException(null);
		assertTrue(se.getMessage().length() > 0);
		assertEquals(503, se.getHttpStatusCode());
		assertTrue(se.getCause() == e);
		
		// a 403 with an empty message is identified by code, and not retried
		ApiException forbidden = new ApiException("", 403, null, "Forbidden");
		ApiFailure forbiddenFailure = ApiFailure.from("WoRMS", forbidden);
		assertFalse(forbiddenFailure.isRetryable());
		assertEquals(403, forbiddenFailure.toServiceException(null).getHttpStatusCode());
		
		// an ApiException with no message, code, or cause
		ApiFailure empty = ApiFailure.from("IRMNG", new org.irmng.aphia.v1_0.handler.ApiException());
		assertEquals(ApiFailure.Kind.OTHER, empty.getKind());
		assertFalse(empty.isRetryable());
		assertTrue(empty.describe().length() > "IRMNG".length());
		assertTrue(empty.toServiceException(null).getMessage().trim().length() > 0);
	}
	
	@Test
	public void testDescribeTransportFailures() { 
		ApiFailure unknownHost = ApiFailure.from("WoRMS", new ApiException(new UnknownHostException("www.marinespecies.org")));
		assertEquals(ApiFailure.Kind.TRANSPORT, unknownHost.getKind());
		assertTrue(unknownHost.isRetryable());
		assertTrue(unknownHost.describe(), unknownHost.describe().contains("UnknownHostException"));
		assertTrue(unknownHost.describe(), unknownHost.describe().contains("www.marinespecies.org"));
		assertEquals(0, unknownHost.toServiceException(null).getHttpStatusCode());
		
		ApiFailure timeout = ApiFailure.from("WoRMS", new ApiException(new SocketTimeoutException("Read timed out")));
		assertEquals(ApiFailure.Kind.TRANSPORT, timeout.getKind());
		assertTrue(timeout.describe(), timeout.describe().contains("SocketTimeoutException"));
		
		ApiFailure refused = ApiFailure.from("WoRMS", new ApiException(new ConnectException("Connection refused")));
		assertTrue(refused.describe(), refused.describe().contains("ConnectException"));
		
		ApiFailure parse = ApiFailure.from("WoRMS", new JsonParseException("Expected BEGIN_ARRAY"));
		assertEquals(ApiFailure.Kind.DESERIALIZATION, parse.getKind());
		assertFalse(parse.isRetryable());
		assertTrue(parse.describe(), parse.describe().contains("could not be parsed"));
	}
	
	@Test
	public void testBodyTruncation() { 
		StringBuilder body = new StringBuilder();
		for (int i=0; i<2000; i++) { 
			body.append("x");
		}
		ApiFailure failure = new ApiFailure("WoRMS", null, 500, null, body.toString(), null, null);
		String description = failure.describe();
		assertTrue(description, description.contains("truncated"));
		assertTrue(description.length() < 700);
		assertNull(ApiFailure.truncate(null, 10));
		assertEquals("a b", ApiFailure.truncate("a\n  b", 10));
	}
	
	@Test
	public void testServiceExceptionNeverEmpty() { 
		assertEquals(ServiceException.UNSPECIFIED_MESSAGE, new ServiceException(null).getMessage());
		assertEquals(ServiceException.UNSPECIFIED_MESSAGE, new ServiceException("").getMessage());
		assertEquals(ServiceException.UNSPECIFIED_MESSAGE, new ServiceException("  ", new RuntimeException()).getMessage());
		assertEquals("HTTP 502 (Bad Gateway)", new ServiceException("", 502, null).getMessage());
		assertEquals("a message", new ServiceException("a message").getMessage());
		assertEquals(0, new ServiceException("a message").getHttpStatusCode());
		assertFalse(new ServiceException("a message").isHttpError());
	}
	
	@Test
	public void testRetrierRetriesTransientFailures() throws Exception { 
		ServiceClientConfig.setBackoffBaseMillis(1L);
		ServiceClientConfig.setBackoffMaxMillis(2L);
		ServiceClientConfig.setMaxRetries(3);
		final AtomicInteger calls = new AtomicInteger();
		String result = ServiceRetrier.execute("WoRMS", "test", () -> { 
			if (calls.incrementAndGet() < 3) { 
				throw new ApiException("", 503, null, "unavailable");
			}
			return "ok";
		});
		assertEquals("ok", result);
		assertEquals(3, calls.get());
		
		// gives up after maxRetries+1 attempts, with a useful message
		calls.set(0);
		try { 
			ServiceRetrier.execute("WoRMS", "test", () -> { 
				calls.incrementAndGet();
				throw new ApiException("", 429, headers("Retry-After", "0"), "slow down");
			});
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(4, calls.get());
			assertEquals(429, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().contains("HTTP 429"));
			assertTrue(e.getMessage(), e.getMessage().contains("slow down"));
		}
		
		// configurable retry count
		ServiceClientConfig.setMaxRetries(0);
		calls.set(0);
		try { 
			ServiceRetrier.execute("WoRMS", "test", () -> { 
				calls.incrementAndGet();
				throw new ApiException(new IOException("connection reset"));
			});
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(1, calls.get());
			assertEquals(0, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().contains("connection reset"));
		}
	}
	
	@Test
	public void testRetrierDoesNotRetryNonTransientFailures() { 
		ServiceClientConfig.setBackoffBaseMillis(1L);
		ServiceClientConfig.setBackoffMaxMillis(2L);
		int[] codes = { 400, 401, 403, 404 };
		for (final int code : codes) { 
			final AtomicInteger calls = new AtomicInteger();
			try { 
				ServiceRetrier.execute("WoRMS", "test", () -> { 
					calls.incrementAndGet();
					throw new ApiException(null, code, null, null);
				});
				fail("Expected ServiceException");
			} catch (ServiceException e) { 
				assertEquals(1, calls.get());
				assertEquals(code, e.getHttpStatusCode());
				assertTrue(e.getMessage(), e.getMessage().contains("HTTP " + code));
			}
		}
		// Retry-After beyond maximum wait is not retried
		ServiceClientConfig.setMaxRetryAfterMillis(1000L);
		final AtomicInteger calls = new AtomicInteger();
		try { 
			ServiceRetrier.execute("WoRMS", "test", () -> { 
				calls.incrementAndGet();
				throw new ApiException("", 503, headers("Retry-After", "3600"), null);
			});
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(1, calls.get());
			assertEquals(503, e.getHttpStatusCode());
		}
	}
	
	@Test
	public void testLookupCache() { 
		final AtomicInteger size = new AtomicInteger(2);
		LookupCache<String,String> cache = new LookupCache<String,String>(size::get);
		assertTrue(cache.isEnabled());
		assertNull(cache.get("a"));
		assertEquals(1L, cache.getMissCount());
		cache.put("a", "A");
		cache.put("b", null);
		LookupCache.Entry<String> entry = cache.get("a");
		assertNotNull(entry);
		assertEquals("A", entry.getValue());
		// null values are cached
		entry = cache.get("b");
		assertNotNull(entry);
		assertNull(entry.getValue());
		assertEquals(2L, cache.getHitCount());
		// least recently used entry is evicted when full
		cache.get("a");
		cache.put("c", "C");
		assertEquals(2, cache.size());
		assertNull(cache.get("b"));
		assertNotNull(cache.get("a"));
		assertNotNull(cache.get("c"));
		// disabling
		size.set(0);
		assertFalse(cache.isEnabled());
		assertNull(cache.get("a"));
		cache.put("d", "D");
		size.set(2);
		assertNull(cache.get("d"));
		cache.clear();
		assertEquals(0, cache.size());
		assertEquals(0L, cache.getHitCount());
	}
	
	@Test
	public void testLookupCacheConcurrentAccess() throws Exception { 
		final LookupCache<Integer,Integer> cache = new LookupCache<Integer,Integer>(() -> 50);
		List<Thread> threads = new ArrayList<Thread>();
		final AtomicInteger errors = new AtomicInteger();
		for (int t=0; t<8; t++) { 
			Thread thread = new Thread(() -> { 
				try { 
					for (int i=0; i<2000; i++) { 
						int key = i % 100;
						LookupCache.Entry<Integer> e = cache.get(key);
						if (e!=null && e.getValue().intValue()!=key) { 
							errors.incrementAndGet();
						}
						cache.put(key, key);
					}
				} catch (RuntimeException e) { 
					errors.incrementAndGet();
				}
			});
			threads.add(thread);
			thread.start();
		}
		for (Thread thread : threads) { 
			thread.join();
		}
		assertEquals(0, errors.get());
		assertTrue(cache.size() <= 50);
	}
	
	@Test
	public void testDefaultUserAgent() { 
		String userAgent = ServiceClientConfig.getUserAgent();
		assertTrue(userAgent, userAgent.startsWith("FilteredPush-sci_name_qc/"));
		assertTrue(userAgent, userAgent.contains("(+https://github.com/FilteredPush/sci_name_qc)"));
		assertFalse(userAgent, userAgent.contains("Swagger"));
		ServiceClientConfig.setUserAgent("Custom/1.0");
		assertEquals("Custom/1.0", ServiceClientConfig.getUserAgent());
		ServiceClientConfig.setUserAgent(null);
		assertEquals(ServiceClientConfig.getDefaultUserAgent(), ServiceClientConfig.getUserAgent());
	}
	
	@Test
	public void testConfigFromSystemProperties() { 
		String old = System.getProperty(ServiceClientConfig.MAX_RETRIES_PROPERTY);
		try { 
			System.setProperty(ServiceClientConfig.MAX_RETRIES_PROPERTY, "7");
			System.setProperty(ServiceClientConfig.CACHE_SIZE_PROPERTY, "notanumber");
			ServiceClientConfig.resetToDefaults();
			assertEquals(7, ServiceClientConfig.getMaxRetries());
			assertEquals(ServiceClientConfig.DEFAULT_CACHE_SIZE, ServiceClientConfig.getCacheSize());
		} finally { 
			if (old==null) { 
				System.clearProperty(ServiceClientConfig.MAX_RETRIES_PROPERTY);
			} else { 
				System.setProperty(ServiceClientConfig.MAX_RETRIES_PROPERTY, old);
			}
			System.clearProperty(ServiceClientConfig.CACHE_SIZE_PROPERTY);
			ServiceClientConfig.resetToDefaults();
		}
		assertEquals(ServiceClientConfig.DEFAULT_MAX_RETRIES, ServiceClientConfig.getMaxRetries());
	}

	/**
	 * getOrLoad caches values, including null, and does not reload a cached key.
	 */
	@Test
	public void testGetOrLoadCaches() throws Exception { 
		LookupCache<String,String> cache = new LookupCache<String,String>(() -> 10, () -> 60000L);
		AtomicInteger loads = new AtomicInteger();
		assertEquals("A", cache.getOrLoad("a", () -> { loads.incrementAndGet(); return "A"; }));
		assertEquals("A", cache.getOrLoad("a", () -> { loads.incrementAndGet(); return "other"; }));
		assertNull(cache.getOrLoad("none", () -> { loads.incrementAndGet(); return null; }));
		assertNull(cache.getOrLoad("none", () -> { loads.incrementAndGet(); return "other"; }));
		assertEquals(2, loads.get());
		
		// with the cache disabled, each call loads
		LookupCache<String,String> disabled = new LookupCache<String,String>(() -> 0, () -> 60000L);
		disabled.getOrLoad("a", () -> { loads.incrementAndGet(); return "A"; });
		disabled.getOrLoad("a", () -> { loads.incrementAndGet(); return "A"; });
		assertEquals(4, loads.get());
	}
	
	/**
	 * getOrLoad remembers failures for the configured period, with the status code, and forgets them afterwards.
	 */
	@Test
	public void testGetOrLoadRemembersFailures() throws Exception { 
		LookupCache<String,String> cache = new LookupCache<String,String>(() -> 10, () -> 200L);
		AtomicInteger loads = new AtomicInteger();
		try { 
			cache.getOrLoad("a", () -> { loads.incrementAndGet(); throw new ServiceException("down", 503, null); });
			fail("Expected ServiceException");
		} catch (ServiceUnavailableException e) { 
			fail("Unexpected ServiceUnavailableException");
		} catch (ServiceException e) { 
			assertEquals("down", e.getMessage());
		}
		assertEquals(1, cache.getFailureCount());
		try { 
			cache.getOrLoad("a", () -> { loads.incrementAndGet(); return "A"; });
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertEquals(503, e.getHttpStatusCode());
			assertTrue(e.getMessage(), e.getMessage().contains("failed recently"));
			assertTrue(e.getMessage(), e.getMessage().contains("down"));
		}
		assertEquals(1, loads.get());
		// other keys are loaded
		assertEquals("B", cache.getOrLoad("b", () -> { loads.incrementAndGet(); return "B"; }));
		// after the period, the lookup is made again
		Thread.sleep(300L);
		assertEquals("A", cache.getOrLoad("a", () -> { loads.incrementAndGet(); return "A"; }));
		assertEquals(3, loads.get());
		
		// clear forgets failures
		try { 
			cache.getOrLoad("c", () -> { throw new ServiceException("down"); });
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			// expected
		}
		cache.clear();
		assertEquals(0, cache.getFailureCount());
		assertEquals("C", cache.getOrLoad("c", () -> "C"));
	}
	
	/**
	 * getOrLoad does not remember calls that were not made, or failures when configured not to.
	 */
	@Test
	public void testGetOrLoadDoesNotRememberSomeFailures() throws Exception { 
		LookupCache<String,String> cache = new LookupCache<String,String>(() -> 10, () -> 60000L);
		try { 
			cache.getOrLoad("a", () -> { throw new ServiceUnavailableException("circuit open"); });
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertEquals("circuit open", e.getMessage());
		}
		try { 
			cache.getOrLoad("b", () -> { throw new ServiceException("interrupted", new InterruptedException()); });
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			// expected
		}
		assertEquals(0, cache.getFailureCount());
		assertEquals("A", cache.getOrLoad("a", () -> "A"));
		
		LookupCache<String,String> forgetful = new LookupCache<String,String>(() -> 10, () -> 0L);
		try { 
			forgetful.getOrLoad("a", () -> { throw new ServiceException("down"); });
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			// expected
		}
		assertEquals(0, forgetful.getFailureCount());
		assertEquals("A", forgetful.getOrLoad("a", () -> "A"));
	}
	
	/**
	 * Concurrent getOrLoad calls for the same key make one lookup, and share its result or failure.
	 */
	@Test
	public void testGetOrLoadCombinesConcurrentLookups() throws Exception { 
		// cache disabled, so that sharing is by combining in progress lookups, not by caching
		final LookupCache<String,String> cache = new LookupCache<String,String>(() -> 0, () -> 0L);
		final AtomicInteger loads = new AtomicInteger();
		final java.util.concurrent.CountDownLatch start = new java.util.concurrent.CountDownLatch(1);
		java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(8);
		try { 
			List<java.util.concurrent.Future<String>> results = new ArrayList<java.util.concurrent.Future<String>>();
			for (int i=0; i<8; i++) { 
				results.add(executor.submit(() -> { 
					start.await();
					return cache.getOrLoad("a", () -> { 
						loads.incrementAndGet();
						try { Thread.sleep(300L); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
						return "A";
					});
				}));
			}
			start.countDown();
			for (java.util.concurrent.Future<String> result : results) { 
				assertEquals("A", result.get(30, java.util.concurrent.TimeUnit.SECONDS));
			}
			assertEquals(1, loads.get());
			
			// a failure is shared too
			final java.util.concurrent.CountDownLatch start2 = new java.util.concurrent.CountDownLatch(1);
			List<java.util.concurrent.Future<String>> failures = new ArrayList<java.util.concurrent.Future<String>>();
			for (int i=0; i<4; i++) { 
				failures.add(executor.submit(() -> { 
					start2.await();
					return cache.getOrLoad("b", () -> { 
						loads.incrementAndGet();
						try { Thread.sleep(300L); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
						throw new ServiceException("down", 500, null);
					});
				}));
			}
			start2.countDown();
			for (java.util.concurrent.Future<String> failure : failures) { 
				try { 
					failure.get(30, java.util.concurrent.TimeUnit.SECONDS);
					fail("Expected failure");
				} catch (java.util.concurrent.ExecutionException e) { 
					assertTrue(e.getCause() instanceof ServiceException);
					assertEquals(500, ((ServiceException) e.getCause()).getHttpStatusCode());
				}
			}
			assertEquals(2, loads.get());
		} finally { 
			executor.shutdownNow();
		}
	}
	
	/**
	 * The circuit breaker trips after the threshold of consecutive failures, allows a single 
	 * trial after the open period, and closes on success.
	 */
	@Test
	public void testCircuitBreaker() throws Exception { 
		ServiceClientConfig.setCircuitBreakerThreshold(2);
		ServiceClientConfig.setCircuitBreakerOpenMillis(200L);
		CircuitBreaker breaker = new CircuitBreaker("Test");
		assertFalse(breaker.check("a"));
		breaker.recordFailure();
		assertFalse(breaker.isOpen());
		// a success resets the count
		assertFalse(breaker.check("a"));
		breaker.recordSuccess();
		assertEquals(0, breaker.getConsecutiveFailures());
		breaker.recordFailure();
		breaker.recordFailure();
		assertTrue(breaker.isOpen());
		try { 
			breaker.check("a");
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertTrue(e.getMessage(), e.getMessage().contains("Test unavailable after 2 consecutive failed calls"));
			assertTrue(e.getMessage(), e.getMessage().contains("[a]"));
		}
		Thread.sleep(300L);
		assertFalse(breaker.isOpen());
		// one trial call is allowed
		assertTrue(breaker.check("trial"));
		try { 
			breaker.check("other");
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertTrue(e.getMessage(), e.getMessage().contains("trial"));
		}
		// a trial that says nothing about the service releases the trial slot
		breaker.release(true);
		assertTrue(breaker.check("trial"));
		// a failed trial trips the breaker again
		breaker.recordFailure();
		assertTrue(breaker.isOpen());
		Thread.sleep(300L);
		assertTrue(breaker.check("trial"));
		breaker.recordSuccess();
		assertFalse(breaker.check("a"));
		assertEquals(0, breaker.getConsecutiveFailures());
		
		// reset closes the breaker
		breaker.recordFailure();
		breaker.recordFailure();
		assertTrue(breaker.isOpen());
		breaker.reset();
		assertFalse(breaker.isOpen());
		
		// a threshold of 0 disables the breaker
		ServiceClientConfig.setCircuitBreakerThreshold(0);
		for (int i=0; i<10; i++) { 
			breaker.recordFailure();
		}
		assertFalse(breaker.isOpen());
		
		// breakers are shared per service
		assertTrue(CircuitBreaker.forService("Test") == CircuitBreaker.forService("Test"));
		assertFalse(CircuitBreaker.forService("Test") == CircuitBreaker.forService("Other"));
	}
	
	/**
	 * ServiceRetrier reports outcomes to the circuit breaker, a non-transient HTTP error counts as a success, 
	 * a request not sent is reported as a ServiceUnavailableException and is not counted.
	 */
	@Test
	public void testServiceRetrierAndCircuitBreaker() throws Exception { 
		ServiceClientConfig.setMaxRetries(0);
		ServiceClientConfig.setCircuitBreakerThreshold(2);
		String service = "RetrierTest";
		CircuitBreaker breaker = CircuitBreaker.forService(service);
		breaker.reset();
		try { 
			ServiceRetrier.execute(service, "a", () -> { throw new ApiException(503, "unavailable"); });
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(503, e.getHttpStatusCode());
		}
		assertEquals(1, breaker.getConsecutiveFailures());
		try { 
			ServiceRetrier.execute(service, "a", () -> { throw new ApiException(404, "not found"); });
			fail("Expected ServiceException");
		} catch (ServiceException e) { 
			assertEquals(404, e.getHttpStatusCode());
		}
		assertEquals(0, breaker.getConsecutiveFailures());
		try { 
			ServiceRetrier.execute(service, "a", () -> { throw new ApiException(new RequestNotSentException("Timed out waiting")); });
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertTrue(e.getMessage(), e.getMessage().contains("not sent"));
		}
		assertEquals(0, breaker.getConsecutiveFailures());
		for (int i=0; i<2; i++) { 
			try { 
				ServiceRetrier.execute(service, "a", () -> { throw new ApiException(new ConnectException("refused")); });
				fail("Expected ServiceException");
			} catch (ServiceException e) { 
				assertFalse(e instanceof ServiceUnavailableException);
			}
		}
		assertTrue(breaker.isOpen());
		AtomicInteger calls = new AtomicInteger();
		try { 
			ServiceRetrier.execute(service, "a", () -> calls.incrementAndGet());
			fail("Expected ServiceUnavailableException");
		} catch (ServiceUnavailableException e) { 
			assertEquals(0, calls.get());
		}
		breaker.reset();
		assertEquals(Integer.valueOf(1), ServiceRetrier.execute(service, "a", () -> calls.incrementAndGet()));
	}
	
	/**
	 * The new settings have defaults, minimums, and can be set by system properties.
	 */
	@Test
	public void testGuardSettings() { 
		assertEquals(ServiceClientConfig.DEFAULT_ACQUIRE_TIMEOUT_MILLIS, ServiceClientConfig.getAcquireTimeoutMillis());
		assertEquals(ServiceClientConfig.DEFAULT_FAILURE_CACHE_MILLIS, ServiceClientConfig.getFailureCacheMillis());
		assertEquals(ServiceClientConfig.DEFAULT_CIRCUIT_BREAKER_THRESHOLD, ServiceClientConfig.getCircuitBreakerThreshold());
		assertEquals(ServiceClientConfig.DEFAULT_CIRCUIT_BREAKER_OPEN_MILLIS, ServiceClientConfig.getCircuitBreakerOpenMillis());
		ServiceClientConfig.setAcquireTimeoutMillis(0L);
		assertEquals(1L, ServiceClientConfig.getAcquireTimeoutMillis());
		ServiceClientConfig.setFailureCacheMillis(-5L);
		assertEquals(0L, ServiceClientConfig.getFailureCacheMillis());
		ServiceClientConfig.setCircuitBreakerThreshold(-1);
		assertEquals(0, ServiceClientConfig.getCircuitBreakerThreshold());
		try { 
			System.setProperty(ServiceClientConfig.CIRCUIT_BREAKER_THRESHOLD_PROPERTY, "7");
			System.setProperty(ServiceClientConfig.FAILURE_CACHE_PROPERTY, "not a number");
			ServiceClientConfig.resetToDefaults();
			assertEquals(7, ServiceClientConfig.getCircuitBreakerThreshold());
			assertEquals(ServiceClientConfig.DEFAULT_FAILURE_CACHE_MILLIS, ServiceClientConfig.getFailureCacheMillis());
		} finally { 
			System.clearProperty(ServiceClientConfig.CIRCUIT_BREAKER_THRESHOLD_PROPERTY);
			System.clearProperty(ServiceClientConfig.FAILURE_CACHE_PROPERTY);
			ServiceClientConfig.resetToDefaults();
		}
		
		RequestThrottle throttle = new RequestThrottle(2, 0L, 0L);
		assertEquals(1L, throttle.getAcquireTimeoutMillis());
		assertEquals(ServiceClientConfig.getAcquireTimeoutMillis(), new RequestThrottle(2, 0L).getAcquireTimeoutMillis());
	}

}
