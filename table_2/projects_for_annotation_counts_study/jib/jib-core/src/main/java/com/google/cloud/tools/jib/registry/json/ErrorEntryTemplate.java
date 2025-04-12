package com.google.cloud.tools.jib.registry.json;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorEntryTemplate implements JsonTemplate {
   private String code;
   private String message;
  public ErrorEntryTemplate(String code, String message) {
    this.code = code;
    this.message = message;
  }
  private ErrorEntryTemplate() {}
  public String getCode() {
    return code;
  }
  public String getMessage() {
    return message;
  }
}
