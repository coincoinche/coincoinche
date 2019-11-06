package com.coincoinche.events;

public class GameFinishedEvent extends Event {
  private boolean winning;

  /**
   * This events marks the end of a game (a team has won).
   *
   * @param winning - true is the player the event is sent to has won, false otherwise.
   */
  public GameFinishedEvent(boolean winning) {
    super(EventType.GAME_FINISHED);
    this.winning = winning;
  }

  public boolean isWinning() {
    return winning;
  }
}
