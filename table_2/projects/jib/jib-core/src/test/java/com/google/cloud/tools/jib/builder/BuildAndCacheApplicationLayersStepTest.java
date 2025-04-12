package com.google.cloud.tools.jib.builder;
@RunWith(MockitoJUnitRunner.class)
public class BuildAndCacheApplicationLayersStepTest {
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @Mock private BuildConfiguration mockBuildConfiguration;
  @Test
  public void testRun()
      throws LayerPropertyNotFoundException, IOException, CacheMetadataCorruptedException,
          URISyntaxException, ExecutionException, InterruptedException {
    Mockito.when(mockBuildConfiguration.getBuildLogger()).thenReturn(new TestBuildLogger());
    TestSourceFilesConfiguration testSourceFilesConfiguration = new TestSourceFilesConfiguration();
    Path temporaryCacheDirectory = temporaryFolder.newFolder().toPath();
    ImageLayers<CachedLayer> applicationLayers = new ImageLayers<>();
    try (Cache cache = Cache.init(temporaryCacheDirectory)) {
      BuildAndCacheApplicationLayersStep buildAndCacheApplicationLayersStep =
          new BuildAndCacheApplicationLayersStep(
              mockBuildConfiguration,
              testSourceFilesConfiguration,
              cache,
              MoreExecutors.newDirectExecutorService());
      for (ListenableFuture<CachedLayer> applicationLayerFuture :
          buildAndCacheApplicationLayersStep.call()) {
        applicationLayers.add(applicationLayerFuture.get());
      }
      Assert.assertEquals(3, applicationLayers.size());
    }
    Cache cache = Cache.init(temporaryCacheDirectory);
    CacheReader cacheReader = new CacheReader(cache);
    Assert.assertEquals(
        applicationLayers.get(0).getBlobDescriptor(),
        cacheReader
            .getUpToDateLayerBySourceFiles(testSourceFilesConfiguration.getDependenciesFiles())
            .getBlobDescriptor());
    Assert.assertEquals(
        applicationLayers.get(1).getBlobDescriptor(),
        cacheReader
            .getUpToDateLayerBySourceFiles(testSourceFilesConfiguration.getResourcesFiles())
            .getBlobDescriptor());
    Assert.assertEquals(
        applicationLayers.get(2).getBlobDescriptor(),
        cacheReader
            .getUpToDateLayerBySourceFiles(testSourceFilesConfiguration.getClassesFiles())
            .getBlobDescriptor());
    Assert.assertEquals(
        applicationLayers.get(0).getContentFile(),
        cacheReader.getLayerFile(testSourceFilesConfiguration.getDependenciesFiles()));
    Assert.assertEquals(
        applicationLayers.get(1).getContentFile(),
        cacheReader.getLayerFile(testSourceFilesConfiguration.getResourcesFiles()));
    Assert.assertEquals(
        applicationLayers.get(2).getContentFile(),
        cacheReader.getLayerFile(testSourceFilesConfiguration.getClassesFiles()));
  }
}
