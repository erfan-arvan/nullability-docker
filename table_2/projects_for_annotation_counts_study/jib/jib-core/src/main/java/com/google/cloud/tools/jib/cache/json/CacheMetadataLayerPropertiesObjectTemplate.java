package com.google.cloud.tools.jib.cache.json;
public class CacheMetadataLayerPropertiesObjectTemplate implements JsonTemplate {
  private List<String> sourceFiles = new ArrayList<>();
  private long lastModifiedTime;
  public List<String> getSourceFiles() {
    return sourceFiles;
  }
  public FileTime getLastModifiedTime() {
    return FileTime.fromMillis(lastModifiedTime);
  }
  public CacheMetadataLayerPropertiesObjectTemplate setSourceFiles(List<String> sourceFiles) {
    this.sourceFiles = sourceFiles;
    return this;
  }
  public CacheMetadataLayerPropertiesObjectTemplate setLastModifiedTime(FileTime lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime.toMillis();
    return this;
  }
}
