package de.zuellich.meal_planner.algorithms;
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
