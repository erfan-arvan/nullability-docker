package com.google.cloud.tools.jib.registry;
public class UnexpectedBlobDigestException extends RegistryException {
  UnexpectedBlobDigestException(String message) {
    super(message);
  }
}
