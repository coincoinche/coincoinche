package com.coincoinche.events;

import com.coincoinche.engine.BiddingMove;
import com.coincoinche.engine.cards.Suit;

public class PlayerBadeEvent extends Event {
  private BiddingMove.Special special;
  private int value;
  private Suit suit;

  /**
   * Event sent by the client when the player did a special bidding during the bidding phase of the
   * round.
   *
   * @param special - the value of the special bidding.
   */
  public PlayerBadeEvent(BiddingMove.Special special) {
    super(EventType.PLAYER_BADE);
    this.special = special;
  }

  /**
   * Event sent by the client when the player did a contract bidding during the bidding phase of the
   * round.
   *
   * @param value - the value of the bidding.
   * @param suit - the suit of the bidding.
   */
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
