package com.namelessmc.java_api.integrations;
public class IntegrationData {
	private final  String integrationType;
	private final  String identifier;
	private final  String username;
	public IntegrationData(final  String integrationType,
					final  String identifier,
					final  String username) {
		this.integrationType = integrationType;
		this.identifier = identifier;
		this.username = username;
	}
	public  String getIntegrationType() {
		return this.integrationType;
	}
	public  String getIdentifier() {
		return this.identifier;
	}
	public  String getUsername() {
		return this.username;
	}
}
