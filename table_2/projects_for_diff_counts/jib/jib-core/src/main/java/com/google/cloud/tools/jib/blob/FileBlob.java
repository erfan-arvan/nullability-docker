package com.google.cloud.tools.jib.blob;
class FileBlob implements Blob {
  private final Path file;
  FileBlob(Path file) {
    this.file = file;
  }
  @Override
  public BlobDescriptor writeTo(OutputStream outputStream) throws IOException {
    try (InputStream fileStream = new BufferedInputStream(Files.newInputStream(file))) {
      return BlobDescriptor.fromPipe(fileStream, outputStream);
    }
  }
}
