package org.cache2k.config;
public interface CustomizationCollection<T> extends
  Collection<CustomizationSupplier<T>>, Serializable {
  @Override
  boolean add(CustomizationSupplier<T> e);
  @Override
  boolean addAll(Collection<? extends CustomizationSupplier<T>> c);
}
