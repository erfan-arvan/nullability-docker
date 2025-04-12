package de.zuellich.meal_planner.algorithms;

import de.zuellich.meal_planner.datatypes.RecipeFormat;

/**
 * Interface that describes a type to detect a recipe format.
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public interface FormatDetector {

    /**
     * Try to detect a format.
     *
     * @return True if the format is of the given type.
     */
    @org.checkerframework.dataflow.qual.Impure
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isSupported(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String source);

    /**
     * Return the format supported by this detector.
     *
     * @return A recipe format.
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeFormat getFormat();

    /**
     * @return Returns an instance of RecipeParser that can parse the provided source.
     */
    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeParser getParserInstance();
}
