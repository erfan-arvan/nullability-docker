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
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;
import com.google.cloud.tools.jib.Timer;
import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.BlobDescriptor;
import com.google.cloud.tools.jib.builder.BuildLogger;
import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.http.Connection;
import com.google.cloud.tools.jib.http.Request;
import com.google.cloud.tools.jib.http.Response;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.image.json.BuildableManifestTemplate;
import com.google.cloud.tools.jib.image.json.ManifestTemplate;
import com.google.cloud.tools.jib.image.json.V21ManifestTemplate;
import com.google.cloud.tools.jib.image.json.V22ManifestTemplate;
import com.google.cloud.tools.jib.json.JsonTemplateMapper;
import com.google.cloud.tools.jib.registry.json.ErrorEntryTemplate;
import com.google.cloud.tools.jib.registry.json.ErrorResponseTemplate;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.NoHttpResponseException;

/**
 * Interfaces with a registry.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class RegistryClient {

    // TODO: Remove
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Timer parentTimer = new Timer(new BuildLogger() {

        public void debug(CharSequence message) {
        }

        public void info(CharSequence message) {
        }

        public void warn(CharSequence message) {
        }

        public void error(CharSequence message) {
        }
    }, "NULL TIMER");

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryClient setTimer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Timer parentTimer) {
        this.parentTimer = parentTimer;
        return this;
    }

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String PROTOCOL = "https";

    private static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String userAgentSuffix;

    // TODO: Inject via a RegistryClientFactory.
    /**
     * Sets a suffix to append to {@code User-Agent} headers.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static void setUserAgentSuffix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String userAgentSuffix) {
        RegistryClient.userAgentSuffix = userAgentSuffix;
    }

    /**
     * Gets the {@code User-Agent} header to send.
     */
    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getUserAgent() {
        String version = RegistryClient.class.getPackage().getImplementationVersion();
        StringBuilder userAgentBuilder = new StringBuilder();
        userAgentBuilder.append("jib");
        if (version != null) {
            userAgentBuilder.append(" ").append(version);
        }
        if (userAgentSuffix != null) {
            userAgentBuilder.append(" ").append(userAgentSuffix);
        }
        return userAgentBuilder.toString();
    }

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Authorization authorization;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties registryEndpointProperties;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public RegistryClient(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Authorization authorization, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String serverUrl, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String imageName) {
        this.authorization = authorization;
        this.registryEndpointProperties = new RegistryEndpointProperties(serverUrl, imageName);
    }

    /**
     * @return the {@link RegistryAuthenticator} to authenticate pulls/pushes with the registry, or
     *     {@code null} if no token authentication is necessary
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryAuthenticator getRegistryAuthenticator() throws IOException, RegistryException {
        // Gets the WWW-Authenticate header (eg. 'WWW-Authenticate: Bearer
        // realm="https://gcr.io/v2/token",service="gcr.io"')
        return callRegistryEndpoint(new AuthenticationMethodRetriever(registryEndpointProperties));
    }

    /**
     * Pulls the image manifest for a specific tag.
     *
     * @param imageTag the tag to pull on
     * @param manifestTemplateClass the specific version of manifest template to pull, or {@link
     *     ManifestTemplate} to pull either {@link V22ManifestTemplate} or {@link V21ManifestTemplate}
     */
    @org.checkerframework.dataflow.qual.Impure
    public <T extends ManifestTemplate> T pullManifest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String imageTag, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> manifestTemplateClass) throws IOException, RegistryException {
        ManifestPuller<T> manifestPuller = new ManifestPuller<>(registryEndpointProperties, imageTag, manifestTemplateClass);
        T manifestTemplate = callRegistryEndpoint(manifestPuller);
        if (manifestTemplate == null) {
            throw new IllegalStateException("ManifestPuller#handleResponse does not return null");
        }
        return manifestTemplate;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ManifestTemplate pullManifest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String imageTag) throws IOException, RegistryException {
        return pullManifest(imageTag, ManifestTemplate.class);
    }

    /**
     * Pushes the image manifest for a specific tag.
     */
    @org.checkerframework.dataflow.qual.Impure
    public void pushManifest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildableManifestTemplate manifestTemplate, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String imageTag) throws IOException, RegistryException {
        callRegistryEndpoint(new ManifestPusher(registryEndpointProperties, manifestTemplate, imageTag));
    }

    /**
     * @return the BLOB's {@link BlobDescriptor} if the BLOB exists on the registry, or {@code null}
     *     if it doesn't
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor checkBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest blobDigest) throws IOException, RegistryException {
        BlobChecker blobChecker = new BlobChecker(registryEndpointProperties, blobDigest);
        return callRegistryEndpoint(blobChecker);
    }

    /**
     * Downloads the BLOB to a file.
     *
     * @param blobDigest the digest of the BLOB to download
     * @param destinationOutputStream the {@link OutputStream} to write the BLOB to
     * @return a {@link Blob} backed by the file at {@code destPath}. The file at {@code destPath}
     *     must exist for {@link Blob} to be valid.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Void pullBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest blobDigest, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull OutputStream destinationOutputStream) throws RegistryException, IOException {
        BlobPuller blobPuller = new BlobPuller(registryEndpointProperties, blobDigest, destinationOutputStream);
        return callRegistryEndpoint(blobPuller);
    }

    // TODO: Add mount with 'from' parameter
    /**
     * Pushes the BLOB, or skips if the BLOB already exists on the registry.
     *
     * @param blobDigest the digest of the BLOB, used for existence-check
     * @param blob the BLOB to push
     * @return {@code true} if the BLOB already exists on the registry and pushing was skipped; false
     *     if the BLOB was pushed
     */
    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean pushBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest blobDigest, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Blob blob) throws IOException, RegistryException {
        BlobPusher blobPusher = new BlobPusher(registryEndpointProperties, blobDigest, blob);
        try (Timer t = parentTimer.subTimer("pushBlob")) {
            try (Timer t2 = t.subTimer("pushBlob POST " + blobDigest)) {
                // POST /v2/<name>/blobs/uploads/?mount={blob.digest}
                String locationHeader = callRegistryEndpoint(blobPusher.initializer());
                if (locationHeader == null) {
                    // The BLOB exists already.
                    return true;
                }
                URL patchLocation = new URL(locationHeader);
                t2.lap("pushBlob PATCH " + blobDigest);
                // PATCH <Location> with BLOB
                URL putLocation = new URL(callRegistryEndpoint(blobPusher.writer(patchLocation)));
                t2.lap("pushBlob PUT " + blobDigest);
                // PUT <Location>?digest={blob.digest}
                callRegistryEndpoint(blobPusher.committer(putLocation));
                return false;
            }
        }
    }

    /**
     * @return the registry endpoint's API root URL
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getApiRouteBase() {
        return PROTOCOL + "://" + registryEndpointProperties.getServerUrl() + "/v2/";
    }

    /**
     * Calls the registry endpoint.
     *
     * @param registryEndpointProvider the {@link RegistryEndpointProvider} to the endpoint
     */
    @org.checkerframework.dataflow.qual.Impure
    private <T> T callRegistryEndpoint(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProvider<T> registryEndpointProvider) throws IOException, RegistryException {
        return callRegistryEndpoint(null, registryEndpointProvider);
    }

    /**
     * Calls the registry endpoint with an override URL.
     *
     * @param url the endpoint URL to call, or {@code null} to use default from {@code
     *     registryEndpointProvider}
     * @param registryEndpointProvider the {@link RegistryEndpointProvider} to the endpoint
     */
    @org.checkerframework.dataflow.qual.Impure
    private <T> T callRegistryEndpoint(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable URL url, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProvider<T> registryEndpointProvider) throws IOException, RegistryException {
        if (url == null) {
            url = registryEndpointProvider.getApiRoute(getApiRouteBase());
        }
        try (Connection connection = new Connection(url)) {
            Request request = Request.builder().setAuthorization(authorization).setUserAgent(getUserAgent()).setAccept(registryEndpointProvider.getAccept()).setBody(registryEndpointProvider.getContent()).build();
            Response response = connection.send(registryEndpointProvider.getHttpMethod(), request);
            return registryEndpointProvider.handleResponse(response);
        } catch (HttpResponseException ex) {
            // First, see if the endpoint provider handles an exception as an expected response.
            try {
                return registryEndpointProvider.handleHttpResponseException(ex);
            } catch (HttpResponseException httpResponseException) {
                if (httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_BAD_REQUEST || httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_NOT_FOUND || httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_METHOD_NOT_ALLOWED) {
                    // The name or reference was invalid.
                    ErrorResponseTemplate errorResponse = JsonTemplateMapper.readJson(httpResponseException.getContent(), ErrorResponseTemplate.class);
                    RegistryErrorExceptionBuilder registryErrorExceptionBuilder = new RegistryErrorExceptionBuilder(registryEndpointProvider.getActionDescription(), httpResponseException);
                    for (ErrorEntryTemplate errorEntry : errorResponse.getErrors()) {
                        registryErrorExceptionBuilder.addReason(errorEntry);
                    }
                    throw registryErrorExceptionBuilder.build();
                } else if (httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED || httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_FORBIDDEN) {
                    throw new RegistryUnauthorizedException(registryEndpointProperties.getServerUrl(), registryEndpointProperties.getImageName(), httpResponseException);
                } else if (httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_TEMPORARY_REDIRECT) {
                    return callRegistryEndpoint(new URL(httpResponseException.getHeaders().getLocation()), registryEndpointProvider);
                } else {
                    // Unknown
                    throw httpResponseException;
                }
            }
        } catch (NoHttpResponseException ex) {
            throw new RegistryNoResponseException(ex);
        } catch (SSLPeerUnverifiedException ex) {
            // Fall-back to HTTP
            GenericUrl httpUrl = new GenericUrl(url);
            httpUrl.setScheme("http");
            return callRegistryEndpoint(httpUrl.toURL(), registryEndpointProvider);
        }
    }
}
