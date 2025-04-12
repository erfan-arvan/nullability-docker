package org.cache2k;
public interface AdvancedKeyValueSource<K, V> extends KeyValueSource<K, V> {
  Map<K, V> getAll(Iterable<? extends K> keys);
}
