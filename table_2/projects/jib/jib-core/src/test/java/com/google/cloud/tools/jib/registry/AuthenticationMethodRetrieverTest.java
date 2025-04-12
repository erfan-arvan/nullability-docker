package com.google.cloud.tools.jib.registry;
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationMethodRetrieverTest {
  @Mock private HttpResponseException mockHttpResponseException;
  @Mock private HttpHeaders mockHeaders;
  private final RegistryEndpointProperties fakeRegistryEndpointProperties =
      new RegistryEndpointProperties("someServerUrl", "someImageName");
  private final AuthenticationMethodRetriever testAuthenticationMethodRetriever =
      new AuthenticationMethodRetriever(fakeRegistryEndpointProperties);
  @Test
  public void testGetContent() {
    Assert.assertNull(testAuthenticationMethodRetriever.getContent());
  }
  @Test
  public void testGetAccept() {
    Assert.assertEquals(0, testAuthenticationMethodRetriever.getAccept().size());
  }
  @Test
  public void testHandleResponse() {
    Assert.assertNull(
        testAuthenticationMethodRetriever.handleResponse(Mockito.mock(Response.class)));
  }
  @Test
  public void testGetApiRoute() throws MalformedURLException {
    Assert.assertEquals(
        new URL("http:
        testAuthenticationMethodRetriever.getApiRoute("http:
  }
  @Test
  public void testGetHttpMethod() {
    Assert.assertEquals(HttpMethods.GET, testAuthenticationMethodRetriever.getHttpMethod());
  }
  @Test
  public void testGetActionDescription() {
    Assert.assertEquals(
        "retrieve authentication method for someServerUrl",
        testAuthenticationMethodRetriever.getActionDescription());
  }
  @Test
  public void testHandleHttpResponseException_invalidStatusCode() throws RegistryErrorException {
    Mockito.when(mockHttpResponseException.getStatusCode()).thenReturn(-1);
    try {
      testAuthenticationMethodRetriever.handleHttpResponseException(mockHttpResponseException);
      Assert.fail(
          "Authentication method retriever should only handle HTTP 401 Unauthorized errors");
    } catch (HttpResponseException ex) {
      Assert.assertEquals(mockHttpResponseException, ex);
    }
  }
  @Test
  public void tsetHandleHttpResponseException_noHeader() throws HttpResponseException {
    Mockito.when(mockHttpResponseException.getStatusCode())
        .thenReturn(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED);
    Mockito.when(mockHttpResponseException.getHeaders()).thenReturn(mockHeaders);
    Mockito.when(mockHeaders.getAuthenticate()).thenReturn(null);
    try {
      testAuthenticationMethodRetriever.handleHttpResponseException(mockHttpResponseException);
      Assert.fail(
          "Authentication method retriever should fail if 'WWW-Authenticate' header is not found");
    } catch (RegistryErrorException ex) {
      Assert.assertThat(
          ex.getMessage(), CoreMatchers.containsString("'WWW-Authenticate' header not found"));
    }
  }
  @Test
  public void testHandleHttpResponseException_badAuthenticationMethod()
      throws HttpResponseException {
    String authenticationMethod = "bad authentication method";
    Mockito.when(mockHttpResponseException.getStatusCode())
        .thenReturn(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED);
    Mockito.when(mockHttpResponseException.getHeaders()).thenReturn(mockHeaders);
    Mockito.when(mockHeaders.getAuthenticate()).thenReturn(authenticationMethod);
    try {
      testAuthenticationMethodRetriever.handleHttpResponseException(mockHttpResponseException);
      Assert.fail(
          "Authentication method retriever should fail if 'WWW-Authenticate' header failed to parse");
    } catch (RegistryErrorException ex) {
      Assert.assertThat(
          ex.getMessage(),
          CoreMatchers.containsString(
              "Failed get authentication method from 'WWW-Authenticate' header"));
    }
  }
  @Test
  public void testHandleHttpResponseException_pass()
      throws RegistryErrorException, HttpResponseException, MalformedURLException {
    String authenticationMethod =
        "Bearer realm=\"https:
    Mockito.when(mockHttpResponseException.getStatusCode())
        .thenReturn(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED);
    Mockito.when(mockHttpResponseException.getHeaders()).thenReturn(mockHeaders);
    Mockito.when(mockHeaders.getAuthenticate()).thenReturn(authenticationMethod);
    RegistryAuthenticator registryAuthenticator =
        testAuthenticationMethodRetriever.handleHttpResponseException(mockHttpResponseException);
    Assert.assertEquals(
        new URL("https:
        registryAuthenticator.getAuthenticationUrl("someScope"));
  }
}
