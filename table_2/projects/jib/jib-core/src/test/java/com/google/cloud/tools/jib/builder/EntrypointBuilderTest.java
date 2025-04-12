package com.google.cloud.tools.jib.builder;
public class EntrypointBuilderTest {
  @Test
  public void testMakeEntrypoint() {
    String expectedDependenciesPath = "/app/libs/";
    String expectedResourcesPath = "/app/resources/";
    String expectedClassesPath = "/app/classes/";
    List<String> expectedJvmFlags = Arrays.asList("-flag", "anotherFlag");
    String expectedMainClass = "SomeMainClass";
    SourceFilesConfiguration mockSourceFilesConfiguration =
        Mockito.mock(SourceFilesConfiguration.class);
    Mockito.when(mockSourceFilesConfiguration.getDependenciesPathOnImage())
        .thenReturn(expectedDependenciesPath);
    Mockito.when(mockSourceFilesConfiguration.getResourcesPathOnImage())
        .thenReturn(expectedResourcesPath);
    Mockito.when(mockSourceFilesConfiguration.getClassesPathOnImage())
        .thenReturn(expectedClassesPath);
    Assert.assertEquals(
        Arrays.asList(
            "java",
            "-flag",
            "anotherFlag",
            "-cp",
            "/app/libs/*:/app/resources/:/app/classes/",
            "SomeMainClass"),
        EntrypointBuilder.makeEntrypoint(
            mockSourceFilesConfiguration, expectedJvmFlags, expectedMainClass));
  }
}
