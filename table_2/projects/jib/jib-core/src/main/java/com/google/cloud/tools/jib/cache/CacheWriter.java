package com.google.cloud.tools.jib.cache;
public class CacheWriter {
  private final Cache cache;
  public CacheWriter(Cache cache) {
    this.cache = cache;
  }
  public CachedLayer writeLayer(LayerBuilder layerBuilder)
      throws IOException, LayerPropertyNotFoundException {
    UnwrittenLayer unwrittenLayer = layerBuilder.build();
    Path tempLayerFile = Files.createTempFile(cache.getCacheDirectory(), null, null);
    tempLayerFile.toFile().deleteOnExit();
    try (CountingDigestOutputStream compressedDigestOutputStream =
        new CountingDigestOutputStream(
            new BufferedOutputStream(Files.newOutputStream(tempLayerFile)))) {
      GZIPOutputStream compressorStream = new GZIPOutputStream(compressedDigestOutputStream);
      DescriptorDigest diffId = unwrittenLayer.getBlob().writeTo(compressorStream).getDigest();
      compressorStream.close();
      BlobDescriptor compressedBlobDescriptor = compressedDigestOutputStream.toBlobDescriptor();
      Path layerFile = getLayerFile(compressedBlobDescriptor.getDigest());
      Files.move(tempLayerFile, layerFile, StandardCopyOption.REPLACE_EXISTING);
      CachedLayer cachedLayer = new CachedLayer(layerFile, compressedBlobDescriptor, diffId);
      LayerMetadata layerMetadata =
          LayerMetadata.from(layerBuilder.getSourceFiles(), FileTime.from(Instant.now()));
      cache.addLayerToMetadata(cachedLayer, layerMetadata);
      return cachedLayer;
    }
  }
  public CountingOutputStream getLayerOutputStream(DescriptorDigest layerDigest)
      throws IOException {
    Path layerFile = getLayerFile(layerDigest);
    return new CountingOutputStream(new BufferedOutputStream(Files.newOutputStream(layerFile)));
  }
  public CachedLayer getCachedLayer(
      DescriptorDigest layerDigest, CountingOutputStream countingOutputStream)
      throws IOException, LayerPropertyNotFoundException {
    Path layerFile = getLayerFile(layerDigest);
    countingOutputStream.close();
    CachedLayer cachedLayer =
        new CachedLayer(
            layerFile,
            new BlobDescriptor(countingOutputStream.getCount(), layerDigest),
            getDiffId(layerFile));
    cache.addLayerToMetadata(cachedLayer, null);
    return cachedLayer;
  }
  private Path getLayerFile(DescriptorDigest compressedDigest) {
    return CacheFiles.getLayerFile(cache.getCacheDirectory(), compressedDigest);
  }
  private DescriptorDigest getDiffId(Path layerFile) throws IOException {
    CountingDigestOutputStream diffIdCaptureOutputStream =
        new CountingDigestOutputStream(ByteStreams.nullOutputStream());
    try (InputStream fileInputStream = new BufferedInputStream(Files.newInputStream(layerFile));
        GZIPInputStream decompressorStream = new GZIPInputStream(fileInputStream)) {
      ByteStreams.copy(decompressorStream, diffIdCaptureOutputStream);
    }
    return diffIdCaptureOutputStream.toBlobDescriptor().getDigest();
  }
}
