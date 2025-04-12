package de.zuellich.meal_planner.algorithms;
@Service
public class RecipeService {
  private final RecipeParserFactory parserFactory;
  private final RecipeFetcherService fetcherService;
  public RecipeService(RecipeParserFactory parserFactory, RecipeFetcherService fetcherService) {
    this.parserFactory = parserFactory;
    this.fetcherService = fetcherService;
  }
  public Recipe fromURL(String url) {
    String recipeSource = null;
    try {
      recipeSource = fetcherService.fetchByURL(url);
    } catch (IOException e) {
      throw new RecipeParseException("Error fetching recipe.", e);
    }
    if (recipeSource.isEmpty()) {
      throw new RecipeParseException("Recipe source is empty. Error downloading?");
    }
    RecipeParser parser = parserFactory.getParser(recipeSource);
    return parser.parse(recipeSource);
  }
}
