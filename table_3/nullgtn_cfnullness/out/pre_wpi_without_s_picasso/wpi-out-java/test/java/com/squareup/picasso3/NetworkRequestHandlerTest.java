/*
 * Copyright (C) 2013 Square, Inc.
 *
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
 */
package com.squareup.picasso3;

import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.squareup.picasso3.RequestHandler.Result;
import com.squareup.picasso3.TestUtils.PremadeCall;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import static com.google.common.truth.Truth.assertThat;
import static com.squareup.picasso3.TestUtils.URI_1;
import static com.squareup.picasso3.TestUtils.URI_KEY_1;
import static com.squareup.picasso3.TestUtils.mockNetworkInfo;
import static java.util.concurrent.TimeUnit.SECONDS;
import static okhttp3.Protocol.HTTP_1_1;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class NetworkRequestHandlerTest {

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlockingDeque<Response> responses = new LinkedBlockingDeque<>();

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlockingDeque<okhttp3.Request> requests = new LinkedBlockingDeque<>();

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Picasso picasso;

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Stats stats;

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Dispatcher dispatcher;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull NetworkRequestHandler networkHandler;

    @org.checkerframework.dataflow.qual.Impure
    public void setUp() {
        initMocks(this);
        networkHandler = new NetworkRequestHandler(new Call.Factory() {

            @org.checkerframework.dataflow.qual.Impure
            public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Call newCall(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request) {
                requests.add(request);
                try {
                    return new PremadeCall(request, responses.takeFirst());
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }
            }
        }, stats);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void doesNotForceLocalCacheOnlyWithAirplaneModeOffAndRetryCount() throws Exception {
        responses.add(responseOf(ResponseBody.create(null, new byte[10])));
        Action action = TestUtils.mockAction(URI_KEY_1, URI_1);
        final CountDownLatch latch = new CountDownLatch(1);
        networkHandler.load(picasso, action.getRequest(), new RequestHandler.Callback() {

            @org.checkerframework.dataflow.qual.Impure
            public void onSuccess(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                try {
                    assertThat(requests.takeFirst().cacheControl().toString()).isEmpty();
                    latch.countDown();
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }
            }

            @org.checkerframework.dataflow.qual.SideEffectFree
            public void onError(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Throwable t) {
                throw new AssertionError(t);
            }
        });
        assertThat(latch.await(10, SECONDS)).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void withZeroRetryCountForcesLocalCacheOnly() throws Exception {
        responses.add(responseOf(ResponseBody.create(null, new byte[10])));
        Action action = TestUtils.mockAction(URI_KEY_1, URI_1);
        PlatformLruCache cache = new PlatformLruCache(0);
        BitmapHunter hunter = new BitmapHunter(picasso, dispatcher, cache, stats, action, networkHandler);
        hunter.retryCount = 0;
        hunter.hunt();
        assertThat(requests.takeFirst().cacheControl().toString()).isEqualTo(CacheControl.FORCE_CACHE.toString());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shouldRetryTwiceWithAirplaneModeOffAndNoNetworkInfo() {
        Action action = TestUtils.mockAction(URI_KEY_1, URI_1);
        PlatformLruCache cache = new PlatformLruCache(0);
        BitmapHunter hunter = new BitmapHunter(picasso, dispatcher, cache, stats, action, networkHandler);
        assertThat(hunter.shouldRetry(false, null)).isTrue();
        assertThat(hunter.shouldRetry(false, null)).isTrue();
        assertThat(hunter.shouldRetry(false, null)).isFalse();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shouldRetryWithUnknownNetworkInfo() {
        assertThat(networkHandler.shouldRetry(false, null)).isTrue();
        assertThat(networkHandler.shouldRetry(true, null)).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shouldRetryWithConnectedNetworkInfo() {
        NetworkInfo info = mockNetworkInfo();
        when(info.isConnected()).thenReturn(true);
        assertThat(networkHandler.shouldRetry(false, info)).isTrue();
        assertThat(networkHandler.shouldRetry(true, info)).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shouldNotRetryWithDisconnectedNetworkInfo() {
        NetworkInfo info = mockNetworkInfo();
        when(info.isConnectedOrConnecting()).thenReturn(false);
        assertThat(networkHandler.shouldRetry(false, info)).isFalse();
        assertThat(networkHandler.shouldRetry(true, info)).isFalse();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void noCacheAndKnownContentLengthDispatchToStats() throws Exception {
        responses.add(responseOf(ResponseBody.create(null, new byte[10])));
        Action action = TestUtils.mockAction(URI_KEY_1, URI_1);
        final CountDownLatch latch = new CountDownLatch(1);
        networkHandler.load(picasso, action.getRequest(), new RequestHandler.Callback() {

            @org.checkerframework.dataflow.qual.Impure
            public void onSuccess(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                verify(stats).dispatchDownloadFinished(10);
                latch.countDown();
            }

            @org.checkerframework.dataflow.qual.SideEffectFree
            public void onError(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Throwable t) {
                throw new AssertionError(t);
            }
        });
        assertThat(latch.await(10, SECONDS)).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void unknownContentLengthFromDiskThrows() throws Exception {
        final AtomicBoolean closed = new AtomicBoolean();
        ResponseBody body = new ResponseBody() {

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable MediaType contentType() {
                return null;
            }

            @org.checkerframework.dataflow.qual.Pure
            public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long contentLength() {
                return 0;
            }

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BufferedSource source() {
                return new Buffer();
            }

            @org.checkerframework.dataflow.qual.Impure
            public void close() {
                closed.set(true);
                super.close();
            }
        };
        responses.add(responseOf(body).newBuilder().cacheResponse(responseOf(null)).build());
        Action action = TestUtils.mockAction(URI_KEY_1, URI_1);
        final CountDownLatch latch = new CountDownLatch(1);
        networkHandler.load(picasso, action.getRequest(), new RequestHandler.Callback() {

            @org.checkerframework.dataflow.qual.SideEffectFree
            public void onSuccess(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                throw new AssertionError();
            }

            @org.checkerframework.dataflow.qual.Impure
            public void onError(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Throwable t) {
                verifyZeroInteractions(stats);
                assertTrue(closed.get());
                latch.countDown();
            }
        });
        assertThat(latch.await(10, SECONDS)).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cachedResponseDoesNotDispatchToStats() throws Exception {
        responses.add(responseOf(ResponseBody.create(null, new byte[10])).newBuilder().cacheResponse(responseOf(null)).build());
        Action action = TestUtils.mockAction(URI_KEY_1, URI_1);
        final CountDownLatch latch = new CountDownLatch(1);
        networkHandler.load(picasso, action.getRequest(), new RequestHandler.Callback() {

            @org.checkerframework.dataflow.qual.Impure
            public void onSuccess(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                verifyZeroInteractions(stats);
                latch.countDown();
            }

            @org.checkerframework.dataflow.qual.SideEffectFree
            public void onError(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Throwable t) {
                throw new AssertionError(t);
            }
        });
        assertThat(latch.await(10, SECONDS)).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    private static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response responseOf(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable ResponseBody body) {
        return new Response.Builder().code(200).protocol(HTTP_1_1).request(new okhttp3.Request.Builder().url("http://example.com").build()).message("OK").body(body).build();
    }
}
