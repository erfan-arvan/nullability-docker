package com.google.cloud.tools.jib.builder;
public class BuildImageSteps {
  private static final String DESCRIPTION = "Building and pushing image";
  private final BuildConfiguration buildConfiguration;
  private final SourceFilesConfiguration sourceFilesConfiguration;
  private final Path cacheDirectory;
  public BuildImageSteps(
      BuildConfiguration buildConfiguration,
      SourceFilesConfiguration sourceFilesConfiguration,
      Path cacheDirectory) {
    this.buildConfiguration = buildConfiguration;
    this.sourceFilesConfiguration = sourceFilesConfiguration;
    this.cacheDirectory = cacheDirectory;
  }
  public BuildConfiguration getBuildConfiguration() {
    return buildConfiguration;
  }
  public void run()
      throws InterruptedException, ExecutionException, CacheMetadataCorruptedException,
          IOException {
    List<String> entrypoint =
        EntrypointBuilder.makeEntrypoint(
            sourceFilesConfiguration,
            buildConfiguration.getJvmFlags(),
            buildConfiguration.getMainClass());
    try (Timer timer = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
      try (Timer timer2 = timer.subTimer("Initializing cache")) {
        ListeningExecutorService listeningExecutorService =
            MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        try (Cache cache = Cache.init(cacheDirectory)) {
          timer2.lap("Setting up credential retrieval");
          ListenableFuture<Authorization> retrieveTargetRegistryCredentialsFuture =
              listeningExecutorService.submit(
                  new RetrieveRegistryCredentialsStep(
                      buildConfiguration, buildConfiguration.getTargetRegistry()));
          ListenableFuture<Authorization> retrieveBaseImageRegistryCredentialsFuture =
              listeningExecutorService.submit(
                  new RetrieveRegistryCredentialsStep(
                      buildConfiguration, buildConfiguration.getBaseImageRegistry()));
          timer2.lap("Setting up image push authentication");
          ListenableFuture<Authorization> authenticatePushFuture =
              Futures.whenAllSucceed(retrieveTargetRegistryCredentialsFuture)
                  .call(
                      new AuthenticatePushStep(
                          buildConfiguration, retrieveTargetRegistryCredentialsFuture),
                      listeningExecutorService);
          timer2.lap("Setting up image pull authentication");
          ListenableFuture<Authorization> authenticatePullFuture =
              Futures.whenAllSucceed(retrieveBaseImageRegistryCredentialsFuture)
                  .call(
                      new AuthenticatePullStep(
                          buildConfiguration, retrieveBaseImageRegistryCredentialsFuture),
                      listeningExecutorService);
          timer2.lap("Setting up base image pull");
          ListenableFuture<Image> pullBaseImageFuture =
              Futures.whenAllSucceed(authenticatePullFuture)
                  .call(
                      new PullBaseImageStep(buildConfiguration, authenticatePullFuture),
                      listeningExecutorService);
          timer2.lap("Setting up base image layer pull");
          ListenableFuture<List<ListenableFuture<CachedLayer>>> pullBaseImageLayerFuturesFuture =
              Futures.whenAllSucceed(pullBaseImageFuture)
                  .call(
                      new PullAndCacheBaseImageLayersStep(
                          buildConfiguration,
                          cache,
                          listeningExecutorService,
                          authenticatePullFuture,
                          pullBaseImageFuture),
                      listeningExecutorService);
          timer2.lap("Setting up base image layer push");
          ListenableFuture<List<ListenableFuture<Void>>> pushBaseImageLayerFuturesFuture =
              Futures.whenAllSucceed(pullBaseImageLayerFuturesFuture)
                  .call(
                      new PushLayersStep(
                          buildConfiguration,
                          listeningExecutorService,
                          authenticatePushFuture,
                          pullBaseImageLayerFuturesFuture),
                      listeningExecutorService);
          timer2.lap("Setting up build application layers");
          List<ListenableFuture<CachedLayer>> buildAndCacheApplicationLayerFutures =
              new BuildAndCacheApplicationLayersStep(
                      buildConfiguration, sourceFilesConfiguration, cache, listeningExecutorService)
                  .call();
          timer2.lap("Setting up container configuration push");
          ListenableFuture<ListenableFuture<BlobDescriptor>>
              buildAndPushContainerConfigurationFutureFuture =
                  Futures.whenAllSucceed(pullBaseImageLayerFuturesFuture)
                      .call(
                          new BuildAndPushContainerConfigurationStep(
                              buildConfiguration,
                              listeningExecutorService,
                              authenticatePushFuture,
                              pullBaseImageLayerFuturesFuture,
                              buildAndCacheApplicationLayerFutures,
                              entrypoint),
                          listeningExecutorService);
          timer2.lap("Setting up application layer push");
          List<ListenableFuture<Void>> pushApplicationLayersFuture =
              new PushLayersStep(
                      buildConfiguration,
                      listeningExecutorService,
                      authenticatePushFuture,
                      Futures.immediateFuture(buildAndCacheApplicationLayerFutures))
                  .call();
          timer2.lap("Setting up image manifest push");
          ListenableFuture<Void> pushImageFuture =
              Futures.whenAllSucceed(
                      pushBaseImageLayerFuturesFuture,
                      buildAndPushContainerConfigurationFutureFuture)
                  .call(
                      new PushImageStep(
                          buildConfiguration,
                          listeningExecutorService,
                          authenticatePushFuture,
                          pullBaseImageLayerFuturesFuture,
                          buildAndCacheApplicationLayerFutures,
                          pushBaseImageLayerFuturesFuture,
                          pushApplicationLayersFuture,
                          buildAndPushContainerConfigurationFutureFuture),
                      listeningExecutorService);
          timer2.lap("Running push new image");
          pushImageFuture.get();
        }
      }
    }
    buildConfiguration.getBuildLogger().info("");
    buildConfiguration.getBuildLogger().info("Container entrypoint set to " + entrypoint);
  }
}
