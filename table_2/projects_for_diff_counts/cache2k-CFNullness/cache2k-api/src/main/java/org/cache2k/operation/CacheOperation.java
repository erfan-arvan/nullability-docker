package org.cache2k.operation;
public interface CacheOperation {
  void clear();
  void close();
  void destroy();
  void changeCapacity(long entryCountOrWeight);
}
