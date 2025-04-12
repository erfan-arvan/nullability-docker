package com.google.cloud.tools.jib.registry;
class ManifestPuller<T extends ManifestTemplate> implements RegistryEndpointProvider<T> {
  private final RegistryEndpointProperties registryEndpointProperties;
  private final String imageTag;
  private final Class<T> manifestTemplateClass;
  ManifestPuller(
      RegistryEndpointProperties registryEndpointProperties,
      String imageTag,
      Class<T> manifestTemplateClass) {
    this.registryEndpointProperties = registryEndpointProperties;
    this.imageTag = imageTag;
    this.manifestTemplateClass = manifestTemplateClass;
  }
  @Override
  public BlobHttpContent getContent() {
    return null;
  }
  @Override
  public List<String> getAccept() {
    if (manifestTemplateClass.equals(V21ManifestTemplate.class)) {
      return Collections.singletonList(V21ManifestTemplate.MEDIA_TYPE);
    }
    if (manifestTemplateClass.equals(V22ManifestTemplate.class)) {
      return Collections.singletonList(V22ManifestTemplate.MANIFEST_MEDIA_TYPE);
    }
    if (manifestTemplateClass.equals(OCIManifestTemplate.class)) {
      return Collections.singletonList(OCIManifestTemplate.MANIFEST_MEDIA_TYPE);
    }
    return Arrays.asList(
        OCIManifestTemplate.MANIFEST_MEDIA_TYPE,
        V22ManifestTemplate.MANIFEST_MEDIA_TYPE,
        V21ManifestTemplate.MEDIA_TYPE);
  }
  @Override
  public T handleResponse(Response response) throws IOException, UnknownManifestFormatException {
    return getManifestTemplateFromJson(Blobs.writeToString(response.getBody()));
  }
  @Override
  public URL getApiRoute(String apiRouteBase) throws MalformedURLException {
    return new URL(
        apiRouteBase + registryEndpointProperties.getImageName() + "/manifests/" + imageTag);
  }
  @Override
  public String getHttpMethod() {
    return HttpMethods.GET;
  }
  @Override
  public String getActionDescription() {
    return "pull image manifest for "
        + registryEndpointProperties.getServerUrl()
        + "/"
        + registryEndpointProperties.getImageName()
        + ":"
        + imageTag;
  }
  private T getManifestTemplateFromJson(String jsonString)
      throws IOException, UnknownManifestFormatException {
    ObjectNode node = new ObjectMapper().readValue(jsonString, ObjectNode.class);
    if (!node.has("schemaVersion")) {
      throw new UnknownManifestFormatException("Cannot find field 'schemaVersion' in manifest");
    }
    if (!manifestTemplateClass.equals(ManifestTemplate.class)) {
      return JsonTemplateMapper.readJson(jsonString, manifestTemplateClass);
    }
    int schemaVersion = node.get("schemaVersion").asInt(-1);
    if (schemaVersion == -1) {
      throw new UnknownManifestFormatException("`schemaVersion` field is not an integer");
    }
    if (schemaVersion == 1) {
      return manifestTemplateClass.cast(
          JsonTemplateMapper.readJson(jsonString, V21ManifestTemplate.class));
    }
    if (schemaVersion == 2) {
      String mediaType = node.get("mediaType").asText();
      if (V22ManifestTemplate.MANIFEST_MEDIA_TYPE.equals(mediaType)) {
        return manifestTemplateClass.cast(
            JsonTemplateMapper.readJson(jsonString, V22ManifestTemplate.class));
      }
      if (OCIManifestTemplate.MANIFEST_MEDIA_TYPE.equals(mediaType)) {
        return manifestTemplateClass.cast(
            JsonTemplateMapper.readJson(jsonString, OCIManifestTemplate.class));
      }
      throw new UnknownManifestFormatException("Unknown mediaType: " + mediaType);
    }
    throw new UnknownManifestFormatException(
        "Unknown schemaVersion: " + schemaVersion + " - only 1 and 2 are supported");
  }
}
