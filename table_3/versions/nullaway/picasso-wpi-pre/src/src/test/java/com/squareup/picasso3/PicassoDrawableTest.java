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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import static android.graphics.Color.RED;
import static com.google.common.truth.Truth.assertThat;
import static com.squareup.picasso3.Picasso.LoadedFrom.DISK;
import static com.squareup.picasso3.Picasso.LoadedFrom.MEMORY;
import static com.squareup.picasso3.TestUtils.makeBitmap;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class PicassoDrawableTest {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Context context = RuntimeEnvironment.application;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Drawable placeholder = new ColorDrawable(RED);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap = makeBitmap();

    @org.checkerframework.dataflow.qual.Impure
    public void createWithNoPlaceholderAnimation() {
        PicassoDrawable pd = new PicassoDrawable(context, bitmap, null, DISK, false, false);
        assertThat(pd.getBitmap()).isSameAs(bitmap);
        assertThat(pd.placeholder).isNull();
        assertThat(pd.animating).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void createWithPlaceholderAnimation() {
        PicassoDrawable pd = new PicassoDrawable(context, bitmap, placeholder, DISK, false, false);
        assertThat(pd.getBitmap()).isSameAs(bitmap);
        assertThat(pd.placeholder).isSameAs(placeholder);
        assertThat(pd.animating).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void createWithBitmapCacheHit() {
        PicassoDrawable pd = new PicassoDrawable(context, bitmap, placeholder, MEMORY, false, false);
        assertThat(pd.getBitmap()).isSameAs(bitmap);
        assertThat(pd.placeholder).isNull();
        assertThat(pd.animating).isFalse();
    }
}
