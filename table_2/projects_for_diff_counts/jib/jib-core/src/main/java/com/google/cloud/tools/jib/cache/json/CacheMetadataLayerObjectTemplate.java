package com.google.cloud.tools.jib.cache.json;
public class CacheMetadataLayerObjectTemplate implements JsonTemplate {
  private final ReferenceObject reference = new ReferenceObject();
   private CacheMetadataLayerPropertiesObjectTemplate properties;
  private static class ReferenceObject implements JsonTemplate {
    private long size;
     private DescriptorDigest digest;
     private DescriptorDigest diffId;
  }
  public long getSize() {
    return reference.size;
  }
  public DescriptorDigest getDigest() {
    return reference.digest;
  }
  public DescriptorDigest getDiffId() {
    return reference.diffId;
  }
  public CacheMetadataLayerPropertiesObjectTemplate getProperties() {
    return properties;
  }
  public CacheMetadataLayerObjectTemplate setSize(long size) {
    reference.size = size;
    return this;
  }
  public CacheMetadataLayerObjectTemplate setDigest(DescriptorDigest digest) {
    reference.digest = digest;
    return this;
  }
  public CacheMetadataLayerObjectTemplate setDiffId(DescriptorDigest diffId) {
    reference.diffId = diffId;
    return this;
  }
  public CacheMetadataLayerObjectTemplate setProperties(
      CacheMetadataLayerPropertiesObjectTemplate properties) {
    this.properties = properties;
    return this;
  }
}
