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

import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.registry.credentials.DockerConfigCredentialRetriever;
import com.google.cloud.tools.jib.registry.credentials.DockerCredentialHelper;
import com.google.cloud.tools.jib.registry.credentials.DockerCredentialHelperFactory;
import com.google.cloud.tools.jib.registry.credentials.NonexistentDockerCredentialHelperException;
import com.google.cloud.tools.jib.registry.credentials.NonexistentServerUrlDockerCredentialHelperException;
import com.google.cloud.tools.jib.registry.credentials.RegistryCredentials;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link RetrieveRegistryCredentialsStep}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class RetrieveRegistryCredentialsStepTest {

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull BuildConfiguration mockBuildConfiguration;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull BuildLogger mockBuildLogger;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelperFactory mockDockerCredentialHelperFactory;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelper mockDockerCredentialHelper;

    /**
     * A {@link DockerCredentialHelper} that throws {@link
     * NonexistentServerUrlDockerCredentialHelperException}.
     */
    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelper mockNonexistentServerUrlDockerCredentialHelper;

    /**
     * A {@link DockerCredentialHelper} that throws {@link
     * NonexistentDockerCredentialHelperException}.
     */
    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelper mockNonexistentDockerCredentialHelper;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Authorization mockAuthorization;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerConfigCredentialRetriever mockDockerConfigCredentialRetriever;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull NonexistentServerUrlDockerCredentialHelperException mockNonexistentServerUrlDockerCredentialHelperException;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull NonexistentDockerCredentialHelperException mockNonexistentDockerCredentialHelperException;

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String fakeTargetRegistry = "someRegistry";

    @org.checkerframework.dataflow.qual.Impure
    public void setUpMocks() throws NonexistentServerUrlDockerCredentialHelperException, NonexistentDockerCredentialHelperException, IOException {
        Mockito.when(mockBuildConfiguration.getBuildLogger()).thenReturn(mockBuildLogger);
        Mockito.when(mockDockerCredentialHelper.retrieve()).thenReturn(mockAuthorization);
        Mockito.when(mockNonexistentServerUrlDockerCredentialHelper.retrieve()).thenThrow(mockNonexistentServerUrlDockerCredentialHelperException);
        Mockito.when(mockNonexistentDockerCredentialHelper.retrieve()).thenThrow(mockNonexistentDockerCredentialHelperException);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCall_useCredentialHelper() throws IOException, NonexistentDockerCredentialHelperException, NonexistentServerUrlDockerCredentialHelperException {
        Mockito.when(mockBuildConfiguration.getCredentialHelperNames()).thenReturn(Arrays.asList("someCredentialHelper", "someOtherCredentialHelper"));
        Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("someCredentialHelper")).thenReturn(mockNonexistentServerUrlDockerCredentialHelper);
        Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("someOtherCredentialHelper")).thenReturn(mockDockerCredentialHelper);
        Assert.assertEquals(mockAuthorization, makeRetrieveRegistryCredentialsStep(fakeTargetRegistry).call());
        Mockito.verify(mockBuildLogger).info("Using docker-credential-someOtherCredentialHelper for " + fakeTargetRegistry);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCall_useKnownRegistryCredentials() throws IOException, NonexistentDockerCredentialHelperException, NonexistentServerUrlDockerCredentialHelperException {
        // Has no credential helpers be defined.
        Mockito.when(mockBuildConfiguration.getCredentialHelperNames()).thenReturn(Collections.emptyList());
        Mockito.when(mockBuildConfiguration.getKnownRegistryCredentials()).thenReturn(RegistryCredentials.of(fakeTargetRegistry, "credentialSource", mockAuthorization));
        Assert.assertEquals(mockAuthorization, makeRetrieveRegistryCredentialsStep(fakeTargetRegistry).call());
        Mockito.verify(mockBuildLogger).info("Using credentialSource for " + fakeTargetRegistry);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCall_useDockerConfig() throws IOException, NonexistentDockerCredentialHelperException, NonexistentServerUrlDockerCredentialHelperException {
        // Has no credential helpers be defined.
        Mockito.when(mockBuildConfiguration.getCredentialHelperNames()).thenReturn(Collections.emptyList());
        // Has known credentials be empty.
        Mockito.when(mockBuildConfiguration.getKnownRegistryCredentials()).thenReturn(RegistryCredentials.none());
        Mockito.when(mockDockerConfigCredentialRetriever.retrieve()).thenReturn(mockAuthorization);
        Assert.assertEquals(mockAuthorization, makeRetrieveRegistryCredentialsStep(fakeTargetRegistry).call());
        Mockito.verify(mockBuildLogger).info("Using credentials from Docker config for " + fakeTargetRegistry);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCall_inferCommonCredentialHelpers() throws IOException, NonexistentDockerCredentialHelperException, NonexistentServerUrlDockerCredentialHelperException {
        // Has no credential helpers be defined.
        Mockito.when(mockBuildConfiguration.getCredentialHelperNames()).thenReturn(Collections.emptyList());
        // Has known credentials be empty.
        Mockito.when(mockBuildConfiguration.getKnownRegistryCredentials()).thenReturn(RegistryCredentials.none());
        // Has no Docker config.
        Mockito.when(mockDockerConfigCredentialRetriever.retrieve()).thenReturn(null);
        Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("gcr")).thenReturn(mockDockerCredentialHelper);
        Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("ecr-login")).thenReturn(mockNonexistentDockerCredentialHelper);
        Assert.assertEquals(mockAuthorization, makeRetrieveRegistryCredentialsStep("something.gcr.io").call());
        Mockito.verify(mockBuildLogger).info("Using docker-credential-gcr for something.gcr.io");
        Mockito.when(mockNonexistentDockerCredentialHelperException.getMessage()).thenReturn("warning");
        Assert.assertEquals(null, makeRetrieveRegistryCredentialsStep("something.amazonaws.com").call());
        Mockito.verify(mockBuildLogger).warn("warning");
    }

    /**
     * Creates a fake {@link RetrieveRegistryCredentialsStep} for {@code registry}.
     */
    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RetrieveRegistryCredentialsStep makeRetrieveRegistryCredentialsStep(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String registry) {
        Mockito.when(mockBuildConfiguration.getTargetRegistry()).thenReturn(fakeTargetRegistry);
        return new RetrieveRegistryCredentialsStep(mockBuildConfiguration, registry, mockDockerCredentialHelperFactory, mockDockerConfigCredentialRetriever);
    }
}
