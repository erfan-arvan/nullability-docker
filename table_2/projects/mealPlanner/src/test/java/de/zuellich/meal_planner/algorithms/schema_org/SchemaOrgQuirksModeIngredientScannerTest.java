package de.zuellich.meal_planner.algorithms.schema_org;
public class SchemaOrgQuirksModeIngredientScannerTest extends FixtureBasedTest {
  @Test
  public void canParseWronglyMarkedIngredients() {
    String recipeSource = getResource("/fixtures/ingredientScanner/recipes/schema-org-03.html");
    AmountParser amountParser = new AmountParser();
    IngredientUnitLookup ingredientUnitLookup = IngredientUnitLookup.getInstance();
    IngredientMatcher ingredientMatcher = new IngredientMatcher(ingredientUnitLookup);
    IngredientScanner scanner =
        new SchemaOrgQuirksModeIngredientScanner(
            amountParser, ingredientUnitLookup, ingredientMatcher);
    List<Ingredient> ingredientList = scanner.parse(recipeSource);
    List<Ingredient> expectedIngredients = SchemaOrgExpectations.getIngredients03();
    assertEquals("Can parse quirky ingredients.", expectedIngredients, ingredientList);
  }
}
