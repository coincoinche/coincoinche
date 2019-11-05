package com.coincoinche.engine.cards;

import com.fasterxml.jackson.annotation.JsonValue;

/** Card represents a card, represented by a suit and a rank. */
public class Card implements Comparable<Card> {
  private Suit suit;
  private Rank rank;

  public Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Card)) {
      return false;
    }
    Card otherCard = (Card) obj;
    return this.suit.equals(otherCard.suit) && this.rank.equals(otherCard.rank);
  }

  public Rank getRank() {
    return rank;
  }

  public Suit getSuit() {
    return suit;
  }

  @JsonValue
  public String getShortName() {
    return rank.getShortName() + suit.getShortName();
  }

  /**
   * Get a card by using its short name.
   *
   * @param shortName is the short name of the card.
   * @return the corresponding card.
   */
  public static Card valueOfByShortName(String shortName) {
    char rankChar = shortName.charAt(0);
    char suitChar = shortName.charAt(1);
    Rank rank = Rank.valueOfByShortName(String.valueOf(rankChar));
    Suit suit = Suit.valueOfByShortName(String.valueOf(suitChar));
    return new Card(suit, rank);
  }

  @Override
  public String toString() {
    return rank.toString() + suit.toString();
  }

  @Override
  public int compareTo(Card o) {
    // First, compare the card's suit, then the card's absolute rank
    int suitComparison = suit.compareTo(o.suit);
    if (suitComparison != 0) {
      return suitComparison;
    }
    return rank.compareTo(o.rank);
  }
}
