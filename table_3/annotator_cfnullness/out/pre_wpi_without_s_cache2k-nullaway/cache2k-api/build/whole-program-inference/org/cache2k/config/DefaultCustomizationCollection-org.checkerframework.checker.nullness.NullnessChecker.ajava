package org.cache2k.config;

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
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Default implementation for a customization collection using a list.
 *
 * <p>Rationale: Inserting is a little expansive, since we check whether
 * an entry is already existing. Since there are usually only a few entries
 * the list is sufficient.
 *
 * @author Jens Wilke
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public final class DefaultCustomizationCollection<T> extends AbstractCollection<CustomizationSupplier<T>> implements CustomizationCollection<T> {

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Collection<CustomizationSupplier<T>> list = new ArrayList<CustomizationSupplier<T>>();

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int size(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DefaultCustomizationCollection<T> this) {
        return list.size();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Iterator<CustomizationSupplier<T>> iterator(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DefaultCustomizationCollection<T> this) {
        return list.iterator();
    }

    /**
     * Adds a customization to the collection.
     *
     * @return always {@code true}
     * @throws IllegalArgumentException if the entry is already existing.
     */
    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean add(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DefaultCustomizationCollection<T> this, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CustomizationSupplier<T> entry) {
        if (list.contains(entry)) {
            throw new IllegalArgumentException("duplicate entry");
        }
        return list.add(entry);
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String toString(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DefaultCustomizationCollection<T> this) {
        return getClass().getSimpleName() + list.toString();
    }
}
