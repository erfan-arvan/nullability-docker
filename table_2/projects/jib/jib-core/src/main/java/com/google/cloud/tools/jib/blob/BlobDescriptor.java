package com.google.cloud.tools.jib.blob;
public class BlobDescriptor {
  private final DescriptorDigest digest;
  private final long size;
  static BlobDescriptor fromPipe(InputStream inputStream, OutputStream outputStream)
      throws IOException {
    CountingDigestOutputStream countingDigestOutputStream =
        new CountingDigestOutputStream(outputStream);
    ByteStreams.copy(inputStream, countingDigestOutputStream);
    countingDigestOutputStream.flush();
    return countingDigestOutputStream.toBlobDescriptor();
  }
  public BlobDescriptor(long size, DescriptorDigest digest) {
    this.size = size;
    this.digest = digest;
  }
  public BlobDescriptor(DescriptorDigest digest) {
    this(-1, digest);
  }
  public boolean hasSize() {
    return size >= 0;
  }
  public DescriptorDigest getDigest() {
    return digest;
  }
  public long getSize() {
    return size;
  }
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (size < 0 || !(obj instanceof BlobDescriptor)) {
      return false;
    }
    BlobDescriptor other = (BlobDescriptor) obj;
    return size == other.getSize() && digest.equals(other.getDigest());
  }
  @Override
  public int hashCode() {
    int result = digest.hashCode();
    result = 31 * result + (int) (size ^ (size >>> 32));
    return result;
  }
}
