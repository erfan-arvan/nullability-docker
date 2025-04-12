package de.zuellich.meal_planner.pinterest.services;
@Import(MealPlanner.class)
@Configuration
public class ContextConfigurationTest {
  @Bean("pinterestRestTemplate")
  public OAuth2RestOperations getOAuth2RestOperations() {
    return mock(OAuth2RestOperations.class);
  }
}
