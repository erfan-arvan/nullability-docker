package com.google.cloud.tools.jib.registry;
@RunWith(MockitoJUnitRunner.class)
public class ManifestPusherTest {
  private Path v22manifestJsonFile;
  private V22ManifestTemplate fakeManifestTemplate;
  private ManifestPusher testManifestPusher;
  @Before
  public void setUp() throws URISyntaxException, IOException {
    v22manifestJsonFile = Paths.get(Resources.getResource("json/v22manifest.json").toURI());
    fakeManifestTemplate =
        JsonTemplateMapper.readJsonFromFile(v22manifestJsonFile, V22ManifestTemplate.class);
    testManifestPusher =
        new ManifestPusher(
            new RegistryEndpointProperties("someServerUrl", "someImageName"),
            fakeManifestTemplate,
            "test-image-tag");
  }
  @Test
  public void testGetContent() throws IOException {
    BlobHttpContent body = testManifestPusher.getContent();
    Assert.assertNotNull(body);
    Assert.assertEquals(V22ManifestTemplate.MANIFEST_MEDIA_TYPE, body.getType());
    ByteArrayOutputStream bodyCaptureStream = new ByteArrayOutputStream();
    body.writeTo(bodyCaptureStream);
    String v22manifestJson =
        new String(Files.readAllBytes(v22manifestJsonFile), StandardCharsets.UTF_8);
    Assert.assertEquals(
        v22manifestJson, new String(bodyCaptureStream.toByteArray(), StandardCharsets.UTF_8));
  }
  @Test
  public void testHandleResponse() {
    Assert.assertNull(testManifestPusher.handleResponse(Mockito.mock(Response.class)));
  }
  @Test
  public void testApiRoute() throws MalformedURLException {
    Assert.assertEquals(
        new URL("http:
        testManifestPusher.getApiRoute("http:
  }
  @Test
  public void testGetHttpMethod() {
    Assert.assertEquals("PUT", testManifestPusher.getHttpMethod());
  }
  @Test
  public void testGetActionDescription() {
    Assert.assertEquals(
        "push image manifest for someServerUrl/someImageName:test-image-tag",
        testManifestPusher.getActionDescription());
  }
  @Test
  public void testGetAccept() {
    Assert.assertEquals(0, testManifestPusher.getAccept().size());
  }
}
