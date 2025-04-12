package de.zuellich.meal_planner.algorithms;
@RunWith(PowerMockRunner.class)
@PrepareForTest({Jsoup.class})
public class RecipeFetcherServiceTest extends FixtureBasedTest {
  @Test
  public void canFetchByURL() throws IOException {
    String fixture = getResource("/fixtures/ingredientScanner/recipes/schema-org-01.html");
    String expected = Jsoup.parse(fixture).html();
    Connection connection = Mockito.mock(Connection.class);
    Mockito.when(connection.get()).thenReturn(Jsoup.parse(fixture));
    PowerMockito.mockStatic(Jsoup.class);
    PowerMockito.when(Jsoup.connect(Mockito.anyString())).thenReturn(connection);
    String url = "";
    RecipeFetcherService service = new RecipeFetcherService();
    String result = service.fetchByURL(url);
    Assert.assertEquals(expected, result);
  }
}
