package com.google.cloud.tools.jib.registry;
@RunWith(MockitoJUnitRunner.class)
public class RegistryClientTest {
  @Mock private Authorization mockAuthorization;
  private RegistryClient testRegistryClient;
  @Before
  public void setUp() {
    testRegistryClient =
        new RegistryClient(mockAuthorization, "some.server.url", "some image name");
  }
  @Test
  public void testGetUserAgent_null() {
    Assert.assertTrue(RegistryClient.getUserAgent().startsWith("jib"));
    RegistryClient.setUserAgentSuffix(null);
    Assert.assertTrue(RegistryClient.getUserAgent().startsWith("jib"));
  }
  @Test
  public void testGetUserAgent() {
    RegistryClient.setUserAgentSuffix("some user agent suffix");
    Assert.assertTrue(RegistryClient.getUserAgent().startsWith("jib "));
    Assert.assertTrue(RegistryClient.getUserAgent().endsWith(" some user agent suffix"));
  }
  @Test
  public void testGetApiRouteBase() {
    Assert.assertEquals("https:
  }
}
