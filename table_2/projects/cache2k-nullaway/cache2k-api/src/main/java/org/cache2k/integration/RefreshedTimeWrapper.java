package org.cache2k.integration;
@Deprecated
public final class RefreshedTimeWrapper<V> extends LoadDetail<V> {
  private final long refreshTime;
  public RefreshedTimeWrapper(final Object value, final long refreshTime) {
    super(value);
    this.refreshTime = refreshTime;
  }
  public long getRefreshTime() {
    return refreshTime;
  }
}
