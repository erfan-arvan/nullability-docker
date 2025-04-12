package org.cache2k.spi;
public interface Cache2kCoreProvider {
  void setDefaultManagerName(ClassLoader cl, String s);
  String getDefaultManagerName(ClassLoader cl);
  CacheManager getManager(ClassLoader cl, String name);
  ClassLoader getDefaultClassLoader();
  void close();
  void close(ClassLoader l);
  void close(ClassLoader l, String managerName);
  <K, V> Cache<K, V> createCache(CacheManager m, Cache2kConfig<K, V> cfg);
  Cache2kConfig getDefaultConfig(CacheManager m);
  String getVersion();
}
