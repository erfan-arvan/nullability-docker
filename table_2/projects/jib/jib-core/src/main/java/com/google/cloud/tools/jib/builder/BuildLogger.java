package com.google.cloud.tools.jib.builder;
public interface BuildLogger {
  void debug(CharSequence message);
  void info(CharSequence message);
  void warn(CharSequence message);
  void error(CharSequence message);
}
