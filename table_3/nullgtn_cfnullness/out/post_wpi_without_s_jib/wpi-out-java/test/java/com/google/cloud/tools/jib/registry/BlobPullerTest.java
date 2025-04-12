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
package com.google.cloud.tools.jib.registry;

import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.Blobs;
import com.google.cloud.tools.jib.hash.CountingDigestOutputStream;
import com.google.cloud.tools.jib.http.Response;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link BlobPuller}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class BlobPullerTest {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties fakeRegistryEndpointProperties = new RegistryEndpointProperties("someServerUrl", "someImageName");

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DescriptorDigest fakeDigest;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ByteArrayOutputStream layerContentOutputStream = new ByteArrayOutputStream();

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CountingDigestOutputStream layerOutputStream = new CountingDigestOutputStream(layerContentOutputStream);

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull BlobPuller testBlobPuller;

    @org.checkerframework.dataflow.qual.Impure
    public void setUpFakes() throws DigestException, IOException {
        fakeDigest = DescriptorDigest.fromHash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        testBlobPuller = new BlobPuller(fakeRegistryEndpointProperties, fakeDigest, layerOutputStream);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testHandleResponse() throws IOException, UnexpectedBlobDigestException {
        Blob testBlob = Blobs.from("some BLOB content");
        DescriptorDigest testBlobDigest = testBlob.writeTo(ByteStreams.nullOutputStream()).getDigest();
        Response mockResponse = Mockito.mock(Response.class);
        Mockito.when(mockResponse.getBody()).thenReturn(testBlob);
        BlobPuller blobPuller = new BlobPuller(fakeRegistryEndpointProperties, testBlobDigest, layerOutputStream);
        blobPuller.handleResponse(mockResponse);
        Assert.assertEquals("some BLOB content", new String(layerContentOutputStream.toByteArray(), StandardCharsets.UTF_8));
        Assert.assertEquals(testBlobDigest, layerOutputStream.toBlobDescriptor().getDigest());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testHandleResponse_unexpectedDigest() throws IOException {
        Blob testBlob = Blobs.from("some BLOB content");
        DescriptorDigest testBlobDigest = testBlob.writeTo(ByteStreams.nullOutputStream()).getDigest();
        Response mockResponse = Mockito.mock(Response.class);
        Mockito.when(mockResponse.getBody()).thenReturn(testBlob);
        try {
            testBlobPuller.handleResponse(mockResponse);
            Assert.fail("Receiving an unexpected digest should fail");
        } catch (UnexpectedBlobDigestException ex) {
            Assert.assertEquals("The pulled BLOB has digest '" + testBlobDigest + "', but the request digest was '" + fakeDigest + "'", ex.getMessage());
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGetApiRoute() throws MalformedURLException {
        Assert.assertEquals(new URL("http://someApiBase/someImageName/blobs/" + fakeDigest), testBlobPuller.getApiRoute("http://someApiBase/"));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGetActionDescription() {
        Assert.assertEquals("pull BLOB for someServerUrl/someImageName with digest " + fakeDigest, testBlobPuller.getActionDescription());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGetHttpMethod() {
        Assert.assertEquals("GET", testBlobPuller.getHttpMethod());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGetContent() {
        Assert.assertNull(testBlobPuller.getContent());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGetAccept() {
        Assert.assertEquals(0, testBlobPuller.getAccept().size());
    }
}
