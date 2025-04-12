package com.google.cloud.tools.jib.registry;
public class ManifestPullerIntegrationTest {
  @ClassRule public static LocalRegistry localRegistry = new LocalRegistry(5000);
  @Test
  public void testPull_v21() throws IOException, RegistryException {
    RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
    V21ManifestTemplate manifestTemplate =
        registryClient.pullManifest("latest", V21ManifestTemplate.class);
    Assert.assertEquals(1, manifestTemplate.getSchemaVersion());
    Assert.assertTrue(manifestTemplate.getFsLayers().size() > 0);
  }
  @Test
  public void testPull_v22() throws IOException, RegistryException {
    RegistryClient registryClient = new RegistryClient(null, "gcr.io", "distroless/java");
    ManifestTemplate manifestTemplate = registryClient.pullManifest("latest");
    Assert.assertEquals(2, manifestTemplate.getSchemaVersion());
    V22ManifestTemplate v22ManifestTemplate = (V22ManifestTemplate) manifestTemplate;
    Assert.assertTrue(v22ManifestTemplate.getLayers().size() > 0);
  }
  @Test
  public void testPull_unknownManifest() throws RegistryException, IOException {
    try {
      RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
      registryClient.pullManifest("nonexistent-tag");
      Assert.fail("Trying to pull nonexistent image should have errored");
    } catch (RegistryErrorException ex) {
      Assert.assertThat(
          ex.getMessage(),
          CoreMatchers.containsString(
              "pull image manifest for localhost:5000/busybox:nonexistent-tag"));
    }
  }
}
