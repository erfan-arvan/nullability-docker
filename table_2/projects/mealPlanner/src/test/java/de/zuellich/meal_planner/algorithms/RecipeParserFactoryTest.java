package de.zuellich.meal_planner.algorithms;
public class RecipeParserFactoryTest extends FixtureBasedTest {
  private RecipeParserFactory getInstance() {
    SchemaOrgParser schemaOrgParser = mock(SchemaOrgParser.class);
    when(schemaOrgParser.getFormat()).thenCallRealMethod();
    SchemaOrgQuirksModeParser schemaOrgQuirksModeParser = mock(SchemaOrgQuirksModeParser.class);
    when(schemaOrgQuirksModeParser.getFormat()).thenCallRealMethod();
    Set<FormatDetector> detectors = new HashSet<>(2);
    detectors.add(new SchemaOrgFormatDetector(schemaOrgParser));
    detectors.add(new SchemaOrgQuirksModeFormatDetector(schemaOrgQuirksModeParser));
    return new RecipeParserFactory(detectors);
  }
  @Test
  public void returnsSchemaOrgParser() {
    String source = getResource("/fixtures/ingredientScanner/recipes/schema-org-01.html");
    RecipeParserFactory factory = getInstance();
    RecipeParser actualParser = factory.getParser(source);
    assertEquals(RecipeFormat.SCHEMA_ORG, actualParser.getFormat());
  }
  @Test
  public void returnsSchemaOrgQuirksModeParser() {
    String source = getResource("/fixtures/ingredientScanner/recipes/schema-org-03.html");
    RecipeParserFactory factory = getInstance();
    RecipeParser actualParser = factory.getParser(source);
    assertEquals(RecipeFormat.SCHEMA_ORG_QUIRCKS_MODE, actualParser.getFormat());
  }
  @Test
  public void returnsNullParser() {
    RecipeParserFactory factory = getInstance();
    RecipeParser actualParser = factory.getParser("");
    assertEquals(RecipeFormat.UNKNOWN, actualParser.getFormat());
  }
}
