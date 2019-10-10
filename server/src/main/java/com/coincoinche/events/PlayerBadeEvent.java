package com.coincoinche.events;

import com.coincoinche.engine.BiddingMove;
import com.coincoinche.engine.cards.Suit;

public class PlayerBadeEvent extends Event {
  private BiddingMove.Special special;
  private int value;
  private Suit suit;

  public PlayerBadeEvent(BiddingMove.Special special) {
    super(EventType.PLAYER_BADE);
    this.special = special;
  }

  public PlayerBadeEvent(int value, Suit suit) {
    super(EventType.PLAYER_BADE);
    this.value = value;
    this.suit = suit;
  }

  public BiddingMove.Special getSpecial() {
    return special;
  }

  public int getValue() {
    return value;
  }

  public Suit getSuit() {
    return suit;
  }
}
