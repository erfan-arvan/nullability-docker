package com.namelessmc.java_api;

import com.namelessmc.java_api.exception.UnknownNamelessVersionException;
import org.jetbrains.annotations.NotNull;
import java.util.*;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public enum NamelessVersion {

    V2_0_0_PR_7("2.0.0-pr7", "2.0.0 pre-release 7", 2, 0, true),
    V2_0_0_PR_8("2.0.0-pr8", "2.0.0 pre-release 8", 2, 0, true),
    V2_0_0_PR_9("2.0.0-pr9", "2.0.0 pre-release 9", 2, 0, true),
    V2_0_0_PR_10("2.0.0-pr10", "2.0.0 pre-release 10", 2, 0, true),
    V2_0_0_PR_11("2.0.0-pr11", "2.0.0 pre-release 11", 2, 0, true),
    V2_0_0_PR_12("2.0.0-pr12", "2.0.0 pre-release 12", 2, 0, true),
    V2_0_0_PR_13("2.0.0-pr13", "2.0.0 pre-release 13", 2, 0, true);

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Set<NamelessVersion> SUPPORTED_VERSIONS = EnumSet.of(// Actually, only pr13 is supported but pr13 development releases still report as pr12
    V2_0_0_PR_12, V2_0_0_PR_13);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String name;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String friendlyName;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int major;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int minor;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isBeta;

    @org.checkerframework.dataflow.qual.Impure
    NamelessVersion(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String name, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String friendlyName, final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int major, final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int minor, final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isBeta) {
        this.name = name;
        this.friendlyName = friendlyName;
        this.major = major;
        this.minor = minor;
        this.isBeta = isBeta;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getName() {
        return this.name;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getFriendlyName() {
        return this.friendlyName;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getMajor() {
        return this.major;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getMinor() {
        return this.minor;
    }

    /**
     * @return True if this version is a release candidate, pre-release, beta, alpha.
     */
    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isBeta() {
        return this.isBeta;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String toString(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessVersion this) {
        return this.friendlyName;
    }

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, NamelessVersion> BY_NAME = new HashMap<>();

    static {
        for (final NamelessVersion version : values()) {
            BY_NAME.put(version.getName(), version);
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessVersion parse(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String versionName) throws UnknownNamelessVersionException {
        Objects.requireNonNull(versionName, "Version name is null");
        final NamelessVersion version = BY_NAME.get(versionName);
        if (version == null) {
            throw new UnknownNamelessVersionException(versionName);
        }
        return version;
    }

    /**
     * @return List of NamelessMC versions supported by the Java API
     */
    @org.checkerframework.dataflow.qual.Pure
    public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Set<NamelessVersion> getSupportedVersions() {
        return SUPPORTED_VERSIONS;
    }

    /**
     * @param version A version to check
     * @return Whether the provided NamelessMC version is supported by this Java API library.
     */
    @org.checkerframework.dataflow.qual.Pure
    public static  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isSupportedByJavaApi(final NamelessVersion version) {
        return SUPPORTED_VERSIONS.contains(version);
    }
}
