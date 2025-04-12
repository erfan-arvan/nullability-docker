package com.google.cloud.tools.jib.image.json;
public class OCIManifestTemplate implements BuildableManifestTemplate {
  public static final String MANIFEST_MEDIA_TYPE = "application/vnd.oci.image.manifest.v1+json";
  private static final String CONTAINER_CONFIGURATION_MEDIA_TYPE =
      "application/vnd.oci.image.config.v1+json";
  private static final String LAYER_MEDIA_TYPE = "application/vnd.oci.image.layer.v1.tar+gzip";
  private final int schemaVersion = 2;
  private final String mediaType = MANIFEST_MEDIA_TYPE;
   private ContentDescriptorTemplate config;
  private final List<ContentDescriptorTemplate> layers = new ArrayList<>();
  @Override
  public int getSchemaVersion() {
    return schemaVersion;
  }
  @Override
  public String getManifestMediaType() {
    return MANIFEST_MEDIA_TYPE;
  }
  @Override
  public ContentDescriptorTemplate getContainerConfiguration() {
    return config;
  }
  @Override
  public List<ContentDescriptorTemplate> getLayers() {
    return Collections.unmodifiableList(layers);
  }
  @Override
  public void setContainerConfiguration(long size, DescriptorDigest digest) {
    config = new ContentDescriptorTemplate(CONTAINER_CONFIGURATION_MEDIA_TYPE, size, digest);
  }
  @Override
  public void addLayer(long size, DescriptorDigest digest) {
    layers.add(new ContentDescriptorTemplate(LAYER_MEDIA_TYPE, size, digest));
  }
}
