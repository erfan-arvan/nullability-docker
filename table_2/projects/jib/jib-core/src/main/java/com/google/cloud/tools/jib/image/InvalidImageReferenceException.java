package com.google.cloud.tools.jib.image;
public class InvalidImageReferenceException extends Exception {
  public InvalidImageReferenceException(String reference) {
    super("Invalid image reference: " + reference);
  }
}
