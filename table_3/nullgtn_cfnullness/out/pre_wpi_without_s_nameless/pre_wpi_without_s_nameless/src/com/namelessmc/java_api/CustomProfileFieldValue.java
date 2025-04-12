package com.namelessmc.java_api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomProfileFieldValue {

    private final CustomProfileField field;

    private final String value;

    CustomProfileFieldValue(CustomProfileField field, String value) {
        this.field = field;
        this.value = value;
    }

    public CustomProfileField getField() {
        return this.field;
    }

    public String getValue() {
        return value;
    }
}
