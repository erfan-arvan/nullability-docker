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

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface BuildLogger {

    @org.checkerframework.dataflow.qual.Impure
    void debug(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CharSequence message);

    @org.checkerframework.dataflow.qual.Impure
    void info(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CharSequence message);

    @org.checkerframework.dataflow.qual.Impure
    void warn(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull CharSequence message);

    @org.checkerframework.dataflow.qual.Impure
    void error(CharSequence message);
}
