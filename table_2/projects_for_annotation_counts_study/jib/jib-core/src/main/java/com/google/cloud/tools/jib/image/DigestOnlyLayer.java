package com.google.cloud.tools.jib.image;
public class DigestOnlyLayer implements Layer {
  private final BlobDescriptor blobDescriptor;
  public DigestOnlyLayer(DescriptorDigest digest) {
    blobDescriptor = new BlobDescriptor(digest);
  }
  @Override
  public Blob getBlob() throws LayerPropertyNotFoundException {
    throw new LayerPropertyNotFoundException("Blob not available for digest-only layer");
  }
  @Override
  public BlobDescriptor getBlobDescriptor() {
    return blobDescriptor;
  }
  @Override
  public DescriptorDigest getDiffId() throws LayerPropertyNotFoundException {
    throw new LayerPropertyNotFoundException("Diff ID not available for digest-only layer");
  }
}
