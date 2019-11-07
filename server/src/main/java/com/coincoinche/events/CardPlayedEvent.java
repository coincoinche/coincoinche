package com.coincoinche.events;

import com.fasterxml.jackson.annotation.JsonCreator;

public class CardPlayedEvent extends Event {
  private String card;
  private int playerIndex;

  /**
   * Event sent by the client when the player plays a card during the main phase of the round.
   *
   * @param card - String corresponding to the card short name.
   */
  @JsonCreator
  public CardPlayedEvent(int playerIndex, String card) {
    super(EventType.CARD_PLAYED);
    this.card = card;
    this.playerIndex = playerIndex;
  }

  public String getCard() {
    return card;
  }

  public int getPlayerIndex() {
    return playerIndex;
  }
}
