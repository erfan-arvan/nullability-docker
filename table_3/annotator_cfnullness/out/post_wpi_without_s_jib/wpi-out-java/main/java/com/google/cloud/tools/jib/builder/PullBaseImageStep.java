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
import com.google.cloud.tools.jib.blob.Blobs;
import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.image.Image;
import com.google.cloud.tools.jib.image.LayerCountMismatchException;
import com.google.cloud.tools.jib.image.LayerPropertyNotFoundException;
import com.google.cloud.tools.jib.image.json.ContainerConfigurationTemplate;
import com.google.cloud.tools.jib.image.json.JsonToImageTranslator;
import com.google.cloud.tools.jib.image.json.ManifestTemplate;
import com.google.cloud.tools.jib.image.json.UnknownManifestFormatException;
import com.google.cloud.tools.jib.image.json.V21ManifestTemplate;
import com.google.cloud.tools.jib.image.json.V22ManifestTemplate;
import com.google.cloud.tools.jib.json.JsonTemplateMapper;
import com.google.cloud.tools.jib.registry.RegistryClient;
import com.google.cloud.tools.jib.registry.RegistryException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Pulls the base image manifest.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class PullBaseImageStep implements Callable<Image> {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DESCRIPTION = "Pulling base image manifest";

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<Authorization> pullAuthorizationFuture;

    @org.checkerframework.dataflow.qual.SideEffectFree
    PullBaseImageStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Future<Authorization> pullAuthorizationFuture) {
        this.buildConfiguration = buildConfiguration;
        this.pullAuthorizationFuture = pullAuthorizationFuture;
    }

    /**
     * Depends on {@code pullAuthorizationFuture}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image call(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PullBaseImageStep this) throws IOException, RegistryException, LayerPropertyNotFoundException, LayerCountMismatchException, ExecutionException, InterruptedException {
        try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
            RegistryClient registryClient = new RegistryClient(NonBlockingFutures.get(pullAuthorizationFuture), buildConfiguration.getBaseImageRegistry(), buildConfiguration.getBaseImageRepository());
            ManifestTemplate manifestTemplate = registryClient.pullManifest(buildConfiguration.getBaseImageTag());
            // TODO: Make schema version be enum.
            switch(manifestTemplate.getSchemaVersion()) {
                case 1:
                    V21ManifestTemplate v21ManifestTemplate = (V21ManifestTemplate) manifestTemplate;
                    return JsonToImageTranslator.toImage(v21ManifestTemplate);
                case 2:
                    V22ManifestTemplate v22ManifestTemplate = (V22ManifestTemplate) manifestTemplate;
                    if (v22ManifestTemplate.getContainerConfiguration() == null || v22ManifestTemplate.getContainerConfiguration().getDigest() == null) {
                        throw new UnknownManifestFormatException("Invalid container configuration in Docker V2.2 manifest: \n" + Blobs.writeToString(JsonTemplateMapper.toBlob(v22ManifestTemplate)));
                    }
                    ByteArrayOutputStream containerConfigurationOutputStream = new ByteArrayOutputStream();
                    registryClient.pullBlob(v22ManifestTemplate.getContainerConfiguration().getDigest(), containerConfigurationOutputStream);
                    String containerConfigurationString = new String(containerConfigurationOutputStream.toByteArray(), StandardCharsets.UTF_8);
                    ContainerConfigurationTemplate containerConfigurationTemplate = JsonTemplateMapper.readJson(containerConfigurationString, ContainerConfigurationTemplate.class);
                    return JsonToImageTranslator.toImage(v22ManifestTemplate, containerConfigurationTemplate);
            }
            throw new IllegalStateException("Unknown manifest schema version");
        }
    }
}
