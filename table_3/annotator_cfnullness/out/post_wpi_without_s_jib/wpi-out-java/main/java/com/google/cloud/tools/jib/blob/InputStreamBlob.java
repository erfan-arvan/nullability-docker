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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@link Blob} that holds an {@link InputStream}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class InputStreamBlob implements Blob {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull InputStream inputStream;

    /**
     * Indicates if the {@link Blob} has already been written or not.
     */
    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isWritten = false;

    @org.checkerframework.dataflow.qual.SideEffectFree
    InputStreamBlob(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BlobDescriptor writeTo(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull InputStreamBlob this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull OutputStream outputStream) throws IOException {
        // Cannot rewrite.
        if (isWritten) {
            throw new IllegalStateException("Cannot rewrite Blob backed by an InputStream");
        }
        try (InputStream inputStream = this.inputStream) {
            return BlobDescriptor.fromPipe(inputStream, outputStream);
        } finally {
            isWritten = true;
        }
    }
}
