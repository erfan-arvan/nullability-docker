package com.google.cloud.tools.jib.builder;
class AuthenticatePullStep implements Callable<Authorization> {
  private static final String DESCRIPTION = "Authenticating pull from %s";
  private final BuildConfiguration buildConfiguration;
  private final ListenableFuture<Authorization> registryCredentialsFuture;
  AuthenticatePullStep(
      BuildConfiguration buildConfiguration,
      ListenableFuture<Authorization> registryCredentialsFuture) {
    this.buildConfiguration = buildConfiguration;
    this.registryCredentialsFuture = registryCredentialsFuture;
  }
  @Override
  public Authorization call()
      throws RegistryAuthenticationFailedException, IOException, RegistryException,
          ExecutionException, InterruptedException {
    try (Timer ignored =
        new Timer(
            buildConfiguration.getBuildLogger(),
            String.format(DESCRIPTION, buildConfiguration.getBaseImageRegistry()))) {
      Authorization registryCredentials = NonBlockingFutures.get(registryCredentialsFuture);
      RegistryAuthenticator registryAuthenticator =
          RegistryAuthenticators.forOther(
              buildConfiguration.getBaseImageRegistry(),
              buildConfiguration.getBaseImageRepository());
      if (registryAuthenticator == null) {
        return registryCredentials;
      }
      return registryAuthenticator.setAuthorization(registryCredentials).authenticatePull();
    }
  }
}
