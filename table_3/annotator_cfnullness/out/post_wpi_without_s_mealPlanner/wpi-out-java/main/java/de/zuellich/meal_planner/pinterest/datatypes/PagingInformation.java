package de.zuellich.meal_planner.pinterest.datatypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagingInformation {

  private String cursor = "";

  private String next = "";

  public String getCursor() {
    return cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }

  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }
}
