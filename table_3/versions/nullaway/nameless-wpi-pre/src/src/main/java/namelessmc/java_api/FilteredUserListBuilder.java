package com.namelessmc.java_api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class FilteredUserListBuilder {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessAPI api;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Map<UserFilter<?>, Object> filters;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String operator = "AND";

    @org.checkerframework.dataflow.qual.SideEffectFree
    FilteredUserListBuilder(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessAPI api) {
        this.api = api;
    }

    @org.checkerframework.dataflow.qual.Impure
    public <T> @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull FilteredUserListBuilder withFilter(UserFilter<T> filter, T value) {
        if (filters == null) {
            filters = new HashMap<>();
        }
        filters.put(filter, value);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull FilteredUserListBuilder all() {
        this.operator = "AND";
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull FilteredUserListBuilder any() {
        this.operator = "OR";
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<NamelessUser> makeRequest() throws NamelessException {
        final Object[] parameters;
        if (filters != null) {
            int filterCount = filters.size();
            parameters = new Object[filterCount * 2 + 2];
            parameters[0] = "operator";
            parameters[1] = operator;
            Iterator<Map.Entry<UserFilter<?>, Object>> iterator = filters.entrySet().iterator();
            for (int i = 1; i < filterCount; i++) {
                Map.Entry<UserFilter<?>, Object> entry = iterator.next();
                parameters[i * 2] = entry.getKey().getName();
                parameters[i * 2 + 1] = entry.getValue();
            }
        } else {
            parameters = new Object[0];
        }
        final JsonObject response = this.api.getRequestHandler().get("users", parameters);
        final JsonArray array = response.getAsJsonArray("users");
        final List<NamelessUser> users = new ArrayList<>(array.size());
        for (final JsonElement e : array) {
            final JsonObject o = e.getAsJsonObject();
            final int id = o.get("id").getAsInt();
            final String username = o.get("username").getAsString();
            final UUID uuid;
            if (o.has("uuid")) {
                final String uuidString = o.get("uuid").getAsString();
                if (uuidString == null || uuidString.equals("none") || uuidString.equals("")) {
                    uuid = null;
                } else {
                    uuid = NamelessAPI.websiteUuidToJavaUuid(uuidString);
                }
            } else {
                uuid = null;
            }
            users.add(new NamelessUser(this.api, id, username, true, uuid, false, -1L));
        }
        return Collections.unmodifiableList(users);
    }
}
