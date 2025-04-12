package org.cache2k;
public interface Weigher<K, V> extends Customization<K, V> {
  int weigh(K key, V value);
}
