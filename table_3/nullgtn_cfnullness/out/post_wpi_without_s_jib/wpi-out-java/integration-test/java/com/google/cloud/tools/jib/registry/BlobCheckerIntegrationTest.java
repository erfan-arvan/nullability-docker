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
package com.google.cloud.tools.jib.registry;

import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.cloud.tools.jib.image.json.V22ManifestTemplate;
import java.io.IOException;
import java.security.DigestException;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * Integration tests for {@link BlobChecker}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class BlobCheckerIntegrationTest {

    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LocalRegistry localRegistry = new LocalRegistry(5000);

    @org.checkerframework.dataflow.qual.Impure
    public void testCheck_exists() throws IOException, RegistryException {
        RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
        V22ManifestTemplate manifestTemplate = registryClient.pullManifest("latest", V22ManifestTemplate.class);
        DescriptorDigest blobDigest = manifestTemplate.getLayers().get(0).getDigest();
        Assert.assertEquals(blobDigest, registryClient.checkBlob(blobDigest).getDigest());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCheck_doesNotExist() throws IOException, RegistryException, DigestException {
        RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
        DescriptorDigest fakeBlobDigest = DescriptorDigest.fromHash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Assert.assertEquals(null, registryClient.checkBlob(fakeBlobDigest));
    }
}
