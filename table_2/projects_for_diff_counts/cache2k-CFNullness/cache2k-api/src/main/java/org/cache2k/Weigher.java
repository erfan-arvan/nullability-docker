package org.cache2k;
public interface Weigher<K, V> extends DataAwareCustomization<K, V> {
  int weigh(K key, V value);
}
