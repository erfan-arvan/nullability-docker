package org.cache2k.config;
public interface Feature {
  void enlist(CacheBuildContext<?, ?> ctx);
}
