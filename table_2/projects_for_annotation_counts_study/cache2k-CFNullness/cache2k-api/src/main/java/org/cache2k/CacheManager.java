package org.cache2k;
public abstract class CacheManager implements Closeable {
  public static final String STANDARD_DEFAULT_MANAGER_NAME = "default";
  public static final Cache2kCoreProvider PROVIDER;
  static {
    Iterator<Cache2kCoreProvider> it = ServiceLoader.load(Cache2kCoreProvider.class).iterator();
    if (!it.hasNext()) {
      throw new LinkageError("Cannot resolve cache2k core implementation");
    }
    PROVIDER = it.next();
  }
  public static String getDefaultName() {
    return PROVIDER.getDefaultManagerName(PROVIDER.getDefaultClassLoader());
  }
  public static void setDefaultName(String managerName) {
    PROVIDER.setDefaultManagerName(PROVIDER.getDefaultClassLoader(), managerName);
  }
  public static CacheManager getInstance() {
    ClassLoader defaultClassLoader = PROVIDER.getDefaultClassLoader();
    return PROVIDER.getManager(
      defaultClassLoader, PROVIDER.getDefaultManagerName(defaultClassLoader));
  }
  public static CacheManager getInstance(ClassLoader cl) {
    return PROVIDER.getManager(cl, PROVIDER.getDefaultManagerName(cl));
  }
  public static CacheManager getInstance(String managerName) {
    return PROVIDER.getManager(PROVIDER.getDefaultClassLoader(), managerName);
  }
  public static CacheManager getInstance(ClassLoader cl, String managerName) {
    return PROVIDER.getManager(cl, managerName);
  }
  public static void closeAll() {
    PROVIDER.close();
  }
  public static void closeAll(ClassLoader cl) {
    PROVIDER.close(cl);
  }
  public static void close(ClassLoader cl, String name) {
    PROVIDER.close(cl, name);
  }
  public abstract boolean isDefaultManager();
  public abstract String getName();
  public abstract Iterable<Cache> getActiveCaches();
  public abstract Iterable<String> getConfiguredCacheNames();
  public abstract <K, V> Cache<K, V> getCache(String name);
  public abstract <K, V> Cache<K, V> createCache(Cache2kConfig<K, V> cfg);
  public abstract void clear();
  public abstract void close();
  public abstract boolean isClosed();
  public abstract Properties getProperties();
  public abstract ClassLoader getClassLoader();
}
