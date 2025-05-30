package com.namelessmc.java_api;



import java.util.HashMap;
import java.util.Map;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class LanguageCodeMap {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, String> NAMELESS_TO_POSIX = new HashMap<>();

    static {
        NAMELESS_TO_POSIX.put("Czech", "cs_CZ");
        NAMELESS_TO_POSIX.put("German", "de_DE");
        NAMELESS_TO_POSIX.put("Greek", "el_GR");
        NAMELESS_TO_POSIX.put("EnglishUK", "en_UK");
        NAMELESS_TO_POSIX.put("EnglishUS", "en_US");
        NAMELESS_TO_POSIX.put("Spanish", "es_419");
        NAMELESS_TO_POSIX.put("SpanishES", "es_ES");
        NAMELESS_TO_POSIX.put("French", "fr_FR");
        NAMELESS_TO_POSIX.put("Hungarian", "hu_HU");
        NAMELESS_TO_POSIX.put("Italian", "it_IT");
        NAMELESS_TO_POSIX.put("Lithuanian", "lt_LT");
        NAMELESS_TO_POSIX.put("Norwegian", "nb_NO");
        NAMELESS_TO_POSIX.put("Dutch", "nl_NL");
        NAMELESS_TO_POSIX.put("Polish", "pl_PL");
        NAMELESS_TO_POSIX.put("Portuguese", "pt_BR");
        NAMELESS_TO_POSIX.put("Romanian", "ro_RO");
        NAMELESS_TO_POSIX.put("Russian", "ru_RU");
        NAMELESS_TO_POSIX.put("Slovak", "sk_SK");
        NAMELESS_TO_POSIX.put("SwedishSE", "sv_SE");
        NAMELESS_TO_POSIX.put("Turkish", "tr_TR");
        NAMELESS_TO_POSIX.put("Chinese(Simplified)", "zh_CN");
    }

    @org.checkerframework.dataflow.qual.Pure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getLanguagePosix(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String language) {
        return NAMELESS_TO_POSIX.get(language);
    }
}
