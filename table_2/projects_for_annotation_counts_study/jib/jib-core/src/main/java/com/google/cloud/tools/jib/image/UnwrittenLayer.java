package com.google.cloud.tools.jib.image;
public class UnwrittenLayer implements Layer {
  private final Blob uncompressedBlob;
  public UnwrittenLayer(Blob uncompressedBlob) {
    this.uncompressedBlob = uncompressedBlob;
  }
  @Override
  public Blob getBlob() {
    return uncompressedBlob;
  }
  @Override
  public BlobDescriptor getBlobDescriptor() throws LayerPropertyNotFoundException {
    throw new LayerPropertyNotFoundException("Blob descriptor not available for unwritten layer");
  }
  @Override
  public DescriptorDigest getDiffId() throws LayerPropertyNotFoundException {
    throw new LayerPropertyNotFoundException("Diff ID not available for unwritten layer");
  }
}
