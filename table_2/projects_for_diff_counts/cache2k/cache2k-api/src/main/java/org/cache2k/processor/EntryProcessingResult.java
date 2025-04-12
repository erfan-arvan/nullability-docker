package org.cache2k.processor;
public interface EntryProcessingResult<R> {
  R getResult();
  Throwable getException();
}
