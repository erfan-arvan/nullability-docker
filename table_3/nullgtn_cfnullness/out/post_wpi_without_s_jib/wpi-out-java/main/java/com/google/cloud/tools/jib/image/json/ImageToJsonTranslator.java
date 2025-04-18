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
package com.google.cloud.tools.jib.image.json;

import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.BlobDescriptor;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.image.Image;
import com.google.cloud.tools.jib.image.Layer;
import com.google.cloud.tools.jib.image.LayerPropertyNotFoundException;
import com.google.cloud.tools.jib.json.JsonTemplateMapper;
import java.lang.reflect.InvocationTargetException;

/**
 * Translates an {@link Image} into a manifest or container configuration JSON BLOB.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ImageToJsonTranslator translator = new ImageToJsonTranslator(image);
 * Blob containerConfigurationBlob = translator.getContainerConfigurationBlob();
 * BlobDescriptor containerConfigurationBlobDescriptor = blob.writeTo(outputStream);
 * Blob manifestBlob = translator.getManifestBlob(containerConfigurationBlobDescriptor);
 * }</pre>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ImageToJsonTranslator {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image image;

    /**
     * Instantiate with an {@link Image} that should not be modified afterwards.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public ImageToJsonTranslator(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Image image) {
        this.image = image;
    }

    /**
     * Gets the container configuration as a {@link Blob}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Blob getContainerConfigurationBlob() throws LayerPropertyNotFoundException {
        // Set up the JSON template.
        ContainerConfigurationTemplate template = new ContainerConfigurationTemplate();
        // Adds the layer diff IDs.
        for (Layer layer : image.getLayers()) {
            template.addLayerDiffId(layer.getDiffId());
        }
        // Adds the environment variables.
        template.setContainerEnvironment(image.getEnvironment());
        // Sets the entrypoint.
        template.setContainerEntrypoint(image.getEntrypoint());
        // Serializes into JSON.
        return JsonTemplateMapper.toBlob(template);
    }

    /**
     * Gets the manifest as a JSON template. The {@code containerConfigurationBlobDescriptor} must be
     * the [@link BlobDescriptor} obtained by writing out the container configuration {@link Blob}
     * returned from {@link #getContainerConfigurationBlob()}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public <T extends BuildableManifestTemplate> T getManifestTemplate(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> manifestTemplateClass, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor containerConfigurationBlobDescriptor) throws LayerPropertyNotFoundException {
        try {
            // Set up the JSON template.
            T template = manifestTemplateClass.getDeclaredConstructor().newInstance();
            // Adds the container configuration reference.
            DescriptorDigest containerConfigurationDigest = containerConfigurationBlobDescriptor.getDigest();
            long containerConfigurationSize = containerConfigurationBlobDescriptor.getSize();
            template.setContainerConfiguration(containerConfigurationSize, containerConfigurationDigest);
            // Adds the layers.
            for (Layer layer : image.getLayers()) {
                template.addLayer(layer.getBlobDescriptor().getSize(), layer.getBlobDescriptor().getDigest());
            }
            // Serializes into JSON.
            return template;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            throw new IllegalArgumentException(manifestTemplateClass + " cannot be instantiated", ex);
        }
    }
}
