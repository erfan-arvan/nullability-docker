package com.google.cloud.tools.jib.registry.credentials;
public class DockerConfigCredentialRetriever {
  private static final Path DOCKER_CONFIG_FILE =
      Paths.get(System.getProperty("user.home")).resolve(".docker").resolve("config.json");
  private final String registry;
  private final Path dockerConfigFile;
  private final DockerCredentialHelperFactory dockerCredentialHelperFactory;
  public DockerConfigCredentialRetriever(String registry) {
    this(registry, DOCKER_CONFIG_FILE);
  }
  @VisibleForTesting
  DockerConfigCredentialRetriever(String registry, Path dockerConfigFile) {
    this.registry = registry;
    this.dockerConfigFile = dockerConfigFile;
    this.dockerCredentialHelperFactory = new DockerCredentialHelperFactory(registry);
  }
  @VisibleForTesting
  DockerConfigCredentialRetriever(
      String registry,
      Path dockerConfigFile,
      DockerCredentialHelperFactory dockerCredentialHelperFactory) {
    this.registry = registry;
    this.dockerConfigFile = dockerConfigFile;
    this.dockerCredentialHelperFactory = dockerCredentialHelperFactory;
  }
  public Authorization retrieve() {
    DockerConfigTemplate dockerConfigTemplate = loadDockerConfigTemplate();
    if (dockerConfigTemplate == null) {
      return null;
    }
    String auth = dockerConfigTemplate.getAuthFor(registry);
    if (auth != null) {
      return Authorizations.withBasicToken(auth);
    }
    String credentialHelperSuffix = dockerConfigTemplate.getCredentialHelperFor(registry);
    if (credentialHelperSuffix != null) {
      try {
        return dockerCredentialHelperFactory
            .withCredentialHelperSuffix(credentialHelperSuffix)
            .retrieve();
      } catch (IOException
          | NonexistentServerUrlDockerCredentialHelperException
          | NonexistentDockerCredentialHelperException ex) {
      }
    }
    return null;
  }
  private DockerConfigTemplate loadDockerConfigTemplate() {
    if (!Files.exists(dockerConfigFile)) {
      return null;
    }
    try {
      return JsonTemplateMapper.readJsonFromFile(dockerConfigFile, DockerConfigTemplate.class);
    } catch (IOException ex) {
      return null;
    }
  }
}
