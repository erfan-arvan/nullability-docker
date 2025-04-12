package com.google.cloud.tools.jib.builder;
class TestSourceFilesConfiguration implements SourceFilesConfiguration {
  private static final String EXTRACTION_PATH = "/some/extraction/path/";
  private final List<Path> dependenciesSourceFiles;
  private final List<Path> resourcesSourceFiles;
  private final List<Path> classesSourceFiles;
  TestSourceFilesConfiguration() throws URISyntaxException, IOException {
    dependenciesSourceFiles = getFilesList("application/dependencies");
    resourcesSourceFiles = getFilesList("application/resources");
    classesSourceFiles = getFilesList("application/classes");
  }
  @Override
  public List<Path> getDependenciesFiles() {
    return dependenciesSourceFiles;
  }
  @Override
  public List<Path> getResourcesFiles() {
    return resourcesSourceFiles;
  }
  @Override
  public List<Path> getClassesFiles() {
    return classesSourceFiles;
  }
  @Override
  public String getDependenciesPathOnImage() {
    return EXTRACTION_PATH + "libs/";
  }
  @Override
  public String getResourcesPathOnImage() {
    return EXTRACTION_PATH + "resources/";
  }
  @Override
  public String getClassesPathOnImage() {
    return EXTRACTION_PATH + "classes/";
  }
  private List<Path> getFilesList(String resourcePath) throws URISyntaxException, IOException {
    try (Stream<Path> fileStream =
        Files.list(Paths.get(Resources.getResource(resourcePath).toURI()))) {
      return fileStream.collect(Collectors.toList());
    }
  }
}
