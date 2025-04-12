package com.google.cloud.tools.jib.image.json;
@JsonIgnoreProperties(ignoreUnknown = true)
public interface ManifestTemplate extends JsonTemplate {
  int getSchemaVersion();
}
