package de.zuellich.meal_planner.algorithms.schema_org;

import de.zuellich.meal_planner.algorithms.FormatDetector;
import de.zuellich.meal_planner.algorithms.RecipeParser;
import de.zuellich.meal_planner.datatypes.RecipeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 */
@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class SchemaOrgQuirksModeFormatDetector implements FormatDetector {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeParser parser;

    @org.checkerframework.dataflow.qual.SideEffectFree
    public SchemaOrgQuirksModeFormatDetector(SchemaOrgQuirksModeParser parser) {
        this.parser = parser;
    }

    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isSupported(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgQuirksModeFormatDetector this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String source) {
        Document document = Jsoup.parse(source);
        Elements recipeElement = document.getElementsByAttributeValue("itemtype", "http://schema.org/Recipe");
        Elements ingredientsElements = document.getElementsByAttributeValue("itemprop", "ingredients");
        return !recipeElement.isEmpty() && !ingredientsElements.isEmpty();
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeFormat getFormat(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgQuirksModeFormatDetector this) {
        return RecipeFormat.SCHEMA_ORG_QUIRCKS_MODE;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RecipeParser getParserInstance(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SchemaOrgQuirksModeFormatDetector this) {
        return parser;
    }
}
