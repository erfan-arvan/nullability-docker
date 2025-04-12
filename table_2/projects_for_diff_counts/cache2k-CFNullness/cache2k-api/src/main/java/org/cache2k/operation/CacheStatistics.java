package org.cache2k.operation;
public interface CacheStatistics {
  long getInsertCount();
  long getGetCount();
  long getMissCount();
  long getLoadCount();
  long getLoadExceptionCount();
  long getSuppressedLoadExceptionCount();
  double getMillisPerLoad();
  long getTotalLoadMillis();
  long getRefreshCount();
  long getRefreshFailedCount();
  long getRefreshedHitCount();
  long getExpiredCount();
  long getEvictedCount();
  long getEvictedOrRemovedWeight();
  long getPutCount();
  long getRemoveCount();
  long getClearedCount();
  long getClearCallsCount();
  long getKeyMutationCount();
  double getHitRate();
}
