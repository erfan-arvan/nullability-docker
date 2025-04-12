package com.google.cloud.tools.jib.http;
public class Response {
  private final HttpResponse httpResponse;
  Response(HttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }
  public int getStatusCode() {
    return httpResponse.getStatusCode();
  }
  public List<String> getHeader(String headerName) {
    return httpResponse.getHeaders().getHeaderStringValues(headerName);
  }
  public long getContentLength() throws NumberFormatException {
    String contentLengthHeader =
        httpResponse.getHeaders().getFirstHeaderStringValue(HttpHeaders.CONTENT_LENGTH);
    if (contentLengthHeader == null) {
      return -1;
    }
    try {
      return Long.parseLong(contentLengthHeader);
    } catch (NumberFormatException ex) {
      return -1;
    }
  }
  public Blob getBody() throws IOException {
    return Blobs.from(httpResponse.getContent());
  }
}
