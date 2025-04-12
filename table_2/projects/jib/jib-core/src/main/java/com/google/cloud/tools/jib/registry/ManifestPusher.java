package com.google.cloud.tools.jib.registry;
class ManifestPusher implements RegistryEndpointProvider<Void> {
  private final RegistryEndpointProperties registryEndpointProperties;
  private final BuildableManifestTemplate manifestTemplate;
  private final String imageTag;
  ManifestPusher(
      RegistryEndpointProperties registryEndpointProperties,
      BuildableManifestTemplate manifestTemplate,
      String imageTag) {
    this.registryEndpointProperties = registryEndpointProperties;
    this.manifestTemplate = manifestTemplate;
    this.imageTag = imageTag;
  }
  @Override
  public BlobHttpContent getContent() {
    return new BlobHttpContent(
        JsonTemplateMapper.toBlob(manifestTemplate), manifestTemplate.getManifestMediaType());
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
  public URL getApiRoute(String apiRouteBase) throws MalformedURLException {
    return new URL(
        apiRouteBase + registryEndpointProperties.getImageName() + "/manifests/" + imageTag);
  }
  @Override
  public String getHttpMethod() {
    return HttpMethods.PUT;
  }
  @Override
  public String getActionDescription() {
    return "push image manifest for "
        + registryEndpointProperties.getServerUrl()
        + "/"
        + registryEndpointProperties.getImageName()
        + ":"
        + imageTag;
  }
}
