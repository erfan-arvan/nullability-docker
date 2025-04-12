package org.cache2k;
@Deprecated
public interface CacheOperationCompletionListener extends EventListener {
  void onCompleted();
  void onException(Throwable exception);
}
