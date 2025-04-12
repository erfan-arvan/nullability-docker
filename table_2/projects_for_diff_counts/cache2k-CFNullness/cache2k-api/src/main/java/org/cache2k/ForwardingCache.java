package org.cache2k;
public abstract class ForwardingCache<K, V> implements Cache<K, V> {
  protected abstract Cache<K, V> delegate();
  @Override
  public String getName() {
    return delegate().getName();
  }
  @Override
  public  V get(K key) {
    return delegate().get(key);
  }
  @Override
  public  CacheEntry<K, V> getEntry(K key) {
    return delegate().getEntry(key);
  }
  @Override
  public  V peek(K key) {
    return delegate().peek(key);
  }
  @Override
  public  CacheEntry<K, V> peekEntry(K key) {
    return delegate().peekEntry(key);
  }
  @Override
  public boolean containsKey(K key) {
    return delegate().containsKey(key);
  }
  @Override
  public void put(K key, V value) {
    delegate().put(key, value);
  }
  @Override
  public V computeIfAbsent(K key, Callable<V> callable) {
    return delegate().computeIfAbsent(key, callable);
  }
  @Override
  public boolean putIfAbsent(K key, V value) {
    return delegate().putIfAbsent(key, value);
  }
  @Override
  public  V peekAndReplace(K key, V value) {
    return delegate().peekAndReplace(key, value);
  }
  @Override
  public boolean replace(K key, V value) {
    return delegate().replace(key, value);
  }
  @Override
  public boolean replaceIfEquals(K key, V oldValue, V newValue) {
    return delegate().replaceIfEquals(key, oldValue, newValue);
  }
  @Override
  public  V peekAndRemove(K key) {
    return delegate().peekAndRemove(key);
  }
  @Override
  public boolean containsAndRemove(K key) {
    return delegate().containsAndRemove(key);
  }
  @Override
  public void remove(K key) {
    delegate().remove(key);
  }
  @Override
  public boolean removeIfEquals(K key, V expectedValue) {
    return delegate().removeIfEquals(key, expectedValue);
  }
  @Override
  public void removeAll(Iterable<? extends K> keys) {
    delegate().removeAll(keys);
  }
  @Override
  public  V peekAndPut(K key, V value) {
    return delegate().peekAndPut(key, value);
  }
  @Override
  public void expireAt(K key, long millis) {
    delegate().expireAt(key, millis);
  }
  @Override
  public void loadAll(Iterable<? extends K> keys, CacheOperationCompletionListener listener) {
    delegate().loadAll(keys, listener);
  }
  @Override
  public void reloadAll(Iterable<? extends K> keys, CacheOperationCompletionListener listener) {
    delegate().reloadAll(keys, listener);
  }
  @Override
  public CompletableFuture<Void> loadAll(Iterable<? extends K> keys) {
    return delegate().loadAll(keys);
  }
  @Override
  public CompletableFuture<Void> reloadAll(Iterable<? extends K> keys) {
    return delegate().reloadAll(keys);
  }
  @Override
  public < R> R invoke(K key, EntryProcessor<K, V, R> processor) {
    return delegate().invoke(key, processor);
  }
  @Override
  public void mutate(K key, EntryMutator<K, V> mutator) {
    delegate().mutate(key, mutator);
  }
  @Override
  public < R> Map<K, EntryProcessingResult<R>> invokeAll(
    Iterable<? extends K> keys, EntryProcessor<K, V, R> entryProcessor) {
    return delegate().invokeAll(keys, entryProcessor);
  }
  @Override
  public void mutateAll(Iterable<? extends K> keys, EntryMutator<K, V> mutator) {
    delegate().mutateAll(keys, mutator);
  }
  @Override
  public Map<K, V> getAll(Iterable<? extends K> keys) {
    return delegate().getAll(keys);
  }
  @Override
  public Map<K, V> peekAll(Iterable<? extends K> keys) {
    return delegate().peekAll(keys);
  }
  @Override
  public void putAll(Map<? extends K, ? extends V> valueMap) {
    delegate().putAll(valueMap);
  }
  @Override
  public Iterable<K> keys() {
    return delegate().keys();
  }
  @Override
  public Iterable<CacheEntry<K, V>> entries() {
    return delegate().entries();
  }
  @Override
  public void removeAll() {
    delegate().removeAll();
  }
  @Override
  public void clear() {
    delegate().clear();
  }
  @Override
  public void close() {
    delegate().close();
  }
  @Override
  public CacheManager getCacheManager() {
    return delegate().getCacheManager();
  }
  @Override
  public boolean isClosed() {
    return delegate().isClosed();
  }
  @Override
  public <X> X requestInterface(Class<X> type) {
    return delegate().requestInterface(type);
  }
  @Override
  public ConcurrentMap<K, V> asMap() {
    return delegate().asMap();
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "!" + delegate().toString();
  }
}
