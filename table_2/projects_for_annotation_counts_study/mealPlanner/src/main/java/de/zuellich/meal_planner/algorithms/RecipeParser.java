package de.zuellich.meal_planner.algorithms;
public interface RecipeParser {
  Recipe parse(String source);
  RecipeFormat getFormat();
}
