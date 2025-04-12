package org.cache2k.io;
@FunctionalInterface
public interface ExceptionPropagator<K, V> extends DataAwareCustomization<K, V> {
  RuntimeException propagateException(LoadExceptionInfo<K, V> loadExceptionInfo);
}
