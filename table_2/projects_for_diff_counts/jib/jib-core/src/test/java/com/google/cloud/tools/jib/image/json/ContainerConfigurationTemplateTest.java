package com.google.cloud.tools.jib.image.json;
public class ContainerConfigurationTemplateTest {
  @Test
  public void testToJson() throws IOException, URISyntaxException, DigestException {
    Path jsonFile = Paths.get(Resources.getResource("json/containerconfig.json").toURI());
    String expectedJson = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
    ContainerConfigurationTemplate containerConfigJson = new ContainerConfigurationTemplate();
    containerConfigJson.setContainerEnvironment(Arrays.asList("VAR1=VAL1", "VAR2=VAL2"));
    containerConfigJson.setContainerEntrypoint(Arrays.asList("some", "entrypoint", "command"));
    containerConfigJson.addLayerDiffId(
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"));
    ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
    JsonTemplateMapper.toBlob(containerConfigJson).writeTo(jsonStream);
    Assert.assertEquals(expectedJson, jsonStream.toString());
  }
  @Test
  public void testFromJson() throws IOException, URISyntaxException, DigestException {
    Path jsonFile = Paths.get(Resources.getResource("json/containerconfig.json").toURI());
    ContainerConfigurationTemplate containerConfigJson =
        JsonTemplateMapper.readJsonFromFile(jsonFile, ContainerConfigurationTemplate.class);
    Assert.assertEquals(
        Arrays.asList("VAR1=VAL1", "VAR2=VAL2"), containerConfigJson.getContainerEnvironment());
    Assert.assertEquals(
        Arrays.asList("some", "entrypoint", "command"),
        containerConfigJson.getContainerEntrypoint());
    Assert.assertEquals(
        DescriptorDigest.fromDigest(
            "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad"),
        containerConfigJson.getLayerDiffId(0));
  }
}
