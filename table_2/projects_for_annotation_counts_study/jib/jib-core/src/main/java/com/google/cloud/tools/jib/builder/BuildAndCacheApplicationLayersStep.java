package com.google.cloud.tools.jib.builder;
class BuildAndCacheApplicationLayersStep implements Callable<List<ListenableFuture<CachedLayer>>> {
  private static final String DESCRIPTION = "Building application layers";
  private final BuildConfiguration buildConfiguration;
  private final SourceFilesConfiguration sourceFilesConfiguration;
  private final Cache cache;
  private final ListeningExecutorService listeningExecutorService;
  BuildAndCacheApplicationLayersStep(
      BuildConfiguration buildConfiguration,
      SourceFilesConfiguration sourceFilesConfiguration,
      Cache cache,
      ListeningExecutorService listeningExecutorService) {
    this.buildConfiguration = buildConfiguration;
    this.sourceFilesConfiguration = sourceFilesConfiguration;
    this.cache = cache;
    this.listeningExecutorService = listeningExecutorService;
  }
  @Override
  public List<ListenableFuture<CachedLayer>> call() {
    try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), DESCRIPTION)) {
      List<ListenableFuture<CachedLayer>> applicationLayerFutures = new ArrayList<>(3);
      applicationLayerFutures.add(
          buildAndCacheLayerAsync(
              "dependencies",
              sourceFilesConfiguration.getDependenciesFiles(),
              sourceFilesConfiguration.getDependenciesPathOnImage()));
      applicationLayerFutures.add(
          buildAndCacheLayerAsync(
              "resources",
              sourceFilesConfiguration.getResourcesFiles(),
              sourceFilesConfiguration.getResourcesPathOnImage()));
      applicationLayerFutures.add(
          buildAndCacheLayerAsync(
              "classes",
              sourceFilesConfiguration.getClassesFiles(),
              sourceFilesConfiguration.getClassesPathOnImage()));
      return applicationLayerFutures;
    }
  }
  private ListenableFuture<CachedLayer> buildAndCacheLayerAsync(
      String layerType, List<Path> sourceFiles, String extractionPath) {
    String description = "Building " + layerType + " layer";
    return listeningExecutorService.submit(
        () -> {
          try (Timer ignored = new Timer(buildConfiguration.getBuildLogger(), description)) {
            CachedLayer cachedLayer =
                new CacheReader(cache).getUpToDateLayerBySourceFiles(sourceFiles);
            if (cachedLayer != null) {
              return cachedLayer;
            }
            LayerBuilder layerBuilder =
                new LayerBuilder(
                    sourceFiles, extractionPath, buildConfiguration.getEnableReproducibleBuilds());
            cachedLayer = new CacheWriter(cache).writeLayer(layerBuilder);
            buildConfiguration
                .getBuildLogger()
                .debug(description + " built " + cachedLayer.getBlobDescriptor().getDigest());
            return cachedLayer;
          }
        });
  }
}
