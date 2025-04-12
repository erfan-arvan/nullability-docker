package com.google.cloud.tools.jib.registry;
public class AuthenticationMethodRetrieverIntegrationTest {
  @Test
  public void testGetRegistryAuthenticator()
      throws RegistryAuthenticationFailedException, IOException, RegistryException {
    RegistryClient registryClient =
        new RegistryClient(null, "registry.hub.docker.com", "library/busybox");
    RegistryAuthenticator registryAuthenticator = registryClient.getRegistryAuthenticator();
    Authorization authorization = registryAuthenticator.authenticatePull();
    RegistryClient authorizedRegistryClient =
        new RegistryClient(authorization, "registry.hub.docker.com", "library/busybox");
    authorizedRegistryClient.pullManifest("latest");
  }
}
