package com.google.cloud.tools.jib.cache;
public class CacheReaderTest {
  private static void copyDirectory(Path source, Path destination) throws IOException {
    try (Stream<Path> fileStream = Files.walk(source)) {
      fileStream.forEach(
          path -> {
            try {
              if (path.equals(source)) {
                return;
              }
              Path newPath = destination.resolve(source.relativize(path));
              Files.copy(path, newPath);
            } catch (IOException ex) {
              throw new UncheckedIOException(ex);
            }
          });
    }
  }
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private Path testCacheFolder;
  @Before
  public void setUp() throws IOException, URISyntaxException {
    testCacheFolder = temporaryFolder.newFolder().toPath();
    Path resourceCache = Paths.get(Resources.getResource("cache").toURI());
    copyDirectory(resourceCache, testCacheFolder);
  }
  @Test
  public void testAreBaseImageLayersCached()
      throws DigestException, LayerPropertyNotFoundException, CacheMetadataCorruptedException,
          IOException {
    ImageLayers<ReferenceLayer> layers = new ImageLayers<>();
    layers
        .add(
            new ReferenceLayer(
                new BlobDescriptor(
                    1000,
                    DescriptorDigest.fromDigest(
                        "sha256:5f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef")),
                DescriptorDigest.fromDigest(
                    "sha256:b56ae66c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a4647")))
        .add(
            new ReferenceLayer(
                new BlobDescriptor(
                    1001,
                    DescriptorDigest.fromDigest(
                        "sha256:6f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef")),
                DescriptorDigest.fromDigest(
                    "sha256:b56ae66c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a4647")))
        .add(
            new ReferenceLayer(
                new BlobDescriptor(
                    2000,
                    DescriptorDigest.fromDigest(
                        "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad")),
                DescriptorDigest.fromDigest(
                    "sha256:a3f3e99c29370df48e7377c8f9baa744a3958058a766793f821dadcb144a8372")));
    try (Cache cache = Cache.init(testCacheFolder)) {
      CacheReader cacheReader = new CacheReader(cache);
      Assert.assertNotNull(
          cacheReader.getLayer(
              DescriptorDigest.fromDigest(
                  "sha256:5f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef")));
      Assert.assertNull(
          cacheReader.getLayer(
              DescriptorDigest.fromDigest(
                  "sha256:6f70bf18a086007016e948b04aed3b82103a36bea41755b6cddfaf10ace3c6ef")));
      Assert.assertNotNull(
          cacheReader.getLayer(
              DescriptorDigest.fromDigest(
                  "sha256:8c662931926fa990b41da3c9f42663a537ccd498130030f9149173a0493832ad")));
    }
  }
  @Test
  public void testGetLayerFile() throws CacheMetadataCorruptedException, IOException {
    Path expectedFile =
        testCacheFolder.resolve(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.tar.gz");
    try (Cache cache = Cache.init(testCacheFolder)) {
      CacheReader cacheReader = new CacheReader(cache);
      Assert.assertEquals(
          expectedFile,
          cacheReader.getLayerFile(
              Collections.singletonList(Paths.get("some", "source", "directory"))));
      Assert.assertNull(cacheReader.getLayerFile(Collections.emptyList()));
    }
  }
  @Test
  public void testGetUpToDateLayerBySourceFiles()
      throws URISyntaxException, IOException, CacheMetadataCorruptedException {
    FileTime olderLastModifiedTime = FileTime.fromMillis(1000);
    FileTime newerLastModifiedTime = FileTime.fromMillis(2000);
    Path resourceSourceFiles = Paths.get(Resources.getResource("layer").toURI());
    Path testSourceFiles = temporaryFolder.newFolder().toPath();
    copyDirectory(resourceSourceFiles, testSourceFiles);
    try (Stream<Path> fileStream = Files.walk(testSourceFiles)) {
      fileStream
          .sorted(Comparator.reverseOrder())
          .forEach(
              path -> {
                try {
                  Files.setLastModifiedTime(path, olderLastModifiedTime);
                } catch (IOException ex) {
                  throw new UncheckedIOException(ex);
                }
              });
    }
    CachedLayerWithMetadata classesCachedLayer;
    try (Cache cache = Cache.init(testCacheFolder)) {
      ImageLayers<CachedLayerWithMetadata> cachedLayers =
          cache.getMetadata().filterLayers().filter();
      Assert.assertEquals(3, cachedLayers.size());
      classesCachedLayer = cachedLayers.get(2);
      classesCachedLayer
          .getMetadata()
          .setSourceFiles(Collections.singletonList(testSourceFiles.toString()));
    }
    try (Cache cache = Cache.init(testCacheFolder)) {
      CacheReader cacheReader = new CacheReader(cache);
      Assert.assertEquals(
          classesCachedLayer.getBlobDescriptor(),
          cacheReader
              .getUpToDateLayerBySourceFiles(Collections.singletonList(testSourceFiles))
              .getBlobDescriptor());
      Files.setLastModifiedTime(
          testSourceFiles.resolve("a").resolve("b").resolve("bar"), newerLastModifiedTime);
      Assert.assertNull(
          cacheReader.getUpToDateLayerBySourceFiles(Collections.singletonList(testSourceFiles)));
      Assert.assertNull(
          cacheReader.getUpToDateLayerBySourceFiles(
              Collections.singletonList(resourceSourceFiles)));
    }
  }
}
