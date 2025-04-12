package org.cache2k;
public interface CacheEntry<K, V> extends DataAware<K, V> {
  K getKey();
  V getValue();
  default  Throwable getException() {
    LoadExceptionInfo<K, V> info = getExceptionInfo();
    return info != null ? info.getException() : null;
  }
   LoadExceptionInfo<K, V> getExceptionInfo();
}
