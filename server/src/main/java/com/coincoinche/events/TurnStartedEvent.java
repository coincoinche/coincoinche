package com.coincoinche.events;

public class TurnStartedEvent extends Event {
  private String[] legalMoves;

  public TurnStartedEvent(String[] legalMoves) {
    super(EventType.TURN_STARTED);
    this.legalMoves = legalMoves;
  }

  public String[] getLegalMoves() {
    return legalMoves;
  }
}
