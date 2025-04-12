package com.google.cloud.tools.jib.filesystem;
public class UserCacheHome {
  private static final Logger logger = Logger.getLogger(UserCacheHome.class.getName());
  public static Path getCacheHome() {
    return getCacheHome(System.getProperties(), System.getenv());
  }
  @VisibleForTesting
  static Path getCacheHome(Properties properties, Map<String, String> environment) {
    String xdgCacheHome = environment.get("XDG_CACHE_HOME");
    if (xdgCacheHome != null && !xdgCacheHome.trim().isEmpty()) {
      return Paths.get(xdgCacheHome);
    }
    String userHome = properties.getProperty("user.home");
    Path userHomeDirectory = Paths.get(userHome);
    Path xdgPath = userHomeDirectory.resolve(".cache");
    String rawOsName = properties.getProperty("os.name");
    String osName = rawOsName.toLowerCase(Locale.ENGLISH);
    if (osName.contains("linux")) {
      return xdgPath;
    } else if (osName.contains("windows")) {
      String localAppDataEnv = environment.get("LOCALAPPDATA");
      if (localAppDataEnv == null || localAppDataEnv.trim().isEmpty()) {
        logger.warning("LOCALAPPDATA environment is invalid or missing");
        return xdgPath;
      }
      Path localAppData = Paths.get(localAppDataEnv);
      if (!Files.exists(localAppData)) {
        logger.warning(localAppData + " does not exist");
        return xdgPath;
      }
      return localAppData;
    } else if (osName.contains("mac") || osName.contains("darwin")) {
      Path applicationSupport = userHomeDirectory.resolve("Library").resolve("Application Support");
      if (!Files.exists(applicationSupport)) {
        logger.warning(applicationSupport + " does not exist");
        return xdgPath;
      }
      return applicationSupport;
    }
    throw new IllegalStateException("Unknown OS: " + rawOsName);
  }
  private UserCacheHome() {}
}
