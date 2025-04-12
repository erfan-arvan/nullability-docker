package com.google.cloud.tools.jib.image;
@RunWith(MockitoJUnitRunner.class)
public class ImageLayersTest {
  @Mock private CachedLayer mockCachedLayer;
  @Mock private ReferenceLayer mockReferenceLayer;
  @Mock private DigestOnlyLayer mockDigestOnlyLayer;
  @Mock private UnwrittenLayer mockUnwrittenLayer;
  @Before
  public void setUpFakes() throws LayerPropertyNotFoundException {
    DescriptorDigest mockDescriptorDigest1 = Mockito.mock(DescriptorDigest.class);
    DescriptorDigest mockDescriptorDigest2 = Mockito.mock(DescriptorDigest.class);
    DescriptorDigest mockDescriptorDigest3 = Mockito.mock(DescriptorDigest.class);
    BlobDescriptor cachedLayerBlobDescriptor = new BlobDescriptor(0, mockDescriptorDigest1);
    BlobDescriptor referenceLayerBlobDescriptor = new BlobDescriptor(0, mockDescriptorDigest2);
    BlobDescriptor referenceNoDiffIdLayerBlobDescriptor =
        new BlobDescriptor(0, mockDescriptorDigest3);
    BlobDescriptor unwrittenLayerBlobDescriptor = new BlobDescriptor(0, mockDescriptorDigest1);
    Mockito.when(mockCachedLayer.getBlobDescriptor()).thenReturn(cachedLayerBlobDescriptor);
    Mockito.when(mockReferenceLayer.getBlobDescriptor()).thenReturn(referenceLayerBlobDescriptor);
    Mockito.when(mockDigestOnlyLayer.getBlobDescriptor())
        .thenReturn(referenceNoDiffIdLayerBlobDescriptor);
    Mockito.when(mockUnwrittenLayer.getBlobDescriptor()).thenReturn(unwrittenLayerBlobDescriptor);
  }
  @Test
  public void testAddLayer_success() throws LayerPropertyNotFoundException {
    List<Layer> expectedLayers =
        Arrays.asList(mockCachedLayer, mockReferenceLayer, mockDigestOnlyLayer);
    ImageLayers<Layer> imageLayers = new ImageLayers<>();
    imageLayers.add(mockCachedLayer);
    imageLayers.add(mockReferenceLayer);
    imageLayers.add(mockDigestOnlyLayer);
    Assert.assertThat(imageLayers.getLayers(), CoreMatchers.is(expectedLayers));
  }
  @Test
  public void testAddLayer_sameAsLastLayer() throws LayerPropertyNotFoundException {
    List<Layer> expectedLayers =
        Arrays.asList(mockCachedLayer, mockReferenceLayer, mockDigestOnlyLayer, mockUnwrittenLayer);
    ImageLayers<Layer> imageLayers = new ImageLayers<>();
    imageLayers.add(mockCachedLayer);
    imageLayers.add(mockReferenceLayer);
    imageLayers.add(mockDigestOnlyLayer);
    imageLayers.add(mockUnwrittenLayer);
    imageLayers.add(mockCachedLayer);
    Assert.assertEquals(expectedLayers, imageLayers.getLayers());
  }
}
