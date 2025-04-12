package com.google.cloud.tools.jib.image.json;
public class ImageToJsonTranslatorTest {
  private ImageToJsonTranslator imageToJsonTranslator;
  @Before
  public void setUp() throws DigestException, LayerPropertyNotFoundException {
    Image testImage = new Image();
    testImage.setEnvironmentVariable("VAR1", "VAL1");
    testImage.setEnvironmentVariable("VAR2", "VAL2");
    testImage.setEntrypoint(Arrays.asList("some", "entrypoint", "command"));
    DescriptorDigest fakeDigest =
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad");
    Layer fakeLayer = new ReferenceLayer(new BlobDescriptor(1000, fakeDigest), fakeDigest);
    testImage.addLayer(fakeLayer);
    imageToJsonTranslator = new ImageToJsonTranslator(testImage);
  }
  @Test
  public void testGetContainerConfiguration()
      throws IOException, LayerPropertyNotFoundException, URISyntaxException {
    Path jsonFile = Paths.get(Resources.getResource("json/containerconfig.json").toURI());
    String expectedJson = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
    Blob containerConfigurationBlob = imageToJsonTranslator.getContainerConfigurationBlob();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    containerConfigurationBlob.writeTo(byteArrayOutputStream);
    Assert.assertEquals(
        expectedJson, new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
  }
  @Test
  public void testGetManifest_v22()
      throws URISyntaxException, IOException, LayerPropertyNotFoundException {
    testGetManifest(V22ManifestTemplate.class, "json/translated_v22manifest.json");
  }
  @Test
  public void testGetManifest_oci()
      throws URISyntaxException, IOException, LayerPropertyNotFoundException {
    testGetManifest(OCIManifestTemplate.class, "json/translated_ocimanifest.json");
  }
  private <T extends BuildableManifestTemplate> void testGetManifest(
      Class<T> manifestTemplateClass, String translatedJsonFilename)
      throws URISyntaxException, IOException, LayerPropertyNotFoundException {
    Path jsonFile = Paths.get(Resources.getResource(translatedJsonFilename).toURI());
    String expectedJson = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
    Blob containerConfigurationBlob = imageToJsonTranslator.getContainerConfigurationBlob();
    BlobDescriptor blobDescriptor =
        containerConfigurationBlob.writeTo(ByteStreams.nullOutputStream());
    T manifestTemplate =
        imageToJsonTranslator.getManifestTemplate(manifestTemplateClass, blobDescriptor);
    ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
    JsonTemplateMapper.toBlob(manifestTemplate).writeTo(jsonStream);
    Assert.assertEquals(expectedJson, new String(jsonStream.toByteArray(), StandardCharsets.UTF_8));
  }
}
