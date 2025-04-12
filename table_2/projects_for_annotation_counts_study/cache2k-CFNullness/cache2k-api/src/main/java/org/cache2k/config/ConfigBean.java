package org.cache2k.config;
public interface ConfigBean
  <SELF extends ConfigBean<SELF, B>, B extends ConfigBuilder<B, SELF>>
  extends BeanMarker {
  B builder();
}
