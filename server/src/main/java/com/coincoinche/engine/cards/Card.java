package com.coincoinche.engine.cards;

import com.fasterxml.jackson.annotation.JsonValue;

/** Card represents a card, represented by a suit and a rank. */
public class Card {
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
}
