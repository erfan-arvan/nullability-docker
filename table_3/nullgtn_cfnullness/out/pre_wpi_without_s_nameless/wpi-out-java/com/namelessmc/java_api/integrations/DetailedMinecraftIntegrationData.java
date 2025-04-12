package com.namelessmc.java_api.integrations;

import com.google.gson.JsonObject;
import com.namelessmc.java_api.NamelessAPI;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DetailedMinecraftIntegrationData extends DetailedIntegrationData implements IMinecraftIntegrationData {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UUID uuid;

    @org.checkerframework.dataflow.qual.Impure
    public DetailedMinecraftIntegrationData(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull JsonObject json) {
        super(json);
        this.uuid = NamelessAPI.websiteUuidToJavaUuid(this.getIdentifier());
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull UUID getUniqueId(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DetailedMinecraftIntegrationData this) {
        return this.uuid;
    }
}
