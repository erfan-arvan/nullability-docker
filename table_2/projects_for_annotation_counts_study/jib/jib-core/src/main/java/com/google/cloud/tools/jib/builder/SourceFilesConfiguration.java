package com.google.cloud.tools.jib.builder;
public interface SourceFilesConfiguration {
  List<Path> getDependenciesFiles();
  List<Path> getResourcesFiles();
  List<Path> getClassesFiles();
  String getDependenciesPathOnImage();
  String getResourcesPathOnImage();
  String getClassesPathOnImage();
}
