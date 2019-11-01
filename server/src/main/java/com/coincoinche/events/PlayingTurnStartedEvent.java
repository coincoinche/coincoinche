package com.coincoinche.events;

public class PlayingTurnStartedEvent extends TurnStartedEvent {
  public PlayingTurnStartedEvent(String[] legalMoves, int playerIndex) {
    super(EventType.PLAYING_TURN_STARTED, legalMoves, playerIndex);
  }
}
