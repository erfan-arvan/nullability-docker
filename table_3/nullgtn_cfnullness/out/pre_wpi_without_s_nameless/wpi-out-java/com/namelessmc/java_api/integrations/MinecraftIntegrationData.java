package com.namelessmc.java_api.integrations;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class MinecraftIntegrationData extends IntegrationData implements IMinecraftIntegrationData {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UUID uuid;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public MinecraftIntegrationData(final UUID uuid, final String username) {
        super(StandardIntegrationTypes.MINECRAFT, uuid.toString(), username);
        this.uuid = uuid;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UUID getUniqueId(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull MinecraftIntegrationData this) {
        return this.uuid;
    }
}
