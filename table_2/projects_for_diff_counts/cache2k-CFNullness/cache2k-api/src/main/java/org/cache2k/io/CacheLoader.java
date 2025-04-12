package org.cache2k.io;
@FunctionalInterface
public interface CacheLoader<K, V> extends DataAwareCustomization<K, V> {
  V load(K key) throws Exception;
}
