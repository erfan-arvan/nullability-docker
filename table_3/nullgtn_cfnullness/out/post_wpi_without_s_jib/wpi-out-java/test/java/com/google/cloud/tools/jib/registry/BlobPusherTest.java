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
import com.google.cloud.tools.jib.http.BlobHttpContent;
import com.google.cloud.tools.jib.http.Response;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestException;
import java.util.Arrays;
import java.util.Collections;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link BlobPusher}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class BlobPusherTest {

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Blob mockBlob;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull URL mockURL;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DescriptorDigest fakeDescriptorDigest;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull BlobPusher testBlobPusher;

    @org.checkerframework.dataflow.qual.Impure
    public void setUpFakes() throws DigestException {
        fakeDescriptorDigest = DescriptorDigest.fromHash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        testBlobPusher = new BlobPusher(new RegistryEndpointProperties("someServerUrl", "someImageName"), fakeDescriptorDigest, mockBlob);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_getContent() {
        Assert.assertNull(testBlobPusher.initializer().getContent());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGetAccept() {
        Assert.assertEquals(0, testBlobPusher.initializer().getAccept().size());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_handleResponse_created() throws IOException, RegistryException {
        Response mockResponse = Mockito.mock(Response.class);
        // Created
        Mockito.when(mockResponse.getStatusCode()).thenReturn(201);
        Assert.assertNull(testBlobPusher.initializer().handleResponse(mockResponse));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_handleResponse_accepted() throws IOException, RegistryException {
        Response mockResponse = Mockito.mock(Response.class);
        // Accepted
        Mockito.when(mockResponse.getStatusCode()).thenReturn(202);
        Mockito.when(mockResponse.getHeader("Location")).thenReturn(Collections.singletonList("location"));
        Assert.assertEquals("location", testBlobPusher.initializer().handleResponse(mockResponse));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_handleResponse_accepted_multipleLocations() throws IOException, RegistryException {
        Response mockResponse = Mockito.mock(Response.class);
        // Accepted
        Mockito.when(mockResponse.getStatusCode()).thenReturn(202);
        Mockito.when(mockResponse.getHeader("Location")).thenReturn(Arrays.asList("location1", "location2"));
        try {
            testBlobPusher.initializer().handleResponse(mockResponse);
            Assert.fail("Multiple 'Location' headers should be a registry error");
        } catch (RegistryErrorException ex) {
            Assert.assertThat(ex.getMessage(), CoreMatchers.containsString("Expected 1 'Location' header, but found 2"));
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_handleResponse_unrecognized() throws IOException, RegistryException {
        Response mockResponse = Mockito.mock(Response.class);
        // Unrecognized
        Mockito.when(mockResponse.getStatusCode()).thenReturn(-1);
        try {
            testBlobPusher.initializer().handleResponse(mockResponse);
            Assert.fail("Multiple 'Location' headers should be a registry error");
        } catch (RegistryErrorException ex) {
            Assert.assertThat(ex.getMessage(), CoreMatchers.containsString("Received unrecognized status code -1"));
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_getApiRoute() throws MalformedURLException {
        Assert.assertEquals(new URL("http://someApiBase/someImageName/blobs/uploads/?mount=" + fakeDescriptorDigest), testBlobPusher.initializer().getApiRoute("http://someApiBase/"));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_getHttpMethod() {
        Assert.assertEquals("POST", testBlobPusher.initializer().getHttpMethod());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer_getActionDescription() {
        Assert.assertEquals("push BLOB for someServerUrl/someImageName with digest " + fakeDescriptorDigest, testBlobPusher.initializer().getActionDescription());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testWriter_getContent() throws IOException {
        BlobHttpContent body = testBlobPusher.writer(mockURL).getContent();
        Assert.assertNotNull(body);
        Assert.assertEquals("application/octet-stream", body.getType());
        body.writeTo(ByteStreams.nullOutputStream());
        Mockito.verify(mockBlob).writeTo(ByteStreams.nullOutputStream());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testWriter_GetAccept() {
        Assert.assertEquals(0, testBlobPusher.writer(mockURL).getAccept().size());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testWriter_handleResponse() throws IOException, RegistryException {
        Response mockResponse = Mockito.mock(Response.class);
        Mockito.when(mockResponse.getHeader("Location")).thenReturn(Collections.singletonList("location"));
        Assert.assertEquals("location", testBlobPusher.writer(mockURL).handleResponse(mockResponse));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testWriter_getApiRoute() throws MalformedURLException {
        URL fakeUrl = new URL("http://someurl");
        Assert.assertEquals(fakeUrl, testBlobPusher.writer(fakeUrl).getApiRoute(""));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testWriter_getHttpMethod() {
        Assert.assertEquals("PATCH", testBlobPusher.writer(mockURL).getHttpMethod());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testWriter_getActionDescription() {
        Assert.assertEquals("push BLOB for someServerUrl/someImageName with digest " + fakeDescriptorDigest, testBlobPusher.writer(mockURL).getActionDescription());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCommitter_getContent() {
        Assert.assertNull(testBlobPusher.committer(mockURL).getContent());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCommitter_GetAccept() {
        Assert.assertEquals(0, testBlobPusher.committer(mockURL).getAccept().size());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCommitter_handleResponse() throws IOException, RegistryException {
        Assert.assertNull(testBlobPusher.committer(mockURL).handleResponse(Mockito.mock(Response.class)));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCommitter_getApiRoute() throws MalformedURLException {
        Assert.assertEquals(new URL("https://someurl?somequery=somevalue&digest=" + fakeDescriptorDigest), testBlobPusher.committer(new URL("https://someurl?somequery=somevalue")).getApiRoute(""));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCommitter_getHttpMethod() {
        Assert.assertEquals("PUT", testBlobPusher.committer(mockURL).getHttpMethod());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCommitter_getActionDescription() {
        Assert.assertEquals("push BLOB for someServerUrl/someImageName with digest " + fakeDescriptorDigest, testBlobPusher.committer(mockURL).getActionDescription());
    }
}
