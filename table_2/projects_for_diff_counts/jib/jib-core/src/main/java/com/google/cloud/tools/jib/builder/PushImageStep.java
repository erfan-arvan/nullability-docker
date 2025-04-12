package com.google.cloud.tools.jib.builder;
class PushImageStep implements Callable<Void> {
  private static final String DESCRIPTION = "Pushing new image";
  private final BuildConfiguration buildConfiguration;
  private final ListeningExecutorService listeningExecutorService;
  private final ListenableFuture<Authorization> pushAuthorizationFuture;
  private final ListenableFuture<List<ListenableFuture<CachedLayer>>>
      pullBaseImageLayerFuturesFuture;
  private final List<ListenableFuture<CachedLayer>> buildApplicationLayerFutures;
  private final ListenableFuture<List<ListenableFuture<Void>>> pushBaseImageLayerFuturesFuture;
  private final List<ListenableFuture<Void>> pushApplicationLayerFutures;
  private final ListenableFuture<ListenableFuture<BlobDescriptor>>
      containerConfigurationBlobDescriptorFutureFuture;
  PushImageStep(
      BuildConfiguration buildConfiguration,
      ListeningExecutorService listeningExecutorService,
      ListenableFuture<Authorization> pushAuthorizationFuture,
      ListenableFuture<List<ListenableFuture<CachedLayer>>> pullBaseImageLayerFuturesFuture,
      List<ListenableFuture<CachedLayer>> buildApplicationLayerFutures,
      ListenableFuture<List<ListenableFuture<Void>>> pushBaseImageLayerFuturesFuture,
      List<ListenableFuture<Void>> pushApplicationLayerFutures,
      ListenableFuture<ListenableFuture<BlobDescriptor>>
          containerConfigurationBlobDescriptorFutureFuture) {
    this.buildConfiguration = buildConfiguration;
    this.listeningExecutorService = listeningExecutorService;
    this.pushAuthorizationFuture = pushAuthorizationFuture;
    this.pullBaseImageLayerFuturesFuture = pullBaseImageLayerFuturesFuture;
    this.buildApplicationLayerFutures = buildApplicationLayerFutures;
    this.pushBaseImageLayerFuturesFuture = pushBaseImageLayerFuturesFuture;
    this.pushApplicationLayerFutures = pushApplicationLayerFutures;
    this.containerConfigurationBlobDescriptorFutureFuture =
        containerConfigurationBlobDescriptorFutureFuture;
  }
  @Override
  public Void call() throws ExecutionException, InterruptedException {
    List<ListenableFuture<?>> dependencies = new ArrayList<>();
    dependencies.add(pushAuthorizationFuture);
    dependencies.addAll(NonBlockingFutures.get(pushBaseImageLayerFuturesFuture));
    dependencies.addAll(pushApplicationLayerFutures);
    dependencies.add(NonBlockingFutures.get(containerConfigurationBlobDescriptorFutureFuture));
    return Futures.whenAllComplete(dependencies)
        .call(this::afterPushBaseImageLayerFuturesFuture, listeningExecutorService)
        .get();
  }
  private Void afterPushBaseImageLayerFuturesFuture()
      throws IOException, RegistryException, ExecutionException, InterruptedException,
          LayerPropertyNotFoundException {
    try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
      RegistryClient registryClient =
          new RegistryClient(
              NonBlockingFutures.get(pushAuthorizationFuture),
              buildConfiguration.getTargetRegistry(),
              buildConfiguration.getTargetRepository());
      Image image = new Image();
      for (Future<CachedLayer> cachedLayerFuture :
          NonBlockingFutures.get(pullBaseImageLayerFuturesFuture)) {
        image.addLayer(NonBlockingFutures.get(cachedLayerFuture));
      }
      for (Future<CachedLayer> cachedLayerFuture : buildApplicationLayerFutures) {
        image.addLayer(NonBlockingFutures.get(cachedLayerFuture));
      }
      ImageToJsonTranslator imageToJsonTranslator = new ImageToJsonTranslator(image);
      BuildableManifestTemplate manifestTemplate =
          imageToJsonTranslator.getManifestTemplate(
              buildConfiguration.getTargetFormat(),
              NonBlockingFutures.get(
                  NonBlockingFutures.get(containerConfigurationBlobDescriptorFutureFuture)));
      registryClient.pushManifest(manifestTemplate, buildConfiguration.getTargetTag());
    }
    return null;
  }
}
