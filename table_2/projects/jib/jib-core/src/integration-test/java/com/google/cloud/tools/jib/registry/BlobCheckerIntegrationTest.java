package com.google.cloud.tools.jib.registry;
public class BlobCheckerIntegrationTest {
  @ClassRule public static LocalRegistry localRegistry = new LocalRegistry(5000);
  @Test
  public void testCheck_exists() throws IOException, RegistryException {
    RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
    V22ManifestTemplate manifestTemplate =
        registryClient.pullManifest("latest", V22ManifestTemplate.class);
    DescriptorDigest blobDigest = manifestTemplate.getLayers().get(0).getDigest();
    Assert.assertEquals(blobDigest, registryClient.checkBlob(blobDigest).getDigest());
  }
  @Test
  public void testCheck_doesNotExist() throws IOException, RegistryException, DigestException {
    RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "busybox");
    DescriptorDigest fakeBlobDigest =
        DescriptorDigest.fromHash(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    Assert.assertEquals(null, registryClient.checkBlob(fakeBlobDigest));
  }
}
