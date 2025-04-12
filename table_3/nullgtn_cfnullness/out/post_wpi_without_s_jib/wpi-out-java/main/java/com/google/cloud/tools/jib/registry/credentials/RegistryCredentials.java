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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Stores retrieved registry credentials.
 *
 * <p>The credentials are referred to by the registry they are used for.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class RegistryCredentials {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials EMPTY = new RegistryCredentials();

    /**
     * Instantiates with no credentials.
     */
    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials none() {
        return EMPTY;
    }

    /**
     * Instantiates with credentials for a single registry.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials of(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String registry, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialSource, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Authorization authorization) {
        return new RegistryCredentials().store(registry, credentialSource, authorization);
    }

    /**
     * Retrieves credentials for {@code registries} using the credential helpers referred to by {@code
     * credentialHelperSuffixes}.
     *
     * <p>This obtains the registry credentials, not the <a
     * href="https://docs.docker.com/registry/spec/auth/token/">Docker authentication token</a>.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials from(List<String> credentialHelperSuffixes, List<String> registries) throws IOException, NonexistentDockerCredentialHelperException {
        RegistryCredentials registryCredentials = new RegistryCredentials();
        // TODO: These can be done in parallel.
        for (String registry : registries) {
            for (String credentialHelperSuffix : credentialHelperSuffixes) {
                // Attempts to retrieve authorization for the registry using
                // docker-credential-[credentialSource].
                try {
                    registryCredentials.store(registry, "docker-credential-" + credentialHelperSuffix, new DockerCredentialHelper(registry, credentialHelperSuffix).retrieve());
                } catch (NonexistentServerUrlDockerCredentialHelperException ex) {
                    // No authorization is found, so continues on to the next credential helper.
                }
            }
        }
        return registryCredentials;
    }

    /**
     * Instantiates from a credential source and a map of registry credentials.
     *
     * @param credentialSource the source of the credentials, useful for informing users where the
     *     credentials came from
     * @param registryCredentialMap a map from registries to their respective credentials
     */
    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials from(String credentialSource, Map<String, Authorization> registryCredentialMap) {
        RegistryCredentials registryCredentials = new RegistryCredentials();
        for (Map.Entry<String, Authorization> registryCredential : registryCredentialMap.entrySet()) {
            registryCredentials.store(registryCredential.getKey(), credentialSource, registryCredential.getValue());
        }
        return registryCredentials;
    }

    /**
     * Pair of (source of credentials, {@link Authorization}).
     */
    private static class AuthorizationSourcePair {

        /**
         * A string representation of where the credentials were retrieved from. This is useful for
         * letting the user know which credentials were used.
         */
        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialSource;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Authorization authorization;

        @org.checkerframework.dataflow.qual.SideEffectFree
        private AuthorizationSourcePair(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialSource, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Authorization authorization) {
            this.credentialSource = credentialSource;
            this.authorization = authorization;
        }
    }

    /**
     * Maps from registry to the credentials for that registry.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, AuthorizationSourcePair> credentials = new HashMap<>();

    /**
     * Instantiate using {@link #from}. Immutable after instantiation.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    private RegistryCredentials() {
    }

    /**
     * @return {@code true} if there are credentials for {@code registry}; {@code false} otherwise
     */
    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean has(String registry) {
        return credentials.containsKey(registry);
    }

    /**
     * @return the {@code Authorization} retrieved for the {@code registry}, or {@code null} if none
     *     exists
     */
    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Authorization getAuthorization(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String registry) {
        if (credentials.get(registry) == null) {
            return null;
        }
        return credentials.get(registry).authorization;
    }

    /**
     * @return the name of the credential helper used to retrieve authorization for the {@code
     *     registry}, or {@code null} if none exists
     */
    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getCredentialSource(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String registry) {
        if (credentials.get(registry) == null) {
            return null;
        }
        return credentials.get(registry).credentialSource;
    }

    /**
     * Only to be called in static initializers.
     */
    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryCredentials store(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String registry, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialSource, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Authorization authorization) {
        credentials.put(registry, new AuthorizationSourcePair(credentialSource, authorization));
        return this;
    }
}
