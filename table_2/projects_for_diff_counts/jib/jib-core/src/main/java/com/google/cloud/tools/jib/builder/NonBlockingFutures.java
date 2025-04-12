package com.google.cloud.tools.jib.builder;
class NonBlockingFutures {
  static <T> T get(Future<T> future) throws ExecutionException, InterruptedException {
    if (!future.isDone()) {
      throw new IllegalStateException("get() called before done");
    }
    return future.get();
  }
  private NonBlockingFutures() {}
}
