package org.cache2k.config;
public interface DataCustomizationSupplier<K, V, T extends Customization<K, V>>
  extends CustomizationSupplier<T> {
}
