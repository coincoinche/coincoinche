package com.coincoinche.engine.teams;


/** Team. TODO nockty: check that this is useful. */
public class Team {
  public enum Color {
    RED,
    BLUE
  }

  private Player[] players = new Player[2];
  private Color color;

  /**
   * Create a new team of two players.
   * @param player1 is the first player.
   * @param player2 is the second player.
   * @param color is the team's color (red or blue).
   */
  public Team(Player player1, Player player2, Color color) {
    this.players[0] = player1;
    this.players[1] = player2;
    player1.setTeamMate(player2);
    player2.setTeamMate(player1);
    this.color = color;
  }

  public Player[] getPlayers() {
    return players;
  }

  // TODO nockty: check that this is useful.
  public Color getColor() {
    return color;
  }
}
