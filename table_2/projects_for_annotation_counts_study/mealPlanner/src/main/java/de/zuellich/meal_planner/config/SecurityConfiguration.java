package de.zuellich.meal_planner.config;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
  }
  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(User.withUsername("test").password("test").roles("USER").build());
    return manager;
  }
}
