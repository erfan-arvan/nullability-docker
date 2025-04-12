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
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class PushLayersStep implements Callable<List<ListenableFuture<Void>>> {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DESCRIPTION = "Setting up to push layers";

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListeningExecutorService listeningExecutorService;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListenableFuture<Authorization> pushAuthorizationFuture;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListenableFuture<List<ListenableFuture<CachedLayer>>> cachedLayerFuturesFuture;

    @org.checkerframework.dataflow.qual.SideEffectFree
    PushLayersStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListeningExecutorService listeningExecutorService, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListenableFuture<Authorization> pushAuthorizationFuture, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListenableFuture<List<ListenableFuture<CachedLayer>>> cachedLayerFuturesFuture) {
        this.buildConfiguration = buildConfiguration;
        this.listeningExecutorService = listeningExecutorService;
        this.pushAuthorizationFuture = pushAuthorizationFuture;
        this.cachedLayerFuturesFuture = cachedLayerFuturesFuture;
    }

    /**
     * Depends on {@code cachedLayerFuturesFuture}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<ListenableFuture<Void>> call(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PushLayersStep this) throws ExecutionException, InterruptedException {
        try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
            // Pushes the image layers.
            List<ListenableFuture<Void>> pushLayerFutures = new ArrayList<>();
            for (ListenableFuture<CachedLayer> cachedLayerFuture : NonBlockingFutures.get(cachedLayerFuturesFuture)) {
                pushLayerFutures.add(Futures.whenAllComplete(pushAuthorizationFuture, cachedLayerFuture).call(new PushBlobStep(buildConfiguration, pushAuthorizationFuture, cachedLayerFuture), listeningExecutorService));
            }
            return pushLayerFutures;
        }
    }
}
