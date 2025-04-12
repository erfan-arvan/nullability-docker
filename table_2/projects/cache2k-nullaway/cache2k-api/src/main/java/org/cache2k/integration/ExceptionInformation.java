package org.cache2k.integration;
@Deprecated
public interface ExceptionInformation {
  ExceptionPropagator getExceptionPropagator();
  Throwable getException();
  int getRetryCount();
  long getSinceTime();
  long getLoadTime();
  long getUntil();
}
