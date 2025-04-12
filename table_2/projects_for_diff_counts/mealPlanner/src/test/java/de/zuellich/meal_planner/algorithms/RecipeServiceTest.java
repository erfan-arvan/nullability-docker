package de.zuellich.meal_planner.algorithms;
public class RecipeServiceTest extends FixtureBasedTest {
  @Test
  public void returnsRecipeForSource() throws IOException {
    String url = "";
    String fixture = getResource("/fixtures/ingredientScanner/recipes/schema-org-01.html");
    Recipe expected = SchemaOrgExpectations.getSchemaOrg01();
    RecipeParserFactory mockedParserFactory = getMockedParserFactory(expected);
    RecipeFetcherService mockedFetcherService = mock(RecipeFetcherService.class);
    when(mockedFetcherService.fetchByURL(anyString())).thenReturn(fixture);
    RecipeService service = new RecipeService(mockedParserFactory, mockedFetcherService);
    Recipe result = service.fromURL(url);
    assertEquals(expected, result);
  }
  @Test(expected = RecipeParseException.class)
  public void throwsExceptionOnError() throws IOException {
    RecipeFetcherService mockedFetcherService = mock(RecipeFetcherService.class);
    when(mockedFetcherService.fetchByURL(anyString())).thenThrow(new IOException());
    RecipeParserFactory factory = getMockedParserFactory(null);
    RecipeService service = new RecipeService(factory, mockedFetcherService);
    String url = "";
    service.fromURL(url);
  }
  private RecipeParserFactory getMockedParserFactory(Recipe expected) {
    RecipeParser schemaOrgParser = mock(SchemaOrgParser.class);
    when(schemaOrgParser.parse(anyString())).thenReturn(expected);
    RecipeParserFactory parserFactory = mock(RecipeParserFactory.class);
    when(parserFactory.getParser(anyString())).thenReturn(schemaOrgParser);
    return parserFactory;
  }
}
