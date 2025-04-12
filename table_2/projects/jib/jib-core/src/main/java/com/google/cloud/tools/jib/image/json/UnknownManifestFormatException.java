package com.google.cloud.tools.jib.image.json;
public class UnknownManifestFormatException extends RegistryException {
  public UnknownManifestFormatException(String message) {
    super(message);
  }
}
