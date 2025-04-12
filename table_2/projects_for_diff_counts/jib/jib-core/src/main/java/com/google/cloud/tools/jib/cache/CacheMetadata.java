package com.google.cloud.tools.jib.cache;
class CacheMetadata {
  private final ImageLayers<CachedLayerWithMetadata> layers = new ImageLayers<>();
  static class LayerFilter {
    private final ImageLayers<CachedLayerWithMetadata> layers;
     private List<Path> sourceFiles;
    private LayerFilter(ImageLayers<CachedLayerWithMetadata> layers) {
      this.layers = layers;
    }
    LayerFilter bySourceFiles(List<Path> sourceFiles) {
      this.sourceFiles = sourceFiles;
      return this;
    }
    ImageLayers<CachedLayerWithMetadata> filter() throws CacheMetadataCorruptedException {
      try {
        ImageLayers<CachedLayerWithMetadata> filteredLayers = new ImageLayers<>();
        for (CachedLayerWithMetadata layer : layers) {
          if (sourceFiles != null) {
            if (layer.getMetadata() == null) {
              continue;
            }
            List<String> cachedLayerSourceFilePaths = layer.getMetadata().getSourceFiles();
            if (cachedLayerSourceFilePaths != null) {
              List<Path> cachedLayerSourceFiles = new ArrayList<>();
              for (String sourceFile : cachedLayerSourceFilePaths) {
                cachedLayerSourceFiles.add(Paths.get(sourceFile));
              }
              if (!cachedLayerSourceFiles.equals(sourceFiles)) {
                continue;
              }
            }
          }
          filteredLayers.add(layer);
        }
        return filteredLayers;
      } catch (LayerPropertyNotFoundException ex) {
        throw new CacheMetadataCorruptedException(ex);
      }
    }
  }
  ImageLayers<CachedLayerWithMetadata> getLayers() {
    return layers;
  }
  synchronized void addLayer(CachedLayerWithMetadata layer) throws LayerPropertyNotFoundException {
    layers.add(layer);
  }
  LayerFilter filterLayers() {
    return new LayerFilter(layers);
  }
}
