package com.coincoinche.engine;

/** Move represents a move which can be applied on a game. */
public abstract class Move {

  /**
   * TODO nockty add documentation here.
   *
   * @param state is the state of the round where the move will be applied.
   * @throws IllegalMoveException if the move can't be applied to the state.
   */
  protected abstract void applyOnRoundState(GameState state) throws IllegalMoveException;
}
