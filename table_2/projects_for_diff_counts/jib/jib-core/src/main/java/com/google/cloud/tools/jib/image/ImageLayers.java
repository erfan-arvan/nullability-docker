package com.google.cloud.tools.jib.image;
public class ImageLayers<T extends Layer> implements Iterable<T> {
  private final List<T> layers = new ArrayList<>();
  private final Set<DescriptorDigest> layerDigests = new HashSet<>();
  public List<T> getLayers() {
    return Collections.unmodifiableList(layers);
  }
  public int size() {
    return layers.size();
  }
  public boolean isEmpty() {
    return layers.isEmpty();
  }
  public T get(int index) {
    return layers.get(index);
  }
  public T get(DescriptorDigest digest) throws LayerPropertyNotFoundException {
    if (!has(digest)) {
      return null;
    }
    for (T layer : layers) {
      if (layer.getBlobDescriptor().getDigest().equals(digest)) {
        return layer;
      }
    }
    throw new IllegalStateException("Layer digest exists but layer not found");
  }
  public boolean has(DescriptorDigest digest) {
    return layerDigests.contains(digest);
  }
  public ImageLayers<T> add(T layer) throws LayerPropertyNotFoundException {
    if (!isSameAsLastLayer(layer)) {
      layerDigests.add(layer.getBlobDescriptor().getDigest());
      layers.add(layer);
    }
    return this;
  }
  public <U extends T> ImageLayers<T> addAll(ImageLayers<U> layers)
      throws LayerPropertyNotFoundException {
    for (U layer : layers) {
      add(layer);
    }
    return this;
  }
  @Override
  public Iterator<T> iterator() {
    return getLayers().iterator();
  }
  private boolean isSameAsLastLayer(T layer) throws LayerPropertyNotFoundException {
    if (layers.size() == 0) {
      return false;
    }
    T lastLayer = Iterables.getLast(layers);
    return layer.getBlobDescriptor().getDigest().equals(lastLayer.getBlobDescriptor().getDigest());
  }
}
