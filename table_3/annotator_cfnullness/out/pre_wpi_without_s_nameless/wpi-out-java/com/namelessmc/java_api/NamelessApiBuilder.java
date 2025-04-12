package com.namelessmc.java_api;

import com.github.mizosoft.methanol.Methanol;
import com.google.gson.GsonBuilder;
import com.namelessmc.java_api.logger.ApiLogger;
import com.namelessmc.java_api.logger.PrintStreamLogger;
import com.namelessmc.java_api.logger.Slf4jLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.*;
import java.time.Duration;
import java.util.concurrent.Executor;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class NamelessApiBuilder {

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String DEFAULT_USER_AGENT = "Nameless-Java-API";

    private static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int DEFAULT_RESPONSE_SIZE_LIMIT = 32 * 1024 * 1024;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull URL apiUrl;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiKey;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull GsonBuilder gsonBuilder;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable ApiLogger debugLogger = null;

    private final Methanol.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder httpClientBuilder;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int responseSizeLimit = DEFAULT_RESPONSE_SIZE_LIMIT;

    @org.checkerframework.dataflow.qual.Impure
    NamelessApiBuilder(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull URL apiUrl, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String apiKey) {
        try {
            this.apiUrl = apiUrl.toString().endsWith("/") ? apiUrl : new URL(apiUrl + "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.apiKey = apiKey;
        this.gsonBuilder = new GsonBuilder();
        this.gsonBuilder.disableHtmlEscaping();
        this.httpClientBuilder = Methanol.newBuilder().defaultHeader("X-Api-Key", this.apiKey).userAgent(DEFAULT_USER_AGENT).readTimeout(DEFAULT_TIMEOUT).requestTimeout(DEFAULT_TIMEOUT).connectTimeout(DEFAULT_TIMEOUT).autoAcceptEncoding(true);
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder userAgent(final String userAgent) {
        this.httpClientBuilder.userAgent(userAgent);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder debug(final boolean debug) {
        if (debug) {
            return this.withStdErrDebugLogging();
        } else {
            this.debugLogger = null;
            return this;
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withStdErrDebugLogging() {
        this.debugLogger = PrintStreamLogger.DEFAULT_INSTANCE;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder stdErrDebugLogger() {
        this.debugLogger = PrintStreamLogger.DEFAULT_INSTANCE;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withSlf4jDebugLogging() {
        this.debugLogger = Slf4jLogger.DEFAULT_INSTANCE;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder slf4jDebugLogger() {
        this.debugLogger = Slf4jLogger.DEFAULT_INSTANCE;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withCustomDebugLogger(final ApiLogger debugLogger) {
        this.debugLogger = debugLogger;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder customDebugLogger(final ApiLogger debugLogger) {
        this.debugLogger = debugLogger;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withTimeoutMillis(final int timeout) {
        return this.withTimeout(Duration.ofMillis(timeout));
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withTimeout(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Duration timeout) {
        this.httpClientBuilder.readTimeout(timeout).requestTimeout(timeout).connectTimeout(timeout);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder timeout(final Duration timeout) {
        this.httpClientBuilder.readTimeout(timeout).requestTimeout(timeout).connectTimeout(timeout);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withProxy(ProxySelector proxy) {
        this.httpClientBuilder.proxy(proxy);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder proxy(ProxySelector proxy) {
        this.httpClientBuilder.proxy(proxy);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withAuthenticator(Authenticator authenticator) {
        this.httpClientBuilder.authenticator(authenticator);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder authenticator(Authenticator authenticator) {
        this.httpClientBuilder.authenticator(authenticator);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withPrettyJson() {
        gsonBuilder.setPrettyPrinting();
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder pettyJsonRequests() {
        gsonBuilder.setPrettyPrinting();
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder withResponseSizeLimit(int responseSizeLimitBytes) {
        this.responseSizeLimit = responseSizeLimitBytes;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder responseSizeLimit(int responseSizeLimitBytes) {
        this.responseSizeLimit = responseSizeLimitBytes;
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessApiBuilder executor(final Executor executor) {
        this.httpClientBuilder.executor(executor);
        return this;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessAPI build() {
        return new NamelessAPI(new RequestHandler(this.apiUrl, this.httpClientBuilder.build(), this.gsonBuilder.create(), this.debugLogger, this.responseSizeLimit), this.apiUrl, this.apiKey);
    }
}
