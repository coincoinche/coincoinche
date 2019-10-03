package com.coincoinche.engine.cards;

/** Rank is an enum representing a card's rank. */
public enum Rank {
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6"),
  SEVEN("7"),
  EIGHT("8"),
  NINE("9"),
  TEN("T"),
  JACK("J"),
  QUEEN("Q"),
  KING("K"),
  ACE("A");

  private final String shortName;

  private Rank(String shortName) {
    this.shortName = shortName;
  }

  /**
   * Get a rank by using its short name.
   *
   * @param shortName is the short name of the rank.
   * @return the corresponding rank.
   * @throws IllegalArgumentException if the short name doesn't correspond to a rank.
   */
  public static Rank valueOfByShortName(String shortName) throws IllegalArgumentException {
    for (Rank rank : Rank.values()) {
      if (rank.getShortName().equalsIgnoreCase(shortName)) {
        return rank;
      }
    }
    throw new IllegalArgumentException("Rank with short name " + shortName + " doesn't exist.");
  }

  public String getShortName() {
    return shortName;
  }

  @Override
  public String toString() {
    return shortName;
  }
}
