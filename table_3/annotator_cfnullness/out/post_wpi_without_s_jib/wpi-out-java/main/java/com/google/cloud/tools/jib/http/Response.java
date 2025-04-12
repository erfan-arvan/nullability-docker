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

import com.google.api.client.http.HttpResponse;
import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.Blobs;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.List;

/**
 * Holds an HTTP response.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Response {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull HttpResponse httpResponse;

    @org.checkerframework.dataflow.qual.SideEffectFree
    Response(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * Gets the HTTP status code of the response.
     */
    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getStatusCode() {
        return httpResponse.getStatusCode();
    }

    /**
     * Gets a header in the response.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getHeader(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String headerName) {
        return httpResponse.getHeaders().getHeaderStringValues(headerName);
    }

    /**
     * @return the first {@code Content-Length} header, or {@code -1} if not found
     */
    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long getContentLength() throws NumberFormatException {
        String contentLengthHeader = httpResponse.getHeaders().getFirstHeaderStringValue(HttpHeaders.CONTENT_LENGTH);
        if (contentLengthHeader == null) {
            return -1;
        }
        try {
            return Long.parseLong(contentLengthHeader);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Gets the HTTP response body as a {@link Blob}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Blob getBody() throws IOException {
        return Blobs.from(httpResponse.getContent());
    }
}
