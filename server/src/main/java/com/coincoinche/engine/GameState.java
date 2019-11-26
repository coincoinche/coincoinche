package com.coincoinche.engine;

import com.coincoinche.engine.teams.Player;
import java.util.List;

/** Interface representing the state of a game phase. */
public interface GameState {

  /**
   * Get the legal moves of the current state of the game, <i>i.e.</i> all the moves that can be
   * applied to the game according to the rules of coinche. The order of returned moves must be
   * guaranteed by the implementation.
   *
   * @return a list of moves that are legal on the current state.
   */
  public List<Move> getLegalMoves();

  /**
   * Rotate players both for the state and the round. This method must ensure consistency of the
   * current player between the state and the round, <i>i.e.</i> they must be the same player.
   *
   * @param round is the coinche game round.
   */
  public void rotatePlayers(CoincheGameRound round);

  /**
   * Boolean indicating if the state must change to a new phase of the game.
   *
   * @return true if a new phase must begin.
   */
  public boolean mustChange();

  public Player getCurrentPlayer();
}
