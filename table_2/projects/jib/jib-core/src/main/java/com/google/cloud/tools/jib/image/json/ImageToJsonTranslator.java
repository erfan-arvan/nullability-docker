package com.google.cloud.tools.jib.image.json;
public class ImageToJsonTranslator {
  private final Image image;
  public ImageToJsonTranslator(Image image) {
    this.image = image;
  }
  public Blob getContainerConfigurationBlob() throws LayerPropertyNotFoundException {
    ContainerConfigurationTemplate template = new ContainerConfigurationTemplate();
    for (Layer layer : image.getLayers()) {
      template.addLayerDiffId(layer.getDiffId());
    }
    template.setContainerEnvironment(image.getEnvironment());
    template.setContainerEntrypoint(image.getEntrypoint());
    return JsonTemplateMapper.toBlob(template);
  }
  public <T extends BuildableManifestTemplate> T getManifestTemplate(
      Class<T> manifestTemplateClass, BlobDescriptor containerConfigurationBlobDescriptor)
      throws LayerPropertyNotFoundException {
    try {
      T template = manifestTemplateClass.getDeclaredConstructor().newInstance();
      DescriptorDigest containerConfigurationDigest =
          containerConfigurationBlobDescriptor.getDigest();
      long containerConfigurationSize = containerConfigurationBlobDescriptor.getSize();
      template.setContainerConfiguration(containerConfigurationSize, containerConfigurationDigest);
      for (Layer layer : image.getLayers()) {
        template.addLayer(
            layer.getBlobDescriptor().getSize(), layer.getBlobDescriptor().getDigest());
      }
      return template;
    } catch (InstantiationException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException ex) {
      throw new IllegalArgumentException(manifestTemplateClass + " cannot be instantiated", ex);
    }
  }
}
