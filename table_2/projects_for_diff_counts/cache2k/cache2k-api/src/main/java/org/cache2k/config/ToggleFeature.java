package org.cache2k.config;
public abstract class ToggleFeature implements SingleFeature {
  public static <T extends ToggleFeature> T enable(Cache2kBuilder<?, ?> builder,
                                                   Class<T> featureType) {
    try {
      T feature = featureType.getConstructor().newInstance();
      builder.config().getFeatures().add(feature);
      return feature;
    } catch (Exception e) {
      throw new LinkageError("Instantiation failed", e);
    }
  }
  public static void disable(Cache2kBuilder<?, ?> builder,
                            Class<? extends ToggleFeature> featureType) {
    Iterator<Feature> it = builder.config().getFeatures().iterator();
    while (it.hasNext()) {
      if (it.next().getClass().equals(featureType)) {
        it.remove();
      }
    }
  }
  public static <T extends ToggleFeature>  T extract(Cache2kBuilder<?, ?> builder,
                                                              Class<T> featureType) {
    Iterator<Feature> it = builder.config().getFeatures().iterator();
    while (it.hasNext()) {
      Feature feature = it.next();
      if (feature.getClass().equals(featureType)) {
        return (T) feature;
      }
    }
    return null;
  }
  public static boolean isEnabled(Cache2kBuilder<?, ?> builder,
                                  Class<? extends ToggleFeature> featureType) {
    ToggleFeature f = extract(builder, featureType);
    return f != null ? f.isEnabled() : false;
  }
  private boolean enabled = true;
  @Override
  public final void enlist(CacheBuildContext<?, ?> ctx) {
    if (enabled) {
      doEnlist(ctx);
    }
  }
  protected abstract void doEnlist(CacheBuildContext<?, ?> ctx);
  public final boolean isEnabled() {
    return enabled;
  }
  public final void setEnabled(boolean v) {
    this.enabled = v;
  }
  public final void setEnable(boolean v) {
    this.enabled = v;
  }
  @Override
  public final boolean equals(Object o) {
    return getClass().equals(o);
  }
  @Override
  public final int hashCode() {
    return getClass().hashCode();
  }
  @Override
  public String toString() {
    return getClass().getSimpleName() + '{' + (enabled ? "enabled" : "disabled") + '}';
  }
}
