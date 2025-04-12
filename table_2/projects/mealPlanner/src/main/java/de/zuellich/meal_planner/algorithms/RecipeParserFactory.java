package de.zuellich.meal_planner.algorithms;
@Service
public class RecipeParserFactory {
  private final Set<FormatDetector> formatDetectors;
  @Autowired
  public RecipeParserFactory(Set<FormatDetector> formatDetectors) {
    this.formatDetectors = formatDetectors;
  }
  public RecipeParser getParser(String source) {
    for (FormatDetector formatDetector : formatDetectors) {
      if (formatDetector.isSupported(source)) {
        return formatDetector.getParserInstance();
      }
    }
    return new NullParser();
  }
}
