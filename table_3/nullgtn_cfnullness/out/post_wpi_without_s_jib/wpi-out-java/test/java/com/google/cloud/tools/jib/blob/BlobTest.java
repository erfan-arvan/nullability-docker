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
package com.google.cloud.tools.jib.blob;

import com.google.cloud.tools.jib.hash.CountingDigestOutputStream;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.common.io.Resources;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link Blob}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class BlobTest {

    @org.checkerframework.dataflow.qual.Impure
    public void testFromInputStream() throws IOException {
        String expected = "crepecake";
        InputStream inputStream = new ByteArrayInputStream(expected.getBytes(StandardCharsets.UTF_8));
        verifyBlobWriteTo(expected, Blobs.from(inputStream));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testFromFile() throws IOException, URISyntaxException {
        Path fileA = Paths.get(Resources.getResource("fileA").toURI());
        String expected = new String(Files.readAllBytes(fileA), StandardCharsets.UTF_8);
        verifyBlobWriteTo(expected, Blobs.from(fileA));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testFromString() throws IOException {
        String expected = "crepecake";
        verifyBlobWriteTo(expected, Blobs.from(expected));
    }

    @org.checkerframework.dataflow.qual.Impure
    public void testFromBlobWriter() throws IOException {
        String expected = "crepecake";
        BlobWriter writer = outputStream -> outputStream.write(expected.getBytes(StandardCharsets.UTF_8));
        verifyBlobWriteTo(expected, Blobs.from(writer));
    }

    /**
     * Checks that the {@link Blob} streams the expected string.
     */
    @org.checkerframework.dataflow.qual.Impure
    private void verifyBlobWriteTo(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String expected, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Blob blob) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        BlobDescriptor blobDescriptor = blob.writeTo(outputStream);
        String output = outputStream.toString();
        Assert.assertEquals(expected, output);
        byte[] expectedBytes = expected.getBytes(StandardCharsets.UTF_8);
        Assert.assertEquals(expectedBytes.length, blobDescriptor.getSize());
        CountingDigestOutputStream countingDigestOutputStream = new CountingDigestOutputStream(Mockito.mock(OutputStream.class));
        countingDigestOutputStream.write(expectedBytes);
        DescriptorDigest expectedDigest = countingDigestOutputStream.toBlobDescriptor().getDigest();
        Assert.assertEquals(expectedDigest, blobDescriptor.getDigest());
    }
}
