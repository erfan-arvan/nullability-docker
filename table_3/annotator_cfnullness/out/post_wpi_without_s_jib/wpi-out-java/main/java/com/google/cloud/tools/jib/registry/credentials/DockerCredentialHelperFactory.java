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

/**
 * Factory class for constructing {@link DockerCredentialHelper}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DockerCredentialHelperFactory {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String registry;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public DockerCredentialHelperFactory(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String registry) {
        this.registry = registry;
    }

    /**
     * @return a {@link DockerCredentialHelper} that uses the {@code
     *     docker-credential-[credentialHelperSuffix]} command
     */
    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DockerCredentialHelper withCredentialHelperSuffix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialHelperSuffix) {
        return new DockerCredentialHelper(registry, credentialHelperSuffix);
    }
}
