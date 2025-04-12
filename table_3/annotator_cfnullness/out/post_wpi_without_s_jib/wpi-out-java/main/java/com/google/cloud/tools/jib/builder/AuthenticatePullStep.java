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
import com.google.cloud.tools.jib.registry.RegistryAuthenticationFailedException;
import com.google.cloud.tools.jib.registry.RegistryAuthenticator;
import com.google.cloud.tools.jib.registry.RegistryAuthenticators;
import com.google.cloud.tools.jib.registry.RegistryException;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Authenticates pull from the base image registry using Docker Token Authentication.
 *
 * @see <a
 *     href="https://docs.docker.com/registry/spec/auth/token/">https://docs.docker.com/registry/spec/auth/token/</a>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class AuthenticatePullStep implements Callable<Authorization> {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DESCRIPTION = "Authenticating pull from %s";

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListenableFuture<Authorization> registryCredentialsFuture;

    @org.checkerframework.dataflow.qual.SideEffectFree
    AuthenticatePullStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildConfiguration buildConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ListenableFuture<Authorization> registryCredentialsFuture) {
        this.buildConfiguration = buildConfiguration;
        this.registryCredentialsFuture = registryCredentialsFuture;
    }

    /**
     * Depends on {@link RetrieveRegistryCredentialsStep}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Authorization call(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticatePullStep this) throws RegistryAuthenticationFailedException, IOException, RegistryException, ExecutionException, InterruptedException {
        try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), String.format(DESCRIPTION, buildConfiguration.getBaseImageRegistry()))) {
            Authorization registryCredentials = NonBlockingFutures.get(registryCredentialsFuture);
            RegistryAuthenticator registryAuthenticator = RegistryAuthenticators.forOther(buildConfiguration.getBaseImageRegistry(), buildConfiguration.getBaseImageRepository());
            if (registryAuthenticator == null) {
                return registryCredentials;
            }
            return registryAuthenticator.setAuthorization(registryCredentials).authenticatePull();
        }
    }
}
