package org.cache2k.config;
public interface CacheBuildContext<K, V> {
  CacheManager getCacheManager();
  String getName();
  Cache2kConfig<K, V> getConfig();
  <T> T createCustomization(CustomizationSupplier<T> supplier);
}
