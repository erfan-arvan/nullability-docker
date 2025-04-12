package org.cache2k.processor;
@FunctionalInterface
public interface EntryProcessor<K, V, R> {
   R process(MutableCacheEntry<K, V> entry) throws Exception;
}
