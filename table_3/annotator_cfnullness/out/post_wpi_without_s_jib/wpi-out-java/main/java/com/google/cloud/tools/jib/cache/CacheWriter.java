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
package com.google.cloud.tools.jib.cache;

import com.google.cloud.tools.jib.blob.BlobDescriptor;
import com.google.cloud.tools.jib.hash.CountingDigestOutputStream;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.image.LayerBuilder;
import com.google.cloud.tools.jib.image.LayerPropertyNotFoundException;
import com.google.cloud.tools.jib.image.UnwrittenLayer;
import com.google.common.io.ByteStreams;
import com.google.common.io.CountingOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Writes {@link UnwrittenLayer}s to the cache.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class CacheWriter {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Cache cache;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public CacheWriter(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Cache cache) {
        this.cache = cache;
    }

    /**
     * Builds an {@link UnwrittenLayer} from a {@link LayerBuilder} and compresses and writes the
     * {@link UnwrittenLayer}'s uncompressed layer content BLOB to cache.
     *
     * @param layerBuilder the layer builder
     * @return the cached layer
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CachedLayer writeLayer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LayerBuilder layerBuilder) throws IOException, LayerPropertyNotFoundException {
        UnwrittenLayer unwrittenLayer = layerBuilder.build();
        // Writes to a temporary file first because the UnwrittenLayer needs to be written first to
        // obtain its digest.
        Path tempLayerFile = Files.createTempFile(cache.getCacheDirectory(), null, null);
        // TODO: Find a way to do this with java.nio.file
        tempLayerFile.toFile().deleteOnExit();
        // Writes the UnwrittenLayer layer BLOB to a file to convert into a CachedLayer.
        try (CountingDigestOutputStream compressedDigestOutputStream = new CountingDigestOutputStream(new BufferedOutputStream(Files.newOutputStream(tempLayerFile)))) {
            // Writes the layer with GZIP compression. The original bytes are captured as the layer's
            // diff ID and the bytes outputted from the GZIP compression are captured as the layer's
            // content descriptor.
            GZIPOutputStream compressorStream = new GZIPOutputStream(compressedDigestOutputStream);
            DescriptorDigest diffId = unwrittenLayer.getBlob().writeTo(compressorStream).getDigest();
            // The GZIPOutputStream must be closed in order to write out the remaining compressed data.
            compressorStream.close();
            BlobDescriptor compressedBlobDescriptor = compressedDigestOutputStream.toBlobDescriptor();
            // Renames the temporary layer file to the correct filename. If the file already exists, we
            // skip renaming and use the existing file. This happens if a new layer happens to have the
            // same content as a previously-cached layer.
            Path layerFile = getLayerFile(compressedBlobDescriptor.getDigest());
            Files.move(tempLayerFile, layerFile, StandardCopyOption.REPLACE_EXISTING);
            CachedLayer cachedLayer = new CachedLayer(layerFile, compressedBlobDescriptor, diffId);
            LayerMetadata layerMetadata = LayerMetadata.from(layerBuilder.getSourceFiles(), FileTime.from(Instant.now()));
            cache.addLayerToMetadata(cachedLayer, layerMetadata);
            return cachedLayer;
        }
    }

    /**
     * @return the {@link CountingOutputStream} to write to to cache a layer with the specified
     *     compressed digest
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CountingOutputStream getLayerOutputStream(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest layerDigest) throws IOException {
        Path layerFile = getLayerFile(layerDigest);
        return new CountingOutputStream(new BufferedOutputStream(Files.newOutputStream(layerFile)));
    }

    /**
     * @return a {@link CachedLayer} from a layer digest and the {@link CountingOutputStream} the
     *     layer BLOB was written to
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CachedLayer getCachedLayer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest layerDigest, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CountingOutputStream countingOutputStream) throws IOException, LayerPropertyNotFoundException {
        Path layerFile = getLayerFile(layerDigest);
        countingOutputStream.close();
        CachedLayer cachedLayer = new CachedLayer(layerFile, new BlobDescriptor(countingOutputStream.getCount(), layerDigest), getDiffId(layerFile));
        cache.addLayerToMetadata(cachedLayer, null);
        return cachedLayer;
    }

    /**
     * @return the path to the file for the layer with the specified compressed digest
     */
    @org.checkerframework.dataflow.qual.Pure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path getLayerFile(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest compressedDigest) {
        return CacheFiles.getLayerFile(cache.getCacheDirectory(), compressedDigest);
    }

    /**
     * @return the layer diff ID by decompressing the layer content file
     */
    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest getDiffId(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path layerFile) throws IOException {
        CountingDigestOutputStream diffIdCaptureOutputStream = new CountingDigestOutputStream(ByteStreams.nullOutputStream());
        try (InputStream fileInputStream = new BufferedInputStream(Files.newInputStream(layerFile));
            GZIPInputStream decompressorStream = new GZIPInputStream(fileInputStream)) {
            ByteStreams.copy(decompressorStream, diffIdCaptureOutputStream);
        }
        return diffIdCaptureOutputStream.toBlobDescriptor().getDigest();
    }
}
