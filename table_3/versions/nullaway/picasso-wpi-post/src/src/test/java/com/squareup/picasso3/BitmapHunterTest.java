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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.FutureTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowBitmap;
import org.robolectric.shadows.ShadowMatrix;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.support.media.ExifInterface.ORIENTATION_FLIP_HORIZONTAL;
import static android.support.media.ExifInterface.ORIENTATION_FLIP_VERTICAL;
import static android.support.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.support.media.ExifInterface.ORIENTATION_TRANSPOSE;
import static android.support.media.ExifInterface.ORIENTATION_TRANSVERSE;
import static com.google.common.truth.Truth.assertThat;
import static com.squareup.picasso3.BitmapHunter.forRequest;
import static com.squareup.picasso3.MatrixTransformation.transformResult;
import static com.squareup.picasso3.Picasso.LoadedFrom.MEMORY;
import static com.squareup.picasso3.Picasso.Priority.HIGH;
import static com.squareup.picasso3.Picasso.Priority.LOW;
import static com.squareup.picasso3.Picasso.Priority.NORMAL;
import static com.squareup.picasso3.TestUtils.ASSET_KEY_1;
import static com.squareup.picasso3.TestUtils.ASSET_URI_1;
import static com.squareup.picasso3.TestUtils.BITMAP_RESOURCE_VALUE;
import static com.squareup.picasso3.TestUtils.CONTACT_KEY_1;
import static com.squareup.picasso3.TestUtils.CONTACT_PHOTO_KEY_1;
import static com.squareup.picasso3.TestUtils.CONTACT_PHOTO_URI_1;
import static com.squareup.picasso3.TestUtils.CONTACT_URI_1;
import static com.squareup.picasso3.TestUtils.CONTENT_1_URL;
import static com.squareup.picasso3.TestUtils.CONTENT_KEY_1;
import static com.squareup.picasso3.TestUtils.CUSTOM_URI;
import static com.squareup.picasso3.TestUtils.CUSTOM_URI_KEY;
import static com.squareup.picasso3.TestUtils.FILE_1_URL;
import static com.squareup.picasso3.TestUtils.FILE_KEY_1;
import static com.squareup.picasso3.TestUtils.MEDIA_STORE_CONTENT_1_URL;
import static com.squareup.picasso3.TestUtils.MEDIA_STORE_CONTENT_KEY_1;
import static com.squareup.picasso3.TestUtils.NO_TRANSFORMERS;
import static com.squareup.picasso3.TestUtils.RESOURCE_ID_1;
import static com.squareup.picasso3.TestUtils.RESOURCE_ID_KEY_1;
import static com.squareup.picasso3.TestUtils.RESOURCE_ID_URI;
import static com.squareup.picasso3.TestUtils.RESOURCE_ID_URI_KEY;
import static com.squareup.picasso3.TestUtils.RESOURCE_TYPE_URI;
import static com.squareup.picasso3.TestUtils.RESOURCE_TYPE_URI_KEY;
import static com.squareup.picasso3.TestUtils.UNUSED_CALL_FACTORY;
import static com.squareup.picasso3.TestUtils.URI_1;
import static com.squareup.picasso3.TestUtils.URI_KEY_1;
import static com.squareup.picasso3.TestUtils.XML_RESOURCE_VALUE;
import static com.squareup.picasso3.TestUtils.makeBitmap;
import static com.squareup.picasso3.TestUtils.makeLoaderWithDrawable;
import static com.squareup.picasso3.TestUtils.mockAction;
import static com.squareup.picasso3.TestUtils.mockImageViewTarget;
import static com.squareup.picasso3.TestUtils.mockPicasso;
import static com.squareup.picasso3.TestUtils.mockResources;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Shadows.shadowOf;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public final class BitmapHunterTest {

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Context context;

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Picasso picasso;

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache = new PlatformLruCache(2048);

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Stats stats = new Stats(cache);

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Dispatcher dispatcher;

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap = makeBitmap();

    @org.checkerframework.dataflow.qual.Impure
    public void setUp() {
        initMocks(this);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void nullDecodeResponseIsError() {
        Action action = mockAction(URI_KEY_1, URI_1);
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action, null);
        hunter.run();
        verify(dispatcher).dispatchFailed(hunter);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void runWithResultDispatchComplete() {
        Action action = mockAction(URI_KEY_1, URI_1);
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action, bitmap);
        hunter.run();
        verify(dispatcher).dispatchComplete(hunter);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void runWithNoResultDispatchFailed() {
        Action action = mockAction(URI_KEY_1, URI_1);
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action);
        hunter.run();
        verify(dispatcher).dispatchFailed(hunter);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void responseExceptionDispatchFailed() {
        Action action = mockAction(URI_KEY_1, URI_1);
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action, null, new NetworkRequestHandler.ResponseException(504, 0));
        hunter.run();
        verify(dispatcher).dispatchFailed(hunter);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void outOfMemoryDispatchFailed() {
        Action action = mockAction(URI_KEY_1, URI_1);
        BitmapHunter hunter = new OOMBitmapHunter(picasso, dispatcher, cache, stats, action);
        hunter.run();
        Exception exception = hunter.getException();
        verify(dispatcher).dispatchFailed(hunter);
        assertThat(hunter.getResult()).isNull();
        assertThat(exception).hasMessageThat().contains("BEGIN PICASSO STATS");
        assertThat(exception.getCause()).isInstanceOf(OutOfMemoryError.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void runWithIoExceptionDispatchRetry() {
        Action action = mockAction(URI_KEY_1, URI_1);
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action, null, new IOException());
        hunter.run();
        verify(dispatcher).dispatchRetry(hunter);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void huntDecodesWhenNotInCache() throws Exception {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        TestableBitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action, bitmap);
        RequestHandler.Result result = hunter.hunt();
        assertThat(cache.missCount()).isEqualTo(1);
        Request request = action.getRequest();
        verify(hunter.requestHandler).load(eq(picasso), eq(request), any(RequestHandler.Callback.class));
        assertThat(result.getBitmap()).isEqualTo(bitmap);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void huntReturnsWhenResultInCache() throws Exception {
        cache.set(URI_KEY_1, bitmap);
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        TestableBitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action, bitmap);
        RequestHandler.Result result = hunter.hunt();
        assertThat(cache.hitCount()).isEqualTo(1);
        Request request = action.getRequest();
        verify(hunter.requestHandler, never()).load(eq(picasso), eq(request), any(RequestHandler.Callback.class));
        assertThat(result.getBitmap()).isEqualTo(bitmap);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void huntUnrecognizedUri() throws Exception {
        Action action = mockAction(CUSTOM_URI_KEY, CUSTOM_URI);
        BitmapHunter hunter = forRequest(picasso, dispatcher, cache, stats, action);
        try {
            hunter.hunt();
            fail("Unrecognized URI should throw exception.");
        } catch (IllegalStateException ignored) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void huntDecodesWithRequestHandler() throws Exception {
        Action action = mockAction(CUSTOM_URI_KEY, CUSTOM_URI);
        BitmapHunter hunter = forRequest(mockPicasso(new CustomRequestHandler()), dispatcher, cache, stats, action);
        RequestHandler.Result result = hunter.hunt();
        assertThat(result.getBitmap()).isEqualTo(bitmap);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void attachSingleRequest() {
        Action action1 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action1);
        assertThat(hunter.action).isEqualTo(action1);
        hunter.detach(action1);
        hunter.attach(action1);
        assertThat(hunter.action).isEqualTo(action1);
        assertThat(hunter.actions).isNull();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void attachMultipleRequests() {
        Action action1 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        Action action2 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action1);
        assertThat(hunter.actions).isNull();
        hunter.attach(action2);
        assertThat(hunter.actions).isNotNull();
        assertThat(hunter.actions).hasSize(1);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void detachSingleRequest() {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action);
        assertThat(hunter.action).isNotNull();
        hunter.detach(action);
        assertThat(hunter.action).isNull();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void detachMultipleRequests() {
        Action action = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        Action action2 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action);
        hunter.attach(action2);
        hunter.detach(action2);
        assertThat(hunter.action).isNotNull();
        assertThat(hunter.actions).isNotNull();
        assertThat(hunter.actions).isEmpty();
        hunter.detach(action);
        assertThat(hunter.action).isNull();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelSingleRequest() {
        Action action1 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action1);
        hunter.future = new FutureTask<>(mock(Runnable.class), mock(Object.class));
        assertThat(hunter.isCancelled()).isFalse();
        assertThat(hunter.cancel()).isFalse();
        hunter.detach(action1);
        assertThat(hunter.cancel()).isTrue();
        assertThat(hunter.isCancelled()).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cancelMultipleRequests() {
        Action action1 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        Action action2 = mockAction(URI_KEY_1, URI_1, mockImageViewTarget());
        BitmapHunter hunter = new TestableBitmapHunter(picasso, dispatcher, cache, stats, action1);
        hunter.future = new FutureTask<>(mock(Runnable.class), mock(Object.class));
        hunter.attach(action2);
        assertThat(hunter.isCancelled()).isFalse();
        assertThat(hunter.cancel()).isFalse();
        hunter.detach(action1);
        hunter.detach(action2);
        assertThat(hunter.cancel()).isTrue();
        assertThat(hunter.isCancelled()).isTrue();
    }

    // ---------------------------------------
    @org.checkerframework.dataflow.qual.Impure
    public void forContentProviderRequest() {
        Action action = mockAction(CONTENT_KEY_1, CONTENT_1_URL);
        BitmapHunter hunter = forRequest(mockPicasso(new ContentStreamRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(ContentStreamRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forMediaStoreRequest() {
        Action action = mockAction(MEDIA_STORE_CONTENT_KEY_1, MEDIA_STORE_CONTENT_1_URL);
        BitmapHunter hunter = forRequest(mockPicasso(new MediaStoreRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(MediaStoreRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forContactsPhotoRequest() {
        Action action = mockAction(CONTACT_KEY_1, CONTACT_URI_1);
        BitmapHunter hunter = forRequest(mockPicasso(new ContactsPhotoRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(ContactsPhotoRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forContactsThumbnailPhotoRequest() {
        Action action = mockAction(CONTACT_PHOTO_KEY_1, CONTACT_PHOTO_URI_1);
        BitmapHunter hunter = forRequest(mockPicasso(new ContactsPhotoRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(ContactsPhotoRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forNetworkRequest() {
        Action action = mockAction(URI_KEY_1, URI_1);
        NetworkRequestHandler requestHandler = new NetworkRequestHandler(UNUSED_CALL_FACTORY, stats);
        BitmapHunter hunter = forRequest(mockPicasso(requestHandler), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isSameAs(requestHandler);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forFileWithAuthorityRequest() {
        Action action = mockAction(FILE_KEY_1, FILE_1_URL);
        BitmapHunter hunter = forRequest(mockPicasso(new FileRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(FileRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forAndroidBitmapResourceRequest() {
        Resources resources = mockResources(BITMAP_RESOURCE_VALUE);
        when(context.getResources()).thenReturn(resources);
        Action action = mockAction(RESOURCE_ID_KEY_1, null, null, RESOURCE_ID_1);
        BitmapHunter hunter = forRequest(mockPicasso(new ResourceRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(ResourceRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forAndroidBitmapResourceUriWithId() {
        Action action = mockAction(RESOURCE_ID_URI_KEY, RESOURCE_ID_URI);
        BitmapHunter hunter = forRequest(mockPicasso(new ResourceRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(ResourceRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forAndroidBitmapResourceUriWithType() {
        Action action = mockAction(RESOURCE_TYPE_URI_KEY, RESOURCE_TYPE_URI);
        BitmapHunter hunter = forRequest(mockPicasso(new ResourceRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(ResourceRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forAndroidXmlResourceRequest() {
        Resources resources = mockResources(XML_RESOURCE_VALUE);
        when(context.getResources()).thenReturn(resources);
        Action action = mockAction(RESOURCE_ID_KEY_1, null, null, RESOURCE_ID_1);
        ResourceDrawableRequestHandler requestHandler = ResourceDrawableRequestHandler.create(context, makeLoaderWithDrawable(null));
        BitmapHunter hunter = forRequest(mockPicasso(requestHandler), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(ResourceDrawableRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forAssetRequest() {
        Action action = mockAction(ASSET_KEY_1, ASSET_URI_1);
        BitmapHunter hunter = forRequest(mockPicasso(new AssetRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(AssetRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forFileWithNoPathSegments() {
        Action action = mockAction("keykeykey", Uri.fromFile(new File("/")));
        BitmapHunter hunter = forRequest(mockPicasso(new FileRequestHandler(context)), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(FileRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forCustomRequest() {
        Action action = mockAction(CUSTOM_URI_KEY, CUSTOM_URI);
        BitmapHunter hunter = forRequest(mockPicasso(new CustomRequestHandler()), dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isInstanceOf(CustomRequestHandler.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void forOverrideRequest() {
        Action action = mockAction(ASSET_KEY_1, ASSET_URI_1);
        RequestHandler handler = new AssetRequestHandler(context);
        List<RequestHandler> handlers = Collections.singletonList(handler);
        // Must use non-mock constructor because that is where Picasso's list of handlers is created.
        Picasso picasso = new Picasso(context, dispatcher, UNUSED_CALL_FACTORY, null, cache, null, NO_TRANSFORMERS, handlers, stats, ARGB_8888, false, false);
        BitmapHunter hunter = forRequest(picasso, dispatcher, cache, stats, action);
        assertThat(hunter.requestHandler).isEqualTo(handler);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void sequenceIsIncremented() {
        Action action = mockAction(URI_KEY_1, URI_1);
        Picasso picasso = mockPicasso();
        BitmapHunter hunter1 = forRequest(picasso, dispatcher, cache, stats, action);
        BitmapHunter hunter2 = forRequest(picasso, dispatcher, cache, stats, action);
        assertThat(hunter2.sequence).isGreaterThan(hunter1.sequence);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getPriorityWithNoRequests() {
        Action action = mockAction(URI_KEY_1, URI_1);
        NetworkRequestHandler requestHandler = new NetworkRequestHandler(UNUSED_CALL_FACTORY, stats);
        BitmapHunter hunter = forRequest(mockPicasso(requestHandler), dispatcher, cache, stats, action);
        hunter.detach(action);
        assertThat(hunter.getAction()).isNull();
        assertThat(hunter.getActions()).isNull();
        assertThat(hunter.getPriority()).isEqualTo(LOW);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getPriorityWithSingleRequest() {
        Action action = mockAction(URI_KEY_1, URI_1, HIGH);
        NetworkRequestHandler requestHandler = new NetworkRequestHandler(UNUSED_CALL_FACTORY, stats);
        BitmapHunter hunter = forRequest(mockPicasso(requestHandler), dispatcher, cache, stats, action);
        assertThat(hunter.getAction()).isEqualTo(action);
        assertThat(hunter.getActions()).isNull();
        assertThat(hunter.getPriority()).isEqualTo(HIGH);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getPriorityWithMultipleRequests() {
        Action action1 = mockAction(URI_KEY_1, URI_1, NORMAL);
        Action action2 = mockAction(URI_KEY_1, URI_1, HIGH);
        NetworkRequestHandler requestHandler = new NetworkRequestHandler(UNUSED_CALL_FACTORY, stats);
        BitmapHunter hunter = forRequest(mockPicasso(requestHandler), dispatcher, cache, stats, action1);
        hunter.attach(action2);
        assertThat(hunter.getAction()).isEqualTo(action1);
        assertThat(hunter.getActions()).containsExactly(action2);
        assertThat(hunter.getPriority()).isEqualTo(HIGH);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getPriorityAfterDetach() {
        Action action1 = mockAction(URI_KEY_1, URI_1, NORMAL);
        Action action2 = mockAction(URI_KEY_1, URI_1, HIGH);
        NetworkRequestHandler requestHandler = new NetworkRequestHandler(UNUSED_CALL_FACTORY, stats);
        BitmapHunter hunter = forRequest(mockPicasso(requestHandler), dispatcher, cache, stats, action1);
        hunter.attach(action2);
        assertThat(hunter.getAction()).isEqualTo(action1);
        assertThat(hunter.getActions()).containsExactly(action2);
        assertThat(hunter.getPriority()).isEqualTo(HIGH);
        hunter.detach(action2);
        assertThat(hunter.getAction()).isEqualTo(action1);
        assertThat(hunter.getActions()).isEmpty();
        assertThat(hunter.getPriority()).isEqualTo(NORMAL);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifRotation() {
        Request data = new Request.Builder(URI_1).rotate(-45).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, ORIENTATION_ROTATE_90);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("rotate 90.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifRotationSizing() {
        Request data = new Request.Builder(URI_1).resize(5, 10).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, ORIENTATION_ROTATE_90);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).contains("scale 1.0 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifRotationNoSizing() {
        Request data = new Request.Builder(URI_1).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, ORIENTATION_ROTATE_90);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).contains("rotate 90.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void rotation90Sizing() {
        Request data = new Request.Builder(URI_1).rotate(90).resize(5, 10).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).contains("scale 1.0 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void rotation180Sizing() {
        Request data = new Request.Builder(URI_1).rotate(180).resize(5, 10).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).contains("scale 0.5 1.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void rotation90WithPivotSizing() {
        Request data = new Request.Builder(URI_1).rotate(90, 0, 10).resize(5, 10).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).contains("scale 1.0 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifVerticalFlip() {
        Request data = new Request.Builder(URI_1).rotate(-45).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, ORIENTATION_FLIP_VERTICAL);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPostOperations()).containsExactly("scale -1.0 1.0");
        assertThat(shadowMatrix.getPreOperations()).containsExactly("rotate 180.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifHorizontalFlip() {
        Request data = new Request.Builder(URI_1).rotate(-45).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, ORIENTATION_FLIP_HORIZONTAL);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPostOperations()).containsExactly("scale -1.0 1.0");
        assertThat(shadowMatrix.getPreOperations()).doesNotContain("rotate 180.0");
        assertThat(shadowMatrix.getPreOperations()).doesNotContain("rotate 90.0");
        assertThat(shadowMatrix.getPreOperations()).doesNotContain("rotate 270.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifTranspose() {
        Request data = new Request.Builder(URI_1).rotate(-45).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, ORIENTATION_TRANSPOSE);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPostOperations()).containsExactly("scale -1.0 1.0");
        assertThat(shadowMatrix.getPreOperations()).containsExactly("rotate 90.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifTransverse() {
        Request data = new Request.Builder(URI_1).rotate(-45).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, ORIENTATION_TRANSVERSE);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPostOperations()).containsExactly("scale -1.0 1.0");
        assertThat(shadowMatrix.getPreOperations()).containsExactly("rotate 270.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void keepsAspectRationWhileResizingWhenDesiredWidthIs0() {
        Request request = new Request.Builder(URI_1).resize(20, 0).build();
        Bitmap source = Bitmap.createBitmap(40, 20, ARGB_8888);
        Bitmap result = transformResult(request, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.5 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void keepsAspectRationWhileResizingWhenDesiredHeightIs0() {
        Request request = new Request.Builder(URI_1).resize(0, 10).build();
        Bitmap source = Bitmap.createBitmap(40, 20, ARGB_8888);
        Bitmap result = transformResult(request, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.5 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropResultMatchesTargetSize() {
        Request request = new Request.Builder(URI_1).resize(1080, 642).centerCrop().build();
        Bitmap source = Bitmap.createBitmap(640, 640, ARGB_8888);
        Bitmap result = transformResult(request, source, 0);
        int transformedWidth = Math.round(result.getWidth());
        int transformedHeight = Math.round(result.getHeight());
        assertThat(transformedWidth).isEqualTo(1080);
        assertThat(transformedHeight).isEqualTo(642);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropResultMatchesTargetSizeWhileDesiredWidthIs0() {
        Request request = new Request.Builder(URI_1).resize(0, 642).centerCrop().build();
        Bitmap source = Bitmap.createBitmap(640, 640, ARGB_8888);
        Bitmap result = transformResult(request, source, 0);
        int transformedWidth = Math.round(result.getWidth());
        int transformedHeight = Math.round(result.getHeight());
        assertThat(transformedWidth).isEqualTo(642);
        assertThat(transformedHeight).isEqualTo(642);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropResultMatchesTargetSizeWhileDesiredHeightIs0() {
        Request request = new Request.Builder(URI_1).resize(1080, 0).centerCrop().build();
        Bitmap source = Bitmap.createBitmap(640, 640, ARGB_8888);
        Bitmap result = transformResult(request, source, 0);
        int transformedWidth = Math.round(result.getWidth());
        int transformedHeight = Math.round(result.getHeight());
        assertThat(transformedWidth).isEqualTo(1080);
        assertThat(transformedHeight).isEqualTo(1080);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerInsideResultMatchesTargetSizeWhileDesiredWidthIs0() {
        Request request = new Request.Builder(URI_1).resize(0, 642).centerInside().build();
        Bitmap source = Bitmap.createBitmap(640, 640, ARGB_8888);
        Bitmap result = transformResult(request, source, 0);
        int transformedWidth = Math.round(result.getWidth());
        int transformedHeight = Math.round(result.getHeight());
        assertThat(transformedWidth).isEqualTo(642);
        assertThat(transformedHeight).isEqualTo(642);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerInsideResultMatchesTargetSizeWhileDesiredHeightIs0() {
        Request request = new Request.Builder(URI_1).resize(1080, 0).centerInside().build();
        Bitmap source = Bitmap.createBitmap(640, 640, ARGB_8888);
        Bitmap result = transformResult(request, source, 0);
        int transformedWidth = Math.round(result.getWidth());
        int transformedHeight = Math.round(result.getHeight());
        assertThat(transformedWidth).isEqualTo(1080);
        assertThat(transformedHeight).isEqualTo(1080);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void exifRotationWithManualRotation() {
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).rotate(-45).build();
        Bitmap result = transformResult(data, source, ORIENTATION_ROTATE_90);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("rotate 90.0");
        assertThat(shadowMatrix.getSetOperations()).containsEntry("rotate", "-45.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void rotation() {
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).rotate(-45).build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getSetOperations()).containsEntry("rotate", "-45.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void pivotRotation() {
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).rotate(-45, 10, 10).build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getSetOperations()).containsEntry("rotate", "-45.0 10.0 10.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void resize() {
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(20, 15).build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 2.0 1.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropTallTooSmall() {
        Bitmap source = Bitmap.createBitmap(10, 20, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(40, 40).centerCrop().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(5);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(10);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 4.0 4.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropTallTooLarge() {
        Bitmap source = Bitmap.createBitmap(100, 200, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(50, 50).centerCrop().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(50);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(100);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(100);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.5 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropWideTooSmall() {
        Bitmap source = Bitmap.createBitmap(20, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(40, 40).centerCrop().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(5);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(10);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 4.0 4.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropWithGravityHorizontalLeft() {
        Bitmap source = Bitmap.createBitmap(20, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(40, 40).centerCrop(Gravity.LEFT).build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(10);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 4.0 4.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropWithGravityHorizontalRight() {
        Bitmap source = Bitmap.createBitmap(20, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(40, 40).centerCrop(Gravity.RIGHT).build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(10);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 4.0 4.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropWithGravityVerticalTop() {
        Bitmap source = Bitmap.createBitmap(10, 20, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(40, 40).centerCrop(Gravity.TOP).build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(10);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 4.0 4.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropWithGravityVerticalBottom() {
        Bitmap source = Bitmap.createBitmap(10, 20, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(40, 40).centerCrop(Gravity.BOTTOM).build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(10);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(10);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 4.0 4.0");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerCropWideTooLarge() {
        Bitmap source = Bitmap.createBitmap(200, 100, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(50, 50).centerCrop().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        assertThat(shadowBitmap.getCreatedFromX()).isEqualTo(50);
        assertThat(shadowBitmap.getCreatedFromY()).isEqualTo(0);
        assertThat(shadowBitmap.getCreatedFromWidth()).isEqualTo(100);
        assertThat(shadowBitmap.getCreatedFromHeight()).isEqualTo(100);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.5 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerInsideTallTooSmall() {
        Bitmap source = Bitmap.createBitmap(20, 10, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(50, 50).centerInside().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 2.5 2.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerInsideTallTooLarge() {
        Bitmap source = Bitmap.createBitmap(100, 50, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(50, 50).centerInside().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.5 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerInsideWideTooSmall() {
        Bitmap source = Bitmap.createBitmap(10, 20, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(50, 50).centerInside().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 2.5 2.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void centerInsideWideTooLarge() {
        Bitmap source = Bitmap.createBitmap(50, 100, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(50, 50).centerInside().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.5 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onlyScaleDownOriginalBigger() {
        Bitmap source = Bitmap.createBitmap(100, 100, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(50, 50).onlyScaleDown().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.5 0.5");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onlyScaleDownOriginalSmaller() {
        Bitmap source = Bitmap.createBitmap(50, 50, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(100, 100).onlyScaleDown().build();
        Bitmap result = transformResult(data, source, 0);
        assertThat(result).isSameAs(source);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isNull();
        assertThat(shadowBitmap.getCreatedFromBitmap()).isNotSameAs(source);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onlyScaleDownOriginalSmallerWidthIs0() {
        Bitmap source = Bitmap.createBitmap(50, 50, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(0, 60).onlyScaleDown().build();
        Bitmap result = transformResult(data, source, 0);
        assertThat(result).isSameAs(source);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isNull();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onlyScaleDownOriginalSmallerHeightIs0() {
        Bitmap source = Bitmap.createBitmap(50, 50, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(60, 0).onlyScaleDown().build();
        Bitmap result = transformResult(data, source, 0);
        assertThat(result).isSameAs(source);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isNull();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onlyScaleDownOriginalBiggerWidthIs0() {
        Bitmap source = Bitmap.createBitmap(50, 50, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(0, 40).onlyScaleDown().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.8 0.8");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onlyScaleDownOriginalBiggerHeightIs0() {
        Bitmap source = Bitmap.createBitmap(50, 50, ARGB_8888);
        Request data = new Request.Builder(URI_1).resize(40, 0).onlyScaleDown().build();
        Bitmap result = transformResult(data, source, 0);
        ShadowBitmap shadowBitmap = shadowOf(result);
        assertThat(shadowBitmap.getCreatedFromBitmap()).isSameAs(source);
        Matrix matrix = shadowBitmap.getCreatedFromMatrix();
        ShadowMatrix shadowMatrix = shadowOf(matrix);
        assertThat(shadowMatrix.getPreOperations()).containsExactly("scale 0.8 0.8");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void reusedBitmapIsNotRecycled() {
        Request data = new Request.Builder(URI_1).build();
        Bitmap source = Bitmap.createBitmap(10, 10, ARGB_8888);
        Bitmap result = transformResult(data, source, 0);
        assertThat(result).isSameAs(source);
        assertThat(result.isRecycled()).isFalse();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void crashingOnTransformationThrows() {
        Transformation badTransformation = new Transformation() {

            @org.checkerframework.dataflow.qual.Pure
            public RequestHandler.Result transform(RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result source) {
                throw new NullPointerException("hello");
            }

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key() {
                return "test";
            }
        };
        List<Transformation> transformations = Collections.singletonList(badTransformation);
        Bitmap original = Bitmap.createBitmap(10, 10, ARGB_8888);
        RequestHandler.Result result = new RequestHandler.Result(original, MEMORY, 0);
        Request data = new Request.Builder(URI_1).build();
        try {
            BitmapHunter.applyTransformations(picasso, data, transformations, result);
            fail("Expected exception to be thrown.");
        } catch (RuntimeException e) {
            assertThat(e).hasMessageThat().isEqualTo("Transformation " + badTransformation.key() + " crashed with exception.");
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void nullResultFromTransformationThrows() {
        Transformation badTransformation = new Transformation() {

            @org.checkerframework.dataflow.qual.Pure
            public RequestHandler.@org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Result transform(RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result source) {
                return null;
            }

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key() {
                return "test";
            }
        };
        List<Transformation> transformations = Collections.singletonList(badTransformation);
        Bitmap original = Bitmap.createBitmap(10, 10, ARGB_8888);
        RequestHandler.Result result = new RequestHandler.Result(original, MEMORY, 0);
        Request data = new Request.Builder(URI_1).build();
        try {
            BitmapHunter.applyTransformations(picasso, data, transformations, result);
            fail("Expected exception to be thrown.");
        } catch (RuntimeException e) {
            assertThat(e).hasMessageThat().contains("Transformation " + badTransformation.key() + " returned null");
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void doesNotRecycleOriginalTransformationThrows() {
        Transformation badTransformation = new Transformation() {

            @org.checkerframework.dataflow.qual.Impure
            public RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result transform(RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result source) {
                // Should recycle source.
                return new RequestHandler.Result(Bitmap.createBitmap(10, 10, ARGB_8888), MEMORY, 0);
            }

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key() {
                return "test";
            }
        };
        List<Transformation> transformations = Collections.singletonList(badTransformation);
        Bitmap original = Bitmap.createBitmap(10, 10, ARGB_8888);
        RequestHandler.Result result = new RequestHandler.Result(original, MEMORY, 0);
        Request data = new Request.Builder(URI_1).build();
        try {
            BitmapHunter.applyTransformations(picasso, data, transformations, result);
            fail("Expected exception to be thrown.");
        } catch (RuntimeException e) {
            assertThat(e).hasMessageThat().isEqualTo("Transformation " + badTransformation.key() + " mutated input Bitmap but failed to recycle the original.");
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void recycledOriginalTransformationThrows() {
        Transformation badTransformation = new Transformation() {

            @org.checkerframework.dataflow.qual.Impure
            public RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result transform(RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result source) {
                source.getBitmap().recycle();
                return source;
            }

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key() {
                return "test";
            }
        };
        List<Transformation> transformations = Collections.singletonList(badTransformation);
        Bitmap original = Bitmap.createBitmap(10, 10, ARGB_8888);
        RequestHandler.Result result = new RequestHandler.Result(original, MEMORY, 0);
        Request data = new Request.Builder(URI_1).build();
        try {
            BitmapHunter.applyTransformations(picasso, data, transformations, result);
            fail("Expected exception to be thrown.");
        } catch (RuntimeException e) {
            assertThat(e).hasMessageThat().isEqualTo("Transformation " + badTransformation.key() + " returned input Bitmap but recycled it.");
        }
    }

    private static class TestableBitmapHunter extends BitmapHunter {

        @org.checkerframework.dataflow.qual.Impure
        TestableBitmapHunter(@org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Dispatcher dispatcher, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Stats stats, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action action) {
            this(picasso, dispatcher, cache, stats, action, null);
        }

        @org.checkerframework.dataflow.qual.Impure
        TestableBitmapHunter(@org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Dispatcher dispatcher, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Stats stats, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action action, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Bitmap result) {
            this(picasso, dispatcher, cache, stats, action, result, null);
        }

        @org.checkerframework.dataflow.qual.Impure
        TestableBitmapHunter(@org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Dispatcher dispatcher, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Stats stats, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action action, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Bitmap result, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Exception exception) {
            super(picasso, dispatcher, cache, stats, action, spy(new TestableRequestHandler(result, exception)));
        }
    }

    private static class TestableRequestHandler extends RequestHandler {

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Bitmap bitmap;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Exception exception;

        @org.checkerframework.dataflow.qual.Impure
        TestableRequestHandler(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Bitmap bitmap, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Exception exception) {
            this.bitmap = bitmap;
            this.exception = exception;
        }

        @org.checkerframework.dataflow.qual.Pure
        public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean canHandleRequest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TestableRequestHandler this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request data) {
            return true;
        }

        @org.checkerframework.dataflow.qual.Impure
        public void load(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TestableRequestHandler this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request request, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
            if (exception != null) {
                callback.onError(exception);
            } else {
                callback.onSuccess(new Result(bitmap, MEMORY));
            }
        }

        @org.checkerframework.dataflow.qual.Pure
         @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getRetryCount(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TestableRequestHandler this) {
            return 1;
        }
    }

    private static class OOMBitmapHunter extends BitmapHunter {

        @org.checkerframework.dataflow.qual.Impure
        OOMBitmapHunter(@org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Dispatcher dispatcher, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Stats stats, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action action) {
            super(picasso, dispatcher, cache, stats, action, spy(new OOMRequestHandler()));
        }
    }

    private static class OOMRequestHandler extends TestableRequestHandler {

        @org.checkerframework.dataflow.qual.Impure
        OOMRequestHandler() {
            super(null, null);
        }

        @org.checkerframework.dataflow.qual.Impure
        public void load(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull OOMRequestHandler this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request request, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
            callback.onError(new OutOfMemoryError());
        }
    }

    private class CustomRequestHandler extends RequestHandler {

        @org.checkerframework.dataflow.qual.Impure
        public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean canHandleRequest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CustomRequestHandler this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request data) {
            return CUSTOM_URI.getScheme().equals(data.uri.getScheme());
        }

        @org.checkerframework.dataflow.qual.Impure
        public void load(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CustomRequestHandler this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request request, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
            callback.onSuccess(new Result(bitmap, MEMORY));
        }
    }
}
