package org.cache2k.config;
public final class CustomizationSupplierByClassName<T>
  implements CustomizationSupplier<T>, ValidatingConfigBean {
  private  String className;
  public CustomizationSupplierByClassName() { }
  public CustomizationSupplierByClassName(String className) {
    if (className == null) {
      throw new NullPointerException("className");
    }
    this.className = className;
  }
  public  String getClassName() {
    return className;
  }
  public void setClassName(String v) {
    className = v;
  }
  @Override
  public void validate() {
    if (className == null) {
      throw new IllegalArgumentException("className not set");
    }
  }
  @Override
  public ConfigBuilder builder() {
    throw new UnsupportedOperationException();
  }
  @Override
  public T supply(CacheBuildContext<?, ?> ctx) {
    try {
      return (T) ctx.getCacheManager().getClassLoader()
        .loadClass(className).getConstructor().newInstance();
    } catch (Exception e) {
      throw new LinkageError("error loading customization class", e);
    }
  }
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof CustomizationSupplierByClassName)) return false;
    CustomizationSupplierByClassName<?> that = (CustomizationSupplierByClassName<?>) other;
    if (className == null) {
      return that.className == null;
    }
    return className.equals(that.className);
  }
  @Override
  public int hashCode() {
    if (className != null) {
      return className.hashCode();
    }
    return 0;
  }
}
