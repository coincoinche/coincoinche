package com.coincoinche.engine;

import com.coincoinche.engine.teams.Player;
import java.util.List;

/** Interface representing the state of a game phase. */
public interface GameState {
  List<Move> getLegalMoves();

  void setCurrentPlayer(Player currentPlayer);

  boolean mustChange();

  Player getCurrentPlayer();
}
