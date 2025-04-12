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

import com.google.cloud.tools.jib.image.ImageReference;
import com.google.cloud.tools.jib.image.json.BuildableManifestTemplate;
import com.google.cloud.tools.jib.image.json.V22ManifestTemplate;
import com.google.cloud.tools.jib.registry.credentials.RegistryCredentials;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Immutable configuration options for the builder process.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class BuildConfiguration {

    public static class Builder {

        // All the parameters below are set to their default values.
        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull ImageReference baseImageReference;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull ImageReference targetImageReference;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> credentialHelperNames = new ArrayList<>();

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials knownRegistryCredentials = RegistryCredentials.none();

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean enableReproducibleBuilds = true;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String mainClass;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> jvmFlags = new ArrayList<>();

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, String> environmentMap = new HashMap<>();

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends BuildableManifestTemplate> targetFormat = V22ManifestTemplate.class;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger;

        @org.checkerframework.dataflow.qual.SideEffectFree
        private Builder(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger) {
            this.buildLogger = buildLogger;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setBaseImage(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageReference imageReference) {
            baseImageReference = imageReference;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setTargetImage(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageReference imageReference) {
            targetImageReference = imageReference;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setCredentialHelperNames(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> credentialHelperNames) {
            if (credentialHelperNames != null) {
                this.credentialHelperNames = credentialHelperNames;
            }
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setKnownRegistryCredentials(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials knownRegistryCredentials) {
            if (knownRegistryCredentials != null) {
                this.knownRegistryCredentials = knownRegistryCredentials;
            }
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setEnableReproducibleBuilds( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isEnabled) {
            enableReproducibleBuilds = isEnabled;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setMainClass(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String mainClass) {
            this.mainClass = mainClass;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setJvmFlags(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> jvmFlags) {
            if (jvmFlags != null) {
                this.jvmFlags = jvmFlags;
            }
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setEnvironment(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, String> environmentMap) {
            if (environmentMap != null) {
                this.environmentMap = environmentMap;
            }
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setTargetFormat(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends BuildableManifestTemplate> targetFormat) {
            this.targetFormat = targetFormat;
            return this;
        }

        /**
         * @return the corresponding build configuration
         */
        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration build() {
            // Validates the parameters.
            List<String> errorMessages = new ArrayList<>();
            if (baseImageReference == null) {
                errorMessages.add("base image is required but not set");
            }
            if (targetImageReference == null) {
                errorMessages.add("target image is required but not set");
            }
            if (mainClass == null) {
                errorMessages.add("main class is required but not set");
            }
            switch(errorMessages.size()) {
                case // No errors
                0:
                    if (baseImageReference == null || targetImageReference == null || mainClass == null) {
                        throw new IllegalStateException("Required fields should not be null");
                    }
                    return new BuildConfiguration(buildLogger, baseImageReference, targetImageReference, credentialHelperNames, knownRegistryCredentials, enableReproducibleBuilds, mainClass, jvmFlags, environmentMap, targetFormat);
                case 1:
                    throw new IllegalStateException(errorMessages.get(0));
                case 2:
                    throw new IllegalStateException(errorMessages.get(0) + " and " + errorMessages.get(1));
                default:
                    // Appends the descriptions in correct grammar.
                    StringBuilder errorMessage = new StringBuilder(errorMessages.get(0));
                    for (int errorMessageIndex = 1; errorMessageIndex < errorMessages.size(); errorMessageIndex++) {
                        if (errorMessageIndex == errorMessages.size() - 1) {
                            errorMessage.append(", and ");
                        } else {
                            errorMessage.append(", ");
                        }
                        errorMessage.append(errorMessages.get(errorMessageIndex));
                    }
                    throw new IllegalStateException(errorMessage.toString());
            }
        }
    }

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageReference baseImageReference;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageReference targetImageReference;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> credentialHelperNames;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials knownRegistryCredentials;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean enableReproducibleBuilds;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String mainClass;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> jvmFlags;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, String> environmentMap;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends BuildableManifestTemplate> targetFormat;

    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder builder(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger) {
        return new Builder(buildLogger);
    }

    /**
     * Instantiate with {@link Builder#build}.
     */
    @org.checkerframework.dataflow.qual.Impure
    private BuildConfiguration(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageReference baseImageReference, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageReference targetImageReference, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> credentialHelperNames, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials knownRegistryCredentials,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean enableReproducibleBuilds, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String mainClass, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> jvmFlags, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, String> environmentMap, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends BuildableManifestTemplate> targetFormat) {
        this.buildLogger = buildLogger;
        this.baseImageReference = baseImageReference;
        this.targetImageReference = targetImageReference;
        this.credentialHelperNames = Collections.unmodifiableList(credentialHelperNames);
        this.knownRegistryCredentials = knownRegistryCredentials;
        this.enableReproducibleBuilds = enableReproducibleBuilds;
        this.mainClass = mainClass;
        this.jvmFlags = Collections.unmodifiableList(jvmFlags);
        this.environmentMap = Collections.unmodifiableMap(environmentMap);
        this.targetFormat = targetFormat;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger getBuildLogger() {
        return buildLogger;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getBaseImageRegistry() {
        return baseImageReference.getRegistry();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getBaseImageRepository() {
        return baseImageReference.getRepository();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getBaseImageTag() {
        return baseImageReference.getTag();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getTargetRegistry() {
        return targetImageReference.getRegistry();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getTargetRepository() {
        return targetImageReference.getRepository();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getTargetTag() {
        return targetImageReference.getTag();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials getKnownRegistryCredentials() {
        return knownRegistryCredentials;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getCredentialHelperNames() {
        return credentialHelperNames;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean getEnableReproducibleBuilds() {
        return enableReproducibleBuilds;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getMainClass() {
        return mainClass;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getJvmFlags() {
        return jvmFlags;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, String> getEnvironment() {
        return environmentMap;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends BuildableManifestTemplate> getTargetFormat() {
        return targetFormat;
    }
}
