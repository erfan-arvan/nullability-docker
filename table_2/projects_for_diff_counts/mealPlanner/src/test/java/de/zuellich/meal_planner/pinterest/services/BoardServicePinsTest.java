package de.zuellich.meal_planner.pinterest.services;
public class BoardServicePinsTest extends FixtureBasedTest {
  private IBoardService service;
  private OAuth2RestTemplate getRestTemplate() {
    OAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(ACCESS_TOKEN);
    DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
    clientContext.setAccessToken(accessToken);
    return new OAuth2RestTemplate(new AuthorizationCodeResourceDetails(), clientContext);
  }
  private IBoardService getBoardService(OAuth2RestTemplate restTemplate) {
    return new BoardService(restTemplate);
  }
  @Before
  public void setUp() {
    final String pinResponseJSON =
        getResource("/fixtures/pinterest/responses/v1/board_pins_with_recipe_name.json");
    OAuth2RestTemplate restTemplate = getRestTemplate();
    MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
    server
        .expect(
            requestTo(
                "https:
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "bearer " + ACCESS_TOKEN))
        .andRespond(withSuccess(pinResponseJSON, MediaType.APPLICATION_JSON));
    this.service = getBoardService(restTemplate);
  }
  @Test
  public void returnsRecipeNameFromPin() {
    List<Pin> pins = this.service.getPins("exampleBoard");
    Pin pin = pins.get(0);
    assertEquals("Irischer Rindfleischeintopf mit Guinness", pin.getName());
  }
  @Test
  public void returnsOriginalLinkAsLinkFromPin() {
    List<Pin> pins = this.service.getPins("exampleBoard");
    Pin pin = pins.get(0);
    assertEquals(
        "https:
        pin.getOriginalLink());
  }
  @Test
  public void returnsAllPinsAcrossDifferentPages() {
    final String boardResponsePage1 =
        getResource("/fixtures/pinterest/responses/v1/board_pins_with_cursor1.json");
    final String boardResponsePage2 =
        getResource("/fixtures/pinterest/responses/v1/board_pins_with_cursor2.json");
    OAuth2RestTemplate restTemplate = getRestTemplate();
    MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
    server
        .expect(
            requestTo(
                "https:
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "bearer " + ACCESS_TOKEN))
        .andRespond(withSuccess(boardResponsePage1, MediaType.APPLICATION_JSON));
    server
        .expect(
            requestTo(
                "https:
        .andRespond(withSuccess(boardResponsePage2, MediaType.APPLICATION_JSON));
    IBoardService boardService = getBoardService(restTemplate);
    boardService.getPins("exampleBoardId");
    server.verify();
  }
}
