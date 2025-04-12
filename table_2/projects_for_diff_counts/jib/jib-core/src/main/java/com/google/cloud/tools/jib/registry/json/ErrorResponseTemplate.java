package com.google.cloud.tools.jib.registry.json;
public class ErrorResponseTemplate implements JsonTemplate {
  private final List<ErrorEntryTemplate> errors = new ArrayList<>();
  public List<ErrorEntryTemplate> getErrors() {
    return Collections.unmodifiableList(errors);
  }
  @VisibleForTesting
  public ErrorResponseTemplate addError(ErrorEntryTemplate errorEntryTemplate) {
    errors.add(errorEntryTemplate);
    return this;
  }
}
