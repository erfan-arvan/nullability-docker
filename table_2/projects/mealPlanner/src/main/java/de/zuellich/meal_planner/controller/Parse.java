package de.zuellich.meal_planner.controller;
@RestController
public class Parse {
  private final RecipeService recipeService;
  @Autowired
  public Parse(RecipeService recipeService) {
    this.recipeService = recipeService;
  }
  @RequestMapping("/parse")
  public ResponseEntity<Object> parse(@RequestParam(value = "url") String url) {
    UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
    if (!urlValidator.isValid(url)) {
      return ResponseEntity.badRequest().build();
    }
    Recipe recipe = recipeService.fromURL(url);
    return ResponseEntity.ok(recipe);
  }
}
