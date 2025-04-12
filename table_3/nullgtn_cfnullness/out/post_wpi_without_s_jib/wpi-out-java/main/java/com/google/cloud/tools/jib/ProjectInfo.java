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
package com.google.cloud.tools.jib;

/**
 * Constants relating to the Jib project.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ProjectInfo {

    /**
     * Link to the GitHub repository.
     */
    public static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String GITHUB_URL = "https://github.com/google/jib";

    /**
     * Link to file an issue against the GitHub repository.
     */
    public static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String GITHUB_NEW_ISSUE_URL = GITHUB_URL + "/issues/new";

    @org.checkerframework.dataflow.qual.SideEffectFree
    private ProjectInfo() {
    }
}
