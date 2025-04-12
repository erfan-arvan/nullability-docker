package org.cache2k.integration;
@Deprecated
public abstract class CacheWriter<K, V> implements org.cache2k.io.CacheWriter<K, V> {
  public abstract void write(K key, V value) throws Exception;
  public abstract void delete(K key) throws Exception;
}
