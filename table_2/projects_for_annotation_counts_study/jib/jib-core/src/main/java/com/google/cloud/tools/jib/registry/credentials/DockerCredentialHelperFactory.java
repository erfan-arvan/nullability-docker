package com.google.cloud.tools.jib.registry.credentials;
public class DockerCredentialHelperFactory {
  private final String registry;
  public DockerCredentialHelperFactory(String registry) {
    this.registry = registry;
  }
  public DockerCredentialHelper withCredentialHelperSuffix(String credentialHelperSuffix) {
    return new DockerCredentialHelper(registry, credentialHelperSuffix);
  }
}
