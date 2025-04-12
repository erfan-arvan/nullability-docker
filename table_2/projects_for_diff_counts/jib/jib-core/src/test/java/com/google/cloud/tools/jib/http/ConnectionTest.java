package com.google.cloud.tools.jib.http;
@RunWith(MockitoJUnitRunner.class)
public class ConnectionTest {
  @Mock private HttpRequestFactory mockHttpRequestFactory;
  @Mock private HttpRequest mockHttpRequest;
  @Mock private HttpResponse mockHttpResponse;
  private final ArgumentCaptor<HttpHeaders> httpHeadersArgumentCaptor =
      ArgumentCaptor.forClass(HttpHeaders.class);
  private final ArgumentCaptor<BlobHttpContent> blobHttpContentArgumentCaptor =
      ArgumentCaptor.forClass(BlobHttpContent.class);
  private final GenericUrl fakeUrl = new GenericUrl("http:
  private Request fakeRequest;
  @InjectMocks private final Connection testConnection = new Connection(fakeUrl.toURL());
  @Before
  public void setUpMocksAndFakes() throws IOException {
    fakeRequest =
        Request.builder()
            .setAccept(Arrays.asList("fake.accept", "another.fake.accept"))
            .setUserAgent("fake user agent")
            .setBody(new BlobHttpContent(Blobs.from("crepecake"), "fake.content.type"))
            .setAuthorization(Authorizations.withBasicCredentials("fake-username", "fake-secret"))
            .build();
    Mockito.when(
            mockHttpRequestFactory.buildRequest(
                Mockito.any(String.class), Mockito.eq(fakeUrl), Mockito.any(BlobHttpContent.class)))
        .thenReturn(mockHttpRequest);
    Mockito.when(mockHttpRequest.setHeaders(Mockito.any(HttpHeaders.class)))
        .thenReturn(mockHttpRequest);
    Mockito.when(mockHttpRequest.execute()).thenReturn(mockHttpResponse);
  }
  @Test
  public void testGet() throws IOException {
    testSend(HttpMethods.GET, Connection::get);
  }
  @Test
  public void testPost() throws IOException {
    testSend(HttpMethods.POST, Connection::post);
  }
  @Test
  public void testPut() throws IOException {
    testSend(HttpMethods.PUT, Connection::put);
  }
  @FunctionalInterface
  private interface SendFunction {
    Response send(Connection connection, Request request) throws IOException;
  }
  private void testSend(String httpMethod, SendFunction sendFunction) throws IOException {
    try (Connection connection = testConnection) {
      sendFunction.send(connection, fakeRequest);
    }
    Mockito.verify(mockHttpRequest).setHeaders(httpHeadersArgumentCaptor.capture());
    Mockito.verify(mockHttpResponse).disconnect();
    Assert.assertEquals(
        "fake.accept,another.fake.accept", httpHeadersArgumentCaptor.getValue().getAccept());
    Assert.assertEquals("fake user agent", httpHeadersArgumentCaptor.getValue().getUserAgent());
    Assert.assertEquals(
        "Basic ZmFrZS11c2VybmFtZTpmYWtlLXNlY3JldA==",
        httpHeadersArgumentCaptor.getValue().getAuthorization());
    Mockito.verify(mockHttpRequestFactory)
        .buildRequest(
            Mockito.eq(httpMethod), Mockito.eq(fakeUrl), blobHttpContentArgumentCaptor.capture());
    Assert.assertEquals("fake.content.type", blobHttpContentArgumentCaptor.getValue().getType());
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    blobHttpContentArgumentCaptor.getValue().writeTo(byteArrayOutputStream);
    Assert.assertEquals(
        "crepecake", new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
  }
}
