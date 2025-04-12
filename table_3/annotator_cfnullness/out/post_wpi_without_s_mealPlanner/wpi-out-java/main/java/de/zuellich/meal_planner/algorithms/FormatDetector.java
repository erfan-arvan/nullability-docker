package de.zuellich.meal_planner.algorithms;

import de.zuellich.meal_planner.datatypes.RecipeFormat;

/** Interface that describes a type to detect a recipe format. */
public interface FormatDetector {

  /**
   * Try to detect a format.
   *
   * @return True if the format is of the given type.
   */
  boolean isSupported(String source);

  /**
   * Return the format supported by this detector.
   *
   * @return A recipe format.
   */
  RecipeFormat getFormat();

  /** @return Returns an instance of RecipeParser that can parse the provided source. */
  RecipeParser getParserInstance();
}
