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
package com.google.cloud.tools.jib.cache.json;

import com.google.cloud.tools.jib.json.JsonTemplate;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Inner JSON template for extra properties for an application layer, as part of {@link
 * CacheMetadataLayerObjectTemplate}.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class CacheMetadataLayerPropertiesObjectTemplate implements JsonTemplate {

    /**
     * The paths to the source files that the layer was constructed from.
     */
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> sourceFiles = new ArrayList<>();

    /**
     * The last time the layer was constructed.
     */
    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long lastModifiedTime;

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> getSourceFiles() {
        return sourceFiles;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull FileTime getLastModifiedTime() {
        return FileTime.fromMillis(lastModifiedTime);
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheMetadataLayerPropertiesObjectTemplate setSourceFiles(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> sourceFiles) {
        this.sourceFiles = sourceFiles;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CacheMetadataLayerPropertiesObjectTemplate setLastModifiedTime(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull FileTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime.toMillis();
        return this;
    }
}
