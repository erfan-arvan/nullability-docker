package com.google.cloud.tools.jib.builder;
class PushLayersStep implements Callable<List<ListenableFuture<Void>>> {
  private static final String DESCRIPTION = "Setting up to push layers";
  private final BuildConfiguration buildConfiguration;
  private final ListeningExecutorService listeningExecutorService;
  private final ListenableFuture<Authorization> pushAuthorizationFuture;
  private final ListenableFuture<List<ListenableFuture<CachedLayer>>> cachedLayerFuturesFuture;
  PushLayersStep(
      BuildConfiguration buildConfiguration,
      ListeningExecutorService listeningExecutorService,
      ListenableFuture<Authorization> pushAuthorizationFuture,
      ListenableFuture<List<ListenableFuture<CachedLayer>>> cachedLayerFuturesFuture) {
    this.buildConfiguration = buildConfiguration;
    this.listeningExecutorService = listeningExecutorService;
    this.pushAuthorizationFuture = pushAuthorizationFuture;
    this.cachedLayerFuturesFuture = cachedLayerFuturesFuture;
  }
  @Override
  public List<ListenableFuture<Void>> call() throws ExecutionException, InterruptedException {
    try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
      List<ListenableFuture<Void>> pushLayerFutures = new ArrayList<>();
      for (ListenableFuture<CachedLayer> cachedLayerFuture :
          NonBlockingFutures.get(cachedLayerFuturesFuture)) {
        pushLayerFutures.add(
            Futures.whenAllComplete(pushAuthorizationFuture, cachedLayerFuture)
                .call(
                    new PushBlobStep(
                        buildConfiguration, pushAuthorizationFuture, cachedLayerFuture),
                    listeningExecutorService));
      }
      return pushLayerFutures;
    }
  }
}
