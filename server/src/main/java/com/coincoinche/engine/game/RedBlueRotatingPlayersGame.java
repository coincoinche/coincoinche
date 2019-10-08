package com.coincoinche.engine.game;

import java.util.ArrayList;
import java.util.List;

/** Implement a game with two teams and rotating players. */
public class RedBlueRotatingPlayersGame<P> extends RotatingPlayersGame<P> {

  protected TeamInterface<P> redTeam;
  protected TeamInterface<P> blueTeam;

  /**
   * Create a new game with two teams: a red team and a blue team. The implementation provides
   * methods to rotate players turns.
   *
   * <p><strong>NB: teams must have the same size.</strong> A runtime error can occur if teams have
   * different sizes.
   *
   * @param redTeam is the first team.
   * @param blueTeam is the second team.
   */
  public RedBlueRotatingPlayersGame(TeamInterface<P> redTeam, TeamInterface<P> blueTeam) {
    super();
    this.redTeam = redTeam;
    this.blueTeam = blueTeam;
    setPlayers(generatePlayersList(redTeam, blueTeam));
  }

  private List<P> generatePlayersList(TeamInterface<P> redTeam, TeamInterface<P> blueTeam) {
    List<P> redPlayers = redTeam.getPlayers();
    List<P> bluePlayers = blueTeam.getPlayers();
    int redTeamSize = redPlayers.size();
    int blueTeamSize = bluePlayers.size();
    if (redTeamSize != blueTeamSize) {
      throw new RuntimeException("Teams don't have the same length.");
    }
    List<P> players = new ArrayList<>();
    for (int i = 0; i < redTeamSize; i++) {
      players.add(redPlayers.get(i));
      players.add(bluePlayers.get(i));
    }
    return players;
  }

  public TeamInterface<P> getBlueTeam() {
    return blueTeam;
  }

  public TeamInterface<P> getRedTeam() {
    return redTeam;
  }
}
