package com.namelessmc.java_api.integrations;

import com.google.gson.JsonObject;
import com.namelessmc.java_api.NamelessAPI;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class DetailedMinecraftIntegrationData extends DetailedIntegrationData implements IMinecraftIntegrationData {

    private final UUID uuid;

    public DetailedMinecraftIntegrationData(JsonObject json) {
        super(json);
        this.uuid = NamelessAPI.websiteUuidToJavaUuid(this.getIdentifier());
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }
}
