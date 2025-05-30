package com.google.cloud.tools.jib.image;
@JsonSerialize(using = DescriptorDigestSerializer.class)
@JsonDeserialize(using = DescriptorDigestDeserializer.class)
public class DescriptorDigest {
  private static final String HASH_REGEX = "[a-f0-9]{64}";
  private static final String DIGEST_PREFIX = "sha256:";
  static final String DIGEST_REGEX = DIGEST_PREFIX + HASH_REGEX;
  private final String hash;
  public static DescriptorDigest fromHash(String hash) throws DigestException {
    if (!hash.matches(HASH_REGEX)) {
      throw new DigestException("Invalid hash: " + hash);
    }
    return new DescriptorDigest(hash);
  }
  public static DescriptorDigest fromDigest(String digest) throws DigestException {
    if (!digest.matches(DIGEST_REGEX)) {
      throw new DigestException("Invalid digest: " + digest);
    }
    String hash = digest.substring(DIGEST_PREFIX.length());
    return new DescriptorDigest(hash);
  }
  private DescriptorDigest(String hash) {
    this.hash = hash;
  }
  public String getHash() {
    return hash;
  }
  @Override
  public String toString() {
    return "sha256:" + hash;
  }
  @Override
  public int hashCode() {
    return hash.hashCode();
  }
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DescriptorDigest) {
      return hash.equals(((DescriptorDigest) obj).hash);
    }
    return false;
  }
}
