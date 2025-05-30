package com.namelessmc.java_api.integrations;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.UUID;

public class MinecraftIntegrationData extends IntegrationData implements IMinecraftIntegrationData {

    private final UUID uuid;

    public MinecraftIntegrationData(final UUID uuid, final String username) {
        super(StandardIntegrationTypes.MINECRAFT, uuid.toString(), username);
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }
}
