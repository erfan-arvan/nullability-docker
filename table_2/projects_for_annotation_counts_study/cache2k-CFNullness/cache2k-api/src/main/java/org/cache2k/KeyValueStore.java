package org.cache2k;
public interface KeyValueStore<K, V>
  extends AdvancedKeyValueSource<K, V> {
  void put(K key, V value);
  void putAll(Map<? extends K, ? extends V> valueMap);
  void remove(K key);
  void removeAll(Iterable<? extends K> keys);
}
