package org.cache2k.config;
public interface DataCustomizationSupplier<K, V, T extends DataAwareCustomization<K, V>>
  extends CustomizationSupplier<T> {
}
