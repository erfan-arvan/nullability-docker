package org.cache2k.config;

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

import org.cache2k.Cache2kBuilder;
import org.cache2k.Weigher;
import org.cache2k.annotation.NonNull;
import org.cache2k.annotation.Nullable;
import org.cache2k.event.CacheEntryOperationListener;
import org.cache2k.event.CacheLifecycleListener;
import org.cache2k.expiry.ExpiryPolicy;
import org.cache2k.expiry.ExpiryTimeValues;
import org.cache2k.io.AdvancedCacheLoader;
import org.cache2k.io.AsyncCacheLoader;
import org.cache2k.io.CacheLoader;
import org.cache2k.io.CacheWriter;
import org.cache2k.io.ExceptionPropagator;
import org.cache2k.io.ResiliencePolicy;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Configuration for a cache2k cache.
 *
 * <p>To create a cache, the {@link Cache2kBuilder} is used. All configuration properties
 * are present on the builder and are documented in this place. Consequently all properties
 * refer to the corresponding builder method.
 *
 * <p>The configuration bean is designed to be serializable. This is used for example to copy
 * default configurations. The builder allows object references to customizations to be set.
 * If this happens the configuration is not serializable. Such configuration is only used for
 * immediate creation of one cache via the builder.
 *
 * <p>The configuration may contain additional beans, called configuration sections, that are
 * used to configure extensions or sub modules.
 *
 * <p>Within the XML configuration of a cache manager different default configuration
 * values may be specified. To get a configuration bean with the effective defaults of
 * a specific manager do {@code Cache2kBuilder.forUnknownTypes().manager(...).toConfiguration()}
 *
 * @author Jens Wilke
 */

