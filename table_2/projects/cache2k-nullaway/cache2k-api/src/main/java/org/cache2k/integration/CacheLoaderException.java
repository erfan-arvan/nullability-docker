package org.cache2k.integration;
@Deprecated
public class CacheLoaderException extends org.cache2k.io.CacheLoaderException {
  public CacheLoaderException(String message) {
    super(message);
  }
  public CacheLoaderException(String message, Throwable ex) {
    super(message, ex);
  }
  public CacheLoaderException(Throwable ex) {
    super(ex);
  }
}
