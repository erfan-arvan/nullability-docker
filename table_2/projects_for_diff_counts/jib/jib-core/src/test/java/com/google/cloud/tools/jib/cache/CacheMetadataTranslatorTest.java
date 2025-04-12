package com.google.cloud.tools.jib.cache;
@RunWith(MockitoJUnitRunner.class)
public class CacheMetadataTranslatorTest {
  @Mock private Path mockPath;
  private BlobDescriptor baseLayerBlobDescriptor;
  private DescriptorDigest baseLayerDiffId;
  private BlobDescriptor classesLayerBlobDescriptor;
  private DescriptorDigest classesLayerDiffId;
  private final List<String> classesLayerSourceFiles =
      Collections.singletonList(Paths.get("some", "source", "path").toString());
  private final FileTime classesLayerLastModifiedTime = FileTime.fromMillis(255073580723571L);
  @Before
  public void setUp() throws DigestException {
    baseLayerBlobDescriptor =
        new BlobDescriptor(
            631,
            DescriptorDigest.fromDigest(
                "sha256:5f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef"));
    baseLayerDiffId =
        DescriptorDigest.fromDigest(
            "sha256:b56ae66c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a4647");
    classesLayerBlobDescriptor =
        new BlobDescriptor(
            223,
            DescriptorDigest.fromDigest(
                "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"));
    classesLayerDiffId =
        DescriptorDigest.fromDigest(
            "sha256:a3f3e99c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a8372");
  }
  @Test
  public void testFromTemplate()
      throws URISyntaxException, IOException, CacheMetadataCorruptedException {
    Path fakePath = Paths.get("fake/path");
    Path jsonFile = PlatformSpecificMetadataJson.getMetadataJsonFile();
    CacheMetadataTemplate metadataTemplate =
        JsonTemplateMapper.readJsonFromFile(jsonFile, CacheMetadataTemplate.class);
    CacheMetadata cacheMetadata = CacheMetadataTranslator.fromTemplate(metadataTemplate, fakePath);
    List<CachedLayerWithMetadata> layers = cacheMetadata.getLayers().getLayers();
    CachedLayerWithMetadata baseLayer = layers.get(0);
    Assert.assertEquals(
        CacheFiles.getLayerFile(fakePath, baseLayerBlobDescriptor.getDigest()),
        baseLayer.getContentFile());
    Assert.assertEquals(baseLayerBlobDescriptor, baseLayer.getBlobDescriptor());
    Assert.assertEquals(baseLayerDiffId, baseLayer.getDiffId());
    CachedLayerWithMetadata classesLayer = layers.get(1);
    Assert.assertEquals(
        CacheFiles.getLayerFile(fakePath, classesLayerBlobDescriptor.getDigest()),
        classesLayer.getContentFile());
    Assert.assertEquals(classesLayerBlobDescriptor, classesLayer.getBlobDescriptor());
    Assert.assertEquals(classesLayerDiffId, classesLayer.getDiffId());
    Assert.assertNotNull(classesLayer.getMetadata());
    Assert.assertEquals(classesLayerSourceFiles, classesLayer.getMetadata().getSourceFiles());
    Assert.assertEquals(
        classesLayerLastModifiedTime, classesLayer.getMetadata().getLastModifiedTime());
  }
  @Test
  public void testToTemplate()
      throws LayerPropertyNotFoundException, URISyntaxException, IOException {
    Path jsonFile = PlatformSpecificMetadataJson.getMetadataJsonFile();
    String expectedJson = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
    CacheMetadata cacheMetadata = new CacheMetadata();
    CachedLayer baseCachedLayer =
        new CachedLayer(mockPath, baseLayerBlobDescriptor, baseLayerDiffId);
    CachedLayerWithMetadata baseLayer = new CachedLayerWithMetadata(baseCachedLayer, null);
    CachedLayer classesCachedLayer =
        new CachedLayer(mockPath, classesLayerBlobDescriptor, classesLayerDiffId);
    LayerMetadata classesLayerMetadata =
        new LayerMetadata(classesLayerSourceFiles, classesLayerLastModifiedTime);
    CachedLayerWithMetadata classesLayer =
        new CachedLayerWithMetadata(classesCachedLayer, classesLayerMetadata);
    cacheMetadata.addLayer(baseLayer);
    cacheMetadata.addLayer(classesLayer);
    CacheMetadataTemplate cacheMetadataTemplate = CacheMetadataTranslator.toTemplate(cacheMetadata);
    ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
    JsonTemplateMapper.toBlob(cacheMetadataTemplate).writeTo(jsonStream);
    Assert.assertEquals(expectedJson, jsonStream.toString());
  }
}
