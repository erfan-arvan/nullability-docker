package com.google.cloud.tools.jib.registry;
public class BlobPullerIntegrationTest {
  @ClassRule public static LocalRegistry localRegistry = new LocalRegistry(5000);
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @Test
  public void testPull() throws IOException, RegistryException {
    RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
    V21ManifestTemplate manifestTemplate =
        registryClient.pullManifest("latest", V21ManifestTemplate.class);
    DescriptorDigest realDigest = manifestTemplate.getLayerDigests().get(0);
    CountingDigestOutputStream layerOutputStream =
        new CountingDigestOutputStream(ByteStreams.nullOutputStream());
    registryClient.pullBlob(realDigest, layerOutputStream);
    Assert.assertEquals(realDigest, layerOutputStream.toBlobDescriptor().getDigest());
  }
  @Test
  public void testPull_unknownBlob() throws RegistryException, IOException, DigestException {
    DescriptorDigest nonexistentDigest =
        DescriptorDigest.fromHash(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    try {
      RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
      registryClient.pullBlob(nonexistentDigest, Mockito.mock(OutputStream.class));
      Assert.fail("Trying to pull nonexistent blob should have errored");
    } catch (RegistryErrorException ex) {
      Assert.assertThat(
          ex.getMessage(),
          CoreMatchers.containsString(
              "pull BLOB for localhost:5000/busybox with digest " + nonexistentDigest));
    }
  }
}
