package com.google.cloud.tools.jib.builder;
@RunWith(MockitoJUnitRunner.class)
public class RetrieveRegistryCredentialsStepTest {
  @Mock private BuildConfiguration mockBuildConfiguration;
  @Mock private BuildLogger mockBuildLogger;
  @Mock private DockerCredentialHelperFactory mockDockerCredentialHelperFactory;
  @Mock private DockerCredentialHelper mockDockerCredentialHelper;
  @Mock private DockerCredentialHelper mockNonexistentServerUrlDockerCredentialHelper;
  @Mock private DockerCredentialHelper mockNonexistentDockerCredentialHelper;
  @Mock private Authorization mockAuthorization;
  @Mock private DockerConfigCredentialRetriever mockDockerConfigCredentialRetriever;
  @Mock
  private NonexistentServerUrlDockerCredentialHelperException
      mockNonexistentServerUrlDockerCredentialHelperException;
  @Mock
  private NonexistentDockerCredentialHelperException mockNonexistentDockerCredentialHelperException;
  private static final String fakeTargetRegistry = "someRegistry";
  @Before
  public void setUpMocks()
      throws NonexistentServerUrlDockerCredentialHelperException,
          NonexistentDockerCredentialHelperException, IOException {
    Mockito.when(mockBuildConfiguration.getBuildLogger()).thenReturn(mockBuildLogger);
    Mockito.when(mockDockerCredentialHelper.retrieve()).thenReturn(mockAuthorization);
    Mockito.when(mockNonexistentServerUrlDockerCredentialHelper.retrieve())
        .thenThrow(mockNonexistentServerUrlDockerCredentialHelperException);
    Mockito.when(mockNonexistentDockerCredentialHelper.retrieve())
        .thenThrow(mockNonexistentDockerCredentialHelperException);
  }
  @Test
  public void testCall_useCredentialHelper()
      throws IOException, NonexistentDockerCredentialHelperException,
          NonexistentServerUrlDockerCredentialHelperException {
    Mockito.when(mockBuildConfiguration.getCredentialHelperNames())
        .thenReturn(Arrays.asList("someCredentialHelper", "someOtherCredentialHelper"));
    Mockito.when(
            mockDockerCredentialHelperFactory.withCredentialHelperSuffix("someCredentialHelper"))
        .thenReturn(mockNonexistentServerUrlDockerCredentialHelper);
    Mockito.when(
            mockDockerCredentialHelperFactory.withCredentialHelperSuffix(
                "someOtherCredentialHelper"))
        .thenReturn(mockDockerCredentialHelper);
    Assert.assertEquals(
        mockAuthorization, makeRetrieveRegistryCredentialsStep(fakeTargetRegistry).call());
    Mockito.verify(mockBuildLogger)
        .info("Using docker-credential-someOtherCredentialHelper for " + fakeTargetRegistry);
  }
  @Test
  public void testCall_useKnownRegistryCredentials()
      throws IOException, NonexistentDockerCredentialHelperException,
          NonexistentServerUrlDockerCredentialHelperException {
    Mockito.when(mockBuildConfiguration.getCredentialHelperNames())
        .thenReturn(Collections.emptyList());
    Mockito.when(mockBuildConfiguration.getKnownRegistryCredentials())
        .thenReturn(
            RegistryCredentials.of(fakeTargetRegistry, "credentialSource", mockAuthorization));
    Assert.assertEquals(
        mockAuthorization, makeRetrieveRegistryCredentialsStep(fakeTargetRegistry).call());
    Mockito.verify(mockBuildLogger).info("Using credentialSource for " + fakeTargetRegistry);
  }
  @Test
  public void testCall_useDockerConfig()
      throws IOException, NonexistentDockerCredentialHelperException,
          NonexistentServerUrlDockerCredentialHelperException {
    Mockito.when(mockBuildConfiguration.getCredentialHelperNames())
        .thenReturn(Collections.emptyList());
    Mockito.when(mockBuildConfiguration.getKnownRegistryCredentials())
        .thenReturn(RegistryCredentials.none());
    Mockito.when(mockDockerConfigCredentialRetriever.retrieve()).thenReturn(mockAuthorization);
    Assert.assertEquals(
        mockAuthorization, makeRetrieveRegistryCredentialsStep(fakeTargetRegistry).call());
    Mockito.verify(mockBuildLogger)
        .info("Using credentials from Docker config for " + fakeTargetRegistry);
  }
  @Test
  public void testCall_inferCommonCredentialHelpers()
      throws IOException, NonexistentDockerCredentialHelperException,
          NonexistentServerUrlDockerCredentialHelperException {
    Mockito.when(mockBuildConfiguration.getCredentialHelperNames())
        .thenReturn(Collections.emptyList());
    Mockito.when(mockBuildConfiguration.getKnownRegistryCredentials())
        .thenReturn(RegistryCredentials.none());
    Mockito.when(mockDockerConfigCredentialRetriever.retrieve()).thenReturn(null);
    Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("gcr"))
        .thenReturn(mockDockerCredentialHelper);
    Mockito.when(mockDockerCredentialHelperFactory.withCredentialHelperSuffix("ecr-login"))
        .thenReturn(mockNonexistentDockerCredentialHelper);
    Assert.assertEquals(
        mockAuthorization, makeRetrieveRegistryCredentialsStep("something.gcr.io").call());
    Mockito.verify(mockBuildLogger).info("Using docker-credential-gcr for something.gcr.io");
    Mockito.when(mockNonexistentDockerCredentialHelperException.getMessage()).thenReturn("warning");
    Assert.assertEquals(
        null, makeRetrieveRegistryCredentialsStep("something.amazonaws.com").call());
    Mockito.verify(mockBuildLogger).warn("warning");
  }
  private RetrieveRegistryCredentialsStep makeRetrieveRegistryCredentialsStep(String registry) {
    Mockito.when(mockBuildConfiguration.getTargetRegistry()).thenReturn(fakeTargetRegistry);
    return new RetrieveRegistryCredentialsStep(
        mockBuildConfiguration,
        registry,
        mockDockerCredentialHelperFactory,
        mockDockerConfigCredentialRetriever);
  }
}
