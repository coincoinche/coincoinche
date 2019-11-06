package com.coincoinche.events;

import com.coincoinche.engine.MoveBidding;
import com.coincoinche.engine.MoveType;
import com.coincoinche.engine.cards.Suit;

public class PlayerBadeEvent extends Event {
  private MoveBidding.Special special;
  private int value;
  private Suit suit;
  private MoveType moveType;
  private int playerIndex;

  public PlayerBadeEvent() {
    super(EventType.PLAYER_BADE);
  }

  /**
   * Event sent by the client when the player did a special bidding during the bidding phase of the
   * round.
   *
   * @param special - the value of the special bidding.
   */
  public PlayerBadeEvent(int playerIndex, MoveBidding.Special special) {
    super(EventType.PLAYER_BADE);
    this.special = special;
    this.moveType = MoveType.SPECIAL_BIDDING;
    this.playerIndex = playerIndex;
  }

  /**
   * Event sent by the client when the player did a contract bidding during the bidding phase of the
   * round.
   *
   * @param value - the value of the bidding.
   * @param suit - the suit of the bidding.
   */
  public PlayerBadeEvent(
      int playerIndex, MoveType moveType, int value, Suit suit, MoveBidding.Special special) {
    super(EventType.PLAYER_BADE);
    this.value = value;
    this.suit = suit;
    this.moveType = moveType;
    this.special = special;
    this.playerIndex = playerIndex;
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

  public int getPlayerIndex() {
    return playerIndex;
  }
}
