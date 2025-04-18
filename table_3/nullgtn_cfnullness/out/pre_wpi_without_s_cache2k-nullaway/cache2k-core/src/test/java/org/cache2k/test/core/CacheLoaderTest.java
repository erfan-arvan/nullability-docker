package org.cache2k.test.core;

/*
 * #%L
 * cache2k core implementation
 * %%
 * Copyright (C) 2000 - 2020 headissue GmbH, Munich
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import static org.assertj.core.api.Assertions.assertThat;
import org.cache2k.expiry.ExpiryPolicy;
import org.cache2k.expiry.ExpiryTimeValues;
import org.cache2k.integration.FunctionalCacheLoader;
import org.cache2k.io.AsyncCacheLoader;
import org.cache2k.io.CacheLoaderException;
import org.cache2k.test.core.expiry.ExpiryTest;
import org.cache2k.test.util.CacheRule;
import org.cache2k.test.util.Condition;
import org.cache2k.io.AdvancedCacheLoader;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.CacheEntry;
import org.cache2k.io.CacheLoader;
import org.cache2k.CacheOperationCompletionListener;
import org.cache2k.test.util.ExpectedException;
import org.cache2k.test.util.TestingBase;
import org.cache2k.testing.category.FastTests;
import org.cache2k.test.util.IntCacheRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.Timeout;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.Assert.*;
import static org.cache2k.test.core.StaticUtil.*;

/**
 * Test the cache loader.
 *
 * @author Jens Wilke
 * @see CacheLoader
 * @see AdvancedCacheLoader
 * @see CacheOperationCompletionListener
 * @see AsyncCacheLoader
 */
@SuppressWarnings("unchecked")
@Category(FastTests.class)
public class CacheLoaderTest extends TestingBase {

    @Rule
    public CacheRule<Integer, Integer> target = new IntCacheRule();

    @Rule
    public Timeout globalTimeout = new Timeout((int) TestingParameters.MAX_FINISH_WAIT_MILLIS * 2);

    volatile int loaderExecutionCount = 0;

