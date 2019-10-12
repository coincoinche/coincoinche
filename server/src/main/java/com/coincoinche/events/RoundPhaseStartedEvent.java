package com.coincoinche.events;

public class RoundPhaseStartedEvent extends Event {
  private int firstPlayerIndex;

  /**
   * Event broadcasted by the server to all players when a new round phase starts for a game. (i.e.
   * bidding or main phase).
   *
   * @param firstPlayerIndex - index of the first player to play in this phase.
   */
  public RoundPhaseStartedEvent(int firstPlayerIndex) {
    super(EventType.ROUND_PHASE_STARTED);
    this.firstPlayerIndex = firstPlayerIndex;
  }

  public int getFirstPlayerIndex() {
    return firstPlayerIndex;
  }
}
