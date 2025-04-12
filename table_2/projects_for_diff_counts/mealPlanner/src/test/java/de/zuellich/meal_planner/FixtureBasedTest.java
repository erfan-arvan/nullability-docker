package de.zuellich.meal_planner;
public class FixtureBasedTest {
  public String getResource(String path) {
    try (InputStream in = FixtureBasedTest.class.getResourceAsStream(path)) {
      Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A");
      return scanner.hasNext() ? scanner.next() : "";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
