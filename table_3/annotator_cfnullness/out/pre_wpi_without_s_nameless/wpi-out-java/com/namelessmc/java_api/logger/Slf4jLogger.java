package com.namelessmc.java_api.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Slf4jLogger extends ApiLogger {

    public static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Slf4jLogger DEFAULT_INSTANCE = new Slf4jLogger(LoggerFactory.getLogger("Nameless-Java-API Debug"));

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Logger logger;

    @org.checkerframework.dataflow.qual.Impure
    public Slf4jLogger(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Logger logger) {
        this.logger = logger;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void log(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Slf4jLogger this, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String string) {
        this.logger.info(string);
    }
}
