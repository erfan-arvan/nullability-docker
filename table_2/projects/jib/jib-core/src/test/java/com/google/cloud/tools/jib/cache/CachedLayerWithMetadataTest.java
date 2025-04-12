package com.google.cloud.tools.jib.cache;
public class CachedLayerWithMetadataTest {
  @Test
  public void testNew() {
    LayerMetadata mockLayerMetadata = Mockito.mock(LayerMetadata.class);
    CachedLayerWithMetadata cachedLayerWithMetadata =
        new CachedLayerWithMetadata(Mockito.mock(CachedLayer.class), mockLayerMetadata);
    Assert.assertEquals(mockLayerMetadata, cachedLayerWithMetadata.getMetadata());
  }
}
