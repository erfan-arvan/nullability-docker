package com.google.cloud.tools.jib.cache;
public class CacheDirectoryNotOwnedException extends Exception {
  private final Path cacheDirectory;
  CacheDirectoryNotOwnedException(Path cacheDirectory) {
    super();
    this.cacheDirectory = cacheDirectory;
  }
  public Path getCacheDirectory() {
    return cacheDirectory;
  }
}
