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

import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;
import com.google.cloud.tools.jib.http.BlobHttpContent;
import com.google.cloud.tools.jib.http.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Retrieves the {@code WWW-Authenticate} header from the registry API.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class AuthenticationMethodRetriever implements RegistryEndpointProvider<RegistryAuthenticator> {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties registryEndpointProperties;

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable BlobHttpContent getContent(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticationMethodRetriever this) {
        return null;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getAccept(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticationMethodRetriever this) {
        return Collections.emptyList();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable RegistryAuthenticator handleResponse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticationMethodRetriever this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) {
        // The registry does not require authentication.
        return null;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull URL getApiRoute(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticationMethodRetriever this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiRouteBase) throws MalformedURLException {
        return new URL(apiRouteBase);
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getHttpMethod(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticationMethodRetriever this) {
        return HttpMethods.GET;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getActionDescription(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticationMethodRetriever this) {
        return "retrieve authentication method for " + registryEndpointProperties.getServerUrl();
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable RegistryAuthenticator handleHttpResponseException(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AuthenticationMethodRetriever this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull HttpResponseException httpResponseException) throws HttpResponseException, RegistryErrorException {
        // Only valid for status code of '401 Unauthorized'.
        if (httpResponseException.getStatusCode() != HttpStatusCodes.STATUS_CODE_UNAUTHORIZED) {
            throw httpResponseException;
        }
        // Checks if the 'WWW-Authenticate' header is present.
        String authenticationMethod = httpResponseException.getHeaders().getAuthenticate();
        if (authenticationMethod == null) {
            throw new RegistryErrorExceptionBuilder(getActionDescription(), httpResponseException).addReason("'WWW-Authenticate' header not found").build();
        }
        // Parses the header to retrieve the components.
        try {
            return RegistryAuthenticator.fromAuthenticationMethod(authenticationMethod, registryEndpointProperties.getImageName());
        } catch (RegistryAuthenticationFailedException | MalformedURLException ex) {
            throw new RegistryErrorExceptionBuilder(getActionDescription(), ex).addReason("Failed get authentication method from 'WWW-Authenticate' header").build();
        }
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    AuthenticationMethodRetriever(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties registryEndpointProperties) {
        this.registryEndpointProperties = registryEndpointProperties;
    }
}
