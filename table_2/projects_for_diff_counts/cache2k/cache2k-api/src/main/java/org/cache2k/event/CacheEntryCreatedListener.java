package org.cache2k.event;
public interface CacheEntryCreatedListener<K, V> extends CacheEntryOperationListener<K, V> {
  void onEntryCreated(Cache<K, V> cache, CacheEntry<K, V> entry);
}
