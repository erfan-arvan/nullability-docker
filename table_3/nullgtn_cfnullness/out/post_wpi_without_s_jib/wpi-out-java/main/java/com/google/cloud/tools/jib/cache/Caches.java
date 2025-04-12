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

import com.google.cloud.tools.jib.filesystem.UserCacheHome;
import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

/**
 * Manages both the base image layers cache and the application image layers cache.
 *
 * <p>In general, the cache for base image layers should be shared between projects, while the cache
 * for the application image layers should be specific to a single project.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Caches implements Closeable {

    /**
     * Initializes a {@link Caches} with directory paths. Use {@link #newInitializer} to construct.
     */
    public static class Initializer {

        /**
         * The default directory for caching the base image layers, in {@code [user cache
         * home]/google-cloud-tools-java/jib}.
         */
        private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path DEFAULT_BASE_CACHE_DIRECTORY = UserCacheHome.getCacheHome().resolve("google-cloud-tools-java").resolve("jib");

        /**
         * A file to store in the default base image layers cache to check ownership by Jib.
         */
        private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String OWNERSHIP_FILE_NAME = ".jib";

        @org.checkerframework.dataflow.qual.Impure
        static /**
         * Ensures ownership of {@code cacheDirectory} by checking for the existence of {@link
         * #OWNERSHIP_FILE_NAME}.
         *
         * <p>This is a safety check to make sure we are not writing to a directory not created by Jib.
         */
        void ensureOwnership(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path cacheDirectory) throws CacheDirectoryNotOwnedException, IOException {
            Path ownershipFile = cacheDirectory.resolve(OWNERSHIP_FILE_NAME);
            if (Files.exists(cacheDirectory)) {
                // Checks for the ownership file.
                if (!Files.exists(ownershipFile)) {
                    throw new CacheDirectoryNotOwnedException(cacheDirectory);
                }
            } else {
                // Creates the cache directory and ownership file.
                Files.createDirectories(cacheDirectory);
                Files.createFile(ownershipFile);
            }
        }

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path applicationCacheDirectory;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path baseCacheDirectory = DEFAULT_BASE_CACHE_DIRECTORY;

        @org.checkerframework.dataflow.qual.SideEffectFree
        private Initializer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path applicationCacheDirectory) {
            this.applicationCacheDirectory = applicationCacheDirectory;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer setBaseCacheDirectory(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path baseCacheDirectory) {
            this.baseCacheDirectory = baseCacheDirectory;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Caches init() throws CacheMetadataCorruptedException, IOException, CacheDirectoryNotOwnedException {
            if (DEFAULT_BASE_CACHE_DIRECTORY.equals(baseCacheDirectory)) {
                ensureOwnership(DEFAULT_BASE_CACHE_DIRECTORY);
            }
            return new Caches(baseCacheDirectory, applicationCacheDirectory);
        }
    }

    /**
     * @param applicationCacheDirectory Cache for the application image layers - should be local to
     *     the application project
     * @return a new {@link Initializer} to initialize the caches.
     */
    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Initializer newInitializer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path applicationCacheDirectory) {
        return new Initializer(applicationCacheDirectory);
    }

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache baseCache;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache applicationCache;

    /**
     * Instantiate with {@link Initializer#init}.
     */
    @org.checkerframework.dataflow.qual.Impure
    private Caches(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path baseCacheDirectory, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Path applicationCacheDirectory) throws CacheMetadataCorruptedException, NotDirectoryException {
        baseCache = Cache.init(baseCacheDirectory);
        applicationCache = Cache.init(applicationCacheDirectory);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void close(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Caches this) throws IOException {
        baseCache.close();
        applicationCache.close();
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache getBaseCache() {
        return baseCache;
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache getApplicationCache() {
        return applicationCache;
    }
}
