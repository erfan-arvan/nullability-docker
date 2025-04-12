package com.google.cloud.tools.jib.registry;
public class RegistryUnauthorizedException extends RegistryException {
  private final String registry;
  private final String repository;
  public RegistryUnauthorizedException(
      String registry, String repository, HttpResponseException cause) {
    super(cause);
    this.registry = registry;
    this.repository = repository;
  }
  public String getRegistry() {
    return registry;
  }
  public String getRepository() {
    return repository;
  }
  public String getImageReference() {
    return registry + "/" + repository;
  }
  public HttpResponseException getHttpResponseException() {
    return (HttpResponseException) getCause();
  }
}
