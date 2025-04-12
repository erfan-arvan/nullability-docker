package org.cache2k.integration;
@Deprecated
public interface ExceptionPropagator<K> {
  RuntimeException propagateException(K key, ExceptionInformation exceptionInformation);
}
