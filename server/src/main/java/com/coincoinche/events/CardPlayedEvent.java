package com.coincoinche.events;

public class CardPlayedEvent extends Event {
  private int playerIndex;
  private String suit;
  private String rank;

  public CardPlayedEvent(int playerIndex, String suit, String rank) {
    super(EventType.CARD_PLAYED);
    this.playerIndex = playerIndex;
    this.suit = suit;
    this.rank = rank;
  }

  public int getPlayerIndex() {
    return playerIndex;
  }

  public String getSuit() {
    return suit;
  }

  public String getRank() {
    return rank;
  }
}
