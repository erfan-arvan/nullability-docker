package com.google.cloud.tools.jib.cache.json;
public class CacheMetadataTemplate implements JsonTemplate {
  private final List<CacheMetadataLayerObjectTemplate> layers = new ArrayList<>();
  public List<CacheMetadataLayerObjectTemplate> getLayers() {
    return layers;
  }
  public CacheMetadataTemplate addLayer(CacheMetadataLayerObjectTemplate layer) {
    layers.add(layer);
    return this;
  }
}
