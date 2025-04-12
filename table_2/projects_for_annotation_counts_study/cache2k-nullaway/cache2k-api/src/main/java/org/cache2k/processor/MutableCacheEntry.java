package org.cache2k.processor;
public interface MutableCacheEntry<K, V> extends CacheEntry<K, V> {
  @Override
   V getValue();
  @Override
   Throwable getException();
  @Override
   LoadExceptionInfo<K> getExceptionInfo();
  boolean exists();
  long getStartTime();
  MutableCacheEntry<K, V> lock();
  MutableCacheEntry<K, V> setValue(V v);
  MutableCacheEntry<K, V> load();
  MutableCacheEntry<K, V> remove();
  MutableCacheEntry<K, V> setException(Throwable ex);
  MutableCacheEntry<K, V> setExpiryTime(long t);
  long getExpiryTime();
  long getModificationTime();
  MutableCacheEntry<K, V> setModificationTime(long t);
}
