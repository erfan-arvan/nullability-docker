/*
 * Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.tools.jib.builder;

import com.google.cloud.tools.jib.Timer;
import com.google.cloud.tools.jib.cache.Cache;
import com.google.cloud.tools.jib.cache.CacheReader;
import com.google.cloud.tools.jib.cache.CacheWriter;
import com.google.cloud.tools.jib.cache.CachedLayer;
import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.image.LayerPropertyNotFoundException;
import com.google.cloud.tools.jib.registry.RegistryClient;
import com.google.cloud.tools.jib.registry.RegistryException;
import com.google.common.io.CountingOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Pulls and caches a single base image layer.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class PullAndCacheBaseImageLayerStep implements Callable<CachedLayer> {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DESCRIPTION = "Pulling base image layer %s";

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache cache;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DescriptorDigest layerDigest;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<Authorization> pullAuthorizationFuture;

    @org.checkerframework.dataflow.qual.SideEffectFree
    PullAndCacheBaseImageLayerStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache cache, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest layerDigest, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<Authorization> pullAuthorizationFuture) {
        this.buildConfiguration = buildConfiguration;
        this.cache = cache;
        this.layerDigest = layerDigest;
        this.pullAuthorizationFuture = pullAuthorizationFuture;
    }

    /**
     * Depends on {@code pullAuthorizationFuture}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CachedLayer call(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PullAndCacheBaseImageLayerStep this) throws IOException, RegistryException, LayerPropertyNotFoundException, ExecutionException, InterruptedException {
        try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), String.format(DESCRIPTION, layerDigest))) {
            RegistryClient registryClient = new RegistryClient(pullAuthorizationFuture.get(), buildConfiguration.getBaseImageRegistry(), buildConfiguration.getBaseImageRepository());
            // Checks if the layer already exists in the cache.
            CachedLayer cachedLayer = new CacheReader(cache).getLayer(layerDigest);
            if (cachedLayer != null) {
                return cachedLayer;
            }
            CacheWriter cacheWriter = new CacheWriter(cache);
            CountingOutputStream layerOutputStream = cacheWriter.getLayerOutputStream(layerDigest);
            registryClient.pullBlob(layerDigest, layerOutputStream);
            return cacheWriter.getCachedLayer(layerDigest, layerOutputStream);
        }
    }
}
