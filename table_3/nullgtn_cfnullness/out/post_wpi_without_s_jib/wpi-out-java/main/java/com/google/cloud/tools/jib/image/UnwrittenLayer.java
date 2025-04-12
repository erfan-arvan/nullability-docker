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
package com.google.cloud.tools.jib.image;

import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.BlobDescriptor;
import com.google.cloud.tools.jib.cache.CachedLayer;

/**
 * A layer that has not been written out and only has the unwritten content {@link Blob}. Once
 * written, this layer becomes a {@link CachedLayer}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class UnwrittenLayer implements Layer {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Blob uncompressedBlob;

    /**
     * Initializes with the uncompressed {@link Blob} of the layer content.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public UnwrittenLayer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Blob uncompressedBlob) {
        this.uncompressedBlob = uncompressedBlob;
    }

    /**
     * Gets the uncompressed layer content BLOB.
     */
    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Blob getBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UnwrittenLayer this) {
        return uncompressedBlob;
    }

    @org.checkerframework.dataflow.qual.Pure
    public BlobDescriptor getBlobDescriptor(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UnwrittenLayer this) throws LayerPropertyNotFoundException {
        throw new LayerPropertyNotFoundException("Blob descriptor not available for unwritten layer");
    }

    @org.checkerframework.dataflow.qual.Pure
    public DescriptorDigest getDiffId(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UnwrittenLayer this) throws LayerPropertyNotFoundException {
        throw new LayerPropertyNotFoundException("Diff ID not available for unwritten layer");
    }
}
