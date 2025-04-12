package com.google.cloud.tools.jib.builder;
class BuildAndPushContainerConfigurationStep implements Callable<ListenableFuture<BlobDescriptor>> {
  private static final String DESCRIPTION = "Building container configuration";
  private final BuildConfiguration buildConfiguration;
  private final ListeningExecutorService listeningExecutorService;
  private final ListenableFuture<Authorization> pushAuthorizationFuture;
  private final ListenableFuture<List<ListenableFuture<CachedLayer>>>
      pullBaseImageLayerFuturesFuture;
  private final List<ListenableFuture<CachedLayer>> buildApplicationLayerFutures;
  private final List<String> entrypoint;
  BuildAndPushContainerConfigurationStep(
      BuildConfiguration buildConfiguration,
      ListeningExecutorService listeningExecutorService,
      ListenableFuture<Authorization> pushAuthorizationFuture,
      ListenableFuture<List<ListenableFuture<CachedLayer>>> pullBaseImageLayerFuturesFuture,
      List<ListenableFuture<CachedLayer>> buildApplicationLayerFutures,
      List<String> entrypoint) {
    this.buildConfiguration = buildConfiguration;
    this.listeningExecutorService = listeningExecutorService;
    this.pushAuthorizationFuture = pushAuthorizationFuture;
    this.pullBaseImageLayerFuturesFuture = pullBaseImageLayerFuturesFuture;
    this.buildApplicationLayerFutures = buildApplicationLayerFutures;
    this.entrypoint = entrypoint;
  }
  @Override
  public ListenableFuture<BlobDescriptor> call() throws ExecutionException, InterruptedException {
    List<ListenableFuture<?>> afterBaseImageLayerFuturesFutureDependencies = new ArrayList<>();
    afterBaseImageLayerFuturesFutureDependencies.add(pushAuthorizationFuture);
    afterBaseImageLayerFuturesFutureDependencies.addAll(
        NonBlockingFutures.get(pullBaseImageLayerFuturesFuture));
    afterBaseImageLayerFuturesFutureDependencies.addAll(buildApplicationLayerFutures);
    return Futures.whenAllSucceed(afterBaseImageLayerFuturesFutureDependencies)
        .call(this::afterBaseImageLayerFuturesFuture, listeningExecutorService);
  }
  private BlobDescriptor afterBaseImageLayerFuturesFuture()
      throws ExecutionException, InterruptedException, LayerPropertyNotFoundException, IOException,
          RegistryException {
    try (Timer timer = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
      RegistryClient registryClient =
          new RegistryClient(
                  NonBlockingFutures.get(pushAuthorizationFuture),
                  buildConfiguration.getTargetRegistry(),
                  buildConfiguration.getTargetRepository())
              .setTimer(timer);
      Image image = new Image();
      for (Future<CachedLayer> cachedLayerFuture :
          NonBlockingFutures.get(pullBaseImageLayerFuturesFuture)) {
        image.addLayer(NonBlockingFutures.get(cachedLayerFuture));
      }
      for (Future<CachedLayer> cachedLayerFuture : buildApplicationLayerFutures) {
        image.addLayer(NonBlockingFutures.get(cachedLayerFuture));
      }
      image.setEnvironment(buildConfiguration.getEnvironment());
      image.setEntrypoint(entrypoint);
      ImageToJsonTranslator imageToJsonTranslator = new ImageToJsonTranslator(image);
      Blob containerConfigurationBlob = imageToJsonTranslator.getContainerConfigurationBlob();
      CountingDigestOutputStream digestOutputStream =
          new CountingDigestOutputStream(ByteStreams.nullOutputStream());
      containerConfigurationBlob.writeTo(digestOutputStream);
      BlobDescriptor containerConfigurationBlobDescriptor = digestOutputStream.toBlobDescriptor();
      timer.lap(
          "Pushing container configuration " + containerConfigurationBlobDescriptor.getDigest());
      registryClient.pushBlob(
          containerConfigurationBlobDescriptor.getDigest(), containerConfigurationBlob);
      return containerConfigurationBlobDescriptor;
    }
  }
}
