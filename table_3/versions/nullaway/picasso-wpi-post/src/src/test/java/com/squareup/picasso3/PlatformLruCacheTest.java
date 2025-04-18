/*
 * Copyright (C) 2011 The Android Open Source Project
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static android.graphics.Bitmap.Config.ALPHA_8;
import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class PlatformLruCacheTest {

    // The use of ALPHA_8 simplifies the size math in tests since only one byte is used per-pixel.
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap A = Bitmap.createBitmap(1, 1, ALPHA_8);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap B = Bitmap.createBitmap(1, 1, ALPHA_8);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap C = Bitmap.createBitmap(1, 1, ALPHA_8);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap D = Bitmap.createBitmap(1, 1, ALPHA_8);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap E = Bitmap.createBitmap(1, 1, ALPHA_8);

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int expectedPutCount;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int expectedHitCount;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int expectedMissCount;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int expectedEvictionCount;

    @org.checkerframework.dataflow.qual.Impure
    public void testStatistics() {
        PlatformLruCache cache = new PlatformLruCache(3);
        assertStatistics(cache);
        cache.set("a", A);
        expectedPutCount++;
        assertStatistics(cache);
        assertHit(cache, "a", A);
        cache.set("b", B);
        expectedPutCount++;
        assertStatistics(cache);
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertSnapshot(cache, "a", A, "b", B);
        cache.set("c", C);
        expectedPutCount++;
        assertStatistics(cache);
        assertHit(cache, "a", A);
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertSnapshot(cache, "a", A, "b", B, "c", C);
        cache.set("d", D);
        expectedPutCount++;
        // a should have been evicted
        expectedEvictionCount++;
        assertStatistics(cache);
        assertMiss(cache, "a");
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertHit(cache, "d", D);
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertSnapshot(cache, "d", D, "b", B, "c", C);
        cache.set("e", E);
        expectedPutCount++;
        // d should have been evicted
        expectedEvictionCount++;
        assertStatistics(cache);
        assertMiss(cache, "d");
        assertMiss(cache, "a");
        assertHit(cache, "e", E);
        assertHit(cache, "b", B);
        assertHit(cache, "c", C);
        assertSnapshot(cache, "e", E, "b", B, "c", C);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cannotPutNullKey() {
        PlatformLruCache cache = new PlatformLruCache(3);
        try {
            cache.set(null, A);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void cannotPutNullValue() {
        PlatformLruCache cache = new PlatformLruCache(3);
        try {
            cache.set("a", null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void evictionWithSingletonCache() {
        PlatformLruCache cache = new PlatformLruCache(1);
        cache.set("a", A);
        cache.set("b", B);
        assertSnapshot(cache, "b", B);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void throwsWithNullKey() {
        PlatformLruCache cache = new PlatformLruCache(1);
        try {
            cache.get(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
        }
    }

    /**
     * Replacing the value for a key doesn't cause an eviction but it does bring the replaced entry to
     * the front of the queue.
     */
    @org.checkerframework.dataflow.qual.Impure
    public void putCauseEviction() {
        PlatformLruCache cache = new PlatformLruCache(3);
        cache.set("a", A);
        cache.set("b", B);
        cache.set("c", C);
        cache.set("b", D);
        assertSnapshot(cache, "a", A, "c", C, "b", D);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void evictAll() {
        PlatformLruCache cache = new PlatformLruCache(4);
        cache.set("a", A);
        cache.set("b", B);
        cache.set("c", C);
        cache.clear();
        assertThat(cache.cache.snapshot()).isEmpty();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void clearPrefixedKey() {
        PlatformLruCache cache = new PlatformLruCache(3);
        cache.set("Hello\nAlice!", A);
        cache.set("Hello\nBob!", B);
        cache.set("Hello\nEve!", C);
        cache.set("Hellos\nWorld!", D);
        cache.clearKeyUri("Hello");
        assertThat(cache.cache.snapshot()).hasSize(1);
        assertThat(cache.cache.snapshot()).containsKey("Hellos\nWorld!");
    }

    @org.checkerframework.dataflow.qual.Impure
    public void invalidate() {
        PlatformLruCache cache = new PlatformLruCache(3);
        cache.set("Hello\nAlice!", A);
        assertThat(cache.size()).isEqualTo(1);
        cache.clearKeyUri("Hello");
        assertThat(cache.size()).isEqualTo(0);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void overMaxSizeDoesNotClear() {
        PlatformLruCache cache = new PlatformLruCache(16);
        Bitmap size4 = Bitmap.createBitmap(2, 2, ALPHA_8);
        Bitmap size16 = Bitmap.createBitmap(4, 4, ALPHA_8);
        Bitmap size25 = Bitmap.createBitmap(5, 5, ALPHA_8);
        cache.set("4", size4);
        expectedPutCount++;
        assertHit(cache, "4", size4);
        cache.set("16", size16);
        expectedPutCount++;
        // size4 was evicted.
        expectedEvictionCount++;
        assertMiss(cache, "4");
        assertHit(cache, "16", size16);
        cache.set("25", size25);
        assertHit(cache, "16", size16);
        assertMiss(cache, "25");
        assertThat(cache.size()).isEqualTo(16);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void overMaxSizeRemovesExisting() {
        PlatformLruCache cache = new PlatformLruCache(20);
        Bitmap size4 = Bitmap.createBitmap(2, 2, ALPHA_8);
        Bitmap size16 = Bitmap.createBitmap(4, 4, ALPHA_8);
        Bitmap size25 = Bitmap.createBitmap(5, 5, ALPHA_8);
        cache.set("small", size4);
        expectedPutCount++;
        assertHit(cache, "small", size4);
        cache.set("big", size16);
        expectedPutCount++;
        assertHit(cache, "small", size4);
        assertHit(cache, "big", size16);
        cache.set("big", size25);
        assertHit(cache, "small", size4);
        assertMiss(cache, "big");
        assertThat(cache.size()).isEqualTo(4);
    }

    @org.checkerframework.dataflow.qual.Impure
    private void assertHit(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap value) {
        assertThat(cache.get(key)).isEqualTo(value);
        expectedHitCount++;
        assertStatistics(cache);
    }

    @org.checkerframework.dataflow.qual.Impure
    private void assertMiss(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key) {
        assertThat(cache.get(key)).isNull();
        expectedMissCount++;
        assertStatistics(cache);
    }

    @org.checkerframework.dataflow.qual.Impure
    private void assertStatistics(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache) {
        assertThat(cache.putCount()).isEqualTo(expectedPutCount);
        assertThat(cache.hitCount()).isEqualTo(expectedHitCount);
        assertThat(cache.missCount()).isEqualTo(expectedMissCount);
        assertThat(cache.evictionCount()).isEqualTo(expectedEvictionCount);
    }

    @org.checkerframework.dataflow.qual.Impure
    private void assertSnapshot(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ... keysAndValues) {
        List<Object> actualKeysAndValues = new ArrayList<>();
        for (Map.Entry<String, PlatformLruCache.BitmapAndSize> entry : cache.cache.snapshot().entrySet()) {
            actualKeysAndValues.add(entry.getKey());
            actualKeysAndValues.add(entry.getValue().bitmap);
        }
        // assert using lists because order is important for LRUs
        assertThat(actualKeysAndValues).isEqualTo(Arrays.asList(keysAndValues));
    }
}
