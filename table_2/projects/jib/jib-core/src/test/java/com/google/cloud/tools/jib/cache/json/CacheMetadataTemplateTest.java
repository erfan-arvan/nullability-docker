package com.google.cloud.tools.jib.cache.json;
public class CacheMetadataTemplateTest {
  @Test
  public void testToJson() throws URISyntaxException, IOException, DigestException {
    Path jsonFile = PlatformSpecificMetadataJson.getMetadataJsonFile();
    String expectedJson = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
    CacheMetadataTemplate cacheMetadataTemplate = new CacheMetadataTemplate();
    CacheMetadataLayerObjectTemplate baseLayerTemplate =
        new CacheMetadataLayerObjectTemplate()
            .setSize(631)
            .setDigest(
                DescriptorDigest.fromDigest(
                    "sha256:5f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef"))
            .setDiffId(
                DescriptorDigest.fromDigest(
                    "sha256:b56ae66c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a4647"));
    CacheMetadataLayerPropertiesObjectTemplate propertiesTemplate =
        new CacheMetadataLayerPropertiesObjectTemplate()
            .setSourceFiles(
                Collections.singletonList(Paths.get("some", "source", "path").toString()))
            .setLastModifiedTime(FileTime.fromMillis(255073580723571L));
    CacheMetadataLayerObjectTemplate classesLayerTemplate =
        new CacheMetadataLayerObjectTemplate()
            .setSize(223)
            .setDigest(
                DescriptorDigest.fromDigest(
                    "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"))
            .setDiffId(
                DescriptorDigest.fromDigest(
                    "sha256:a3f3e99c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a8372"))
            .setProperties(propertiesTemplate);
    cacheMetadataTemplate.addLayer(baseLayerTemplate).addLayer(classesLayerTemplate);
    ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
    JsonTemplateMapper.toBlob(cacheMetadataTemplate).writeTo(jsonStream);
    Assert.assertEquals(expectedJson, jsonStream.toString());
  }
  @Test
  public void testFromJson() throws URISyntaxException, IOException, DigestException {
    Path jsonFile = PlatformSpecificMetadataJson.getMetadataJsonFile();
    CacheMetadataTemplate metadataTemplate =
        JsonTemplateMapper.readJsonFromFile(jsonFile, CacheMetadataTemplate.class);
    List<CacheMetadataLayerObjectTemplate> layers = metadataTemplate.getLayers();
    Assert.assertEquals(2, layers.size());
    CacheMetadataLayerObjectTemplate baseLayerTemplate = layers.get(0);
    Assert.assertEquals(631, baseLayerTemplate.getSize());
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:5f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef"),
        baseLayerTemplate.getDigest());
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:b56ae66c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a4647"),
        baseLayerTemplate.getDiffId());
    Assert.assertNull(baseLayerTemplate.getProperties());
    CacheMetadataLayerObjectTemplate classesLayerTemplate = layers.get(1);
    Assert.assertEquals(223, classesLayerTemplate.getSize());
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"),
        classesLayerTemplate.getDigest());
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:a3f3e99c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a8372"),
        classesLayerTemplate.getDiffId());
    Assert.assertNotNull(classesLayerTemplate.getProperties());
    Assert.assertEquals(
        Collections.singletonList(Paths.get("some", "source", "path").toString()),
        classesLayerTemplate.getProperties().getSourceFiles());
    Assert.assertEquals(
        FileTime.fromMillis(255073580723571L),
        classesLayerTemplate.getProperties().getLastModifiedTime());
  }
}
