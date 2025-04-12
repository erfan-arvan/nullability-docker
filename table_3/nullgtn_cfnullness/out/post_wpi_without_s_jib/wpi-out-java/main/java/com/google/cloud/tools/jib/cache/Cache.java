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
package com.google.cloud.tools.jib.cache;

import com.google.cloud.tools.jib.cache.json.CacheMetadataTemplate;
import com.google.cloud.tools.jib.image.LayerPropertyNotFoundException;
import com.google.cloud.tools.jib.json.JsonTemplateMapper;
import com.google.common.annotations.VisibleForTesting;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import javax.annotation.Nullable;

/**
 * Manages a cache. Implementation is thread-safe.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Cache implements Closeable {

    /**
     * The path to the root of the cache.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path cacheDirectory;

    /**
     * The metadata that corresponds to the cache at {@link #cacheDirectory}.
     */
    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheMetadata cacheMetadata;

    /**
     * Initializes a cache with a directory. This also loads the cache metadata if it exists in the
     * directory.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache init(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Path cacheDirectory) throws NotDirectoryException, CacheMetadataCorruptedException {
        if (!Files.isDirectory(cacheDirectory)) {
            throw new NotDirectoryException("The cache can only write to a directory");
        }
        CacheMetadata cacheMetadata = loadCacheMetadata(cacheDirectory);
        return new Cache(cacheDirectory, cacheMetadata);
    }

    @org.checkerframework.dataflow.qual.Impure
    private static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheMetadata loadCacheMetadata(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path cacheDirectory) throws CacheMetadataCorruptedException {
        Path cacheMetadataJsonFile = cacheDirectory.resolve(CacheFiles.METADATA_FILENAME);
        if (!Files.exists(cacheMetadataJsonFile)) {
            return new CacheMetadata();
        }
        try {
            CacheMetadataTemplate cacheMetadataJson = JsonTemplateMapper.readJsonFromFile(cacheMetadataJsonFile, CacheMetadataTemplate.class);
            return CacheMetadataTranslator.fromTemplate(cacheMetadataJson, cacheDirectory);
        } catch (IOException ex) {
            // The cache metadata is probably corrupted.
            throw new CacheMetadataCorruptedException(ex);
        }
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    private Cache(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path cacheDirectory, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheMetadata cacheMetadata) {
        this.cacheDirectory = cacheDirectory;
        this.cacheMetadata = cacheMetadata;
    }

    /**
     * Finishes the use of the cache by flushing any unsaved changes.
     */
    @org.checkerframework.dataflow.qual.Impure
    public void close(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache this) throws IOException {
        saveCacheMetadata(cacheDirectory);
    }

    /**
     * Adds the cached layer to the cache metadata.
     */
    @org.checkerframework.dataflow.qual.Impure
    void addLayerToMetadata(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CachedLayer cachedLayer, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable LayerMetadata layerMetadata) throws LayerPropertyNotFoundException {
        cacheMetadata.addLayer(new CachedLayerWithMetadata(cachedLayer, layerMetadata));
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path getCacheDirectory() {
        return cacheDirectory;
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheMetadata getMetadata() {
        return cacheMetadata;
    }

    /**
     * Saves the updated cache metadata back to the cache.
     */
    @org.checkerframework.dataflow.qual.Impure
    private void saveCacheMetadata(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path cacheDirectory) throws IOException {
        Path cacheMetadataJsonFile = cacheDirectory.resolve(CacheFiles.METADATA_FILENAME);
        CacheMetadataTemplate cacheMetadataJson = CacheMetadataTranslator.toTemplate(cacheMetadata);
        try (OutputStream fileOutputStream = new BufferedOutputStream(Files.newOutputStream(cacheMetadataJsonFile))) {
            JsonTemplateMapper.toBlob(cacheMetadataJson).writeTo(fileOutputStream);
        }
    }
}
