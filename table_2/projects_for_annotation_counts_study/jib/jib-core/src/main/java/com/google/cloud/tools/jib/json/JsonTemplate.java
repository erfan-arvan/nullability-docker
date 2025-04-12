package com.google.cloud.tools.jib.json;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.ANY,
  getterVisibility = JsonAutoDetect.Visibility.NONE,
  setterVisibility = JsonAutoDetect.Visibility.NONE,
  creatorVisibility = JsonAutoDetect.Visibility.NONE
)
public interface JsonTemplate {}
