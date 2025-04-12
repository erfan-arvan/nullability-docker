package com.google.cloud.tools.jib.builder;
class PullAndCacheBaseImageLayerStep implements Callable<CachedLayer> {
  private static final String DESCRIPTION = "Pulling base image layer %s";
  private final BuildConfiguration buildConfiguration;
  private final Cache cache;
  private final DescriptorDigest layerDigest;
  private final Future<Authorization> pullAuthorizationFuture;
  PullAndCacheBaseImageLayerStep(
      BuildConfiguration buildConfiguration,
      Cache cache,
      DescriptorDigest layerDigest,
      Future<Authorization> pullAuthorizationFuture) {
    this.buildConfiguration = buildConfiguration;
    this.cache = cache;
    this.layerDigest = layerDigest;
    this.pullAuthorizationFuture = pullAuthorizationFuture;
  }
  @Override
  public CachedLayer call()
      throws IOException, RegistryException, LayerPropertyNotFoundException, ExecutionException,
          InterruptedException {
    try (Timer ignored =
        new Timer(buildConfiguration.getBuildLogger(), String.format(DESCRIPTION, layerDigest))) {
      RegistryClient registryClient =
          new RegistryClient(
              pullAuthorizationFuture.get(),
              buildConfiguration.getBaseImageRegistry(),
              buildConfiguration.getBaseImageRepository());
      CachedLayer cachedLayer = new CacheReader(cache).getLayer(layerDigest);
      if (cachedLayer != null) {
        return cachedLayer;
      }
      CacheWriter cacheWriter = new CacheWriter(cache);
      CountingOutputStream layerOutputStream = cacheWriter.getLayerOutputStream(layerDigest);
      registryClient.pullBlob(layerDigest, layerOutputStream);
      return cacheWriter.getCachedLayer(layerDigest, layerOutputStream);
    }
  }
}
