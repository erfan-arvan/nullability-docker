package com.google.cloud.tools.jib.filesystem;
public class UserCacheHomeTest {
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private String fakeCacheHome;
  @Before
  public void setUp() throws IOException {
    fakeCacheHome = temporaryFolder.newFolder().getPath();
  }
  @Test
  public void testGetCacheHome_hasXdgCacheHome() {
    Map<String, String> fakeEnvironment = ImmutableMap.of("XDG_CACHE_HOME", fakeCacheHome);
    Assert.assertEquals(
        Paths.get(fakeCacheHome),
        UserCacheHome.getCacheHome(Mockito.mock(Properties.class), fakeEnvironment));
  }
  @Test
  public void testGetCacheHome_linux() {
    Properties fakeProperties = new Properties();
    fakeProperties.setProperty("user.home", fakeCacheHome);
    fakeProperties.setProperty("os.name", "os is LiNuX");
    Assert.assertEquals(
        Paths.get(fakeCacheHome).resolve(".cache"),
        UserCacheHome.getCacheHome(fakeProperties, Collections.emptyMap()));
  }
  @Test
  public void testGetCacheHome_windows() {
    Properties fakeProperties = new Properties();
    fakeProperties.setProperty("user.home", "nonexistent");
    fakeProperties.setProperty("os.name", "os is WiNdOwS");
    Map<String, String> fakeEnvironment = ImmutableMap.of("LOCALAPPDATA", fakeCacheHome);
    Assert.assertEquals(
        Paths.get(fakeCacheHome), UserCacheHome.getCacheHome(fakeProperties, fakeEnvironment));
  }
  @Test
  public void testGetCacheHome_mac() throws IOException {
    Path libraryApplicationSupport =
        Paths.get(fakeCacheHome).resolve("Library").resolve("Application Support");
    Files.createDirectories(libraryApplicationSupport);
    Properties fakeProperties = new Properties();
    fakeProperties.setProperty("user.home", fakeCacheHome);
    fakeProperties.setProperty("os.name", "os is mAc or DaRwIn");
    Assert.assertEquals(
        libraryApplicationSupport,
        UserCacheHome.getCacheHome(fakeProperties, Collections.emptyMap()));
  }
}
