package org.cache2k.operation;
public interface CacheInfo {
  static CacheInfo of(Cache<?, ?> cache) { return cache.requestInterface(CacheInfo.class); }
  String getName();
  String getManagerName();
  String getKeyType();
  String getValueType();
  long getSize();
  long getEntryCapacity();
  long getMaximumWeight();
  long getTotalWeight();
  long getCapacityLimit();
  String getImplementation();
  boolean isLoaderPresent();
  boolean isWeigherPresent();
  boolean isStatisticsEnabled();
  Date getCreatedTime();
  Date getClearedTime();
}
