package de.zuellich.meal_planner.algorithms.schema_org;
@Service
public class SchemaOrgQuirksModeIngredientScanner extends SchemaOrgIngredientScanner {
  private IngredientMatcher ingredientMatcher;
  public SchemaOrgQuirksModeIngredientScanner(
      AmountParser amountParser,
      IngredientUnitLookup ingredientUnitLookup,
      IngredientMatcher ingredientMatcher) {
    super(amountParser, ingredientUnitLookup);
    this.ingredientMatcher = ingredientMatcher;
  }
  @Override
  protected Elements getIngredientElements(Document document) {
    return document.getElementsByAttributeValue("itemprop", "ingredients");
  }
  @Override
  protected Ingredient parseIngredient(Element ingredient) {
    String ingredientDescription = ingredient.text();
    IngredientMatcher.IngredientMatcherResult result =
        ingredientMatcher.match(ingredientDescription);
    float amount = 0;
    IngredientUnit unit = IngredientUnit.NULL;
    String name = "";
    if (result.isMatching()) {
      amount = getAmountParser().parseAmount(result.getAmount());
      unit = result.getUnit();
      name = result.getName();
    }
    return new Ingredient(name, amount, unit);
  }
}
