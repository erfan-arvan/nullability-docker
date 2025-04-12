package com.namelessmc.java_api.integrations;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import java.util.Date;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DetailedIntegrationData extends IntegrationData {

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean verified;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Date linkedDate;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean shownPublicly;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public DetailedIntegrationData(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String integrationType, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String identifier, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String username, final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean verified, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Date linkedDate, final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean shownPublicly) {
        super(integrationType, identifier, username);
        this.verified = verified;
        this.linkedDate = linkedDate;
        this.shownPublicly = shownPublicly;
    }

    @org.checkerframework.dataflow.qual.Impure
    public DetailedIntegrationData(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull JsonObject json) {
        this(json.get("integration").getAsString(), json.get("identifier").getAsString(), json.get("username").getAsString(), json.get("verified").getAsBoolean(), new Date(json.get("linked_date").getAsLong()), json.get("show_publicly").getAsBoolean());
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isVerified() {
        return verified;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Date getLinkedDate() {
        return this.linkedDate;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isShownPublicly() {
        return this.shownPublicly;
    }
}
