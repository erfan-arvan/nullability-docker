package org.cache2k.io;
public interface CacheWriter<K, V> extends DataAwareCustomization<K, V> {
  void write(K key, V value) throws Exception;
  void delete(K key) throws Exception;
}
