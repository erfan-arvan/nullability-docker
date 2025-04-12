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
import com.google.cloud.tools.jib.cache.CachedLayer;
import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.registry.RegistryClient;
import com.google.cloud.tools.jib.registry.RegistryException;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Pushes a BLOB to the target registry.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class PushBlobStep implements Callable<Void> {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DESCRIPTION = "Pushing BLOB ";

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<Authorization> pushAuthorizationFuture;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<CachedLayer> pullLayerFuture;

    @org.checkerframework.dataflow.qual.SideEffectFree
    PushBlobStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<Authorization> pushAuthorizationFuture, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<CachedLayer> pullLayerFuture) {
        this.buildConfiguration = buildConfiguration;
        this.pushAuthorizationFuture = pushAuthorizationFuture;
        this.pullLayerFuture = pullLayerFuture;
    }

    /**
     * Depends on {@code pushAuthorizationFuture} and {@code pullLayerFuture}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Void call(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PushBlobStep this) throws IOException, RegistryException, ExecutionException, InterruptedException {
        CachedLayer layer = pullLayerFuture.get();
        DescriptorDigest layerDigest = layer.getBlobDescriptor().getDigest();
        try (Timer timer = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION + layerDigest)) {
            RegistryClient registryClient = new RegistryClient(pushAuthorizationFuture.get(), buildConfiguration.getTargetRegistry(), buildConfiguration.getTargetRepository()).setTimer(timer);
            if (registryClient.checkBlob(layerDigest) != null) {
                buildConfiguration.getBuildLogger().info("BLOB : " + layerDigest + " already exists on registry");
                return null;
            }
            registryClient.pushBlob(layerDigest, layer.getBlob());
            return null;
        }
    }
}
