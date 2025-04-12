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
package com.google.cloud.tools.jib.http;

import com.google.api.client.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Static initializers for {@link Authorization}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Authorizations {

    /**
     * Creates an {@link Authorization} with a {@code Bearer} token.
     */
    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Authorization withBearerToken(@org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.NonNull String token) {
        return new Authorization("Bearer", token);
    }

    /**
     * Creates an {@link Authorization} with a {@code Basic} credentials.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Authorization withBasicCredentials(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String username, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String secret) {
        String credentials = username + ":" + secret;
        String token = new String(Base64.encodeBase64(credentials.getBytes(StandardCharsets.US_ASCII)), StandardCharsets.UTF_8);
        return new Authorization("Basic", token);
    }

    /**
     * Creates an {@link Authorization} with a base64-encoded {@code username:password} string.
     */
    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Authorization withBasicToken(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String token) {
        return new Authorization("Basic", token);
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    private Authorizations() {
    }
}
