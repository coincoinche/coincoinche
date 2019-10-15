package com.coincoinche.events;

import com.coincoinche.engine.MoveBidding;
import com.coincoinche.engine.cards.Suit;

public class PlayerBadeEvent extends Event {
  public enum MoveType {
    SPECIAL_BIDDING("SPECIAL_BIDDING"),
    CONTRACT_BIDDING("CONTRACT_BIDDING");

    private String type;

    MoveType(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }
  }

  private MoveBidding.Special special;
  private int value;
  private Suit suit;
  private MoveType moveType;

  /**
   * Event sent by the client when the player did a special bidding during the bidding phase of the
   * round.
   *
   * @param special - the value of the special bidding.
   */
  public PlayerBadeEvent(MoveBidding.Special special) {
    super(EventType.PLAYER_BADE);
    this.special = special;
    this.moveType = MoveType.SPECIAL_BIDDING;
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
    this.moveType = MoveType.CONTRACT_BIDDING;
  }

  public MoveBidding.Special getSpecial() {
    return special;
  }

  public int getValue() {
    return value;
  }

  public Suit getSuit() {
    return suit;
  }

  public MoveType getMoveType() {
    return moveType;
  }
}
