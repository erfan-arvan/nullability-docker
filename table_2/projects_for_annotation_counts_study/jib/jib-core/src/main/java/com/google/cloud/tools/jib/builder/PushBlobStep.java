package com.google.cloud.tools.jib.builder;
class PushBlobStep implements Callable<Void> {
  private static final String DESCRIPTION = "Pushing BLOB ";
  private final BuildConfiguration buildConfiguration;
  private final Future<Authorization> pushAuthorizationFuture;
  private final Future<CachedLayer> pullLayerFuture;
  PushBlobStep(
      BuildConfiguration buildConfiguration,
      Future<Authorization> pushAuthorizationFuture,
      Future<CachedLayer> pullLayerFuture) {
    this.buildConfiguration = buildConfiguration;
    this.pushAuthorizationFuture = pushAuthorizationFuture;
    this.pullLayerFuture = pullLayerFuture;
  }
  @Override
  public Void call()
      throws IOException, RegistryException, ExecutionException, InterruptedException {
    CachedLayer layer = pullLayerFuture.get();
    DescriptorDigest layerDigest = layer.getBlobDescriptor().getDigest();
    try (Timer timer = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION + layerDigest)) {
      RegistryClient registryClient =
          new RegistryClient(
                  pushAuthorizationFuture.get(),
                  buildConfiguration.getTargetRegistry(),
                  buildConfiguration.getTargetRepository())
              .setTimer(timer);
      if (registryClient.checkBlob(layerDigest) != null) {
        buildConfiguration
            .getBuildLogger()
            .info("BLOB : " + layerDigest + " already exists on registry");
        return null;
      }
      registryClient.pushBlob(layerDigest, layer.getBlob());
      return null;
    }
  }
}
