package com.google.cloud.tools.jib.registry.credentials;
public class NonexistentServerUrlDockerCredentialHelperException extends Exception {
  NonexistentServerUrlDockerCredentialHelperException(
      String credentialHelper, String serverUrl, String credentialHelperOutput) {
    super(
        "The credential helper ("
            + credentialHelper
            + ") has nothing for server URL: "
            + serverUrl
            + "\n\nGot output:\n\n"
            + credentialHelperOutput);
  }
}
