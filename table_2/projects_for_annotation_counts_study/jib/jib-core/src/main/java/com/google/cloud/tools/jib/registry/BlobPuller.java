package com.google.cloud.tools.jib.registry;
class BlobPuller implements RegistryEndpointProvider<Void> {
  private final RegistryEndpointProperties registryEndpointProperties;
  private final DescriptorDigest blobDigest;
  private final OutputStream destinationOutputStream;
  BlobPuller(
      RegistryEndpointProperties registryEndpointProperties,
      DescriptorDigest blobDigest,
      OutputStream destinationOutputStream) {
    this.registryEndpointProperties = registryEndpointProperties;
    this.blobDigest = blobDigest;
    this.destinationOutputStream = destinationOutputStream;
  }
  @Override
  public Void handleResponse(Response response) throws IOException, UnexpectedBlobDigestException {
    try (OutputStream outputStream = destinationOutputStream) {
      BlobDescriptor receivedBlobDescriptor = response.getBody().writeTo(outputStream);
      if (!blobDigest.equals(receivedBlobDescriptor.getDigest())) {
        throw new UnexpectedBlobDigestException(
            "The pulled BLOB has digest '"
                + receivedBlobDescriptor.getDigest()
                + "', but the request digest was '"
                + blobDigest
                + "'");
      }
    }
    return null;
  }
  @Override
  public BlobHttpContent getContent() {
    return null;
  }
  @Override
  public List<String> getAccept() {
    return Collections.emptyList();
  }
  @Override
  public URL getApiRoute(String apiRouteBase) throws MalformedURLException {
    return new URL(
        apiRouteBase + registryEndpointProperties.getImageName() + "/blobs/" + blobDigest);
  }
  @Override
  public String getHttpMethod() {
    return HttpMethods.GET;
  }
  @Override
  public String getActionDescription() {
    return "pull BLOB for "
        + registryEndpointProperties.getServerUrl()
        + "/"
        + registryEndpointProperties.getImageName()
        + " with digest "
        + blobDigest;
  }
}
