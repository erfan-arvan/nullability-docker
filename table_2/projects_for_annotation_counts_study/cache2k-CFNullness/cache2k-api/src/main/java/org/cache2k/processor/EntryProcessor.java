package org.cache2k.processor;
@FunctionalInterface
public interface EntryProcessor<K, V,  R> extends DataAware<K, V> {
   R process(MutableCacheEntry<K, V> entry) throws Exception;
}
