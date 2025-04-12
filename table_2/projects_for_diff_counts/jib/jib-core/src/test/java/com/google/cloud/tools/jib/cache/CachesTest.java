package com.google.cloud.tools.jib.cache;
public class CachesTest {
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @Test
  public void testInitializer()
      throws CacheMetadataCorruptedException, IOException, CacheDirectoryNotOwnedException {
    Path tempBaseCacheDirectory = temporaryFolder.newFolder().toPath();
    Path tempApplicationCacheDirectory = temporaryFolder.newFolder().toPath();
    try (Caches caches =
        Caches.newInitializer(tempApplicationCacheDirectory)
            .setBaseCacheDirectory(tempBaseCacheDirectory)
            .init()) {
      Assert.assertEquals(tempBaseCacheDirectory, caches.getBaseCache().getCacheDirectory());
      Assert.assertEquals(
          tempApplicationCacheDirectory, caches.getApplicationCache().getCacheDirectory());
    }
    Assert.assertTrue(Files.exists(tempBaseCacheDirectory.resolve(CacheFiles.METADATA_FILENAME)));
    Assert.assertTrue(
        Files.exists(tempApplicationCacheDirectory.resolve(CacheFiles.METADATA_FILENAME)));
  }
  @Test
  public void testEnsureOwnership_notOwned() throws IOException {
    Path cacheDirectory = temporaryFolder.newFolder().toPath();
    try {
      Caches.Initializer.ensureOwnership(cacheDirectory);
      Assert.fail("Expected CacheDirectoryNotOwnedException to be thrown");
    } catch (CacheDirectoryNotOwnedException ex) {
      Assert.assertEquals(cacheDirectory, ex.getCacheDirectory());
    }
  }
  @Test
  public void testEnsureOwnership_create() throws IOException, CacheDirectoryNotOwnedException {
    Path cacheDirectory = temporaryFolder.newFolder().toPath();
    Path nonexistentDirectory = cacheDirectory.resolve("somefolder");
    Caches.Initializer.ensureOwnership(nonexistentDirectory);
  }
}
