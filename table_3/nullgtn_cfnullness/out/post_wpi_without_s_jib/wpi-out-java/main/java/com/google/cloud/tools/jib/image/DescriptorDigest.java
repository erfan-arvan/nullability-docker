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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.tools.jib.image.json.DescriptorDigestDeserializer;
import com.google.cloud.tools.jib.image.json.DescriptorDigestSerializer;
import java.security.DigestException;

/**
 * Represents a SHA-256 content descriptor digest as defined by the Registry HTTP API v2 reference.
 *
 * @see <a
 *     href="https://docs.docker.com/registry/spec/api/#content-digests">https://docs.docker.com/registry/spec/api/#content-digests</a>
 * @see <a href="https://github.com/opencontainers/image-spec/blob/master/descriptor.md#digests">OCI
 *     Content Descriptor Digest</a>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DescriptorDigest {

    /**
     * Pattern matches a SHA-256 hash - 32 bytes in lowercase hexadecimal.
     */
    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String HASH_REGEX = "[a-f0-9]{64}";

    /**
     * The algorithm prefix for the digest string.
     */
    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DIGEST_PREFIX = "sha256:";

    /**
     * Pattern matches a SHA-256 digest - a SHA-256 hash prefixed with "sha256:".
     */
    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DIGEST_REGEX = DIGEST_PREFIX + HASH_REGEX;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String hash;

    /**
     * Creates a new instance from a valid hash string.
     */
    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DescriptorDigest fromHash(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String hash) throws DigestException {
        if (!hash.matches(HASH_REGEX)) {
            throw new DigestException("Invalid hash: " + hash);
        }
        return new DescriptorDigest(hash);
    }

    /**
     * Creates a new instance from a valid digest string.
     */
    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DescriptorDigest fromDigest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String digest) throws DigestException {
        if (!digest.matches(DIGEST_REGEX)) {
            throw new DigestException("Invalid digest: " + digest);
        }
        // Extracts the hash portion of the digest.
        String hash = digest.substring(DIGEST_PREFIX.length());
        return new DescriptorDigest(hash);
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    private DescriptorDigest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String hash) {
        this.hash = hash;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getHash() {
        return hash;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String toString(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DescriptorDigest this) {
        return "sha256:" + hash;
    }

    /**
     * Pass-through hash code of the digest string.
     */
    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int hashCode(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DescriptorDigest this) {
        return hash.hashCode();
    }

    /**
     * Two digest objects are equal if their digest strings are equal.
     */
    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean equals(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DescriptorDigest this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object obj) {
        if (obj instanceof DescriptorDigest) {
            return hash.equals(((DescriptorDigest) obj).hash);
        }
        return false;
    }
}
