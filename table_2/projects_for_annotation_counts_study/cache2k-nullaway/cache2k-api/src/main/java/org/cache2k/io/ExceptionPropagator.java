package org.cache2k.io;
@FunctionalInterface
public interface ExceptionPropagator<K> extends Customization<K, Void> {
  RuntimeException propagateException(LoadExceptionInfo<K> loadExceptionInfo);
}
