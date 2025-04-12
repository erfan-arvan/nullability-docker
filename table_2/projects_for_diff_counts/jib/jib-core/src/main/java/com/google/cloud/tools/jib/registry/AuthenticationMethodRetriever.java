package com.google.cloud.tools.jib.registry;
class AuthenticationMethodRetriever implements RegistryEndpointProvider<RegistryAuthenticator> {
  private final RegistryEndpointProperties registryEndpointProperties;
  @Override
  public BlobHttpContent getContent() {
    return null;
  }
  @Override
  public List<String> getAccept() {
    return Collections.emptyList();
  }
  @Override
  public RegistryAuthenticator handleResponse(Response response) {
    return null;
  }
  @Override
  public URL getApiRoute(String apiRouteBase) throws MalformedURLException {
    return new URL(apiRouteBase);
  }
  @Override
  public String getHttpMethod() {
    return HttpMethods.GET;
  }
  @Override
  public String getActionDescription() {
    return "retrieve authentication method for " + registryEndpointProperties.getServerUrl();
  }
  @Override
  public RegistryAuthenticator handleHttpResponseException(
      HttpResponseException httpResponseException)
      throws HttpResponseException, RegistryErrorException {
    if (httpResponseException.getStatusCode() != HttpStatusCodes.STATUS_CODE_UNAUTHORIZED) {
      throw httpResponseException;
    }
    String authenticationMethod = httpResponseException.getHeaders().getAuthenticate();
    if (authenticationMethod == null) {
      throw new RegistryErrorExceptionBuilder(getActionDescription(), httpResponseException)
          .addReason("'WWW-Authenticate' header not found")
          .build();
    }
    try {
      return RegistryAuthenticator.fromAuthenticationMethod(
          authenticationMethod, registryEndpointProperties.getImageName());
    } catch (RegistryAuthenticationFailedException | MalformedURLException ex) {
      throw new RegistryErrorExceptionBuilder(getActionDescription(), ex)
          .addReason("Failed get authentication method from 'WWW-Authenticate' header")
          .build();
    }
  }
  AuthenticationMethodRetriever(RegistryEndpointProperties registryEndpointProperties) {
    this.registryEndpointProperties = registryEndpointProperties;
  }
}
