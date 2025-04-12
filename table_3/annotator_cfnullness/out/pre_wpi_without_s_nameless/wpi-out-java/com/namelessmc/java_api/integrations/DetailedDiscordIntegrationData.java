package com.namelessmc.java_api.integrations;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DetailedDiscordIntegrationData extends DetailedIntegrationData implements IDiscordIntegrationData {

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long idLong;

    @org.checkerframework.dataflow.qual.Impure
    public DetailedDiscordIntegrationData(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull JsonObject json) {
        super(json);
        this.idLong = Integer.parseInt(this.getIdentifier());
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long getIdLong(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DetailedDiscordIntegrationData this) {
        return this.idLong;
    }
}
