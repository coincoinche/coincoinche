package com.coincoinche.engine.cards;

import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.Collections;
import java.util.Stack;

/** Deck represents a deck of 32 cards. */
public class Deck {
  private Stack<Card> cards;

  /** Create a new shuffled deck of 32 cards. */
  public Deck() {
    cards = new Stack<>();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        cards.add(new Card(suit, rank));
      }
    }
    Collections.shuffle(cards);
  }

  /**
   * Deal all cards in the deck to players from two teams.
   *
   * @param teamOne is the first team.
   * @param teamTwo is the second team.
   */
  public void dealCardsToTeams(Team teamOne, Team teamTwo) {
    Team[] teams = new Team[] {teamOne, teamTwo};
    for (Team team : teams) {
      for (Player player : team.getPlayers()) {
        player.clearCards();
        for (int i = 0; i < 8; i++) {
          player.addCard(cards.pop());
        }
        player.sortCards();
      }
    }
  }

  @Override
  public String toString() {
    StringBuffer prettyString = new StringBuffer();
    for (Card card : cards) {
      prettyString.append(card.toString());
    }
    return prettyString.toString();
  }
}
