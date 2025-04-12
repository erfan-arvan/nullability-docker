package com.google.cloud.tools.jib.image.json;
public class V21ManifestTemplateTest {
  @Test
  public void testFromJson() throws URISyntaxException, IOException, DigestException {
    Path jsonFile = Paths.get(Resources.getResource("json/v21manifest.json").toURI());
    V21ManifestTemplate manifestJson =
        JsonTemplateMapper.readJsonFromFile(jsonFile, V21ManifestTemplate.class);
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"),
        manifestJson.getFsLayers().get(0).getDigest());
    Assert.assertEquals("some v1-compatible object", manifestJson.getV1Compatibility(0));
  }
}
