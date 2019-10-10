package com.coincoinche.events;

import com.coincoinche.engine.RoundPhase;

public class RoundPhaseStartedEvent extends Event {
  private RoundPhase phase;
  private int firstPlayerIndex;

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
