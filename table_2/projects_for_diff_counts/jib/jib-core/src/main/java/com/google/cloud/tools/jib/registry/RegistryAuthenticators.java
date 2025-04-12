package com.google.cloud.tools.jib.registry;
public abstract class RegistryAuthenticators {
  public static RegistryAuthenticator forDockerHub(String repository)
      throws RegistryAuthenticationFailedException {
    try {
      return new RegistryAuthenticator(
          "https:
    } catch (MalformedURLException ex) {
      throw new RegistryAuthenticationFailedException(ex);
    }
  }
  public static RegistryAuthenticator forOther(String serverUrl, String repository)
      throws RegistryAuthenticationFailedException, IOException, RegistryException {
    try {
      RegistryClient registryClient = new RegistryClient(null, serverUrl, repository);
      return registryClient.getRegistryAuthenticator();
    } catch (MalformedURLException ex) {
      throw new RegistryAuthenticationFailedException(ex);
    }
  }
}
