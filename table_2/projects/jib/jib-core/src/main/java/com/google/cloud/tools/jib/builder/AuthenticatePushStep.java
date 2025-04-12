package com.google.cloud.tools.jib.builder;
class AuthenticatePushStep implements Callable<Authorization> {
  private static final String DESCRIPTION = "Authenticating with push to %s";
  private final BuildConfiguration buildConfiguration;
  private final ListenableFuture<Authorization> registryCredentialsFuture;
  AuthenticatePushStep(
      BuildConfiguration buildConfiguration,
      ListenableFuture<Authorization> registryCredentialsFuture) {
    this.buildConfiguration = buildConfiguration;
    this.registryCredentialsFuture = registryCredentialsFuture;
  }
  @Override
  public Authorization call()
      throws ExecutionException, InterruptedException, RegistryAuthenticationFailedException,
          IOException, RegistryException {
    try (Timer ignored =
        new Timer(
            buildConfiguration.getBuildLogger(),
            String.format(DESCRIPTION, buildConfiguration.getTargetRegistry()))) {
      Authorization registryCredentials = NonBlockingFutures.get(registryCredentialsFuture);
      RegistryAuthenticator registryAuthenticator =
          RegistryAuthenticators.forOther(
              buildConfiguration.getTargetRegistry(), buildConfiguration.getTargetRepository());
      if (registryAuthenticator == null) {
        return registryCredentials;
      }
      return registryAuthenticator
          .setAuthorization(NonBlockingFutures.get(registryCredentialsFuture))
          .authenticatePush();
    }
  }
}
