package com.google.cloud.tools.jib.blob;
class InputStreamBlob implements Blob {
  private final InputStream inputStream;
  private boolean isWritten = false;
  InputStreamBlob(InputStream inputStream) {
    this.inputStream = inputStream;
  }
  @Override
  public BlobDescriptor writeTo(OutputStream outputStream) throws IOException {
    if (isWritten) {
      throw new IllegalStateException("Cannot rewrite Blob backed by an InputStream");
    }
    try (InputStream inputStream = this.inputStream) {
      return BlobDescriptor.fromPipe(inputStream, outputStream);
    } finally {
      isWritten = true;
    }
  }
}
