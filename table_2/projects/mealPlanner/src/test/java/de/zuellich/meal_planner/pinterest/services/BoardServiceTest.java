package de.zuellich.meal_planner.pinterest.services;
public class BoardServiceTest extends FixtureBasedTest {
  private static final String EXAMPLE_BOARD_ID = "111111111111111111";
  private static final String ACCESS_TOKEN = "abcdef";
  private static final String EXPECTED_URL_FOR_PIN_REQUEST =
      "https:
  private OAuth2RestTemplate getRestTemplate() {
    OAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(ACCESS_TOKEN);
    DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
    clientContext.setAccessToken(accessToken);
    return new OAuth2RestTemplate(new AuthorizationCodeResourceDetails(), clientContext);
  }
  private IBoardService getBoardService(OAuth2RestTemplate restTemplate) {
    return new BoardService(restTemplate);
  }
  @Test
  public void returnsUsersBoards() {
    final String responseJSON = getResource("/fixtures/pinterest/responses/v1/me_boards.json");
    OAuth2RestTemplate restTemplate = getRestTemplate();
    MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
    server
        .expect(requestTo("https:
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "bearer " + ACCESS_TOKEN))
        .andRespond(withSuccess(responseJSON, MediaType.APPLICATION_JSON));
    IBoardService service = getBoardService(restTemplate);
    List<Board> boards = service.getBoards();
    server.verify();
    assertEquals("Two boards are returned.", 2, boards.size());
    Board board = boards.get(0);
    assertEquals("https:
    assertEquals("1", board.getId());
    assertEquals("Board Name 1", board.getName());
    board = boards.get(1);
    assertEquals("https:
    assertEquals("2", board.getId());
    assertEquals("Board2", board.getName());
  }
  @Test
  public void returnsBoardsPins() {
    final String responseJSON = getResource("/fixtures/pinterest/responses/v1/board_pins.json");
    OAuth2RestTemplate restTemplate = getRestTemplate();
    MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
    server
        .expect(requestTo(EXPECTED_URL_FOR_PIN_REQUEST))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "bearer " + ACCESS_TOKEN))
        .andRespond(withSuccess(responseJSON, MediaType.APPLICATION_JSON));
    IBoardService service = getBoardService(restTemplate);
    List<Pin> pins = service.getPins(EXAMPLE_BOARD_ID);
    server.verify();
    assertEquals("5 pins are returned", 5, pins.size());
    assertPinsNotEmptyOrNull(pins);
  }
  private void assertPinsNotEmptyOrNull(List<Pin> pins) {
    for (Pin pin : pins) {
      assertFalse("Pin's id should be set.", pin.getId().isEmpty());
      assertFalse("Pin's link should be set.", pin.getOriginalLink().isEmpty());
    }
  }
  @Test
  public void returnsABoardWithItsPins() {
    final String boardResponseJSON = getResource("/fixtures/pinterest/responses/v1/get_board.json");
    final String pinResponseJSON = getResource("/fixtures/pinterest/responses/v1/board_pins.json");
    OAuth2RestTemplate restTemplate = getRestTemplate();
    MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
    server
        .expect(
            requestTo(
                "https:
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "bearer " + ACCESS_TOKEN))
        .andRespond(withSuccess(boardResponseJSON, MediaType.APPLICATION_JSON));
    server
        .expect(requestTo(EXPECTED_URL_FOR_PIN_REQUEST))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header("Authorization", "bearer " + ACCESS_TOKEN))
        .andRespond(withSuccess(pinResponseJSON, MediaType.APPLICATION_JSON));
    IBoardService service = getBoardService(restTemplate);
    BoardListing result = service.getBoardListing(EXAMPLE_BOARD_ID);
    Board resultBoard = result.getBoard();
    assertEquals("The board name is returned.", "Board name", resultBoard.getName());
    assertEquals(
        "The board link is returned.",
        "https:
        resultBoard.getUrl());
    assertEquals("The board id is returned.", "1111111111111111111", resultBoard.getId());
    assertEquals("5 Pins are returned.", 5, result.getPins().size());
    assertPinsNotEmptyOrNull(result.getPins());
    server.verify();
  }
}
