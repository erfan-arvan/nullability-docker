package com.namelessmc.java_api.integrations;

import org.jetbrains.annotations.NotNull;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DiscordIntegrationData extends IntegrationData {

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long id;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public DiscordIntegrationData(final long id, final String username) {
        super(StandardIntegrationTypes.DISCORD, String.valueOf(id), username);
        this.id = id;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long getIdLong() {
        return this.id;
    }
}
