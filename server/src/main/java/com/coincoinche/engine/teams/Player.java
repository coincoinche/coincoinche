package com.coincoinche.engine.teams;

import com.coincoinche.engine.cards.Card;
import java.util.LinkedList;
import java.util.List;

/** Player represents a coinche player. */
public class Player {

  private List<Card> cards;
  private Team team;
  private String username;

  public Player(String username) {
    this.username = username;
    this.cards = new LinkedList<>();
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
    return team.containsPlayer(player);
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return username;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Player)) {
      return false;
    }
    Player otherPlayer = (Player) obj;
    return this.username == otherPlayer.username;
  }
}
