package com.google.cloud.tools.jib.image.json;
public class JsonToImageTranslatorTest {
  @Test
  public void testToImage_v21()
      throws IOException, LayerPropertyNotFoundException, DigestException, URISyntaxException {
    Path jsonFile =
        Paths.get(getClass().getClassLoader().getResource("json/v21manifest.json").toURI());
    V21ManifestTemplate manifestTemplate =
        JsonTemplateMapper.readJsonFromFile(jsonFile, V21ManifestTemplate.class);
    Image image = JsonToImageTranslator.toImage(manifestTemplate);
    List<Layer> layers = image.getLayers();
    Assert.assertEquals(1, layers.size());
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"),
        layers.get(0).getBlobDescriptor().getDigest());
  }
  @Test
  public void testToImage_v22()
      throws IOException, LayerPropertyNotFoundException, LayerCountMismatchException,
          DigestException, URISyntaxException {
    testToImage_buildable("json/v22manifest.json", V22ManifestTemplate.class);
  }
  @Test
  public void testToImage_oci()
      throws IOException, LayerPropertyNotFoundException, LayerCountMismatchException,
          DigestException, URISyntaxException {
    testToImage_buildable("json/ocimanifest.json", OCIManifestTemplate.class);
  }
  private <T extends BuildableManifestTemplate> void testToImage_buildable(
      String jsonFilename, Class<T> manifestTemplateClass)
      throws IOException, LayerPropertyNotFoundException, LayerCountMismatchException,
          DigestException, URISyntaxException {
    Path containerConfigurationJsonFile =
        Paths.get(getClass().getClassLoader().getResource("json/containerconfig.json").toURI());
    ContainerConfigurationTemplate containerConfigurationTemplate =
        JsonTemplateMapper.readJsonFromFile(
            containerConfigurationJsonFile, ContainerConfigurationTemplate.class);
    Path manifestJsonFile =
        Paths.get(getClass().getClassLoader().getResource(jsonFilename).toURI());
    T manifestTemplate =
        JsonTemplateMapper.readJsonFromFile(manifestJsonFile, manifestTemplateClass);
    Image image = JsonToImageTranslator.toImage(manifestTemplate, containerConfigurationTemplate);
    List<Layer> layers = image.getLayers();
    Assert.assertEquals(1, layers.size());
    Assert.assertEquals(
        new BlobDescriptor(
            1000000,
            DescriptorDigest.fromDigest(
                "sha256:4945ba5011739b0b98c4a41afe224e417f47c7c99b2ce76830999c9a0861b236")),
        layers.get(0).getBlobDescriptor());
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"),
        layers.get(0).getDiffId());
    Assert.assertEquals(Arrays.asList("some", "entrypoint", "command"), image.getEntrypoint());
    Assert.assertEquals(Arrays.asList("VAR1=VAL1", "VAR2=VAL2"), image.getEnvironment());
  }
}
