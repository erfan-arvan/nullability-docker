package com.google.cloud.tools.jib.cache;
public class Cache implements Closeable {
  private final Path cacheDirectory;
  private final CacheMetadata cacheMetadata;
  public static Cache init(Path cacheDirectory)
      throws NotDirectoryException, CacheMetadataCorruptedException {
    if (!Files.isDirectory(cacheDirectory)) {
      throw new NotDirectoryException("The cache can only write to a directory");
    }
    CacheMetadata cacheMetadata = loadCacheMetadata(cacheDirectory);
    return new Cache(cacheDirectory, cacheMetadata);
  }
  private static CacheMetadata loadCacheMetadata(Path cacheDirectory)
      throws CacheMetadataCorruptedException {
    Path cacheMetadataJsonFile = cacheDirectory.resolve(CacheFiles.METADATA_FILENAME);
    if (!Files.exists(cacheMetadataJsonFile)) {
      return new CacheMetadata();
    }
    try {
      CacheMetadataTemplate cacheMetadataJson =
          JsonTemplateMapper.readJsonFromFile(cacheMetadataJsonFile, CacheMetadataTemplate.class);
      return CacheMetadataTranslator.fromTemplate(cacheMetadataJson, cacheDirectory);
    } catch (IOException ex) {
      throw new CacheMetadataCorruptedException(ex);
    }
  }
  private Cache(Path cacheDirectory, CacheMetadata cacheMetadata) {
    this.cacheDirectory = cacheDirectory;
    this.cacheMetadata = cacheMetadata;
  }
  @Override
  public void close() throws IOException {
    saveCacheMetadata(cacheDirectory);
  }
  void addLayerToMetadata(CachedLayer cachedLayer,  LayerMetadata layerMetadata)
      throws LayerPropertyNotFoundException {
    cacheMetadata.addLayer(new CachedLayerWithMetadata(cachedLayer, layerMetadata));
  }
  @VisibleForTesting
  Path getCacheDirectory() {
    return cacheDirectory;
  }
  @VisibleForTesting
  CacheMetadata getMetadata() {
    return cacheMetadata;
  }
  private void saveCacheMetadata(Path cacheDirectory) throws IOException {
    Path cacheMetadataJsonFile = cacheDirectory.resolve(CacheFiles.METADATA_FILENAME);
    CacheMetadataTemplate cacheMetadataJson = CacheMetadataTranslator.toTemplate(cacheMetadata);
    try (OutputStream fileOutputStream =
        new BufferedOutputStream(Files.newOutputStream(cacheMetadataJsonFile))) {
      JsonTemplateMapper.toBlob(cacheMetadataJson).writeTo(fileOutputStream);
    }
  }
}
