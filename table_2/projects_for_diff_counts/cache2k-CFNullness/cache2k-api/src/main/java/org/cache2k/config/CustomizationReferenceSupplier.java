package org.cache2k.config;
public final class CustomizationReferenceSupplier< T> implements CustomizationSupplier<T> {
  private final  T object;
  public CustomizationReferenceSupplier( T obj) {
    Objects.requireNonNull(obj);
    object = obj;
  }
  @Override
  public T supply(CacheBuildContext ignored) {
    return object;
  }
  @Override
  public boolean equals( Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CustomizationReferenceSupplier)) {
      return false;
    }
    CustomizationReferenceSupplier<?> obj = (CustomizationReferenceSupplier<?>) other;
    return object.equals(obj.object);
  }
  @Override
  public int hashCode() {
    return object.hashCode();
  }
}
