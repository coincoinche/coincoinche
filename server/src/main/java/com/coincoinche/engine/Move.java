package com.coincoinche.engine;

/** Move represents a move which can be applied on a state */
public abstract class Move {

  /**
   * TODO nockty add documentation here
   *
   * @param state
   * @throws IllegalMoveException
   */
  protected abstract void applyOnRoundState(GameState state) throws IllegalMoveException;
}
