package com.coincoinche.engine.game;

/** GameResult represents the result of a game. */
public class GameResult<T> {
  private boolean finished;
  // only defined when result is finished
  private T winnerTeam;
  private int winnerPoints;

  private GameResult(boolean finished, T winnerTeam, int winnerPoints) {
    this.finished = finished;
    this.winnerTeam = winnerTeam;
    this.winnerPoints = winnerPoints;
  }

  public static <T> GameResult<T> UnfinishedResult() {
    return new GameResult<T>(false, null, -1);
  }

  public static <T> GameResult<T> FinishedResult(T winnerTeam, int winnerPoints) {
    return new GameResult<T>(true, winnerTeam, winnerPoints);
  }

  public boolean isFinished() {
    return finished;
  }

  public T getWinnerTeam() {
    return winnerTeam;
  }

  public int getWinnerPoints() {
    return winnerPoints;
  }
}
