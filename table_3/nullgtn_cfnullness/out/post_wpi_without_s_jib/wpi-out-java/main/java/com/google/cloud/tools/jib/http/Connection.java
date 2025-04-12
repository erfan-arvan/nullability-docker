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
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import javax.annotation.Nullable;
import org.apache.http.NoHttpResponseException;

/**
 * Sends an HTTP {@link Request} and stores the {@link Response}.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * try (Connection connection = new Connection(url)) {
 *   Response response = connection.get(request);
 *   // ... process the response
 * }
 * }</pre>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Connection implements Closeable {

    /**
     * Do not use {@link NetHttpTransport}. It does not process response errors properly. A new {@link
     * ApacheHttpTransport} needs to be created for each connection because otherwise HTTP connection
     * persistence causes the connection to throw {@link NoHttpResponseException}.
     *
     * @see <a
     *     href="https://github.com/google/google-http-java-client/issues/39">https://github.com/google/google-http-java-client/issues/39</a>
     */
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull HttpRequestFactory requestFactory = new ApacheHttpTransport().createRequestFactory();

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull HttpResponse httpResponse;

    /**
     * The URL to send the request to.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull GenericUrl url;

    /**
     * Make sure to wrap with a try-with-resource to ensure that the connection is closed after usage.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public Connection(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL url) {
        this.url = new GenericUrl(url);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void close(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Connection this) throws IOException {
        if (httpResponse == null) {
            return;
        }
        httpResponse.disconnect();
    }

    /**
     * Sends the request with method GET.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response get(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request) throws IOException {
        return send(HttpMethods.GET, request);
    }

    /**
     * Sends the request with method POST.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response post(Request request) throws IOException {
        return send(HttpMethods.POST, request);
    }

    /**
     * Sends the request with method PUT.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response put(Request request) throws IOException {
        return send(HttpMethods.PUT, request);
    }

    /**
     * Sends the request.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response send(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String httpMethod, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request) throws IOException {
        httpResponse = requestFactory.buildRequest(httpMethod, url, request.getHttpContent()).setHeaders(request.getHeaders()).execute();
        return new Response(httpResponse);
    }
}
