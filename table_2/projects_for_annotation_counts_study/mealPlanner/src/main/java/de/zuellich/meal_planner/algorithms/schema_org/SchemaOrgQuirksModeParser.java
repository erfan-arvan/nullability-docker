package de.zuellich.meal_planner.algorithms.schema_org;
@Service
public class SchemaOrgQuirksModeParser extends SchemaOrgParser {
  @Autowired
  public SchemaOrgQuirksModeParser(
      SchemaOrgRecipeScanner recipeScanner,
      SchemaOrgQuirksModeIngredientScanner ingredientScanner) {
    super(recipeScanner, ingredientScanner);
  }
  @Override
  public RecipeFormat getFormat() {
    return RecipeFormat.SCHEMA_ORG_QUIRCKS_MODE;
  }
}
