package com.coincoinche.events;

import com.coincoinche.engine.RoundPhase;

public class RoundPhaseStartedEvent extends Event {
  private RoundPhase phase;
  private int firstPlayerIndex;

  /**
   * Event broadcasted by the server to all players when a new round phase starts for a game. (i.e.
   * bidding or main phase).
   *
   * @param phase - the phase that just started.
   * @param firstPlayerIndex - index of the first player to play in this phase.
   */
  public RoundPhaseStartedEvent(RoundPhase phase, int firstPlayerIndex) {
    super(EventType.ROUND_PHASE_STARTED);
    this.phase = phase;
    this.firstPlayerIndex = firstPlayerIndex;
  }

  public RoundPhase getPhase() {
    return phase;
  }

  public int getFirstPlayerIndex() {
    return firstPlayerIndex;
  }
}
