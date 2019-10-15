package com.coincoinche.engine.cards;

import java.util.Comparator;

/** Implementation of the comparision between valued cards. */
public class ValuedCardComparator implements Comparator<ValuedCard> {

  private Suit desiredSuit;

  public ValuedCardComparator(Suit desiredSuit) {
    super();
    this.desiredSuit = desiredSuit;
  }

  /**
   * Compare two valued cards to know which one won the trick. This means that trump cards are
   * greater than other cards, and cards with the desired suit are greater than cards with other
   * suits (except the trump suit).
   *
   * @param o1 is the first valued card.
   * @param o2 is the second valued card.
   * @return a negative integer, zero, or a positive integer as the first card is lower than, equal
   *     to, or higher than the second card.
   */
  @Override
  public int compare(ValuedCard o1, ValuedCard o2) {
    // same suit: compare the value
    if (o1.getSuit().equals(o2.getSuit())) {
      return o1.getValue() - o2.getValue();
    }
    // trump is higher than other suits
    if (o1.isTrump()) {
      return 1;
    }
    if (o2.isTrump()) {
      return -1;
    }
    // desired suit is higher than other suits
    if (o1.getSuit().equals(desiredSuit)) {
      return 1;
    }
    if (o2.getSuit().equals(desiredSuit)) {
      return -1;
    }
    // this case doesn't really matter but must ensure transitivity
    return o1.getValue() - o2.getValue();
  }
}
