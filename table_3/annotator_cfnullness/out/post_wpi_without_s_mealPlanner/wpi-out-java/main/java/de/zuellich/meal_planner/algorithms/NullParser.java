package de.zuellich.meal_planner.algorithms;

import de.zuellich.meal_planner.datatypes.Recipe;
import de.zuellich.meal_planner.datatypes.RecipeFormat;

/** */
public class NullParser implements RecipeParser {

  @Override
  public Recipe parse(String source) {
    return new NullRecipe();
  }

  @Override
  public RecipeFormat getFormat() {
    return RecipeFormat.UNKNOWN;
  }
}
