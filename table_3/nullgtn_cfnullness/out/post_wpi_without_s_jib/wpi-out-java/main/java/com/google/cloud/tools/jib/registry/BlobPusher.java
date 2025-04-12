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

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpStatusCodes;
import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.http.BlobHttpContent;
import com.google.cloud.tools.jib.http.Response;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.common.net.MediaType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Pushes an image's BLOB (layer or container configuration).
 *
 * <p>The BLOB is pushed in three stages:
 *
 * <ol>
 *   <li>Initialize - Gets a location back to write the BLOB content to
 *   <li>Write BLOB - Write the BLOB content to the received location
 *   <li>Commit BLOB - Commits the BLOB with its digest
 * </ol>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class BlobPusher {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties registryEndpointProperties;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DescriptorDigest blobDigest;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Blob blob;

    /**
     * Initializes the BLOB upload.
     */
    private class Initializer implements RegistryEndpointProvider<String> {

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable BlobHttpContent getContent(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer this) {
            return null;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getAccept(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer this) {
            return Collections.emptyList();
        }

        /**
         * @return a URL to continue pushing the BLOB to, or {@code null} if the BLOB already exists on
         *     the registry
         */
        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String handleResponse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) throws RegistryErrorException {
            switch(response.getStatusCode()) {
                case HttpStatusCodes.STATUS_CODE_CREATED:
                    // The BLOB exists in the registry.
                    return null;
                case HttpURLConnection.HTTP_ACCEPTED:
                    return extractLocationHeader(response);
                default:
                    throw buildRegistryErrorException("Received unrecognized status code " + response.getStatusCode());
            }
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull URL getApiRoute(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiRouteBase) throws MalformedURLException {
            return new URL(apiRouteBase + registryEndpointProperties.getImageName() + "/blobs/uploads/?mount=" + blobDigest);
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getHttpMethod(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer this) {
            return HttpMethods.POST;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getActionDescription(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer this) {
            return BlobPusher.this.getActionDescription();
        }
    }

    /**
     * Writes the BLOB content to the upload location.
     */
    private class Writer implements RegistryEndpointProvider<String> {

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull URL location;

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobHttpContent getContent(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Writer this) {
            return new BlobHttpContent(blob, MediaType.OCTET_STREAM.toString());
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getAccept(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Writer this) {
            return Collections.emptyList();
        }

        /**
         * @return a URL to continue pushing the BLOB to
         */
        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String handleResponse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Writer this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) throws RegistryException {
            // TODO: Handle 204 No Content
            return extractLocationHeader(response);
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL getApiRoute(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Writer this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiRouteBase) {
            return location;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getHttpMethod(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Writer this) {
            return HttpMethods.PATCH;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getActionDescription(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Writer this) {
            return BlobPusher.this.getActionDescription();
        }

        @org.checkerframework.dataflow.qual.SideEffectFree
        private Writer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL location) {
            this.location = location;
        }
    }

    /**
     * Commits the written BLOB.
     */
    private class Committer implements RegistryEndpointProvider<Void> {

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull URL location;

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable BlobHttpContent getContent(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Committer this) {
            return null;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getAccept(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Committer this) {
            return Collections.emptyList();
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Void handleResponse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Committer this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) {
            return null;
        }

        /**
         * @return {@code location} with query parameter 'digest' set to the BLOB's digest
         */
        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull URL getApiRoute(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Committer this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiRouteBase) {
            return new GenericUrl(location).set("digest", blobDigest).toURL();
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getHttpMethod(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Committer this) {
            return HttpMethods.PUT;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getActionDescription(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Committer this) {
            return BlobPusher.this.getActionDescription();
        }

        @org.checkerframework.dataflow.qual.SideEffectFree
        private Committer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL location) {
            this.location = location;
        }
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    BlobPusher(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties registryEndpointProperties, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest blobDigest, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Blob blob) {
        this.registryEndpointProperties = registryEndpointProperties;
        this.blobDigest = blobDigest;
        this.blob = blob;
    }

    /**
     * @return a {@link RegistryEndpointProvider} for initializing the BLOB upload with an existence
     *     check
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProvider<String> initializer() {
        return new Initializer();
    }

    /**
     * @param location the upload URL
     * @return a {@link RegistryEndpointProvider} for writing the BLOB to an upload location
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProvider<String> writer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL location) {
        return new Writer(location);
    }

    /**
     * @param location the upload URL
     * @return a {@link RegistryEndpointProvider} for committing the written BLOB with its digest
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProvider<Void> committer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL location) {
        return new Committer(location);
    }

    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryErrorException buildRegistryErrorException(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String reason) {
        RegistryErrorExceptionBuilder registryErrorExceptionBuilder = new RegistryErrorExceptionBuilder(getActionDescription());
        registryErrorExceptionBuilder.addReason(reason);
        return registryErrorExceptionBuilder.build();
    }

    /**
     * @return the common action description for {@link Initializer}, {@link Writer}, and {@link
     *     Committer}
     */
    @org.checkerframework.dataflow.qual.Pure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getActionDescription() {
        return "push BLOB for " + registryEndpointProperties.getServerUrl() + "/" + registryEndpointProperties.getImageName() + " with digest " + blobDigest;
    }

    /**
     * @param response the response to extract the 'Location' header from
     * @return the value of the 'Location' header
     * @throws RegistryErrorException if there was not a single 'Location' header
     */
    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String extractLocationHeader(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) throws RegistryErrorException {
        // Extracts and returns the 'Location' header.
        List<String> locationHeaders = response.getHeader("Location");
        if (locationHeaders.size() != 1) {
            throw buildRegistryErrorException("Expected 1 'Location' header, but found " + locationHeaders.size());
        }
        return locationHeaders.get(0);
    }
}
