package org.cache2k.integration;
@Deprecated
public abstract class LoadDetail<V> {
  private Object value;
  public LoadDetail(final Object valueOrWrapper) {
    value = valueOrWrapper;
  }
  public V getValue() {
    if (value instanceof LoadDetail) {
      return ((LoadDetail<V>) value).getValue();
    }
    return (V) value;
  }
  public  LoadDetail<V> getNextInChain() {
    if (value instanceof LoadDetail) {
      return ((LoadDetail<V>) value);
    }
    return null;
  }
}
