package com.coincoinche.events;

public class BiddingTurnStartedEvent extends TurnStartedEvent {
  public BiddingTurnStartedEvent(String[] legalMoves) {
    super(EventType.BIDDING_TURN_STARTED, legalMoves);
  }
}
