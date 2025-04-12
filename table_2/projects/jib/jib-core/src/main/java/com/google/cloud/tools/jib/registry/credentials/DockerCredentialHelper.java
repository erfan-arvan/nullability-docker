package com.google.cloud.tools.jib.registry.credentials;
public class DockerCredentialHelper {
  private final String serverUrl;
  private final String credentialHelperSuffix;
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class DockerCredentialsTemplate implements JsonTemplate {
     private String Username;
     private String Secret;
  }
  DockerCredentialHelper(String serverUrl, String credentialHelperSuffix) {
    this.serverUrl = serverUrl;
    this.credentialHelperSuffix = credentialHelperSuffix;
  }
  public Authorization retrieve()
      throws IOException, NonexistentServerUrlDockerCredentialHelperException,
          NonexistentDockerCredentialHelperException {
    try {
      String credentialHelper = "docker-credential-" + credentialHelperSuffix;
      String[] credentialHelperCommand = {credentialHelper, "get"};
      Process process = new ProcessBuilder(credentialHelperCommand).start();
      try (OutputStream processStdin = process.getOutputStream()) {
        processStdin.write(serverUrl.getBytes(StandardCharsets.UTF_8));
      }
      try (InputStreamReader processStdoutReader =
          new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
        String output = CharStreams.toString(processStdoutReader);
        if (output.contains("credentials not found in native keychain")) {
          throw new NonexistentServerUrlDockerCredentialHelperException(
              credentialHelper, serverUrl, output);
        }
        if (output.isEmpty()) {
          try (InputStreamReader processStderrReader =
              new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)) {
            String errorOutput = CharStreams.toString(processStderrReader);
            throw new NonexistentServerUrlDockerCredentialHelperException(
                credentialHelper, serverUrl, errorOutput);
          }
        }
        try {
          DockerCredentialsTemplate dockerCredentials =
              JsonTemplateMapper.readJson(output, DockerCredentialsTemplate.class);
          if (dockerCredentials.Username == null || dockerCredentials.Secret == null) {
            throw new NonexistentServerUrlDockerCredentialHelperException(
                credentialHelper, serverUrl, output);
          }
          return Authorizations.withBasicCredentials(
              dockerCredentials.Username, dockerCredentials.Secret);
        } catch (JsonProcessingException ex) {
          throw new NonexistentServerUrlDockerCredentialHelperException(
              credentialHelper, serverUrl, output);
        }
      }
    } catch (IOException ex) {
      if (ex.getMessage() == null) {
        throw ex;
      }
      if (ex.getMessage().contains("No such file or directory")
          || ex.getMessage().contains("cannot find the file")) {
        throw new NonexistentDockerCredentialHelperException(credentialHelperSuffix, ex);
      }
      throw ex;
    }
  }
}
