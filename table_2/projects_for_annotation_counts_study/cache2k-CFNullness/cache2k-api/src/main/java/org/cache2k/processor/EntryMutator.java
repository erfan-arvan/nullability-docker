package org.cache2k.processor;
public interface EntryMutator<K, V> extends DataAware<K, V> {
  void mutate(MutableCacheEntry<K, V> entry) throws Exception;
}
