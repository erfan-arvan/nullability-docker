package com.google.cloud.tools.jib.blob;
public class Blobs {
  public static Blob from(InputStream inputStream) {
    return new InputStreamBlob(inputStream);
  }
  public static Blob from(Path file) {
    return new FileBlob(file);
  }
  public static Blob from(String content) {
    return new StringBlob(content);
  }
  public static Blob from(BlobWriter writer) {
    return new WriterBlob(writer);
  }
  public static String writeToString(Blob blob) throws IOException {
    return new String(writeToByteArray(blob), StandardCharsets.UTF_8);
  }
  public static byte[] writeToByteArray(Blob blob) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    blob.writeTo(byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
  private Blobs() {}
}
