package org.cache2k.event;
public interface CacheEntryRemovedListener<K, V> extends CacheEntryOperationListener<K, V> {
  void onEntryRemoved(Cache<K, V> cache, CacheEntry<K, V> entry);
}
