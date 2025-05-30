/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.picasso3;

/**
 * Designates the policy to use for network requests.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public enum NetworkPolicy {

    /**
     * Skips checking the disk cache and forces loading through the network.
     */
    NO_CACHE(1 << 0),
    /**
     * Skips storing the result into the disk cache.
     */
    NO_STORE(1 << 1),
    /**
     * Forces the request through the disk cache only, skipping network.
     */
    OFFLINE(1 << 2);

    @org.checkerframework.dataflow.qual.Pure
    public static  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean shouldReadFromDiskCache( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int networkPolicy) {
        return (networkPolicy & NetworkPolicy.NO_CACHE.index) == 0;
    }

    @org.checkerframework.dataflow.qual.Pure
    public static  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean shouldWriteToDiskCache( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int networkPolicy) {
        return (networkPolicy & NetworkPolicy.NO_STORE.index) == 0;
    }

    @org.checkerframework.dataflow.qual.Pure
    public static  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isOfflineOnly( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int networkPolicy) {
        return (networkPolicy & NetworkPolicy.OFFLINE.index) != 0;
    }

    final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int index;

    @org.checkerframework.dataflow.qual.Impure
    NetworkPolicy( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int index) {
        this.index = index;
    }
}
