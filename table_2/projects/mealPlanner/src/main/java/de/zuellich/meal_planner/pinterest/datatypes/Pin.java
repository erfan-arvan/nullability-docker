package de.zuellich.meal_planner.pinterest.datatypes;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pin {
  private String id = "";
  @JsonProperty("original_link")
  private String originalLink = "";
  private String note = "";
  private String name = "";
  public Pin() {}
  @JsonCreator
  public Pin(@JsonProperty("metadata") Map<String, Object> metadata) {
    if (metadata == null || !metadata.containsKey("article")) {
      return;
    }
    Map<String, Object> article = (Map<String, Object>) metadata.getOrDefault("article", "");
    this.name = String.valueOf(article.get("name"));
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getOriginalLink() {
    return originalLink;
  }
  public void setOriginalLink(String originalLink) {
    this.originalLink = originalLink;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pin pin = (Pin) o;
    return Objects.equals(getId(), pin.getId())
        && Objects.equals(getOriginalLink(), pin.getOriginalLink());
  }
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getOriginalLink());
  }
  @Override
  public String toString() {
    return "Pin{" + "id='" + id + '\'' + ", originalLink='" + originalLink + '\'' + '}';
  }
  public String getNote() {
    return note;
  }
  public void setNote(String note) {
    this.note = note;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
}
