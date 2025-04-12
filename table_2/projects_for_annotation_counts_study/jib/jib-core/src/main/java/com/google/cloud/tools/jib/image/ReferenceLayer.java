package com.google.cloud.tools.jib.image;
public class ReferenceLayer implements Layer {
  private final BlobDescriptor blobDescriptor;
  private final DescriptorDigest diffId;
  public ReferenceLayer(BlobDescriptor blobDescriptor, DescriptorDigest diffId) {
    this.blobDescriptor = blobDescriptor;
    this.diffId = diffId;
  }
  @Override
  public Blob getBlob() throws LayerPropertyNotFoundException {
    throw new LayerPropertyNotFoundException("Blob not available for reference layer");
  }
  @Override
  public BlobDescriptor getBlobDescriptor() {
    return blobDescriptor;
  }
  @Override
  public DescriptorDigest getDiffId() {
    return diffId;
  }
}
