package com.google.cloud.tools.jib.hash;
public class CountingDigestOutputStreamTest {
  private Map<String, String> knownSha256Hashes;
  @Before
  public void setUp() {
    knownSha256Hashes =
        Collections.unmodifiableMap(
            new HashMap<String, String>() {
              {
                put(
                    "crepecake",
                    "52a9e4d4ba4333ce593707f98564fee1e6d898db0d3602408c0b2a6a424d357c");
                put("12345", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5");
                put("", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
              }
            });
  }
  @Test
  public void test_smokeTest() throws IOException, DigestException {
    for (Map.Entry<String, String> knownHash : knownSha256Hashes.entrySet()) {
      String toHash = knownHash.getKey();
      String expectedHash = knownHash.getValue();
      OutputStream underlyingOutputStream = new ByteArrayOutputStream();
      CountingDigestOutputStream countingDigestOutputStream =
          new CountingDigestOutputStream(underlyingOutputStream);
      byte[] bytesToHash = toHash.getBytes(StandardCharsets.UTF_8);
      InputStream toHashInputStream = new ByteArrayInputStream(bytesToHash);
      ByteStreams.copy(toHashInputStream, countingDigestOutputStream);
      BlobDescriptor expectedBlobDescriptor =
          new BlobDescriptor(bytesToHash.length, DescriptorDigest.fromHash(expectedHash));
      Assert.assertEquals(expectedBlobDescriptor, countingDigestOutputStream.toBlobDescriptor());
      Assert.assertEquals(bytesToHash.length, countingDigestOutputStream.getTotalBytes());
    }
  }
}
