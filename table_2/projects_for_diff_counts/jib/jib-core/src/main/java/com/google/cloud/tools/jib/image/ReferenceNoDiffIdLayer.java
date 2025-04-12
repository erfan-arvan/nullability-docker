package com.google.cloud.tools.jib.image;
public class ReferenceNoDiffIdLayer implements Layer {
  private final BlobDescriptor blobDescriptor;
  public ReferenceNoDiffIdLayer(BlobDescriptor blobDescriptor) {
    this.blobDescriptor = blobDescriptor;
  }
  @Override
  public Blob getBlob() throws LayerPropertyNotFoundException {
    throw new LayerPropertyNotFoundException(
        "Blob not available for reference layer without diff ID");
  }
  @Override
  public BlobDescriptor getBlobDescriptor() {
    return blobDescriptor;
  }
  @Override
  public DescriptorDigest getDiffId() throws LayerPropertyNotFoundException {
    throw new LayerPropertyNotFoundException(
        "Diff ID not available for reference layer without diff ID");
  }
}
