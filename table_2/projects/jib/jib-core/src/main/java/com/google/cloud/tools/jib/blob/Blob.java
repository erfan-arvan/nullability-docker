package com.google.cloud.tools.jib.blob;
public interface Blob {
  BlobDescriptor writeTo(OutputStream outputStream) throws IOException;
}
