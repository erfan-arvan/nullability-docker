package org.cache2k.event;
public interface CacheClosedListener extends CacheLifecycleListener {
  CompletableFuture<Void> onCacheClosed(Cache<?, ?> cache);
}
