package org.cache2k.config;
public interface WithSection
    <CFG extends ConfigSection<CFG, B>,
    B extends SectionBuilder<B, CFG>> {
  Class<CFG> getConfigClass();
}
