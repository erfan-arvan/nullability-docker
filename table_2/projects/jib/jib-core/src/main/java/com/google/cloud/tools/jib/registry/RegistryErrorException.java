package com.google.cloud.tools.jib.registry;
public class RegistryErrorException extends RegistryException {
  RegistryErrorException(String message,  Throwable cause) {
    super(message, cause);
  }
}
