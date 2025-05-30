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
/**
 * Configuration options for a cache manager. The options can only be changed if a
 * XML file is provided. This bean is in the API artifact for documentation purposes.
 *
 * @author Jens Wilke
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Cache2kManagerConfig implements ConfigBean<Cache2kManagerConfig, Cache2kManagerConfig.Builder> {

    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.MonotonicNonNull
    @Nullable
    private String version = null;

    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.MonotonicNonNull
    @Nullable
    private String defaultManagerName = null;

    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    private boolean ignoreMissingCacheConfiguration = false;

    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    private boolean skipCheckOnStartup = false;

    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    private boolean ignoreAnonymousCache = false;

    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public boolean isIgnoreMissingCacheConfiguration() {
        return ignoreMissingCacheConfiguration;
    }

    /**
     * Configure a cache with default parameters if configuration has no specific section for it.
     */
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Impure
    public void setIgnoreMissingCacheConfiguration(boolean f) {
        ignoreMissingCacheConfiguration = f;
    }

    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    public String getDefaultManagerName() {
        return defaultManagerName;
    }

    /**
     * Replace the default name of the default cache manager.
     */
    @org.checkerframework.checker.nullness.qual.EnsuresNonNull({ "this.defaultManagerName" })
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Impure
    public void setDefaultManagerName(String v) {
        defaultManagerName = v;
    }

    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.Nullable
    public String getVersion() {
        return version;
    }

    /**
     * Version of the configuration. Mandatory in every cache configuration. The version affects
     * how the configuration XML file is interpreted.
     */
    @org.checkerframework.checker.nullness.qual.EnsuresNonNull({ "this.version" })
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Impure
    public void setVersion(String v) {
        version = v;
    }

    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public boolean isSkipCheckOnStartup() {
        return skipCheckOnStartup;
    }

    /**
     * The configuration for each cache is parsed and checked as soon as the cache manager is created.
     */
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Impure
    public void setSkipCheckOnStartup(boolean f) {
        skipCheckOnStartup = f;
    }

    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized
    @org.checkerframework.checker.nullness.qual.NonNull
    public boolean isIgnoreAnonymousCache() {
        return ignoreAnonymousCache;
    }

    /**
     * When a configuration is present, every cache needs a cache name so that the configuration
     * can be applied.
     */
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.defaultManagerName" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.framework.qual.EnsuresQualifier(expression = { "this.version" }, qualifier = org.checkerframework.checker.nullness.qual.Nullable.class)
    @org.checkerframework.dataflow.qual.Impure
    public void setIgnoreAnonymousCache(boolean f) {
        ignoreAnonymousCache = f;
    }

    /**
     * Not supported, but will eventually get one.
     */
    @org.checkerframework.dataflow.qual.Pure
    public Builder builder(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Cache2kManagerConfig this) {
        throw new UnsupportedOperationException();
    }

    public static class Builder implements ConfigBuilder<Builder, Cache2kManagerConfig> {

        @org.checkerframework.dataflow.qual.Pure
        @org.checkerframework.checker.initialization.qual.FBCBottom
        public Cache2kManagerConfig config(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder this) {
            return null;
        }
    }
}
