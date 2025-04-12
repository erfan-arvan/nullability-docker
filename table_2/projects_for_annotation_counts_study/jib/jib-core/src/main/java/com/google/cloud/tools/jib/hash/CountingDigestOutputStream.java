package com.google.cloud.tools.jib.hash;
public class CountingDigestOutputStream extends DigestOutputStream {
  private static final String SHA_256_ALGORITHM = "SHA-256";
  private long totalBytes = 0;
  public CountingDigestOutputStream(OutputStream outputStream) {
    super(outputStream, null);
    try {
      setMessageDigest(MessageDigest.getInstance(SHA_256_ALGORITHM));
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException(
          "SHA-256 algorithm implementation not found - might be a broken JVM");
    }
  }
  public BlobDescriptor toBlobDescriptor() {
    try {
      byte[] hashedBytes = digest.digest();
      StringBuilder stringBuilder = new StringBuilder(2 * hashedBytes.length);
      for (byte b : hashedBytes) {
        stringBuilder.append(String.format("%02x", b));
      }
      String hash = stringBuilder.toString();
      DescriptorDigest digest = DescriptorDigest.fromHash(hash);
      return new BlobDescriptor(totalBytes, digest);
    } catch (DigestException ex) {
      throw new RuntimeException("SHA-256 algorithm produced invalid hash: " + ex.getMessage(), ex);
    }
  }
  public long getTotalBytes() {
    return totalBytes;
  }
  @Override
  public void write(byte[] data, int offset, int length) throws IOException {
    super.write(data, offset, length);
    totalBytes += length;
  }
  @Override
  public void write(int singleByte) throws IOException {
    super.write(singleByte);
    totalBytes++;
  }
}
