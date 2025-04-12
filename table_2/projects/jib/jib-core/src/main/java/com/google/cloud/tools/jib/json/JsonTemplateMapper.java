package com.google.cloud.tools.jib.json;
public class JsonTemplateMapper {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  public static <T extends JsonTemplate> T readJsonFromFile(Path jsonFile, Class<T> templateClass)
      throws IOException {
    return objectMapper.readValue(Files.newInputStream(jsonFile), templateClass);
  }
  public static <T extends JsonTemplate> T readJson(String jsonString, Class<T> templateClass)
      throws IOException {
    return objectMapper.readValue(jsonString, templateClass);
  }
  public static Blob toBlob(JsonTemplate template) {
    return Blobs.from(
        outputStream -> {
          objectMapper.writeValue(outputStream, template);
        });
  }
  private JsonTemplateMapper() {}
}
