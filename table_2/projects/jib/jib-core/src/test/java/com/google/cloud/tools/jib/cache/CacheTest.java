package com.google.cloud.tools.jib.cache;
public class CacheTest {
  @Rule public TemporaryFolder temporaryCacheDirectory = new TemporaryFolder();
  @Test
  public void testInit_empty() throws IOException, CacheMetadataCorruptedException {
    Path cacheDirectory = temporaryCacheDirectory.newFolder().toPath();
    Cache cache = Cache.init(cacheDirectory);
    Assert.assertEquals(0, cache.getMetadata().getLayers().getLayers().size());
  }
  @Test
  public void testInit_notDirectory() throws CacheMetadataCorruptedException, IOException {
    Path tempFile = temporaryCacheDirectory.newFile().toPath();
    try {
      Cache.init(tempFile);
      Assert.fail("Cache should not be able to initialize on non-directory");
    } catch (NotDirectoryException ex) {
      Assert.assertEquals("The cache can only write to a directory", ex.getMessage());
    }
  }
  @Test
  public void testInit_withMetadata()
      throws URISyntaxException, IOException, CacheMetadataCorruptedException {
    Path cacheDirectory = temporaryCacheDirectory.newFolder().toPath();
    Path resourceMetadataJsonPath =
        Paths.get(getClass().getClassLoader().getResource("json/metadata.json").toURI());
    Path testMetadataJsonPath = cacheDirectory.resolve(CacheFiles.METADATA_FILENAME);
    Files.copy(resourceMetadataJsonPath, testMetadataJsonPath);
    try (Cache cache = Cache.init(cacheDirectory)) {
      Assert.assertEquals(2, cache.getMetadata().getLayers().getLayers().size());
    }
    Assert.assertArrayEquals(
        Files.readAllBytes(resourceMetadataJsonPath), Files.readAllBytes(testMetadataJsonPath));
  }
}
