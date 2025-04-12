package org.cache2k.config;
public interface ConfigBuilder
  <SELF extends ConfigBuilder<SELF, T>, T extends ConfigBean<T, SELF>> {
  T config();
}
