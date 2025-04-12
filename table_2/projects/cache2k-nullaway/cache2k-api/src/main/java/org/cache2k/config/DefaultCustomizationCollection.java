package org.cache2k.config;
public final class DefaultCustomizationCollection<T>
  extends AbstractCollection<CustomizationSupplier<T>>
  implements CustomizationCollection<T> {
  private Collection<CustomizationSupplier<T>> list = new ArrayList<CustomizationSupplier<T>>();
  @Override
  public int size() {
    return list.size();
  }
  @Override
  public Iterator<CustomizationSupplier<T>> iterator() {
    return list.iterator();
  }
  @Override
  public boolean add(final CustomizationSupplier<T> entry) {
    if (list.contains(entry)) {
      throw new IllegalArgumentException("duplicate entry");
    }
    return list.add(entry);
  }
  public String toString() {
    return getClass().getSimpleName() + list.toString();
  }
}
