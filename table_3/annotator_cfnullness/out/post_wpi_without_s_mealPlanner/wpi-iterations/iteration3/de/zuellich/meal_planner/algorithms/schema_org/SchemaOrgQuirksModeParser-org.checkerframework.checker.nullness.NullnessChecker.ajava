package de.zuellich.meal_planner.algorithms.schema_org;

import de.zuellich.meal_planner.datatypes.RecipeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class SchemaOrgQuirksModeParser extends SchemaOrgParser {

    @org.checkerframework.dataflow.qual.SideEffectFree
    public SchemaOrgQuirksModeParser(SchemaOrgRecipeScanner recipeScanner, SchemaOrgQuirksModeIngredientScanner ingredientScanner) {
        super(recipeScanner, ingredientScanner);
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeFormat getFormat(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgQuirksModeParser this) {
        return RecipeFormat.SCHEMA_ORG_QUIRCKS_MODE;
    }
}
