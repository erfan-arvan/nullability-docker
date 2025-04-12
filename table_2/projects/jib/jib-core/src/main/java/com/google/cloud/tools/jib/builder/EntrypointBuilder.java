package com.google.cloud.tools.jib.builder;
public class EntrypointBuilder {
  public static List<String> makeEntrypoint(
      SourceFilesConfiguration sourceFilesConfiguration, List<String> jvmFlags, String mainClass) {
    List<String> classPaths = new ArrayList<>();
    classPaths.add(sourceFilesConfiguration.getDependenciesPathOnImage() + "*");
    classPaths.add(sourceFilesConfiguration.getResourcesPathOnImage());
    classPaths.add(sourceFilesConfiguration.getClassesPathOnImage());
    String classPathsString = String.join(":", classPaths);
    List<String> entrypoint = new ArrayList<>(4 + jvmFlags.size());
    entrypoint.add("java");
    entrypoint.addAll(jvmFlags);
    entrypoint.add("-cp");
    entrypoint.add(classPathsString);
    entrypoint.add(mainClass);
    return entrypoint;
  }
  private EntrypointBuilder() {}
}
