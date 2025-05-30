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

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.squareup.picasso3.Picasso.RequestTransformer;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import static android.graphics.Bitmap.Config.ALPHA_8;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.google.common.truth.Truth.assertThat;
import static com.squareup.picasso3.Picasso.Listener;
import static com.squareup.picasso3.Picasso.LoadedFrom.MEMORY;
import static com.squareup.picasso3.RemoteViewsAction.RemoteViewsTarget;
import static com.squareup.picasso3.TestUtils.NO_HANDLERS;
import static com.squareup.picasso3.TestUtils.NO_TRANSFORMERS;
import static com.squareup.picasso3.TestUtils.UNUSED_CALL_FACTORY;
import static com.squareup.picasso3.TestUtils.URI_1;
import static com.squareup.picasso3.TestUtils.URI_KEY_1;
import static com.squareup.picasso3.TestUtils.makeBitmap;
import static com.squareup.picasso3.TestUtils.mockAction;
import static com.squareup.picasso3.TestUtils.mockCanceledAction;
import static com.squareup.picasso3.TestUtils.mockDeferredRequestCreator;
import static com.squareup.picasso3.TestUtils.mockHunter;
import static com.squareup.picasso3.TestUtils.mockImageViewTarget;
import static com.squareup.picasso3.TestUtils.mockPicasso;
import static com.squareup.picasso3.TestUtils.mockTarget;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public final class PicassoTest {

    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TemporaryFolder temporaryFolder = new TemporaryFolder();

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Context context;

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Dispatcher dispatcher;

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull RequestHandler requestHandler;

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache = new PlatformLruCache(2048);

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Listener listener;

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Stats stats;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Picasso picasso;

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap = makeBitmap();

    @org.checkerframework.dataflow.qual.Impure
    public void setUp() {
        initMocks(this);
        picasso = new Picasso(context, dispatcher, UNUSED_CALL_FACTORY, null, cache, listener, NO_TRANSFORMERS, NO_HANDLERS, stats, ARGB_8888, false, false);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void submitWithTargetInvokesDispatcher() {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        assertThat(picasso.targetToAction).isEmpty();
        picasso.enqueueAndSubmit(action);
        assertThat(picasso.targetToAction).hasSize(1);
        verify(dispatcher).dispatchSubmit(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void submitWithSameActionDoesNotCancel() {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        picasso.enqueueAndSubmit(action);
        verify(dispatcher).dispatchSubmit(action);
        assertThat(picasso.targetToAction).hasSize(1);
        assertThat(picasso.targetToAction.containsValue(action)).isTrue();
        picasso.enqueueAndSubmit(action);
        verify(action, never()).cancel();
        verify(dispatcher, never()).dispatchCancel(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void quickMemoryCheckReturnsBitmapIfInCache() {
        cache.set(URI_KEY_1, bitmap);
        Bitmap cached = picasso.quickMemoryCacheCheck(URI_KEY_1);
        assertThat(cached).isEqualTo(bitmap);
        verify(stats).dispatchCacheHit();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void quickMemoryCheckReturnsNullIfNotInCache() {
        Bitmap cached = picasso.quickMemoryCacheCheck(URI_KEY_1);
        assertThat(cached).isNull();
        verify(stats).dispatchCacheMiss();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void completeInvokesSuccessOnAllSuccessfulRequests() {
        Action action1 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        Action action2 = mockCanceledAction();
        BitmapHunter hunter = mockHunter(URI_KEY_1, new RequestHandler.Result(bitmap, MEMORY));
        when(hunter.getActions()).thenReturn(Arrays.asList(action1, action2));
        picasso.complete(hunter);
        verifyActionComplete(action1);
        verify(action2, never()).complete(any(RequestHandler.Result.class));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void completeInvokesErrorOnAllFailedRequests() {
        Action action1 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        Action action2 = mockCanceledAction();
        Exception exception = mock(Exception.class);
        BitmapHunter hunter = mockHunter(URI_KEY_1, null);
        when(hunter.getException()).thenReturn(exception);
        when(hunter.getActions()).thenReturn(Arrays.asList(action1, action2));
        picasso.complete(hunter);
        verify(action1).error(exception);
        verify(action2, never()).error(exception);
        verify(listener).onImageLoadFailed(picasso, URI_1, exception);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void completeDeliversToSingle() {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = mockHunter(URI_KEY_1, new RequestHandler.Result(bitmap, MEMORY));
        when(hunter.getAction()).thenReturn(action);
        when(hunter.getActions()).thenReturn(Collections.<Action>emptyList());
        picasso.complete(hunter);
        verifyActionComplete(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void completeWithReplayDoesNotRemove() {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        when(action.willReplay()).thenReturn(true);
        BitmapHunter hunter = mockHunter(URI_KEY_1, new RequestHandler.Result(bitmap, MEMORY));
        when(hunter.getAction()).thenReturn(action);
        picasso.enqueueAndSubmit(action);
        assertThat(picasso.targetToAction).hasSize(1);
        picasso.complete(hunter);
        assertThat(picasso.targetToAction).hasSize(1);
        verifyActionComplete(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void completeDeliversToSingleAndMultiple() {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        Action action2 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = mockHunter(URI_KEY_1, new RequestHandler.Result(bitmap, MEMORY));
        when(hunter.getAction()).thenReturn(action);
        when(hunter.getActions()).thenReturn(Arrays.asList(action2));
        picasso.complete(hunter);
        verifyActionComplete(action);
        verifyActionComplete(action2);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void completeSkipsIfNoActions() {
        BitmapHunter hunter = mockHunter(URI_KEY_1, new RequestHandler.Result(bitmap, MEMORY));
        picasso.complete(hunter);
        verify(hunter).getAction();
        verify(hunter).getActions();
        verifyNoMoreInteractions(hunter);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void resumeActionTriggersSubmitOnPausedAction() {
        Request request = new Request.Builder(URI_1, 0, ARGB_8888).build();
        Action<Object> action = new Action<Object>(mockPicasso(), new Target<>(null), request) {

            @org.checkerframework.dataflow.qual.Impure
            void complete(RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                fail("Test execution should not call this method");
            }

            @org.checkerframework.dataflow.qual.Impure
            void error(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Exception e) {
                fail("Test execution should not call this method");
            }
        };
        picasso.resumeAction(action);
        verify(dispatcher).dispatchSubmit(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void resumeActionImmediatelyCompletesCachedRequest() {
        cache.set(URI_KEY_1, bitmap);
        Request request = new Request.Builder(URI_1, 0, ARGB_8888).build();
        Action<Object> action = new Action<Object>(mockPicasso(), new Target<>(null), request) {

            @org.checkerframework.dataflow.qual.Impure
            void complete(RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                assertThat(result.getBitmap()).isEqualTo(bitmap);
                assertThat(result.getLoadedFrom()).isEqualTo(MEMORY);
            }

            @org.checkerframework.dataflow.qual.Impure
            void error(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Exception e) {
                fail("Reading from memory cache should not throw an exception");
            }
        };
        picasso.resumeAction(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithUnknownTarget() {
        ImageView target = mockImageViewTarget();
        Action action = mockAction(URI_KEY_1, URI_1, target);
        picasso.cancelRequest(target);
        verifyZeroInteractions(action, dispatcher);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithNullImageView() {
        try {
            picasso.cancelRequest((ImageView) null);
            fail("Canceling with a null ImageView should throw exception.");
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithNullTarget() {
        try {
            picasso.cancelRequest((BitmapTarget) null);
            fail("Canceling with a null target should throw exception.");
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithImageViewTarget() {
        ImageView target = mockImageViewTarget();
        Action action = mockAction(URI_KEY_1, URI_1, target);
        picasso.enqueueAndSubmit(action);
        assertThat(picasso.targetToAction).hasSize(1);
        picasso.cancelRequest(target);
        assertThat(picasso.targetToAction).isEmpty();
        verify(action).cancel();
        verify(dispatcher).dispatchCancel(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithDeferredImageViewTarget() {
        ImageView target = mockImageViewTarget();
        DeferredRequestCreator deferredRequestCreator = mockDeferredRequestCreator();
        picasso.targetToDeferredRequestCreator.put(target, deferredRequestCreator);
        picasso.cancelRequest(target);
        verify(deferredRequestCreator).cancel();
        assertThat(picasso.targetToDeferredRequestCreator).isEmpty();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void enqueueingDeferredRequestCancelsThePreviousOne() {
        ImageView target = mockImageViewTarget();
        DeferredRequestCreator firstRequestCreator = mockDeferredRequestCreator();
        picasso.defer(target, firstRequestCreator);
        assertThat(picasso.targetToDeferredRequestCreator).containsKey(target);
        DeferredRequestCreator secondRequestCreator = mockDeferredRequestCreator();
        picasso.defer(target, secondRequestCreator);
        verify(firstRequestCreator).cancel();
        assertThat(picasso.targetToDeferredRequestCreator).containsKey(target);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithTarget() {
        BitmapTarget target = mockTarget();
        Action action = mockAction(URI_KEY_1, URI_1, target);
        picasso.enqueueAndSubmit(action);
        assertThat(picasso.targetToAction).hasSize(1);
        picasso.cancelRequest(target);
        assertThat(picasso.targetToAction).isEmpty();
        verify(action).cancel();
        verify(dispatcher).dispatchCancel(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithNullRemoteViews() {
        try {
            picasso.cancelRequest(null, 0);
            fail("Canceling with a null RemoteViews should throw exception.");
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelExistingRequestWithRemoteViewTarget() {
        int layoutId = 0;
        int viewId = 1;
        RemoteViews remoteViews = new RemoteViews("packageName", layoutId);
        RemoteViewsTarget target = new RemoteViewsTarget(remoteViews, viewId);
        Action action = mockAction(URI_KEY_1, URI_1, target);
        picasso.enqueueAndSubmit(action);
        assertThat(picasso.targetToAction).hasSize(1);
        picasso.cancelRequest(remoteViews, viewId);
        assertThat(picasso.targetToAction).isEmpty();
        verify(action).cancel();
        verify(dispatcher).dispatchCancel(action);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelNullTagThrows() {
        try {
            picasso.cancelTag(null);
            fail("Canceling with a null tag should throw exception.");
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelTagAllActions() {
        ImageView target = mockImageViewTarget();
        Action action = mockAction(URI_KEY_1, URI_1, target, "TAG");
        picasso.enqueueAndSubmit(action);
        assertThat(picasso.targetToAction).hasSize(1);
        picasso.cancelTag("TAG");
        assertThat(picasso.targetToAction).isEmpty();
        verify(action).cancel();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelTagAllDeferredRequests() {
        ImageView target = mockImageViewTarget();
        DeferredRequestCreator deferredRequestCreator = mockDeferredRequestCreator();
        when(deferredRequestCreator.getTag()).thenReturn("TAG");
        picasso.defer(target, deferredRequestCreator);
        picasso.cancelTag("TAG");
        verify(deferredRequestCreator).cancel();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void deferAddsToMap() {
        ImageView target = mockImageViewTarget();
        DeferredRequestCreator deferredRequestCreator = mockDeferredRequestCreator();
        assertThat(picasso.targetToDeferredRequestCreator).isEmpty();
        picasso.defer(target, deferredRequestCreator);
        assertThat(picasso.targetToDeferredRequestCreator).hasSize(1);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shutdown() {
        cache.set("key", makeBitmap(1, 1));
        assertThat(cache.size()).isEqualTo(1);
        picasso.shutdown();
        assertThat(cache.size()).isEqualTo(0);
        verify(stats).shutdown();
        verify(dispatcher).shutdown();
        assertThat(picasso.shutdown).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shutdownClosesUnsharedCache() {
        okhttp3.Cache cache = new okhttp3.Cache(temporaryFolder.getRoot(), 100);
        Picasso picasso = new Picasso(context, dispatcher, UNUSED_CALL_FACTORY, cache, this.cache, listener, NO_TRANSFORMERS, NO_HANDLERS, stats, ARGB_8888, false, false);
        picasso.shutdown();
        assertThat(cache.isClosed()).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shutdownTwice() {
        cache.set("key", makeBitmap(1, 1));
        assertThat(cache.size()).isEqualTo(1);
        picasso.shutdown();
        picasso.shutdown();
        assertThat(cache.size()).isEqualTo(0);
        verify(stats).shutdown();
        verify(dispatcher).shutdown();
        assertThat(picasso.shutdown).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void shutdownClearsDeferredRequests() {
        DeferredRequestCreator deferredRequestCreator = mockDeferredRequestCreator();
        ImageView target = mockImageViewTarget();
        picasso.targetToDeferredRequestCreator.put(target, deferredRequestCreator);
        picasso.shutdown();
        verify(deferredRequestCreator).cancel();
        assertThat(picasso.targetToDeferredRequestCreator).isEmpty();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void throwWhenTransformRequestReturnsNull() {
        RequestTransformer brokenTransformer = new RequestTransformer() {

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Request transformRequest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request) {
                return null;
            }
        };
        Picasso picasso = new Picasso(context, dispatcher, UNUSED_CALL_FACTORY, null, cache, listener, Collections.singletonList(brokenTransformer), NO_HANDLERS, stats, ARGB_8888, false, false);
        Request request = new Request.Builder(URI_1).build();
        try {
            picasso.transformRequest(request);
            fail("Returning null from transformRequest() should throw");
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessageThat().isEqualTo("Request transformer " + brokenTransformer.getClass().getCanonicalName() + " returned null for " + request);
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getSnapshotInvokesStats() {
        picasso.getSnapshot();
        verify(stats).createSnapshot();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void enableIndicators() {
        assertThat(picasso.getIndicatorsEnabled()).isFalse();
        picasso.setIndicatorsEnabled(true);
        assertThat(picasso.getIndicatorsEnabled()).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void loadThrowsWithInvalidInput() {
        try {
            picasso.load("");
            fail("Empty URL should throw exception.");
        } catch (IllegalArgumentException expected) {
        }
        try {
            picasso.load("      ");
            fail("Empty URL should throw exception.");
        } catch (IllegalArgumentException expected) {
        }
        try {
            picasso.load(0);
            fail("Zero resourceId should throw exception.");
        } catch (IllegalArgumentException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderInvalidListener() {
        try {
            new Picasso.Builder(context).listener(null);
            fail("Null listener should throw exception.");
        } catch (NullPointerException expected) {
        }
        try {
            new Picasso.Builder(context).listener(listener).listener(listener);
            fail("Setting Listener twice should throw exception.");
        } catch (IllegalStateException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderInvalidClient() {
        try {
            new Picasso.Builder(context).client(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessageThat().isEqualTo("client == null");
        }
        try {
            new Picasso.Builder(context).callFactory(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessageThat().isEqualTo("factory == null");
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderInvalidExecutor() {
        try {
            new Picasso.Builder(context).executor(null);
            fail("Null Executor should throw exception.");
        } catch (NullPointerException expected) {
        }
        try {
            ExecutorService executor = mock(ExecutorService.class);
            new Picasso.Builder(context).executor(executor).executor(executor);
            fail("Setting Executor twice should throw exception.");
        } catch (IllegalStateException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderInvalidCache() {
        try {
            new Picasso.Builder(context).withCacheSize(-1);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessageThat().isEqualTo("maxByteCount < 0: -1");
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderNullRequestTransformer() {
        try {
            new Picasso.Builder(context).addRequestTransformer(null);
            fail("Null request transformer should throw exception.");
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderDuplicateRequestTransformer() {
        RequestTransformer identity = new RequestTransformer() {

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request transformRequest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request) {
                return request;
            }
        };
        try {
            new Picasso.Builder(context).addRequestTransformer(identity).addRequestTransformer(identity);
            fail("Setting request transformer twice should throw exception.");
        } catch (IllegalStateException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderNullRequestHandler() {
        try {
            new Picasso.Builder(context).addRequestHandler(null);
            fail("Null request handler should throw exception.");
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void buildDuplicateRequestHandler() {
        try {
            new Picasso.Builder(context).addRequestHandler(requestHandler).addRequestHandler(requestHandler);
            fail("Registering same request handler twice should throw exception.");
        } catch (IllegalStateException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderWithoutRequestHandler() {
        Picasso picasso = new Picasso.Builder(RuntimeEnvironment.application).build();
        assertThat(picasso.getRequestHandlers()).isNotEmpty();
        assertThat(picasso.getRequestHandlers()).doesNotContain(requestHandler);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderWithRequestHandler() {
        Picasso picasso = new Picasso.Builder(RuntimeEnvironment.application).addRequestHandler(requestHandler).build();
        assertThat(picasso.getRequestHandlers()).isNotNull();
        assertThat(picasso.getRequestHandlers()).isNotEmpty();
        assertThat(picasso.getRequestHandlers()).contains(requestHandler);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderInvalidContext() {
        try {
            new Picasso.Builder(null);
            fail("Null context should throw exception.");
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void builderWithDebugIndicators() {
        Picasso picasso = new Picasso.Builder(RuntimeEnvironment.application).indicatorsEnabled(true).build();
        assertThat(picasso.getIndicatorsEnabled()).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void evictAll() {
        Picasso picasso = new Picasso.Builder(RuntimeEnvironment.application).indicatorsEnabled(true).build();
        picasso.cache.set("key", Bitmap.createBitmap(1, 1, ALPHA_8));
        assertThat(picasso.cache.size()).isEqualTo(1);
        picasso.evictAll();
        assertThat(picasso.cache.size()).isEqualTo(0);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void invalidateString() {
        Request request = new Request.Builder(Uri.parse("https://example.com")).build();
        cache.set(request.key, makeBitmap(1, 1));
        assertThat(cache.size()).isEqualTo(1);
        picasso.invalidate("https://example.com");
        assertThat(cache.size()).isEqualTo(0);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void invalidateFile() {
        Request request = new Request.Builder(Uri.fromFile(new File("/foo/bar/baz"))).build();
        cache.set(request.key, makeBitmap(1, 1));
        assertThat(cache.size()).isEqualTo(1);
        picasso.invalidate(new File("/foo/bar/baz"));
        assertThat(cache.size()).isEqualTo(0);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void invalidateUri() {
        Request request = new Request.Builder(URI_1).build();
        cache.set(request.key, makeBitmap(1, 1));
        assertThat(cache.size()).isEqualTo(1);
        picasso.invalidate(URI_1);
        assertThat(cache.size()).isEqualTo(0);
    }

    @org.checkerframework.dataflow.qual.Impure
    private void verifyActionComplete(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action action) {
        ArgumentCaptor<RequestHandler.Result> captor = ArgumentCaptor.forClass(RequestHandler.Result.class);
        verify(action).complete(captor.capture());
        RequestHandler.Result result = captor.getValue();
        assertThat(result.getBitmap()).isEqualTo(bitmap);
        assertThat(result.getLoadedFrom()).isEqualTo(MEMORY);
    }
}
