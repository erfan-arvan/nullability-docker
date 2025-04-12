package com.google.cloud.tools.jib.registry.credentials.json;
public class DockerConfigTemplateTest {
  @Test
  public void test_toJson() throws URISyntaxException, IOException {
    Path jsonFile = Paths.get(Resources.getResource("json/dockerconfig.json").toURI());
    String expectedJson = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
    DockerConfigTemplate dockerConfigTemplate =
        new DockerConfigTemplate()
            .addAuth("some registry", "some auth")
            .addAuth("some other registry", "some other auth")
            .addAuth("just registry", null)
            .setCredsStore("some credential store")
            .addCredHelper("some registry", "some credential helper")
            .addCredHelper("another registry", "another credential helper");
    ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
    JsonTemplateMapper.toBlob(dockerConfigTemplate).writeTo(jsonStream);
    Assert.assertEquals(expectedJson, jsonStream.toString());
  }
  @Test
  public void test_fromJson() throws URISyntaxException, IOException {
    Path jsonFile = Paths.get(Resources.getResource("json/dockerconfig.json").toURI());
    DockerConfigTemplate dockerConfigTemplate =
        JsonTemplateMapper.readJsonFromFile(jsonFile, DockerConfigTemplate.class);
    Assert.assertEquals("some auth", dockerConfigTemplate.getAuthFor("some registry"));
    Assert.assertEquals("some other auth", dockerConfigTemplate.getAuthFor("some other registry"));
    Assert.assertEquals(null, dockerConfigTemplate.getAuthFor("just registry"));
    Assert.assertEquals(
        "some credential store", dockerConfigTemplate.getCredentialHelperFor("some registry"));
    Assert.assertEquals(
        "some credential store",
        dockerConfigTemplate.getCredentialHelperFor("some other registry"));
    Assert.assertEquals(
        "some credential store", dockerConfigTemplate.getCredentialHelperFor("just registry"));
    Assert.assertEquals(
        "another credential helper",
        dockerConfigTemplate.getCredentialHelperFor("another registry"));
    Assert.assertEquals(null, dockerConfigTemplate.getCredentialHelperFor("unknonwn registry"));
  }
}
