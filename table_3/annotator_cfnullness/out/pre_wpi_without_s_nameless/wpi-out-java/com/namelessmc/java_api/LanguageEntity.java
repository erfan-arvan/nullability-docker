package com.namelessmc.java_api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface LanguageEntity {

    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getLanguage() throws NamelessException;

    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getLanguagePosix() throws NamelessException;
}
