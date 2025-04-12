package com.google.cloud.tools.jib.blob;
class WriterBlob implements Blob {
  private final BlobWriter writer;
  WriterBlob(BlobWriter writer) {
    this.writer = writer;
  }
  @Override
  public BlobDescriptor writeTo(OutputStream outputStream) throws IOException {
    CountingDigestOutputStream countingDigestOutputStream =
        new CountingDigestOutputStream(outputStream);
    writer.writeTo(countingDigestOutputStream);
    countingDigestOutputStream.flush();
    return countingDigestOutputStream.toBlobDescriptor();
  }
}
