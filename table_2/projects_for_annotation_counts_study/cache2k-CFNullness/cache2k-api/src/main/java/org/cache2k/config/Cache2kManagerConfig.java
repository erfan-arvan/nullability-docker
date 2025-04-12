package org.cache2k.config;
public class Cache2kManagerConfig
  implements ConfigBean<Cache2kManagerConfig, Cache2kManagerConfig.Builder> {
  private  String version = null;
  private  String defaultManagerName = null;
  private boolean ignoreMissingCacheConfiguration = false;
  private boolean skipCheckOnStartup = false;
  private boolean ignoreAnonymousCache = false;
  public boolean isIgnoreMissingCacheConfiguration() {
    return ignoreMissingCacheConfiguration;
  }
  public void setIgnoreMissingCacheConfiguration(boolean f) {
    ignoreMissingCacheConfiguration = f;
  }
  public  String getDefaultManagerName() {
    return defaultManagerName;
  }
  public void setDefaultManagerName(String v) {
    defaultManagerName = v;
  }
  public  String getVersion() {
    return version;
  }
  public void setVersion(String v) {
    version = v;
  }
  public boolean isSkipCheckOnStartup() {
    return skipCheckOnStartup;
  }
  public void setSkipCheckOnStartup(boolean f) {
    skipCheckOnStartup = f;
  }
  public boolean isIgnoreAnonymousCache() {
    return ignoreAnonymousCache;
  }
  public void setIgnoreAnonymousCache(boolean f) {
    ignoreAnonymousCache = f;
  }
  @Override
  public Builder builder() {
    throw new UnsupportedOperationException();
  }
  public static class Builder implements ConfigBuilder<Builder, Cache2kManagerConfig> {
    @Override
    public Cache2kManagerConfig config() {
      throw new UnsupportedOperationException();
    }
  }
}
