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
package com.google.cloud.tools.jib.hash;

import com.google.cloud.tools.jib.blob.BlobDescriptor;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.annotation.Nullable;

/**
 * Tests for {@link CountingDigestOutputStream}.
 */
public class CountingDigestOutputStreamTest {

    private Map<String, String> knownSha256Hashes;

    @Before
    public void setUp() {
        knownSha256Hashes = Collections.unmodifiableMap(new HashMap<String, String>() {

            {
                put("crepecake", "52a9e4d4ba4333ce593707f98564fee1e6d898db0d3602408c0b2a6a424d357c");
                put("12345", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5");
                put("", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
            }
        });
    }

    @Test
    public void test_smokeTest() throws IOException, DigestException {
        for (Map.Entry<String, String> knownHash : knownSha256Hashes.entrySet()) {
            String toHash = knownHash.getKey();
            String expectedHash = knownHash.getValue();
            OutputStream underlyingOutputStream = new ByteArrayOutputStream();
            CountingDigestOutputStream countingDigestOutputStream = new CountingDigestOutputStream(underlyingOutputStream);
            byte[] bytesToHash = toHash.getBytes(StandardCharsets.UTF_8);
            InputStream toHashInputStream = new ByteArrayInputStream(bytesToHash);
            ByteStreams.copy(toHashInputStream, countingDigestOutputStream);
            BlobDescriptor expectedBlobDescriptor = new BlobDescriptor(bytesToHash.length, DescriptorDigest.fromHash(expectedHash));
            Assert.assertEquals(expectedBlobDescriptor, countingDigestOutputStream.toBlobDescriptor());
            Assert.assertEquals(bytesToHash.length, countingDigestOutputStream.getTotalBytes());
        }
    }
}
