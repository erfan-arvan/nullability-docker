package com.google.cloud.tools.jib.image.json;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerConfigurationTemplate implements JsonTemplate {
  private String architecture = "amd64";
  private String os = "linux";
  private final ConfigurationObjectTemplate config = new ConfigurationObjectTemplate();
  private final RootFilesystemObjectTemplate rootfs = new RootFilesystemObjectTemplate();
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class ConfigurationObjectTemplate implements JsonTemplate {
     private List<String> Env;
     private List<String> Entrypoint;
  }
  private static class RootFilesystemObjectTemplate implements JsonTemplate {
    private final String type = "layers";
    private final List<DescriptorDigest> diff_ids = new ArrayList<>();
  }
  public void setContainerEnvironment(List<String> environment) {
    config.Env = environment;
  }
  public void setContainerEntrypoint(List<String> command) {
    config.Entrypoint = command;
  }
  public void addLayerDiffId(DescriptorDigest diffId) {
    rootfs.diff_ids.add(diffId);
  }
  List<DescriptorDigest> getDiffIds() {
    return rootfs.diff_ids;
  }
  List<String> getContainerEnvironment() {
    return config.Env;
  }
  List<String> getContainerEntrypoint() {
    return config.Entrypoint;
  }
  @VisibleForTesting
  DescriptorDigest getLayerDiffId(int index) {
    return rootfs.diff_ids.get(index);
  }
}
