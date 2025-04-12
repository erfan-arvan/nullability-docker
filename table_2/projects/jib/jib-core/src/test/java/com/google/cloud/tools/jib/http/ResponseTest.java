package com.google.cloud.tools.jib.http;
@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {
  @Mock private HttpResponse httpResponseMock;
  @Test
  public void testGetContent() throws IOException {
    String expectedResponse = "crepecake\nis\ngood!";
    ByteArrayInputStream responseInputStream =
        new ByteArrayInputStream(expectedResponse.getBytes(StandardCharsets.UTF_8));
    Mockito.when(httpResponseMock.getContent()).thenReturn(responseInputStream);
    Response response = new Response(httpResponseMock);
    Blob responseStream = response.getBody();
    ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
    responseStream.writeTo(responseOutputStream);
    Assert.assertEquals(
        expectedResponse, new String(responseOutputStream.toByteArray(), StandardCharsets.UTF_8));
  }
}
