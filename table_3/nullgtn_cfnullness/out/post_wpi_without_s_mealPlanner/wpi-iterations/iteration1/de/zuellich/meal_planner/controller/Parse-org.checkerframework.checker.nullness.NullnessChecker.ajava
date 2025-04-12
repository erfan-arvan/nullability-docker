package de.zuellich.meal_planner.controller;

import de.zuellich.meal_planner.algorithms.RecipeService;
import de.zuellich.meal_planner.datatypes.Recipe;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handles the endpoint for parsing raw recipes.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class Parse {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeService recipeService;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public Parse(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ResponseEntity<Object> parse(String url) {
        UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
        if (!urlValidator.isValid(url)) {
            return ResponseEntity.badRequest().build();
        }
        Recipe recipe = recipeService.fromURL(url);
        return ResponseEntity.ok(recipe);
    }
}
