package com.google.cloud.tools.jib.builder;
public class BuildImageStepsIntegrationTest {
  @ClassRule public static LocalRegistry localRegistry = new LocalRegistry(5000);
  private static final TestBuildLogger logger = new TestBuildLogger();
  @Rule public TemporaryFolder temporaryCacheDirectory = new TemporaryFolder();
  @Test
  public void testSteps() throws Exception {
    SourceFilesConfiguration sourceFilesConfiguration = new TestSourceFilesConfiguration();
    BuildConfiguration buildConfiguration =
        BuildConfiguration.builder(logger)
            .setBaseImage(ImageReference.of("gcr.io", "distroless/java", "latest"))
            .setTargetImage(ImageReference.of("localhost:5000", "testimage", "testtag"))
            .setMainClass("HelloWorld")
            .build();
    BuildImageSteps buildImageSteps =
        new BuildImageSteps(
            buildConfiguration,
            sourceFilesConfiguration,
            temporaryCacheDirectory.getRoot().toPath());
    long lastTime = System.nanoTime();
    buildImageSteps.run();
    logger.info("Initial build time: " + ((System.nanoTime() - lastTime) / 1_000_000));
    lastTime = System.nanoTime();
    buildImageSteps.run();
    logger.info("Secondary build time: " + ((System.nanoTime() - lastTime) / 1_000_000));
    String imageReference = "localhost:5000/testimage:testtag";
    new Command("docker", "pull", imageReference).run();
    Assert.assertEquals("Hello world\n", new Command("docker", "run", imageReference).run());
  }
}
