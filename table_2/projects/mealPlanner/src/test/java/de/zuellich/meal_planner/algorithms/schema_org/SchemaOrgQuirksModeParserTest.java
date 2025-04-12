package de.zuellich.meal_planner.algorithms.schema_org;
@RunWith(Parameterized.class)
public class SchemaOrgQuirksModeParserTest extends FixtureBasedTest {
  private static final String recipeFixtureBasePath = "/fixtures/ingredientScanner/recipes";
  @Parameterized.Parameter public String recipeSourcePath;
  @Parameterized.Parameter(1)
  public Recipe expectedRecipe;
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {recipeFixtureBasePath + "/schema-org-03.html", SchemaOrgExpectations.getSchemaOrg03()},
        });
  }
  @Test
  public void canParseQuirkySchemaOrgRecipes() {
    IngredientUnitLookup ingredientUnitLookup = IngredientUnitLookup.getInstance();
    SchemaOrgRecipeScanner recipeScanner = new SchemaOrgRecipeScanner();
    SchemaOrgQuirksModeIngredientScanner ingredientScanner =
        new SchemaOrgQuirksModeIngredientScanner(
            new AmountParser(), ingredientUnitLookup, new IngredientMatcher(ingredientUnitLookup));
    RecipeParser parser = new SchemaOrgQuirksModeParser(recipeScanner, ingredientScanner);
    String recipeSource = getResource(recipeSourcePath);
    Recipe parsedRecipe = parser.parse(recipeSource);
    assertEquals(expectedRecipe, parsedRecipe);
  }
}
