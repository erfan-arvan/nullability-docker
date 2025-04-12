package com.google.cloud.tools.jib.http;
public class Authorization {
  private final String scheme;
  private final String token;
  Authorization(String scheme, String token) {
    this.scheme = scheme;
    this.token = token;
  }
  public String getScheme() {
    return scheme;
  }
  public String getToken() {
    return token;
  }
  @Override
  public String toString() {
    return scheme + " " + token;
  }
}
