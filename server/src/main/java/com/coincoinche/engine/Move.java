package com.coincoinche.engine;

import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;

/** Move represents a move which can be applied on a game. */
public abstract class Move {

  /**
   * Apply the move to a coinche game, changing its round or its state depending on what the move is
   * supposed to do.
   *
   * @param game is the game where to move should be applied.
   * @return the result of the game (finished or unfinished).
   * @throws IllegalMoveException if the move can't be applied to the game.
   */
  public GameResult<Team> applyOnGame(CoincheGame game) throws IllegalMoveException {
    CoincheGameRound round = game.getCurrentRound();
    Player player = round.getCurrentPlayer();
    GameState state = round.getState();
    applyOnRoundState(state, player);
    return game.moveWasApplied();
  }

  /**
   * Apply the move to a round state. This method is called by applyOnGame on the state of the
   * game's current round. It changes the state according to what the move is supposed to do.
   *
   * @param state is the state of the round where the move will be applied.
   * @param player is the player making the move.
   * @throws IllegalMoveException if the move can't be applied to the state.
   */
  protected abstract void applyOnRoundState(GameState state, Player player)
      throws IllegalMoveException;

  public abstract String getShortName();
}
