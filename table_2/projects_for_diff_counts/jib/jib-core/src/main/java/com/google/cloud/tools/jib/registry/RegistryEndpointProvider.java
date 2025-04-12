package com.google.cloud.tools.jib.registry;
interface RegistryEndpointProvider<T> {
  String getHttpMethod();
  URL getApiRoute(String apiRouteBase) throws MalformedURLException;
  BlobHttpContent getContent();
  List<String> getAccept();
  T handleResponse(Response response) throws IOException, RegistryException;
  default T handleHttpResponseException(HttpResponseException httpResponseException)
      throws HttpResponseException, RegistryErrorException {
    throw httpResponseException;
  }
  String getActionDescription();
}
