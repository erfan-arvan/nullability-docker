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
import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.registry.credentials.DockerConfigCredentialRetriever;
import com.google.cloud.tools.jib.registry.credentials.DockerCredentialHelperFactory;
import com.google.cloud.tools.jib.registry.credentials.NonexistentDockerCredentialHelperException;
import com.google.cloud.tools.jib.registry.credentials.NonexistentServerUrlDockerCredentialHelperException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

/**
 * Attempts to retrieve registry credentials.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class RetrieveRegistryCredentialsStep implements Callable<Authorization> {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DESCRIPTION = "Retrieving registry credentials for %s";

    /**
     * Defines common credential helpers to use as defaults. Maps from registry suffix to credential
     * helper suffix.
     */
    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImmutableMap<String, String> COMMON_CREDENTIAL_HELPERS = ImmutableMap.of("gcr.io", "gcr", "amazonaws.com", "ecr-login");

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String registry;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelperFactory dockerCredentialHelperFactory;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerConfigCredentialRetriever dockerConfigCredentialRetriever;

    @org.checkerframework.dataflow.qual.SideEffectFree
    RetrieveRegistryCredentialsStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String registry) {
        this(buildConfiguration, registry, new DockerCredentialHelperFactory(registry), new DockerConfigCredentialRetriever(registry));
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    RetrieveRegistryCredentialsStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String registry, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DockerCredentialHelperFactory dockerCredentialHelperFactory, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DockerConfigCredentialRetriever dockerConfigCredentialRetriever) {
        this.buildConfiguration = buildConfiguration;
        this.registry = registry;
        this.dockerCredentialHelperFactory = dockerCredentialHelperFactory;
        this.dockerConfigCredentialRetriever = dockerConfigCredentialRetriever;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Authorization call(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RetrieveRegistryCredentialsStep this) throws IOException, NonexistentDockerCredentialHelperException {
        try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), String.format(DESCRIPTION, buildConfiguration.getTargetRegistry()))) {
            // Tries to get registry credentials from Docker credential helpers.
            for (String credentialHelperSuffix : buildConfiguration.getCredentialHelperNames()) {
                Authorization authorization = retrieveFromCredentialHelper(credentialHelperSuffix);
                if (authorization != null) {
                    return authorization;
                }
            }
            // Tries to get registry credentials from known registry credentials.
            String credentialSource = buildConfiguration.getKnownRegistryCredentials().getCredentialSource(registry);
            if (credentialSource != null) {
                logGotCredentialsFrom(credentialSource);
                return buildConfiguration.getKnownRegistryCredentials().getAuthorization(registry);
            }
            // Tries to get registry credentials from the Docker config.
            Authorization dockerConfigAuthorization = dockerConfigCredentialRetriever.retrieve();
            if (dockerConfigAuthorization != null) {
                buildConfiguration.getBuildLogger().info("Using credentials from Docker config for " + registry);
                return dockerConfigAuthorization;
            }
            // Tries to infer common credential helpers for known registries.
            for (String registrySuffix : COMMON_CREDENTIAL_HELPERS.keySet()) {
                if (registry.endsWith(registrySuffix)) {
                    try {
                        String commonCredentialHelper = COMMON_CREDENTIAL_HELPERS.get(registrySuffix);
                        if (commonCredentialHelper == null) {
                            throw new IllegalStateException("No COMMON_CREDENTIAL_HELPERS should be null");
                        }
                        Authorization authorization = retrieveFromCredentialHelper(commonCredentialHelper);
                        if (authorization != null) {
                            return authorization;
                        }
                    } catch (NonexistentDockerCredentialHelperException ex) {
                        if (ex.getMessage() != null) {
                            // Warns the user that the specified (or inferred) credential helper is not on the
                            // system.
                            buildConfiguration.getBuildLogger().warn(ex.getMessage());
                        }
                    }
                }
            }
            /*
       * If no credentials found, give an info (not warning because in most cases, the base image is
       * public and does not need extra credentials) and return null.
       */
            buildConfiguration.getBuildLogger().info("No credentials could be retrieved for registry " + registry);
            return null;
        }
    }

    /**
     * Attempts to retrieve authorization for the registry using {@code
     * docker-credential-[credentialHelperSuffix]}.
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Authorization retrieveFromCredentialHelper(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialHelperSuffix) throws NonexistentDockerCredentialHelperException, IOException {
        buildConfiguration.getBuildLogger().info("Checking credentials from docker-credential-" + credentialHelperSuffix);
        try {
            Authorization authorization = dockerCredentialHelperFactory.withCredentialHelperSuffix(credentialHelperSuffix).retrieve();
            logGotCredentialsFrom("docker-credential-" + credentialHelperSuffix);
            return authorization;
        } catch (NonexistentServerUrlDockerCredentialHelperException ex) {
            buildConfiguration.getBuildLogger().info("No credentials for " + registry + " in docker-credential-" + credentialHelperSuffix);
            return null;
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    private void logGotCredentialsFrom(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialSource) {
        buildConfiguration.getBuildLogger().info("Using " + credentialSource + " for " + registry);
    }
}
