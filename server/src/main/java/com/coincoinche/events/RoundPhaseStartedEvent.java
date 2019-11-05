package com.coincoinche.events;

import com.coincoinche.engine.CoincheGame;

public class RoundPhaseStartedEvent extends Event {
  private CoincheGame.Phase phase;

  /**
   * Event broadcasted by the server to all players when a new round phase starts for a game. (i.e.
   * bidding or main phase).
   *
   * @param phase - the round phase name that started.
   */
  public RoundPhaseStartedEvent(CoincheGame.Phase phase) {
    super(EventType.ROUND_PHASE_STARTED);
    this.phase = phase;
  }

  public CoincheGame.Phase getPhase() {
    return phase;
  }
}
