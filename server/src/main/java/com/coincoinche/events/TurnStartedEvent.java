package com.coincoinche.events;

public class TurnStartedEvent extends Event {
  private String[] legalMoves;

  TurnStartedEvent(EventType eventType, String[] legalMoves) {
    super(eventType);
    this.legalMoves = legalMoves;
  }

  public String[] getLegalMoves() {
    return legalMoves;
  }
}
