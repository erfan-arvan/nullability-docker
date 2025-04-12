package com.google.cloud.tools.jib.blob;
class StringBlob implements Blob {
  private final String content;
  StringBlob(String content) {
    this.content = content;
  }
  @Override
  public BlobDescriptor writeTo(OutputStream outputStream) throws IOException {
    try (InputStream stringInputStream =
        new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
      return BlobDescriptor.fromPipe(stringInputStream, outputStream);
    }
  }
}
