package org.cache2k.io;

/*
 * #%L
 * cache2k API
 * %%
 * Copyright (C) 2000 - 2020 headissue GmbH, Munich
 * %%
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
 * #L%
 */
import org.cache2k.CacheEntry;

/**
 * Relevant information of a load attempt that generated an exception.
 * This is used by the exception propagator and the resilience policy.
 *
 * <p>Compatibility: This interface is not intended for implementation
 * or extension by a client and may get additional methods in a new minor release.
 *
 * <p>Rationale: The information does not contain the time of the original
 * expiry of the cache value. This is intentional. There is no information
 * field to record the time of expiry past the expiry itself. The expiry time
 * information is reset as soon as the expiry time is reached. To produce no
 * overhead, the information provided by this interface is captured only
 * in case an exception happens.
 *
 * @see ExceptionPropagator
 * @see ResiliencePolicy
 * @author Jens Wilke
 * @since 2
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface LoadExceptionInfo<K> extends CacheEntry<K, Void> {

    /**
     * The key of the entry.
     * Inherited from {@link CacheEntry}
     */
    @org.checkerframework.dataflow.qual.Pure
    K getKey();

    /**
     * Always throws exception based on exception propagator semantics.
     * Inherited from {@link CacheEntry}
     */
    @org.checkerframework.dataflow.qual.Pure
    default Void getValue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LoadExceptionInfo<K> this) {
        throw generateExceptionToPropagate();
    }

    /**
     * The original exception generated by the last recent loader call.
     * Inherited from {@link CacheEntry}
     */
    @org.checkerframework.dataflow.qual.Pure
    Throwable getException();

    /**
     * Returns ourselves. Useful because this implements {@link CacheEntry}
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default LoadExceptionInfo<K> getExceptionInfo(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull LoadExceptionInfo<K> this) {
        return this;
    }

    /**
     * Generate the exception to propagate with the exception propagator
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    default RuntimeException generateExceptionToPropagate() {
        return getExceptionPropagator().propagateException(this);
    }

    /**
     * The exception propagator in effect.
     */
    @org.checkerframework.dataflow.qual.Pure
    ExceptionPropagator getExceptionPropagator();

    /**
     * Number of retry attempts to load the value for the requested key.
     * The value is starting 0 for the first load attempt that yields an
     * exception. The counter is incremented for each consecutive
     * loader exception. After a successful attempt to load the value the
     * counter is reset.
     *
     * @return counter starting at 0 for the first load attempt that
     *         yields an exception.
     */
    @org.checkerframework.dataflow.qual.Pure
    int getRetryCount();

    /**
     * Start time of the load that generated the first exception.
     *
     * @return time in millis since epoch
     */
    @org.checkerframework.dataflow.qual.Pure
    long getSinceTime();

    /**
     * Start time of the load operation that generated the recent exception.
     *
     * @return time in millis since epoch
     */
    @org.checkerframework.dataflow.qual.Pure
    long getLoadTime();

    /**
     * Time in millis until the next retry attempt.
     * This property is only set in the context of the {@link ExceptionPropagator}.
     *
     * @return time in millis since epoch
     */
    @org.checkerframework.dataflow.qual.Pure
    long getUntil();
}
