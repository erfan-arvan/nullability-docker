package com.google.cloud.tools.jib.registry;
@RunWith(MockitoJUnitRunner.class)
public class RegistryErrorExceptionBuilderTest {
  @Mock private HttpResponseException mockHttpResponseException;
  @Test
  public void testAddErrorEntry() {
    RegistryErrorExceptionBuilder builder =
        new RegistryErrorExceptionBuilder("do something", mockHttpResponseException);
    builder.addReason(
        new ErrorEntryTemplate(ErrorCodes.MANIFEST_INVALID.name(), "manifest invalid"));
    builder.addReason(new ErrorEntryTemplate(ErrorCodes.BLOB_UNKNOWN.name(), "blob unknown"));
    builder.addReason(
        new ErrorEntryTemplate(ErrorCodes.MANIFEST_UNKNOWN.name(), "manifest unknown"));
    builder.addReason(new ErrorEntryTemplate(ErrorCodes.TAG_INVALID.name(), "tag invalid"));
    builder.addReason(
        new ErrorEntryTemplate(ErrorCodes.MANIFEST_UNVERIFIED.name(), "manifest unverified"));
    builder.addReason(
        new ErrorEntryTemplate(ErrorCodes.UNSUPPORTED.name(), "some other error happened"));
    builder.addReason(new ErrorEntryTemplate("unknown", "some unknown error happened"));
    try {
      throw builder.build();
    } catch (RegistryErrorException ex) {
      Assert.assertEquals(
          "Tried to do something but failed because: manifest invalid (something went wrong), blob unknown (something went wrong), manifest unknown, tag invalid, manifest unverified, other: some other error happened, unknown: some unknown error happened | If this is a bug, please file an issue at https:
          ex.getMessage());
    }
  }
}
