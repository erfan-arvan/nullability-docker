package de.zuellich.meal_planner.algorithms.schema_org;

import de.zuellich.meal_planner.algorithms.RecipeParser;
import de.zuellich.meal_planner.algorithms.RecipeScanner;
import de.zuellich.meal_planner.datatypes.Ingredient;
import de.zuellich.meal_planner.datatypes.Recipe;
import de.zuellich.meal_planner.datatypes.RecipeFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * A parser that accepts an HTML source that is schema.org conform.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class SchemaOrgParser implements RecipeParser {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeScanner recipeScanner;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgIngredientScanner ingredientScanner;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public SchemaOrgParser(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgRecipeScanner recipeScanner, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgIngredientScanner ingredientScanner) {
        this.recipeScanner = recipeScanner;
        this.ingredientScanner = ingredientScanner;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Recipe parse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgParser this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String source) {
        List<Ingredient> ingredientList = ingredientScanner.parse(source);
        Recipe recipe = recipeScanner.parse(source);
        recipe.setIngredients(ingredientList);
        return recipe;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeFormat getFormat(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgParser this) {
        return RecipeFormat.SCHEMA_ORG;
    }
}
