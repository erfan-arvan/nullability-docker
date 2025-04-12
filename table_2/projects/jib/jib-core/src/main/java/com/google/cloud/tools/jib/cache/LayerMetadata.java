package com.google.cloud.tools.jib.cache;
class LayerMetadata {
  static LayerMetadata from(List<Path> sourceFiles, FileTime lastModifiedTime) {
    List<String> sourceFilesStrings = new ArrayList<>(sourceFiles.size());
    for (Path sourceFile : sourceFiles) {
      sourceFilesStrings.add(sourceFile.toString());
    }
    return new LayerMetadata(sourceFilesStrings, lastModifiedTime);
  }
  private List<String> sourceFiles;
  private final FileTime lastModifiedTime;
  LayerMetadata(List<String> sourceFiles, FileTime lastModifiedTime) {
    this.sourceFiles = sourceFiles;
    this.lastModifiedTime = lastModifiedTime;
  }
  List<String> getSourceFiles() {
    return sourceFiles;
  }
  public FileTime getLastModifiedTime() {
    return lastModifiedTime;
  }
  @VisibleForTesting
  void setSourceFiles(List<String> sourceFiles) {
    this.sourceFiles = sourceFiles;
  }
}
