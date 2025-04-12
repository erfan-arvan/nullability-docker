package com.google.cloud.tools.jib.image;
@RunWith(MockitoJUnitRunner.class)
public class ImageTest {
  @Mock private Layer mockLayer;
  @Mock private ImageLayers<Layer> mockImageLayers;
  @InjectMocks private Image image;
  @Test
  public void test_smokeTest() throws LayerPropertyNotFoundException {
    ImmutableList<String> expectedEnvironment =
        ImmutableList.of("crepecake=is great", "VARIABLE=VALUE");
    image.setEnvironmentVariable("crepecake", "is great");
    image.setEnvironmentVariable("VARIABLE", "VALUE");
    image.setEntrypoint(Arrays.asList("some", "command"));
    image.addLayer(mockLayer);
    Mockito.verify(mockImageLayers).add(mockLayer);
    Assert.assertEquals(expectedEnvironment, image.getEnvironment());
    Assert.assertEquals(Arrays.asList("some", "command"), image.getEntrypoint());
  }
}
