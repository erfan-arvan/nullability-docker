package de.zuellich.meal_planner.algorithms;

import de.zuellich.meal_planner.datatypes.IngredientUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class implements a matching behaviour for a text description of ingredients.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class IngredientMatcher {

    /**
     * A regular expression capable of parsing most ingredient descriptions so that amount, unit and
     * name of the ingredient can be extracted. The first group also matches simple fractions.
     * Something to look out for:
     *
     * <p>(?:(...+)?) <- allows us to optionally match the first group. This allows us to match
     * ingredients without amount like "Salt & Pepper".
     */
    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String INGREDIENT_REGEX = "(?:([\\/\\d½¼¾\\s]+)?)\\s?(\\w+)(.*)?";

    private static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Pattern INGREDIENT_PATTERN = Pattern.compile(INGREDIENT_REGEX);

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnitLookup ingredientUnitLookup;

    /**
     * @param ingredientUnitLookup Used to refine the match result.
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    public IngredientMatcher(IngredientUnitLookup ingredientUnitLookup) {
        this.ingredientUnitLookup = ingredientUnitLookup;
    }

    /**
     * Try to match the given description. If the match operation was not successful only empty values
     * will be returned from the result. An empty result might be an empty string or for primitive
     * values their initial value.
     *
     * @param description The ingredient description.
     * @return The result.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientMatcherResult match(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String description) {
        Matcher matcher = INGREDIENT_PATTERN.matcher(description);
        boolean isMatching = matcher.find();
        if (!isMatching) {
            return new IngredientMatcherResult(false);
        }
        String rawAmount = matchRawAmount(matcher);
        String rawUnit = matcher.group(2).trim();
        String rawName = matcher.group(3).trim();
        // If we have a string without unit like "1 egg" we match the ingredient name as unit. Reverse
        // this.
        if (rawName.isEmpty() && !rawUnit.isEmpty()) {
            rawName = rawUnit;
            rawUnit = "";
        }
        IngredientUnit unit = tryToMatchUnit(rawUnit);
        if (unit.equals(IngredientUnit.NULL) && !rawUnit.isEmpty()) {
            rawName = rawUnit + " " + rawName;
        }
        return new IngredientMatcherResult(rawAmount, unit, rawName);
    }

    /**
     * Due to the nature of the regular expression the first group might return nothing which means we
     * need to take a bit more care here.
     *
     * @param matcher The matcher to extract the amount from.
     * @return An empty string if the amount group was not matched or the result without surrounding
     *     whitespace.
     */
    @org.checkerframework.dataflow.qual.Pure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String matchRawAmount(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Matcher matcher) {
        String amountGroup = matcher.group(1);
        if (amountGroup == null) {
            return "";
        }
        return amountGroup.trim();
    }

    /**
     * Try to match the unit already and if not move it to the name. Might look like we mix up some
     * responsibilities here and it's true, but right now it's the most efficient way that comes to my
     * mind and that yields good results.
     *
     * @param rawUnit The raw representation of the unit.
     * @return IngredientUnit.NULL if could not be found.
     */
    @org.checkerframework.dataflow.qual.Pure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnit tryToMatchUnit(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String rawUnit) {
        if (!rawUnit.isEmpty()) {
            return ingredientUnitLookup.lookup(rawUnit);
        }
        return IngredientUnit.NULL;
    }

    /**
     * Class represents the result of a match operation.
     */
    public static class IngredientMatcherResult {

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String amount;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnit unit;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String name;

        private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean matching;

        @org.checkerframework.dataflow.qual.SideEffectFree
        public IngredientMatcherResult( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isMatching) {
            this.matching = isMatching;
            this.amount = "";
            this.unit = IngredientUnit.NULL;
            this.name = "";
        }

        @org.checkerframework.dataflow.qual.SideEffectFree
        public IngredientMatcherResult(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String rawAmount, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnit unit, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String rawName) {
            this.matching = true;
            this.amount = rawAmount;
            this.unit = unit;
            this.name = rawName;
        }

        /**
         * @return True if the supplied description could be matched.
         */
        @org.checkerframework.dataflow.qual.Pure
        public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isMatching() {
            return matching;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getAmount() {
            return amount;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull IngredientUnit getUnit() {
            return unit;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getName() {
            return name;
        }
    }
}
