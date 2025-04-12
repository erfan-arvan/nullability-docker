package com.namelessmc.java_api.integrations;
public class DetailedMinecraftIntegrationData extends DetailedIntegrationData implements IMinecraftIntegrationData {
	private final  UUID uuid;
	public DetailedMinecraftIntegrationData(final  JsonObject json) {
		super(json);
		this.uuid = NamelessAPI.websiteUuidToJavaUuid(this.getIdentifier());
	}
	@Override
	public  UUID getUniqueId() {
		return this.uuid;
	}
}
