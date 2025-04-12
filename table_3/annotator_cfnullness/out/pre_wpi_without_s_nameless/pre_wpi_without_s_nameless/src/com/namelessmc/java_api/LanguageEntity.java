package com.namelessmc.java_api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LanguageEntity {

    String getLanguage() throws NamelessException;

    String getLanguagePosix() throws NamelessException;
}
