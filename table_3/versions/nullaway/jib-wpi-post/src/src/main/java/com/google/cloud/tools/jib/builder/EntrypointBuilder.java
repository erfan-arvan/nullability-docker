/*
 * Copyright 2018 Google Inc.
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
package com.google.cloud.tools.jib.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds an image entrypoint for the Java application.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class EntrypointBuilder {

    /**
     * Builds the container entrypoint.
     *
     * <p>The entrypoint is {@code java [jvm flags] -cp [classpaths] [main class]}.
     */
    @org.checkerframework.dataflow.qual.Impure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> makeEntrypoint(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SourceFilesConfiguration sourceFilesConfiguration, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<String> jvmFlags, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String mainClass) {
        List<String> classPaths = new ArrayList<>();
        classPaths.add(sourceFilesConfiguration.getDependenciesPathOnImage() + "*");
        classPaths.add(sourceFilesConfiguration.getResourcesPathOnImage());
        classPaths.add(sourceFilesConfiguration.getClassesPathOnImage());
        String classPathsString = String.join(":", classPaths);
        List<String> entrypoint = new ArrayList<>(4 + jvmFlags.size());
        entrypoint.add("java");
        entrypoint.addAll(jvmFlags);
        entrypoint.add("-cp");
        entrypoint.add(classPathsString);
        entrypoint.add(mainClass);
        return entrypoint;
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    private EntrypointBuilder() {
    }
}
