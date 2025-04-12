package com.namelessmc.java_api;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Notification {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String message;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String url;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NotificationType type;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public Notification(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String message, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String url, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NotificationType type) {
        this.message = message;
        this.url = url;
        this.type = type;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getMessage() {
        return this.message;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getUrl() {
        return this.url;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NotificationType getType() {
        return this.type;
    }

    public enum NotificationType {

        TAG,
        MESSAGE,
        LIKE,
        PROFILE_COMMENT,
        COMMENT_REPLY,
        THREAD_REPLY,
        FOLLOW,
        UNKNOWN;

        @org.checkerframework.dataflow.qual.Impure
        public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NotificationType fromString(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String string) {
            try {
                return NotificationType.valueOf(string.replace('-', '_').toUpperCase());
            } catch (final IllegalArgumentException e) {
                return NotificationType.UNKNOWN;
            }
        }
    }
}
