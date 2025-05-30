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
package com.google.cloud.tools.jib.registry.credentials;

import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.http.Authorizations;
import com.google.cloud.tools.jib.json.JsonTemplateMapper;
import com.google.cloud.tools.jib.registry.credentials.json.DockerConfigTemplate;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nullable;

/**
 * Retrieves registry credentials from the Docker config.
 *
 * <p>The credentials are searched in the following order (stopping when credentials are found):
 *
 * <ol>
 *   <li>If there is an {@code auth} defined for a registry.
 *   <li>Using the {@code credsStore} credential helper, if available.
 *   <li>Using the credential helper from {@code credHelpers}, if available.
 * </ol>
 *
 * @see <a
 *     href="https://docs.docker.com/engine/reference/commandline/login/">https://docs.docker.com/engine/reference/commandline/login/</a>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DockerConfigCredentialRetriever {

    /**
     * @see <a
     *     href="https://docs.docker.com/engine/reference/commandline/login/#privileged-user-requirement">https://docs.docker.com/engine/reference/commandline/login/#privileged-user-requirement</a>
     */
    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path DOCKER_CONFIG_FILE = Paths.get(System.getProperty("user.home")).resolve(".docker").resolve("config.json");

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String registry;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Path dockerConfigFile;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelperFactory dockerCredentialHelperFactory;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public DockerConfigCredentialRetriever(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String registry) {
        this(registry, DOCKER_CONFIG_FILE);
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    DockerConfigCredentialRetriever(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String registry, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Path dockerConfigFile) {
        this.registry = registry;
        this.dockerConfigFile = dockerConfigFile;
        this.dockerCredentialHelperFactory = new DockerCredentialHelperFactory(registry);
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    DockerConfigCredentialRetriever(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String registry, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Path dockerConfigFile, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable DockerCredentialHelperFactory dockerCredentialHelperFactory) {
        this.registry = registry;
        this.dockerConfigFile = dockerConfigFile;
        this.dockerCredentialHelperFactory = dockerCredentialHelperFactory;
    }

    /**
     * @return {@link Authorization} found for {@code registry}, or {@code null} if not found
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Authorization retrieve() {
        DockerConfigTemplate dockerConfigTemplate = loadDockerConfigTemplate();
        if (dockerConfigTemplate == null) {
            return null;
        }
        String auth = dockerConfigTemplate.getAuthFor(registry);
        if (auth != null) {
            return Authorizations.withBasicToken(auth);
        }
        String credentialHelperSuffix = dockerConfigTemplate.getCredentialHelperFor(registry);
        if (credentialHelperSuffix != null) {
            try {
                return dockerCredentialHelperFactory.withCredentialHelperSuffix(credentialHelperSuffix).retrieve();
            } catch (IOException | NonexistentServerUrlDockerCredentialHelperException | NonexistentDockerCredentialHelperException ex) {
                // Ignores credential helper retrieval exceptions.
            }
        }
        return null;
    }

    /**
     * Loads the Docker config JSON and caches it.
     */
    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DockerConfigTemplate loadDockerConfigTemplate() {
        // Loads the Docker config.
        if (!Files.exists(dockerConfigFile)) {
            return null;
        }
        try {
            return JsonTemplateMapper.readJsonFromFile(dockerConfigFile, DockerConfigTemplate.class);
        } catch (IOException ex) {
            // TODO: Throw some exception about not being able to parse Docker config.
            return null;
        }
    }
}
