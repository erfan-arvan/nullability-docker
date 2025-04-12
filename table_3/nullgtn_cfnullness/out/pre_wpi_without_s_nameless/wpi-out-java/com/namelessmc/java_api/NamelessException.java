package com.namelessmc.java_api;

import org.jetbrains.annotations.NotNull;

/**
 * Generic exception thrown by many methods in the Nameless API
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class NamelessException extends Exception {

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long serialVersionUID = -3698433855091611529L;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public NamelessException(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String message) {
        super(message);
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    public NamelessException(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String message, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Throwable cause) {
        super(message, cause);
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    public NamelessException(final Throwable cause) {
        super(cause);
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    public NamelessException() {
        super();
    }
}
