package com.google.cloud.tools.jib.filesystem;
public class DirectoryWalkerTest {
  @Test
  public void testWalk() throws URISyntaxException, IOException {
    Path testDir = Paths.get(Resources.getResource("layer").toURI());
    Set<Path> walkedPaths = new HashSet<>();
    PathConsumer addToWalkedPaths = walkedPaths::add;
    new DirectoryWalker(testDir).walk(addToWalkedPaths);
    Set<Path> expectedPaths =
        new HashSet<>(
            Arrays.asList(
                testDir,
                testDir.resolve("a"),
                testDir.resolve("a").resolve("b"),
                testDir.resolve("a").resolve("b").resolve("bar"),
                testDir.resolve("c"),
                testDir.resolve("c").resolve("cat"),
                testDir.resolve("foo")));
    Assert.assertEquals(expectedPaths, walkedPaths);
  }
}
