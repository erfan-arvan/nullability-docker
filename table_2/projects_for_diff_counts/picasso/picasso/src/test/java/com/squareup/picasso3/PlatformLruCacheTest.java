package com.squareup.picasso3;
@RunWith(RobolectricTestRunner.class)
public class PlatformLruCacheTest {
  private final Bitmap A = Bitmap.createBitmap(1, 1, ALPHA_8);
  private final Bitmap B = Bitmap.createBitmap(1, 1, ALPHA_8);
  private final Bitmap C = Bitmap.createBitmap(1, 1, ALPHA_8);
  private final Bitmap D = Bitmap.createBitmap(1, 1, ALPHA_8);
  private final Bitmap E = Bitmap.createBitmap(1, 1, ALPHA_8);
  private int expectedPutCount;
  private int expectedHitCount;
  private int expectedMissCount;
  private int expectedEvictionCount;
  @Test public void testStatistics() {
    PlatformLruCache cache = new PlatformLruCache(3);
    assertStatistics(cache);
    cache.set("a", A);
    expectedPutCount++;
    assertStatistics(cache);
    assertHit(cache, "a", A);
    cache.set("b", B);
    expectedPutCount++;
    assertStatistics(cache);
    assertHit(cache, "a", A);
    assertHit(cache, "b", B);
    assertSnapshot(cache, "a", A, "b", B);
    cache.set("c", C);
    expectedPutCount++;
    assertStatistics(cache);
    assertHit(cache, "a", A);
    assertHit(cache, "b", B);
    assertHit(cache, "c", C);
    assertSnapshot(cache, "a", A, "b", B, "c", C);
    cache.set("d", D);
    expectedPutCount++;
    expectedEvictionCount++; 
    assertStatistics(cache);
    assertMiss(cache, "a");
    assertHit(cache, "b", B);
    assertHit(cache, "c", C);
    assertHit(cache, "d", D);
    assertHit(cache, "b", B);
    assertHit(cache, "c", C);
    assertSnapshot(cache, "d", D, "b", B, "c", C);
    cache.set("e", E);
    expectedPutCount++;
    expectedEvictionCount++; 
    assertStatistics(cache);
    assertMiss(cache, "d");
    assertMiss(cache, "a");
    assertHit(cache, "e", E);
    assertHit(cache, "b", B);
    assertHit(cache, "c", C);
    assertSnapshot(cache, "e", E, "b", B, "c", C);
  }
  @Test public void cannotPutNullKey() {
    PlatformLruCache cache = new PlatformLruCache(3);
    try {
      cache.set(null, A);
      fail();
    } catch (NullPointerException expected) {
    }
  }
  @Test public void cannotPutNullValue() {
    PlatformLruCache cache = new PlatformLruCache(3);
    try {
      cache.set("a", null);
      fail();
    } catch (NullPointerException expected) {
    }
  }
  @Test public void evictionWithSingletonCache() {
    PlatformLruCache cache = new PlatformLruCache(1);
    cache.set("a", A);
    cache.set("b", B);
    assertSnapshot(cache, "b", B);
  }
  @Test public void throwsWithNullKey() {
    PlatformLruCache cache = new PlatformLruCache(1);
    try {
      cache.get(null);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
    }
  }
  @Test public void putCauseEviction() {
    PlatformLruCache cache = new PlatformLruCache(3);
    cache.set("a", A);
    cache.set("b", B);
    cache.set("c", C);
    cache.set("b", D);
    assertSnapshot(cache, "a", A, "c", C, "b", D);
  }
  @Test public void evictAll() {
    PlatformLruCache cache = new PlatformLruCache(4);
    cache.set("a", A);
    cache.set("b", B);
    cache.set("c", C);
    cache.clear();
    assertThat(cache.cache.snapshot()).isEmpty();
  }
  @Test public void clearPrefixedKey() {
    PlatformLruCache cache = new PlatformLruCache(3);
    cache.set("Hello\nAlice!", A);
    cache.set("Hello\nBob!", B);
    cache.set("Hello\nEve!", C);
    cache.set("Hellos\nWorld!", D);
    cache.clearKeyUri("Hello");
    assertThat(cache.cache.snapshot()).hasSize(1);
    assertThat(cache.cache.snapshot()).containsKey("Hellos\nWorld!");
  }
  @Test public void invalidate() {
    PlatformLruCache cache = new PlatformLruCache(3);
    cache.set("Hello\nAlice!", A);
    assertThat(cache.size()).isEqualTo(1);
    cache.clearKeyUri("Hello");
    assertThat(cache.size()).isEqualTo(0);
  }
  @Test public void overMaxSizeDoesNotClear() {
    PlatformLruCache cache = new PlatformLruCache(16);
    Bitmap size4 = Bitmap.createBitmap(2, 2, ALPHA_8);
    Bitmap size16 = Bitmap.createBitmap(4, 4, ALPHA_8);
    Bitmap size25 = Bitmap.createBitmap(5, 5, ALPHA_8);
    cache.set("4", size4);
    expectedPutCount++;
    assertHit(cache, "4", size4);
    cache.set("16", size16);
    expectedPutCount++;
    expectedEvictionCount++; 
    assertMiss(cache, "4");
    assertHit(cache, "16", size16);
    cache.set("25", size25);
    assertHit(cache, "16", size16);
    assertMiss(cache, "25");
    assertThat(cache.size()).isEqualTo(16);
  }
  @Test public void overMaxSizeRemovesExisting() {
    PlatformLruCache cache = new PlatformLruCache(20);
    Bitmap size4 = Bitmap.createBitmap(2, 2, ALPHA_8);
    Bitmap size16 = Bitmap.createBitmap(4, 4, ALPHA_8);
    Bitmap size25 = Bitmap.createBitmap(5, 5, ALPHA_8);
    cache.set("small", size4);
    expectedPutCount++;
    assertHit(cache, "small", size4);
    cache.set("big", size16);
    expectedPutCount++;
    assertHit(cache, "small", size4);
    assertHit(cache, "big", size16);
    cache.set("big", size25);
    assertHit(cache, "small", size4);
    assertMiss(cache, "big");
    assertThat(cache.size()).isEqualTo(4);
  }
  private void assertHit(PlatformLruCache cache, String key, Bitmap value) {
    assertThat(cache.get(key)).isEqualTo(value);
    expectedHitCount++;
    assertStatistics(cache);
  }
  private void assertMiss(PlatformLruCache cache, String key) {
    assertThat(cache.get(key)).isNull();
    expectedMissCount++;
    assertStatistics(cache);
  }
  private void assertStatistics(PlatformLruCache cache) {
    assertThat(cache.putCount()).isEqualTo(expectedPutCount);
    assertThat(cache.hitCount()).isEqualTo(expectedHitCount);
    assertThat(cache.missCount()).isEqualTo(expectedMissCount);
    assertThat(cache.evictionCount()).isEqualTo(expectedEvictionCount);
  }
  private void assertSnapshot(PlatformLruCache cache, Object... keysAndValues) {
    List<Object> actualKeysAndValues = new ArrayList<>();
    for (Map.Entry<String, PlatformLruCache.BitmapAndSize> entry : cache.cache.snapshot().entrySet()) {
      actualKeysAndValues.add(entry.getKey());
      actualKeysAndValues.add(entry.getValue().bitmap);
    }
    assertThat(actualKeysAndValues).isEqualTo(Arrays.asList(keysAndValues));
  }
}
