package org.cache2k;
public class CustomizationException extends CacheException {
  public CustomizationException(String message) {
    super(message);
  }
  public CustomizationException(Throwable cause) {
    super(cause);
  }
  public CustomizationException(String message, Throwable cause) {
    super(message, cause);
  }
}
