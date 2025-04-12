package de.zuellich.meal_planner.config;
@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/public/");
  }
}
