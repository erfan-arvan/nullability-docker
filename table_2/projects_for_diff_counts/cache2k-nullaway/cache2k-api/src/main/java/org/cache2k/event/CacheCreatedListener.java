package org.cache2k.event;
public interface CacheCreatedListener extends CacheLifecycleListener {
  <K, V> CompletableFuture<Void> onCacheCreated(Cache<K, V> cache,
                                                CacheBuildContext<K, V> ctx);
}
