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
import org.cache2k.Cache2kBuilder;
import java.util.Iterator;

/**
 * Base class for a cache feature that can be enabled or disables and
 * appears only once in the feature set. A feature can be disabled in
 * two ways: Either by setting enabled to {@code false} or by removing
 * it from the feature set.
 *
 * <p>This allows enablement of features based on the users preference:
 * A feature can be enabled by default and then disabled per individual
 * cache or just enabled at the individual cache level.
 *
 * @author Jens Wilke
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public abstract class ToggleFeature implements SingleFeature {

    /**
     * Enable the feature in the main configuration. If the feature is
     * already existing it is replaced by a newly created one.
     *
     * @return the created feature to set additional parameters
     */
    @org.checkerframework.dataflow.qual.Impure
    public static <T extends ToggleFeature> T enable(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache2kBuilder<?, ?> builder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> featureType) {
        try {
            T feature = featureType.getConstructor().newInstance();
            builder.config().getFeatures().add(feature);
            return feature;
        } catch (Exception e) {
            throw new LinkageError("Instantiation failed", e);
        }
    }

    /**
     * Disable the feature by removing it from the configuration.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static void disable(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache2kBuilder<?, ?> builder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<? extends ToggleFeature> featureType) {
        Iterator<Feature> it = builder.config().getFeatures().iterator();
        while (it.hasNext()) {
            if (it.next().getClass().equals(featureType)) {
                it.remove();
            }
        }
    }

    /**
     * Returns the feature instance, if present.
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.nullness.qual.Nullable
    public static <T extends ToggleFeature> T extract(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache2kBuilder<?, ?> builder, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Class<T> featureType) {
        Iterator<Feature> it = builder.config().getFeatures().iterator();
        while (it.hasNext()) {
            Feature feature = it.next();
            if (feature.getClass().equals(featureType)) {
                return (T) feature;
            }
        }
        return null;
    }

    /**
     * Returns true if the feature is enabled. Meaning, the feature instance is present
     * and enabled.
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public static boolean isEnabled(Cache2kBuilder<?, ?> builder, Class<? extends ToggleFeature> featureType) {
        ToggleFeature f = extract(builder, featureType);
        return f != null ? f.isEnabled() : false;
    }

    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    private boolean enabled = true;

    /**
     * Check whether enabled and call implementations' doEnlist method.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public final void enlist(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ToggleFeature this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheBuildContext<?, ?> ctx) {
        if (enabled) {
            doEnlist(ctx);
        }
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    protected abstract void doEnlist(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheBuildContext<?, ?> ctx);

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public final boolean isEnabled() {
        return enabled;
    }

    @org.checkerframework.dataflow.qual.Impure
    public final void setEnabled(boolean v) {
        this.enabled = v;
    }

    /**
     * Alternate setter for spelling flexibility in XML configuration.
     */
    @org.checkerframework.dataflow.qual.Impure
    public final void setEnable(boolean v) {
        this.enabled = v;
    }

    /**
     * Identical if its the same implementation class.
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public final boolean equals(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ToggleFeature this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object o) {
        return getClass().equals(o);
    }

    /**
     * Hashcode from the implementation class.
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public final int hashCode(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ToggleFeature this) {
        return getClass().hashCode();
    }

    /**
     * Override if this takes additional parameters.
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public String toString(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ToggleFeature this) {
        return getClass().getSimpleName() + '{' + (enabled ? "enabled" : "disabled") + '}';
    }
}
