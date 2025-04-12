package com.google.cloud.tools.jib.http;
public class Connection implements Closeable {
  private HttpRequestFactory requestFactory = new ApacheHttpTransport().createRequestFactory();
   private HttpResponse httpResponse;
  private final GenericUrl url;
  public Connection(URL url) {
    this.url = new GenericUrl(url);
  }
  @Override
  public void close() throws IOException {
    if (httpResponse == null) {
      return;
    }
    httpResponse.disconnect();
  }
  public Response get(Request request) throws IOException {
    return send(HttpMethods.GET, request);
  }
  public Response post(Request request) throws IOException {
    return send(HttpMethods.POST, request);
  }
  public Response put(Request request) throws IOException {
    return send(HttpMethods.PUT, request);
  }
  public Response send(String httpMethod, Request request) throws IOException {
    httpResponse =
        requestFactory
            .buildRequest(httpMethod, url, request.getHttpContent())
            .setHeaders(request.getHeaders())
            .execute();
    return new Response(httpResponse);
  }
}
