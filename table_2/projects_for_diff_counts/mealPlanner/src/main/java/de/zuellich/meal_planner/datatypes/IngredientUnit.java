package de.zuellich.meal_planner.datatypes;
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
  private final String singular;
  private final String plural;
  private final String shorthand;
  IngredientUnit(String singular, String plural, String shorthand) {
    this.singular = singular;
    this.plural = plural;
    this.shorthand = shorthand;
  }
  public String getSingular() {
    return singular;
  }
  public String getPlural() {
    return plural;
  }
  public String getShorthand() {
    return shorthand;
  }
}
