package de.zuellich.meal_planner.algorithms;

import de.zuellich.meal_planner.datatypes.Ingredient;
import java.util.List;

/**
 * Scans a list of ingredients.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface IngredientScanner {

    /**
     * Get a list of ingredients from the specified source.
     *
     * @return The list of ingredients parsed or an empty list if none found.
     */
    @org.checkerframework.dataflow.qual.Impure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<Ingredient> parse(String source);
}
