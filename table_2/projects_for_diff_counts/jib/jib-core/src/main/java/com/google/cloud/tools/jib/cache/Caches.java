package com.google.cloud.tools.jib.cache;
public class Caches implements Closeable {
  public static class Initializer {
    private static final Path DEFAULT_BASE_CACHE_DIRECTORY =
        UserCacheHome.getCacheHome().resolve("google-cloud-tools-java").resolve("jib");
    private static final String OWNERSHIP_FILE_NAME = ".jib";
    @VisibleForTesting
    static void ensureOwnership(Path cacheDirectory)
        throws CacheDirectoryNotOwnedException, IOException {
      Path ownershipFile = cacheDirectory.resolve(OWNERSHIP_FILE_NAME);
      if (Files.exists(cacheDirectory)) {
        if (!Files.exists(ownershipFile)) {
          throw new CacheDirectoryNotOwnedException(cacheDirectory);
        }
      } else {
        Files.createDirectories(cacheDirectory);
        Files.createFile(ownershipFile);
      }
    }
    private final Path applicationCacheDirectory;
    private Path baseCacheDirectory = DEFAULT_BASE_CACHE_DIRECTORY;
    private Initializer(Path applicationCacheDirectory) {
      this.applicationCacheDirectory = applicationCacheDirectory;
    }
    public Initializer setBaseCacheDirectory(Path baseCacheDirectory) {
      this.baseCacheDirectory = baseCacheDirectory;
      return this;
    }
    public Caches init()
        throws CacheMetadataCorruptedException, IOException, CacheDirectoryNotOwnedException {
      if (DEFAULT_BASE_CACHE_DIRECTORY.equals(baseCacheDirectory)) {
        ensureOwnership(DEFAULT_BASE_CACHE_DIRECTORY);
      }
      return new Caches(baseCacheDirectory, applicationCacheDirectory);
    }
  }
  public static Initializer newInitializer(Path applicationCacheDirectory) {
    return new Initializer(applicationCacheDirectory);
  }
  private final Cache baseCache;
  private final Cache applicationCache;
  private Caches(Path baseCacheDirectory, Path applicationCacheDirectory)
      throws CacheMetadataCorruptedException, NotDirectoryException {
    baseCache = Cache.init(baseCacheDirectory);
    applicationCache = Cache.init(applicationCacheDirectory);
  }
  @Override
  public void close() throws IOException {
    baseCache.close();
    applicationCache.close();
  }
  Cache getBaseCache() {
    return baseCache;
  }
  Cache getApplicationCache() {
    return applicationCache;
  }
}
