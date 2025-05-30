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
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.io.IOException;
import okio.Okio;
import okio.Source;
import static android.content.ContentResolver.SCHEME_FILE;
import static com.squareup.picasso3.Picasso.LoadedFrom.DISK;
import static com.squareup.picasso3.Utils.checkNotNull;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class AssetRequestHandler extends RequestHandler {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String ANDROID_ASSET = "android_asset";

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int ASSET_PREFIX_LENGTH = (SCHEME_FILE + ":///" + ANDROID_ASSET + "/").length();

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Context context;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object lock = new Object();

    private volatile @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull AssetManager assetManager;

    @org.checkerframework.dataflow.qual.Impure
    AssetRequestHandler(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Context context) {
        this.context = context;
    }

    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean canHandleRequest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AssetRequestHandler this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request data) {
        Uri uri = data.uri;
        return uri != null && SCHEME_FILE.equals(uri.getScheme()) && !uri.getPathSegments().isEmpty() && ANDROID_ASSET.equals(uri.getPathSegments().get(0));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void load(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AssetRequestHandler this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request request, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
        initializeIfFirstTime();
        boolean signaledCallback = false;
        try {
            Source source = Okio.source(assetManager.open(getFilePath(request)));
            try {
                Bitmap bitmap = decodeStream(source, request);
                signaledCallback = true;
                callback.onSuccess(new Result(bitmap, DISK));
            } finally {
                try {
                    source.close();
                } catch (IOException ignored) {
                }
            }
        } catch (Exception e) {
            if (!signaledCallback) {
                callback.onError(e);
            }
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    private void initializeIfFirstTime() {
        if (assetManager == null) {
            synchronized (lock) {
                if (assetManager == null) {
                    assetManager = context.getAssets();
                }
            }
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getFilePath(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request request) {
        Uri uri = checkNotNull(request.uri, "request.uri == null");
        return uri.toString().substring(ASSET_PREFIX_LENGTH);
    }
}
