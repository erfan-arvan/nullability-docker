package org.cache2k.config;
public interface SectionBuilder
  <SELF extends SectionBuilder<SELF, T>,
    T extends ConfigSection<T, SELF>> extends ConfigBuilder<SELF, T> {
}
