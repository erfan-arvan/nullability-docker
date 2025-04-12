package org.cache2k;
public interface CacheEntry<K, V> {
  K getKey();
  V getValue();
  default  Throwable getException() {
    LoadExceptionInfo<K> info = getExceptionInfo();
    return info != null ? info.getException() : null;
  }
   LoadExceptionInfo<K> getExceptionInfo();
}
