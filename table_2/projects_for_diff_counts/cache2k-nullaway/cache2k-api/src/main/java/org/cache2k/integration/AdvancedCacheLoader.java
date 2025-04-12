package org.cache2k.integration;
@Deprecated
public abstract class AdvancedCacheLoader<K, V>
  implements org.cache2k.io.AdvancedCacheLoader<K, V> {
  @Override
  public abstract V load(K key, long startTime,  CacheEntry<K, V> currentEntry) throws Exception;
}
