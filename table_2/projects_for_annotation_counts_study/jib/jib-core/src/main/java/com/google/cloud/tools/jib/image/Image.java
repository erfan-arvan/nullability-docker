package com.google.cloud.tools.jib.image;
public class Image {
  private ImageLayers<Layer> layers = new ImageLayers<>();
  private final List<String> environment = new ArrayList<>();
   private List<String> entrypoint;
  public List<String> getEnvironment() {
    return Collections.unmodifiableList(environment);
  }
  public Image setEnvironment(Map<String, String> environment) {
    for (Map.Entry<String, String> environmentVariable : environment.entrySet()) {
      setEnvironmentVariable(environmentVariable.getKey(), environmentVariable.getValue());
    }
    return this;
  }
  public Image setEnvironmentVariable(String name, String value) {
    environment.add(name + "=" + value);
    return this;
  }
  public Image addEnvironmentVariableDefinition(String environmentVariableDefinition) {
    environment.add(environmentVariableDefinition);
    return this;
  }
  public List<String> getEntrypoint() {
    return Collections.unmodifiableList(entrypoint);
  }
  public Image setEntrypoint(List<String> entrypoint) {
    this.entrypoint = entrypoint;
    return this;
  }
  public List<Layer> getLayers() {
    return layers.getLayers();
  }
  public Image addLayer(Layer layer) throws LayerPropertyNotFoundException {
    layers.add(layer);
    return this;
  }
  public <T extends Layer> Image addLayers(ImageLayers<T> layers)
      throws LayerPropertyNotFoundException {
    this.layers.addAll(layers);
    return this;
  }
}
