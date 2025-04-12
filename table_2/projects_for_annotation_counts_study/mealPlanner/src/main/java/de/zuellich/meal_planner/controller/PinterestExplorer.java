package de.zuellich.meal_planner.controller;
@RestController
public class PinterestExplorer {
  private final IBoardService service;
  private final RecipeService recipeService;
  private final OAuth2RestOperations restTemplate;
  @Autowired
  public PinterestExplorer(
      IBoardService service, RecipeService recipeService, OAuth2RestOperations restTemplate) {
    this.service = service;
    this.recipeService = recipeService;
    this.restTemplate = restTemplate;
  }
  @RequestMapping("/connect")
  public void connect(HttpServletResponse response) {
    service.getBoards();
    if (!restTemplate.getAccessToken().isExpired()) {
      response.setStatus(HttpStatus.FOUND.value());
      response.setHeader("Location", "https:
    }
  }
  @RequestMapping("/boards")
  public ResponseEntity<List<Board>> getBoards() {
    List<Board> boards = service.getBoards();
    return ResponseEntity.ok(boards);
  }
  @RequestMapping("/board")
  public ResponseEntity<BoardListing> getBoard(@RequestParam(value = "boardId") String boardId) {
    BoardListing board = service.getBoardListing(boardId);
    return ResponseEntity.ok(board);
  }
  @RequestMapping("/recipes")
  public ResponseEntity<List<Recipe>> getRecipes() throws InterruptedException {
    List<Board> boards = service.getBoards();
    List<Recipe> recipes = new ArrayList<>();
    for (Board board : boards) {
      System.out.println("Retrieving pins for board: " + board.getName());
      List<Pin> pins = service.getPins(board.getId());
      for (Pin pin : pins) {
        try {
          System.out.println("\r:: Download pin < " + pin.getOriginalLink() + " >");
          Recipe recipe = recipeService.fromURL(pin.getOriginalLink());
          if (recipe.getSource().isEmpty()) {
            recipe.setSource(pin.getOriginalLink());
          }
          recipes.add(recipe);
          Thread.sleep(100);
        } catch (RecipeParseException e) {
          System.out.println("!! Error downloading pin: " + e.getCause().getMessage());
        }
      }
    }
    System.out.println("\nDone.");
    return ResponseEntity.ok(recipes);
  }
}
