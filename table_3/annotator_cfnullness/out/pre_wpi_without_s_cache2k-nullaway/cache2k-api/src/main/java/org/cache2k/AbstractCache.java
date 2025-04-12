package org.cache2k;

/*
 * #%L
 * cache2k API
 * %%
 * Copyright (C) 2000 - 2020 headissue GmbH, Munich
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import org.cache2k.processor.EntryProcessingResult;
import org.cache2k.processor.EntryProcessor;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

/**
 * Base class for implementations of the cache interface. By default every methods throws
 * {@link UnsupportedOperationException}.
 *
 * @author Jens Wilke
 */
public class AbstractCache<K, V> implements Cache<K, V> {

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CacheEntry<K, V> getEntry(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V peek(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CacheEntry<K, V> peekEntry(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(@Nullable() K key, @Nullable() V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V computeIfAbsent(@Nullable() K key, @Nullable() Callable<V> callable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean putIfAbsent(@Nullable() K key, @Nullable() V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V peekAndReplace(@Nullable() K key, @Nullable() V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(@Nullable() K key, @Nullable() V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replaceIfEquals(@Nullable() K key, @Nullable() V oldValue, @Nullable() V newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V peekAndRemove(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAndRemove(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(@Nullable() K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIfEquals(@Nullable() K key, @Nullable() V expectedValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAll(@Nullable() Iterable<? extends K> keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V peekAndPut(@Nullable() K key, @Nullable() V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void expireAt(@Nullable() K key, @Nullable() long millis) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadAll(@Nullable() Iterable<? extends K> keys, @Nullable() CacheOperationCompletionListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reloadAll(@Nullable() Iterable<? extends K> keys, @Nullable() CacheOperationCompletionListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> loadAll(@Nullable() Iterable<? extends K> keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> reloadAll(@Nullable() Iterable<? extends K> keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R> R invoke(@Nullable() K key, @Nullable() EntryProcessor<K, V, R> entryProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R> Map<K, EntryProcessingResult<R>> invokeAll(@Nullable() Iterable<? extends K> keys, @Nullable() EntryProcessor<K, V, R> entryProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<K, V> getAll(@Nullable() Iterable<? extends K> keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<K, V> peekAll(@Nullable() Iterable<? extends K> keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(@Nullable() Map<? extends K, ? extends V> valueMap) {
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
    public <X> X requestInterface(@Nullable() Class<X> type) {
        throw new UnsupportedOperationException();
    }
}
