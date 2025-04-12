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
package com.google.cloud.tools.jib.image.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.json.JsonTemplate;
import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

/**
 * JSON Template for Docker Container Configuration referenced in Docker Manifest Schema V2.2
 *
 * <p>Example container config JSON:
 *
 * <pre>{@code
 * {
 *   "architecture": "amd64",
 *   "os": "linux",
 *   "config": {
 *     "Env": ["/usr/bin/java"],
 *     "Entrypoint": ["PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"]
 *   },
 *   "rootfs": {
 *     "diff_ids": [
 *       "sha256:2aebd096e0e237b447781353379722157e6c2d434b9ec5a0d63f2a6f07cf90c2",
 *       "sha256:5f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef",
 *     ],
 *     "type": "layers"
 *   }
 * }
 * }</pre>
 *
 * @see <a href="https://docs.docker.com/registry/spec/manifest-v2-2/">Image Manifest Version 2,
 *     Schema 2</a>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ContainerConfigurationTemplate implements JsonTemplate {

    /**
     * The CPU architecture to run the binaries in this container.
     */
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String architecture = "amd64";

    /**
     * The operating system to run the container on.
     */
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String os = "linux";

    /**
     * Execution parameters that should be used as a base when running the container.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ConfigurationObjectTemplate config = new ConfigurationObjectTemplate();

    /**
     * Layer content digests that are used to build the container filesystem.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RootFilesystemObjectTemplate rootfs = new RootFilesystemObjectTemplate();

    /**
     * Template for inner JSON object representing the configuration for running the container.
     */
    private static class ConfigurationObjectTemplate implements JsonTemplate {

        /**
         * Environment variables in the format {@code VARNAME=VARVALUE}.
         */
        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull List<String> Env;

        /**
         * Command to run when container starts.
         */
        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull List<String> Entrypoint;
    }

    /**
     * Template for inner JSON object representing the filesystem changesets used to build the
     * container filesystem.
     */
    private static class RootFilesystemObjectTemplate implements JsonTemplate {

        /**
         * The type must always be {@code "layers"}.
         */
        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String type = "layers";

        /**
         * The in-order list of layer content digests (hashes of the uncompressed partial filesystem
         * changeset).
         */
        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<DescriptorDigest> diff_ids = new ArrayList<>();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void setContainerEnvironment(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> environment) {
        config.Env = environment;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void setContainerEntrypoint(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> command) {
        config.Entrypoint = command;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void addLayerDiffId(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest diffId) {
        rootfs.diff_ids.add(diffId);
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<DescriptorDigest> getDiffIds() {
        return rootfs.diff_ids;
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable List<String> getContainerEnvironment() {
        return config.Env;
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable List<String> getContainerEntrypoint() {
        return config.Entrypoint;
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DescriptorDigest getLayerDiffId( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int index) {
        return rootfs.diff_ids.get(index);
    }
}
