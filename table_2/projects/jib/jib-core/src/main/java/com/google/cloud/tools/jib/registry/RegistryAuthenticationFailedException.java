package com.google.cloud.tools.jib.registry;
public class RegistryAuthenticationFailedException extends Exception {
  private static final String REASON_PREFIX = "Failed to authenticate with the registry because: ";
  RegistryAuthenticationFailedException(Throwable cause) {
    super(REASON_PREFIX + cause.getMessage(), cause);
  }
  RegistryAuthenticationFailedException(String reason) {
    super(REASON_PREFIX + reason);
  }
}
