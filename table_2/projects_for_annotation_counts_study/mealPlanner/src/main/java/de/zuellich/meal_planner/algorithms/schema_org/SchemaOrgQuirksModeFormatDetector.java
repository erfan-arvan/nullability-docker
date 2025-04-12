package de.zuellich.meal_planner.algorithms.schema_org;
@Service
public class SchemaOrgQuirksModeFormatDetector implements FormatDetector {
  private final RecipeParser parser;
  public SchemaOrgQuirksModeFormatDetector(SchemaOrgQuirksModeParser parser) {
    this.parser = parser;
  }
  @Override
  public boolean isSupported(String source) {
    Document document = Jsoup.parse(source);
    Elements recipeElement =
        document.getElementsByAttributeValue("itemtype", "http:
    Elements ingredientsElements = document.getElementsByAttributeValue("itemprop", "ingredients");
    return !recipeElement.isEmpty() && !ingredientsElements.isEmpty();
  }
  @Override
  public RecipeFormat getFormat() {
    return RecipeFormat.SCHEMA_ORG_QUIRCKS_MODE;
  }
  @Override
  public RecipeParser getParserInstance() {
    return parser;
  }
}
