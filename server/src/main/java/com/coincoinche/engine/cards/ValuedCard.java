package com.coincoinche.engine.cards;

import java.util.Map;

/** ValuedCard represents a card with a value. */
public class ValuedCard extends Card {

  private static Map<Rank, Integer> trumpValues =
      Map.of(
          Rank.SEVEN, 0,
          Rank.EIGHT, 0,
          Rank.NINE, 14,
          Rank.TEN, 10,
          Rank.JACK, 20,
          Rank.QUEEN, 3,
          Rank.KING, 4,
          Rank.ACE, 11);

  private static Map<Rank, Integer> normalValues =
      Map.of(
          Rank.SEVEN, 0,
          Rank.EIGHT, 0,
          Rank.NINE, 0,
          Rank.TEN, 10,
          Rank.JACK, 2,
          Rank.QUEEN, 3,
          Rank.KING, 4,
          Rank.ACE, 11);

  private boolean trump;

  public ValuedCard(Suit suit, Rank rank, boolean trump) {
    super(suit, rank);
    this.trump = trump;
  }

  public static ValuedCard fromCard(Card card, boolean trump) {
    return new ValuedCard(card.getSuit(), card.getRank(), trump);
  }

  public static ValuedCard fromCard(Card card, Suit trumpSuit) {
    return new ValuedCard(card.getSuit(), card.getRank(), card.getSuit().equals(trumpSuit));
  }

  /**
   * Get the value from a card. The value depends on the trump nature of the card.
   *
   * @return the value of the card (points earned by winning the card).
   */
  public int getValue() {
    if (trump) {
      return trumpValues.get(getRank());
    }
    return normalValues.get(getRank());
  }

  public boolean isTrump() {
    return trump;
  }
}
