package org.cache2k;
public interface KeyValueSource<K, V> extends DataAware<K, V> {
   V get(K key);
}
