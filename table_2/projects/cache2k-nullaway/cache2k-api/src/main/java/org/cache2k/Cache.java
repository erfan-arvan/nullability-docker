package org.cache2k;
public interface Cache<K, V> extends KeyValueStore<K, V>, Closeable {
  String getName();
  @Override
   V get(K key);
   CacheEntry<K, V> getEntry(K key);
   V peek(K key);
   CacheEntry<K, V> peekEntry(K key);
  boolean containsKey(K key);
  @Override
  void put(K key, V value);
  V computeIfAbsent(K key, Callable<V> callable);
  boolean putIfAbsent(K key, V value);
  V peekAndReplace(K key, V value);
  boolean replace(K key, V value);
  boolean replaceIfEquals(K key, V oldValue, V newValue);
   V peekAndRemove(K key);
  boolean containsAndRemove(K key);
  @Override
  void remove(K key);
  boolean removeIfEquals(K key, V expectedValue);
  @Override
  void removeAll(Iterable<? extends K> keys);
   V peekAndPut(K key, V value);
  @Deprecated
  void expireAt(K key, long millis);
  @Deprecated
  void loadAll(Iterable<? extends K> keys, CacheOperationCompletionListener listener);
  @Deprecated
  void reloadAll(Iterable<? extends K> keys, CacheOperationCompletionListener listener);
  CompletableFuture<Void> loadAll(Iterable<? extends K> keys);
  CompletableFuture<Void> reloadAll(Iterable<? extends K> keys);
  <R>  R invoke(K key, EntryProcessor<K, V, R> processor);
  default void mutate(K key, EntryMutator<K, V> mutator) {
    invoke(key, entry -> { mutator.mutate(entry); return this; });
  }
  <R> Map< K,  EntryProcessingResult<R>> invokeAll(
    Iterable<? extends K> keys, EntryProcessor<K, V, R> entryProcessor);
  default void mutateAll(Iterable<? extends K> keys, EntryMutator<K, V> mutator) {
    invokeAll(keys, entry -> { mutator.mutate(entry); return this; });
  }
  @Override
  Map<K, V> getAll(Iterable<? extends K> keys);
  Map<K, V> peekAll(Iterable<? extends K> keys);
    @Override
    void putAll(Map<? extends K, ? extends V> valueMap);
  Iterable<K> keys();
  Iterable<CacheEntry<K, V>> entries();
  void removeAll();
  void clear();
  @Override
  void close();
  CacheManager getCacheManager();
  boolean isClosed();
  @Override
  String toString();
  <T> T requestInterface(Class<T> type);
  ConcurrentMap<K, V> asMap();
}
