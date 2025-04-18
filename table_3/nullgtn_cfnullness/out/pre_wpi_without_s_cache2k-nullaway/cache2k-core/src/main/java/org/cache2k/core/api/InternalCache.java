package org.cache2k.core.api;

/*
 * #%L
 * cache2k core implementation
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
import org.cache2k.Cache;
import org.cache2k.CacheEntry;
import org.cache2k.config.CacheType;
import org.cache2k.core.CanCheckIntegrity;
import org.cache2k.core.ConcurrentMapWrapper;
import org.cache2k.core.eviction.Eviction;
import org.cache2k.core.operation.ExaminationEntry;
import org.cache2k.core.timing.TimerEventListener;
import org.cache2k.core.log.Log;

/**
 * Interface to extended cache functions for the internal components.
 *
 * @author Jens Wilke
 */
public interface InternalCache<K, V> extends Cache<K, V>, CanCheckIntegrity, TimerEventListener<K, V>, InternalCacheCloseContext {

    CommonMetrics getCommonMetrics();

    /**
     * used from the cache manager
     */
    Log getLog();

    CacheType getKeyType();

    CacheType getValueType();

    /**
     * used from the cache manager for shutdown
     */
    void cancelTimerJobs();

    /**
     * Generate cache statistics. Some of the statistic values involve scanning portions
     * of the cache content. To prevent system stress e.g. monitoring there is a
     * the compute intensive parts will only be repeated if some time is passed.
     */
    InternalCacheInfo getInfo();

    /**
     * Generate fresh statistics. This version is used by internal tests. This method
     * is not intended to be called at high frequencies or for attaching monitoring or logging.
     * Use the {@link #getInfo} method for requesting information for monitoring.
     */
    InternalCacheInfo getLatestInfo();

    String getEntryState(@Nullable() K key);

    /**
     * This method is used for {@link ConcurrentMapWrapper#size()}
     */
    int getTotalEntryCount();

    void logAndCountInternalException(@Nullable() String s, @Nullable() Throwable t);

    boolean isNullValuePermitted();

    /**
     * Time reference for the cache.
     */
    InternalClock getClock();

    CacheEntry<K, V> returnCacheEntry(@Nullable() ExaminationEntry<K, V> e);

    boolean isWeigherPresent();

    boolean isLoaderPresent();

    Eviction getEviction();
}
