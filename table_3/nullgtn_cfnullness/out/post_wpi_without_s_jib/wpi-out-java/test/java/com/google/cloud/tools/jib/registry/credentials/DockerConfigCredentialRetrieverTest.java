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
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link DockerConfigCredentialRetriever}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DockerConfigCredentialRetrieverTest {

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Authorization mockAuthorization;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelper mockDockerCredentialHelper;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull DockerCredentialHelperFactory mockDockerCredentialHelperFactory;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Path dockerConfigFile;

    @org.checkerframework.dataflow.qual.Impure
    public void setUp() throws URISyntaxException, NonexistentServerUrlDockerCredentialHelperException, NonexistentDockerCredentialHelperException, IOException {
        dockerConfigFile = Paths.get(Resources.getResource("json/dockerconfig.json").toURI());
        Mockito.when(mockDockerCredentialHelper.retrieve()).thenReturn(mockAuthorization);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testRetrieve_nonexistentDockerConfigFile() {
        DockerConfigCredentialRetriever dockerConfigCredentialRetriever = new DockerConfigCredentialRetriever("some registry", Paths.get("fake/path"));
        Assert.assertNull(dockerConfigCredentialRetriever.retrieve());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testRetrieve_hasAuth() {
        DockerConfigCredentialRetriever dockerConfigCredentialRetriever = new DockerConfigCredentialRetriever("some registry", dockerConfigFile, null);
        Authorization authorization = dockerConfigCredentialRetriever.retrieve();
        Assert.assertNotNull(authorization);
        Assert.assertEquals("some auth", authorization.getToken());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testRetrieve_useCredsStore() {
        Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("some credential store")).thenReturn(mockDockerCredentialHelper);
        DockerConfigCredentialRetriever dockerConfigCredentialRetriever = new DockerConfigCredentialRetriever("just registry", dockerConfigFile, mockDockerCredentialHelperFactory);
        Authorization authorization = dockerConfigCredentialRetriever.retrieve();
        Assert.assertNotNull(authorization);
        Assert.assertEquals(mockAuthorization, authorization);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testRetrieve_useCredHelper() {
        Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("another credential helper")).thenReturn(mockDockerCredentialHelper);
        DockerConfigCredentialRetriever dockerConfigCredentialRetriever = new DockerConfigCredentialRetriever("another registry", dockerConfigFile, mockDockerCredentialHelperFactory);
        Authorization authorization = dockerConfigCredentialRetriever.retrieve();
        Assert.assertNotNull(authorization);
        Assert.assertEquals(mockAuthorization, authorization);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testRetrieve_none() {
        DockerConfigCredentialRetriever dockerConfigCredentialRetriever = new DockerConfigCredentialRetriever("unknown registry", dockerConfigFile);
        Assert.assertNull(dockerConfigCredentialRetriever.retrieve());
    }
}
