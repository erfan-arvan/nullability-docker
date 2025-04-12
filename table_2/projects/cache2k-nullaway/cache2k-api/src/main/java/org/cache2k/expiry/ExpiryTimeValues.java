package org.cache2k.expiry;
public interface ExpiryTimeValues {
  long NEUTRAL = Cache2kConfig.UNSET_LONG;
  @Deprecated
  long NO_CACHE = 0;
  long NOW = 0;
  long REFRESH = 1;
  long ETERNAL = Long.MAX_VALUE;
}
