package com.google.cloud.tools.jib.image.json;
public class OCIManifestTemplateTest {
  @Test
  public void testToJson() throws DigestException, IOException, URISyntaxException {
    Path jsonFile = Paths.get(Resources.getResource("json/ocimanifest.json").toURI());
    String expectedJson = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
    OCIManifestTemplate manifestJson = new OCIManifestTemplate();
    manifestJson.setContainerConfiguration(
        1000,
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"));
    manifestJson.addLayer(
        1000_000,
        DescriptorDigest.fromHash(
            "4945ba5011739b0b98c4a41afe224e417f47c7c99b2ce76830999c9a0861b236"));
    ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
    JsonTemplateMapper.toBlob(manifestJson).writeTo(jsonStream);
    Assert.assertEquals(expectedJson, jsonStream.toString());
  }
  @Test
  public void testFromJson() throws IOException, URISyntaxException, DigestException {
    Path jsonFile = Paths.get(Resources.getResource("json/ocimanifest.json").toURI());
    OCIManifestTemplate manifestJson =
        JsonTemplateMapper.readJsonFromFile(jsonFile, OCIManifestTemplate.class);
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"),
        manifestJson.getContainerConfiguration().getDigest());
    Assert.assertEquals(1000, manifestJson.getContainerConfiguration().getSize());
    Assert.assertEquals(
        DescriptorDigest.fromHash(
            "4945ba5011739b0b98c4a41afe224e417f47c7c99b2ce76830999c9a0861b236"),
        manifestJson.getLayers().get(0).getDigest());
    Assert.assertEquals(1000_000, manifestJson.getLayers().get(0).getSize());
  }
}
