package com.google.cloud.tools.jib.blob;
public class BlobTest {
  @Test
  public void testFromInputStream() throws IOException {
    String expected = "crepecake";
    InputStream inputStream = new ByteArrayInputStream(expected.getBytes(StandardCharsets.UTF_8));
    verifyBlobWriteTo(expected, Blobs.from(inputStream));
  }
  @Test
  public void testFromFile() throws IOException, URISyntaxException {
    Path fileA = Paths.get(Resources.getResource("fileA").toURI());
    String expected = new String(Files.readAllBytes(fileA), StandardCharsets.UTF_8);
    verifyBlobWriteTo(expected, Blobs.from(fileA));
  }
  @Test
  public void testFromString() throws IOException {
    String expected = "crepecake";
    verifyBlobWriteTo(expected, Blobs.from(expected));
  }
  @Test
  public void testFromBlobWriter() throws IOException {
    String expected = "crepecake";
    BlobWriter writer =
        outputStream -> outputStream.write(expected.getBytes(StandardCharsets.UTF_8));
    verifyBlobWriteTo(expected, Blobs.from(writer));
  }
  private void verifyBlobWriteTo(String expected, Blob blob) throws IOException {
    OutputStream outputStream = new ByteArrayOutputStream();
    BlobDescriptor blobDescriptor = blob.writeTo(outputStream);
    String output = outputStream.toString();
    Assert.assertEquals(expected, output);
    byte[] expectedBytes = expected.getBytes(StandardCharsets.UTF_8);
    Assert.assertEquals(expectedBytes.length, blobDescriptor.getSize());
    CountingDigestOutputStream countingDigestOutputStream =
        new CountingDigestOutputStream(Mockito.mock(OutputStream.class));
    countingDigestOutputStream.write(expectedBytes);
    DescriptorDigest expectedDigest = countingDigestOutputStream.toBlobDescriptor().getDigest();
    Assert.assertEquals(expectedDigest, blobDescriptor.getDigest());
  }
}
