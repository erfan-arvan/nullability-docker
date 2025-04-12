package org.cache2k.expiry;
public interface ExpiryPolicy<K, V> extends ExpiryTimeValues, DataAware<K, V> {
  long calculateExpiryTime(K key, V value, long loadTime,  CacheEntry<K, V> currentEntry);
}
