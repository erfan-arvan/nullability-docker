package com.google.cloud.tools.jib.registry;
@RunWith(MockitoJUnitRunner.class)
public class ManifestPullerTest {
  @Mock private Response mockResponse;
  private final RegistryEndpointProperties fakeRegistryEndpointProperties =
      new RegistryEndpointProperties("someServerUrl", "someImageName");
  private final ManifestPuller<ManifestTemplate> testManifestPuller =
      new ManifestPuller<>(
          fakeRegistryEndpointProperties, "test-image-tag", ManifestTemplate.class);
  @Test
  public void testHandleResponse_v21()
      throws URISyntaxException, IOException, UnknownManifestFormatException {
    Path v21ManifestFile = Paths.get(Resources.getResource("json/v21manifest.json").toURI());
    Mockito.when(mockResponse.getBody()).thenReturn(Blobs.from(v21ManifestFile));
    ManifestTemplate manifestTemplate =
        new ManifestPuller<>(
                fakeRegistryEndpointProperties, "test-image-tag", V21ManifestTemplate.class)
            .handleResponse(mockResponse);
    Assert.assertThat(manifestTemplate, CoreMatchers.instanceOf(V21ManifestTemplate.class));
  }
  @Test
  public void testHandleResponse_v22()
      throws URISyntaxException, IOException, UnknownManifestFormatException {
    Path v22ManifestFile = Paths.get(Resources.getResource("json/v22manifest.json").toURI());
    Mockito.when(mockResponse.getBody()).thenReturn(Blobs.from(v22ManifestFile));
    ManifestTemplate manifestTemplate =
        new ManifestPuller<>(
                fakeRegistryEndpointProperties, "test-image-tag", V22ManifestTemplate.class)
            .handleResponse(mockResponse);
    Assert.assertThat(manifestTemplate, CoreMatchers.instanceOf(V22ManifestTemplate.class));
  }
  @Test
  public void testHandleResponse_noSchemaVersion() throws IOException {
    Mockito.when(mockResponse.getBody()).thenReturn(Blobs.from("{}"));
    try {
      testManifestPuller.handleResponse(mockResponse);
      Assert.fail("An empty manifest should throw an error");
    } catch (UnknownManifestFormatException ex) {
      Assert.assertEquals("Cannot find field 'schemaVersion' in manifest", ex.getMessage());
    }
  }
  @Test
  public void testHandleResponse_invalidSchemaVersion() throws IOException {
    Mockito.when(mockResponse.getBody())
        .thenReturn(Blobs.from("{\"schemaVersion\":\"not valid\"}"));
    try {
      testManifestPuller.handleResponse(mockResponse);
      Assert.fail("A non-integer schemaVersion should throw an error");
    } catch (UnknownManifestFormatException ex) {
      Assert.assertEquals("`schemaVersion` field is not an integer", ex.getMessage());
    }
  }
  @Test
  public void testHandleResponse_unknownSchemaVersion() throws IOException {
    Mockito.when(mockResponse.getBody()).thenReturn(Blobs.from("{\"schemaVersion\":0}"));
    try {
      testManifestPuller.handleResponse(mockResponse);
      Assert.fail("An unknown manifest schemaVersion should throw an error");
    } catch (UnknownManifestFormatException ex) {
      Assert.assertEquals("Unknown schemaVersion: 0 - only 1 and 2 are supported", ex.getMessage());
    }
  }
  @Test
  public void testGetApiRoute() throws MalformedURLException {
    Assert.assertEquals(
        new URL("http:
        testManifestPuller.getApiRoute("http:
  }
  @Test
  public void testGetHttpMethod() {
    Assert.assertEquals("GET", testManifestPuller.getHttpMethod());
  }
  @Test
  public void testGetActionDescription() {
    Assert.assertEquals(
        "pull image manifest for someServerUrl/someImageName:test-image-tag",
        testManifestPuller.getActionDescription());
  }
  @Test
  public void testGetContent() {
    Assert.assertNull(testManifestPuller.getContent());
  }
  @Test
  public void testGetAccept() {
    Assert.assertEquals(
        Arrays.asList(
            OCIManifestTemplate.MANIFEST_MEDIA_TYPE,
            V22ManifestTemplate.MANIFEST_MEDIA_TYPE,
            V21ManifestTemplate.MEDIA_TYPE),
        testManifestPuller.getAccept());
    Assert.assertEquals(
        Collections.singletonList(OCIManifestTemplate.MANIFEST_MEDIA_TYPE),
        new ManifestPuller<>(
                fakeRegistryEndpointProperties, "test-image-tag", OCIManifestTemplate.class)
            .getAccept());
    Assert.assertEquals(
        Collections.singletonList(V22ManifestTemplate.MANIFEST_MEDIA_TYPE),
        new ManifestPuller<>(
                fakeRegistryEndpointProperties, "test-image-tag", V22ManifestTemplate.class)
            .getAccept());
    Assert.assertEquals(
        Collections.singletonList(V21ManifestTemplate.MEDIA_TYPE),
        new ManifestPuller<>(
                fakeRegistryEndpointProperties, "test-image-tag", V21ManifestTemplate.class)
            .getAccept());
  }
}
