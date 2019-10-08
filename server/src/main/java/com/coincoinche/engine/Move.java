package com.coincoinche.engine;

/** Move represents a move which can be applied on a game. */
public abstract class Move {

  /**
   * Apply the move to a coinche game, changing its round or its state depending on what the move is
   * supposed to do.
   *
   * @param game is the game where to move should be applied.
   * @throws IllegalMoveException if the move can't be applied to the game.
   */
  public void applyOnGame(CoincheGame game) throws IllegalMoveException {
    CoincheGameRound round = game.getCurrentRound();
    GameState state = round.getState();
    applyOnRoundState(state);
    game.moveWasApplied();
  }

  /**
   * Apply the move to a round state. This method is called by applyOnGame on the state of the
   * game's current round. It changes the state according to what the move is supposed to do.
   *
   * @param state is the state of the round where the move will be applied.
   * @throws IllegalMoveException if the move can't be applied to the state.
   */
  protected abstract void applyOnRoundState(GameState state) throws IllegalMoveException;
}
