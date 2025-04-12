package com.google.cloud.tools.jib.cache;
public class CacheMetadataTranslator {
  static CacheMetadata fromTemplate(CacheMetadataTemplate template, Path cacheDirectory)
      throws CacheMetadataCorruptedException {
    try {
      CacheMetadata cacheMetadata = new CacheMetadata();
      for (CacheMetadataLayerObjectTemplate layerObjectTemplate : template.getLayers()) {
        if (layerObjectTemplate.getDigest() == null || layerObjectTemplate.getDiffId() == null) {
          throw new IllegalStateException(
              "Cannot translate cache metadata layer without a digest or diffId");
        }
        Path layerContentFile =
            CacheFiles.getLayerFile(cacheDirectory, layerObjectTemplate.getDigest());
        CacheMetadataLayerPropertiesObjectTemplate propertiesObjectTemplate =
            layerObjectTemplate.getProperties();
        LayerMetadata layerMetadata = null;
        if (propertiesObjectTemplate != null) {
          layerMetadata =
              new LayerMetadata(
                  propertiesObjectTemplate.getSourceFiles(),
                  propertiesObjectTemplate.getLastModifiedTime());
        }
        CachedLayer cachedLayer =
            new CachedLayer(
                layerContentFile,
                new BlobDescriptor(layerObjectTemplate.getSize(), layerObjectTemplate.getDigest()),
                layerObjectTemplate.getDiffId());
        CachedLayerWithMetadata cachedLayerWithMetadata =
            new CachedLayerWithMetadata(cachedLayer, layerMetadata);
        cacheMetadata.addLayer(cachedLayerWithMetadata);
      }
      return cacheMetadata;
    } catch (LayerPropertyNotFoundException ex) {
      throw new CacheMetadataCorruptedException(ex);
    }
  }
  static CacheMetadataTemplate toTemplate(CacheMetadata cacheMetadata) {
    CacheMetadataTemplate template = new CacheMetadataTemplate();
    for (CachedLayerWithMetadata cachedLayerWithMetadata : cacheMetadata.getLayers()) {
      CacheMetadataLayerObjectTemplate layerObjectTemplate =
          new CacheMetadataLayerObjectTemplate()
              .setSize(cachedLayerWithMetadata.getBlobDescriptor().getSize())
              .setDigest(cachedLayerWithMetadata.getBlobDescriptor().getDigest())
              .setDiffId(cachedLayerWithMetadata.getDiffId());
      if (cachedLayerWithMetadata.getMetadata() != null) {
        layerObjectTemplate.setProperties(
            new CacheMetadataLayerPropertiesObjectTemplate()
                .setSourceFiles(cachedLayerWithMetadata.getMetadata().getSourceFiles())
                .setLastModifiedTime(cachedLayerWithMetadata.getMetadata().getLastModifiedTime()));
      }
      template.addLayer(layerObjectTemplate);
    }
    return template;
  }
}
