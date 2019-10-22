package com.coincoinche.engine;

import com.coincoinche.engine.teams.Player;
import java.util.List;

/** Interface representing the state of a game phase. */
public interface GameState {
  List<Move> getLegalMoves();

  /**
   * Rotate players both for the state and the round. This method must ensure consistency of the
   * current player between the state and the round, <i>i.e.</i> they must be the same player.
   *
   * @param round is the coinche game round.
   */
  void rotatePlayers(CoincheGameRound round);

  boolean mustChange();

  Player getCurrentPlayer();
}
