package com.namelessmc.java_api.integrations;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface IMinecraftIntegrationData {

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UUID getUniqueId();
}
