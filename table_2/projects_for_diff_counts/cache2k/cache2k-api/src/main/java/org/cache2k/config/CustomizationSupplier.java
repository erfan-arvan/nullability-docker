package org.cache2k.config;
  @FunctionalInterface
public interface CustomizationSupplier<T> {
  T supply(CacheBuildContext<?, ?> buildContext);
}
