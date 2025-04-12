package org.cache2k.io;
public interface ResiliencePolicy<K, V> extends ExpiryTimeValues, DataAwareCustomization<K, V> {
  ResiliencePolicy<?, ?> DISABLED_POLICY = new ResiliencePolicy<Object, Object>() {
    @Override
    public long suppressExceptionUntil(Object key, LoadExceptionInfo<Object, Object> loadExceptionInfo,
                                       CacheEntry<Object, Object> cachedEntry) {
      return NOW;
    }
    @Override
    public long retryLoadAfter(Object key, LoadExceptionInfo<Object, Object> loadExceptionInfo) {
      return NOW;
    }
  };
  static <K, V> ResiliencePolicy<K, V> disabledPolicy() {
    return (ResiliencePolicy<K, V>) DISABLED_POLICY;
  }
  static <K, V> void disable(Cache2kBuilder<K, V> b) {
    b.config().setResiliencePolicy(null);
  }
  long suppressExceptionUntil(K key,
                              LoadExceptionInfo<K, V> loadExceptionInfo,
                              CacheEntry<K, V> cachedEntry);
  long retryLoadAfter(K key, LoadExceptionInfo<K, V> loadExceptionInfo);
}
