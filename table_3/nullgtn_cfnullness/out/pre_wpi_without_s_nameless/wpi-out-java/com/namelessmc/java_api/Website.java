package com.namelessmc.java_api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.namelessmc.java_api.exception.UnknownNamelessVersionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Website implements LanguageEntity {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String version;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Update update;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull [] modules;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String language;

    @org.checkerframework.dataflow.qual.Impure
    Website(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull JsonObject json) {
        Objects.requireNonNull(json, "Provided json object is null");
        this.version = json.get("nameless_version").getAsString();
        this.modules = StreamSupport.stream(json.get("modules").getAsJsonArray().spliterator(), false).map(JsonElement::getAsString).toArray(String[]::new);
        if (json.has("version_update")) {
            final JsonObject updateJson = json.get("version_update").getAsJsonObject();
            final boolean updateAvailable = updateJson.get("update").getAsBoolean();
            if (updateAvailable) {
                final String updateVersion = updateJson.get("version").getAsString();
                final boolean isUrgent = updateJson.get("urgent").getAsBoolean();
                this.update = new Update(isUrgent, updateVersion);
            } else {
                this.update = null;
            }
        } else {
            this.update = null;
        }
        this.language = json.get("language").getAsString();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getVersion() {
        return this.version;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessVersion getParsedVersion() throws UnknownNamelessVersionException {
        return NamelessVersion.parse(this.version);
    }

    /**
     * @return Information about an update, or empty if no update is available.
     */
    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Optional<Update> getUpdate() {
        return Optional.ofNullable(this.update);
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull [] getModules() {
        return this.modules;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getLanguage(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Website this) {
        return this.language;
    }

    /**
     * Get POSIX code for website language (uses lookup table)
     * @return Language code or null if the website's language does not exist in our lookup table
     */
    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getLanguagePosix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Website this) {
        return LanguageCodeMap.getLanguagePosix(this.language);
    }

    public static class Update {

        private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isUrgent;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String version;

        @org.checkerframework.dataflow.qual.SideEffectFree
        Update(final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isUrgent, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String version) {
            this.isUrgent = isUrgent;
            this.version = version;
        }

        @org.checkerframework.dataflow.qual.Pure
        public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isUrgent() {
            return this.isUrgent;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getVersion() {
            return this.version;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessVersion getParsedVersion() throws UnknownNamelessVersionException {
            return NamelessVersion.parse(this.version);
        }
    }
}
