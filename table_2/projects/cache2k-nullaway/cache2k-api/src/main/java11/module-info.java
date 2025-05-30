module org.cache2k.api {
  requires static jsr305;
  exports org.cache2k;
  exports org.cache2k.config;
  exports org.cache2k.annotation;
  exports org.cache2k.event;
  exports org.cache2k.expiry;
  exports org.cache2k.integration;
  exports org.cache2k.io;
  exports org.cache2k.processor;
  exports org.cache2k.spi;
  exports org.cache2k.operation;
  uses org.cache2k.spi.Cache2kCoreProvider;
  uses org.cache2k.spi.Cache2kExtensionProvider;
}
