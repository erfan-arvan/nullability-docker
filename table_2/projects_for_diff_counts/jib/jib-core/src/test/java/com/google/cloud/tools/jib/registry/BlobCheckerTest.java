package com.google.cloud.tools.jib.registry;
@RunWith(MockitoJUnitRunner.class)
public class BlobCheckerTest {
  @Mock private Response mockResponse;
  private final RegistryEndpointProperties fakeRegistryEndpointProperties =
      new RegistryEndpointProperties("someServerUrl", "someImageName");
  private BlobChecker testBlobChecker;
  private DescriptorDigest fakeDigest;
  @Before
  public void setUpFakes() throws DigestException {
    fakeDigest =
        DescriptorDigest.fromHash(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    testBlobChecker = new BlobChecker(fakeRegistryEndpointProperties, fakeDigest);
  }
  @Test
  public void testHandleResponse() throws RegistryErrorException {
    Mockito.when(mockResponse.getContentLength()).thenReturn(0L);
    BlobDescriptor expectedBlobDescriptor = new BlobDescriptor(0, fakeDigest);
    BlobDescriptor blobDescriptor = testBlobChecker.handleResponse(mockResponse);
    Assert.assertEquals(expectedBlobDescriptor, blobDescriptor);
  }
  @Test
  public void testHandleResponse_noContentLength() {
    Mockito.when(mockResponse.getContentLength()).thenReturn(-1L);
    try {
      testBlobChecker.handleResponse(mockResponse);
      Assert.fail("Should throw exception if Content-Length header is not present");
    } catch (RegistryErrorException ex) {
      Assert.assertThat(
          ex.getMessage(), CoreMatchers.containsString("Did not receive Content-Length header"));
    }
  }
  @Test
  public void testHandleHttpResponseException() throws IOException, RegistryErrorException {
    HttpResponseException mockHttpResponseException = Mockito.mock(HttpResponseException.class);
    Mockito.when(mockHttpResponseException.getStatusCode())
        .thenReturn(HttpStatusCodes.STATUS_CODE_NOT_FOUND);
    ErrorResponseTemplate emptyErrorResponseTemplate =
        new ErrorResponseTemplate()
            .addError(new ErrorEntryTemplate(ErrorCodes.BLOB_UNKNOWN.name(), "some message"));
    Mockito.when(mockHttpResponseException.getContent())
        .thenReturn(Blobs.writeToString(JsonTemplateMapper.toBlob(emptyErrorResponseTemplate)));
    BlobDescriptor blobDescriptor =
        testBlobChecker.handleHttpResponseException(mockHttpResponseException);
    Assert.assertNull(blobDescriptor);
  }
  @Test
  public void testHandleHttpResponseException_hasOtherErrors()
      throws IOException, RegistryErrorException {
    HttpResponseException mockHttpResponseException = Mockito.mock(HttpResponseException.class);
    Mockito.when(mockHttpResponseException.getStatusCode())
        .thenReturn(HttpStatusCodes.STATUS_CODE_NOT_FOUND);
    ErrorResponseTemplate emptyErrorResponseTemplate =
        new ErrorResponseTemplate()
            .addError(new ErrorEntryTemplate(ErrorCodes.BLOB_UNKNOWN.name(), "some message"))
            .addError(new ErrorEntryTemplate(ErrorCodes.MANIFEST_UNKNOWN.name(), "some message"));
    Mockito.when(mockHttpResponseException.getContent())
        .thenReturn(Blobs.writeToString(JsonTemplateMapper.toBlob(emptyErrorResponseTemplate)));
    try {
      testBlobChecker.handleHttpResponseException(mockHttpResponseException);
      Assert.fail("Non-BLOB_UNKNOWN errors should not be handled");
    } catch (HttpResponseException ex) {
      Assert.assertEquals(mockHttpResponseException, ex);
    }
  }
  @Test
  public void testHandleHttpResponseException_notBlobUnknown()
      throws IOException, RegistryErrorException {
    HttpResponseException mockHttpResponseException = Mockito.mock(HttpResponseException.class);
    Mockito.when(mockHttpResponseException.getStatusCode())
        .thenReturn(HttpStatusCodes.STATUS_CODE_NOT_FOUND);
    ErrorResponseTemplate emptyErrorResponseTemplate = new ErrorResponseTemplate();
    Mockito.when(mockHttpResponseException.getContent())
        .thenReturn(Blobs.writeToString(JsonTemplateMapper.toBlob(emptyErrorResponseTemplate)));
    try {
      testBlobChecker.handleHttpResponseException(mockHttpResponseException);
      Assert.fail("Non-BLOB_UNKNOWN errors should not be handled");
    } catch (HttpResponseException ex) {
      Assert.assertEquals(mockHttpResponseException, ex);
    }
  }
  @Test
  public void testHandleHttpResponseException_invalidStatusCode() throws RegistryErrorException {
    HttpResponseException mockHttpResponseException = Mockito.mock(HttpResponseException.class);
    Mockito.when(mockHttpResponseException.getStatusCode()).thenReturn(-1);
    try {
      testBlobChecker.handleHttpResponseException(mockHttpResponseException);
      Assert.fail("Non-404 status codes should not be handled");
    } catch (HttpResponseException ex) {
      Assert.assertEquals(mockHttpResponseException, ex);
    }
  }
  @Test
  public void testGetApiRoute() throws MalformedURLException {
    Assert.assertEquals(
        new URL("http:
        testBlobChecker.getApiRoute("http:
  }
  @Test
  public void testGetContent() {
    Assert.assertNull(testBlobChecker.getContent());
  }
  @Test
  public void testGetAccept() {
    Assert.assertEquals(0, testBlobChecker.getAccept().size());
  }
  @Test
  public void testGetActionDescription() {
    Assert.assertEquals(
        "check BLOB exists for someServerUrl/someImageName with digest " + fakeDigest,
        testBlobChecker.getActionDescription());
  }
  @Test
  public void testGetHttpMethod() {
    Assert.assertEquals("HEAD", testBlobChecker.getHttpMethod());
  }
}
