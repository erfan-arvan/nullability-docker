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
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A {@link DigestOutputStream} that also keeps track of the total number of bytes written.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class CountingDigestOutputStream extends DigestOutputStream {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String SHA_256_ALGORITHM = "SHA-256";

    /**
     * Keeps track of the total number of bytes appended.
     */
    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long totalBytes = 0;

    /**
     * Wraps the {@code outputStream}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public CountingDigestOutputStream(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull OutputStream outputStream) {
        super(outputStream, null);
        try {
            setMessageDigest(MessageDigest.getInstance(SHA_256_ALGORITHM));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("SHA-256 algorithm implementation not found - might be a broken JVM");
        }
    }

    /**
     * Builds a {@link BlobDescriptor} with the hash and size of the bytes written.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor toBlobDescriptor() {
        try {
            byte[] hashedBytes = digest.digest();
            // Encodes each hashed byte into 2-character hexadecimal representation.
            StringBuilder stringBuilder = new StringBuilder(2 * hashedBytes.length);
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            String hash = stringBuilder.toString();
            DescriptorDigest digest = DescriptorDigest.fromHash(hash);
            return new BlobDescriptor(totalBytes, digest);
        } catch (DigestException ex) {
            throw new RuntimeException("SHA-256 algorithm produced invalid hash: " + ex.getMessage(), ex);
        }
    }

    /**
     * @return the total number of bytes that were hashed
     */
    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long getTotalBytes() {
        return totalBytes;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void write(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CountingDigestOutputStream this,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull byte @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull [] data,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int offset,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int length) throws IOException {
        super.write(data, offset, length);
        totalBytes += length;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void write(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CountingDigestOutputStream this,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int singleByte) throws IOException {
        super.write(singleByte);
        totalBytes++;
    }
}
