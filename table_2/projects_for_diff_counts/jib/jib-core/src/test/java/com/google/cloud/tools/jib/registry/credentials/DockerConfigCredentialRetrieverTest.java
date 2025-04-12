package com.google.cloud.tools.jib.registry.credentials;
@RunWith(MockitoJUnitRunner.class)
public class DockerConfigCredentialRetrieverTest {
  @Mock private Authorization mockAuthorization;
  @Mock private DockerCredentialHelper mockDockerCredentialHelper;
  @Mock private DockerCredentialHelperFactory mockDockerCredentialHelperFactory;
  private Path dockerConfigFile;
  @Before
  public void setUp()
      throws URISyntaxException, NonexistentServerUrlDockerCredentialHelperException,
          NonexistentDockerCredentialHelperException, IOException {
    dockerConfigFile = Paths.get(Resources.getResource("json/dockerconfig.json").toURI());
    Mockito.when(mockDockerCredentialHelper.retrieve()).thenReturn(mockAuthorization);
  }
  @Test
  public void testRetrieve_nonexistentDockerConfigFile() {
    DockerConfigCredentialRetriever dockerConfigCredentialRetriever =
        new DockerConfigCredentialRetriever("some registry", Paths.get("fake/path"));
    Assert.assertNull(dockerConfigCredentialRetriever.retrieve());
  }
  @Test
  public void testRetrieve_hasAuth() {
    DockerConfigCredentialRetriever dockerConfigCredentialRetriever =
        new DockerConfigCredentialRetriever("some registry", dockerConfigFile, null);
    Authorization authorization = dockerConfigCredentialRetriever.retrieve();
    Assert.assertNotNull(authorization);
    Assert.assertEquals("some auth", authorization.getToken());
  }
  @Test
  public void testRetrieve_useCredsStore() {
    Mockito.when(
            mockDockerCredentialHelperFactory.withCredentialHelperSuffix("some credential store"))
        .thenReturn(mockDockerCredentialHelper);
    DockerConfigCredentialRetriever dockerConfigCredentialRetriever =
        new DockerConfigCredentialRetriever(
            "just registry", dockerConfigFile, mockDockerCredentialHelperFactory);
    Authorization authorization = dockerConfigCredentialRetriever.retrieve();
    Assert.assertNotNull(authorization);
    Assert.assertEquals(mockAuthorization, authorization);
  }
  @Test
  public void testRetrieve_useCredHelper() {
    Mockito.when(
            mockDockerCredentialHelperFactory.withCredentialHelperSuffix(
                "another credential helper"))
        .thenReturn(mockDockerCredentialHelper);
    DockerConfigCredentialRetriever dockerConfigCredentialRetriever =
        new DockerConfigCredentialRetriever(
            "another registry", dockerConfigFile, mockDockerCredentialHelperFactory);
    Authorization authorization = dockerConfigCredentialRetriever.retrieve();
    Assert.assertNotNull(authorization);
    Assert.assertEquals(mockAuthorization, authorization);
  }
  @Test
  public void testRetrieve_none() {
    DockerConfigCredentialRetriever dockerConfigCredentialRetriever =
        new DockerConfigCredentialRetriever("unknown registry", dockerConfigFile);
    Assert.assertNull(dockerConfigCredentialRetriever.retrieve());
  }
}
