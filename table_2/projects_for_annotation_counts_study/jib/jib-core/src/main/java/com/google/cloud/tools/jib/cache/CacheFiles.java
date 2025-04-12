package com.google.cloud.tools.jib.cache;
class CacheFiles {
  static final String METADATA_FILENAME = "metadata.json";
  private static final String LAYER_FILE_EXTENSION = ".tar.gz";
  static Path getMetadataFile(Path cacheDirectory) {
    return cacheDirectory.resolve(METADATA_FILENAME);
  }
  static Path getLayerFile(Path cacheDirectory, DescriptorDigest layerDigest) {
    return cacheDirectory.resolve(layerDigest.getHash() + LAYER_FILE_EXTENSION);
  }
  private CacheFiles() {}
}
