package com.google.cloud.tools.jib.registry;
class RegistryEndpointProperties {
  private final String serverUrl;
  private final String imageName;
  RegistryEndpointProperties(String serverUrl, String imageName) {
    this.serverUrl = serverUrl;
    this.imageName = imageName;
  }
  String getServerUrl() {
    return serverUrl;
  }
  String getImageName() {
    return imageName;
  }
}
