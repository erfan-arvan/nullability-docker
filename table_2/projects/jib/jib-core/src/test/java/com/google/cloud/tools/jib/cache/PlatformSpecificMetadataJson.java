package com.google.cloud.tools.jib.cache;
public class PlatformSpecificMetadataJson {
  public static Path getMetadataJsonFile() throws URISyntaxException {
    String metadataResourceFilename = "json/metadata.json";
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      metadataResourceFilename = "json/metadata_windows.json";
    }
    return Paths.get(Resources.getResource(metadataResourceFilename).toURI());
  }
  private PlatformSpecificMetadataJson() {}
}
