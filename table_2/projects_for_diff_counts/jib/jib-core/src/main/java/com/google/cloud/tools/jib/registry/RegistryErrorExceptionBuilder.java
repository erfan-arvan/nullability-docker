package com.google.cloud.tools.jib.registry;
class RegistryErrorExceptionBuilder {
   private final Throwable cause;
  private final StringBuilder errorMessageBuilder = new StringBuilder();
  private boolean firstErrorReason = true;
  private static String getReason( String errorCodeString,  String message) {
    if (message == null) {
      message = "no details";
    }
    try {
      if (errorCodeString == null) {
        throw new IllegalArgumentException();
      }
      ErrorCodes errorCode = ErrorCodes.valueOf(errorCodeString);
      if (errorCode == ErrorCodes.MANIFEST_INVALID || errorCode == ErrorCodes.BLOB_UNKNOWN) {
        return message + " (something went wrong)";
      } else if (errorCode == ErrorCodes.MANIFEST_UNKNOWN
          || errorCode == ErrorCodes.TAG_INVALID
          || errorCode == ErrorCodes.MANIFEST_UNVERIFIED) {
        return message;
      } else {
        return "other: " + message;
      }
    } catch (IllegalArgumentException ex) {
      return "unknown: " + message;
    }
  }
  RegistryErrorExceptionBuilder(String method,  Throwable cause) {
    this.cause = cause;
    errorMessageBuilder.append("Tried to ");
    errorMessageBuilder.append(method);
    errorMessageBuilder.append(" but failed because: ");
  }
  RegistryErrorExceptionBuilder(String method) {
    this(method, null);
  }
  RegistryErrorExceptionBuilder addReason(ErrorEntryTemplate errorEntry) {
    String reason = getReason(errorEntry.getCode(), errorEntry.getMessage());
    addReason(reason);
    return this;
  }
  RegistryErrorExceptionBuilder addReason(String reason) {
    if (!firstErrorReason) {
      errorMessageBuilder.append(", ");
    }
    errorMessageBuilder.append(reason);
    firstErrorReason = false;
    return this;
  }
  RegistryErrorException build() {
    errorMessageBuilder.append(
        " | If this is a bug, please file an issue at " + ProjectInfo.GITHUB_NEW_ISSUE_URL);
    return new RegistryErrorException(errorMessageBuilder.toString(), cause);
  }
}
