package com.google.cloud.tools.jib.builder;
public class TestBuildLogger implements BuildLogger {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestBuildLogger.class);
  @Override
  public void debug(CharSequence message) {
    LOGGER.debug(message.toString());
  }
  @Override
  public void info(CharSequence message) {
    LOGGER.info(message.toString());
  }
  @Override
  public void warn(CharSequence message) {
    LOGGER.warn(message.toString());
  }
  @Override
  public void error(CharSequence message) {
    LOGGER.error(message.toString());
  }
}
