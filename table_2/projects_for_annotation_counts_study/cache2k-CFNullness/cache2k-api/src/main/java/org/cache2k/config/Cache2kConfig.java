package org.cache2k.config;
public class Cache2kConfig< K,  V>
  implements ConfigBean<Cache2kConfig<K, V>, Cache2kBuilder<K, V>>, ConfigWithSections {
  public static final Duration EXPIRY_ETERNAL = Duration.ofMillis(ExpiryTimeValues.ETERNAL);
  public static final Duration EXPIRY_NOT_ETERNAL = Duration.ofMillis(ExpiryTimeValues.ETERNAL - 1);
  public static final long UNSET_LONG = -1;
  private boolean storeByReference;
  private  String name;
  private boolean nameWasGenerated;
  private  CacheType<K> keyType;
  private  CacheType<V> valueType;
  private long entryCapacity = UNSET_LONG;
  private  Duration expireAfterWrite = null;
  private  Duration timerLag = null;
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
  private  CustomizationSupplier<? extends Executor> loaderExecutor;
  private  CustomizationSupplier<? extends Executor> refreshExecutor;
  private  CustomizationSupplier<? extends Executor> asyncListenerExecutor;
  private  CustomizationSupplier<? extends Executor> executor;
  private  CustomizationSupplier<? extends ExpiryPolicy<K, V>> expiryPolicy;
  private  CustomizationSupplier<? extends ResiliencePolicy<K, V>> resiliencePolicy;
  private  CustomizationSupplier<? extends CacheLoader<K, V>> loader;
  private  CustomizationSupplier<? extends CacheWriter<K, V>> writer;
  private  CustomizationSupplier<? extends AdvancedCacheLoader<K, V>> advancedLoader;
  private  CustomizationSupplier<? extends AsyncCacheLoader<K, V>> asyncLoader;
  private  CustomizationSupplier<? extends ExceptionPropagator<K, V>> exceptionPropagator;
  private  CustomizationSupplier<? extends Weigher<K, V>> weigher;
  private  CustomizationCollection<CacheEntryOperationListener<K, V>> listeners;
  private  CustomizationCollection<CacheEntryOperationListener<K, V>> asyncListeners;
  private  Collection<CustomizationSupplier<CacheLifecycleListener>> lifecycleListeners;
  private  Set<Feature> features;
  private  SectionContainer sections;
  public static <K, V> Cache2kConfig<K, V> of(Class<K> keyType, Class<V> valueType) {
    Cache2kConfig<K, V> c = new Cache2kConfig<K, V>();
    c.setKeyType(CacheType.of(keyType));
    c.setValueType(CacheType.of(valueType));
    return c;
  }
  public static <K, V> Cache2kConfig<K, V> of(CacheType<K> keyType, CacheType<V> valueType) {
    Cache2kConfig<K, V> c = new Cache2kConfig<K, V>();
    c.setKeyType(keyType);
    c.setValueType(valueType);
    return c;
  }
  public  String getName() { return name; }
  public void setName( String name) {
    this.name = name;
  }
  public boolean isNameWasGenerated() {
    return nameWasGenerated;
  }
  public void setNameWasGenerated(boolean v) {
    this.nameWasGenerated = v;
  }
  public long getEntryCapacity() {
    return entryCapacity;
  }
  public void setEntryCapacity(long v) {
    this.entryCapacity = v;
  }
  public boolean isRefreshAhead() {
    return refreshAhead;
  }
  public void setRefreshAhead(boolean v) {
    this.refreshAhead = v;
  }
  public  CacheType<K> getKeyType() {
    return keyType;
  }
  public static void checkNull(Object obj) {
    if (obj == null) {
      throw new NullPointerException("Null value not allowed");
    }
  }
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
  public  CacheType<V> getValueType() {
    return valueType;
  }
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
  public  Duration getExpireAfterWrite() {
    return expireAfterWrite;
  }
  public void setExpireAfterWrite( Duration v) {
    this.expireAfterWrite = durationCheckAndSanitize(v);
  }
  public boolean isEternal() {
    return eternal;
  }
  public void setEternal(boolean v) {
    this.eternal = v;
  }
  public  Duration getTimerLag() {
    return timerLag;
  }
  public void setTimerLag( Duration v) {
    this.timerLag = durationCheckAndSanitize(v);
  }
  public boolean isKeepDataAfterExpired() {
    return keepDataAfterExpired;
  }
  public long getMaximumWeight() {
    return maximumWeight;
  }
  public void setMaximumWeight(long v) {
    if (entryCapacity >= 0) {
      throw new IllegalArgumentException(
        "entryCapacity already set, setting maximumWeight is illegal");
    }
    maximumWeight = v;
  }
  public void setKeepDataAfterExpired(boolean v) {
    this.keepDataAfterExpired = v;
  }
  public boolean isSharpExpiry() {
    return sharpExpiry;
  }
  public void setSharpExpiry(boolean v) {
    this.sharpExpiry = v;
  }
  public boolean isExternalConfigurationPresent() {
    return externalConfigurationPresent;
  }
  public void setExternalConfigurationPresent(boolean v) {
    externalConfigurationPresent = v;
  }
  public SectionContainer getSections() {
    if (sections == null) {
      sections = new SectionContainer();
    }
    return sections;
  }
  public void setSections(Collection<ConfigSection> c) {
    getSections().addAll(c);
  }
  public  CustomizationSupplier<? extends CacheLoader<K, V>> getLoader() {
    return loader;
  }
  public void setLoader( CustomizationSupplier<? extends CacheLoader<K, V>> v) {
    loader = v;
  }
  public  CustomizationSupplier<? extends AdvancedCacheLoader<K, V>> getAdvancedLoader() {
    return advancedLoader;
  }
  public void setAdvancedLoader( CustomizationSupplier<? extends AdvancedCacheLoader<K, V>> v) {
    advancedLoader = v;
  }
  public  CustomizationSupplier<? extends AsyncCacheLoader<K, V>> getAsyncLoader() {
    return asyncLoader;
  }
  public void setAsyncLoader( CustomizationSupplier<? extends AsyncCacheLoader<K, V>> v) {
    asyncLoader = v;
  }
  public int getLoaderThreadCount() {
    return loaderThreadCount;
  }
  public void setLoaderThreadCount(int v) {
    loaderThreadCount = v;
  }
  public  CustomizationSupplier<? extends ExpiryPolicy<K, V>> getExpiryPolicy() {
    return expiryPolicy;
  }
  public void setExpiryPolicy( CustomizationSupplier<? extends ExpiryPolicy<K, V>> v) {
    expiryPolicy = v;
  }
  public  CustomizationSupplier<? extends CacheWriter<K, V>> getWriter() {
    return writer;
  }
  public void setWriter( CustomizationSupplier<? extends CacheWriter<K, V>> v) {
    writer = v;
  }
  public boolean isStoreByReference() {
    return storeByReference;
  }
  public void setStoreByReference(boolean v) {
    storeByReference = v;
  }
  public  CustomizationSupplier<? extends ExceptionPropagator<K, V>> getExceptionPropagator() {
    return exceptionPropagator;
  }
  public void setExceptionPropagator( CustomizationSupplier<? extends ExceptionPropagator<K, V>> v) {
    exceptionPropagator = v;
  }
  public 
  CustomizationCollection<CacheEntryOperationListener<K, V>> getListeners() {
    if (listeners == null) {
      listeners = new DefaultCustomizationCollection<CacheEntryOperationListener<K, V>>();
    }
    return listeners;
  }
  public boolean hasListeners() {
    return listeners != null && !listeners.isEmpty();
  }
  public void setListeners(
    Collection<CustomizationSupplier<CacheEntryOperationListener<K, V>>> c) {
    getListeners().addAll(c);
  }
  public 
  CustomizationCollection<CacheEntryOperationListener<K, V>> getAsyncListeners() {
    if (asyncListeners == null) {
      asyncListeners = new DefaultCustomizationCollection<CacheEntryOperationListener<K, V>>();
    }
    return asyncListeners;
  }
  public boolean hasAsyncListeners() {
    return asyncListeners != null && !asyncListeners.isEmpty();
  }
  public void setAsyncListeners(
    Collection<CustomizationSupplier<CacheEntryOperationListener<K, V>>> c) {
    getAsyncListeners().addAll(c);
  }
  public 
  Collection<CustomizationSupplier<? extends CacheLifecycleListener>> getLifecycleListeners() {
    if (lifecycleListeners == null) {
      lifecycleListeners = new DefaultCustomizationCollection<CacheLifecycleListener>();
    }
    return (Collection<CustomizationSupplier<? extends CacheLifecycleListener>>) (Object)
      lifecycleListeners;
  }
  public boolean hasLifecycleListeners() {
    return lifecycleListeners != null && !lifecycleListeners.isEmpty();
  }
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
  public  CustomizationSupplier<? extends ResiliencePolicy<K, V>> getResiliencePolicy() {
    return resiliencePolicy;
  }
  public void setResiliencePolicy( CustomizationSupplier<? extends ResiliencePolicy<K, V>> v) {
    resiliencePolicy = v;
  }
  public boolean isStrictEviction() {
    return strictEviction;
  }
  public void setStrictEviction(boolean v) {
    strictEviction = v;
  }
  public boolean isPermitNullValues() {
    return permitNullValues;
  }
  public void setPermitNullValues(boolean v) {
    permitNullValues = v;
  }
  public boolean isDisableStatistics() {
    return disableStatistics;
  }
  public void setDisableStatistics(boolean v) {
    disableStatistics = v;
  }
  public  CustomizationSupplier<? extends Executor> getLoaderExecutor() {
    return loaderExecutor;
  }
  public void setLoaderExecutor( CustomizationSupplier<? extends Executor> v) {
    loaderExecutor = v;
  }
  public boolean isRecordModificationTime() {
    return recordModificationTime;
  }
  public void setRecordModificationTime(boolean v) {
    recordModificationTime = v;
  }
  public  CustomizationSupplier<? extends Executor> getRefreshExecutor() {
    return refreshExecutor;
  }
  public void setRefreshExecutor( CustomizationSupplier<? extends Executor> v) {
    refreshExecutor = v;
  }
  public  CustomizationSupplier<? extends Executor> getExecutor() {
    return executor;
  }
  public void setExecutor( CustomizationSupplier<? extends Executor> v) {
    executor = v;
  }
  public  CustomizationSupplier<? extends Executor> getAsyncListenerExecutor() {
    return asyncListenerExecutor;
  }
  public void setAsyncListenerExecutor( CustomizationSupplier<? extends Executor> v) {
    asyncListenerExecutor = v;
  }
  public  CustomizationSupplier<? extends Weigher<K, V>> getWeigher() {
    return weigher;
  }
  public void setWeigher( CustomizationSupplier<? extends Weigher<K, V>> v) {
    weigher = v;
  }
  public boolean isBoostConcurrency() {
    return boostConcurrency;
  }
  public void setBoostConcurrency(boolean v) {
    boostConcurrency = v;
  }
  public boolean isDisableMonitoring() {
    return disableMonitoring;
  }
  public void setDisableMonitoring(boolean disableMonitoring) {
    this.disableMonitoring = disableMonitoring;
  }
  private  Duration durationCheckAndSanitize( Duration v) {
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
  public Cache2kBuilder<K, V> builder() {
    return Cache2kBuilder.of(this);
  }
}
