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

import java.security.DigestException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link DescriptorDigest}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DescriptorDigestTest {

    @org.checkerframework.dataflow.qual.Impure
    public void testCreateFromHash_pass() throws DigestException {
        String goodHash = createGoodHash('a');
        DescriptorDigest descriptorDigest = DescriptorDigest.fromHash(goodHash);
        Assert.assertEquals(goodHash, descriptorDigest.getHash());
        Assert.assertEquals("sha256:" + goodHash, descriptorDigest.toString());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCreateFromHash_fail() {
        String badHash = "not a valid hash";
        try {
            DescriptorDigest.fromHash(badHash);
            Assert.fail("Invalid hash should have caused digest creation failure.");
        } catch (DigestException ex) {
            Assert.assertEquals("Invalid hash: " + badHash, ex.getMessage());
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCreateFromHash_failIncorrectLength() {
        String badHash = createGoodHash('a') + 'a';
        try {
            DescriptorDigest.fromHash(badHash);
            Assert.fail("Invalid hash should have caused digest creation failure.");
        } catch (DigestException ex) {
            Assert.assertEquals("Invalid hash: " + badHash, ex.getMessage());
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCreateFromDigest_pass() throws DigestException {
        String goodHash = createGoodHash('a');
        String goodDigest = "sha256:" + createGoodHash('a');
        DescriptorDigest descriptorDigest = DescriptorDigest.fromDigest(goodDigest);
        Assert.assertEquals(goodHash, descriptorDigest.getHash());
        Assert.assertEquals(goodDigest, descriptorDigest.toString());
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testCreateFromDigest_fail() {
        String badDigest = "sha256:not a valid digest";
        try {
            DescriptorDigest.fromDigest(badDigest);
            Assert.fail("Invalid digest should have caused digest creation failure.");
        } catch (DigestException ex) {
            Assert.assertEquals("Invalid digest: " + badDigest, ex.getMessage());
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testUseAsMapKey() throws DigestException {
        DescriptorDigest descriptorDigestA1 = DescriptorDigest.fromHash(createGoodHash('a'));
        DescriptorDigest descriptorDigestA2 = DescriptorDigest.fromHash(createGoodHash('a'));
        DescriptorDigest descriptorDigestA3 = DescriptorDigest.fromDigest("sha256:" + createGoodHash('a'));
        DescriptorDigest descriptorDigestB = DescriptorDigest.fromHash(createGoodHash('b'));
        Map<DescriptorDigest, String> digestMap = new HashMap<>();
        digestMap.put(descriptorDigestA1, "digest with a");
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA1));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA2));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA3));
        Assert.assertNull(digestMap.get(descriptorDigestB));
        digestMap.put(descriptorDigestA2, "digest with a");
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA1));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA2));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA3));
        Assert.assertNull(digestMap.get(descriptorDigestB));
        digestMap.put(descriptorDigestA3, "digest with a");
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA1));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA2));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA3));
        Assert.assertNull(digestMap.get(descriptorDigestB));
        digestMap.put(descriptorDigestB, "digest with b");
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA1));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA2));
        Assert.assertEquals("digest with a", digestMap.get(descriptorDigestA3));
        Assert.assertEquals("digest with b", digestMap.get(descriptorDigestB));
    }

    /**
     * Creates a 32 byte hexademical string to fit valid hash pattern.
     */
    @org.checkerframework.dataflow.qual.Impure
    private static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String createGoodHash( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull char character) {
        StringBuilder goodHashBuffer = new StringBuilder(64);
        for (int i = 0; i < 64; i++) {
            goodHashBuffer.append(character);
        }
        return goodHashBuffer.toString();
    }
}
