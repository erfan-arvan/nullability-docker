package de.zuellich.meal_planner.algorithms;
@Service
public class AmountParser {
  private static final List<String> UNICODE_FRACTIONS = Arrays.asList("½", "¼", "¾");
  private static final ImmutableMap<String, Float> fractionLookup;
  static {
    fractionLookup =
        ImmutableMap.<String, Float>builderWithExpectedSize(7)
            .put("1/8", 0.125f)
            .put("1/4", 0.25f)
            .put("¼", 0.25f)
            .put("1/2", 0.5f)
            .put("½", 0.5f)
            .put("3/4", 0.75f)
            .put("¾", 0.75f)
            .build();
  }
  public static final String CONTAINS_DIGITS_PATTERN = "\\d\\s?[½¼¾]";
  public float parseAmount(String raw) {
    if (raw == null || raw.isEmpty()) {
      return 0;
    }
    boolean containsUnicodeFractions = containsUnicodeFractions(raw);
    if (raw.contains("/") || containsUnicodeFractions) {
      return resolveFraction(raw);
    } else {
      return gracefullyParseFloat(raw);
    }
  }
  private float gracefullyParseFloat(String raw) {
    try {
      return Float.parseFloat(raw);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0;
    }
  }
  private boolean containsUnicodeFractions(String raw) {
    for (String unicodeSymbol : UNICODE_FRACTIONS) {
      boolean containsUnicode = raw.contains(unicodeSymbol);
      if (containsUnicode) {
        return true;
      }
    }
    return false;
  }
  private float resolveFraction(String raw) {
    boolean isMixedFraction = raw.matches(CONTAINS_DIGITS_PATTERN);
    if (!isMixedFraction) {
      return parsePrimitiveFraction(raw);
    }
    return parseMixedUnicodeFraction(raw);
  }
  private float parsePrimitiveFraction(String raw) {
    Float value = fractionLookup.get(raw);
    if (value == null) {
      return 0f;
    } else {
      return value;
    }
  }
  private float parseMixedUnicodeFraction(String raw) {
    String withoutUnicodeFraction = "0";
    String usedFraction = "";
    for (String unicodeFraction : UNICODE_FRACTIONS) {
      if (raw.contains(unicodeFraction)) {
        withoutUnicodeFraction = raw.replace(unicodeFraction, "");
        usedFraction = unicodeFraction;
        break;
      }
    }
    return Float.parseFloat(withoutUnicodeFraction) + fractionLookup.getOrDefault(usedFraction, 0f);
  }
}
