/*
 * Copyright (C) 2018 Square, Inc.
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.util.LruCache;
import static com.squareup.picasso3.Request.KEY_SEPARATOR;

/**
 * A memory cache which uses a least-recently used eviction policy.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
final class PlatformLruCache {

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LruCache<String, BitmapAndSize> cache;

    /**
     * Create a cache with a given maximum size in bytes.
     */
    @org.checkerframework.dataflow.qual.Impure
    PlatformLruCache( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int maxByteCount) {
        cache = new LruCache<String, BitmapAndSize>(maxByteCount != 0 ? maxByteCount : 1) {

            protected int sizeOf(String key, BitmapAndSize value) {
                return value.byteCount;
            }
        };
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Bitmap get(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String key) {
        BitmapAndSize bitmapAndSize = cache.get(key);
        return bitmapAndSize != null ? bitmapAndSize.bitmap : null;
    }

    @org.checkerframework.dataflow.qual.Impure
    void set(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Bitmap bitmap) {
        if (key == null || bitmap == null) {
            throw new NullPointerException("key == null || bitmap == null");
        }
        int byteCount = BitmapCompat.getAllocationByteCount(bitmap);
        // If the bitmap is too big for the cache, don't even attempt to store it. Doing so will cause
        // the cache to be cleared. Instead just evict an existing element with the same key if it
        // exists.
        if (byteCount > maxSize()) {
            cache.remove(key);
            return;
        }
        cache.put(key, new BitmapAndSize(bitmap, byteCount));
    }

    @org.checkerframework.dataflow.qual.Impure
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int size() {
        return cache.size();
    }

    @org.checkerframework.dataflow.qual.Impure
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int maxSize() {
        return cache.maxSize();
    }

    @org.checkerframework.dataflow.qual.Impure
    void clear() {
        cache.evictAll();
    }

    @org.checkerframework.dataflow.qual.Impure
    void clearKeyUri(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String uri) {
        // Keys are prefixed with a URI followed by '\n'.
        for (String key : cache.snapshot().keySet()) {
            if (key.startsWith(uri) && key.length() > uri.length() && key.charAt(uri.length()) == KEY_SEPARATOR) {
                cache.remove(key);
            }
        }
    }

    /**
     * Returns the number of times {@link #get} returned a value.
     */
    @org.checkerframework.dataflow.qual.Impure
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int hitCount() {
        return cache.hitCount();
    }

    /**
     * Returns the number of times {@link #get} returned {@code null}.
     */
    @org.checkerframework.dataflow.qual.Impure
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int missCount() {
        return cache.missCount();
    }

    /**
     * Returns the number of times {@link #set(String, Bitmap)} was called.
     */
    @org.checkerframework.dataflow.qual.Impure
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int putCount() {
        return cache.putCount();
    }

    /**
     * Returns the number of values that have been evicted.
     */
    @org.checkerframework.dataflow.qual.Impure
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int evictionCount() {
        return cache.evictionCount();
    }

    static final class BitmapAndSize {

        final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap;

        final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int byteCount;

        @org.checkerframework.dataflow.qual.SideEffectFree
        BitmapAndSize(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int byteCount) {
            this.bitmap = bitmap;
            this.byteCount = byteCount;
        }
    }
}
