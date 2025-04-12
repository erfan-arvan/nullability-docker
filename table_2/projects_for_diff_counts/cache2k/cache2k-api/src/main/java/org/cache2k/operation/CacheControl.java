package org.cache2k.operation;
public interface CacheControl extends CacheOperation, CacheInfo {
  static CacheControl of(Cache<?, ?> cache) {
    return cache.requestInterface(CacheControl.class);
  }
  CacheStatistics sampleStatistics();
}
