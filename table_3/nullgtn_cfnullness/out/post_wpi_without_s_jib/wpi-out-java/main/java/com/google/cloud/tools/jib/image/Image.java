/*
 * Copyright 2017 Google Inc.
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
package com.google.cloud.tools.jib.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Represents an image.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Image {

    /**
     * The layers of the image, in the order in which they are applied.
     */
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageLayers<Layer> layers = new ImageLayers<>();

    /**
     * Environment variable definitions for running the image, in the format {@code NAME=VALUE}.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> environment = new ArrayList<>();

    /**
     * Initial command to run when running the image.
     */
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull List<String> entrypoint;

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getEnvironment() {
        return Collections.unmodifiableList(environment);
    }

    /**
     * Sets the environment with a map from environment variable names to values.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image setEnvironment(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, String> environment) {
        for (Map.Entry<String, String> environmentVariable : environment.entrySet()) {
            setEnvironmentVariable(environmentVariable.getKey(), environmentVariable.getValue());
        }
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image setEnvironmentVariable(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String name, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String value) {
        environment.add(name + "=" + value);
        return this;
    }

    /**
     * Adds an environment variable definition in the format {@code NAME=VALUE}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image addEnvironmentVariableDefinition(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String environmentVariableDefinition) {
        environment.add(environmentVariableDefinition);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getEntrypoint() {
        return Collections.unmodifiableList(entrypoint);
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image setEntrypoint(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> entrypoint) {
        this.entrypoint = entrypoint;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<Layer> getLayers() {
        return layers.getLayers();
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image addLayer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Layer layer) throws LayerPropertyNotFoundException {
        layers.add(layer);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public <T extends Layer> @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image addLayers(ImageLayers<T> layers) throws LayerPropertyNotFoundException {
        this.layers.addAll(layers);
        return this;
    }
}
