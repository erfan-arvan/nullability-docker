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

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;
import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.Blobs;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.image.json.ManifestTemplate;
import com.google.cloud.tools.jib.image.json.V22ManifestTemplate;
import java.io.IOException;
import java.security.DigestException;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * Integration tests for {@link ManifestPusher}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ManifestPusherIntegrationTest {

    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LocalRegistry localRegistry = new LocalRegistry(5000);

    @org.checkerframework.dataflow.qual.Impure
    public void testPush_missingBlobs() throws IOException, RegistryException {
        RegistryClient registryClient = new RegistryClient(null, "gcr.io", "distroless/java");
        ManifestTemplate manifestTemplate = registryClient.pullManifest("latest");
        registryClient = new RegistryClient(null, "localhost:5000", "busybox");
        try {
            registryClient.pushManifest((V22ManifestTemplate) manifestTemplate, "latest");
            Assert.fail("Pushing manifest without its BLOBs should fail");
        } catch (RegistryErrorException ex) {
            HttpResponseException httpResponseException = (HttpResponseException) ex.getCause();
            Assert.assertEquals(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, httpResponseException.getStatusCode());
        }
    }

    /**
     * Tests manifest pushing. This test is a comprehensive test of push and pull.
     */
    @org.checkerframework.dataflow.qual.Impure
    public void testPush() throws DigestException, IOException, RegistryException {
        Blob testLayerBlob = Blobs.from("crepecake");
        // Known digest for 'crepecake'
        DescriptorDigest testLayerBlobDigest = DescriptorDigest.fromHash("52a9e4d4ba4333ce593707f98564fee1e6d898db0d3602408c0b2a6a424d357c");
        Blob testContainerConfigurationBlob = Blobs.from("12345");
        DescriptorDigest testContainerConfigurationBlobDigest = DescriptorDigest.fromHash("5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5");
        // Creates a valid image manifest.
        V22ManifestTemplate expectedManifestTemplate = new V22ManifestTemplate();
        expectedManifestTemplate.addLayer(9, testLayerBlobDigest);
        expectedManifestTemplate.setContainerConfiguration(5, testContainerConfigurationBlobDigest);
        // Pushes the BLOBs.
        RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "testimage");
        Assert.assertFalse(registryClient.pushBlob(testLayerBlobDigest, testLayerBlob));
        Assert.assertFalse(registryClient.pushBlob(testContainerConfigurationBlobDigest, testContainerConfigurationBlob));
        // Pushes the manifest.
        registryClient.pushManifest(expectedManifestTemplate, "latest");
        // Pulls the manifest.
        V22ManifestTemplate manifestTemplate = registryClient.pullManifest("latest", V22ManifestTemplate.class);
        Assert.assertEquals(1, manifestTemplate.getLayers().size());
        Assert.assertEquals(testLayerBlobDigest, manifestTemplate.getLayers().get(0).getDigest());
        Assert.assertEquals(testContainerConfigurationBlobDigest, manifestTemplate.getContainerConfiguration().getDigest());
    }
}