public class Cache2kConfig<K, V>
  implements ConfigBean<Cache2kConfig<K, V>, Cache2kBuilder<K, V>>, ConfigWithSections {

  /**
   * A marker duration used in {@link #setExpireAfterWrite(Duration)}, to request
   * eternal expiry. The maximum duration after the duration is considered as eternal
   * for our purposes.
   */
  public static final Duration EXPIRY_ETERNAL = Duration.ofMillis(ExpiryTimeValues.ETERNAL);
  /**
   * Marker duration that {@code setEternal(false)} was set.
   */
  public static final Duration EXPIRY_NOT_ETERNAL = Duration.ofMillis(ExpiryTimeValues.ETERNAL - 1);
  public static final long UNSET_LONG = -1;

  private boolean storeByReference;
  @Nullable private  String name;
  private boolean nameWasGenerated;
  @Nullable private  CacheType<K> keyType;
  @Nullable private  CacheType<V> valueType;
  private long entryCapacity = UNSET_LONG;
  @Nullable private  Duration expireAfterWrite = null;
  @Nullable private  Duration timerLag = null;
  private long maximumWeight = UNSET_LONG;
  private int loaderThreadCount;

  private boolean eternal = false;
  private boolean keepDataAfterExpired = false;
  private boolean sharpExpiry = false;
  private boolean strictEviction = false;
  private boolean refreshAhead = false;
  private boolean permitNullValues = false;
  private boolean recordModificationTime = false;
  private boolean boostConcurrency = false;

  private boolean disableStatistics = false;
  private boolean disableMonitoring = false;

  private boolean externalConfigurationPresent = false;

  @Nullable private  CustomizationSupplier<? extends Executor> loaderExecutor;
  @Nullable private  CustomizationSupplier<? extends Executor> refreshExecutor;
  @Nullable private  CustomizationSupplier<? extends Executor> asyncListenerExecutor;
  @Nullable private  CustomizationSupplier<? extends Executor> executor;
  @Nullable private  CustomizationSupplier<? extends ExpiryPolicy<K, V>> expiryPolicy;
  @Nullable private  CustomizationSupplier<? extends ResiliencePolicy<K, V>> resiliencePolicy;
  @Nullable private  CustomizationSupplier<? extends CacheLoader<K, V>> loader;
  @Nullable private  CustomizationSupplier<? extends CacheWriter<K, V>> writer;
  @Nullable private  CustomizationSupplier<? extends AdvancedCacheLoader<K, V>> advancedLoader;
  @Nullable private  CustomizationSupplier<? extends AsyncCacheLoader<K, V>> asyncLoader;
  @Nullable private  CustomizationSupplier<? extends ExceptionPropagator<K>> exceptionPropagator;
  @Nullable private  CustomizationSupplier<? extends Weigher<K, V>> weigher;

  @Nullable private  CustomizationCollection<CacheEntryOperationListener<K, V>> listeners;
  @Nullable private  CustomizationCollection<CacheEntryOperationListener<K, V>> asyncListeners;
  @Nullable private  Collection<CustomizationSupplier<CacheLifecycleListener>> lifecycleListeners;
  @Nullable private  Set<Feature> features;
  @Nullable private  SectionContainer sections;

  /**
   * Construct a config instance setting the type parameters and returning a
   * proper generic type.
   *
   * @see Cache2kBuilder#keyType(Class)
   * @see Cache2kBuilder#valueType(Class)
   */
  public static <K, V> Cache2kConfig<K, V> of(Class<K> keyType, Class<V> valueType) {
    Cache2kConfig<K, V> c = new Cache2kConfig<K, V>();
    c.setKeyType(CacheType.of(keyType));
    c.setValueType(CacheType.of(valueType));
    return c;
  }

  /**
   * Construct a config instance setting the type parameters and returning a
   * proper generic type.
   *
   * @see Cache2kBuilder#keyType(CacheType)
   * @see Cache2kBuilder#valueType(CacheType)
   */
  public static <K, V> Cache2kConfig<K, V> of(CacheType<K> keyType, CacheType<V> valueType) {
    Cache2kConfig<K, V> c = new Cache2kConfig<K, V>();
    c.setKeyType(keyType);
    c.setValueType(valueType);
    return c;
  }

  /**
   * @see Cache2kBuilder#name(String)
   */
  @Nullable public  String getName() { return name; }

  /**
   *
   * @see Cache2kBuilder#name(String)
   */
  public void setName( String name) {
    this.name = name;
  }

  /**
   * True if name is generated and not set by the cache client.
   */
  public boolean isNameWasGenerated() {
    return nameWasGenerated;
  }

  public void setNameWasGenerated(boolean v) {
    this.nameWasGenerated = v;
  }

  /**
   *
   * @see Cache2kBuilder#entryCapacity
   */
  public long getEntryCapacity() {
    return entryCapacity;
  }

  public void setEntryCapacity(long v) {
    this.entryCapacity = v;
  }

  /**
   * @see Cache2kBuilder#refreshAhead(boolean)
   */
  public boolean isRefreshAhead() {
    return refreshAhead;
  }

  /**
   * @see Cache2kBuilder#refreshAhead(boolean)
   */
  public void setRefreshAhead(boolean v) {
    this.refreshAhead = v;
  }

  @Nullable public  CacheType<K> getKeyType() {
    return keyType;
  }

  /**
   * @throws NullPointerException if parameter is null
   */
  public static void checkNull(Object obj) {
    if (obj == null) {
      throw new NullPointerException("Null value not allowed");
    }
  }

  /**
   * @see Cache2kBuilder#keyType(CacheType)
   * @see CacheType for a general discussion on types
   */
  public void setKeyType( CacheType<K> v) {
    if (v == null) {
      valueType = null;
      return;
    }
    if (v.isArray()) {
      throw new IllegalArgumentException("Arrays are not supported for keys");
    }
    keyType = v;
  }

  @Nullable public  CacheType<V> getValueType() {
    return valueType;
  }

  /**
   * @see Cache2kBuilder#valueType(CacheType)
   * @see CacheType for a general discussion on types
   */
  public void setValueType( CacheType<V> v) {
    if (v == null) {
      valueType = null;
      return;
    }
    if (v.isArray()) {
      throw new IllegalArgumentException("Arrays are not supported for values");
    }
    valueType = v;
  }

  @Nullable public  Duration getExpireAfterWrite() {
    return expireAfterWrite;
  }

  /**
   * Sets expire after write. The value is capped at {@link #EXPIRY_ETERNAL}, meaning
   * an equal or higher duration is treated as eternal expiry.
   *
   * @see Cache2kBuilder#expireAfterWrite
   */
  public void setExpireAfterWrite( Duration v) {
    this.expireAfterWrite = durationCheckAndSanitize(v);
  }

  public boolean isEternal() {
    return eternal;
  }

  public void setEternal(boolean v) {
    this.eternal = v;
  }

  @Nullable public  Duration getTimerLag() {
    return timerLag;
  }

  /**
   * @see Cache2kBuilder#timerLag(long, TimeUnit)
   */
  public void setTimerLag( Duration v) {
    this.timerLag = durationCheckAndSanitize(v);
  }

  public boolean isKeepDataAfterExpired() {
    return keepDataAfterExpired;
  }

  public long getMaximumWeight() {
    return maximumWeight;
  }

  /**
   * @see Cache2kBuilder#maximumWeight
   */
  public void setMaximumWeight(long v) {
    if (entryCapacity >= 0) {
      throw new IllegalArgumentException(
        "entryCapacity already set, setting maximumWeight is illegal");
    }
    maximumWeight = v;
  }

  /**
   * @see Cache2kBuilder#keepDataAfterExpired(boolean)
   */
  public void setKeepDataAfterExpired(boolean v) {
    this.keepDataAfterExpired = v;
  }

  public boolean isSharpExpiry() {
    return sharpExpiry;
  }

  /**
   * @see Cache2kBuilder#sharpExpiry(boolean)
   */
  public void setSharpExpiry(boolean v) {
    this.sharpExpiry = v;
  }

  /**
   * An external configuration for the cache was found and is applied.
   * This is {@code true} if default values are set via the XML configuration or
   * if there is a specific section for the cache name.
   */
  public boolean isExternalConfigurationPresent() {
    return externalConfigurationPresent;
  }

  public void setExternalConfigurationPresent(boolean v) {
    externalConfigurationPresent = v;
  }

  /**
   * Mutable collection of additional configuration sections
   */
  public SectionContainer getSections() {
    if (sections == null) {
      sections = new SectionContainer();
    }
    return sections;
  }

  /**
   * Adds the collection of sections to the existing list. This method is intended to
   * improve integration with bean configuration mechanisms that use the set method and
   * construct a set or list, like Springs' bean XML configuration.
   */
  public void setSections(Collection<ConfigSection> c) {
    getSections().addAll(c);
  }

  @Nullable public  CustomizationSupplier<? extends CacheLoader<K, V>> getLoader() {
    return loader;
  }

  public void setLoader( CustomizationSupplier<? extends CacheLoader<K, V>> v) {
    loader = v;
  }

  @Nullable public  CustomizationSupplier<? extends AdvancedCacheLoader<K, V>> getAdvancedLoader() {
    return advancedLoader;
  }

  /**
   * @see Cache2kBuilder#loader(AdvancedCacheLoader)
   */
  public void setAdvancedLoader( CustomizationSupplier<? extends AdvancedCacheLoader<K, V>> v) {
    advancedLoader = v;
  }

  @Nullable public  CustomizationSupplier<? extends AsyncCacheLoader<K, V>> getAsyncLoader() {
    return asyncLoader;
  }

  public void setAsyncLoader( CustomizationSupplier<? extends AsyncCacheLoader<K, V>> v) {
    asyncLoader = v;
  }

  public int getLoaderThreadCount() {
    return loaderThreadCount;
  }

  /**
   * @see Cache2kBuilder#loaderThreadCount(int)
   */
  public void setLoaderThreadCount(int v) {
    loaderThreadCount = v;
  }

  @Nullable public  CustomizationSupplier<? extends ExpiryPolicy<K, V>> getExpiryPolicy() {
    return expiryPolicy;
  }

  public void setExpiryPolicy( CustomizationSupplier<? extends ExpiryPolicy<K, V>> v) {
    expiryPolicy = v;
  }

  @Nullable public  CustomizationSupplier<? extends CacheWriter<K, V>> getWriter() {
    return writer;
  }

  /**
   * @see Cache2kBuilder#writer(CacheWriter)
   */
  public void setWriter( CustomizationSupplier<? extends CacheWriter<K, V>> v) {
    writer = v;
  }

  public boolean isStoreByReference() {
    return storeByReference;
  }

  /**
   * @see Cache2kBuilder#storeByReference(boolean)
   */
  public void setStoreByReference(boolean v) {
    storeByReference = v;
  }

  @Nullable public  CustomizationSupplier<? extends ExceptionPropagator<K>> getExceptionPropagator() {
    return exceptionPropagator;
  }

  /**
   * @see Cache2kBuilder#exceptionPropagator(ExceptionPropagator)
   */
  public void setExceptionPropagator( CustomizationSupplier<? extends ExceptionPropagator<K>> v) {
    exceptionPropagator = v;
  }

  /**
   * A set of listeners. Listeners added in this collection will be
   * executed in a synchronous mode, meaning, further processing for
   * an entry will stall until a registered listener is executed.
   * The expiry will be always executed asynchronously.
   *
   * <p>A listener can be added by adding it to the collection.
   * Duplicate (in terms of equal objects) listeners will be ignored.
   *
   * @return Mutable collection of listeners
   */
  public 
  CustomizationCollection<CacheEntryOperationListener<K, V>> getListeners() {
    if (listeners == null) {
      listeners = new DefaultCustomizationCollection<CacheEntryOperationListener<K, V>>();
    }
    return listeners;
  }

  /**
   * @return True if listeners are added to this configuration.
   */
  public boolean hasListeners() {
    return listeners != null && !listeners.isEmpty();
  }

  /**
   * Adds the collection of customizations to the existing list. This method is intended to
   * improve integration with bean configuration mechanisms that use the set method and
   * construct a set or list, like Springs' bean XML configuration.
   */
  public void setListeners(
    Collection<CustomizationSupplier<CacheEntryOperationListener<K, V>>> c) {
    getListeners().addAll(c);
  }

  /**
   * A set of listeners. A listener can be added by adding it to the collection.
   * Duplicate (in terms of equal objects) listeners will be ignored.
   *
   * @return Mutable collection of listeners
   */
  public 
  CustomizationCollection<CacheEntryOperationListener<K, V>> getAsyncListeners() {
    if (asyncListeners == null) {
      asyncListeners = new DefaultCustomizationCollection<CacheEntryOperationListener<K, V>>();
    }
    return asyncListeners;
  }

  /**
   * @return True if listeners are added to this configuration.
   */
  public boolean hasAsyncListeners() {
    return asyncListeners != null && !asyncListeners.isEmpty();
  }

  /**
   * Adds the collection of customizations to the existing list. This method is intended to
   * improve integration with bean configuration mechanisms that use the set method and
   * construct a set or list, like Springs' bean XML configuration.
   */
  public void setAsyncListeners(
    Collection<CustomizationSupplier<CacheEntryOperationListener<K, V>>> c) {
    getAsyncListeners().addAll(c);
  }

  /**
   * A set of listeners. A listener can be added by adding it to the collection.
   * Duplicate (in terms of equal objects) listeners will be ignored.
   *
   * @return Mutable collection of listeners
   */
  public 
  Collection<CustomizationSupplier<? extends CacheLifecycleListener>> getLifecycleListeners() {
    if (lifecycleListeners == null) {
      lifecycleListeners = new DefaultCustomizationCollection<CacheLifecycleListener>();
    }
    return (Collection<CustomizationSupplier<? extends CacheLifecycleListener>>) (Object)
      lifecycleListeners;
  }

  /**
   * @return True if listeners are added to this configuration.
   */
  public boolean hasLifecycleListeners() {
    return lifecycleListeners != null && !lifecycleListeners.isEmpty();
  }

  /**
   * Adds the collection of customizations to the existing list. This method is intended to
   * improve integration with bean configuration mechanisms that use the set method and
   * construct a set or list, like Springs' bean XML configuration.
   */
  public void setLifecycleListeners( Collection<CustomizationSupplier<? extends CacheLifecycleListener>> c) {
    getLifecycleListeners().addAll(c);
  }

  public 
  Set<Feature> getFeatures() {
    if (features == null) {
      features = new HashSet<>();
    }
    return features;
  }

  public boolean hasFeatures() {
    return features != null && !features.isEmpty();
  }

  public void setFeatures( Set<? extends Feature> v) {
    getFeatures().addAll(v);
  }

  @Nullable public  CustomizationSupplier<? extends ResiliencePolicy<K, V>> getResiliencePolicy() {
    return resiliencePolicy;
  }

  /**
   * @see Cache2kBuilder#resiliencePolicy
   */
  public void setResiliencePolicy( @Nullable CustomizationSupplier<? extends ResiliencePolicy<K, V>> v) {
    resiliencePolicy = v;
  }

  public boolean isStrictEviction() {
    return strictEviction;
  }

  /**
   * @see Cache2kBuilder#strictEviction(boolean)
   */
  public void setStrictEviction(boolean v) {
    strictEviction = v;
  }

  public boolean isPermitNullValues() {
    return permitNullValues;
  }

  /**
   * @see Cache2kBuilder#permitNullValues(boolean)
   */
  public void setPermitNullValues(boolean v) {
    permitNullValues = v;
  }

  public boolean isDisableStatistics() {
    return disableStatistics;
  }

  /**
   * @see Cache2kBuilder#disableStatistics
   */
  public void setDisableStatistics(boolean v) {
    disableStatistics = v;
  }

  @Nullable public  CustomizationSupplier<? extends Executor> getLoaderExecutor() {
    return loaderExecutor;
  }

  /**
   * @see Cache2kBuilder#loaderExecutor(Executor)
   */
  public void setLoaderExecutor( CustomizationSupplier<? extends Executor> v) {
    loaderExecutor = v;
  }

  public boolean isRecordModificationTime() {
    return recordModificationTime;
  }

  /**
   * @see Cache2kBuilder#recordModificationTime
   */
  public void setRecordModificationTime(boolean v) {
    recordModificationTime = v;
  }

  @Nullable public  CustomizationSupplier<? extends Executor> getRefreshExecutor() {
    return refreshExecutor;
  }

  /**
   * @see Cache2kBuilder#refreshExecutor(Executor)
   */
  public void setRefreshExecutor( CustomizationSupplier<? extends Executor> v) {
    refreshExecutor = v;
  }

  @Nullable public  CustomizationSupplier<? extends Executor> getExecutor() {
    return executor;
  }

  /**
   * @see Cache2kBuilder#executor(Executor)
   */
  public void setExecutor( CustomizationSupplier<? extends Executor> v) {
    executor = v;
  }

  @Nullable public  CustomizationSupplier<? extends Executor> getAsyncListenerExecutor() {
    return asyncListenerExecutor;
  }

  /**
   * @see Cache2kBuilder#asyncListenerExecutor(Executor)
   */
  public void setAsyncListenerExecutor( CustomizationSupplier<? extends Executor> v) {
    asyncListenerExecutor = v;
  }

  @Nullable public  CustomizationSupplier<? extends Weigher<K, V>> getWeigher() {
    return weigher;
  }

  /**
   * @see Cache2kBuilder#weigher(Weigher)
   */
  public void setWeigher( CustomizationSupplier<? extends Weigher<K, V>> v) {
    /*
    if (entryCapacity >= 0) {
      throw new IllegalArgumentException(
        "entryCapacity already set, specifying a weigher is illegal");
    }
    */
    weigher = v;
  }

  public boolean isBoostConcurrency() {
    return boostConcurrency;
  }

  /**
   * @see Cache2kBuilder#boostConcurrency(boolean)
   */
  public void setBoostConcurrency(boolean v) {
    boostConcurrency = v;
  }

  public boolean isDisableMonitoring() {
    return disableMonitoring;
  }

  /**
   * @see Cache2kBuilder#disableMonitoring(boolean)
   */
  public void setDisableMonitoring(boolean disableMonitoring) {
    this.disableMonitoring = disableMonitoring;
  }

  @Nullable private  Duration durationCheckAndSanitize( Duration v) {
    if (v == null) {
      return null;
    }
    if (v.isNegative()) {
      throw new IllegalArgumentException("Duration must be positive");
    }
    if (EXPIRY_ETERNAL.compareTo(v) <= 0) {
      v = EXPIRY_ETERNAL;
    }
    return v;
  }

  /**
   * Creates a cache builder from the configuration.
   */
  public Cache2kBuilder<K, V> builder() {
    return Cache2kBuilder.of(this);
  }

}
