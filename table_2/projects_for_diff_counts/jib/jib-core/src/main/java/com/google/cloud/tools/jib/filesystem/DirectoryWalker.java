package com.google.cloud.tools.jib.filesystem;
public class DirectoryWalker {
  private final Path rootDir;
  public DirectoryWalker(Path rootDir) throws NotDirectoryException {
    if (!Files.isDirectory(rootDir)) {
      throw new NotDirectoryException(rootDir + " is not a directory");
    }
    this.rootDir = rootDir;
  }
  public void walk(PathConsumer pathConsumer) throws IOException {
    try (Stream<Path> fileStream = Files.walk(rootDir)) {
      List<Path> files = fileStream.collect(Collectors.toList());
      for (Path path : files) {
        pathConsumer.accept(path);
      }
    }
  }
}
