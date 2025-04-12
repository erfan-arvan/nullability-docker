package org.cache2k.integration;
@Deprecated
public interface FunctionalCacheLoader<K, V> extends org.cache2k.io.CacheLoader<K, V> {
  V load(K key) throws Exception;
}
