package org.cache2k.io;
public interface AdvancedCacheLoader<K, V> extends Customization<K, V> {
  V load(K key, long startTime,  CacheEntry<K, V> currentEntry) throws Exception;
}
