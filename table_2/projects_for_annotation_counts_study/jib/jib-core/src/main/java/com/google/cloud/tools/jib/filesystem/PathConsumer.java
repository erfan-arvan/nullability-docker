package com.google.cloud.tools.jib.filesystem;
@FunctionalInterface
public interface PathConsumer {
  void accept(Path path) throws IOException;
}