    /**
     * Some tests expect that there are at least two loader threads.
     */
    @Test
    public void testThreadCount() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                assertThat(b.config().getLoaderThreadCount()).describedAs("minim thread count").isGreaterThanOrEqualTo(2);
            }
        });
    }

    @Test
    public void testFunctionalLoader() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new FunctionalCacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key) {
                        return key * 7;
                    }
                });
            }
        });
        int v = c.get(1);
        assertEquals(7, v);
    }

    @Test
    public void testSeparateLoaderExecutor() throws ExecutionException, InterruptedException {
        final AtomicInteger executionCount = new AtomicInteger(0);
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key) {
                        return key * 2;
                    }
                });
                b.loaderExecutor(new Executor() {

                    @Override
                    public void execute(Runnable command) {
                        executionCount.incrementAndGet();
                        getLoaderExecutor().execute(command);
                    }
                });
            }
        });
        assertEquals((Integer) 10, c.get(5));
        assertEquals((Integer) 20, c.get(10));
        assertEquals(0, executionCount.get());
        CompletionWaiter waiter = new CompletionWaiter();
        c.loadAll(asList(1, 2, 3)).get();
        assertEquals("executor is used", 3, executionCount.get());
    }

    @Test
    public void testSeparatePrefetchExecutor() {
        final AtomicInteger executionCount = new AtomicInteger(0);
        final AtomicInteger prefetchExecutionCount = new AtomicInteger(0);
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key) {
                        return key * 2;
                    }
                });
                b.loaderExecutor(new Executor() {

                    @Override
                    public void execute(Runnable command) {
                        executionCount.incrementAndGet();
                        getLoaderExecutor().execute(command);
                    }
                });
                b.refreshExecutor(new Executor() {

                    @Override
                    public void execute(Runnable command) {
                        prefetchExecutionCount.incrementAndGet();
                        getLoaderExecutor().execute(command);
                    }
                });
            }
        });
        assertEquals((Integer) 10, c.get(5));
        assertEquals((Integer) 20, c.get(10));
        assertEquals(0, executionCount.get());
        CompletionWaiter waiter = new CompletionWaiter();
        c.loadAll(toIterable(1, 2, 3), waiter);
        waiter.awaitCompletion();
        assertEquals("executor is used", 3, executionCount.get());
    }

    @Test
    public void testLoader() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key) {
                        return key * 2;
                    }
                });
            }
        });
        assertEquals((Integer) 10, c.get(5));
        assertEquals((Integer) 20, c.get(10));
        assertFalse(c.containsKey(2));
        assertTrue(c.containsKey(5));
    }

    @Test
    public void testLoadNull() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(@Nullable() Integer key) {
                        return null;
                    }
                }).permitNullValues(true);
            }
        });
        assertNull(c.get(5));
        assertTrue(c.containsKey(5));
    }

    static class MarkerException extends RuntimeException {
    }

    static class AlwaysFailException extends MarkerException {
    }

    /**
     * Test all aspects of when a loader throws an exception permanently.
     */
    @Test
    public void loadExceptionSyncLoader() {
        Cache<Integer, Integer> c = target.cache(b -> b.loader(k -> {
            throw new AlwaysFailException();
        }));
        loadExceptionChecks(c);
    }

    @Test
    public void loadExceptionAsyncSyncLoaderImmediateFail() {
        Cache<Integer, Integer> c = target.cache(b -> b.loader(new AsyncCacheLoader<Integer, Integer>() {

            @Override
            public void load(@Nullable() Integer key, @Nullable() Context<Integer, Integer> context, @Nullable() Callback<Integer> callback) throws Exception {
                throw new AlwaysFailException();
            }
        }));
        loadExceptionChecks(c);
    }

    @Test
    public void loadExceptionAsyncSyncLoaderDelayedFail() {
        Cache<Integer, Integer> c = target.cache(b -> b.loader(new AsyncCacheLoader<Integer, Integer>() {

            @Override
            public void load(@Nullable() Integer key, Context<Integer, Integer> context, Callback<Integer> callback) {
                context.getExecutor().execute(() -> {
                    callback.onLoadFailure(new AlwaysFailException());
                });
            }
        }));
        loadExceptionChecks(c);
    }

    private void loadExceptionChecks(Cache<Integer, Integer> c) {
        final Integer key = 6;
        assertThatCode(() -> c.get(5)).as("get() propagates loader exception").isInstanceOf(CacheLoaderException.class).getCause().isInstanceOf(AlwaysFailException.class);
        assertThatCode(() -> c.loadAll(asList(key)).get()).as("loadAll().get() single value propagates loader exception").isInstanceOf(ExecutionException.class).getCause().isInstanceOf(CacheLoaderException.class).getCause().isInstanceOf(AlwaysFailException.class);
        assertThatCode(() -> c.loadAll(asList(key, 7, 8)).get()).as("loadAll().get() propagates loader exception").isInstanceOf(ExecutionException.class).getCause().isInstanceOf(CacheLoaderException.class).as("contains number of exceptions").hasMessageContaining("3").getCause().isInstanceOf(AlwaysFailException.class);
        assertThatCode(() -> c.reloadAll(asList(key, 7, 8)).get()).as("reloadAll().get() propagates loader exception").isInstanceOf(ExecutionException.class).getCause().isInstanceOf(CacheLoaderException.class).as("contains number of exceptions").hasMessageContaining("3").hasMessageContaining("finished with 3 exceptions out of 3 operations, " + "one propagated as cause").getCause().isInstanceOf(AlwaysFailException.class);
        assertThat(c.peek(key)).as("expect nothing loaded and no exception present on entry").isNull();
        c.put(key, 123);
        assertThat(c.peek(key)).as("entry value can be set").isEqualTo(123);
    }

    @Test
    public void testLoadNull_Reject() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(@Nullable() Integer key) {
                        return null;
                    }
                });
            }
        });
        try {
            c.get(5);
            fail();
        } catch (CacheLoaderException expected) {
        }
    }

    @Test
    public void testLoadNull_NoCache() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(@Nullable() Integer key) {
                        return null;
                    }
                }).expiryPolicy(new ExpiryPolicy<Integer, Integer>() {

                    @Override
                    public long calculateExpiryTime(@Nullable() Integer key, @Nullable() Integer value, @Nullable() long loadTime, @Nullable() CacheEntry<Integer, Integer> currentEntry) {
                        return NOW;
                    }
                });
            }
        });
        assertNull(c.get(5));
        assertFalse(c.containsKey(5));
    }

    @Test
    public void testAdvancedLoader() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AdvancedCacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key, @Nullable() long startTime, @Nullable() CacheEntry<Integer, Integer> e) {
                        return key * 2;
                    }
                });
            }
        });
        assertEquals((Integer) 10, c.get(5));
        assertEquals((Integer) 20, c.get(10));
        assertFalse(c.containsKey(2));
        assertTrue(c.containsKey(5));
    }

    @Test
    public void testLoadAll() {
        final AtomicInteger countLoad = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(@Nullable() Integer key) {
                        return countLoad.incrementAndGet();
                    }
                });
            }
        });
        c.get(5);
        CompletionWaiter w = new CompletionWaiter();
        c.loadAll(toIterable(5, 6), w);
        w.awaitCompletion();
        assertEquals(2, countLoad.get());
        assertEquals((Integer) 2, c.get(6));
        c.loadAll(toIterable(5, 6), null);
        c.loadAll(Collections.EMPTY_SET, null);
    }

    @Test
    public void testReloadAll() throws ExecutionException, InterruptedException {
        final AtomicInteger countLoad = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(@Nullable() Integer key) {
                        return countLoad.incrementAndGet();
                    }
                });
            }
        });
        c.get(5);
        assertEquals(1, countLoad.get());
        c.reloadAll(asList(5, 6)).get();
        assertEquals(3, countLoad.get());
        c.reloadAll(toIterable(5, 6));
        c.reloadAll(Collections.EMPTY_SET);
    }

    /**
     * getAsyncLoadsStarted uses the task count from the executor which is not
     * exact. We use is since we only want to know whether the loader will
     * be invoked, testing for the enqueued task is sufficient and faster.
     * Await the execution of the loader as fallback.
     */
    private boolean isLoadStarted(Cache<Integer, Integer> cache) {
        if (latestInfo(cache).getAsyncLoadsStarted() > 0) {
            return true;
        }
        await("Await loader execution", new Condition() {

            @Override
            public boolean check() {
                return loaderExecutionCount > 0;
            }
        });
        return true;
    }

    /**
     * We should always have two loader threads.
     */
    @Test
    public void testTwoLoaderThreadsAndPoolInfo() throws Exception {
        final CountDownLatch inLoader = new CountDownLatch(2);
        final CountDownLatch releaseLoader = new CountDownLatch(1);
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key) throws Exception {
                        inLoader.countDown();
                        releaseLoader.await();
                        return key * 2;
                    }
                });
            }
        });
        c.loadAll(toIterable(1), null);
        c.loadAll(toIterable(2), null);
        inLoader.await();
        assertEquals(2, latestInfo(c).getAsyncLoadsStarted());
        assertEquals(2, latestInfo(c).getAsyncLoadsInFlight());
        assertEquals(2, latestInfo(c).getLoaderThreadsMaxActive());
        releaseLoader.countDown();
    }

    /**
     * Start two overlapping loads, expect that one is done in the caller thread,
     * since only one thread is available.
     */
    @Test
    public void testOneLoaderThreadsAndPoolInfo() throws Exception {
        final Thread callingThread = Thread.currentThread();
        final CountDownLatch inLoader = new CountDownLatch(1);
        final CountDownLatch releaseLoader = new CountDownLatch(1);
        final AtomicInteger asyncCount = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loaderThreadCount(1).loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key) throws Exception {
                        if (callingThread != Thread.currentThread()) {
                            asyncCount.incrementAndGet();
                            inLoader.countDown();
                            releaseLoader.await();
                        }
                        return key * 2;
                    }
                });
            }
        });
        c.loadAll(toIterable(1), null);
        c.loadAll(toIterable(2), null);
        inLoader.await();
        assertEquals("only one load is separate thread", 1, latestInfo(c).getAsyncLoadsStarted());
        assertEquals("only one load is separate thread", 1, asyncCount.get());
        assertEquals(1, latestInfo(c).getAsyncLoadsInFlight());
        assertEquals(1, latestInfo(c).getLoaderThreadsMaxActive());
        releaseLoader.countDown();
    }

    @Test
    public void multipleWaitersCompleteAfterLoad_noThreads_sync() {
        multipleWaitersCompleteAfterLoad(false, false);
    }

    @Test
    public void multipleWaitersCompleteAfterLoad_threads_sync() {
        multipleWaitersCompleteAfterLoad(true, false);
    }

    @Test
    public void multipleWaitersCompleteAfterLoad_noThreads_async() {
        multipleWaitersCompleteAfterLoad(false, true);
    }

    @Test
    public void multipleWaitersCompleteAfterLoad_threads_async() {
        multipleWaitersCompleteAfterLoad(true, true);
    }

    /**
     * Test multiple threads waiting for a single load to complete. Calls to
     * {@link Cache#loadAll(Iterable, CacheOperationCompletionListener)} are not allowed to
     * block. Multiple load requests only lead to one load. All requests are completed when the
     * load is completed.
     */
    private void multipleWaitersCompleteAfterLoad(boolean useThreads, boolean async) {
        final int anyKey = 1;
        final int waiterCount = MINIMAL_LOADER_THREADS;
        final CountDownLatch complete = new CountDownLatch(waiterCount);
        final CountDownLatch releaseLoader = new CountDownLatch(1);
        final CountDownLatch threadsStarted = new CountDownLatch(waiterCount);
        final CountDownLatch threadsCompleted = new CountDownLatch(waiterCount);
        final AtomicInteger loaderCallCount = new AtomicInteger();
        Cache2kBuilder<Integer, Integer> b = builder(Integer.class, Integer.class);
        if (async) {
            b.loader(new AsyncCacheLoader<Integer, Integer>() {

                @Override
                public void load(@Nullable() Integer key, @Nullable() Context<Integer, Integer> context, final Callback<Integer> callback) {
                    loaderCallCount.incrementAndGet();
                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                releaseLoader.await();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            callback.onLoadSuccess(123);
                        }
                    });
                    t.start();
                }
            });
        } else {
            b.loader(new CacheLoader<Integer, Integer>() {

                @Override
                public Integer load(@Nullable() Integer key) throws Exception {
                    loaderCallCount.incrementAndGet();
                    releaseLoader.await();
                    return 123;
                }
            });
        }
        final Cache<Integer, Integer> c = b.build();
        final CacheOperationCompletionListener l = new CacheOperationCompletionListener() {

            @Override
            public void onCompleted() {
                complete.countDown();
            }

            @Override
            public void onException(@Nullable Throwable exception) {
            }
        };
        Thread[] ta = new Thread[waiterCount];
        for (int i = 0; i < waiterCount; i++) {
            if (useThreads) {
                ta[i] = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        threadsStarted.countDown();
                        c.loadAll(toIterable(anyKey), l);
                        threadsCompleted.countDown();
                    }
                });
                ta[i].start();
            } else {
                c.loadAll(toIterable(anyKey), l);
            }
        }
        if (useThreads) {
            awaitCountdown(threadsStarted);
            awaitCountdown(threadsCompleted);
        }
        releaseLoader.countDown();
        awaitCountdown(complete);
        assertEquals(1, loaderCallCount.get());
    }

    void awaitCountdown(CountDownLatch latch) {
        try {
            boolean gotTimeout = !latch.await(TestingParameters.MAX_FINISH_WAIT_MILLIS / 2, TimeUnit.MILLISECONDS);
            if (gotTimeout) {
                fail("timeout");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Execute loader in another thread.
     */
    @Test
    public void blockAndComplete() throws Exception {
        final int count = 1000;
        final AtomicInteger loaderCalled = new AtomicInteger();
        final CountDownLatch complete = new CountDownLatch(count);
        final AtomicInteger loaderExecuted = new AtomicInteger();
        final CountDownLatch releaseLoader = new CountDownLatch(1);
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(final Integer key, Context<Integer, Integer> ctx, final Callback<Integer> callback) {
                        loaderCalled.incrementAndGet();
                        ctx.getLoaderExecutor().execute(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    releaseLoader.await();
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                loaderExecuted.incrementAndGet();
                                callback.onLoadSuccess(key);
                            }
                        });
                    }
                });
            }
        });
        CacheOperationCompletionListener l = new CacheOperationCompletionListener() {

            @Override
            public void onCompleted() {
                complete.countDown();
            }

            @Override
            public void onException(@Nullable Throwable exception) {
            }
        };
        for (int i = 0; i < count; i++) {
            c.loadAll(toIterable(1, 2, 3), l);
        }
        releaseLoader.countDown();
        complete.await(TestingParameters.MAX_FINISH_WAIT_MILLIS, TimeUnit.MILLISECONDS);
    }

    /**
     * Execute loader in another thread.
     */
    @Test
    public void testAsyncLoaderLoadViaExecutor() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        final AtomicInteger loaderExecuted = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(final Integer key, Context<Integer, Integer> ctx, final Callback<Integer> callback) {
                        loaderCalled.incrementAndGet();
                        ctx.getLoaderExecutor().execute(new Runnable() {

                            @Override
                            public void run() {
                                loaderExecuted.incrementAndGet();
                                callback.onLoadSuccess(key);
                            }
                        });
                    }
                });
            }
        });
        Integer v = c.get(1);
        assertEquals(1, (int) v);
    }

    /**
     * Call the callback within the loading thread.
     */
    @Test
    public void testAsyncLoaderLoadDirect() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(Integer key, @Nullable() Context<Integer, Integer> ctx, Callback<Integer> callback) {
                        loaderCalled.incrementAndGet();
                        callback.onLoadSuccess(key);
                    }
                });
            }
        });
        Integer v = c.get(1);
        assertEquals(1, (int) v);
    }

    @Test
    public void testAsyncLoaderContextProperties() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(Integer key, Context<Integer, Integer> ctx, Callback<Integer> callback) {
                        int cnt = loaderCalled.getAndIncrement();
                        if (cnt == 0) {
                            assertNull(ctx.getCurrentEntry());
                        } else {
                            assertEquals(key, ctx.getCurrentEntry().getValue());
                            assertNull(ctx.getCurrentEntry().getException());
                        }
                        callback.onLoadSuccess(key);
                    }
                });
            }
        });
        Integer v = c.get(1);
        assertEquals(1, (int) v);
        reload(c, 1);
    }

    @Test
    public void testAsyncLoaderContextProperties_withException() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.expireAfterWrite(TestingParameters.MAX_FINISH_WAIT_MILLIS, TimeUnit.MILLISECONDS);
                b.resiliencePolicy(new ExpiryTest.EnableExceptionCaching());
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(@Nullable() Integer key, Context<Integer, Integer> ctx, Callback<Integer> callback) {
                        int cnt = loaderCalled.getAndIncrement();
                        if (cnt == 0) {
                            assertNull(ctx.getCurrentEntry());
                        } else {
                            assertNull(ctx.getCurrentEntry());
                        }
                        callback.onLoadFailure(new ExpectedException());
                    }
                });
            }
        });
        assertThatCode(() -> c.get(1)).getRootCause().isInstanceOf(ExpectedException.class);
        assertNotNull("exception cached", c.peekEntry(1).getException());
        assertThatCode(() -> c.reloadAll(asList(1)).get()).getRootCause().isInstanceOf(ExpectedException.class);
    }

    /**
     * Check that exception isn't blocking anything
     */
    @Test
    public void testAsyncLoaderException() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(@Nullable() Integer key, @Nullable() Context<Integer, Integer> ctx, @Nullable() Callback<Integer> callback) {
                        loaderCalled.incrementAndGet();
                        throw new ExpectedException();
                    }
                });
            }
        });
        try {
            c.get(1);
            fail("exception expected");
        } catch (CacheLoaderException expected) {
            assertTrue(expected.getCause() instanceof ExpectedException);
        }
        c.put(1, 1);
        assertNotNull(c.get(1));
    }

    @Test
    public void testAsyncLoaderWithExecutorWithAsync() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        final AtomicInteger loaderExecuted = new AtomicInteger();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(final Integer key, Context<Integer, Integer> ctx, final Callback<Integer> callback) {
                        loaderCalled.incrementAndGet();
                        ctx.getLoaderExecutor().execute(new Runnable() {

                            @Override
                            public void run() {
                                loaderExecuted.incrementAndGet();
                                callback.onLoadSuccess(key);
                            }
                        });
                    }
                });
            }
        });
        CompletionWaiter w = new CompletionWaiter();
        c.loadAll(TestingBase.keys(1, 2, 1802), w);
        w.awaitCompletion();
        assertEquals(1, (int) c.peek(1));
        Object o1 = c.peek(1802);
        assertTrue(c.peek(1802) == o1);
        w = new CompletionWaiter();
        c.reloadAll(TestingBase.keys(1802, 4, 5), w);
        w.awaitCompletion();
        assertNotNull(c.peek(1802));
        assertTrue(c.peek(1802) != o1);
    }

    @Test
    public void testAsyncLoaderWithExecutorWithAsyncCopy() throws Exception {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(final Integer key, Context<Integer, Integer> ctx, final Callback<Integer> callback) {
                        ctx.getLoaderExecutor().execute(new Runnable() {

                            @Override
                            public void run() {
                                callback.onLoadSuccess(key);
                            }
                        });
                    }
                });
            }
        });
        c.loadAll(TestingBase.keys(1, 2, 1802)).get();
        assertNotNull(c.peek(1802));
        assertEquals(1, (int) c.peek(1));
        Object o1 = c.peek(1802);
        assertTrue(c.peek(1802) == o1);
        c.reloadAll(TestingBase.keys(1802, 4, 5)).get();
        assertNotNull(c.peek(1802));
        assertTrue(c.peek(1802) != o1);
    }

    @Test
    public void testAsyncLoaderDoubleCallback() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        final AtomicInteger loaderExecuted = new AtomicInteger();
        final AtomicInteger gotException = new AtomicInteger();
        final AtomicInteger gotNoException = new AtomicInteger();
        final AtomicReference<Throwable> otherException = new AtomicReference<Throwable>();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(final Integer key, Context<Integer, Integer> ctx, final Callback<Integer> callback) {
                        ctx.getLoaderExecutor().execute(new Runnable() {

                            @Override
                            public void run() {
                                loaderExecuted.incrementAndGet();
                                callback.onLoadSuccess(key);
                                try {
                                    callback.onLoadSuccess(key);
                                    gotNoException.incrementAndGet();
                                } catch (IllegalStateException ex) {
                                    gotException.incrementAndGet();
                                } catch (Throwable ex) {
                                    ex.printStackTrace();
                                    otherException.set(ex);
                                }
                            }
                        });
                        loaderCalled.incrementAndGet();
                    }
                });
            }
        });
        CompletionWaiter w = new CompletionWaiter();
        c.loadAll(Collections.EMPTY_LIST, w);
        w.awaitCompletion();
        w = new CompletionWaiter();
        c.loadAll(TestingBase.keys(1, 2, 1802), w);
        w.awaitCompletion();
        assertNull(otherException.get());
        assertEquals("loader called", 3, loaderCalled.get());
        assertEquals("loader Executed", 3, loaderExecuted.get());
        await("wait for 3 exceptions", new Condition() {

            @Override
            public boolean check() {
                return gotException.get() == 3;
            }
        });
        assertEquals("always throws exception", 0, gotNoException.get());
        w = new CompletionWaiter();
        c.loadAll(TestingBase.keys(1, 2, 1802), w);
        w.awaitCompletion();
        assertEquals(1, (int) c.peek(1));
        Object o1 = c.peek(1802);
        assertTrue(c.peek(1802) == o1);
        w = new CompletionWaiter();
        c.reloadAll(TestingBase.keys(1802, 4, 5), w);
        w.awaitCompletion();
        assertNotNull(c.peek(1802));
        assertTrue(c.peek(1802) != o1);
    }

    @Test
    public void testAsyncLoaderDoubleCallbackDifferentThreads() {
        final AtomicInteger loaderCalled = new AtomicInteger();
        final AtomicInteger loaderExecuted = new AtomicInteger();
        final AtomicInteger gotException = new AtomicInteger();
        final AtomicInteger gotNoException = new AtomicInteger();
        final AtomicReference<Throwable> otherException = new AtomicReference<Throwable>();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loaderExecutor(Executors.newCachedThreadPool());
                b.loader(new AsyncCacheLoader<Integer, Integer>() {

                    @Override
                    public void load(final Integer key, Context<Integer, Integer> ctx, final Callback<Integer> callback) {
                        Runnable command = new Runnable() {

                            @Override
                            public void run() {
                                loaderExecuted.incrementAndGet();
                                try {
                                    callback.onLoadSuccess(key);
                                    gotNoException.incrementAndGet();
                                } catch (IllegalStateException ex) {
                                    gotException.incrementAndGet();
                                } catch (Throwable ex) {
                                    ex.printStackTrace();
                                    otherException.set(ex);
                                }
                            }
                        };
                        ctx.getLoaderExecutor().execute(command);
                        ctx.getLoaderExecutor().execute(command);
                        loaderCalled.incrementAndGet();
                    }
                });
            }
        });
        CompletionWaiter w = new CompletionWaiter();
        c.loadAll(Collections.EMPTY_LIST, w);
        w.awaitCompletion();
        w = new CompletionWaiter();
        c.loadAll(TestingBase.keys(1, 2, 1802), w);
        w.awaitCompletion();
        if (otherException.get() != null) {
            otherException.get().printStackTrace();
            assertNull(otherException.get().toString(), otherException.get());
        }
        assertEquals("loader called", 3, loaderCalled.get());
        await("wait for 6 executions", new Condition() {

            @Override
            public boolean check() {
                return loaderExecuted.get() == 6;
            }
        });
        await("wait for 3 exceptions", new Condition() {

            @Override
            public boolean check() {
                return gotException.get() == 3;
            }
        });
        await("wait for 3 successful executions", new Condition() {

            @Override
            public boolean check() {
                return gotNoException.get() == 3;
            }
        });
        w = new CompletionWaiter();
        c.loadAll(TestingBase.keys(1, 2, 1802), w);
        w.awaitCompletion();
        assertEquals(1, (int) c.peek(1));
        Object o1 = c.peek(1802);
        assertTrue(c.peek(1802) == o1);
        w = new CompletionWaiter();
        c.reloadAll(TestingBase.keys(1802, 4, 5), w);
        w.awaitCompletion();
        assertNotNull(c.peek(1802));
        assertTrue(c.peek(1802) != o1);
    }

    protected Cache<Integer, Integer> cacheWithLoader() {
        return target.cache(new CacheRule.Specialization<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new CacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key) {
                        loaderExecutionCount++;
                        return key * 2;
                    }
                });
            }
        });
    }

    @Test
    public void advancedLoaderEntryNotSetIfExpired() {
        Cache<Integer, Integer> c = target.cache(new CacheRule.Context<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.loader(new AdvancedCacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key, @Nullable() long startTime, CacheEntry<Integer, Integer> currentEntry) {
                        assertNull(currentEntry);
                        return key;
                    }
                });
            }
        });
        c.get(123);
        c.expireAt(123, ExpiryTimeValues.NOW);
        c.get(123);
    }

    @Test
    public void advancedLoaderEntrySetIfExpiredWithKeepData() {
        final AtomicBoolean expectEntry = new AtomicBoolean();
        Cache<Integer, Integer> c = target.cache(new CacheRule.Context<Integer, Integer>() {

            @Override
            public void extend(Cache2kBuilder<Integer, Integer> b) {
                b.keepDataAfterExpired(true);
                b.loader(new AdvancedCacheLoader<Integer, Integer>() {

                    @Override
                    public Integer load(Integer key, @Nullable() long startTime, CacheEntry<Integer, Integer> currentEntry) {
                        if (expectEntry.get()) {
                            assertNotNull(currentEntry);
                        } else {
                            assertNull(currentEntry);
                        }
                        return key;
                    }
                });
            }
        });
        c.get(123);
        c.expireAt(123, ExpiryTimeValues.NOW);
        expectEntry.set(true);
        c.get(123);
    }

    public static class CompletionWaiter implements CacheOperationCompletionListener {

        CountDownLatch latch = new CountDownLatch(1);

        @Nullable
        volatile Throwable exception;

        @Override
        public void onCompleted() {
            latch.countDown();
        }

        @Override
        public void onException(Throwable exception) {
            this.exception = exception;
            latch.countDown();
        }

        public void awaitCompletion() {
            while (latch.getCount() > 0) {
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            if (exception != null) {
                throw new RuntimeException(exception);
            }
        }

        @Nullable()
        public Throwable getException() {
            return exception;
        }
    }
}
