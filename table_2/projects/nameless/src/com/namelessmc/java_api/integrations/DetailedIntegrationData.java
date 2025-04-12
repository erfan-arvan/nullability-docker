package com.namelessmc.java_api.integrations;
public class DetailedIntegrationData extends IntegrationData {
	private final boolean verified;
	private final  Date linkedDate;
	private final boolean shownPublicly;
	public DetailedIntegrationData(final  String integrationType,
							final  String identifier,
							final  String username,
							final boolean verified,
							final  Date linkedDate,
							final boolean shownPublicly) {
		super(integrationType, identifier, username);
		this.verified = verified;
		this.linkedDate = linkedDate;
		this.shownPublicly = shownPublicly;
	}
	public DetailedIntegrationData(final  JsonObject json) {
		this(
				json.get("integration").getAsString(),
				json.get("identifier").getAsString(),
				json.get("username").getAsString(),
				json.get("verified").getAsBoolean(),
				new Date(json.get("linked_date").getAsLong()),
				json.get("show_publicly").getAsBoolean()
		);
	}
	public boolean isVerified() {
		return verified;
	}
	public  Date getLinkedDate() {
		return this.linkedDate;
	}
	public boolean isShownPublicly() {
		return this.shownPublicly;
	}
}
