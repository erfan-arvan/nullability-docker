package com.google.cloud.tools.jib.image.json;
public class JsonToImageTranslator {
  public static Image toImage(V21ManifestTemplate manifestTemplate)
      throws LayerPropertyNotFoundException {
    Image image = new Image();
    for (DescriptorDigest digest : manifestTemplate.getLayerDigests()) {
      Layer layer = new DigestOnlyLayer(digest);
      image.addLayer(layer);
    }
    return image;
  }
  public static Image toImage(
      BuildableManifestTemplate manifestTemplate,
      ContainerConfigurationTemplate containerConfigurationTemplate)
      throws LayerCountMismatchException, LayerPropertyNotFoundException {
    Image image = new Image();
    List<ReferenceNoDiffIdLayer> layers = new ArrayList<>();
    for (BuildableManifestTemplate.ContentDescriptorTemplate layerObjectTemplate :
        manifestTemplate.getLayers()) {
      if (layerObjectTemplate.getDigest() == null) {
        throw new IllegalArgumentException(
            "All layers in the manifest template must have digest set");
      }
      layers.add(
          new ReferenceNoDiffIdLayer(
              new BlobDescriptor(layerObjectTemplate.getSize(), layerObjectTemplate.getDigest())));
    }
    List<DescriptorDigest> diffIds = containerConfigurationTemplate.getDiffIds();
    if (layers.size() != diffIds.size()) {
      throw new LayerCountMismatchException(
          "Mismatch between image manifest and container configuration");
    }
    for (int layerIndex = 0; layerIndex < layers.size(); layerIndex++) {
      ReferenceNoDiffIdLayer noDiffIdLayer = layers.get(layerIndex);
      DescriptorDigest diffId = diffIds.get(layerIndex);
      Layer layer = new ReferenceLayer(noDiffIdLayer.getBlobDescriptor(), diffId);
      image.addLayer(layer);
    }
    if (containerConfigurationTemplate.getContainerEntrypoint() == null) {
      throw new IllegalArgumentException("containerConfigurationTemplate must have an entrypoint");
    }
    image.setEntrypoint(containerConfigurationTemplate.getContainerEntrypoint());
    for (String environmentVariable : containerConfigurationTemplate.getContainerEnvironment()) {
      image.addEnvironmentVariableDefinition(environmentVariable);
    }
    return image;
  }
  private JsonToImageTranslator() {}
}
