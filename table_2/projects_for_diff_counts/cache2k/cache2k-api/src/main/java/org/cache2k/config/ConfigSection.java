package org.cache2k.config;
public interface ConfigSection
  <SELF extends ConfigSection<SELF, B>, B extends SectionBuilder<B, SELF>>
  extends ConfigBean<SELF, B> {
  B builder();
}
