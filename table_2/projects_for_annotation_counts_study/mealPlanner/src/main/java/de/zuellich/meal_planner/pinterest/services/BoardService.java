package de.zuellich.meal_planner.pinterest.services;
@Service
public class BoardService implements IBoardService {
  public static final String USERS_BOARDS = "https:
  public static final String BOARDS_PINS =
      "https:
  public static final String BOARDS_PINS_WITH_CURSOR = BOARDS_PINS + "&cursor={cursor}";
  private static final String GET_BOARD =
      "https:
  private OAuth2RestOperations restTemplate;
  @Autowired
  public BoardService(OAuth2RestOperations restTemplate) {
    this.restTemplate = restTemplate;
  }
  @Override
  @Cacheable("boards")
  public List<Board> getBoards() {
    try {
      ResponseEntity<BoardList> boards = restTemplate.getForEntity(USERS_BOARDS, BoardList.class);
      return boards.getBody().getBoards();
    } catch (RestClientException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
  @Override
  @Cacheable("pins")
  public List<Pin> getPins(String boardId) {
    List<Pin> result = new ArrayList<>();
    String cursor = null;
    do {
      String requestURL = BOARDS_PINS;
      Map<String, String> requestParameter = new HashMap<>(3);
      requestParameter.put("id", boardId);
      if (cursor != null && !cursor.isEmpty()) {
        requestParameter.put("cursor", cursor);
        requestURL = BOARDS_PINS_WITH_CURSOR;
      }
      ResponseEntity<PinList> response =
          restTemplate.getForEntity(requestURL, PinList.class, requestParameter);
      result.addAll(response.getBody().getPins());
      if (response.getBody().getPage() != null) {
        cursor = response.getBody().getPage().getCursor();
      }
    } while (cursor != null);
    return result;
  }
  @Override
  @Cacheable("boardListing")
  public BoardListing getBoardListing(String boardId) {
    ResponseEntity<BoardRequest> board =
        restTemplate.getForEntity(GET_BOARD, BoardRequest.class, boardId);
    List<Pin> pins = getPins(boardId);
    BoardListing result = new BoardListing();
    result.setBoard(board.getBody().getBoard());
    result.setPins(pins);
    return result;
  }
}
