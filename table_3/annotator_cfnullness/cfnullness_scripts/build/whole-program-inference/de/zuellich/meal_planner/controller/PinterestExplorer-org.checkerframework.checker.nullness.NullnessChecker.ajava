package de.zuellich.meal_planner.controller;

import de.zuellich.meal_planner.algorithms.RecipeService;
import de.zuellich.meal_planner.datatypes.Recipe;
import de.zuellich.meal_planner.exception.RecipeParseException;
import de.zuellich.meal_planner.pinterest.datatypes.Board;
import de.zuellich.meal_planner.pinterest.datatypes.BoardListing;
import de.zuellich.meal_planner.pinterest.datatypes.Pin;
import de.zuellich.meal_planner.pinterest.services.IBoardService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller that allows to explore a users pinterestOAuth2Configuration boards and pins.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class PinterestExplorer {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IBoardService service;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeService recipeService;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull OAuth2RestOperations restTemplate;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public PinterestExplorer(IBoardService service, RecipeService recipeService, OAuth2RestOperations restTemplate) {
        this.service = service;
        this.recipeService = recipeService;
        this.restTemplate = restTemplate;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void connect(HttpServletResponse response) {
        service.getBoards();
        if (!restTemplate.getAccessToken().isExpired()) {
            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader("Location", "https://localhost:8443/resources/web/index.html");
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ResponseEntity<List<Board>> getBoards() {
        List<Board> boards = service.getBoards();
        return ResponseEntity.ok(boards);
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ResponseEntity<BoardListing> getBoard(String boardId) {
        BoardListing board = service.getBoardListing(boardId);
        return ResponseEntity.ok(board);
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ResponseEntity<List<Recipe>> getRecipes() throws InterruptedException {
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
