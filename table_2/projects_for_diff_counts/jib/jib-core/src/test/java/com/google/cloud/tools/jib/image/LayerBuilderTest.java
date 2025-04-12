package com.google.cloud.tools.jib.image;
@RunWith(MockitoJUnitRunner.class)
public class LayerBuilderTest {
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
  @Test
  public void testBuild() throws URISyntaxException, IOException {
    Path layerDirectory = Paths.get(Resources.getResource("layer").toURI());
    Path blobA = Paths.get(Resources.getResource("blobA").toURI());
    String extractionPathBase = "extract/here";
    LayerBuilder layerBuilder =
        new LayerBuilder(
            new ArrayList<>(Arrays.asList(layerDirectory, blobA)), extractionPathBase, false);
    UnwrittenLayer unwrittenLayer = layerBuilder.build();
    Path temporaryFile = temporaryFolder.newFile().toPath();
    try (OutputStream temporaryFileOutputStream =
        new BufferedOutputStream(Files.newOutputStream(temporaryFile))) {
      unwrittenLayer.getBlob().writeTo(temporaryFileOutputStream);
    }
    try (TarArchiveInputStream tarArchiveInputStream =
            new TarArchiveInputStream(Files.newInputStream(temporaryFile));
        Stream<Path> layerDirectoryFiles = Files.walk(layerDirectory)) {
      layerDirectoryFiles
          .filter(path -> !path.equals(layerDirectory))
          .forEach(
              path -> {
                try {
                  TarArchiveEntry header = tarArchiveInputStream.getNextTarEntry();
                  StringBuilder expectedExtractionPath = new StringBuilder("extract/here");
                  for (Path pathComponent : layerDirectory.getParent().relativize(path)) {
                    expectedExtractionPath.append("/").append(pathComponent);
                  }
                  Assert.assertEquals(
                      Paths.get(expectedExtractionPath.toString()), Paths.get(header.getName()));
                  if (Files.isRegularFile(path)) {
                    String expectedFileString =
                        new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                    String extractedFileString =
                        CharStreams.toString(
                            new InputStreamReader(tarArchiveInputStream, StandardCharsets.UTF_8));
                    Assert.assertEquals(expectedFileString, extractedFileString);
                  }
                } catch (IOException ex) {
                  throw new RuntimeException(ex);
                }
              });
      TarArchiveEntry header = tarArchiveInputStream.getNextTarEntry();
      String expectedExtractionPath = "extract/here/blobA";
      Assert.assertEquals(expectedExtractionPath, header.getName());
      String expectedFileString = new String(Files.readAllBytes(blobA), StandardCharsets.UTF_8);
      String extractedFileString =
          CharStreams.toString(
              new InputStreamReader(tarArchiveInputStream, StandardCharsets.UTF_8));
      Assert.assertEquals(expectedFileString, extractedFileString);
    }
  }
  @Test
  public void testToBlob_reproducibility() throws IOException {
    Path testRoot = temporaryFolder.getRoot().toPath();
    Path root1 = Files.createDirectories(testRoot.resolve("files1"));
    Path root2 = Files.createDirectories(testRoot.resolve("files2"));
    String extractionPath = "/somewhere";
    String contentA = "abcabc";
    Path fileA1 = createFile(root1, "fileA", contentA, 10000);
    Path fileA2 = createFile(root2, "fileA", contentA, 20000);
    String contentB = "yumyum";
    Path fileB1 = createFile(root1, "fileB", contentB, 10000);
    Path fileB2 = createFile(root2, "fileB", contentB, 20000);
    Assert.assertNotEquals(Files.getLastModifiedTime(fileA1), Files.getLastModifiedTime(fileA2));
    Assert.assertNotEquals(Files.getLastModifiedTime(fileB1), Files.getLastModifiedTime(fileB2));
    Blob layer =
        new LayerBuilder(Arrays.asList(fileA1, fileB1), extractionPath, true).build().getBlob();
    Blob reproduced =
        new LayerBuilder(Arrays.asList(fileB2, fileA2), extractionPath, true).build().getBlob();
    Blob notReproduced =
        new LayerBuilder(Arrays.asList(fileB2, fileA2), extractionPath, false).build().getBlob();
    byte[] layerContent = Blobs.writeToByteArray(layer);
    byte[] reproducedLayerContent = Blobs.writeToByteArray(reproduced);
    byte[] notReproducedLayerContent = Blobs.writeToByteArray(notReproduced);
    Assert.assertThat(layerContent, CoreMatchers.is(reproducedLayerContent));
    Assert.assertThat(layerContent, CoreMatchers.not(notReproducedLayerContent));
  }
  private Path createFile(Path root, String filename, String content, long lastModifiedTime)
      throws IOException {
    Path newFile =
        Files.write(
            root.resolve(filename),
            content.getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE_NEW);
    Files.setLastModifiedTime(newFile, FileTime.fromMillis(lastModifiedTime));
    return newFile;
  }
}
