package com.google.cloud.tools.jib.image.json;
public interface BuildableManifestTemplate extends ManifestTemplate {
  @VisibleForTesting
  class ContentDescriptorTemplate implements JsonTemplate {
     private String mediaType;
     private DescriptorDigest digest;
    private long size;
    ContentDescriptorTemplate(String mediaType, long size, DescriptorDigest digest) {
      this.mediaType = mediaType;
      this.size = size;
      this.digest = digest;
    }
    private ContentDescriptorTemplate() {}
    @VisibleForTesting
    public long getSize() {
      return size;
    }
    void setSize(long size) {
      this.size = size;
    }
    @VisibleForTesting
    public DescriptorDigest getDigest() {
      return digest;
    }
    void setDigest(DescriptorDigest digest) {
      this.digest = digest;
    }
  }
  String getManifestMediaType();
  ContentDescriptorTemplate getContainerConfiguration();
  List<ContentDescriptorTemplate> getLayers();
  void setContainerConfiguration(long size, DescriptorDigest digest);
  void addLayer(long size, DescriptorDigest digest);
}
