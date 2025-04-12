package org.cache2k.event;
public interface CacheEntryUpdatedListener<K, V> extends CacheEntryOperationListener<K, V> {
  void onEntryUpdated(
    Cache<K, V> cache, CacheEntry<K, V> currentEntry, CacheEntry<K, V> newEntry);
}
