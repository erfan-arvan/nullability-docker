package de.zuellich.meal_planner;
public final class SSLUtil {
  private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER =
      new TrustManager[] {
        new X509TrustManager() {
          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }
          @Override
          public void checkClientTrusted(final X509Certificate[] certs, final String authType) {}
          @Override
          public void checkServerTrusted(final X509Certificate[] certs, final String authType) {}
        }
      };
  private SSLUtil() {
    throw new UnsupportedOperationException("Do not instantiate libraries.");
  }
  public static void turnOffSslChecking() throws NoSuchAlgorithmException, KeyManagementException {
    final SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, SSLUtil.UNQUESTIONING_TRUST_MANAGER, null);
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }
  public static void turnOnSslChecking() throws KeyManagementException, NoSuchAlgorithmException {
    SSLContext.getInstance("SSL").init(null, null, null);
  }
}
