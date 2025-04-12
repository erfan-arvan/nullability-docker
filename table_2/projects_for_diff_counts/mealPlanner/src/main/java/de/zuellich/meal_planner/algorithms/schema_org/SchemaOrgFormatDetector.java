package de.zuellich.meal_planner.algorithms.schema_org;
@Service
public class SchemaOrgFormatDetector implements FormatDetector {
  private RecipeParser parser;
  @Autowired
  public SchemaOrgFormatDetector(@Qualifier("schemaOrgParser") SchemaOrgParser parser) {
    this.parser = parser;
  }
  @Override
  public boolean isSupported(String htmlSource) {
    Document document = Jsoup.parse(htmlSource);
    return canFindSchemaOrgAnnotation(document);
  }
  private boolean canFindSchemaOrgAnnotation(Document document) {
    Elements recipeElement =
        document.getElementsByAttributeValue("itemtype", "http:
    Elements ingredientsElement =
        document.getElementsByAttributeValue("itemprop", "recipeIngredient");
    return !recipeElement.isEmpty() && !ingredientsElement.isEmpty();
  }
  @Override
  public RecipeFormat getFormat() {
    return RecipeFormat.SCHEMA_ORG;
  }
  @Override
  public RecipeParser getParserInstance() {
    return parser;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SchemaOrgFormatDetector that = (SchemaOrgFormatDetector) o;
    return Objects.equals(parser, that.parser);
  }
  @Override
  public int hashCode() {
    return Objects.hash(parser);
  }
}
