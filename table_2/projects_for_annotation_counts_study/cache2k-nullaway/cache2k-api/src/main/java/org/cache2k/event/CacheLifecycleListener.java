package org.cache2k.event;
public interface CacheLifecycleListener extends EventListener {
  CompletableFuture<Void> COMPLETE = new CompletableFuture<Void>() {
    public CompletableFuture<Void> newIncompleteFuture() {
      return this;
    }
    public void obtrudeValue(Void value) { throw new UnsupportedOperationException(); }
    public void obtrudeException(Throwable ex) { throw new UnsupportedOperationException(); }
  };
}
