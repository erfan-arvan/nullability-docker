package com.namelessmc.java_api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class ApiError extends NamelessException {

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNKNOWN_ERROR = 0;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_API_KEY = 1;

    // 2 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_API_METHOD = 3;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int NO_UNIQUE_SITE_ID_AVAILABLE = 4;

    // 5 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_GET_POST_CONTENTS = 6;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_EMAIL_ADDRESS = 7;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_USERNAME = 8;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_UUID = 9;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int EMAIL_ALREADY_EXISTS = 10;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int USERNAME_ALREADY_EXISTS = 11;

    // 12 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_CREATE_ACCOUNT = 13;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_SEND_REGISTRATION_EMAIL = 14;

    // 15 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_FIND_USER = 16;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_FIND_GROUP = 17;

    // 18 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int REPORT_CONTENT_TOO_LARGE = 19;

    // 20 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int USER_CREATING_REPORT_BANNED = 21;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int USER_ALREADY_HAS_OPEN_REPORT = 22;

    // 23 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_UPDATE_USERNAME = 24;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_UPDATE_SERVER_INFO = 25;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int CANNOT_REPORT_YOURSELF = 26;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_SERVER_ID = 27;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_VALIDATE_CODE = 28;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_SET_USER_DISCORD_ID = 29;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_SET_DISCORD_BOT_URL = 30;

    // 31 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int ACCOUNT_ALREADY_ACTIVATED = 32;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int UNABLE_TO_SET_DISCORD_GUILD_ID = 33;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int DISCORD_INTEGRATION_DISABLED = 34;

    // 35 intentionally missing
    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int REQUEST_NOT_AUTHORIZED = 36;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INVALID_INTEGRATION = 37;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INTEGRATION_USERNAME_ALREADY_EXISTS = 38;

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int INTEGRATION_ID_ALREADY_EXISTS = 39;

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long serialVersionUID = 3093028909912281912L;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int code;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String meta;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public ApiError(final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int code, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String meta) {
        super("An unexpected API error occurred with error code " + code + " and " + (meta == null ? "no meta" : "meta " + meta));
        this.code = code;
        this.meta = meta;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getError() {
        return this.code;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Optional<String> getMeta() {
        return Optional.ofNullable(meta);
    }
}
