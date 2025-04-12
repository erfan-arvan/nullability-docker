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
 * layer with its digest, size, and diff ID.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ReferenceLayer implements Layer {

    /**
     * The {@link BlobDescriptor} of the compressed layer content.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull BlobDescriptor blobDescriptor;

    /**
     * The digest of the uncompressed layer content.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DescriptorDigest diffId;

    /**
     * Instantiate with a {@link BlobDescriptor} and diff ID.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public ReferenceLayer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable BlobDescriptor blobDescriptor, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest diffId) {
        this.blobDescriptor = blobDescriptor;
        this.diffId = diffId;
    }

    @org.checkerframework.dataflow.qual.Pure
    public Blob getBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReferenceLayer this) throws LayerPropertyNotFoundException {
        throw new LayerPropertyNotFoundException("Blob not available for reference layer");
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable BlobDescriptor getBlobDescriptor(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReferenceLayer this) {
        return blobDescriptor;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest getDiffId(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ReferenceLayer this) {
        return diffId;
    }
}
