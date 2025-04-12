package de.zuellich.meal_planner.algorithms.schema_org;
@Service
public class SchemaOrgRecipeScanner implements RecipeScanner {
  @Override
  public Recipe parse(String source) {
    Document document = Jsoup.parse(source);
    Elements recipeRoot =
        document.getElementsByAttributeValue("itemtype", "http:
    String name = parseName(recipeRoot);
    String url = parseURL(recipeRoot);
    List<Ingredient> emptyList = Collections.emptyList();
    return new Recipe(name, emptyList, url);
  }
  private String parseURL(Elements recipeRoot) {
    Elements url = recipeRoot.select("[itemprop=url]");
    if (url.isEmpty()) {
      return "";
    } else {
      return url.first().attr("href");
    }
  }
  private String parseName(Elements recipeRoot) {
    Elements elements = recipeRoot.select("[itemprop=name]");
    if (elements.isEmpty()) {
      return "";
    } else {
      return elements.text();
    }
  }
}
