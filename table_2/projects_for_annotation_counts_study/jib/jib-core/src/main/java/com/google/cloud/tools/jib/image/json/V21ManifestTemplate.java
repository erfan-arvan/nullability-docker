package com.google.cloud.tools.jib.image.json;
public class V21ManifestTemplate implements ManifestTemplate {
  public static final String MEDIA_TYPE = "application/vnd.docker.distribution.manifest.v1+json";
  private final int schemaVersion = 1;
  private final List<LayerObjectTemplate> fsLayers = new ArrayList<>();
  private final List<V1CompatibilityTemplate> history = new ArrayList<>();
  static class LayerObjectTemplate implements JsonTemplate {
     private DescriptorDigest blobSum;
    DescriptorDigest getDigest() {
      return blobSum;
    }
  }
  private static class V1CompatibilityTemplate implements JsonTemplate {
     private String v1Compatibility;
  }
  public List<DescriptorDigest> getLayerDigests() {
    List<DescriptorDigest> layerDigests = new ArrayList<>();
    for (LayerObjectTemplate layerObjectTemplate : fsLayers) {
      layerDigests.add(layerObjectTemplate.blobSum);
    }
    return layerDigests;
  }
  @Override
  public int getSchemaVersion() {
    return schemaVersion;
  }
  public List<LayerObjectTemplate> getFsLayers() {
    return Collections.unmodifiableList(fsLayers);
  }
  @VisibleForTesting
  String getV1Compatibility(int index) {
    return history.get(index).v1Compatibility;
  }
}
