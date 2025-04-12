package org.cache2k.config;
public class SectionContainer extends AbstractCollection<ConfigSection>
  implements Collection<ConfigSection>, Serializable {
  private final Map<Class<? extends ConfigSection>, ConfigSection> class2section = new HashMap<>();
  @Override
  public boolean add(ConfigSection section) {
    if (getSection(section.getClass()) !=  null) {
      throw new IllegalArgumentException(
        "Section of same type already inserted: " + section.getClass().getName());
    }
    class2section.put(section.getClass(), section);
    return true;
  }
  public <T extends ConfigSection> T getSection(Class<T> sectionType, T defaultFallback) {
    ConfigSection section = class2section.get(sectionType);
    return section != null ? sectionType.cast(section) : defaultFallback;
  }
  public  <T extends ConfigSection> T getSection(Class<T> sectionType) {
    ConfigSection section = class2section.get(sectionType);
    return sectionType.cast(section);
  }
  @Override
  public Iterator<ConfigSection> iterator() {
    return class2section.values().iterator();
  }
  @Override
  public int size() {
    return class2section.size();
  }
  public String toString() {
    return getClass().getSimpleName() + class2section.values().toString();
  }
}
