package org.cache2k.event;
public interface CacheEntryExpiredListener<K, V> extends CacheEntryOperationListener<K, V> {
  void onEntryExpired(Cache<K, V> cache, CacheEntry<K, V> entry);
}
