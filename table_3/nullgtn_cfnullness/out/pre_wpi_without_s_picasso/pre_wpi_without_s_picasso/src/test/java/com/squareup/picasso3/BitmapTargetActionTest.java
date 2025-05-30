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
import android.graphics.drawable.Drawable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.squareup.picasso3.Picasso.LoadedFrom.MEMORY;
import static com.squareup.picasso3.TestUtils.NO_HANDLERS;
import static com.squareup.picasso3.TestUtils.NO_TRANSFORMERS;
import static com.squareup.picasso3.TestUtils.RESOURCE_ID_1;
import static com.squareup.picasso3.TestUtils.UNUSED_CALL_FACTORY;
import static com.squareup.picasso3.TestUtils.makeBitmap;
import static com.squareup.picasso3.TestUtils.mockTarget;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import androidx.annotation.Nullable;

@RunWith(RobolectricTestRunner.class)
public class BitmapTargetActionTest {

    @Test(expected = AssertionError.class)
    public void throwsErrorWithNullResult() {
        BitmapTarget target = mockTarget();
        BitmapTargetAction request = new BitmapTargetAction(mock(Picasso.class), new Target<>(target), null);
        request.complete(null);
    }

    @Test
    public void invokesSuccessIfTargetIsNotNull() {
        Bitmap bitmap = makeBitmap();
        BitmapTarget target = mockTarget();
        BitmapTargetAction request = new BitmapTargetAction(mock(Picasso.class), new Target<>(target), null);
        request.complete(new RequestHandler.Result(bitmap, MEMORY));
        verify(target).onBitmapLoaded(bitmap, MEMORY);
    }

    @Test
    public void invokesOnBitmapFailedIfTargetIsNotNullWithErrorDrawable() {
        Drawable errorDrawable = mock(Drawable.class);
        BitmapTarget target = mockTarget();
        Target<BitmapTarget> wrapper = new Target<>(target, errorDrawable);
        BitmapTargetAction request = new BitmapTargetAction(mock(Picasso.class), wrapper, null);
        Exception e = new RuntimeException();
        request.error(e);
        verify(target).onBitmapFailed(e, errorDrawable);
    }

    @Test
    public void invokesOnBitmapFailedIfTargetIsNotNullWithErrorResourceId() {
        Drawable errorDrawable = mock(Drawable.class);
        BitmapTarget target = mockTarget();
        Context context = mock(Context.class);
        Dispatcher dispatcher = mock(Dispatcher.class);
        PlatformLruCache cache = new PlatformLruCache(0);
        Picasso picasso = new Picasso(context, dispatcher, UNUSED_CALL_FACTORY, null, cache, null, NO_TRANSFORMERS, NO_HANDLERS, mock(Stats.class), ARGB_8888, false, false);
        Resources res = mock(Resources.class);
        Target<BitmapTarget> wrapper = new Target<>(target, RESOURCE_ID_1);
        BitmapTargetAction request = new BitmapTargetAction(picasso, wrapper, null);
        when(context.getResources()).thenReturn(res);
        when(res.getDrawable(RESOURCE_ID_1)).thenReturn(errorDrawable);
        Exception e = new RuntimeException();
        request.error(e);
        verify(target).onBitmapFailed(e, errorDrawable);
    }

    @Test
    public void recyclingInSuccessThrowsException() {
        BitmapTarget bad = new BitmapTarget() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, @Nullable() Picasso.LoadedFrom from) {
                bitmap.recycle();
            }

            @Override
            public void onBitmapFailed(@Nullable() Exception e, @Nullable() Drawable errorDrawable) {
                throw new AssertionError();
            }

            @Override
            public void onPrepareLoad(@Nullable() Drawable placeHolderDrawable) {
                throw new AssertionError();
            }
        };
        Picasso picasso = mock(Picasso.class);
        Bitmap bitmap = makeBitmap();
        BitmapTargetAction tr = new BitmapTargetAction(picasso, new Target<>(bad), null);
        try {
            tr.complete(new RequestHandler.Result(bitmap, MEMORY));
            fail();
        } catch (IllegalStateException ignored) {
        }
    }
}
