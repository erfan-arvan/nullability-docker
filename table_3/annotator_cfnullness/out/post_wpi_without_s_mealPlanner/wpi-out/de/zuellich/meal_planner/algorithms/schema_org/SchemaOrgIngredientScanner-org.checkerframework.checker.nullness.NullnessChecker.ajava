package de.zuellich.meal_planner.algorithms.schema_org;

import de.zuellich.meal_planner.algorithms.AmountParser;
import de.zuellich.meal_planner.algorithms.IngredientScanner;
import de.zuellich.meal_planner.algorithms.IngredientUnitLookup;
import de.zuellich.meal_planner.datatypes.Ingredient;
import de.zuellich.meal_planner.datatypes.IngredientUnit;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * Returns a list of ingredients based on XML.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class SchemaOrgIngredientScanner implements IngredientScanner {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnitLookup ingredientUnitLookup;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AmountParser amountParser;

    /**
     * Create a new instance.
     *
     * @param amountParser Used to parse amounts for the ingredients.
     * @param ingredientUnitLookup Used to lookup the units for an ingredient.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public SchemaOrgIngredientScanner(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AmountParser amountParser, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnitLookup ingredientUnitLookup) {
        this.amountParser = amountParser;
        this.ingredientUnitLookup = ingredientUnitLookup;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<Ingredient> parse(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgIngredientScanner this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String source) {
        Document document = Jsoup.parse(source);
        Elements ingredients = getIngredientElements(document);
        List<Ingredient> result = new ArrayList<>(ingredients.size());
        for (Element ingredient : ingredients) {
            result.add(parseIngredient(ingredient));
        }
        return result;
    }

    /**
     * Extract a list of ingredient elements from the given document. This method should be
     * overwritten by other parsers that require a different selector to be used.
     *
     * @param document The document to extract ingredients from.
     * @return An empty Elements if none found.
     */
    @org.checkerframework.dataflow.qual.Impure
    protected @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Elements getIngredientElements(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Document document) {
        return document.getElementsByAttributeValue("itemprop", "recipeIngredient");
    }

    /**
     * Method is called to extract an Ingredient instance from the elements gathered by
     * getIngredientElements.
     *
     * @param ingredient The element representing an element.
     * @return An ingredient instance.
     */
    @org.checkerframework.dataflow.qual.Impure
    protected @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Ingredient parseIngredient(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Element ingredient) {
        Elements typeElement = ingredient.select(".wprm-recipe-ingredient-name");
        Elements amountElement = ingredient.select(".wprm-recipe-ingredient-amount");
        Elements ingredientElement = ingredient.select(".wprm-recipe-ingredient-unit");
        String type = typeElement.text();
        float amount = amountParser.parseAmount(amountElement.text());
        IngredientUnit ingredientUnit = ingredientUnitLookup.lookup(ingredientElement.text());
        return new Ingredient(type, amount, ingredientUnit);
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnitLookup getIngredientUnitLookup() {
        return ingredientUnitLookup;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AmountParser getAmountParser() {
        return amountParser;
    }
}
