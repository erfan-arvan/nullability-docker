package de.zuellich.meal_planner.algorithms;
public interface FormatDetector {
  boolean isSupported(String source);
  RecipeFormat getFormat();
  RecipeParser getParserInstance();
}
