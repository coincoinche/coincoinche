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

  @JsonValue
  public String getShortName() {
    return rank.getShortName() + suit.getShortName();
  }

  @Override
  public String toString() {
    return rank.toString() + suit.toString();
  }
}
