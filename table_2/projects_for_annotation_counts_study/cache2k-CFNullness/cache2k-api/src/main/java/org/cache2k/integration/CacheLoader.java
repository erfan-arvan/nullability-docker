package org.cache2k.integration;
@Deprecated
public abstract class CacheLoader<K, V> implements FunctionalCacheLoader<K, V> {
  public abstract V load(K key) throws Exception;
  public Map<K, V> loadAll(Iterable<? extends K> keys, Executor executor) throws Exception {
    throw new UnsupportedOperationException();
  }
}
