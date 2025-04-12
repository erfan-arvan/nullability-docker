package com.namelessmc.java_api.integrations;
public class MinecraftIntegrationData extends IntegrationData implements IMinecraftIntegrationData {
	private final  UUID uuid;
	public MinecraftIntegrationData(final  UUID uuid,
									final  String username) {
		super(StandardIntegrationTypes.MINECRAFT, uuid.toString(), username);
		this.uuid = uuid;
	}
	public  UUID getUniqueId() {
		return this.uuid;
	}
}
