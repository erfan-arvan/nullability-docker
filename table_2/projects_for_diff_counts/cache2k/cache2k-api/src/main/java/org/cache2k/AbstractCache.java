package org.cache2k;
public class AbstractCache<K, V> implements Cache<K, V> {
  @Override
  public String getName() {
    throw new UnsupportedOperationException();
  }
  @Override
  public  V get(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public CacheEntry<K, V> getEntry(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public V peek(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public CacheEntry<K, V> peekEntry(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean containsKey(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public void put(K key, V value) {
    throw new UnsupportedOperationException();
  }
  @Override
  public V computeIfAbsent(K key, Callable<V> callable) {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean putIfAbsent(K key, V value) {
    throw new UnsupportedOperationException();
  }
  @Override
  public V peekAndReplace(K key, V value) {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean replace(K key, V value) {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean replaceIfEquals(K key, V oldValue, V newValue) {
    throw new UnsupportedOperationException();
  }
  @Override
  public V peekAndRemove(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean containsAndRemove(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public void remove(K key) {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean removeIfEquals(K key, V expectedValue) {
    throw new UnsupportedOperationException();
  }
  @Override
  public void removeAll(Iterable<? extends K> keys) {
    throw new UnsupportedOperationException();
  }
  @Override
  public V peekAndPut(K key, V value) {
    throw new UnsupportedOperationException();
  }
  @Override
  public void expireAt(K key, long millis) {
    throw new UnsupportedOperationException();
  }
  @Override
  public void loadAll(Iterable<? extends K> keys, CacheOperationCompletionListener listener) {
    throw new UnsupportedOperationException();
  }
  @Override
  public void reloadAll(Iterable<? extends K> keys, CacheOperationCompletionListener listener) {
    throw new UnsupportedOperationException();
  }
  @Override
  public CompletableFuture<Void> loadAll(Iterable<? extends K> keys) {
    throw new UnsupportedOperationException();
  }
  @Override
  public CompletableFuture<Void> reloadAll(Iterable<? extends K> keys) {
    throw new UnsupportedOperationException();
  }
  @Override
  public <R> R invoke(K key, EntryProcessor<K, V, R> processor) {
    throw new UnsupportedOperationException();
  }
  @Override
  public <R> Map<K, EntryProcessingResult<R>> invokeAll(
    Iterable<? extends K> keys, EntryProcessor<K, V, R> entryProcessor) {
    throw new UnsupportedOperationException();
  }
  @Override
  public Map<K, V> getAll(Iterable<? extends K> keys) {
    throw new UnsupportedOperationException();
  }
  @Override
  public Map<K, V> peekAll(Iterable<? extends K> keys) {
    throw new UnsupportedOperationException();
  }
  @Override
  public void putAll(Map<? extends K, ? extends V> valueMap) {
    throw new UnsupportedOperationException();
  }
  @Override
  public Iterable<K> keys() {
    throw new UnsupportedOperationException();
  }
  @Override
  public Iterable<CacheEntry<K, V>> entries() {
    throw new UnsupportedOperationException();
  }
  @Override
  public void removeAll() {
    throw new UnsupportedOperationException();
  }
  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }
  @Override
  public void close() {
    throw new UnsupportedOperationException();
  }
  @Override
  public CacheManager getCacheManager() {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean isClosed() {
    throw new UnsupportedOperationException();
  }
  @Override
  public ConcurrentMap<K, V> asMap() {
    throw new UnsupportedOperationException();
  }
  @Override
  public <X> X requestInterface(Class<X> type) {
    throw new UnsupportedOperationException();
  }
  @Override
  public String toString() {
    throw new UnsupportedOperationException();
  }
}
