package com.google.cloud.tools.jib.builder;
class PullAndCacheBaseImageLayersStep implements Callable<List<ListenableFuture<CachedLayer>>> {
  private static final String DESCRIPTION = "Setting up base image caching";
  private final BuildConfiguration buildConfiguration;
  private final Cache cache;
  private final ListeningExecutorService listeningExecutorService;
  private final ListenableFuture<Authorization> pullAuthorizationFuture;
  private final ListenableFuture<Image> baseImageFuture;
  PullAndCacheBaseImageLayersStep(
      BuildConfiguration buildConfiguration,
      Cache cache,
      ListeningExecutorService listeningExecutorService,
      ListenableFuture<Authorization> pullAuthorizationFuture,
      ListenableFuture<Image> baseImageFuture) {
    this.buildConfiguration = buildConfiguration;
    this.cache = cache;
    this.listeningExecutorService = listeningExecutorService;
    this.pullAuthorizationFuture = pullAuthorizationFuture;
    this.baseImageFuture = baseImageFuture;
  }
  @Override
  public List<ListenableFuture<CachedLayer>> call()
      throws ExecutionException, InterruptedException, LayerPropertyNotFoundException {
    try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
      List<ListenableFuture<CachedLayer>> pullAndCacheBaseImageLayerFutures = new ArrayList<>();
      for (Layer layer : NonBlockingFutures.get(baseImageFuture).getLayers()) {
        pullAndCacheBaseImageLayerFutures.add(
            Futures.whenAllSucceed(pullAuthorizationFuture)
                .call(
                    new PullAndCacheBaseImageLayerStep(
                        buildConfiguration,
                        cache,
                        layer.getBlobDescriptor().getDigest(),
                        pullAuthorizationFuture),
                    listeningExecutorService));
      }
      return pullAndCacheBaseImageLayerFutures;
    }
  }
}
