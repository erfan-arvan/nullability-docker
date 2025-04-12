package com.google.cloud.tools.jib.registry.credentials.json;
public class DockerConfigTemplate implements JsonTemplate {
  private static class AuthTemplate implements JsonTemplate {
     private String auth;
  }
  private final Map<String, AuthTemplate> auths = new HashMap<>();
   private String credsStore;
  private final Map<String, String> credHelpers = new HashMap<>();
  public String getAuthFor(String registry) {
    if (!auths.containsKey(registry)) {
      return null;
    }
    return auths.get(registry).auth;
  }
  public String getCredentialHelperFor(String registry) {
    if (credsStore != null && auths.containsKey(registry)) {
      return credsStore;
    }
    if (credHelpers.containsKey(registry)) {
      return credHelpers.get(registry);
    }
    return null;
  }
  @VisibleForTesting
  DockerConfigTemplate addAuth(String registry,  String auth) {
    AuthTemplate authTemplate = new AuthTemplate();
    authTemplate.auth = auth;
    auths.put(registry, authTemplate);
    return this;
  }
  @VisibleForTesting
  DockerConfigTemplate setCredsStore(String credsStore) {
    this.credsStore = credsStore;
    return this;
  }
  @VisibleForTesting
  DockerConfigTemplate addCredHelper(String registry, String credHelper) {
    credHelpers.put(registry, credHelper);
    return this;
  }
}
