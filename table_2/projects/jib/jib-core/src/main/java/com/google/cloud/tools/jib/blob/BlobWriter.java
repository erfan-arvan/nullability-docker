package com.google.cloud.tools.jib.blob;
@FunctionalInterface
public interface BlobWriter {
  void writeTo(OutputStream outputStream) throws IOException;
}
