package com.google.cloud.tools.jib.cache;
public class CacheWriterTest {
  @Rule public TemporaryFolder temporaryCacheDirectory = new TemporaryFolder();
  private Cache testCache;
  private Path resourceBlob;
  private static class ExpectedLayer {
    private final BlobDescriptor blobDescriptor;
    private final DescriptorDigest diffId;
    private final Blob blob;
    private ExpectedLayer(BlobDescriptor blobDescriptor, DescriptorDigest diffId, Blob blob) {
      this.blobDescriptor = blobDescriptor;
      this.diffId = diffId;
      this.blob = blob;
    }
  }
  @Before
  public void setUp() throws CacheMetadataCorruptedException, IOException, URISyntaxException {
    Path cacheDirectory = temporaryCacheDirectory.newFolder().toPath();
    testCache = Cache.init(cacheDirectory);
    resourceBlob = Paths.get(Resources.getResource("blobA").toURI());
  }
  @Test
  public void testWriteLayer_unwritten() throws IOException, LayerPropertyNotFoundException {
    ExpectedLayer expectedLayer = getExpectedLayer();
    CacheWriter cacheWriter = new CacheWriter(testCache);
    UnwrittenLayer unwrittenLayer = new UnwrittenLayer(Blobs.from(resourceBlob));
    List<Path> fakeSourceFiles = Collections.singletonList(Paths.get("some", "source", "file"));
    LayerBuilder mockLayerBuilder = Mockito.mock(LayerBuilder.class);
    Mockito.when(mockLayerBuilder.build()).thenReturn(unwrittenLayer);
    Mockito.when(mockLayerBuilder.getSourceFiles()).thenReturn(fakeSourceFiles);
    CachedLayer cachedLayer = cacheWriter.writeLayer(mockLayerBuilder);
    CachedLayerWithMetadata layerInMetadata = testCache.getMetadata().getLayers().get(0);
    Assert.assertNotNull(layerInMetadata.getMetadata());
    Assert.assertEquals(
        Collections.singletonList(Paths.get("some", "source", "file").toString()),
        layerInMetadata.getMetadata().getSourceFiles());
    verifyCachedLayerIsExpected(expectedLayer, cachedLayer);
  }
  @Test
  public void testGetLayerOutputStream() throws IOException, LayerPropertyNotFoundException {
    ExpectedLayer expectedLayer = getExpectedLayer();
    CacheWriter cacheWriter = new CacheWriter(testCache);
    CountingOutputStream layerOutputStream =
        cacheWriter.getLayerOutputStream(expectedLayer.blobDescriptor.getDigest());
    expectedLayer.blob.writeTo(layerOutputStream);
    CachedLayer cachedLayer =
        cacheWriter.getCachedLayer(expectedLayer.blobDescriptor.getDigest(), layerOutputStream);
    CachedLayerWithMetadata layerInMetadata = testCache.getMetadata().getLayers().get(0);
    Assert.assertNull(layerInMetadata.getMetadata());
    verifyCachedLayerIsExpected(expectedLayer, cachedLayer);
  }
  private ExpectedLayer getExpectedLayer() throws IOException {
    String expectedBlobAString =
        new String(Files.readAllBytes(resourceBlob), StandardCharsets.UTF_8);
    ByteArrayOutputStream compressedBlobOutputStream = new ByteArrayOutputStream();
    CountingDigestOutputStream compressedDigestOutputStream =
        new CountingDigestOutputStream(compressedBlobOutputStream);
    CountingDigestOutputStream uncompressedDigestOutputStream;
    try (GZIPOutputStream compressorStream = new GZIPOutputStream(compressedDigestOutputStream)) {
      uncompressedDigestOutputStream = new CountingDigestOutputStream(compressorStream);
      uncompressedDigestOutputStream.write(expectedBlobAString.getBytes(StandardCharsets.UTF_8));
    }
    BlobDescriptor expectedBlobADescriptor = compressedDigestOutputStream.toBlobDescriptor();
    DescriptorDigest expectedBlobADiffId =
        uncompressedDigestOutputStream.toBlobDescriptor().getDigest();
    ByteArrayInputStream compressedBlobInputStream =
        new ByteArrayInputStream(compressedBlobOutputStream.toByteArray());
    Blob blob = Blobs.from(compressedBlobInputStream);
    return new ExpectedLayer(expectedBlobADescriptor, expectedBlobADiffId, blob);
  }
  private void verifyCachedLayerIsExpected(ExpectedLayer expectedLayer, CachedLayer cachedLayer)
      throws IOException {
    Path compressedBlobFile = cachedLayer.getContentFile();
    try (InputStreamReader fileReader =
        new InputStreamReader(
            new GZIPInputStream(Files.newInputStream(compressedBlobFile)),
            StandardCharsets.UTF_8)) {
      String decompressedString = CharStreams.toString(fileReader);
      String expectedBlobAString =
          new String(Files.readAllBytes(resourceBlob), StandardCharsets.UTF_8);
      Assert.assertEquals(expectedBlobAString, decompressedString);
      Assert.assertEquals(
          expectedLayer.blobDescriptor.getSize(), cachedLayer.getBlobDescriptor().getSize());
      Assert.assertEquals(
          expectedLayer.blobDescriptor.getDigest(), cachedLayer.getBlobDescriptor().getDigest());
      Assert.assertEquals(expectedLayer.blobDescriptor, cachedLayer.getBlobDescriptor());
      Assert.assertEquals(expectedLayer.diffId, cachedLayer.getDiffId());
    }
  }
}
