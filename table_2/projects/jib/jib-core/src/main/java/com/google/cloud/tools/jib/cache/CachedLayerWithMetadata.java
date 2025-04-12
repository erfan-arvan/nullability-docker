package com.google.cloud.tools.jib.cache;
class CachedLayerWithMetadata extends CachedLayer {
   private final LayerMetadata metadata;
  CachedLayerWithMetadata(CachedLayer cachedLayer,  LayerMetadata metadata) {
    super(cachedLayer.getContentFile(), cachedLayer.getBlobDescriptor(), cachedLayer.getDiffId());
    this.metadata = metadata;
  }
  LayerMetadata getMetadata() {
    return metadata;
  }
}
