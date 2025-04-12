package com.google.cloud.tools.jib.registry.credentials;
public class RegistryCredentials {
  private static final RegistryCredentials EMPTY = new RegistryCredentials();
  public static RegistryCredentials none() {
    return EMPTY;
  }
  public static RegistryCredentials of(
      String registry, String credentialSource, Authorization authorization) {
    return new RegistryCredentials().store(registry, credentialSource, authorization);
  }
  public static RegistryCredentials from(
      List<String> credentialHelperSuffixes, List<String> registries)
      throws IOException, NonexistentDockerCredentialHelperException {
    RegistryCredentials registryCredentials = new RegistryCredentials();
    for (String registry : registries) {
      for (String credentialHelperSuffix : credentialHelperSuffixes) {
        try {
          registryCredentials.store(
              registry,
              "docker-credential-" + credentialHelperSuffix,
              new DockerCredentialHelper(registry, credentialHelperSuffix).retrieve());
        } catch (NonexistentServerUrlDockerCredentialHelperException ex) {
        }
      }
    }
    return registryCredentials;
  }
  public static RegistryCredentials from(
      String credentialSource, Map<String, Authorization> registryCredentialMap) {
    RegistryCredentials registryCredentials = new RegistryCredentials();
    for (Map.Entry<String, Authorization> registryCredential : registryCredentialMap.entrySet()) {
      registryCredentials.store(
          registryCredential.getKey(), credentialSource, registryCredential.getValue());
    }
    return registryCredentials;
  }
  private static class AuthorizationSourcePair {
    private final String credentialSource;
    private final Authorization authorization;
    private AuthorizationSourcePair(String credentialSource, Authorization authorization) {
      this.credentialSource = credentialSource;
      this.authorization = authorization;
    }
  }
  private final Map<String, AuthorizationSourcePair> credentials = new HashMap<>();
  private RegistryCredentials() {};
  public boolean has(String registry) {
    return credentials.containsKey(registry);
  }
  public Authorization getAuthorization(String registry) {
    if (credentials.get(registry) == null) {
      return null;
    }
    return credentials.get(registry).authorization;
  }
  public String getCredentialSource(String registry) {
    if (credentials.get(registry) == null) {
      return null;
    }
    return credentials.get(registry).credentialSource;
  }
  private RegistryCredentials store(
      String registry, String credentialSource, Authorization authorization) {
    credentials.put(registry, new AuthorizationSourcePair(credentialSource, authorization));
    return this;
  }
}
