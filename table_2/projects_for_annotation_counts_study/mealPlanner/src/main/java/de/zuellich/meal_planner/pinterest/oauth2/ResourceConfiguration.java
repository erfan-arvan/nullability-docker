package de.zuellich.meal_planner.pinterest.oauth2;
@Configuration
@EnableOAuth2Client
public class ResourceConfiguration {
  @Value("${meal_planner.oauth2.clientId}")
  private String clientId = "";
  @Value("${meal_planner.oauth2.clientSecret}")
  private String clientSecret = "";
  @Value("${meal_planner.oauth2.accessTokenUri}")
  private String accessTokenUri = "";
  @Value("${meal_planner.oauth2.authorizationUri}")
  private String authorizationUri = "";
  @Bean
  public OAuth2ProtectedResourceDetails pinterestOAuth2Configuration() {
    AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
    resourceDetails.setClientId(clientId);
    resourceDetails.setClientSecret(clientSecret);
    resourceDetails.setAccessTokenUri(accessTokenUri);
    resourceDetails.setUserAuthorizationUri(authorizationUri);
    resourceDetails.setClientAuthenticationScheme(AuthenticationScheme.query);
    resourceDetails.setScope(Collections.singletonList("read_public"));
    resourceDetails.setId("pinterest");
    resourceDetails.setAuthenticationScheme(AuthenticationScheme.query);
    return resourceDetails;
  }
  @Bean("pinterestRestTemplate")
  @Profile("production")
  public OAuth2RestTemplate pinterestRestTemplate(OAuth2ClientContext clientContext) {
    return new OAuth2RestTemplate(pinterestOAuth2Configuration(), clientContext);
  }
  @Bean("pinterestRestTemplate")
  @Profile("testing")
  public OAuth2RestTemplate staticallyConfiguredRestTemplate(OAuth2ClientContext clientContext) {
    AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
    resourceDetails.setClientId("clientId");
    resourceDetails.setClientSecret("clientSecret");
    resourceDetails.setAccessTokenUri("accessTokenUri");
    resourceDetails.setUserAuthorizationUri("authorizationUri");
    resourceDetails.setClientAuthenticationScheme(AuthenticationScheme.query);
    resourceDetails.setScope(Collections.singletonList("read_public"));
    resourceDetails.setId("pinterest");
    resourceDetails.setAuthenticationScheme(AuthenticationScheme.query);
    return new OAuth2RestTemplate(pinterestOAuth2Configuration(), clientContext);
  }
}
