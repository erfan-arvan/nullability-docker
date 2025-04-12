package com.google.cloud.tools.jib.image.json;
public class DescriptorDigestDeserializer extends JsonDeserializer<DescriptorDigest> {
  @Override
  public DescriptorDigest deserialize(JsonParser jsonParser, DeserializationContext ignored)
      throws IOException {
    try {
      return DescriptorDigest.fromDigest(jsonParser.getValueAsString());
    } catch (DigestException ex) {
      throw new IOException(ex);
    }
  }
}
