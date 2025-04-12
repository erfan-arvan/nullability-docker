package com.google.cloud.tools.jib.registry;
public class ManifestPusherIntegrationTest {
  @ClassRule public static LocalRegistry localRegistry = new LocalRegistry(5000);
  @Test
  public void testPush_missingBlobs() throws IOException, RegistryException {
    RegistryClient registryClient = new RegistryClient(null, "gcr.io", "distroless/java");
    ManifestTemplate manifestTemplate = registryClient.pullManifest("latest");
    registryClient = new RegistryClient(null, "localhost:5000", "busybox");
    try {
      registryClient.pushManifest((V22ManifestTemplate) manifestTemplate, "latest");
      Assert.fail("Pushing manifest without its BLOBs should fail");
    } catch (RegistryErrorException ex) {
      HttpResponseException httpResponseException = (HttpResponseException) ex.getCause();
      Assert.assertEquals(
          HttpStatusCodes.STATUS_CODE_BAD_REQUEST, httpResponseException.getStatusCode());
    }
  }
  @Test
  public void testPush() throws DigestException, IOException, RegistryException {
    Blob testLayerBlob = Blobs.from("crepecake");
    DescriptorDigest testLayerBlobDigest =
        DescriptorDigest.fromHash(
            "52a9e4d4ba4333ce593707f98564fee1e6d898db0d3602408c0b2a6a424d357c");
    Blob testContainerConfigurationBlob = Blobs.from("12345");
    DescriptorDigest testContainerConfigurationBlobDigest =
        DescriptorDigest.fromHash(
            "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5");
    V22ManifestTemplate expectedManifestTemplate = new V22ManifestTemplate();
    expectedManifestTemplate.addLayer(9, testLayerBlobDigest);
    expectedManifestTemplate.setContainerConfiguration(5, testContainerConfigurationBlobDigest);
    RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "testimage");
    Assert.assertFalse(registryClient.pushBlob(testLayerBlobDigest, testLayerBlob));
    Assert.assertFalse(
        registryClient.pushBlob(
            testContainerConfigurationBlobDigest, testContainerConfigurationBlob));
    registryClient.pushManifest(expectedManifestTemplate, "latest");
    V22ManifestTemplate manifestTemplate =
        registryClient.pullManifest("latest", V22ManifestTemplate.class);
    Assert.assertEquals(1, manifestTemplate.getLayers().size());
    Assert.assertEquals(testLayerBlobDigest, manifestTemplate.getLayers().get(0).getDigest());
    Assert.assertEquals(
        testContainerConfigurationBlobDigest,
        manifestTemplate.getContainerConfiguration().getDigest());
  }
}
