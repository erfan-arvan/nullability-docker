package org.cache2k.io;
public interface LoadExceptionInfo<K> extends CacheEntry<K, Void> {
  @Override
  K getKey();
  @Override
  default Void getValue() {
    throw generateExceptionToPropagate();
  }
  @Override
  Throwable getException();
  @Override
  default LoadExceptionInfo<K> getExceptionInfo() { return this; }
  default RuntimeException generateExceptionToPropagate() {
    return getExceptionPropagator().propagateException(this);
  }
  ExceptionPropagator getExceptionPropagator();
  int getRetryCount();
  long getSinceTime();
  long getLoadTime();
  long getUntil();
}
