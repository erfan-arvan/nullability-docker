package com.google.cloud.tools.jib.registry;
class BlobChecker implements RegistryEndpointProvider<BlobDescriptor> {
  private final RegistryEndpointProperties registryEndpointProperties;
  private final DescriptorDigest blobDigest;
  BlobChecker(RegistryEndpointProperties registryEndpointProperties, DescriptorDigest blobDigest) {
    this.registryEndpointProperties = registryEndpointProperties;
    this.blobDigest = blobDigest;
  }
  @Override
  public BlobDescriptor handleResponse(Response response) throws RegistryErrorException {
    long contentLength = response.getContentLength();
    if (contentLength < 0) {
      throw new RegistryErrorExceptionBuilder(getActionDescription())
          .addReason("Did not receive Content-Length header")
          .build();
    }
    return new BlobDescriptor(contentLength, blobDigest);
  }
  @Override
  public BlobDescriptor handleHttpResponseException(HttpResponseException httpResponseException)
      throws RegistryErrorException, HttpResponseException {
    if (httpResponseException.getStatusCode() != HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
      throw httpResponseException;
    }
    String errorContent = httpResponseException.getContent();
    if (errorContent == null) {
      return null;
    } else {
      try {
        ErrorResponseTemplate errorResponse =
            JsonTemplateMapper.readJson(errorContent, ErrorResponseTemplate.class);
        List<ErrorEntryTemplate> errors = errorResponse.getErrors();
        if (errors.size() == 1) {
          String errorCodeString = errors.get(0).getCode();
          if (errorCodeString == null) {
            throw httpResponseException;
          }
          ErrorCodes errorCode = ErrorCodes.valueOf(errorCodeString);
          if (errorCode.equals(ErrorCodes.BLOB_UNKNOWN)) {
            return null;
          }
        }
      } catch (IOException ex) {
        throw new RegistryErrorExceptionBuilder(getActionDescription(), ex)
            .addReason("Failed to parse registry error response body")
            .build();
      }
    }
    throw httpResponseException;
  }
  @Override
  public URL getApiRoute(String apiRouteBase) throws MalformedURLException {
    return new URL(
        apiRouteBase + registryEndpointProperties.getImageName() + "/blobs/" + blobDigest);
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
  public String getHttpMethod() {
    return HttpMethods.HEAD;
  }
  @Override
  public String getActionDescription() {
    return "check BLOB exists for "
        + registryEndpointProperties.getServerUrl()
        + "/"
        + registryEndpointProperties.getImageName()
        + " with digest "
        + blobDigest;
  }
}
