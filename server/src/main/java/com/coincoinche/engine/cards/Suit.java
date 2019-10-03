package com.coincoinche.engine.cards;

/** Suit is an enum representing a card's suit. */
public enum Suit {
  SPADES("s"),
  HEARTS("h"),
  CLUBS("c"),
  DIAMONDS("d");

  private final String shortName;

  private Suit(String shortName) {
    this.shortName = shortName;
  }

  /**
   * Get a suit by using its short name.
   *
   * @param shortName is the short name of the suit.
   * @return the corresponding suit.
   * @throws IllegalArgumentException if the short name doesn't correspond to a suit.
   */
  public static Suit valueOfByShortName(String shortName) throws IllegalArgumentException {
    for (Suit suit : Suit.values()) {
      if (suit.getShortName().equalsIgnoreCase(shortName)) {
        return suit;
      }
    }
    throw new IllegalArgumentException("Suit with short name " + shortName + " doesn't exist.");
  }

  public String getShortName() {
    return shortName;
  }

  @Override
  public String toString() {
    return shortName;
  }
}
