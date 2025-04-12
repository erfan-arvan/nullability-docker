package com.google.cloud.tools.jib.registry.credentials;
public class DockerCredentialHelperIntegrationTest {
  @Test
  public void testRetrieveGCR()
      throws IOException, NonexistentServerUrlDockerCredentialHelperException,
          NonexistentDockerCredentialHelperException, URISyntaxException, InterruptedException {
    new Command("docker-credential-gcr", "store")
        .run(Files.readAllBytes(Paths.get(Resources.getResource("credentials.json").toURI())));
    DockerCredentialHelper dockerCredentialHelper =
        new DockerCredentialHelperFactory("myregistry").withCredentialHelperSuffix("gcr");
    Authorization authorization = dockerCredentialHelper.retrieve();
    Assert.assertEquals("bXl1c2VybmFtZTpteXNlY3JldA==", authorization.getToken());
  }
  @Test
  public void testRetrieve_nonexistentCredentialHelper()
      throws IOException, NonexistentServerUrlDockerCredentialHelperException {
    try {
      DockerCredentialHelper fakeDockerCredentialHelper =
          new DockerCredentialHelperFactory("").withCredentialHelperSuffix("fake-cloud-provider");
      fakeDockerCredentialHelper.retrieve();
      Assert.fail("Retrieve should have failed for nonexistent credential helper");
    } catch (NonexistentDockerCredentialHelperException ex) {
      Assert.assertEquals(
          "The system does not have docker-credential-fake-cloud-provider CLI", ex.getMessage());
    }
  }
  @Test
  public void testRetrieve_nonexistentServerUrl()
      throws IOException, NonexistentDockerCredentialHelperException {
    try {
      DockerCredentialHelper fakeDockerCredentialHelper =
          new DockerCredentialHelperFactory("fake.server.url").withCredentialHelperSuffix("gcr");
      fakeDockerCredentialHelper.retrieve();
      Assert.fail("Retrieve should have failed for nonexistent server URL");
    } catch (NonexistentServerUrlDockerCredentialHelperException ex) {
      Assert.assertThat(
          ex.getMessage(),
          CoreMatchers.containsString(
              "The credential helper (docker-credential-gcr) has nothing for server URL: fake.server.url"));
    }
  }
}
