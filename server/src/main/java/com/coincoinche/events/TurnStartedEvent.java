package com.coincoinche.events;

public class TurnStartedEvent extends Event {
  private String[] legalMoves;
  private int playerIndex;

  TurnStartedEvent(EventType eventType, String[] legalMoves, int playerIndex) {
    super(eventType);
    this.legalMoves = legalMoves;
    this.playerIndex = playerIndex;
  }

  public String[] getLegalMoves() {
    return legalMoves;
  }

  public int getPlayerIndex() {
    return playerIndex;
  }
}
