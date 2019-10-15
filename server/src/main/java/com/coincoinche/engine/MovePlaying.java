package com.coincoinche.engine;

import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.teams.Player;

/**
 * MovePlaying represents a move during the playing phase of the game. It is basically a card
 * belonging to a player's hand.
 */
public class MovePlaying extends Move implements Comparable<MovePlaying> {

  private Card card;

  private MovePlaying(Card card) {
    this.card = card;
  }

  /**
   * Create a playing move corresponding to a card.
   *
   * @param card represents the card that the player plays.
   * @return the newly constructed playing move.
   */
  public static MovePlaying cardMove(Card card) {
    return new MovePlaying(card);
  }

  @Override
  public int compareTo(MovePlaying o) {
    // First, compare the card's suit, then the card's absolute rank
    int suitComparison = this.card.getSuit().compareTo(o.card.getSuit());
    if (suitComparison != 0) {
      return suitComparison;
    }
    return this.card.getRank().compareTo(o.card.getRank());
  }

  @Override
  protected void applyOnRoundState(GameState state, Player player) throws IllegalMoveException {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MovePlaying)) {
      return false;
    }
    MovePlaying otherMove = (MovePlaying) obj;
    return this.card.equals(otherMove.card);
  }

  @Override
  public String toString() {
    return card.toString();
  }
}
