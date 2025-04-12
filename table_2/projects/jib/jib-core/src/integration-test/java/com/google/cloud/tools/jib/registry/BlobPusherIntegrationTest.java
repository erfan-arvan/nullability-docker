package com.google.cloud.tools.jib.registry;
public class BlobPusherIntegrationTest {
  @ClassRule public static LocalRegistry localRegistry = new LocalRegistry(5000);
  @Test
  public void testPush() throws DigestException, IOException, RegistryException {
    Blob testBlob = Blobs.from("crepecake");
    DescriptorDigest testBlobDigest =
        DescriptorDigest.fromHash(
            "52a9e4d4ba4333ce593707f98564fee1e6d898db0d3602408c0b2a6a424d357c");
    RegistryClient registryClient = new RegistryClient(null, "localhost:5000", "testimage");
    Assert.assertFalse(registryClient.pushBlob(testBlobDigest, testBlob));
  }
}
