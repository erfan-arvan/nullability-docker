package org.cache2k.expiry;
public class Expiry implements ExpiryTimeValues {
  public static long toSharpTime(long millis) {
    if (millis == ETERNAL) {
      return ETERNAL;
    }
    if (millis < 0) {
      return millis;
    }
    return -millis;
  }
  public static long earliestTime(long loadTime, long candidate1, long candidate2) {
    if (candidate1 >= loadTime) {
      if (candidate1 < candidate2 || candidate2 < loadTime) {
        return candidate1;
      }
    }
    if (candidate2 >= loadTime) {
      return candidate2;
    }
    return ETERNAL;
  }
  public static long mixTimeSpanAndPointInTime(long loadTime, long refreshAfter, long pointInTime) {
    long refreshTime = loadTime + refreshAfter;
    if (refreshTime < 0) {
      refreshTime = ETERNAL;
    }
    if (pointInTime == ETERNAL) {
      return refreshTime;
    }
    if (pointInTime > refreshTime) {
      return refreshTime;
    }
    long absPointInTime = Math.abs(pointInTime);
    if (absPointInTime <= refreshTime) {
      return pointInTime;
    }
    long pointInTimeMinusDelta = absPointInTime - refreshAfter;
    if (pointInTimeMinusDelta < refreshTime) {
      return pointInTimeMinusDelta;
    }
    return refreshTime;
  }
}
