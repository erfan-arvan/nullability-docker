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

import com.google.cloud.tools.jib.blob.Blob;
import com.google.cloud.tools.jib.blob.BlobDescriptor;
import com.google.cloud.tools.jib.blob.Blobs;
import com.google.cloud.tools.jib.hash.CountingDigestOutputStream;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.image.LayerBuilder;
import com.google.cloud.tools.jib.image.LayerPropertyNotFoundException;
import com.google.cloud.tools.jib.image.UnwrittenLayer;
import com.google.common.io.CharStreams;
import com.google.common.io.CountingOutputStream;
import com.google.common.io.Resources;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

/**
 * Tests for {@link CacheWriter}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class CacheWriterTest {

    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TemporaryFolder temporaryCacheDirectory = new TemporaryFolder();

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Cache testCache;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Path resourceBlob;

    private static class ExpectedLayer {

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor blobDescriptor;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DescriptorDigest diffId;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Blob blob;

        @org.checkerframework.dataflow.qual.SideEffectFree
        private ExpectedLayer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor blobDescriptor, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest diffId, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Blob blob) {
            this.blobDescriptor = blobDescriptor;
            this.diffId = diffId;
            this.blob = blob;
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void setUp() throws CacheMetadataCorruptedException, IOException, URISyntaxException {
        Path cacheDirectory = temporaryCacheDirectory.newFolder().toPath();
        testCache = Cache.init(cacheDirectory);
        resourceBlob = Paths.get(Resources.getResource("blobA").toURI());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testWriteLayer_unwritten() throws IOException, LayerPropertyNotFoundException {
        ExpectedLayer expectedLayer = getExpectedLayer();
        // Writes resourceBlob as a layer to the cache.
        CacheWriter cacheWriter = new CacheWriter(testCache);
        UnwrittenLayer unwrittenLayer = new UnwrittenLayer(Blobs.from(resourceBlob));
        List<Path> fakeSourceFiles = Collections.singletonList(Paths.get("some", "source", "file"));
        LayerBuilder mockLayerBuilder = Mockito.mock(LayerBuilder.class);
        Mockito.when(mockLayerBuilder.build()).thenReturn(unwrittenLayer);
        Mockito.when(mockLayerBuilder.getSourceFiles()).thenReturn(fakeSourceFiles);
        CachedLayer cachedLayer = cacheWriter.writeLayer(mockLayerBuilder);
        CachedLayerWithMetadata layerInMetadata = testCache.getMetadata().getLayers().get(0);
        Assert.assertNotNull(layerInMetadata.getMetadata());
        Assert.assertEquals(Collections.singletonList(Paths.get("some", "source", "file").toString()), layerInMetadata.getMetadata().getSourceFiles());
        verifyCachedLayerIsExpected(expectedLayer, cachedLayer);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testGetLayerOutputStream() throws IOException, LayerPropertyNotFoundException {
        ExpectedLayer expectedLayer = getExpectedLayer();
        // Writes resourceBlob as a layer to the cache.
        CacheWriter cacheWriter = new CacheWriter(testCache);
        CountingOutputStream layerOutputStream = cacheWriter.getLayerOutputStream(expectedLayer.blobDescriptor.getDigest());
        expectedLayer.blob.writeTo(layerOutputStream);
        CachedLayer cachedLayer = cacheWriter.getCachedLayer(expectedLayer.blobDescriptor.getDigest(), layerOutputStream);
        CachedLayerWithMetadata layerInMetadata = testCache.getMetadata().getLayers().get(0);
        Assert.assertNull(layerInMetadata.getMetadata());
        verifyCachedLayerIsExpected(expectedLayer, cachedLayer);
    }

    /**
     * @return the expected layer to test against, represented by the {@code resourceBlob} resource
     *     file
     */
    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ExpectedLayer getExpectedLayer() throws IOException {
        String expectedBlobAString = new String(Files.readAllBytes(resourceBlob), StandardCharsets.UTF_8);
        // Gets the expected content descriptor, diff ID, and compressed BLOB.
        ByteArrayOutputStream compressedBlobOutputStream = new ByteArrayOutputStream();
        CountingDigestOutputStream compressedDigestOutputStream = new CountingDigestOutputStream(compressedBlobOutputStream);
        CountingDigestOutputStream uncompressedDigestOutputStream;
        try (GZIPOutputStream compressorStream = new GZIPOutputStream(compressedDigestOutputStream)) {
            uncompressedDigestOutputStream = new CountingDigestOutputStream(compressorStream);
            uncompressedDigestOutputStream.write(expectedBlobAString.getBytes(StandardCharsets.UTF_8));
        }
        BlobDescriptor expectedBlobADescriptor = compressedDigestOutputStream.toBlobDescriptor();
        DescriptorDigest expectedBlobADiffId = uncompressedDigestOutputStream.toBlobDescriptor().getDigest();
        ByteArrayInputStream compressedBlobInputStream = new ByteArrayInputStream(compressedBlobOutputStream.toByteArray());
        Blob blob = Blobs.from(compressedBlobInputStream);
        return new ExpectedLayer(expectedBlobADescriptor, expectedBlobADiffId, blob);
    }

    @org.checkerframework.dataflow.qual.Impure
    private void verifyCachedLayerIsExpected(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ExpectedLayer expectedLayer, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CachedLayer cachedLayer) throws IOException {
        // Reads the cached layer back.
        Path compressedBlobFile = cachedLayer.getContentFile();
        try (InputStreamReader fileReader = new InputStreamReader(new GZIPInputStream(Files.newInputStream(compressedBlobFile)), StandardCharsets.UTF_8)) {
            String decompressedString = CharStreams.toString(fileReader);
            String expectedBlobAString = new String(Files.readAllBytes(resourceBlob), StandardCharsets.UTF_8);
            Assert.assertEquals(expectedBlobAString, decompressedString);
            Assert.assertEquals(expectedLayer.blobDescriptor.getSize(), cachedLayer.getBlobDescriptor().getSize());
            Assert.assertEquals(expectedLayer.blobDescriptor.getDigest(), cachedLayer.getBlobDescriptor().getDigest());
            Assert.assertEquals(expectedLayer.blobDescriptor, cachedLayer.getBlobDescriptor());
            Assert.assertEquals(expectedLayer.diffId, cachedLayer.getDiffId());
        }
    }
}
