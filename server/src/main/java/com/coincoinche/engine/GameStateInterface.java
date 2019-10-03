package com.coincoinche.engine;

import java.util.List;

/** Interface representing the state of a game phase. */
public interface GameStateInterface {

  List<MoveInterface> getLegalMoves();
}
