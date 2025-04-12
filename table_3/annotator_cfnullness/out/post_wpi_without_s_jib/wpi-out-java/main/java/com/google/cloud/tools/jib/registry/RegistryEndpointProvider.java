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

import com.google.api.client.http.HttpResponseException;
import com.google.cloud.tools.jib.http.BlobHttpContent;
import com.google.cloud.tools.jib.http.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Provides implementations for a registry endpoint. Implementations should be immutable.
 *
 * @param <T> the type returned from handling the endpoint response
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
interface RegistryEndpointProvider<T> {

    /**
     * @return the HTTP method to send the request with
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getHttpMethod();

    /**
     * @param apiRouteBase the registry's base URL (for example, {@code https://gcr.io/v2/})
     * @return the registry endpoint URL
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL getApiRoute(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiRouteBase) throws MalformedURLException;

    /**
     * @return the {@link BlobHttpContent} to send as the request body
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable BlobHttpContent getContent();

    /**
     * @return a list of MIME types to pass as an HTTP {@code Accept} header
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getAccept();

    /**
     * Handles the response specific to the registry action.
     */
    @org.checkerframework.dataflow.qual.Impure
    T handleResponse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) throws IOException, RegistryException;

    /**
     * Handles an {@link HttpResponseException} that occurs.
     *
     * @param httpResponseException the {@link HttpResponseException} to handle
     * @throws HttpResponseException {@code httpResponseException} if {@code httpResponseException}
     *     could not be handled
     */
    @org.checkerframework.dataflow.qual.Impure
    default T handleHttpResponseException(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull HttpResponseException httpResponseException) throws HttpResponseException, RegistryErrorException {
        throw httpResponseException;
    }

    /**
     * @return a description of the registry action performed, used in error messages to describe the
     *     action that failed
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getActionDescription();
}
