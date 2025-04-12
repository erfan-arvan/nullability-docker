package com.google.cloud.tools.jib.registry;
class BlobPusher {
  private final RegistryEndpointProperties registryEndpointProperties;
  private final DescriptorDigest blobDigest;
  private final Blob blob;
  private class Initializer implements RegistryEndpointProvider<String> {
    @Override
    public BlobHttpContent getContent() {
      return null;
    }
    @Override
    public List<String> getAccept() {
      return Collections.emptyList();
    }
    @Override
    public String handleResponse(Response response) throws RegistryErrorException {
      switch (response.getStatusCode()) {
        case HttpStatusCodes.STATUS_CODE_CREATED:
          return null;
        case HttpURLConnection.HTTP_ACCEPTED:
          return extractLocationHeader(response);
        default:
          throw buildRegistryErrorException(
              "Received unrecognized status code " + response.getStatusCode());
      }
    }
    @Override
    public URL getApiRoute(String apiRouteBase) throws MalformedURLException {
      return new URL(
          apiRouteBase
              + registryEndpointProperties.getImageName()
              + "/blobs/uploads/?mount="
              + blobDigest);
    }
    @Override
    public String getHttpMethod() {
      return HttpMethods.POST;
    }
    @Override
    public String getActionDescription() {
      return BlobPusher.this.getActionDescription();
    }
  }
  private class Writer implements RegistryEndpointProvider<String> {
    private final URL location;
    @Override
    public BlobHttpContent getContent() {
      return new BlobHttpContent(blob, MediaType.OCTET_STREAM.toString());
    }
    @Override
    public List<String> getAccept() {
      return Collections.emptyList();
    }
    @Override
    public String handleResponse(Response response) throws RegistryException {
      return extractLocationHeader(response);
    }
    @Override
    public URL getApiRoute(String apiRouteBase) {
      return location;
    }
    @Override
    public String getHttpMethod() {
      return HttpMethods.PATCH;
    }
    @Override
    public String getActionDescription() {
      return BlobPusher.this.getActionDescription();
    }
    private Writer(URL location) {
      this.location = location;
    }
  }
  private class Committer implements RegistryEndpointProvider<Void> {
    private final URL location;
    @Override
    public BlobHttpContent getContent() {
      return null;
    }
    @Override
    public List<String> getAccept() {
      return Collections.emptyList();
    }
    @Override
    public Void handleResponse(Response response) {
      return null;
    }
    @Override
    public URL getApiRoute(String apiRouteBase) {
      return new GenericUrl(location).set("digest", blobDigest).toURL();
    }
    @Override
    public String getHttpMethod() {
      return HttpMethods.PUT;
    }
    @Override
    public String getActionDescription() {
      return BlobPusher.this.getActionDescription();
    }
    private Committer(URL location) {
      this.location = location;
    }
  }
  BlobPusher(
      RegistryEndpointProperties registryEndpointProperties,
      DescriptorDigest blobDigest,
      Blob blob) {
    this.registryEndpointProperties = registryEndpointProperties;
    this.blobDigest = blobDigest;
    this.blob = blob;
  }
  RegistryEndpointProvider<String> initializer() {
    return new Initializer();
  }
  RegistryEndpointProvider<String> writer(URL location) {
    return new Writer(location);
  }
  RegistryEndpointProvider<Void> committer(URL location) {
    return new Committer(location);
  }
  private RegistryErrorException buildRegistryErrorException(String reason) {
    RegistryErrorExceptionBuilder registryErrorExceptionBuilder =
        new RegistryErrorExceptionBuilder(getActionDescription());
    registryErrorExceptionBuilder.addReason(reason);
    return registryErrorExceptionBuilder.build();
  }
  private String getActionDescription() {
    return "push BLOB for "
        + registryEndpointProperties.getServerUrl()
        + "/"
        + registryEndpointProperties.getImageName()
        + " with digest "
        + blobDigest;
  }
  private String extractLocationHeader(Response response) throws RegistryErrorException {
    List<String> locationHeaders = response.getHeader("Location");
    if (locationHeaders.size() != 1) {
      throw buildRegistryErrorException(
          "Expected 1 'Location' header, but found " + locationHeaders.size());
    }
    return locationHeaders.get(0);
  }
}
