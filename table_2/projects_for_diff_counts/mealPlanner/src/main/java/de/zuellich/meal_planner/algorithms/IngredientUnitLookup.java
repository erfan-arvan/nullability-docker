package de.zuellich.meal_planner.algorithms;
@Service
public class IngredientUnitLookup {
   private static IngredientUnitLookup instance = null;
  private Map<String, IngredientUnit> byShorthand;
  private Map<String, IngredientUnit> byPlural;
  private Map<String, IngredientUnit> bySingular;
  private IngredientUnitLookup() {
    this.setupLookupTable();
  }
  public static IngredientUnitLookup getInstance() {
    if (IngredientUnitLookup.instance == null) {
      IngredientUnitLookup.instance = new IngredientUnitLookup();
    }
    return IngredientUnitLookup.instance;
  }
  private void setupLookupTable() {
    this.byShorthand = new HashMap<>(IngredientUnit.values().length);
    this.byPlural = new HashMap<>(IngredientUnit.values().length);
    this.bySingular = new HashMap<>(IngredientUnit.values().length);
    for (final IngredientUnit unit : IngredientUnit.values()) {
      this.byShorthand.put(unit.getShorthand(), unit);
      this.byPlural.put(unit.getPlural(), unit);
      this.bySingular.put(unit.getSingular(), unit);
    }
  }
  public IngredientUnit byShorthand(final String shorthand) {
    IngredientUnit result = this.byShorthand.get(shorthand);
    if (result == null) {
      result = IngredientUnit.NULL;
    }
    return result;
  }
  public IngredientUnit byPlural(final String plural) {
    IngredientUnit result = this.byPlural.get(plural);
    if (result == null) {
      result = IngredientUnit.NULL;
    }
    return result;
  }
  public IngredientUnit lookup(final String search) {
    IngredientUnit result = this.byShorthand(search);
    if (!result.equals(IngredientUnit.NULL)) {
      return result;
    }
    result = this.byPlural(search);
    if (!result.equals(IngredientUnit.NULL)) {
      return result;
    } else {
      return this.bySingular(search);
    }
  }
  public IngredientUnit bySingular(final String search) {
    IngredientUnit result = this.bySingular.get(search);
    if (result == null) {
      result = IngredientUnit.NULL;
    }
    return result;
  }
}
