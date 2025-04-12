package de.zuellich.meal_planner.algorithms.schema_org;

import de.zuellich.meal_planner.algorithms.AmountParser;
import de.zuellich.meal_planner.algorithms.IngredientMatcher;
import de.zuellich.meal_planner.algorithms.IngredientUnitLookup;
import de.zuellich.meal_planner.datatypes.Ingredient;
import de.zuellich.meal_planner.datatypes.IngredientUnit;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * This IngredientScanner implements a specific quirk found on some websites. Instead of
 * itemprop="recipeIngredient" they use itemprop="ingredients".
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class SchemaOrgQuirksModeIngredientScanner extends SchemaOrgIngredientScanner {

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientMatcher ingredientMatcher;

    /**
     * Create a new instance.
     *
     * @param amountParser Used to parse amounts for the ingredients.
     * @param ingredientUnitLookup Used to lookup the units for an ingredient.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public SchemaOrgQuirksModeIngredientScanner(AmountParser amountParser, IngredientUnitLookup ingredientUnitLookup, IngredientMatcher ingredientMatcher) {
        super(amountParser, ingredientUnitLookup);
        this.ingredientMatcher = ingredientMatcher;
    }

    @org.checkerframework.dataflow.qual.Impure
    protected @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Elements getIngredientElements(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgQuirksModeIngredientScanner this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Document document) {
        return document.getElementsByAttributeValue("itemprop", "ingredients");
    }

    @org.checkerframework.dataflow.qual.Impure
    protected @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Ingredient parseIngredient(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgQuirksModeIngredientScanner this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Element ingredient) {
        String ingredientDescription = ingredient.text();
        IngredientMatcher.IngredientMatcherResult result = ingredientMatcher.match(ingredientDescription);
        float amount = 0;
        IngredientUnit unit = IngredientUnit.NULL;
        String name = "";
        if (result.isMatching()) {
            amount = getAmountParser().parseAmount(result.getAmount());
            unit = result.getUnit();
            name = result.getName();
        }
        return new Ingredient(name, amount, unit);
    }
}
