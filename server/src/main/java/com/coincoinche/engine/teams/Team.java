package com.coincoinche.engine.teams;

import com.coincoinche.engine.game.TeamInterface;
import java.util.ArrayList;
import java.util.List;

/** Team of two coinche players. */
public class Team implements TeamInterface<Player> {
  // TODO nockty: delete this if it happens to be useless
  public enum Color {
    RED,
    BLUE
  }

  private List<Player> players;
  private Color color;
  private int points;

  /**
   * Create a new team of two players.
   *
   * @param player1 is the first player.
   * @param player2 is the second player.
   * @param color is the team's color (red or blue).
   */
  public Team(Player player1, Player player2, Color color) {
    player1.setTeam(this);
    player2.setTeam(this);
    this.players = new ArrayList<>(List.of(player1, player2));
    this.color = color;
  }

  public boolean containsPlayer(Player player) {
    return players.contains(player);
  }

  public void addPoints(int points) {
    this.points += points;
  }

  // TODO nockty: check that this is useful.
  public Color getColor() {
    return color;
  }

  @Override
  public List<Player> getPlayers() {
    return players;
  }

  @Override
  public int getPoints() {
    return points;
  }
}
