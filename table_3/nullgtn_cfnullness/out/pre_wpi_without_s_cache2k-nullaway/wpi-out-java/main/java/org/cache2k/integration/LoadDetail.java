package org.cache2k.integration;

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
/**
 * @author Jens Wilke
 * @deprecated to be removed in version 2.2
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public abstract class LoadDetail<V> {

    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    private Object value;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public LoadDetail(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull final Object valueOrWrapper) {
        value = valueOrWrapper;
    }

    @org.checkerframework.dataflow.qual.Impure
    public V getValue() {
        if (value instanceof LoadDetail) {
            return ((LoadDetail<V>) value).getValue();
        }
        return (V) value;
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    public LoadDetail<V> getNextInChain() {
        if (value instanceof LoadDetail) {
            return ((LoadDetail<V>) value);
        }
        return null;
    }
}
