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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.http.HttpMethods;
import com.google.cloud.tools.jib.blob.Blobs;
import com.google.cloud.tools.jib.http.BlobHttpContent;
import com.google.cloud.tools.jib.http.Response;
import com.google.cloud.tools.jib.image.json.ManifestTemplate;
import com.google.cloud.tools.jib.image.json.OCIManifestTemplate;
import com.google.cloud.tools.jib.image.json.UnknownManifestFormatException;
import com.google.cloud.tools.jib.image.json.V21ManifestTemplate;
import com.google.cloud.tools.jib.image.json.V22ManifestTemplate;
import com.google.cloud.tools.jib.json.JsonTemplateMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Pulls an image's manifest.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class ManifestPuller<T extends ManifestTemplate> implements RegistryEndpointProvider<T> {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties registryEndpointProperties;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String imageTag;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> manifestTemplateClass;

    @org.checkerframework.dataflow.qual.SideEffectFree
    ManifestPuller(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RegistryEndpointProperties registryEndpointProperties, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String imageTag, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> manifestTemplateClass) {
        this.registryEndpointProperties = registryEndpointProperties;
        this.imageTag = imageTag;
        this.manifestTemplateClass = manifestTemplateClass;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable BlobHttpContent getContent(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ManifestPuller<T> this) {
        return null;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getAccept(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ManifestPuller<T> this) {
        if (manifestTemplateClass.equals(V21ManifestTemplate.class)) {
            return Collections.singletonList(V21ManifestTemplate.MEDIA_TYPE);
        }
        if (manifestTemplateClass.equals(V22ManifestTemplate.class)) {
            return Collections.singletonList(V22ManifestTemplate.MANIFEST_MEDIA_TYPE);
        }
        if (manifestTemplateClass.equals(OCIManifestTemplate.class)) {
            return Collections.singletonList(OCIManifestTemplate.MANIFEST_MEDIA_TYPE);
        }
        return Arrays.asList(OCIManifestTemplate.MANIFEST_MEDIA_TYPE, V22ManifestTemplate.MANIFEST_MEDIA_TYPE, V21ManifestTemplate.MEDIA_TYPE);
    }

    /**
     * Parses the response body into a {@link ManifestTemplate}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public T handleResponse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ManifestPuller<T> this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) throws IOException, UnknownManifestFormatException {
        return getManifestTemplateFromJson(Blobs.writeToString(response.getBody()));
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull URL getApiRoute(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ManifestPuller<T> this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiRouteBase) throws MalformedURLException {
        return new URL(apiRouteBase + registryEndpointProperties.getImageName() + "/manifests/" + imageTag);
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getHttpMethod(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ManifestPuller<T> this) {
        return HttpMethods.GET;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getActionDescription(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ManifestPuller<T> this) {
        return "pull image manifest for " + registryEndpointProperties.getServerUrl() + "/" + registryEndpointProperties.getImageName() + ":" + imageTag;
    }

    /**
     * Instantiates a {@link ManifestTemplate} from a JSON string. This checks the {@code
     * schemaVersion} field of the JSON to determine which manifest version to use.
     */
    @org.checkerframework.dataflow.qual.Impure
    private T getManifestTemplateFromJson(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String jsonString) throws IOException, UnknownManifestFormatException {
        ObjectNode node = new ObjectMapper().readValue(jsonString, ObjectNode.class);
        if (!node.has("schemaVersion")) {
            throw new UnknownManifestFormatException("Cannot find field 'schemaVersion' in manifest");
        }
        if (!manifestTemplateClass.equals(ManifestTemplate.class)) {
            return JsonTemplateMapper.readJson(jsonString, manifestTemplateClass);
        }
        int schemaVersion = node.get("schemaVersion").asInt(-1);
        if (schemaVersion == -1) {
            throw new UnknownManifestFormatException("`schemaVersion` field is not an integer");
        }
        if (schemaVersion == 1) {
            return manifestTemplateClass.cast(JsonTemplateMapper.readJson(jsonString, V21ManifestTemplate.class));
        }
        if (schemaVersion == 2) {
            // 'schemaVersion' of 2 can be either Docker V2.2 or OCI.
            String mediaType = node.get("mediaType").asText();
            if (V22ManifestTemplate.MANIFEST_MEDIA_TYPE.equals(mediaType)) {
                return manifestTemplateClass.cast(JsonTemplateMapper.readJson(jsonString, V22ManifestTemplate.class));
            }
            if (OCIManifestTemplate.MANIFEST_MEDIA_TYPE.equals(mediaType)) {
                return manifestTemplateClass.cast(JsonTemplateMapper.readJson(jsonString, OCIManifestTemplate.class));
            }
            throw new UnknownManifestFormatException("Unknown mediaType: " + mediaType);
        }
        throw new UnknownManifestFormatException("Unknown schemaVersion: " + schemaVersion + " - only 1 and 2 are supported");
    }
}
