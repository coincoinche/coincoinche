package com.coincoinche.engine.game;

/** GameResult represents the result of a game. */
public class GameResult<T> {
  private boolean finished;
  private boolean draw;
  // only defined when result is finished and is not a draw
  private T winnerTeam;
  private int winnerPoints;

  private GameResult(boolean finished, boolean draw, T winnerTeam, int winnerPoints) {
    this.finished = finished;
    this.draw = draw;
    this.winnerTeam = winnerTeam;
    this.winnerPoints = winnerPoints;
  }

  public static <T> GameResult<T> unfinishedResult() {
    return new GameResult<T>(false, false, null, -1);
  }

  public static <T> GameResult<T> finishedResult(T winnerTeam, int winnerPoints) {
    return new GameResult<T>(true, false, winnerTeam, winnerPoints);
  }

  public static <T> GameResult<T> drawResult() {
    return new GameResult<T>(true, true, null, -1);
  }

  public boolean isFinished() {
    return finished;
  }

  public boolean isDraw() {
    return draw;
  }

  public T getWinnerTeam() {
    return winnerTeam;
  }

  public int getWinnerPoints() {
    return winnerPoints;
  }
}
