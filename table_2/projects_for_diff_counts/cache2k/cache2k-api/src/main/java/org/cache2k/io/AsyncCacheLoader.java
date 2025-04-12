package org.cache2k.io;
@FunctionalInterface
public interface AsyncCacheLoader<K, V> extends Customization<K, V> {
  void load(K key, Context<K, V> context, Callback<V> callback) throws Exception;
  interface Context<K, V> {
    long getStartTime();
    K getKey();
    Executor getExecutor();
    Executor getLoaderExecutor();
    CacheEntry<K, V> getCurrentEntry();
  }
  interface Callback<V> extends EventListener {
    void onLoadSuccess(V value);
    void onLoadFailure(Throwable t);
  }
}
