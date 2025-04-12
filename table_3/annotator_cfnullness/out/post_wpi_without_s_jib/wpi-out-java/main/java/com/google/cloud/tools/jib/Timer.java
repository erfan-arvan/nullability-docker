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

import com.google.cloud.tools.jib.builder.BuildLogger;
import java.io.Closeable;
import javax.annotation.Nullable;

/**
 * Times execution intervals. This is only for testing purposes and will be removed before the first
 * release.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Timer implements Closeable {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int depth;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String label;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long startTime = System.nanoTime();

    @org.checkerframework.dataflow.qual.Impure
    public Timer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String label) {
        this(buildLogger, label, 0);
    }

    @org.checkerframework.dataflow.qual.Impure
    private Timer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BuildLogger buildLogger, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String label,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int depth) {
        this.buildLogger = buildLogger;
        this.label = label;
        this.depth = depth;
        if (buildLogger != null) {
            buildLogger.debug(getTabs().append("TIMING\t").append(label));
            if (depth == 0) {
                buildLogger.info("RUNNING\t" + label);
            }
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Timer subTimer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String label) {
        return new Timer(buildLogger, label, depth + 1);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void lap(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String label) {
        if (this.label == null) {
            throw new IllegalStateException("Tried to lap Timer after closing");
        }
        if (buildLogger != null) {
            double timeInMillis = (System.nanoTime() - startTime) / 1000 / 1000.0;
            buildLogger.debug(getTabs().append("TIMED\t").append(this.label).append(" : ").append(timeInMillis).append(" ms"));
            if (depth == 0) {
                buildLogger.info(this.label + " : " + timeInMillis + " ms");
            }
        }
        this.label = label;
        startTime = System.nanoTime();
    }

    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull StringBuilder getTabs() {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            tabs.append("\t");
        }
        return tabs;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void close(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Timer this) {
        lap(null);
    }
}
