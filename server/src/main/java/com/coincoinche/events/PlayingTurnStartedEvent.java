package com.coincoinche.events;

public class PlayingTurnStartedEvent extends TurnStartedEvent {
  public PlayingTurnStartedEvent(String[] legalMoves) {
    super(EventType.PLAYING_TURN_STARTED, legalMoves);
  }
}
