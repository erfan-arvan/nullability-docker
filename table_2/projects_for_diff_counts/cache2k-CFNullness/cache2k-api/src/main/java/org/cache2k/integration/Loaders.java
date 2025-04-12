package org.cache2k.integration;
@Deprecated
public class Loaders {
  public static < V> LoadDetail<V> wrapRefreshedTime( V value, long refreshedTimeInMillis) {
    return new RefreshedTimeWrapper<V>(value, refreshedTimeInMillis);
  }
  public static <V> LoadDetail<V> wrapRefreshedTime(
    LoadDetail<V> value, long refreshedTimeInMillis) {
    return new RefreshedTimeWrapper<V>(value, refreshedTimeInMillis);
  }
}
