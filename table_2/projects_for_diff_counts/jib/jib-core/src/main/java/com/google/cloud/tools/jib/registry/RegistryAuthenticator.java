package com.google.cloud.tools.jib.registry;
public class RegistryAuthenticator {
  static RegistryAuthenticator fromAuthenticationMethod(
      String authenticationMethod, String repository)
      throws RegistryAuthenticationFailedException, MalformedURLException {
    if (authenticationMethod.matches("^Basic .*")) {
      return null;
    }
    if (!authenticationMethod.matches("^Bearer .*")) {
      throw newRegistryAuthenticationFailedException(authenticationMethod, "Bearer");
    }
    Pattern realmPattern = Pattern.compile("realm=\"(.*?)\"");
    Matcher realmMatcher = realmPattern.matcher(authenticationMethod);
    if (!realmMatcher.find()) {
      throw newRegistryAuthenticationFailedException(authenticationMethod, "realm");
    }
    String realm = realmMatcher.group(1);
    Pattern servicePattern = Pattern.compile("service=\"(.*?)\"");
    Matcher serviceMatcher = servicePattern.matcher(authenticationMethod);
    if (!serviceMatcher.find()) {
      throw newRegistryAuthenticationFailedException(authenticationMethod, "service");
    }
    String service = serviceMatcher.group(1);
    return new RegistryAuthenticator(realm, service, repository);
  }
  private static RegistryAuthenticationFailedException newRegistryAuthenticationFailedException(
      String authenticationMethod, String authParam) {
    return new RegistryAuthenticationFailedException(
        "'"
            + authParam
            + "' was not found in the 'WWW-Authenticate' header, tried to parse: "
            + authenticationMethod);
  }
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class AuthenticationResponseTemplate implements JsonTemplate {
     private String token;
  }
  private final String authenticationUrlBase;
   private Authorization authorization;
  RegistryAuthenticator(String realm, String service, String repository)
      throws MalformedURLException {
    authenticationUrlBase = realm + "?service=" + service + "&scope=repository:" + repository + ":";
  }
  public RegistryAuthenticator setAuthorization( Authorization authorization) {
    this.authorization = authorization;
    return this;
  }
  public Authorization authenticatePull() throws RegistryAuthenticationFailedException {
    return authenticate("pull");
  }
  public Authorization authenticatePush() throws RegistryAuthenticationFailedException {
    return authenticate("pull,push");
  }
  @VisibleForTesting
  URL getAuthenticationUrl(String scope) throws MalformedURLException {
    return new URL(authenticationUrlBase + scope);
  }
  private Authorization authenticate(String scope) throws RegistryAuthenticationFailedException {
    try {
      URL authenticationUrl = getAuthenticationUrl(scope);
      try (Connection connection = new Connection(authenticationUrl)) {
        Request.Builder requestBuilder = Request.builder();
        if (authorization != null) {
          requestBuilder.setAuthorization(authorization);
        }
        Response response = connection.get(requestBuilder.build());
        String responseString = Blobs.writeToString(response.getBody());
        AuthenticationResponseTemplate responseJson =
            JsonTemplateMapper.readJson(responseString, AuthenticationResponseTemplate.class);
        if (responseJson.token == null) {
          throw new RegistryAuthenticationFailedException(
              "Did not get token in authentication response from " + authenticationUrl);
        }
        return Authorizations.withBearerToken(responseJson.token);
      }
    } catch (IOException ex) {
      throw new RegistryAuthenticationFailedException(ex);
    }
  }
}
