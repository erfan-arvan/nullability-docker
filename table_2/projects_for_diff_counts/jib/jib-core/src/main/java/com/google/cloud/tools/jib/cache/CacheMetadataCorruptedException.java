package com.google.cloud.tools.jib.cache;
public class CacheMetadataCorruptedException extends Exception {
  CacheMetadataCorruptedException(Throwable cause) {
    super(cause);
  }
  public CacheMetadataCorruptedException(String message) {
    super(message);
  }
}
