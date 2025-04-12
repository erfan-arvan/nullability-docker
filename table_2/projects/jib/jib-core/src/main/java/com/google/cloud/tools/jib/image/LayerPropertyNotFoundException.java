package com.google.cloud.tools.jib.image;
public class LayerPropertyNotFoundException extends Exception {
  LayerPropertyNotFoundException(String message) {
    super(message);
  }
}
