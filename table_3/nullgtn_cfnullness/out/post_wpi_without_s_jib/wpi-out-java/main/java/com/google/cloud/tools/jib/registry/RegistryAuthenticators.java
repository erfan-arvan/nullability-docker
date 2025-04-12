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
package com.google.cloud.tools.jib.registry;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.annotation.Nullable;

/**
 * Static initializers for {@link RegistryAuthenticator}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public abstract class RegistryAuthenticators {

    @org.checkerframework.dataflow.qual.SideEffectFree
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryAuthenticator forDockerHub(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String repository) throws RegistryAuthenticationFailedException {
        try {
            return new RegistryAuthenticator("https://auth.docker.io/token", "registry.docker.io", repository);
        } catch (MalformedURLException ex) {
            throw new RegistryAuthenticationFailedException(ex);
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryAuthenticator forOther(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String serverUrl, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String repository) throws RegistryAuthenticationFailedException, IOException, RegistryException {
        try {
            RegistryClient registryClient = new RegistryClient(null, serverUrl, repository);
            return registryClient.getRegistryAuthenticator();
        } catch (MalformedURLException ex) {
            throw new RegistryAuthenticationFailedException(ex);
        }
    }
}
