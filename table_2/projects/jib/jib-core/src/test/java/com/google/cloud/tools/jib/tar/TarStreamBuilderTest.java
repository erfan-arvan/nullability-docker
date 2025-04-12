package com.google.cloud.tools.jib.tar;
public class TarStreamBuilderTest {
  private String expectedFileAString;
  private String expectedFileBString;
  private TarStreamBuilder testTarStreamBuilder = new TarStreamBuilder();
  @Before
  public void setUp() throws IOException, URISyntaxException {
    Path fileA = Paths.get(Resources.getResource("fileA").toURI());
    Path fileB = Paths.get(Resources.getResource("fileB").toURI());
    Path directoryA = Paths.get(Resources.getResource("directoryA").toURI());
    expectedFileAString = new String(Files.readAllBytes(fileA), StandardCharsets.UTF_8);
    expectedFileBString = new String(Files.readAllBytes(fileB), StandardCharsets.UTF_8);
    testTarStreamBuilder.addEntry(
        new TarArchiveEntry(fileA.toFile(), "some/path/to/resourceFileA"));
    testTarStreamBuilder.addEntry(new TarArchiveEntry(fileB.toFile(), "crepecake"));
    testTarStreamBuilder.addEntry(new TarArchiveEntry(directoryA.toFile(), "some/path/to"));
    testTarStreamBuilder.addEntry(
        new TarArchiveEntry(
            fileA.toFile(),
            "some/really/long/path/that/exceeds/100/characters/abcdefghijklmnopqrstuvwxyz0123456789012345678901234567890"));
  }
  @Test
  public void testToBlob() throws IOException {
    Blob blob = testTarStreamBuilder.toBlob();
    ByteArrayOutputStream tarByteOutputStream = new ByteArrayOutputStream();
    blob.writeTo(tarByteOutputStream);
    ByteArrayInputStream tarByteInputStream =
        new ByteArrayInputStream(tarByteOutputStream.toByteArray());
    TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(tarByteInputStream);
    verifyTarArchive(tarArchiveInputStream);
  }
  @Test
  public void testToBlob_withCompression() throws IOException {
    Blob blob = testTarStreamBuilder.toBlob();
    ByteArrayOutputStream tarByteOutputStream = new ByteArrayOutputStream();
    OutputStream compressorStream = new GZIPOutputStream(tarByteOutputStream);
    blob.writeTo(compressorStream);
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(tarByteOutputStream.toByteArray());
    InputStream tarByteInputStream = new GZIPInputStream(byteArrayInputStream);
    TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(tarByteInputStream);
    verifyTarArchive(tarArchiveInputStream);
  }
  private void verifyTarArchive(TarArchiveInputStream tarArchiveInputStream) throws IOException {
    TarArchiveEntry headerFileA = tarArchiveInputStream.getNextTarEntry();
    Assert.assertEquals("some/path/to/resourceFileA", headerFileA.getName());
    String fileAString =
        CharStreams.toString(new InputStreamReader(tarArchiveInputStream, StandardCharsets.UTF_8));
    Assert.assertEquals(expectedFileAString, fileAString);
    TarArchiveEntry headerFileB = tarArchiveInputStream.getNextTarEntry();
    Assert.assertEquals("crepecake", headerFileB.getName());
    String fileBString =
        CharStreams.toString(new InputStreamReader(tarArchiveInputStream, StandardCharsets.UTF_8));
    Assert.assertEquals(expectedFileBString, fileBString);
    TarArchiveEntry headerDirectoryA = tarArchiveInputStream.getNextTarEntry();
    Assert.assertEquals("some/path/to/", headerDirectoryA.getName());
    TarArchiveEntry headerFileALong = tarArchiveInputStream.getNextTarEntry();
    Assert.assertEquals(
        "some/really/long/path/that/exceeds/100/characters/abcdefghijklmnopqrstuvwxyz0123456789012345678901234567890",
        headerFileALong.getName());
    String fileALongString =
        CharStreams.toString(new InputStreamReader(tarArchiveInputStream, StandardCharsets.UTF_8));
    Assert.assertEquals(expectedFileAString, fileALongString);
    Assert.assertNull(tarArchiveInputStream.getNextTarEntry());
  }
}
