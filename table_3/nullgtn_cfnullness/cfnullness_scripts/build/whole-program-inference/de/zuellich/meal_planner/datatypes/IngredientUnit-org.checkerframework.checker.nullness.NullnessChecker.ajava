package de.zuellich.meal_planner.datatypes;

/**
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public enum IngredientUnit {

    G("gram", "grams", "g"),
    LB("pound", "pounds", "lb"),
    NULL("", "", ""),
    CUP("cup", "cups", "cup"),
    TBSP("tablespoon", "tablespoons", "tbsp"),
    TSP("teaspoon", "teaspoons", "tsp"),
    CLOVE("clove", "cloves", "clove"),
    BUNCH("bunch", "bunches", "bunch"),
    BAG("bag", "bags", "bag"),
    CAN("can", "cans", "can");

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String singular;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String plural;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String shorthand;

    /**
     * Create a new ingredient unit.
     *
     * @param singular The singular for the unit.
     * @param plural The plural for the unit.
     * @param shorthand A short hand for the unit.
     */
    @org.checkerframework.dataflow.qual.Impure
    IngredientUnit(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String singular, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String plural, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String shorthand) {
        this.singular = singular;
        this.plural = plural;
        this.shorthand = shorthand;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getSingular() {
        return singular;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getPlural() {
        return plural;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getShorthand() {
        return shorthand;
    }
}
