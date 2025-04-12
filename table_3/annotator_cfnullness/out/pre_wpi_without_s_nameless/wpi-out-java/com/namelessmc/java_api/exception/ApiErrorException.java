package com.namelessmc.java_api.exception;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ApiErrorException extends Exception {

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long serialVersionUID = 1L;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public ApiErrorException(final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int code) {
        super("API error code " + code);
    }
}
