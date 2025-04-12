/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.tools.jib.registry.credentials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.tools.jib.http.Authorization;
import com.google.cloud.tools.jib.http.Authorizations;
import com.google.cloud.tools.jib.json.JsonTemplate;
import com.google.cloud.tools.jib.json.JsonTemplateMapper;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;

/**
 * Retrieves Docker credentials with a Docker credential helper.
 *
 * @see <a
 *     href="https://github.com/docker/docker-credential-helpers">https://github.com/docker/docker-credential-helpers</a>
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class DockerCredentialHelper {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String serverUrl;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialHelperSuffix;

    /**
     * Template for a Docker credential helper output.
     */
    private static class DockerCredentialsTemplate implements JsonTemplate {

        private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull String Username;

        private @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull String Secret;
    }

    /**
     * Construct with {@link DockerCredentialHelperFactory}.
     *
     * @param serverUrl the server URL to pass into the credential helper
     * @param credentialHelperSuffix the credential helper CLI suffix
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    DockerCredentialHelper(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String serverUrl, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String credentialHelperSuffix) {
        this.serverUrl = serverUrl;
        this.credentialHelperSuffix = credentialHelperSuffix;
    }

    /**
     * Retrieves the Docker credentials by calling the corresponding CLI.
     *
     * <p>The credential helper CLI is called in the form:
     *
     * <pre>{@code
     * echo -n <server URL> | docker-credential-<credential helper suffix> get
     * }</pre>
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Authorization retrieve() throws IOException, NonexistentServerUrlDockerCredentialHelperException, NonexistentDockerCredentialHelperException {
        try {
            String credentialHelper = "docker-credential-" + credentialHelperSuffix;
            String[] credentialHelperCommand = { credentialHelper, "get" };
            Process process = new ProcessBuilder(credentialHelperCommand).start();
            try (OutputStream processStdin = process.getOutputStream()) {
                processStdin.write(serverUrl.getBytes(StandardCharsets.UTF_8));
            }
            try (InputStreamReader processStdoutReader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)) {
                String output = CharStreams.toString(processStdoutReader);
                // Throws an exception if the credential store does not have credentials for serverUrl.
                if (output.contains("credentials not found in native keychain")) {
                    throw new NonexistentServerUrlDockerCredentialHelperException(credentialHelper, serverUrl, output);
                }
                if (output.isEmpty()) {
                    try (InputStreamReader processStderrReader = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)) {
                        String errorOutput = CharStreams.toString(processStderrReader);
                        throw new NonexistentServerUrlDockerCredentialHelperException(credentialHelper, serverUrl, errorOutput);
                    }
                }
                try {
                    DockerCredentialsTemplate dockerCredentials = JsonTemplateMapper.readJson(output, DockerCredentialsTemplate.class);
                    if (dockerCredentials.Username == null || dockerCredentials.Secret == null) {
                        throw new NonexistentServerUrlDockerCredentialHelperException(credentialHelper, serverUrl, output);
                    }
                    return Authorizations.withBasicCredentials(dockerCredentials.Username, dockerCredentials.Secret);
                } catch (JsonProcessingException ex) {
                    throw new NonexistentServerUrlDockerCredentialHelperException(credentialHelper, serverUrl, output);
                }
            }
        } catch (IOException ex) {
            if (ex.getMessage() == null) {
                throw ex;
            }
            // Checks if the failure is due to a nonexistent credential helper CLI.
            if (ex.getMessage().contains("No such file or directory") || ex.getMessage().contains("cannot find the file")) {
                throw new NonexistentDockerCredentialHelperException(credentialHelperSuffix, ex);
            }
            throw ex;
        }
    }
}
