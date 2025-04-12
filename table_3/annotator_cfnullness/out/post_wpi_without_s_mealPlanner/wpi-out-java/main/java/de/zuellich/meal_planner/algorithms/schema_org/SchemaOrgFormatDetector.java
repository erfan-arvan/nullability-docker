package de.zuellich.meal_planner.algorithms.schema_org;

import de.zuellich.meal_planner.algorithms.FormatDetector;
import de.zuellich.meal_planner.algorithms.RecipeParser;
import de.zuellich.meal_planner.datatypes.RecipeFormat;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** */
@Service
public class SchemaOrgFormatDetector implements FormatDetector {

  private RecipeParser parser;

  @Autowired
  public SchemaOrgFormatDetector(@Qualifier("schemaOrgParser") SchemaOrgParser parser) {
    this.parser = parser;
  }

  @Override
  public boolean isSupported(String htmlSource) {
    Document document = Jsoup.parse(htmlSource);
    return canFindSchemaOrgAnnotation(document);
  }

  /**
   * Try to find the schema.org recipe annotation.
   *
   * @param document The recipes as jsoup document.
   * @return True if we found the schema.org recipe annotation.
   */
  private boolean canFindSchemaOrgAnnotation(Document document) {
    Elements recipeElement =
        document.getElementsByAttributeValue("itemtype", "http://schema.org/Recipe");
    Elements ingredientsElement =
        document.getElementsByAttributeValue("itemprop", "recipeIngredient");
    return !recipeElement.isEmpty() && !ingredientsElement.isEmpty();
  }

  @Override
  public RecipeFormat getFormat() {
    return RecipeFormat.SCHEMA_ORG;
  }

  @Override
  public RecipeParser getParserInstance() {
    return parser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SchemaOrgFormatDetector that = (SchemaOrgFormatDetector) o;
    return Objects.equals(parser, that.parser);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parser);
  }
}
