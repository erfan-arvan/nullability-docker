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
package com.google.cloud.tools.jib.http;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.cloud.tools.jib.blob.Blobs;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for {@link Connection}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ConnectionTest {

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull HttpRequestFactory mockHttpRequestFactory;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull HttpRequest mockHttpRequest;

    private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull HttpResponse mockHttpResponse;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ArgumentCaptor<HttpHeaders> httpHeadersArgumentCaptor = ArgumentCaptor.forClass(HttpHeaders.class);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ArgumentCaptor<BlobHttpContent> blobHttpContentArgumentCaptor = ArgumentCaptor.forClass(BlobHttpContent.class);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull GenericUrl fakeUrl = new GenericUrl("http://crepecake/fake/url");

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Request fakeRequest;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Connection testConnection = new Connection(fakeUrl.toURL());

    @org.checkerframework.dataflow.qual.Impure
    public void setUpMocksAndFakes() throws IOException {
        fakeRequest = Request.builder().setAccept(Arrays.asList("fake.accept", "another.fake.accept")).setUserAgent("fake user agent").setBody(new BlobHttpContent(Blobs.from("crepecake"), "fake.content.type")).setAuthorization(Authorizations.withBasicCredentials("fake-username", "fake-secret")).build();
        Mockito.when(mockHttpRequestFactory.buildRequest(Mockito.any(String.class), Mockito.eq(fakeUrl), Mockito.any(BlobHttpContent.class))).thenReturn(mockHttpRequest);
        Mockito.when(mockHttpRequest.setHeaders(Mockito.any(HttpHeaders.class))).thenReturn(mockHttpRequest);
        Mockito.when(mockHttpRequest.execute()).thenReturn(mockHttpResponse);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGet() throws IOException {
        testSend(HttpMethods.GET, Connection::get);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testPost() throws IOException {
        testSend(HttpMethods.POST, Connection::post);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testPut() throws IOException {
        testSend(HttpMethods.PUT, Connection::put);
    }

    private interface SendFunction {

        @org.checkerframework.dataflow.qual.Pure
        Response send(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Connection connection, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request request) throws IOException;
    }

    @org.checkerframework.dataflow.qual.Impure
    private void testSend(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String httpMethod, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SendFunction sendFunction) throws IOException {
        try (Connection connection = testConnection) {
            sendFunction.send(connection, fakeRequest);
        }
        Mockito.verify(mockHttpRequest).setHeaders(httpHeadersArgumentCaptor.capture());
        Mockito.verify(mockHttpResponse).disconnect();
        Assert.assertEquals("fake.accept,another.fake.accept", httpHeadersArgumentCaptor.getValue().getAccept());
        Assert.assertEquals("fake user agent", httpHeadersArgumentCaptor.getValue().getUserAgent());
        // Base64 representation of "fake-username:fake-secret"
        Assert.assertEquals("Basic ZmFrZS11c2VybmFtZTpmYWtlLXNlY3JldA==", httpHeadersArgumentCaptor.getValue().getAuthorization());
        Mockito.verify(mockHttpRequestFactory).buildRequest(Mockito.eq(httpMethod), Mockito.eq(fakeUrl), blobHttpContentArgumentCaptor.capture());
        Assert.assertEquals("fake.content.type", blobHttpContentArgumentCaptor.getValue().getType());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        blobHttpContentArgumentCaptor.getValue().writeTo(byteArrayOutputStream);
        Assert.assertEquals("crepecake", new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
    }
}
