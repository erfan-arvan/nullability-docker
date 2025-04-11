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
package com.google.cloud.tools.jib.blob;

import com.google.cloud.tools.jib.hash.CountingDigestOutputStream;
import com.google.cloud.tools.jib.image.DescriptorDigest;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Contains properties describing a BLOB, including its digest and possibly its size (in bytes).
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class BlobDescriptor {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull DescriptorDigest digest;

    /**
     * The size of the BLOB (in bytes). Negative if unknown.
     */
    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long size;

    /**
     * Creates a new {@link BlobDescriptor} from the contents of an {@link InputStream} while piping
     * to an {@link OutputStream}. Does not close either streams.
     */
    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor fromPipe(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull InputStream inputStream, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull OutputStream outputStream) throws IOException {
        CountingDigestOutputStream countingDigestOutputStream = new CountingDigestOutputStream(outputStream);
        ByteStreams.copy(inputStream, countingDigestOutputStream);
        countingDigestOutputStream.flush();
        return countingDigestOutputStream.toBlobDescriptor();
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    public BlobDescriptor( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long size, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest digest) {
        this.size = size;
        this.digest = digest;
    }

    /**
     * Initialize with just digest.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public BlobDescriptor(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest digest) {
        this(-1, digest);
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean hasSize() {
        return size >= 0;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable DescriptorDigest getDigest() {
        return digest;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long getSize() {
        return size;
    }

    /**
     * Two {@link BlobDescriptor} objects are equal if their
     *
     * <ol>
     *   <li>{@code digest}s are not null and equal, and
     *   <li>{@code size}s are non-negative and equal
     * </ol>
     */
    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean equals(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (size < 0 || !(obj instanceof BlobDescriptor)) {
            return false;
        }
        BlobDescriptor other = (BlobDescriptor) obj;
        return size == other.getSize() && digest.equals(other.getDigest());
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int hashCode(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor this) {
        int result = digest.hashCode();
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }
}
