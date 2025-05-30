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
package com.google.cloud.tools.jib.image;

import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.BlobDescriptor;

/**
 * A {@link Layer} reference that <b>does not</b> have the underlying content. It references the
 * layer with its digest and size, but <b>not</b> its diff ID.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ReferenceNoDiffIdLayer implements Layer {

    /**
     * The {@link BlobDescriptor} of the compressed layer content.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor blobDescriptor;

    /**
     * Instantiate with a {@link BlobDescriptor} and diff ID.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public ReferenceNoDiffIdLayer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor blobDescriptor) {
        this.blobDescriptor = blobDescriptor;
    }

    @org.checkerframework.dataflow.qual.Pure
    public Blob getBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReferenceNoDiffIdLayer this) throws LayerPropertyNotFoundException {
        throw new LayerPropertyNotFoundException("Blob not available for reference layer without diff ID");
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor getBlobDescriptor(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReferenceNoDiffIdLayer this) {
        return blobDescriptor;
    }

    @org.checkerframework.dataflow.qual.Pure
    public DescriptorDigest getDiffId(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReferenceNoDiffIdLayer this) throws LayerPropertyNotFoundException {
        throw new LayerPropertyNotFoundException("Diff ID not available for reference layer without diff ID");
    }
}
