package de.zuellich.meal_planner.algorithms;
@Service
public class IngredientMatcher {
  private static final String INGREDIENT_REGEX = "(?:([\\/\\d½¼¾\\s]+)?)\\s?(\\w+)(.*)?";
  private static final Pattern INGREDIENT_PATTERN = Pattern.compile(INGREDIENT_REGEX);
  private final IngredientUnitLookup ingredientUnitLookup;
  @Autowired
  public IngredientMatcher(IngredientUnitLookup ingredientUnitLookup) {
    this.ingredientUnitLookup = ingredientUnitLookup;
  }
  public IngredientMatcherResult match(String description) {
    Matcher matcher = INGREDIENT_PATTERN.matcher(description);
    boolean isMatching = matcher.find();
    if (!isMatching) {
      return new IngredientMatcherResult(false);
    }
    String rawAmount = matchRawAmount(matcher);
    String rawUnit = matcher.group(2).trim();
    String rawName = matcher.group(3).trim();
    if (rawName.isEmpty() && !rawUnit.isEmpty()) {
      rawName = rawUnit;
      rawUnit = "";
    }
    IngredientUnit unit = tryToMatchUnit(rawUnit);
    if (unit.equals(IngredientUnit.NULL) && !rawUnit.isEmpty()) {
      rawName = rawUnit + " " + rawName;
    }
    return new IngredientMatcherResult(rawAmount, unit, rawName);
  }
  private String matchRawAmount(Matcher matcher) {
    String amountGroup = matcher.group(1);
    if (amountGroup == null) {
      return "";
    }
    return amountGroup.trim();
  }
  private IngredientUnit tryToMatchUnit(String rawUnit) {
    if (!rawUnit.isEmpty()) {
      return ingredientUnitLookup.lookup(rawUnit);
    }
    return IngredientUnit.NULL;
  }
  public static class IngredientMatcherResult {
    private final String amount;
    private final IngredientUnit unit;
    private final String name;
    private final boolean matching;
    public IngredientMatcherResult(boolean isMatching) {
      this.matching = isMatching;
      this.amount = "";
      this.unit = IngredientUnit.NULL;
      this.name = "";
    }
    public IngredientMatcherResult(String rawAmount, IngredientUnit unit, String rawName) {
      this.matching = true;
      this.amount = rawAmount;
      this.unit = unit;
      this.name = rawName;
    }
    public boolean isMatching() {
      return matching;
    }
    public String getAmount() {
      return amount;
    }
    public IngredientUnit getUnit() {
      return unit;
    }
    public String getName() {
      return name;
    }
  }
}
