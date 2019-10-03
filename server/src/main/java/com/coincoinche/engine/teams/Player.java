package com.coincoinche.engine.teams;

import com.coincoinche.engine.cards.Card;
import java.util.List;

/** Player represents a coinche player. */
public class Player {

  private List<Card> cards;
  private Player teamMate;
  private String username;

  public Player(String username) {
    this.username = username;
  }

  /**
   * Add a card to the player's hand.
   *
   * @param card is the card to add.
   */
  public void addCard(Card card) {
    cards.add(card);
  }

  public List<Card> getCards() {
    return cards;
  }

  /**
   * Check that a player is a team mate of this player.
   *
   * <p>NB: We consider this player is his own team mate.
   *
   * @param player is another player.
   * @return true if players are team mates.
   */
  public boolean isTeamMate(Player player) {
    return player.equals(teamMate) || player.equals(this);
  }

  public void setTeamMate(Player teamMate) {
    this.teamMate = teamMate;
  }
}
