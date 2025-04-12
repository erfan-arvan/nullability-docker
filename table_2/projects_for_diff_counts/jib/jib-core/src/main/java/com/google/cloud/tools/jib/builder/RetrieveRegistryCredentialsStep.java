package com.google.cloud.tools.jib.builder;
class RetrieveRegistryCredentialsStep implements Callable<Authorization> {
  private static final String DESCRIPTION = "Retrieving registry credentials for %s";
  private static final ImmutableMap<String, String> COMMON_CREDENTIAL_HELPERS =
      ImmutableMap.of("gcr.io", "gcr", "amazonaws.com", "ecr-login");
  private final BuildConfiguration buildConfiguration;
  private final String registry;
  private final DockerCredentialHelperFactory dockerCredentialHelperFactory;
  private final DockerConfigCredentialRetriever dockerConfigCredentialRetriever;
  RetrieveRegistryCredentialsStep(BuildConfiguration buildConfiguration, String registry) {
    this(
        buildConfiguration,
        registry,
        new DockerCredentialHelperFactory(registry),
        new DockerConfigCredentialRetriever(registry));
  }
  @VisibleForTesting
  RetrieveRegistryCredentialsStep(
      BuildConfiguration buildConfiguration,
      String registry,
      DockerCredentialHelperFactory dockerCredentialHelperFactory,
      DockerConfigCredentialRetriever dockerConfigCredentialRetriever) {
    this.buildConfiguration = buildConfiguration;
    this.registry = registry;
    this.dockerCredentialHelperFactory = dockerCredentialHelperFactory;
    this.dockerConfigCredentialRetriever = dockerConfigCredentialRetriever;
  }
  @Override
  public Authorization call() throws IOException, NonexistentDockerCredentialHelperException {
    try (Timer ignored =
        new Timer(
            buildConfiguration.getBuildLogger(),
            String.format(DESCRIPTION, buildConfiguration.getTargetRegistry()))) {
      for (String credentialHelperSuffix : buildConfiguration.getCredentialHelperNames()) {
        Authorization authorization = retrieveFromCredentialHelper(credentialHelperSuffix);
        if (authorization != null) {
          return authorization;
        }
      }
      String credentialSource =
          buildConfiguration.getKnownRegistryCredentials().getCredentialSource(registry);
      if (credentialSource != null) {
        logGotCredentialsFrom(credentialSource);
        return buildConfiguration.getKnownRegistryCredentials().getAuthorization(registry);
      }
      Authorization dockerConfigAuthorization = dockerConfigCredentialRetriever.retrieve();
      if (dockerConfigAuthorization != null) {
        buildConfiguration
            .getBuildLogger()
            .info("Using credentials from Docker config for " + registry);
        return dockerConfigAuthorization;
      }
      for (String registrySuffix : COMMON_CREDENTIAL_HELPERS.keySet()) {
        if (registry.endsWith(registrySuffix)) {
          try {
            String commonCredentialHelper = COMMON_CREDENTIAL_HELPERS.get(registrySuffix);
            if (commonCredentialHelper == null) {
              throw new IllegalStateException("No COMMON_CREDENTIAL_HELPERS should be null");
            }
            Authorization authorization = retrieveFromCredentialHelper(commonCredentialHelper);
            if (authorization != null) {
              return authorization;
            }
          } catch (NonexistentDockerCredentialHelperException ex) {
            if (ex.getMessage() != null) {
              buildConfiguration.getBuildLogger().warn(ex.getMessage());
            }
          }
        }
      }
      buildConfiguration
          .getBuildLogger()
          .info("No credentials could be retrieved for registry " + registry);
      return null;
    }
  }
  @VisibleForTesting
  Authorization retrieveFromCredentialHelper(String credentialHelperSuffix)
      throws NonexistentDockerCredentialHelperException, IOException {
    buildConfiguration
        .getBuildLogger()
        .info("Checking credentials from docker-credential-" + credentialHelperSuffix);
    try {
      Authorization authorization =
          dockerCredentialHelperFactory
              .withCredentialHelperSuffix(credentialHelperSuffix)
              .retrieve();
      logGotCredentialsFrom("docker-credential-" + credentialHelperSuffix);
      return authorization;
    } catch (NonexistentServerUrlDockerCredentialHelperException ex) {
      buildConfiguration
          .getBuildLogger()
          .info(
              "No credentials for " + registry + " in docker-credential-" + credentialHelperSuffix);
      return null;
    }
  }
  private void logGotCredentialsFrom(String credentialSource) {
    buildConfiguration.getBuildLogger().info("Using " + credentialSource + " for " + registry);
  }
}
