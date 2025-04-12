package com.google.cloud.tools.jib.registry;
public class RegistryAuthenticatorIntegrationTest {
  @Test
  public void testAuthenticate() throws RegistryAuthenticationFailedException {
    RegistryAuthenticator registryAuthenticator =
        RegistryAuthenticators.forDockerHub("library/busybox");
    Authorization authorization = registryAuthenticator.authenticatePull();
    Assert.assertTrue(0 < authorization.getToken().length());
  }
}
