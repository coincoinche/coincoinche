package com.coincoinche.engine;

/** IllegalMoveException is thrown when we try to apply an illegal move to a game state */
public class IllegalMoveException extends Exception {
  public IllegalMoveException(String errorMessage) {
    super(errorMessage);
  }
}
