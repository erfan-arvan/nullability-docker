package com.google.cloud.tools.jib.registry.credentials;
public class NonexistentDockerCredentialHelperException extends Exception {
  NonexistentDockerCredentialHelperException(String credentialHelperSuffix, Throwable cause) {
    super("The system does not have docker-credential-" + credentialHelperSuffix + " CLI", cause);
  }
}
