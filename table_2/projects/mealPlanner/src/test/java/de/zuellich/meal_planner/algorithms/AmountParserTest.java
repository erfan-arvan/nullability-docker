package de.zuellich.meal_planner.algorithms;
public class AmountParserTest {
  private AmountParser instance;
  @Before
  public void setUp() {
    instance = new AmountParser();
  }
  @Test
  public void canParseNormalNumber() {
    assertEquals(1f, instance.parseAmount("1"), 0);
    assertEquals(2f, instance.parseAmount("2"), 0);
  }
  @Test
  public void canHandleNegativeNumbers() {
    assertEquals(-1f, instance.parseAmount("-1"), 0);
    assertEquals(-10f, instance.parseAmount("-10"), 0);
  }
  @Test
  public void canHandleBasicFractions() {
    assertEquals(0.125f, instance.parseAmount("1/8"), 0);
    assertEquals(0.25f, instance.parseAmount("1/4"), 0);
    assertEquals(0.5, instance.parseAmount("1/2"), 0);
    assertEquals(0.75f, instance.parseAmount("3/4"), 0);
  }
  @Test
  public void returnsZeroIfEmptyStringOrNull() {
    assertEquals(0f, instance.parseAmount(null), 0);
    assertEquals(0f, instance.parseAmount(""), 0);
  }
  @Test
  public void canHandleUnicodeVulgarFractions() {
    assertEquals(0.5f, instance.parseAmount("½"), 0);
    assertEquals(0.25f, instance.parseAmount("¼"), 0);
    assertEquals(0.75f, instance.parseAmount("¾"), 0);
  }
  @Test
  public void canHandleMixedUnicodeFractions() {
    assertEquals(3.5f, instance.parseAmount("3 ½"), 0);
  }
  @Test
  public void gracefullyHandleNumberFormatException() {
    assertEquals(0f, instance.parseAmount("Not a number."), 0);
  }
}
