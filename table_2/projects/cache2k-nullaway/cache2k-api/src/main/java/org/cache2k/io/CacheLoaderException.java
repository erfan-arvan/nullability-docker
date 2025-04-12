package org.cache2k.io;
public class CacheLoaderException extends CustomizationException {
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
