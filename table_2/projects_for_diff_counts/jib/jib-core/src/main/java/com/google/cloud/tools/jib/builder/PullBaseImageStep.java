package com.google.cloud.tools.jib.builder;
class PullBaseImageStep implements Callable<Image> {
  private static final String DESCRIPTION = "Pulling base image manifest";
  private final BuildConfiguration buildConfiguration;
  private final Future<Authorization> pullAuthorizationFuture;
  PullBaseImageStep(
      BuildConfiguration buildConfiguration, Future<Authorization> pullAuthorizationFuture) {
    this.buildConfiguration = buildConfiguration;
    this.pullAuthorizationFuture = pullAuthorizationFuture;
  }
  @Override
  public Image call()
      throws IOException, RegistryException, LayerPropertyNotFoundException,
          LayerCountMismatchException, ExecutionException, InterruptedException {
    try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
      RegistryClient registryClient =
          new RegistryClient(
              NonBlockingFutures.get(pullAuthorizationFuture),
              buildConfiguration.getBaseImageRegistry(),
              buildConfiguration.getBaseImageRepository());
      ManifestTemplate manifestTemplate =
          registryClient.pullManifest(buildConfiguration.getBaseImageTag());
      switch (manifestTemplate.getSchemaVersion()) {
        case 1:
          V21ManifestTemplate v21ManifestTemplate = (V21ManifestTemplate) manifestTemplate;
          return JsonToImageTranslator.toImage(v21ManifestTemplate);
        case 2:
          V22ManifestTemplate v22ManifestTemplate = (V22ManifestTemplate) manifestTemplate;
          if (v22ManifestTemplate.getContainerConfiguration() == null
              || v22ManifestTemplate.getContainerConfiguration().getDigest() == null) {
            throw new UnknownManifestFormatException(
                "Invalid container configuration in Docker V2.2 manifest: \n"
                    + Blobs.writeToString(JsonTemplateMapper.toBlob(v22ManifestTemplate)));
          }
          ByteArrayOutputStream containerConfigurationOutputStream = new ByteArrayOutputStream();
          registryClient.pullBlob(
              v22ManifestTemplate.getContainerConfiguration().getDigest(),
              containerConfigurationOutputStream);
          String containerConfigurationString =
              new String(containerConfigurationOutputStream.toByteArray(), StandardCharsets.UTF_8);
          ContainerConfigurationTemplate containerConfigurationTemplate =
              JsonTemplateMapper.readJson(
                  containerConfigurationString, ContainerConfigurationTemplate.class);
          return JsonToImageTranslator.toImage(v22ManifestTemplate, containerConfigurationTemplate);
      }
      throw new IllegalStateException("Unknown manifest schema version");
    }
  }
}
