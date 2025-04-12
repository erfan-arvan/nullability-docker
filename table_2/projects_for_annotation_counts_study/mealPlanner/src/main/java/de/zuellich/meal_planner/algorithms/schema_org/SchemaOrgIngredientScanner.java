package de.zuellich.meal_planner.algorithms.schema_org;
@Component
public class SchemaOrgIngredientScanner implements IngredientScanner {
  private final IngredientUnitLookup ingredientUnitLookup;
  private final AmountParser amountParser;
  public SchemaOrgIngredientScanner(
      AmountParser amountParser, IngredientUnitLookup ingredientUnitLookup) {
    this.amountParser = amountParser;
    this.ingredientUnitLookup = ingredientUnitLookup;
  }
  @Override
  public List<Ingredient> parse(String source) {
    Document document = Jsoup.parse(source);
    Elements ingredients = getIngredientElements(document);
    List<Ingredient> result = new ArrayList<>(ingredients.size());
    for (Element ingredient : ingredients) {
      result.add(parseIngredient(ingredient));
    }
    return result;
  }
  protected Elements getIngredientElements(Document document) {
    return document.getElementsByAttributeValue("itemprop", "recipeIngredient");
  }
  protected Ingredient parseIngredient(Element ingredient) {
    Elements typeElement = ingredient.select(".wprm-recipe-ingredient-name");
    Elements amountElement = ingredient.select(".wprm-recipe-ingredient-amount");
    Elements ingredientElement = ingredient.select(".wprm-recipe-ingredient-unit");
    String type = typeElement.text();
    float amount = amountParser.parseAmount(amountElement.text());
    IngredientUnit ingredientUnit = ingredientUnitLookup.lookup(ingredientElement.text());
    return new Ingredient(type, amount, ingredientUnit);
  }
  public IngredientUnitLookup getIngredientUnitLookup() {
    return ingredientUnitLookup;
  }
  public AmountParser getAmountParser() {
    return amountParser;
  }
}
