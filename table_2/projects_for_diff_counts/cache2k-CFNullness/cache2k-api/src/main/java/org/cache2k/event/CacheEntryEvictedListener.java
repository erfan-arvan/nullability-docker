package org.cache2k.event;
public interface CacheEntryEvictedListener<K, V> extends CacheEntryOperationListener<K, V> {
  void onEntryEvicted(Cache<K, V> cache, CacheEntry<K, V> entry);
}
