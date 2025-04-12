package org.cache2k;
public class Cache2kBuilder<K, V>
  implements ConfigBuilder<Cache2kBuilder<K, V>, Cache2kConfig<K, V>>, DataAware<K, V> {
  private static final String MSG_NO_TYPES =
    "Use Cache2kBuilder.forUnknownTypes(), to construct a builder with no key and value types";
  public static Cache2kBuilder<Object, Object> forUnknownTypes() {
    return new Cache2kBuilder(null, null);
  }
  public static <K, V> Cache2kBuilder<K, V> of(Class<K> keyType, Class<V> valueType) {
    return new Cache2kBuilder<K, V>(CacheType.of(keyType), CacheType.of(valueType));
  }
  public static <K, V> Cache2kBuilder<K, V> of(Cache2kConfig<K, V> c) {
    Cache2kBuilder<K, V> cb = new Cache2kBuilder<K, V>(c);
    return cb;
  }
  private  CacheType<K> keyType;
  private  CacheType<V> valueType;
  private  Cache2kConfig<K, V> config = null;
  private  CacheManager manager = null;
  private Cache2kBuilder(Cache2kConfig<K, V> cfg) {
    withConfig(cfg);
  }
  protected Cache2kBuilder() {
    Type t = this.getClass().getGenericSuperclass();
    if (!(t instanceof ParameterizedType)) {
      throw new IllegalArgumentException(MSG_NO_TYPES);
    }
    Type[] types = ((ParameterizedType) t).getActualTypeArguments();
    keyType = (CacheType<K>) CacheType.of(types[0]);
    valueType = (CacheType<V>) CacheType.of(types[1]);
    if (Object.class.equals(keyType.getType()) &&
      Object.class.equals(valueType.getType())) {
      throw new IllegalArgumentException(MSG_NO_TYPES);
    }
  }
  private Cache2kBuilder( CacheType<K> keyType,  CacheType<V> valueType) {
    this.keyType = keyType;
    this.valueType = valueType;
  }
  private void withConfig(Cache2kConfig<K, V> cfg) {
    config = cfg;
  }
  private Cache2kConfig<K, V> cfg() {
    if (config == null) {
      config = CacheManager.PROVIDER.getDefaultConfig(getManager());
      if (keyType != null) {
        config.setKeyType(keyType);
      }
      if (valueType != null) {
        config.setValueType(valueType);
      }
    }
    return config;
  }
  public final Cache2kBuilder<K, V> manager(CacheManager manager) {
    if (this.manager != null) {
      throw new IllegalStateException("manager() must be first operation on builder.");
    }
    this.manager = manager;
    return this;
  }
  public final <K2> Cache2kBuilder<K2, V> keyType(Class<K2> t) {
    Cache2kBuilder<K2, V> me = (Cache2kBuilder<K2, V>) this;
    me.cfg().setKeyType(CacheType.of(t));
    return me;
  }
  public final <V2> Cache2kBuilder<K, V2> valueType(Class<V2> t) {
    Cache2kBuilder<K, V2> me = (Cache2kBuilder<K, V2>) this;
    me.cfg().setValueType(CacheType.of(t));
    return me;
  }
  public final <K2> Cache2kBuilder<K2, V> keyType(CacheType<K2> t) {
    Cache2kBuilder<K2, V> me = (Cache2kBuilder<K2, V>) this;
    me.cfg().setKeyType(t);
    return me;
  }
  public final <V2> Cache2kBuilder<K, V2> valueType(CacheType<V2> t) {
    Cache2kBuilder<K, V2> me = (Cache2kBuilder<K, V2>) this;
    me.cfg().setValueType(t);
    return me;
  }
  public final Cache2kBuilder<K, V> name(String uniqueName, Class<?> clazz, String fieldName) {
    if (fieldName == null) {
      throw new NullPointerException();
    }
    if (uniqueName == null) {
      return name(clazz, fieldName);
    }
    cfg().setName(uniqueName + '~' + clazz.getName() + "." + fieldName);
    return this;
  }
  public final Cache2kBuilder<K, V> name(Class<?> clazz, String fieldName) {
    if (fieldName == null) {
      throw new NullPointerException();
    }
    cfg().setName(clazz.getName() + "." + fieldName);
    return this;
  }
  public final Cache2kBuilder<K, V> name(Class<?> clazz) {
    cfg().setName(clazz.getName());
    return this;
  }
  public final Cache2kBuilder<K, V> name(String v) {
    cfg().setName(v);
    return this;
  }
  public final Cache2kBuilder<K, V> keepDataAfterExpired(boolean v) {
    cfg().setKeepDataAfterExpired(v);
    return this;
  }
  public final Cache2kBuilder<K, V> entryCapacity(long v) {
    cfg().setEntryCapacity(v);
    return this;
  }
  public final Cache2kBuilder<K, V> eternal(boolean v) {
    cfg().setEternal(v);
    return this;
  }
  public final Cache2kBuilder<K, V> expireAfterWrite(long v, TimeUnit u) {
    cfg().setExpireAfterWrite(toDuration(v, u));
    return this;
  }
  public final Cache2kBuilder<K, V> timerLag(long v, TimeUnit u) {
    cfg().setTimerLag(toDuration(v, u));
    return this;
  }
  private static Duration toDuration(long v, TimeUnit u) {
    return Duration.ofMillis(u.toMillis(v));
  }
  public final Cache2kBuilder<K, V> exceptionPropagator(
    final org.cache2k.integration.ExceptionPropagator<K> ep) {
    checkNull(ep);
    ExceptionPropagator<K, V> newPropagator = new ExceptionPropagator<K, V>() {
      @Override
      public RuntimeException propagateException(LoadExceptionInfo<K, V> newInfo) {
        org.cache2k.integration.ExceptionInformation oldInfo =
          new org.cache2k.integration.ExceptionInformation() {
          @Override
          public org.cache2k.integration.ExceptionPropagator getExceptionPropagator() {
            return ep;
          }
          @Override
          public Throwable getException() {
            return newInfo.getException();
          }
          @Override
          public int getRetryCount() {
            return newInfo.getRetryCount();
          }
          @Override
          public long getSinceTime() {
            return newInfo.getSinceTime();
          }
          @Override
          public long getLoadTime() {
            return newInfo.getLoadTime();
          }
          @Override
          public long getUntil() {
            return newInfo.getUntil();
          }
        };
        return ep.propagateException(newInfo.getKey(), oldInfo);
      }
    };
    exceptionPropagator(newPropagator);
    return this;
  }
  public final Cache2kBuilder<K, V> exceptionPropagator(ExceptionPropagator<K, V> ep) {
    cfg().setExceptionPropagator(wrapCustomizationInstance(ep));
    return this;
  }
  private static <T> CustomizationReferenceSupplier<T> wrapCustomizationInstance(T obj) {
    return new CustomizationReferenceSupplier<T>(obj);
  }
  @Deprecated
  public final Cache2kBuilder<K, V> loader(
    final org.cache2k.integration.AsyncCacheLoader<K, V> oldLoader) {
    checkNull(oldLoader);
    AsyncCacheLoader<K, V> newLoader = new AsyncCacheLoader<K, V>() {
      @Override
      public void load(K key, Context<K, V> context, Callback<V> callback) throws Exception {
        org.cache2k.integration.AsyncCacheLoader.Context<K, V> oldContext =
          new org.cache2k.integration.AsyncCacheLoader.Context<K, V>() {
            @Override
            public long getLoadStartTime() {
              return context.getStartTime();
            }
            @Override
            public K getKey() {
              return context.getKey();
            }
            @Override
            public Executor getExecutor() {
              return context.getExecutor();
            }
            @Override
            public Executor getLoaderExecutor() {
              return context.getLoaderExecutor();
            }
            @Override
            public CacheEntry<K, V> getCurrentEntry() {
              return context.getCurrentEntry();
            }
          };
        org.cache2k.integration.AsyncCacheLoader.Callback<V> oldCallback =
          new org.cache2k.integration.AsyncCacheLoader.Callback<V>() {
          @Override
          public void onLoadSuccess(V value) {
            callback.onLoadSuccess(value);
          }
          @Override
          public void onLoadFailure(Throwable t) {
            callback.onLoadFailure(t);
          }
        };
        oldLoader.load(key, oldContext, oldCallback);
      }
    };
    this.loader(newLoader);
    return this;
  }
  public final Cache2kBuilder<K, V> loader(CacheLoader<K, V> l) {
    cfg().setLoader(wrapCustomizationInstance(l));
    return this;
  }
  public final Cache2kBuilder<K, V> loader(AdvancedCacheLoader<K, V> l) {
    cfg().setAdvancedLoader(wrapCustomizationInstance(l));
    return this;
  }
  public final Cache2kBuilder<K, V> loader(AsyncCacheLoader<K, V> l) {
    cfg().setAsyncLoader(wrapCustomizationInstance(l));
    return this;
  }
  public final Cache2kBuilder<K, V> wrappingLoader(AdvancedCacheLoader<K, LoadDetail<V>> l) {
    cfg().setAdvancedLoader((
      CustomizationSupplier<AdvancedCacheLoader<K, V>>) (Object) wrapCustomizationInstance(l));
    return this;
  }
  public final Cache2kBuilder<K, V> wrappingLoader(CacheLoader<K, LoadDetail<V>> l) {
    cfg().setAdvancedLoader((
      CustomizationSupplier<AdvancedCacheLoader<K, V>>) (Object) wrapCustomizationInstance(l));
    return this;
  }
  public final Cache2kBuilder<K, V> writer(CacheWriter<K, V> w) {
    cfg().setWriter(wrapCustomizationInstance(w));
    return this;
  }
  public final Cache2kBuilder<K, V> addCacheClosedListener(CacheClosedListener listener) {
    cfg().getLifecycleListeners().add(wrapCustomizationInstance(listener));
    return this;
  }
  public final Cache2kBuilder<K, V> addListener(CacheEntryOperationListener<K, V> listener) {
    cfg().getListeners().add(wrapCustomizationInstance(listener));
    return this;
  }
  public final Cache2kBuilder<K, V> addAsyncListener(CacheEntryOperationListener<K, V> listener) {
    cfg().getAsyncListeners().add(wrapCustomizationInstance(listener));
    return this;
  }
  public final Cache2kBuilder<K, V> expiryPolicy(ExpiryPolicy<K, V> c) {
    cfg().setExpiryPolicy(wrapCustomizationInstance(c));
    return this;
  }
  public final Cache2kBuilder<K, V> refreshAhead(boolean f) {
    cfg().setRefreshAhead(f);
    return this;
  }
  public final Cache2kBuilder<K, V> sharpExpiry(boolean f) {
    cfg().setSharpExpiry(f);
    return this;
  }
  public final Cache2kBuilder<K, V> loaderThreadCount(int v) {
    cfg().setLoaderThreadCount(v);
    return this;
  }
  public final Cache2kBuilder<K, V> storeByReference(boolean v) {
    cfg().setStoreByReference(v);
    return this;
  }
  public final Cache2kBuilder<K, V> resiliencePolicy(ResiliencePolicy<K, V> v) {
    cfg().setResiliencePolicy(wrapCustomizationInstance(v));
    return this;
  }
  public final Cache2kBuilder<K, V> resiliencePolicy(
    CustomizationSupplier<? extends ResiliencePolicy<K, V>> v) {
    cfg().setResiliencePolicy(v);
    return this;
  }
  public final Cache2kBuilder<K, V> strictEviction(boolean flag) {
    cfg().setStrictEviction(flag);
    return this;
  }
  public final Cache2kBuilder<K, V> permitNullValues(boolean flag) {
    cfg().setPermitNullValues(flag);
    return this;
  }
  public final Cache2kBuilder<K, V> disableStatistics(boolean flag) {
    cfg().setDisableStatistics(flag);
    return this;
  }
  public final Cache2kBuilder<K, V> recordModificationTime(boolean flag) {
    cfg().setRecordModificationTime(flag);
    return this;
  }
  public final Cache2kBuilder<K, V> boostConcurrency(boolean f) {
    cfg().setBoostConcurrency(f);
    return this;
  }
  public final Cache2kBuilder<K, V> disableMonitoring(boolean f) {
    cfg().setDisableMonitoring(f);
    return this;
  }
  public final Cache2kBuilder<K, V> loaderExecutor(Executor v) {
    cfg().setLoaderExecutor(new CustomizationReferenceSupplier<Executor>(v));
    return this;
  }
  public final Cache2kBuilder<K, V> refreshExecutor(Executor v) {
    cfg().setRefreshExecutor(new CustomizationReferenceSupplier<Executor>(v));
    return this;
  }
  public final Cache2kBuilder<K, V> executor(Executor v) {
    cfg().setExecutor(new CustomizationReferenceSupplier<Executor>(v));
    return this;
  }
  public final Cache2kBuilder<K, V> asyncListenerExecutor(Executor v) {
    cfg().setAsyncListenerExecutor(new CustomizationReferenceSupplier<Executor>(v));
    return this;
  }
  public final Cache2kBuilder<K, V> weigher(Weigher<K, V> v) {
    cfg().setWeigher(new CustomizationReferenceSupplier<Weigher<K, V>>(v));
    return this;
  }
  public final Cache2kBuilder<K, V> maximumWeight(long v) {
    cfg().setMaximumWeight(v);
    return this;
  }
  public final Cache2kBuilder<K, V> setup(Consumer<Cache2kBuilder<K, V>> consumer) {
    consumer.accept(this);
    return this;
  }
  public final Cache2kBuilder<K, V> enable(Class<? extends ToggleFeature> feature) {
    ToggleFeature.enable(this, feature);
    return this;
  }
  public final Cache2kBuilder<K, V> disable(Class<? extends ToggleFeature> feature) {
    ToggleFeature.disable(this, feature);
    return this;
  }
  public final <B extends SectionBuilder<B, CFG>, CFG extends ConfigSection<CFG, B>>
    Cache2kBuilder<K, V> with(Class<CFG> configSectionClass, Consumer<B> builderAction) {
    CFG section =
      cfg().getSections().getSection(configSectionClass);
    if (section == null) {
      try {
        section = configSectionClass.getConstructor().newInstance();
      } catch (Exception e) {
        throw new Error("config bean needs working default constructor", e);
      }
      cfg().getSections().add(section);
    }
    builderAction.accept(section.builder());
    return this;
  }
  public Cache2kBuilder<K, V> set(Consumer<Cache2kConfig<K, V>> configAction) {
    configAction.accept(config());
    return this;
  }
  public <B extends SectionBuilder<B, CFG>,
    CFG extends ConfigSection<CFG, B>,
    SUP extends WithSection<CFG, B> & CustomizationSupplier<?>>  Cache2kBuilder<K, V> setupWith(
    Function<Cache2kBuilder<K, V>, SUP> setupAction,
    Consumer<B> builderAction) {
    with(setupAction.apply(this).getConfigClass(), builderAction);
    return this;
  }
  public <B extends ConfigBuilder<B, CFG>,
          CFG extends ConfigBean<CFG, B>>
  Cache2kBuilder<K, V> setup(Function<Cache2kBuilder<K, V>, CFG> enabler,
                             Consumer<B> builderAction) {
    builderAction.accept(enabler.apply(this).builder());
    return this;
  }
  public <B extends ConfigBuilder<B, T>, T extends ToggleFeature & ConfigBean<T, B>>
    Cache2kBuilder<K, V> enable(Class<T> featureType, Consumer<B> builderAction) {
    T bean = ToggleFeature.enable(this, featureType);
    builderAction.accept(bean.builder());
    return this;
  }
  public <B extends SectionBuilder<B, CFG>, T extends ToggleFeature & WithSection<CFG, B>,
    CFG extends ConfigSection<CFG, B>>  Cache2kBuilder<K, V> enableWith(
    Class<T> featureType, Consumer<B> builderAction) {
    T bean = ToggleFeature.enable(this, featureType);
    with(bean.getConfigClass(), builderAction);
    return this;
  }
  @Override
  public final Cache2kConfig<K, V> config() {
    return cfg();
  }
  public final CacheManager getManager() {
    if (manager == null) {
      manager = CacheManager.getInstance();
    }
    return manager;
  }
  public final Cache<K, V> build() {
    return CacheManager.PROVIDER.createCache(getManager(), cfg());
  }
}
