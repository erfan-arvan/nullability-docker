package de.zuellich.meal_planner.algorithms.schema_org;
@RunWith(Parameterized.class)
public class SchemaOrgParserTest extends FixtureBasedTest {
  private static final String recipeFixtureBasePath = "/fixtures/ingredientScanner/recipes";
  @Parameterized.Parameter public String inputRecipePath;
  @Parameterized.Parameter(1)
  public Recipe expectedRecipe;
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {recipeFixtureBasePath + "/schema-org-01.html", SchemaOrgExpectations.getSchemaOrg01()},
          {recipeFixtureBasePath + "/schema-org-02.html", SchemaOrgExpectations.getSchemaOrg02()}
        });
  }
  @Test
  public void testCanReturnAProperRecipeInstance() {
    String source = getResource(inputRecipePath);
    SchemaOrgRecipeScanner recipeScanner = new SchemaOrgRecipeScanner();
    SchemaOrgIngredientScanner ingredientScanner =
        new SchemaOrgIngredientScanner(new AmountParser(), IngredientUnitLookup.getInstance());
    RecipeParser parser = new SchemaOrgParser(recipeScanner, ingredientScanner);
    Recipe recipe = parser.parse(source);
    assertEquals(expectedRecipe, recipe);
  }
}
