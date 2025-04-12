package com.google.cloud.tools.jib.http;
public class BlobHttpContent implements HttpContent {
  private final Blob blob;
  private final String contentType;
  public BlobHttpContent(Blob blob, String contentType) {
    this.blob = blob;
    this.contentType = contentType;
  }
  @Override
  public long getLength() {
    return -1;
  }
  @Override
  public String getType() {
    return contentType;
  }
  @Override
  public boolean retrySupported() {
    return false;
  }
  @Override
  public void writeTo(OutputStream outputStream) throws IOException {
    blob.writeTo(outputStream);
    outputStream.flush();
  }
}
