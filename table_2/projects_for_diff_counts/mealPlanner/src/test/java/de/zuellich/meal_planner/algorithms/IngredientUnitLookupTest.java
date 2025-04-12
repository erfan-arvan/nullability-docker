package de.zuellich.meal_planner.algorithms;
public class IngredientUnitLookupTest {
  private IngredientUnitLookup instance;
  @Before
  public void setUp() {
    instance = IngredientUnitLookup.getInstance();
  }
  @Test
  public void canLookupByUnitShorthand() {
    IngredientUnit cupUnit = instance.byShorthand(IngredientUnit.CUP.getShorthand());
    assertEquals(IngredientUnit.CUP, cupUnit);
  }
  @Test
  public void canLookupByUnitPlural() {
    IngredientUnit cloveUnit = instance.byPlural(IngredientUnit.CLOVE.getPlural());
    assertEquals(IngredientUnit.CLOVE, cloveUnit);
  }
  @Test
  public void canLookupByUnitSingular() {
    IngredientUnit teaspoonUnit = instance.bySingular(IngredientUnit.TSP.getSingular());
    assertEquals(IngredientUnit.TSP, teaspoonUnit);
  }
  @Test
  public void canLookupChained() {
    IngredientUnit unit = instance.lookup(IngredientUnit.CLOVE.getShorthand());
    assertEquals("Search the shorthand for an ingredient", IngredientUnit.CLOVE, unit);
    unit = instance.lookup(IngredientUnit.CLOVE.getPlural());
    assertEquals("Search the plural for an ingredient", IngredientUnit.CLOVE, unit);
    unit = instance.lookup(IngredientUnit.TSP.getSingular());
    assertEquals("Search the singular for an ingredient unit", IngredientUnit.TSP, unit);
  }
  @Test
  public void returnNullIngredientUnitInsteadOfNullIfUnitNotRecognized() {
    IngredientUnit actual = instance.byShorthand("notAnUnitActually");
    assertEquals("Return NULL object when unit not recognized", IngredientUnit.NULL, actual);
    actual = instance.byPlural("notAnUnitActually");
    assertEquals("Return NULL object when unit plural not recognized", IngredientUnit.NULL, actual);
  }
}
