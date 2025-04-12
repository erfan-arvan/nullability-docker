package com.google.cloud.tools.jib.registry;
public class RegistryException extends Exception {
  public RegistryException(String message,  Throwable cause) {
    super(message, cause);
  }
  public RegistryException(String message) {
    super(message);
  }
  public RegistryException(Throwable cause) {
    super(cause);
  }
}
