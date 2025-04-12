package de.zuellich.meal_planner.pinterest.services;
public interface IBoardService {
  List<Board> getBoards();
  List<Pin> getPins(String boardId);
  BoardListing getBoardListing(String boardId);
}
