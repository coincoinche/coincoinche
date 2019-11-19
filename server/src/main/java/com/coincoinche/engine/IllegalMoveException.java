package com.coincoinche.engine;

/** IllegalMoveException is thrown when we try to apply an illegal move to a game state. */
public class IllegalMoveException extends Exception {

  // the exception is serializable so we define this field
  private static final long serialVersionUID = 1987207570160830533L;

  public IllegalMoveException(String errorMessage) {
    super(errorMessage);
  }

  public IllegalMoveException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
