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

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.graphics.BitmapCompat;
import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class Stats {

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int CACHE_HIT = 0;

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int CACHE_MISS = 1;

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int BITMAP_DECODE_FINISHED = 2;

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int BITMAP_TRANSFORMED_FINISHED = 3;

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int DOWNLOAD_FINISHED = 4;

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String STATS_THREAD_NAME = Utils.THREAD_PREFIX + "Stats";

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull HandlerThread statsThread;

    final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache;

    final @org.checkerframework.checker.initialization.qual.UnderInitialization(com.squareup.picasso3.Stats.StatsHandler.class) @org.checkerframework.checker.nullness.qual.NonNull Handler handler;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long cacheHits;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long cacheMisses;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long totalDownloadSize;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long totalOriginalBitmapSize;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long totalTransformedBitmapSize;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long averageDownloadSize;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long averageOriginalBitmapSize;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long averageTransformedBitmapSize;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int downloadCount;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int originalBitmapCount;

     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int transformedBitmapCount;

    @org.checkerframework.dataflow.qual.Impure
    Stats(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PlatformLruCache cache) {
        this.cache = cache;
        this.statsThread = new HandlerThread(STATS_THREAD_NAME, THREAD_PRIORITY_BACKGROUND);
        this.statsThread.start();
        Utils.flushStackLocalLeaks(statsThread.getLooper());
        this.handler = new StatsHandler(statsThread.getLooper(), this);
    }

    @org.checkerframework.dataflow.qual.Impure
    void dispatchBitmapDecoded(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap) {
        processBitmap(bitmap, BITMAP_DECODE_FINISHED);
    }

    @org.checkerframework.dataflow.qual.Impure
    void dispatchBitmapTransformed(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap) {
        processBitmap(bitmap, BITMAP_TRANSFORMED_FINISHED);
    }

    @org.checkerframework.dataflow.qual.Impure
    void dispatchDownloadFinished( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long size) {
        handler.sendMessage(handler.obtainMessage(DOWNLOAD_FINISHED, size));
    }

    @org.checkerframework.dataflow.qual.Impure
    void dispatchCacheHit() {
        handler.sendEmptyMessage(CACHE_HIT);
    }

    @org.checkerframework.dataflow.qual.Impure
    void dispatchCacheMiss() {
        handler.sendEmptyMessage(CACHE_MISS);
    }

    @org.checkerframework.dataflow.qual.Impure
    void shutdown() {
        statsThread.quit();
    }

    @org.checkerframework.dataflow.qual.Impure
    void performCacheHit() {
        cacheHits++;
    }

    @org.checkerframework.dataflow.qual.Impure
    void performCacheMiss() {
        cacheMisses++;
    }

    @org.checkerframework.dataflow.qual.Impure
    void performDownloadFinished(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Long size) {
        downloadCount++;
        totalDownloadSize += size;
        averageDownloadSize = getAverage(downloadCount, totalDownloadSize);
    }

    @org.checkerframework.dataflow.qual.Impure
    void performBitmapDecoded( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long size) {
        originalBitmapCount++;
        totalOriginalBitmapSize += size;
        averageOriginalBitmapSize = getAverage(originalBitmapCount, totalOriginalBitmapSize);
    }

    @org.checkerframework.dataflow.qual.Impure
    void performBitmapTransformed( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long size) {
        transformedBitmapCount++;
        totalTransformedBitmapSize += size;
        averageTransformedBitmapSize = getAverage(originalBitmapCount, totalTransformedBitmapSize);
    }

    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull StatsSnapshot createSnapshot() {
        return new StatsSnapshot(cache.maxSize(), cache.size(), cacheHits, cacheMisses, totalDownloadSize, totalOriginalBitmapSize, totalTransformedBitmapSize, averageDownloadSize, averageOriginalBitmapSize, averageTransformedBitmapSize, downloadCount, originalBitmapCount, transformedBitmapCount, System.currentTimeMillis());
    }

    @org.checkerframework.dataflow.qual.Impure
    private void processBitmap(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap bitmap,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int what) {
        // Never send bitmaps to the handler as they could be recycled before we process them.
        int bitmapSize = BitmapCompat.getAllocationByteCount(bitmap);
        handler.sendMessage(handler.obtainMessage(what, bitmapSize, 0));
    }

    @org.checkerframework.dataflow.qual.Pure
    private static  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long getAverage( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int count,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long totalSize) {
        return totalSize / count;
    }

    private static class StatsHandler extends Handler {

        private final @org.checkerframework.checker.initialization.qual.UnderInitialization(java.lang.Object.class) @org.checkerframework.checker.nullness.qual.NonNull Stats stats;

        @org.checkerframework.dataflow.qual.Impure
        StatsHandler(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Looper looper, @org.checkerframework.checker.initialization.qual.UnderInitialization(java.lang.Object.class) @org.checkerframework.checker.nullness.qual.NonNull Stats stats) {
            super(looper);
            this.stats = stats;
        }

        @org.checkerframework.dataflow.qual.Impure
        public void handleMessage(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull StatsHandler this, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Message msg) {
            switch(msg.what) {
                case CACHE_HIT:
                    stats.performCacheHit();
                    break;
                case CACHE_MISS:
                    stats.performCacheMiss();
                    break;
                case BITMAP_DECODE_FINISHED:
                    stats.performBitmapDecoded(msg.arg1);
                    break;
                case BITMAP_TRANSFORMED_FINISHED:
                    stats.performBitmapTransformed(msg.arg1);
                    break;
                case DOWNLOAD_FINISHED:
                    stats.performDownloadFinished((Long) msg.obj);
                    break;
                default:
                    Picasso.HANDLER.post(new Runnable() {

                        public void run() {
                            throw new AssertionError("Unhandled stats message." + msg.what);
                        }
                    });
            }
        }
    }
}
