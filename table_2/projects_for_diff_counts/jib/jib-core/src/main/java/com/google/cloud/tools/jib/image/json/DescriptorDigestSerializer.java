package com.google.cloud.tools.jib.image.json;
public class DescriptorDigestSerializer extends JsonSerializer<DescriptorDigest> {
  @Override
  public void serialize(
      DescriptorDigest value, JsonGenerator jsonGenerator, SerializerProvider ignored)
      throws IOException {
    jsonGenerator.writeString(value.toString());
  }
}
