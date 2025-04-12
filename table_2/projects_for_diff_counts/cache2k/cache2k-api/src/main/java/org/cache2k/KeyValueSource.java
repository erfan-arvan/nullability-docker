package org.cache2k;
public interface KeyValueSource<K, V> {
   V get(K key);
}
