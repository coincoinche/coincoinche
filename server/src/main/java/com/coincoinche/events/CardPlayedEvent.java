package com.coincoinche.events;

public class CardPlayedEvent extends Event {
  private int playerIndex;
  private String suit;
  private String rank;

  /**
   * Event sent by the client when the player plays a card during the main phase of the round.
   *
   * @param playerIndex - the index of the player.
   * @param suit - the suit of the card played.
   * @param rank - the rank of the card played.
   */
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
