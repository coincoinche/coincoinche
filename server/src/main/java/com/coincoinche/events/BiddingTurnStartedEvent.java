package com.coincoinche.events;

public class BiddingTurnStartedEvent extends TurnStartedEvent {
  public BiddingTurnStartedEvent(String[] legalMoves, int playerIndex) {
    super(EventType.BIDDING_TURN_STARTED, legalMoves, playerIndex);
  }
}
