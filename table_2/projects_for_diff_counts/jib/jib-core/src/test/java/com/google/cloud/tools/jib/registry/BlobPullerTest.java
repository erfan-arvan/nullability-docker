package com.google.cloud.tools.jib.registry;
@RunWith(MockitoJUnitRunner.class)
public class BlobPullerTest {
  private final RegistryEndpointProperties fakeRegistryEndpointProperties =
      new RegistryEndpointProperties("someServerUrl", "someImageName");
  private DescriptorDigest fakeDigest;
  private final ByteArrayOutputStream layerContentOutputStream = new ByteArrayOutputStream();
  private final CountingDigestOutputStream layerOutputStream =
      new CountingDigestOutputStream(layerContentOutputStream);
  private BlobPuller testBlobPuller;
  @Before
  public void setUpFakes() throws DigestException, IOException {
    fakeDigest =
        DescriptorDigest.fromHash(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    testBlobPuller = new BlobPuller(fakeRegistryEndpointProperties, fakeDigest, layerOutputStream);
  }
  @Test
  public void testHandleResponse() throws IOException, UnexpectedBlobDigestException {
    Blob testBlob = Blobs.from("some BLOB content");
    DescriptorDigest testBlobDigest = testBlob.writeTo(ByteStreams.nullOutputStream()).getDigest();
    Response mockResponse = Mockito.mock(Response.class);
    Mockito.when(mockResponse.getBody()).thenReturn(testBlob);
    BlobPuller blobPuller =
        new BlobPuller(fakeRegistryEndpointProperties, testBlobDigest, layerOutputStream);
    blobPuller.handleResponse(mockResponse);
    Assert.assertEquals(
        "some BLOB content",
        new String(layerContentOutputStream.toByteArray(), StandardCharsets.UTF_8));
    Assert.assertEquals(testBlobDigest, layerOutputStream.toBlobDescriptor().getDigest());
  }
  @Test
  public void testHandleResponse_unexpectedDigest() throws IOException {
    Blob testBlob = Blobs.from("some BLOB content");
    DescriptorDigest testBlobDigest = testBlob.writeTo(ByteStreams.nullOutputStream()).getDigest();
    Response mockResponse = Mockito.mock(Response.class);
    Mockito.when(mockResponse.getBody()).thenReturn(testBlob);
    try {
      testBlobPuller.handleResponse(mockResponse);
      Assert.fail("Receiving an unexpected digest should fail");
    } catch (UnexpectedBlobDigestException ex) {
      Assert.assertEquals(
          "The pulled BLOB has digest '"
              + testBlobDigest
              + "', but the request digest was '"
              + fakeDigest
              + "'",
          ex.getMessage());
    }
  }
  @Test
  public void testGetApiRoute() throws MalformedURLException {
    Assert.assertEquals(
        new URL("http:
        testBlobPuller.getApiRoute("http:
  }
  @Test
  public void testGetActionDescription() {
    Assert.assertEquals(
        "pull BLOB for someServerUrl/someImageName with digest " + fakeDigest,
        testBlobPuller.getActionDescription());
  }
  @Test
  public void testGetHttpMethod() {
    Assert.assertEquals("GET", testBlobPuller.getHttpMethod());
  }
  @Test
  public void testGetContent() {
    Assert.assertNull(testBlobPuller.getContent());
  }
  @Test
  public void testGetAccept() {
    Assert.assertEquals(0, testBlobPuller.getAccept().size());
  }
}
