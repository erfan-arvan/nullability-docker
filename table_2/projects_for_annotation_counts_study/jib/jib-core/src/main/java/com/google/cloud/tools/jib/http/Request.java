package com.google.cloud.tools.jib.http;
public class Request {
  private final HttpHeaders headers;
   private BlobHttpContent body;
  public static class Builder {
    private final HttpHeaders headers = new HttpHeaders().setAccept("*
    public Builder setAuthorization( Authorization authorization) {
      if (authorization != null) {
        headers.setAuthorization(authorization.toString());
      }
      return this;
    }
    public Builder setAccept(List<String> mimeTypes) {
      headers.setAccept(String.join(",", mimeTypes));
      return this;
    }
    public Builder setUserAgent(String userAgent) {
      headers.setUserAgent(userAgent);
      return this;
    }
    public Builder setBody( BlobHttpContent blobHttpContent) {
      this.body = blobHttpContent;
      return this;
    }
  }
  public static Builder builder() {
    return new Builder();
  }
  private Request(Builder builder) {
    this.headers = builder.headers;
    this.body = builder.body;
  }
  HttpHeaders getHeaders() {
    return headers;
  }
  BlobHttpContent getHttpContent() {
    return body;
  }
}
