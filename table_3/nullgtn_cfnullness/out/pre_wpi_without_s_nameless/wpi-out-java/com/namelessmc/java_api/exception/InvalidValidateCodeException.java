package com.namelessmc.java_api.exception;

import com.namelessmc.java_api.ApiError;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class InvalidValidateCodeException extends ApiErrorException {

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long serialVersionUID = 1L;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public InvalidValidateCodeException() {
        super(ApiError.INVALID_VALIDATE_CODE);
    }
}
