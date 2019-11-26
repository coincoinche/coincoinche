package com.coincoinche.engine.teams;

import com.coincoinche.engine.game.TeamInterface;
import java.util.ArrayList;
import java.util.List;

/** Team of two coinche players. */
public class Team implements TeamInterface<Player> {

  private List<Player> players;
  private int points;

  /**
   * Create a new team of two players.
   *
   * @param player1 is the first player.
   * @param player2 is the second player.
   */
  public Team(Player player1, Player player2) {
    player1.setTeam(this);
    player2.setTeam(this);
    this.players = new ArrayList<>(List.of(player1, player2));
  }

  public boolean containsPlayer(Player player) {
    return players.contains(player);
  }

  public void addPoints(int points) {
    this.points += points;
  }

  @Override
  public List<Player> getPlayers() {
    return players;
  }

  @Override
  public int getPoints() {
    return points;
  }

  @Override
  public String toString() {
    return String.format("[%s, %s]", players.get(0), players.get(1));
  }
}
