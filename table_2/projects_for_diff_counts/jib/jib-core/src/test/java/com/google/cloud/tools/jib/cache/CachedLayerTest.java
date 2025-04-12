package com.google.cloud.tools.jib.cache;
@RunWith(MockitoJUnitRunner.class)
public class CachedLayerTest {
  @Mock private Path mockPath;
  @Mock private BlobDescriptor mockBlobDescriptor;
  @Mock private DescriptorDigest mockDiffId;
  @Test
  public void testNew() {
    CachedLayer layer = new CachedLayer(mockPath, mockBlobDescriptor, mockDiffId);
    Assert.assertEquals(mockPath, layer.getContentFile());
    Assert.assertEquals(mockBlobDescriptor, layer.getBlobDescriptor());
    Assert.assertEquals(mockDiffId, layer.getDiffId());
  }
  @Test
  public void testGetBlob() throws URISyntaxException, IOException {
    Path fileA = Paths.get(Resources.getResource("fileA").toURI());
    String expectedFileAString = new String(Files.readAllBytes(fileA), StandardCharsets.UTF_8);
    CachedLayer cachedLayer = new CachedLayer(fileA, mockBlobDescriptor, mockDiffId);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Blob fileBlob = cachedLayer.getBlob();
    fileBlob.writeTo(outputStream);
    Assert.assertEquals(
        expectedFileAString, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    Assert.assertEquals(mockBlobDescriptor, cachedLayer.getBlobDescriptor());
    Assert.assertEquals(mockDiffId, cachedLayer.getDiffId());
  }
}
