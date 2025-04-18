/*
 * Copyright (C) 2014 Square, Inc.
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

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.squareup.picasso3.RemoteViewsAction.RemoteViewsTarget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.google.common.truth.Truth.assertThat;
import static com.squareup.picasso3.Picasso.LoadedFrom.NETWORK;
import static com.squareup.picasso3.TestUtils.NO_HANDLERS;
import static com.squareup.picasso3.TestUtils.NO_TRANSFORMERS;
import static com.squareup.picasso3.TestUtils.UNUSED_CALL_FACTORY;
import static com.squareup.picasso3.TestUtils.makeBitmap;
import static com.squareup.picasso3.TestUtils.mockCallback;
import static com.squareup.picasso3.TestUtils.mockImageViewTarget;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class RemoteViewsActionTest {

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Picasso picasso;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull RemoteViews remoteViews;

    @org.checkerframework.dataflow.qual.Impure
    public void setUp() {
        picasso = createPicasso();
        remoteViews = mock(RemoteViews.class);
        when(remoteViews.getLayoutId()).thenReturn(android.R.layout.list_content);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void completeSetsBitmapOnRemoteViews() {
        Callback callback = mockCallback();
        Bitmap bitmap = makeBitmap();
        RemoteViewsAction action = createAction(callback);
        action.complete(new RequestHandler.Result(bitmap, NETWORK));
        verify(remoteViews).setImageViewBitmap(1, bitmap);
        verify(callback).onSuccess();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void errorWithNoResourceIsNoop() {
        Callback callback = mockCallback();
        RemoteViewsAction action = createAction(callback);
        Exception e = new RuntimeException();
        action.error(e);
        verifyZeroInteractions(remoteViews);
        verify(callback).onError(e);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void errorWithResourceSetsResource() {
        Callback callback = mockCallback();
        RemoteViewsAction action = createAction(1, callback);
        Exception e = new RuntimeException();
        action.error(e);
        verify(remoteViews).setImageViewResource(1, 1);
        verify(callback).onError(e);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void clearsCallbackOnCancel() {
        Picasso picasso = mock(Picasso.class);
        ImageView target = mockImageViewTarget();
        Callback callback = mockCallback();
        ImageViewAction request = new ImageViewAction(picasso, new Target<>(target), null, callback);
        request.cancel();
        assertThat(request.callback).isNull();
    }

    @org.checkerframework.dataflow.qual.Pure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TestableRemoteViewsAction createAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
        return createAction(0, callback);
    }

    @org.checkerframework.dataflow.qual.Pure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TestableRemoteViewsAction createAction( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int errorResId, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
        Target<RemoteViewsTarget> wrapper = new Target<>(new RemoteViewsTarget(remoteViews, 1), errorResId);
        return new TestableRemoteViewsAction(picasso, null, wrapper, callback);
    }

    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Picasso createPicasso() {
        Dispatcher dispatcher = mock(Dispatcher.class);
        PlatformLruCache cache = new PlatformLruCache(0);
        return new Picasso(RuntimeEnvironment.application, dispatcher, UNUSED_CALL_FACTORY, null, cache, null, NO_TRANSFORMERS, NO_HANDLERS, mock(Stats.class), ARGB_8888, false, false);
    }

    static class TestableRemoteViewsAction extends RemoteViewsAction {

        @org.checkerframework.dataflow.qual.SideEffectFree
        TestableRemoteViewsAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Request data, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Target<RemoteViewsTarget> target, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
            super(picasso, data, target, callback);
        }

        @org.checkerframework.dataflow.qual.SideEffectFree
        void update(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TestableRemoteViewsAction this) {
        }
    }
}
