package com.google.cloud.tools.jib.cache;
public class CachedLayer implements Layer {
  private final Path contentFile;
  private final BlobDescriptor blobDescriptor;
  private final DescriptorDigest diffId;
  public CachedLayer(Path contentFile, BlobDescriptor blobDescriptor, DescriptorDigest diffId) {
    this.contentFile = contentFile;
    this.blobDescriptor = blobDescriptor;
    this.diffId = diffId;
  }
  public Path getContentFile() {
    return contentFile;
  }
  @Override
  public Blob getBlob() {
    return Blobs.from(contentFile);
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
