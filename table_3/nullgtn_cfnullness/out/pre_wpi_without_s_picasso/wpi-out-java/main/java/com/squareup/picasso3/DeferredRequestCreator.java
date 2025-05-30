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

import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class DeferredRequestCreator implements OnPreDrawListener, OnAttachStateChangeListener {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RequestCreator creator;

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageView target;

    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Callback callback;

    @org.checkerframework.dataflow.qual.Impure
    DeferredRequestCreator(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RequestCreator creator, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageView target, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Callback callback) {
        this.creator = creator;
        this.target = target;
        this.callback = callback;
        target.addOnAttachStateChangeListener(this);
        // Only add the pre-draw listener if the view is already attached.
        // See: https://github.com/square/picasso/issues/1321
        if (target.getWindowToken() != null) {
            onViewAttachedToWindow(target);
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onViewAttachedToWindow(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DeferredRequestCreator this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull View view) {
        view.getViewTreeObserver().addOnPreDrawListener(this);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void onViewDetachedFromWindow(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DeferredRequestCreator this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull View view) {
        view.getViewTreeObserver().removeOnPreDrawListener(this);
    }

    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean onPreDraw(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DeferredRequestCreator this) {
        ImageView target = this.target;
        ViewTreeObserver vto = target.getViewTreeObserver();
        if (!vto.isAlive()) {
            return true;
        }
        int width = target.getWidth();
        int height = target.getHeight();
        if (width <= 0 || height <= 0) {
            return true;
        }
        target.removeOnAttachStateChangeListener(this);
        vto.removeOnPreDrawListener(this);
        this.creator.unfit().resize(width, height).into(target, callback);
        return true;
    }

    @org.checkerframework.dataflow.qual.Impure
    void cancel() {
        creator.clearTag();
        callback = null;
        target.removeOnAttachStateChangeListener(this);
        ViewTreeObserver vto = target.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.removeOnPreDrawListener(this);
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object getTag() {
        return creator.getTag();
    }
}
