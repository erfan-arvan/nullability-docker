package com.google.cloud.tools.jib.registry;
public class RegistryClient {
  private Timer parentTimer =
      new Timer(
          new BuildLogger() {
            @Override
            public void debug(CharSequence message) {}
            @Override
            public void info(CharSequence message) {}
            @Override
            public void warn(CharSequence message) {}
            @Override
            public void error(CharSequence message) {}
          },
          "NULL TIMER");
  public RegistryClient setTimer(Timer parentTimer) {
    this.parentTimer = parentTimer;
    return this;
  }
  private static final String PROTOCOL = "https";
   private static String userAgentSuffix;
  public static void setUserAgentSuffix( String userAgentSuffix) {
    RegistryClient.userAgentSuffix = userAgentSuffix;
  }
  @VisibleForTesting
  static String getUserAgent() {
    String version = RegistryClient.class.getPackage().getImplementationVersion();
    StringBuilder userAgentBuilder = new StringBuilder();
    userAgentBuilder.append("jib");
    if (version != null) {
      userAgentBuilder.append(" ").append(version);
    }
    if (userAgentSuffix != null) {
      userAgentBuilder.append(" ").append(userAgentSuffix);
    }
    return userAgentBuilder.toString();
  }
   private final Authorization authorization;
  private final RegistryEndpointProperties registryEndpointProperties;
  public RegistryClient( Authorization authorization, String serverUrl, String imageName) {
    this.authorization = authorization;
    this.registryEndpointProperties = new RegistryEndpointProperties(serverUrl, imageName);
  }
  public RegistryAuthenticator getRegistryAuthenticator() throws IOException, RegistryException {
    return callRegistryEndpoint(new AuthenticationMethodRetriever(registryEndpointProperties));
  }
  public <T extends ManifestTemplate> T pullManifest(
      String imageTag, Class<T> manifestTemplateClass) throws IOException, RegistryException {
    ManifestPuller<T> manifestPuller =
        new ManifestPuller<>(registryEndpointProperties, imageTag, manifestTemplateClass);
    T manifestTemplate = callRegistryEndpoint(manifestPuller);
    if (manifestTemplate == null) {
      throw new IllegalStateException("ManifestPuller#handleResponse does not return null");
    }
    return manifestTemplate;
  }
  public ManifestTemplate pullManifest(String imageTag) throws IOException, RegistryException {
    return pullManifest(imageTag, ManifestTemplate.class);
  }
  public void pushManifest(BuildableManifestTemplate manifestTemplate, String imageTag)
      throws IOException, RegistryException {
    callRegistryEndpoint(
        new ManifestPusher(registryEndpointProperties, manifestTemplate, imageTag));
  }
  public BlobDescriptor checkBlob(DescriptorDigest blobDigest)
      throws IOException, RegistryException {
    BlobChecker blobChecker = new BlobChecker(registryEndpointProperties, blobDigest);
    return callRegistryEndpoint(blobChecker);
  }
  public Void pullBlob(DescriptorDigest blobDigest, OutputStream destinationOutputStream)
      throws RegistryException, IOException {
    BlobPuller blobPuller =
        new BlobPuller(registryEndpointProperties, blobDigest, destinationOutputStream);
    return callRegistryEndpoint(blobPuller);
  }
  public boolean pushBlob(DescriptorDigest blobDigest, Blob blob)
      throws IOException, RegistryException {
    BlobPusher blobPusher = new BlobPusher(registryEndpointProperties, blobDigest, blob);
    try (Timer t = parentTimer.subTimer("pushBlob")) {
      try (Timer t2 = t.subTimer("pushBlob POST " + blobDigest)) {
        String locationHeader = callRegistryEndpoint(blobPusher.initializer());
        if (locationHeader == null) {
          return true;
        }
        URL patchLocation = new URL(locationHeader);
        t2.lap("pushBlob PATCH " + blobDigest);
        URL putLocation = new URL(callRegistryEndpoint(blobPusher.writer(patchLocation)));
        t2.lap("pushBlob PUT " + blobDigest);
        callRegistryEndpoint(blobPusher.committer(putLocation));
        return false;
      }
    }
  }
  @VisibleForTesting
  String getApiRouteBase() {
    return PROTOCOL + ":
  }
  private <T> T callRegistryEndpoint(RegistryEndpointProvider<T> registryEndpointProvider)
      throws IOException, RegistryException {
    return callRegistryEndpoint(null, registryEndpointProvider);
  }
  private <T> T callRegistryEndpoint(
       URL url, RegistryEndpointProvider<T> registryEndpointProvider)
      throws IOException, RegistryException {
    if (url == null) {
      url = registryEndpointProvider.getApiRoute(getApiRouteBase());
    }
    try (Connection connection = new Connection(url)) {
      Request request =
          Request.builder()
              .setAuthorization(authorization)
              .setUserAgent(getUserAgent())
              .setAccept(registryEndpointProvider.getAccept())
              .setBody(registryEndpointProvider.getContent())
              .build();
      Response response = connection.send(registryEndpointProvider.getHttpMethod(), request);
      return registryEndpointProvider.handleResponse(response);
    } catch (HttpResponseException ex) {
      try {
        return registryEndpointProvider.handleHttpResponseException(ex);
      } catch (HttpResponseException httpResponseException) {
        if (httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_BAD_REQUEST
            || httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_NOT_FOUND
            || httpResponseException.getStatusCode()
                == HttpStatusCodes.STATUS_CODE_METHOD_NOT_ALLOWED) {
          ErrorResponseTemplate errorResponse =
              JsonTemplateMapper.readJson(
                  httpResponseException.getContent(), ErrorResponseTemplate.class);
          RegistryErrorExceptionBuilder registryErrorExceptionBuilder =
              new RegistryErrorExceptionBuilder(
                  registryEndpointProvider.getActionDescription(), httpResponseException);
          for (ErrorEntryTemplate errorEntry : errorResponse.getErrors()) {
            registryErrorExceptionBuilder.addReason(errorEntry);
          }
          throw registryErrorExceptionBuilder.build();
        } else if (httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED
            || httpResponseException.getStatusCode() == HttpStatusCodes.STATUS_CODE_FORBIDDEN) {
          throw new RegistryUnauthorizedException(
              registryEndpointProperties.getServerUrl(),
              registryEndpointProperties.getImageName(),
              httpResponseException);
        } else if (httpResponseException.getStatusCode()
            == HttpStatusCodes.STATUS_CODE_TEMPORARY_REDIRECT) {
          return callRegistryEndpoint(
              new URL(httpResponseException.getHeaders().getLocation()), registryEndpointProvider);
        } else {
          throw httpResponseException;
        }
      }
    } catch (NoHttpResponseException ex) {
      throw new RegistryNoResponseException(ex);
    } catch (SSLPeerUnverifiedException ex) {
      GenericUrl httpUrl = new GenericUrl(url);
      httpUrl.setScheme("http");
      return callRegistryEndpoint(httpUrl.toURL(), registryEndpointProvider);
    }
  }
}
