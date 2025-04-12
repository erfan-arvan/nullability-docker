package de.zuellich.meal_planner.pinterest.datatypes;
public class BoardListing {
  private Board board = new Board();
  private List<Pin> pins = ImmutableList.of();
  public Board getBoard() {
    return this.board;
  }
  public void setBoard(final Board board) {
    this.board = board;
  }
  public List<Pin> getPins() {
    return this.pins;
  }
  public void setPins(final List<Pin> pins) {
    this.pins = pins;
  }
}
