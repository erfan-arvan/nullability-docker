package com.google.cloud.tools.jib.image;
@RunWith(MockitoJUnitRunner.class)
public class LayerTest {
  @Mock private Blob mockUncompressedBlob;
  @Mock private DescriptorDigest mockDescriptorDigest;
  @Mock private BlobDescriptor mockBlobDescriptor;
  @Mock private DescriptorDigest mockDiffId;
  @Test
  public void testNew_unwritten() throws LayerPropertyNotFoundException {
    Layer layer = new UnwrittenLayer(mockUncompressedBlob);
    Assert.assertEquals(mockUncompressedBlob, layer.getBlob());
    try {
      layer.getBlobDescriptor();
      Assert.fail("Blob descriptor should not be available for unwritten layer");
    } catch (LayerPropertyNotFoundException ex) {
      Assert.assertEquals("Blob descriptor not available for unwritten layer", ex.getMessage());
    }
    try {
      layer.getDiffId();
      Assert.fail("Diff ID should not be available for unwritten layer");
    } catch (LayerPropertyNotFoundException ex) {
      Assert.assertEquals("Diff ID not available for unwritten layer", ex.getMessage());
    }
  }
  @Test
  public void testNew_reference() throws LayerPropertyNotFoundException {
    Layer layer = new ReferenceLayer(mockBlobDescriptor, mockDiffId);
    try {
      layer.getBlob();
      Assert.fail("Blob content should not be available for reference layer");
    } catch (LayerPropertyNotFoundException ex) {
      Assert.assertEquals("Blob not available for reference layer", ex.getMessage());
    }
    Assert.assertEquals(mockBlobDescriptor, layer.getBlobDescriptor());
    Assert.assertEquals(mockDiffId, layer.getDiffId());
  }
  @Test
  public void testNew_digestOnly() throws LayerPropertyNotFoundException {
    Layer layer = new DigestOnlyLayer(mockDescriptorDigest);
    try {
      layer.getBlob();
      Assert.fail("Blob content should not be available for digest-only layer");
    } catch (LayerPropertyNotFoundException ex) {
      Assert.assertEquals("Blob not available for digest-only layer", ex.getMessage());
    }
    Assert.assertFalse(layer.getBlobDescriptor().hasSize());
    Assert.assertEquals(mockDescriptorDigest, layer.getBlobDescriptor().getDigest());
    try {
      layer.getDiffId();
      Assert.fail("Diff ID should not be available for digest-only layer");
    } catch (LayerPropertyNotFoundException ex) {
      Assert.assertEquals("Diff ID not available for digest-only layer", ex.getMessage());
    }
  }
}
