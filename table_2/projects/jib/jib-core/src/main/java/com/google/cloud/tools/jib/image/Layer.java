package com.google.cloud.tools.jib.image;
public interface Layer {
  Blob getBlob() throws LayerPropertyNotFoundException;
  BlobDescriptor getBlobDescriptor() throws LayerPropertyNotFoundException;
  DescriptorDigest getDiffId() throws LayerPropertyNotFoundException;
}
