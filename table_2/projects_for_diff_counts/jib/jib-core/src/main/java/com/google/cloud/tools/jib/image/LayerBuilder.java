package com.google.cloud.tools.jib.image;
public class LayerBuilder {
  private final List<Path> sourceFiles;
  private final String extractionPath;
  private final boolean enableReproducibleBuilds;
  public LayerBuilder(
      List<Path> sourceFiles, String extractionPath, boolean enableReproducibleBuilds) {
    this.sourceFiles = new ArrayList<>(sourceFiles);
    this.extractionPath = extractionPath;
    this.enableReproducibleBuilds = enableReproducibleBuilds;
  }
  public UnwrittenLayer build() throws IOException {
    List<TarArchiveEntry> filesystemEntries = new ArrayList<>();
    for (Path sourceFile : sourceFiles) {
      if (Files.isDirectory(sourceFile)) {
        try (Stream<Path> fileStream = Files.walk(sourceFile)) {
          fileStream
              .filter(path -> !path.equals(sourceFile))
              .forEach(
                  path -> {
                    StringBuilder subExtractionPath = new StringBuilder(extractionPath);
                    Path sourceFileRelativePath = sourceFile.getParent().relativize(path);
                    for (Path sourceFileRelativePathComponent : sourceFileRelativePath) {
                      subExtractionPath.append('/').append(sourceFileRelativePathComponent);
                    }
                    filesystemEntries.add(
                        new TarArchiveEntry(path.toFile(), subExtractionPath.toString()));
                  });
        }
      } else {
        TarArchiveEntry tarArchiveEntry =
            new TarArchiveEntry(
                sourceFile.toFile(), extractionPath + "/" + sourceFile.getFileName());
        filesystemEntries.add(tarArchiveEntry);
      }
    }
    if (enableReproducibleBuilds) {
      makeListReproducible(filesystemEntries);
    }
    TarStreamBuilder tarStreamBuilder = new TarStreamBuilder();
    for (TarArchiveEntry entry : filesystemEntries) {
      tarStreamBuilder.addEntry(entry);
    }
    return new UnwrittenLayer(tarStreamBuilder.toBlob());
  }
  private void makeListReproducible(List<TarArchiveEntry> entries) {
    entries.sort(Comparator.comparing(TarArchiveEntry::getName));
    for (TarArchiveEntry entry : entries) {
      entry.setModTime(0);
      entry.setGroupId(0);
      entry.setUserId(0);
      entry.setUserName("");
      entry.setGroupName("");
    }
  }
  public List<Path> getSourceFiles() {
    return Collections.unmodifiableList(sourceFiles);
  }
}
