package com.google.cloud.tools.jib.http;
public class Authorizations {
  public static Authorization withBearerToken(String token) {
    return new Authorization("Bearer", token);
  }
  public static Authorization withBasicCredentials(String username, String secret) {
    String credentials = username + ":" + secret;
    String token =
        new String(
            Base64.encodeBase64(credentials.getBytes(StandardCharsets.US_ASCII)),
            StandardCharsets.UTF_8);
    return new Authorization("Basic", token);
  }
  public static Authorization withBasicToken(String token) {
    return new Authorization("Basic", token);
  }
  private Authorizations() {}
}
