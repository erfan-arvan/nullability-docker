package com.squareup.picasso3;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class MemoryPolicyTest {

    @org.checkerframework.dataflow.qual.Impure
    public void dontReadFromMemoryCache() {
        int memoryPolicy = 0;
        memoryPolicy |= MemoryPolicy.NO_CACHE.index;
        assertThat(MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy)).isFalse();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void readFromMemoryCache() {
        int memoryPolicy = 0;
        memoryPolicy |= MemoryPolicy.NO_STORE.index;
        assertThat(MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy)).isTrue();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void dontWriteToMemoryCache() {
        int memoryPolicy = 0;
        memoryPolicy |= MemoryPolicy.NO_STORE.index;
        assertThat(MemoryPolicy.shouldWriteToMemoryCache(memoryPolicy)).isFalse();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void writeToMemoryCache() {
        int memoryPolicy = 0;
        memoryPolicy |= MemoryPolicy.NO_CACHE.index;
        assertThat(MemoryPolicy.shouldWriteToMemoryCache(memoryPolicy)).isTrue();
    }
}
