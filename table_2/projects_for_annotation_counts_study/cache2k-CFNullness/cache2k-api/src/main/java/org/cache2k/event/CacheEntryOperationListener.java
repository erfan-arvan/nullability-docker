package org.cache2k.event;
public interface CacheEntryOperationListener<K, V> extends EventListener, DataAware<K, V> {
}
