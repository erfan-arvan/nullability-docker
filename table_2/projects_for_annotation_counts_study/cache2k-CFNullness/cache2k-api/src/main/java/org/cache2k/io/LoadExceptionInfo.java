package org.cache2k.io;
public interface LoadExceptionInfo<K, V> extends CacheEntry<K, V> {
  @Override
  K getKey();
  @Override
  default V getValue() {
    throw generateExceptionToPropagate();
  }
  @Override
  Throwable getException();
  @Override
  default LoadExceptionInfo<K, V> getExceptionInfo() { return this; }
  default RuntimeException generateExceptionToPropagate() {
    return getExceptionPropagator().propagateException(this);
  }
  ExceptionPropagator<K, V> getExceptionPropagator();
  int getRetryCount();
  long getSinceTime();
  long getLoadTime();
  long getUntil();
}
