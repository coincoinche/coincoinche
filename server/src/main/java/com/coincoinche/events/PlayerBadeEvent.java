package com.coincoinche.events;

import com.coincoinche.engine.BiddingMove;
import com.coincoinche.engine.cards.Suit;

public class PlayerBadeEvent extends Event {
  private BiddingMove.Special specialMove;
  private int value;
  private Suit suit;

  public PlayerBadeEvent(BiddingMove.Special specialMove, int value, Suit suit) {
    super(EventType.PLAYER_BADE);
    this.specialMove = specialMove;
    this.value = value;
    this.suit = suit;
  }

  public BiddingMove.Special getSpecialMove() {
    return specialMove;
  }

  public int getValue() {
    return value;
  }

  public Suit getSuit() {
    return suit;
  }
}
