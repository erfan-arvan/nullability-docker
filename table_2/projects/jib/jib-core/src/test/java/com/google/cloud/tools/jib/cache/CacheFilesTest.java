package com.google.cloud.tools.jib.cache;
@RunWith(MockitoJUnitRunner.class)
public class CacheFilesTest {
  @Mock private Path mockPath;
  @Test
  public void testGetMetadataFile() {
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.when(mockPath.resolve(fileNameCaptor.capture())).thenReturn(mockPath);
    Path metadataFile = CacheFiles.getMetadataFile(mockPath);
    Assert.assertEquals("metadata.json", fileNameCaptor.getValue());
    Assert.assertEquals(mockPath, metadataFile);
  }
  @Test
  public void testGetLayerFile() throws DigestException {
    DescriptorDigest layerDigest =
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad");
    ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.when(mockPath.resolve(fileNameCaptor.capture())).thenReturn(mockPath);
    Path layerFile = CacheFiles.getLayerFile(mockPath, layerDigest);
    Assert.assertEquals(
        "8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad.tar.gz",
        fileNameCaptor.getValue());
    Assert.assertEquals(mockPath, layerFile);
  }
}
