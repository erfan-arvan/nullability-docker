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
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import static android.graphics.Color.WHITE;
import static com.squareup.picasso3.Picasso.LoadedFrom.MEMORY;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
final class PicassoDrawable extends BitmapDrawable {

    // Only accessed from main thread.
    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Paint DEBUG_PAINT = new Paint();

    //ms
    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull float FADE_DURATION = 200f;

    /**
     * Create or update the drawable on the target {@link ImageView} to display the supplied bitmap
     * image.
     */
    @org.checkerframework.dataflow.qual.Impure
    static void setResult(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageView target, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Context context, RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean noFade,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean debugging) {
        Drawable placeholder = target.getDrawable();
        if (placeholder instanceof Animatable) {
            ((Animatable) placeholder).stop();
        }
        Bitmap bitmap = result.getBitmap();
        if (bitmap != null) {
            Picasso.LoadedFrom loadedFrom = result.getLoadedFrom();
            PicassoDrawable drawable = new PicassoDrawable(context, bitmap, placeholder, loadedFrom, noFade, debugging);
            target.setImageDrawable(drawable);
            return;
        }
        Drawable drawable = result.getDrawable();
        if (drawable != null) {
            target.setImageDrawable(drawable);
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        }
    }

    /**
     * Create or update the drawable on the target {@link ImageView} to display the supplied
     * placeholder image.
     */
    @org.checkerframework.dataflow.qual.Impure
    static void setPlaceholder(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageView target, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Drawable placeholderDrawable) {
        target.setImageDrawable(placeholderDrawable);
        if (target.getDrawable() instanceof Animatable) {
            ((Animatable) target.getDrawable()).start();
        }
    }

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean debugging;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull float density;

    private final Picasso.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LoadedFrom loadedFrom;

    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Drawable placeholder;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long startTimeMillis;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean animating;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int alpha = 0xFF;

    @org.checkerframework.dataflow.qual.Impure
    PicassoDrawable(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Context context, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Drawable placeholder, Picasso.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LoadedFrom loadedFrom,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean noFade,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean debugging) {
        super(context.getResources(), bitmap);
        this.debugging = debugging;
        this.density = context.getResources().getDisplayMetrics().density;
        this.loadedFrom = loadedFrom;
        boolean fade = loadedFrom != MEMORY && !noFade;
        if (fade) {
            this.placeholder = placeholder;
            animating = true;
            startTimeMillis = SystemClock.uptimeMillis();
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void draw(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PicassoDrawable this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Canvas canvas) {
        if (!animating) {
            super.draw(canvas);
        } else {
            float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / FADE_DURATION;
            if (normalized >= 1f) {
                animating = false;
                placeholder = null;
                super.draw(canvas);
            } else {
                if (placeholder != null) {
                    placeholder.draw(canvas);
                }
                // setAlpha will call invalidateSelf and drive the animation.
                int partialAlpha = (int) (alpha * normalized);
                super.setAlpha(partialAlpha);
                super.draw(canvas);
                super.setAlpha(alpha);
            }
        }
        if (debugging) {
            drawDebugIndicator(canvas);
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void setAlpha(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PicassoDrawable this,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int alpha) {
        this.alpha = alpha;
        if (placeholder != null) {
            placeholder.setAlpha(alpha);
        }
        super.setAlpha(alpha);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void setColorFilter(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PicassoDrawable this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable ColorFilter cf) {
        if (placeholder != null) {
            placeholder.setColorFilter(cf);
        }
        super.setColorFilter(cf);
    }

    @org.checkerframework.dataflow.qual.Impure
    protected void onBoundsChange(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PicassoDrawable this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Rect bounds) {
        if (placeholder != null) {
            placeholder.setBounds(bounds);
        }
        super.onBoundsChange(bounds);
    }

    @org.checkerframework.dataflow.qual.Impure
    private void drawDebugIndicator(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Canvas canvas) {
        DEBUG_PAINT.setColor(WHITE);
        Path path = getTrianglePath(0, 0, (int) (16 * density));
        canvas.drawPath(path, DEBUG_PAINT);
        DEBUG_PAINT.setColor(loadedFrom.debugColor);
        path = getTrianglePath(0, 0, (int) (15 * density));
        canvas.drawPath(path, DEBUG_PAINT);
    }

    @org.checkerframework.dataflow.qual.Impure
    private static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path getTrianglePath( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int x1,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int y1,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int width) {
        final Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x1 + width, y1);
        path.lineTo(x1, y1 + width);
        return path;
    }
}
