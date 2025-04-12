package com.google.cloud.tools.jib.registry;
public class RegistryAuthenticatorTest {
  @Test
  public void testFromAuthenticationMethod_bearer()
      throws MalformedURLException, RegistryAuthenticationFailedException {
    RegistryAuthenticator registryAuthenticator =
        RegistryAuthenticator.fromAuthenticationMethod(
            "Bearer realm=\"https:
            "someimage");
    Assert.assertEquals(
        new URL("https:
        registryAuthenticator.getAuthenticationUrl("scope"));
  }
  @Test
  public void testFromAuthenticationMethod_basic()
      throws MalformedURLException, RegistryAuthenticationFailedException {
    Assert.assertNull(
        RegistryAuthenticator.fromAuthenticationMethod(
            "Basic realm=\"https:
            "someimage"));
  }
  @Test
  public void testFromAuthenticationMethod_noBearer() throws MalformedURLException {
    try {
      RegistryAuthenticator.fromAuthenticationMethod(
          "realm=\"https:
      Assert.fail("Authentication method without 'Bearer ' or 'Basic ' should fail");
    } catch (RegistryAuthenticationFailedException ex) {
      Assert.assertEquals(
          "Failed to authenticate with the registry because: 'Bearer' was not found in the 'WWW-Authenticate' header, tried to parse: realm=\"https:
          ex.getMessage());
    }
  }
  @Test
  public void testFromAuthenticationMethod_noRealm() throws MalformedURLException {
    try {
      RegistryAuthenticator.fromAuthenticationMethod("Bearer scope=\"somescope\"", "someimage");
      Assert.fail("Authentication method without 'realm' should fail");
    } catch (RegistryAuthenticationFailedException ex) {
      Assert.assertEquals(
          "Failed to authenticate with the registry because: 'realm' was not found in the 'WWW-Authenticate' header, tried to parse: Bearer scope=\"somescope\"",
          ex.getMessage());
    }
  }
  @Test
  public void testFromAuthenticationMethod_noService() throws MalformedURLException {
    try {
      RegistryAuthenticator.fromAuthenticationMethod(
          "Bearer realm=\"https:
      Assert.fail("Authentication method without 'service' should fail");
    } catch (RegistryAuthenticationFailedException ex) {
      Assert.assertEquals(
          "Failed to authenticate with the registry because: 'service' was not found in the 'WWW-Authenticate' header, tried to parse: Bearer realm=\"https:
          ex.getMessage());
    }
  }
}
