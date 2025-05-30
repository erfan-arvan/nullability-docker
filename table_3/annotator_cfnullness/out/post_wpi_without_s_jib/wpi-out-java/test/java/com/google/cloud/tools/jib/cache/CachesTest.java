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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link Caches}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class CachesTest {

    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull TemporaryFolder temporaryFolder = new TemporaryFolder();

    @org.checkerframework.dataflow.qual.Impure
    public void testInitializer() throws CacheMetadataCorruptedException, IOException, CacheDirectoryNotOwnedException {
        Path tempBaseCacheDirectory = temporaryFolder.newFolder().toPath();
        Path tempApplicationCacheDirectory = temporaryFolder.newFolder().toPath();
        try (Caches caches = Caches.newInitializer(tempApplicationCacheDirectory).setBaseCacheDirectory(tempBaseCacheDirectory).init()) {
            Assert.assertEquals(tempBaseCacheDirectory, caches.getBaseCache().getCacheDirectory());
            Assert.assertEquals(tempApplicationCacheDirectory, caches.getApplicationCache().getCacheDirectory());
        }
        // Checks that the caches were closed (metadata.json saved).
        Assert.assertTrue(Files.exists(tempBaseCacheDirectory.resolve(CacheFiles.METADATA_FILENAME)));
        Assert.assertTrue(Files.exists(tempApplicationCacheDirectory.resolve(CacheFiles.METADATA_FILENAME)));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testEnsureOwnership_notOwned() throws IOException {
        Path cacheDirectory = temporaryFolder.newFolder().toPath();
        try {
            Caches.Initializer.ensureOwnership(cacheDirectory);
            Assert.fail("Expected CacheDirectoryNotOwnedException to be thrown");
        } catch (CacheDirectoryNotOwnedException ex) {
            Assert.assertEquals(cacheDirectory, ex.getCacheDirectory());
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testEnsureOwnership_create() throws IOException, CacheDirectoryNotOwnedException {
        Path cacheDirectory = temporaryFolder.newFolder().toPath();
        Path nonexistentDirectory = cacheDirectory.resolve("somefolder");
        Caches.Initializer.ensureOwnership(nonexistentDirectory);
    }
}
